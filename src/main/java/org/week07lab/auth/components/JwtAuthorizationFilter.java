package org.week07lab.auth.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.week07lab.account.domain.AccountService;

import java.io.IOException;

/**
 * Valida el JWT del header Authorization y puebla el SecurityContext si es valido.
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AccountService userDetailsService;

    public JwtAuthorizationFilter(JwtService jwtService, AccountService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                    } catch (UsernameNotFoundException ignored) {
                        // Token valido de un usuario que ya no existe: la peticion sigue como anonima.
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
