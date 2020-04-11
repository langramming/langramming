package com.github.langramming.rest;

import com.github.langramming.util.EnvironmentVariables;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/auth")
public class AuthenticationResource {

    @GET
    @Path("/login")
    public Response login(@Context UriInfo uriInfo) {
        if (verifyTelegramLogin(uriInfo)) {
            // TODO: handle login
            System.out.println("Logged in successfully!");
        }

        return Response.temporaryRedirect(
                UriBuilder.fromPath("/").build()
        ).build();
    }

    private boolean verifyTelegramLogin(UriInfo uriInfo) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secret = digest.digest(EnvironmentVariables.TELEGRAM_API_TOKEN.getBytes());

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(new SecretKeySpec(secret, "HmacSHA256"));

            Map<String, String> queryParams = uriInfo.getQueryParameters()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().get(0)
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
