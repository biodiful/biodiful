package org.biodiful.web.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.web.filter.OncePerRequestFilter;

public class AngularRouteFilter extends OncePerRequestFilter {

    // add the values you want to redirect for
    private static final Pattern PATTERN = Pattern.compile(
        "^/((api|swagger-ui|management|swagger-resources)/|favicon\\.ico|v2/api-docs).*"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (isServerRoute(request)) {
            filterChain.doFilter(request, response);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        }
    }

    protected static boolean isServerRoute(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) {
            String uri = request.getRequestURI();
            return PATTERN.matcher(uri).matches();
        }
        return true;
    }
}
