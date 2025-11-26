package com.example.web.config;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Component
public class AuthorizationInterceptor implements WebGraphQlInterceptor {

    public static final String AUTH_CONTEXT_KEY = "AuthorizationHeader";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String token = request.getHeaders().getFirst("Authorization");

        request.configureExecutionInput((executionInput, builder) -> {
            if (token != null) {
                Map<String, Object> contextMap = Collections.singletonMap(AUTH_CONTEXT_KEY, token);
                builder.graphQLContext(contextMap);
            }
            return builder.build();
        });

        return chain.next(request);
    }
}