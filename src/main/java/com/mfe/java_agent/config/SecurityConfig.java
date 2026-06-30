package com.mfe.java_agent.config;

import org.springframework.context.annotation.Configuration;

/**
 * Security configuration for the agent API surface.
 * <p>
 * Configures Spring Security filters, OAuth2 client registration, and
 * CORS policies. Every exposed agent endpoint must be protected against
 * unauthorized access — a compromised agent with tool access is a
 * high-impact security risk.
 * <p>
 * In an agentic AI context, this layer also enforces rate limiting and
 * ensures that tool executions are scoped to the authenticated principal.
 */
@Configuration
public class SecurityConfig {

    // TODO: Configure SecurityFilterChain, OAuth2, and CORS
}
