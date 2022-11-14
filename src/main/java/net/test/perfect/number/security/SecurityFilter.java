package net.test.perfect.number.security;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@Provider
@Slf4j
public class SecurityFilter implements ContainerRequestFilter {

    private static final String INVALID_TOKEN_ERROR_MESSAGE = "Invalid token";
    private static final String INVALID_APPLICATION_MISSING =
            "Application in application name header is missing.";
    private static final String INVALID_TOKEN_EXPIRY_TIME =
            "Token expiry time is to far in to the future.";
    private static final String TOKEN_HAS_EXPIRED =
            "Token has expired.";
    private static final String TOKEN_EXPIRY_IS_MISSING =
            "Token expiry is missing or malformed.";
    private static final String AUTHENTICATION_FAILED = "Authentication failed";

    private static final String HEADER_NAME_APPLICATION_NAME = "Application-Name";
    private static final String HEADER_NAME_TOKEN_EXPIRY = "Token-Expiry";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_KEY = "secretKeyForPerfectNumber";


    private String getTokenFromAuthorizationHeader(String authHeader) {
        return authHeader.replace(TOKEN_PREFIX, "").trim();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String authHeader = containerRequestContext.getHeaderString(javax.ws.rs.core.HttpHeaders.AUTHORIZATION);
        String applicationName = containerRequestContext.getHeaderString(HEADER_NAME_APPLICATION_NAME);
        String tokenExpiry = containerRequestContext.getHeaderString(HEADER_NAME_TOKEN_EXPIRY);
        String method = containerRequestContext.getMethod();

        if (Strings.isNullOrEmpty(authHeader)) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(INVALID_TOKEN_ERROR_MESSAGE)
                    .build());
            return;
        }

        String cleanAuthHeader = getTokenFromAuthorizationHeader(authHeader);

        if (Strings.isNullOrEmpty(tokenExpiry)) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(TOKEN_EXPIRY_IS_MISSING)
                    .build());

            return;
        }
        Instant validTo;
        try {
            validTo = Instant.parse(tokenExpiry);
        } catch (DateTimeParseException e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(e.getMessage())
                    .build());
            return;
        }

        if (validTo.isAfter(Instant.now().plusSeconds(HmacData.MAX_EXPIRY_IN_SECONDS))) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(INVALID_TOKEN_EXPIRY_TIME)
                    .build());

            return;
        }

        //Token has expired
        if (validTo.isBefore(Instant.now())) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(TOKEN_HAS_EXPIRED)
                    .build());

            return;
        }

        if (Strings.isNullOrEmpty(applicationName)) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(INVALID_APPLICATION_MISSING)
                    .build());
            return;
        }

        String key = TOKEN_KEY;

        try {
            String hmac = HmacGenerator.createHmacFromData(applicationName, method, key, validTo);
            if (!hmac.equals(cleanAuthHeader)) {
                containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(AUTHENTICATION_FAILED)
                        .build());
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(e.getMessage())
                    .build());
        }
    }
}