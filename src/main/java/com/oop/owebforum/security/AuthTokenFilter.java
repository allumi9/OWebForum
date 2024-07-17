package com.oop.owebforum.security;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.services.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private AppUserService appUserService;

    @Autowired
    public AuthTokenFilter(AppUserService appUserService, JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        this.appUserService = appUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromCookies(request);
            if (jwt != null && jwtUtils.validateJwt(jwt)) {
                String username = jwtUtils.getUsernameFromJwt(jwt);

                AppUser appUser = appUserService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(appUser, null, appUser.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
