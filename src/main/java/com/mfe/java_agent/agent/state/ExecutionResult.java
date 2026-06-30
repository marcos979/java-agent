package com.mfe.java_agent.agent.state;

/**
 * Immutable result envelope for a completed agent execution.
 * <p>
 * Encapsulates the final output of the ReAct loop along with metadata
 * about the execution: number of iterations taken, tokens consumed,
 * and whether the run ended with a final answer or was terminated
 * by a timeout / guardrail.
 * <p>
 * Using a Java {@code record} ensures that consumers of the result
 * (controllers, metrics collectors) receive a stable snapshot.
 */
public record ExecutionResult(
        String finalAnswer,
        String traceId,
        int iterationsUsed,
        int totalTokensConsumed,
        TerminationReason terminationReason) {

    public enum TerminationReason {
        FINAL_ANSWER,
        MAX_ITERATIONS_REACHED,
        GUARDRAIL_INTERVENTION,
        TIMEOUT
    }
}
