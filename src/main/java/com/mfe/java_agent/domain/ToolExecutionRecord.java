package com.mfe.java_agent.domain;

import java.time.Instant;

/**
 * Immutable record of a single tool execution within an agent run.
 * <p>
 * Captures the tool name, arguments, result, duration, and outcome
 * for audit and debugging purposes. These records are the primary
 * data source for tool-level metrics and cost attribution.
 * <p>
 * Using a Java {@code record} ensures immutability — tool execution
 * history must never be modified after the fact.
 */
public record ToolExecutionRecord(
        String toolName,
        String arguments,
        String result,
        long durationMs,
        boolean success,
        Instant timestamp) {
}
