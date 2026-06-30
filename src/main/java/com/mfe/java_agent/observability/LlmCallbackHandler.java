package com.mfe.java_agent.observability;

import org.springframework.stereotype.Component;

/**
 * Callback handler that intercepts Spring AI LLM calls for tracing.
 * <p>
 * Hooks into the Spring AI call lifecycle to capture:
 * <ul>
 *   <li>Prompt sent to the model (including system prompt and tool definitions)</li>
 *   <li>Response received (including tool call requests)</li>
 *   <li>Token counts and latency</li>
 * </ul>
 * These callbacks are the primary data source for per-request
 * observability — without them, the LLM interaction is a black box.
 */
@Component
public class LlmCallbackHandler {

    // TODO: Implement Spring AI callback interface, attach OpenTelemetry spans
}
