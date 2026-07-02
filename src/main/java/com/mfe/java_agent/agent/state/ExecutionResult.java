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

    /**
     * Creates a success result with {@link TerminationReason#FINAL_ANSWER}.
     *
     * @param finalAnswer         the final text produced by the agent
     * @param traceId             tracing identifier for this execution
     * @param iterationsUsed      number of iterations the loop ran
     * @param totalTokensConsumed estimated token consumption
     * @return a new {@code ExecutionResult} marked as successful
     */
    public static ExecutionResult success(String finalAnswer, String traceId, int iterationsUsed, int totalTokensConsumed) {
        return new ExecutionResult(finalAnswer, traceId, iterationsUsed, totalTokensConsumed, TerminationReason.FINAL_ANSWER);
    }

    /**
     * Creates a failure result with {@link TerminationReason#GUARDRAIL_INTERVENTION}.
     * <p>
     * The error cause is stored in the {@code finalAnswer} field so the caller
     * can inspect what went wrong.
     *
     * @param errorCause          description of the failure
     * @param traceId             tracing identifier for this execution
     * @param iterationsUsed      number of iterations the loop ran
     * @param totalTokensConsumed estimated token consumption
     * @return a new {@code ExecutionResult} marked as failure
     */
    public static ExecutionResult failure(String errorCause, String traceId, int iterationsUsed, int totalTokensConsumed) {
        return new ExecutionResult(errorCause, traceId, iterationsUsed, totalTokensConsumed, TerminationReason.GUARDRAIL_INTERVENTION);
    }
}
