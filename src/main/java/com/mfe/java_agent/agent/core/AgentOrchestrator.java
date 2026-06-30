package com.mfe.java_agent.agent.core;

import org.springframework.stereotype.Component;

/**
 * Routes incoming requests to the appropriate agent type.
 * <p>
 * Different use cases may require different agent configurations:
 * a simple Q&A agent may run with a single tool, while a research
 * agent may chain search, summarize, and database tools. This
 * orchestrator selects and initializes the right agent variant
 * based on the request context.
 * <p>
 * In an agentic AI context, this is the strategy-selection layer
 * that sits between the API controllers and the core ReAct loop.
 */
@Component
public class AgentOrchestrator {

    // TODO: Route to specific agent types based on request profile
}
