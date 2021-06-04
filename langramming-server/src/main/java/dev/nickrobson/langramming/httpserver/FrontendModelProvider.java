package dev.nickrobson.langramming.httpserver;

import dev.nickrobson.langramming.client.telegram.TelegramBotClient;
import dev.nickrobson.langramming.manager.BaseUrlManager;
import dev.nickrobson.langramming.manager.SpotifyUserManager;
import dev.nickrobson.langramming.manager.UserManager;
import dev.nickrobson.langramming.model.User;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FrontendModelProvider {

    private final BaseUrlManager baseUrlManager;
    private final UserManager.UserProvider userProvider;
    private final SpotifyUserManager spotifyUserManager;
    private final TelegramBotClient telegramBotClient;

    @Inject
    public FrontendModelProvider(
        BaseUrlManager baseUrlManager,
        UserManager.UserProvider userProvider,
        SpotifyUserManager spotifyUserManager,
        TelegramBotClient telegramBotClient
    ) {
        this.baseUrlManager = baseUrlManager;
        this.userProvider = userProvider;
        this.spotifyUserManager = spotifyUserManager;
        this.telegramBotClient = telegramBotClient;
    }

    public FrontendModel getFrontendModel() {
        return FrontendModel
            .builder()
            .baseUrl(baseUrlManager.getBaseUrl())
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
            .isSpotifyAuthed(spotifyUserManager.getCurrentSpotifyUser().isPresent())
            .build();
    }
}
