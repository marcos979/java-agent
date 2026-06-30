package com.mfe.java_agent.api.dto;

/**
 * Outbound chat response DTO.
 * <p>
 * Encapsulates the agent's final answer along with a correlation ID
 * for traceability. The {@code traceId} links back to the full execution
 * trace (LLM calls, tool invocations, reasoning steps) in the
 * observability backend.
 *
 * @param answer   the agent's natural-language response
 * @param traceId  unique identifier for this execution (maps to OpenTelemetry trace)
 */
public record ChatResponse(String answer, String traceId) {
}
