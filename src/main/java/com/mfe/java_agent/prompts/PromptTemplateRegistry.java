package com.mfe.java_agent.prompts;

import org.springframework.stereotype.Component;

/**
 * Loads and caches prompt templates from the classpath.
 * <p>
 * Prompt templates are externalized as {@code .st} (StringTemplate)
 * files under {@code src/main/resources/prompts/}. This registry
 * loads them at startup and provides typed accessors so the agent
 * core never hardcodes prompt text.
 * <p>
 * In an agentic AI context, separating prompts from code enables
 * non-developers to tune agent behavior, simplifies A/B testing
 * of prompt strategies, and keeps large text blocks out of Java files.
 */
@Component
public class PromptTemplateRegistry {

    // TODO: Load .st files, cache parsed templates, expose by name
}
