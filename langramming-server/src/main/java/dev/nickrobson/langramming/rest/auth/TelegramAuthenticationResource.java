package dev.nickrobson.langramming.rest.auth;

import dev.nickrobson.langramming.configuration.LangrammingTelegramConfiguration;
import dev.nickrobson.langramming.httpserver.UserContextFilter;
import dev.nickrobson.langramming.model.User;
import dev.nickrobson.langramming.rest.response.ErrorDTO;
import dev.nickrobson.langramming.service.BaseUrlService;
import dev.nickrobson.langramming.service.UserService;
import dev.nickrobson.langramming.util.ResponseHelper;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth/telegram")
public class TelegramAuthenticationResource {
    private static final Set<Long> TEMPORARY_allowedTelegramUsers = new HashSet<>(
        Collections.singletonList(112972102L)
    );

    private final BaseUrlService baseUrlService;
    private final UserService userService;
    private final ResponseHelper responseHelper;
    private final LangrammingTelegramConfiguration telegramConfiguration;

    @Inject
    public TelegramAuthenticationResource(
        BaseUrlService baseUrlService,
        UserService userService,
        ResponseHelper responseHelper,
        LangrammingTelegramConfiguration telegramConfiguration
    ) {
        this.baseUrlService = baseUrlService;
        this.userService = userService;
        this.responseHelper = responseHelper;
        this.telegramConfiguration = telegramConfiguration;
    }

    @GetMapping("/login")
    public ResponseEntity<ErrorDTO> login(
        HttpServletRequest httpServletRequest,
        @RequestParam Map<String, String> queryParams
    ) {
        if (!verifyTelegramLogin(queryParams)) {
            log.warn("Returning bad request due to failed Telegram verification");
            return responseHelper.badRequest();
        }

        long telegramId;
        String name;
        try {
            telegramId = Long.parseLong(queryParams.get("id"));
            name = queryParams.get("first_name");
        } catch (Exception ex) {
            log.error("Returning bad request due to failure to extract id/first_name", ex);
            return responseHelper.badRequest();
        }

        if (!TEMPORARY_allowedTelegramUsers.contains(telegramId)) {
            log.warn("Returning forbidden due to non-allowlisted user attempting to log in");
            return responseHelper.forbidden();
        }

        Optional<User> userOpt = userService.getUserByTelegramId(telegramId);

        userOpt.ifPresent(
            user -> {
                user.setName(name);
                userService.updateUser(user);
            }
        );

        User user = userOpt.orElseGet(() -> userService.createUser(telegramId, name));

        UserContextFilter.setLoggedInUser(httpServletRequest.getSession(), user);

        log.info("Successfully signed in as TG user with ID {}", user.getTelegramId());
        return responseHelper.redirect(baseUrlService.getBaseUrl());
    }

    private boolean verifyTelegramLogin(Map<String, String> parameterMap) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secret = digest.digest(telegramConfiguration.getToken().getBytes());

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(new SecretKeySpec(secret, "HmacSHA256"));

            Map<String, String> queryParams = parameterMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            String hash = queryParams.remove("hash");
            if (hash == null) {
                log.warn("No hash parameter in Telegram login attempt");
                return false;
            }

            String dataCheckString = queryParams
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

            byte[] hashedBytes = hmacSha256.doFinal(dataCheckString.getBytes());
            String hashedBytesString = Hex.encodeHexString(hashedBytes);

            return hash.equals(hashedBytesString);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Failed to verify Telegram login", ex);
            return false;
        }
    }
}
