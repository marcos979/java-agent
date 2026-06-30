package com.mfe.java_agent.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the LLM (Large Language Model) integration layer.
 * <p>
 * Defines beans for {@code ChatClient}, {@code ChatModel}, and embedding
 * models used by the agent core. This is where model parameters
 * (temperature, top-p, token limits) and provider-specific settings are
 * centralized.
 * <p>
 * In an agentic AI context, this configuration decouples the reasoning
 * engine from any specific model provider, allowing the agent to swap
 * between local and cloud-hosted models without changes to the core loop.
 */
@Configuration
public class LlmConfig {

    // TODO: Define ChatClient and ChatModel beans
}
