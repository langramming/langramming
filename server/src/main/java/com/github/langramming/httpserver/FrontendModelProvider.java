package com.github.langramming.httpserver;

import com.github.langramming.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FrontendModelProvider {

    private final UserService.UserProvider userProvider;

    @Inject
    public FrontendModelProvider(UserService.UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public FrontendModel getFrontendModel() {
        return FrontendModel.builder()
                .user(userProvider.get().orElse(null))
                .build();
    }
}
