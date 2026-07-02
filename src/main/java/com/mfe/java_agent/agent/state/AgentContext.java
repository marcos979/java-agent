package com.mfe.java_agent.agent.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.messages.Message;

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

    private final String traceId;
    private String sessionId;
    private List<Message> messageHistory;
    private Map<String, Object> toolCalls;
    private final List<String> conversationHistory;
    private int iterationCount;
    private static final int MAX_ITERATIONS = 5;

    /** Creates a new context with an auto-generated UUID traceId. */
    public AgentContext() {
        this.traceId = UUID.randomUUID().toString();
        this.conversationHistory = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        this.toolCalls = new HashMap<>();
    }

    // -- Getters / Setters --

    public String getTraceId() {
        return traceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(List<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public Map<String, Object> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(Map<String, Object> toolCalls) {
        this.toolCalls = toolCalls;
    }

    public List<String> getConversationHistory() {
        return conversationHistory;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public int getMaxIterations() {
        return MAX_ITERATIONS;
    }
}
