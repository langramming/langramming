package com.github.langramming.util;

import java.util.Optional;

public class EnvironmentVariables {

    public static final String WEBSITE_URL = getVariable("WEBSITE_URL");
    public static final Optional<String> FRONTEND_PORT = maybeGetVariable("FRONTEND_PORT");
    public static final Optional<String> SERVER_PORT = maybeGetVariable("SERVER_PORT");

    public static final String TELEGRAM_API_TOKEN = getVariable("TELEGRAM_API_TOKEN");
    public static final String SPOTIFY_API_CLIENT_ID = getVariable("SPOTIFY_API_CLIENT_ID");
    public static final String SPOTIFY_API_CLIENT_SECRET = getVariable("SPOTIFY_API_CLIENT_SECRET");

    private static String getVariable(String environmentVariable) {
        Optional<String> maybeValue = maybeGetVariable(environmentVariable);
        if (maybeValue.isEmpty()) {
            throw new IllegalStateException(
                    String.format(
                            "Expected environment variable %s to be set but it was not",
                            environmentVariable
                    )
            );
        }
        return maybeValue.get();
    }

    private static Optional<String> maybeGetVariable(String environmentVariable) {
        return Optional.ofNullable(System.getenv(environmentVariable));
    }

}
