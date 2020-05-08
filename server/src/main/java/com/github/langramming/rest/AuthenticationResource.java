package com.github.langramming.rest;

import com.github.langramming.httpserver.UserContextFilter;
import com.github.langramming.model.User;
import com.github.langramming.rest.response.ErrorDTO;
import com.github.langramming.service.UserService;
import com.github.langramming.util.EnvironmentVariables;
import com.github.langramming.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationResource {

    private static Set<Long> TEMPORARY_allowedTelegramUsers = new HashSet<>(
            Arrays.asList(112972102L)
    );

    private final UserService userService;
    private final ResponseHelper responseHelper;

    @Inject
    public AuthenticationResource(UserService userService, ResponseHelper responseHelper) {
        this.userService = userService;
        this.responseHelper = responseHelper;
    }

    @GetMapping("/login")
    public ResponseEntity<ErrorDTO> login(HttpServletRequest httpServletRequest) {
        if (!verifyTelegramLogin(httpServletRequest.getParameterMap())) {
            return responseHelper.badRequest();
        }

        long telegramId;
        String name;
        try {
            telegramId = Long.parseLong(httpServletRequest.getParameter("id"));
            name = httpServletRequest.getParameter("first_name");
        } catch (NumberFormatException ex) {
            return responseHelper.badRequest();
        }

        if (!TEMPORARY_allowedTelegramUsers.contains(telegramId)) {
            return responseHelper.forbidden();
        }

        Optional<User> userOpt = userService.getUserByTelegramId(telegramId);

        userOpt.ifPresent(user -> {
            user.setName(name);
            userService.updateUser(user);
        });

        User user = userOpt.orElseGet(() ->
            userService.createUser(telegramId, name));

        UserContextFilter.setLoggedInUser(httpServletRequest.getSession(), user);

        return responseHelper.redirect("/");
    }

    private boolean verifyTelegramLogin(Map<String, String[]> parameterMap) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secret = digest.digest(EnvironmentVariables.TELEGRAM_API_TOKEN.getBytes());

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(new SecretKeySpec(secret, "HmacSHA256"));

            Map<String, String> queryParams = parameterMap
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue().length > 0)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue()[0]
                    ));

            String hash = queryParams.remove("hash");
            if (hash == null) {
                return false;
            }

            String dataCheckString = queryParams.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("\n"));

            byte[] hashedBytes = hmacSha256.doFinal(dataCheckString.getBytes());
            StringBuilder hashedString = new StringBuilder();
            for (byte b : hashedBytes) {
                hashedString.append(String.format("%02x", b));
            }

            return hash.equals(hashedString.toString());
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
