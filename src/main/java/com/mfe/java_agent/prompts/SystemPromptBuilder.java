package com.mfe.java_agent.prompts;

import org.springframework.stereotype.Component;

/**
 * Dynamically assembles the system prompt for each agent execution.
 * <p>
 * Combines the base ReAct prompt template with runtime context: the
 * current tool list (from {@code ToolMetadataRegistry}), session-specific
 * instructions, and any guardrail policies. The result is a fully
 * rendered system message ready for the LLM.
 * <p>
 * In an agentic AI context, the system prompt is the agent's
 * "constitution" — it defines the reasoning format, tool-use rules,
 * and output constraints for the entire execution.
 */
@Component
public class SystemPromptBuilder {

    // TODO: Merge templates + runtime context into final system prompt
}
