package com.github.langramming.httpserver;

import com.github.langramming.model.User;
import com.github.langramming.service.UserService;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class UserContextFilter extends HttpServerProbe.Adapter {

    private static final String USER_CONTEXT_KEY = UserContextFilter.class.getName() + "__USER";
    private static final ThreadLocal<User> userLocal = new ThreadLocal<>();

    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(userLocal.get());
    }

    public static void setLoggedInUser(Request request, User user) {
        userLocal.set(user);
        request.getSession().setAttribute(USER_CONTEXT_KEY, user.getId());
    }

    @Inject
    private UserService userService;

    @Override
    public void onBeforeServiceEvent(HttpServerFilter filter, Connection connection, Request request, HttpHandler httpHandler) {
        Object userId = request.getSession().getAttribute(USER_CONTEXT_KEY);
        request.getSession().setSessionTimeout(TimeUnit.HOURS.toMillis(36));
        if (userId instanceof Long) {
            Optional<User> user = userService.getUser((Long) userId);
            user.ifPresentOrElse(userLocal::set, userLocal::remove);
        } else {
            userLocal.remove();
        }

        request.addAfterServiceListener(r -> userLocal.remove());
    }
}
