package com.mfe.java_agent.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Inbound chat request DTO.
 * <p>
 * Carries the user's message and optional session identifier. Using a
 * Java {@code record} ensures immutability and eliminates boilerplate —
 * important when DTOs pass through guardrails, the agent core, and
 * observability layers without accidental mutation.
 *
 * @param message  the user's natural-language input
 * @param sessionId optional identifier to resume an existing agent session
 */
public record ChatRequest(
        @NotBlank String message,
        String sessionId) {
}
