package com.mfe.java_agent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the agent API surface.
 * <p>
 * Maps domain-specific exceptions (timeouts, guardrail violations,
 * tool failures) to RFC 9457 {@code ProblemDetail} responses. This
 * ensures that every error the agent produces is surfaced with a
 * consistent, machine-readable structure.
 * <p>
 * In an agentic AI context, structured errors are essential —
 * consumers need to distinguish between "the model timed out",
 * "a tool failed", and "the guardrail blocked this" to decide
 * whether to retry, escalate, or degrade.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgentTimeoutException.class)
    public ProblemDetail handleTimeout(AgentTimeoutException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.GATEWAY_TIMEOUT, ex.getMessage());
        problem.setTitle("Agent Execution Timeout");
        return problem;
    }

    @ExceptionHandler(GuardrailViolationException.class)
    public ProblemDetail handleGuardrailViolation(GuardrailViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Guardrail Violation");
        return problem;
    }

    // TODO: Add handlers for tool failures, LLM errors, and validation exceptions
}
