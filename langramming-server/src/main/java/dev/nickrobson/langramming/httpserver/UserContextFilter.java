package dev.nickrobson.langramming.httpserver;

import dev.nickrobson.langramming.model.User;
import dev.nickrobson.langramming.service.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Singleton
public class UserContextFilter implements Filter {

    private static final String USER_CONTEXT_KEY = UserContextFilter.class.getName() + "__USER";
    private static final ThreadLocal<User> userLocal = new ThreadLocal<>();

    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(userLocal.get());
    }

    public static void setLoggedInUser(HttpSession httpSession, User user) {
        userLocal.set(user);

        httpSession.setAttribute(USER_CONTEXT_KEY, user.getId());
    }

    @Inject
    private UserService userService;

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
                HttpSession httpSession = httpServletRequest.getSession();

                Object userId = httpSession.getAttribute(USER_CONTEXT_KEY);
                httpSession.setMaxInactiveInterval(36 * 60 * 60); // 36 hours

                if (userId instanceof Long) {
                    Optional<User> user = userService.getUser((Long) userId);
                    user.ifPresentOrElse(userLocal::set, userLocal::remove);
                } else {
                    userLocal.remove();
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            userLocal.remove();
        }
    }
}
