package com.mfe.java_agent.tools.implementations;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Dummy weather tool to demonstrate Spring AI's {@code @Tool} annotation model.
 * <p>
 * Spring AI automatically discovers {@code @Tool}-annotated methods and
 * exposes them to the LLM as callable functions. The annotation metadata
 * (description and parameter docs) is used to build the function schema
 * sent to the model.
 * <p>
 * In an agentic AI context, tools are the agent's "hands" — the bridge
 * between reasoning and real-world action. Every tool must validate its
 * inputs, fail gracefully, and never leak sensitive data.
 */
@Component
public class WeatherTool {

    /**
     * Returns a simulated weather forecast for the given city.
     *
     * @param city the city name (e.g., "Barcelona")
     * @return a string describing current weather conditions
     */
    @Tool(description = "Get the current weather for a given city")
    public String getWeather(
            @ToolParam(description = "City name, e.g. 'Barcelona' or 'New York'") String city) {
        return "Sunny, 22°C in " + city + " (simulated)";
    }
}
