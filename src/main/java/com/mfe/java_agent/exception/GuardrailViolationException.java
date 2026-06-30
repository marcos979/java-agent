package com.mfe.java_agent.exception;

/**
 * Thrown when an input, output, or tool-execution guardrail check fails.
 * <p>
 * Guardrail violations are distinct from validation errors — they
 * represent policy decisions (blocked content, detected injection,
 * unsafe tool arguments) rather than malformed data. The exception
 * carries enough context for the caller to understand which guardrail
 * triggered and why.
 */
public class GuardrailViolationException extends RuntimeException {

    private final String guardrailName;

    public GuardrailViolationException(String guardrailName, String message) {
        super(message);
        this.guardrailName = guardrailName;
    }

    public String getGuardrailName() {
        return guardrailName;
    }
}
