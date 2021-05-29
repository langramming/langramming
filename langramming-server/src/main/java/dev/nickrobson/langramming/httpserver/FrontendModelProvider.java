package dev.nickrobson.langramming.httpserver;

import dev.nickrobson.langramming.client.telegram.TelegramBotClient;
import dev.nickrobson.langramming.model.User;
import dev.nickrobson.langramming.service.BaseUrlService;
import dev.nickrobson.langramming.service.SpotifyUserService;
import dev.nickrobson.langramming.service.UserService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FrontendModelProvider {

    private final BaseUrlService baseUrlService;
    private final UserService.UserProvider userProvider;
    private final SpotifyUserService spotifyUserService;
    private final TelegramBotClient telegramBotClient;

    @Inject
    public FrontendModelProvider(
        BaseUrlService baseUrlService,
        UserService.UserProvider userProvider,
        SpotifyUserService spotifyUserService,
        TelegramBotClient telegramBotClient
    ) {
        this.baseUrlService = baseUrlService;
        this.userProvider = userProvider;
        this.spotifyUserService = spotifyUserService;
        this.telegramBotClient = telegramBotClient;
    }

    public FrontendModel getFrontendModel() {
        return FrontendModel
            .builder()
            .baseUrl(baseUrlService.getBaseUrl())
            .user(userProvider.get().map(this::toUserModel))
            .telegram(
                FrontendModel.FrontendTelegramModel
                    .builder()
                    .username(telegramBotClient.getTelegramBotInfo().user().username())
                    .build()
            )
            .build();
    }

    private FrontendModel.FrontendUserModel toUserModel(User user) {
        return FrontendModel.FrontendUserModel
            .builder()
            .id(user.getId())
            .name(user.getName())
            .isSpotifyAuthed(spotifyUserService.getCurrentSpotifyUser().isPresent())
            .build();
    }
}
