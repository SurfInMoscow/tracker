package ru.vorobyev.tracker.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserWithDetails authorizedUser = getPrincipal();
        if (authorizedUser != null) {
            request.setAttribute("usr_id", authorizedUser.getId());
        }

        return true;
    }

    private UserWithDetails getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();
        return (principal instanceof UserWithDetails) ? (UserWithDetails) principal : null;
    }
}
