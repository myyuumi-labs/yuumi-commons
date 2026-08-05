package com.yuumi.ecommerce.commons.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Decodes a Bearer JWT payload into the security context without verifying the signature.
 * <p>
 * Intended for private downstream services behind the gateway. The gateway is responsible
 * for authenticating the client and issuing the internal token.
 * <p>
 * Not a Spring {@code @Component} — each service registers a {@code @Bean} explicitly.
 */
public class UnsafeJwtAuthenticationFilter extends OncePerRequestFilter {

  private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

  private final ObjectMapper objectMapper;

  public UnsafeJwtAuthenticationFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7).trim();
      if (!token.isEmpty()) {
        try {
          Map<String, Object> claims = parseClaims(token);
          String subject = claims.get("sub") != null ? String.valueOf(claims.get("sub")) : "unknown";
          var authentication = new UsernamePasswordAuthenticationToken(
              subject,
              null,
              AuthorityUtils.NO_AUTHORITIES
          );
          authentication.setDetails(claims);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ignored) {
          SecurityContextHolder.clearContext();
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  private Map<String, Object> parseClaims(String token) throws IOException {
    String[] parts = token.split("\\.");
    if (parts.length < 2) {
      throw new IllegalArgumentException("Invalid JWT");
    }

    byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
    String json = new String(decoded, StandardCharsets.UTF_8);
    return objectMapper.readValue(json, MAP_TYPE);
  }
}
