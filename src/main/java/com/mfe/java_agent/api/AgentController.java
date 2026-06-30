package com.mfe.java_agent.api;

import com.mfe.java_agent.api.dto.ChatRequest;
import com.mfe.java_agent.api.dto.ChatResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Synchronous REST endpoint for agent interactions.
 * <p>
 * Accepts a user message, delegates to the agent orchestration layer,
 * and returns the final agent response as a standard JSON payload.
 * This is the primary inbound adapter for non-streaming chat use cases.
 * <p>
 * In an agentic AI context, this controller is the entry point for
 * request-response interactions — the caller blocks until the full
 * ReAct loop completes.
 */
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        // TODO: Delegate to AgentOrchestrator and return result
        return ResponseEntity.ok(
                new ChatResponse("Agent response placeholder", java.util.UUID.randomUUID().toString()));
    }
}
