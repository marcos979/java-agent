package com.mfe.java_agent.api;

import com.mfe.java_agent.api.dto.ChatRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

/**
 * Streaming endpoint for agent interactions using Server-Sent Events (SSE).
 * <p>
 * Accepts a user message and returns a reactive {@code Flux<String>} stream
 * that emits each reasoning step and tool result as it happens. This gives
 * the caller real-time visibility into the agent's thought process.
 * <p>
 * In an agentic AI context, streaming is essential for long-running
 * multi-step tasks — it keeps the connection alive and provides
 * progressive feedback while the ReAct loop executes.
 */
@RestController
@RequestMapping("/api/agent")
public class AgentStreamingController {

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request) {
        // TODO: Delegate to ReActAgent streaming pipeline
        return Flux.just("Thinking...", "Tool call: weather", "Final answer placeholder");
    }
}
