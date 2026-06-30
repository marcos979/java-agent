package com.mfe.java_agent.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model representing a single user-agent interaction session.
 * <p>
 * Groups all state for one conversation: the session identifier,
 * creation timestamp, the agent type used, and the accumulated
 * execution history. This is the persistent counterpart to the
 * ephemeral {@code AgentContext}.
 * <p>
 * In an agentic AI context, sessions enable multi-turn conversations
 * where the agent remembers previous interactions and tool results
 * across HTTP requests.
 */
public class AgentSession {

    private final String id = UUID.randomUUID().toString();
    private final Instant createdAt = Instant.now();

    // TODO: Add agent type, execution history, and persistence mapping
}
