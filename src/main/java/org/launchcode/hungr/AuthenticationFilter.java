package org.launchcode.hungr;

import org.launchcode.hungr.controllers.AuthenticationController;
import org.launchcode.hungr.data.UserRepository;
import org.launchcode.hungr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> allowlist = Arrays.asList("/login", "/signup", "/logout","/css", "/images", "/fontawesome", "/script", "/webfonts");

    private static boolean isAllowlisted(String path) {
        for (String pathRoot : allowlist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        // The user is logged in
        if (user != null) {
            // add user attribute to request so we only need one database lookup for the user per request
            request.setAttribute("user", user);
            return true;
        }

        // Don't require sign-in for whitelisted pages
        if (isAllowlisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");
        return false;
    }

}

