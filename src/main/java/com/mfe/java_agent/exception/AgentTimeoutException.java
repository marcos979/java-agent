package com.mfe.java_agent.exception;

/**
 * Thrown when the agent's execution exceeds the configured time limit.
 * <p>
 * The ReAct loop runs with a deadline; if the agent hasn't produced
 * a final answer before the timeout, this exception interrupts the
 * loop and returns a partial result (or an error) to the caller.
 */
public class AgentTimeoutException extends RuntimeException {

    public AgentTimeoutException(String message) {
        super(message);
    }

    public AgentTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
