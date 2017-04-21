/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.security;

import com.swiftpot.timetable.util.SecurityConfigurationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Apr-17 @ 4:39 PM
 */
public class JWTFilter extends GenericFilterBean {

    @Autowired
    private SecurityConfigurationProperties securityConfigurationProperties;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    /**
     * Bearer prefix with one white space after it
     */
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String MISSING_AUTH_HEADER_EXCEPTION_MESSAGE = "Missing or Invalid Authorization Header";
    public static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "Invalid token!.";
    public static final String CLAIMS_ATTRIBUTE = "claims";

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if ((Objects.isNull(authHeader)) ||
                (!authHeader.startsWith(BEARER_PREFIX))) {
            throw new ServletException(MISSING_AUTH_HEADER_EXCEPTION_MESSAGE);
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            String secretMasterKey = securityConfigurationProperties.JWT_SECRET_MASTER_KEY;
            String encodedMasterKeyString = Base64.getEncoder().encodeToString(secretMasterKey.getBytes("utf-8"));
            final Claims claims = Jwts.parser().setSigningKey(encodedMasterKeyString)
                    .parseClaimsJws(token).getBody();
            request.setAttribute(CLAIMS_ATTRIBUTE, claims);
        } catch (final SignatureException e) {
            throw new ServletException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }
        chain.doFilter(req, res);
    }
}

