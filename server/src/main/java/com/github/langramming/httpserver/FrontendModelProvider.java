package com.github.langramming.httpserver;

import com.github.langramming.client.telegram.TelegramBotClient;
import com.github.langramming.configuration.LangrammingServerConfiguration;
import com.github.langramming.model.User;
import com.github.langramming.service.SpotifyUserService;
import com.github.langramming.service.UserService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FrontendModelProvider {
    private final LangrammingServerConfiguration langrammingServerConfiguration;
    private final UserService.UserProvider userProvider;
    private final SpotifyUserService spotifyUserService;
    private final TelegramBotClient telegramBotClient;

    @Inject
    public FrontendModelProvider(
        LangrammingServerConfiguration langrammingServerConfiguration,
        UserService.UserProvider userProvider,
        SpotifyUserService spotifyUserService,
        TelegramBotClient telegramBotClient
    ) {
        this.langrammingServerConfiguration = langrammingServerConfiguration;
        this.userProvider = userProvider;
        this.spotifyUserService = spotifyUserService;
        this.telegramBotClient = telegramBotClient;
    }

    public FrontendModel getFrontendModel() {
        return FrontendModel
            .builder()
            .baseUrl(langrammingServerConfiguration.getUrl())
            .user(userProvider.get().map(this::toUserModel))
            .telegram(
                FrontendModel
                    .FrontendTelegramModel.builder()
                    .username(telegramBotClient.getTelegramBotInfo().user().username())
                    .build()
            )
            .build();
    }

    private FrontendModel.FrontendUserModel toUserModel(User user) {
        return FrontendModel
            .FrontendUserModel.builder()
            .id(user.getId())
            .name(user.getName())
            .isSpotifyAuthed(spotifyUserService.getCurrentSpotifyUser().isPresent())
            .build();
    }
}
