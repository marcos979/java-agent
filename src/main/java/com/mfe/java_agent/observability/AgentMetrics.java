package com.mfe.java_agent.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Custom Micrometer metrics for agent-specific observability.
 * <p>
 * Tracks counters and histograms for:
 * <ul>
 *   <li>Total tokens consumed (by model, by session)</li>
 *   <li>ReAct loop iterations per execution</li>
 *   <li>Tool call latency and success/failure rates</li>
 *   <li>Guardrail rejection counts</li>
 * </ul>
 * In an agentic AI context, these metrics feed cost dashboards,
 * performance alerts, and anomaly detection — the non-deterministic
 * nature of LLMs makes observability critical.
 */
@Component
public class AgentMetrics {

    private final MeterRegistry meterRegistry;

    public AgentMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // TODO: Register counters, timers, and gauges for agent operations
}
