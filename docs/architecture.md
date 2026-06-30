
Example architecture intended as a development guide
```
src/main/java/com/yourcompany/aiagent/
│
├── config/                     # 🛠️ Configuration & Beans
│   ├── LlmConfig.java          # ChatClient, ChatModel beans
│   ├── SecurityConfig.java     # Spring Security, OAuth2
│   └── ObservabilityConfig.java# OpenTelemetry, Micrometer setup
│
├── api/                        # 🌐 Inbound Adapters (The Edge)
│   ├── AgentController.java    # REST endpoints (Standard JSON)
│   ├── AgentStreamingController.java # SSE endpoints (Streaming)
│   └── dto/                    # Request/Response DTOs (Records)
│       ├── ChatRequest.java
│       └── ChatResponse.java
│
├── agent/                      # 🧠 The Core Brain (Orchestration)
│   ├── core/
│   │   ├── ReActAgent.java     # The main while-loop / state machine
│   │   └── AgentOrchestrator.java # Routes requests to specific agent types
│   ├── state/
│   │   ├── AgentContext.java   # Holds state for a single execution (traceId, etc.)
│   │   └── ExecutionResult.java# Encapsulates the final output of a loop
│   └── planner/                # (Optional) For advanced multi-step planning
│
├── tools/                      # 🛠️ Outbound Adapters (LLM Functions)
│   ├── registry/
│   │   └── ToolMetadataRegistry.java # Manages tool descriptions for the LLM
│   └── implementations/        # The actual business logic for tools
│       ├── WeatherTool.java    # @Tool annotated Spring Bean
│       ├── DatabaseQueryTool.java
│       └── WebSearchTool.java
│
├── memory/                     # 🧠 Context & History Management
│   ├── ChatMemoryManager.java  # Handles short-term conversation history
│   └── VectorStoreManager.java # Handles long-term RAG / semantic memory
│
├── prompts/                    # 📝 Prompt Engineering & Management
│   ├── PromptTemplateRegistry.java # Loads and caches .st / .txt files
│   └── SystemPromptBuilder.java    # Dynamically builds system prompts
│
├── guardrails/                 # 🛡️ Security & Safety (Crucial for Prod)
│   ├── InputGuardrail.java     # Checks for prompt injection, PII before LLM
│   ├── OutputGuardrail.java    # Checks for hallucinations, PII after LLM
│   └── ToolExecutionGuardrail.java # Validates tool arguments before execution
│
├── observability/              # 📊 Metrics, Logs, and Tracing
│   ├── AgentMetrics.java       # Custom Micrometer metrics (tokens, latency)
│   ├── ToolTracingAspect.java  # AOP to automatically trace tool executions
│   └── LlmCallbackHandler.java # Spring AI callback to trace LLM calls
│
├── domain/                     # 📦 Internal Domain Models
│   ├── AgentSession.java
│   └── ToolExecutionRecord.java
│
└── exception/                  # ⚠️ Global Error Handling
    ├── GlobalExceptionHandler.java # @RestControllerAdvice
    ├── AgentTimeoutException.java
    └── GuardrailViolationException.java


src/main/resources/
├── application.yml             # Main config (Virtual threads, OTel, etc.)
├── application-dev.yml         # Dev specific (local LLM, mock tools)
├── application-prod.yml        # Prod specific (real LLM, strict security)
├── logback-spring.xml          # JSON Structured logging config
│
└── prompts/                    # Externalized Prompt Templates
    ├── react_system_prompt.st  # Main ReAct system prompt (StringTemplate)
    ├── weather_tool_desc.txt   # Detailed descriptions for complex tools
    └── guardrail_classifier.st # Prompt for the injection classifier LLM
```
