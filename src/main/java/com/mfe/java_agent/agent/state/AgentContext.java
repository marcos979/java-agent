package com.mfe.java_agent.agent.state;

import java.util.List;
import java.util.UUID;

/**
 * Mutable state holder for a single agent execution.
 * <p>
 * Tracks the conversation history, intermediate reasoning steps,
 * tool call results, and the trace ID that ties this execution
 * to the observability backend. Each invocation of the agent
 * creates a fresh {@code AgentContext}.
 * <p>
 * In an agentic AI context, this is the "scratchpad" that the
 * ReAct loop reads from and writes to — it must be thread-safe
 * when concurrent executions are allowed.
 */
public class AgentContext {

    private final String traceId = UUID.randomUUID().toString();
    private final List<String> conversationHistory = new java.util.ArrayList<>();
    private int iterationCount = 0;
    private static final int MAX_ITERATIONS = 15;

    // TODO: Add message list, tool call history, and utility methods
}
