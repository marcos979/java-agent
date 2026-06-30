package com.mfe.java_agent.tools.registry;

import org.springframework.stereotype.Component;

/**
 * Registry that maintains metadata about all available tools for the LLM.
 * <p>
 * Before each reasoning step, the agent asks this registry for the current
 * list of tool names, descriptions, and parameter schemas. The LLM uses
 * this metadata to decide which tool (if any) to invoke.
 * <p>
 * In an agentic AI context, this is the "tool catalog" — dynamically
 * adding or removing tools here changes the agent's capabilities at
 * runtime without modifying the core loop.
 */
@Component
public class ToolMetadataRegistry {

    // TODO: Scan @Tool-annotated beans, build JSON Schema descriptions, and cache them
}
