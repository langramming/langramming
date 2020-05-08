package com.github.langramming.util;

import com.github.langramming.rest.response.ErrorDTO;
import com.github.langramming.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Singleton
public class ResponseHelper {

    @Inject
    private UserService.UserProvider userProvider;

    public boolean isLoggedIn() {
        return userProvider.get().isPresent();
    }

    public Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO.of("Bad request"))
                .build();
    }

    public Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ErrorDTO.of("Unauthorized"))
                .build();
    }

    public Response forbidden() {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(ErrorDTO.of("Forbidden"))
                .build();
    }

    public Response notFound() {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ErrorDTO.of("Not found"))
                .build();
    }

    public Response redirect(String path) {
        return Response.temporaryRedirect(
                UriBuilder.fromPath(path).build()
        ).build();
    }
}
