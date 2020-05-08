package com.github.langramming.util;

import com.github.langramming.rest.response.ErrorDTO;
import com.github.langramming.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;

@Singleton
public class ResponseHelper {

    @Inject
    private UserService.UserProvider userProvider;

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
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(path))
                .build();
    }

    public <T> ResponseEntity<T> ok(T entity) {
        return ResponseEntity.ok(entity);
    }
}
