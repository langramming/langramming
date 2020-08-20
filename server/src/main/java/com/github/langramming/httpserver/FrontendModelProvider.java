package com.github.langramming.httpserver;

import com.github.langramming.model.User;
import com.github.langramming.service.SpotifyUserService;
import com.github.langramming.service.UserService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FrontendModelProvider {
    private final UserService.UserProvider userProvider;
    private final SpotifyUserService spotifyUserService;

    @Inject
    public FrontendModelProvider(
        UserService.UserProvider userProvider,
        SpotifyUserService spotifyUserService
    ) {
        this.userProvider = userProvider;
        this.spotifyUserService = spotifyUserService;
    }

    public FrontendModel getFrontendModel() {
        return FrontendModel.builder().user(userProvider.get().map(this::toUserModel)).build();
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
