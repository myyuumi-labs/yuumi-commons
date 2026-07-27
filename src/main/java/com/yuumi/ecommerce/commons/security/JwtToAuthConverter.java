package com.yuumi.ecommerce.commons.security;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtToAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        // We'll collect all authorities (permissions + scopes) here
        Set<String> scopes = new LinkedHashSet<>();

        // 1. Extract permissions claim (Auth0 adds this when RBAC is enabled)
        Object perms = jwt.getClaims().get("permissions");
        if (perms instanceof Collection<?>) {
            for (Object p : (Collection<?>) perms) {
                // Convert each permission to a string and add to our set
                scopes.add(String.valueOf(p));
            }
        }

        // 2. Extract OAuth2 "scope" claim (space-delimited)
        String scope = jwt.getClaimAsString("scope");
        if (scope != null && !scope.isBlank()) {
            // Split the space-delimited scopes and add each one
            scopes.addAll(Arrays.asList(scope.split(" ")));
        }

        // 3. Convert all collected scopes/permissions into GrantedAuthority objects
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String s : scopes) {
            // Spring convention: prefix each authority with "SCOPE_"
            authorities.add(() -> "SCOPE_" + s);
        }

        // 4. Return a JwtAuthenticationToken with the original JWT and the derived authorities
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
