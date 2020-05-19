package com.github.langramming.util;

import com.github.langramming.rest.response.ErrorDTO;
import com.github.langramming.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;

@Singleton
public class ResponseHelper {

    private final UserService.UserProvider userProvider;

    @Inject
    public ResponseHelper(UserService.UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public boolean isLoggedIn() {
        return userProvider.get().isPresent();
    }

    public ResponseEntity<ErrorDTO> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.of("Bad request"));
    }

    public ResponseEntity<ErrorDTO> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDTO.of("Unauthorized"));
    }

    public ResponseEntity<ErrorDTO> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.of("Forbidden"));
    }

    public ResponseEntity<ErrorDTO> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDTO.of("Not found"));
    }

    public <T> ResponseEntity<T> redirect(String path) {
        return redirect(URI.create(path));
    }

    public <T> ResponseEntity<T> redirect(URI uri) {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(uri)
                .build();
    }

    public <T> ResponseEntity<T> redirectToError(LangrammingClientError clientError) {
        return redirect("/?error=" + clientError.getCode());
    }

    public <T> ResponseEntity<T> ok(T entity) {
        return ResponseEntity.ok(entity);
    }

    public <T> ResponseEntity<T> ok(T entity, MediaType mediaType) {
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(entity);
    }
}
