# Architecture: Hexagonal (Ports & Adapters)

This project follows **Hexagonal Architecture** (also known as Ports and Adapters) to ensure the core domain logic is decoupled from external frameworks, databases, and delivery mechanisms.

---

## High-Level Structure

```
src/main/java/com/mfe/java_agent/
│
├── domain/                          # 🎯 CORE DOMAIN (No Spring, No Frameworks)
│   ├── model/                       #   Pure domain entities & value objects
│   │   ├── AgentSession.java        #   Immutable session aggregate
│   │   ├── AgentContext.java        #   Execution context (traceId, history)
│   │   ├── ExecutionResult.java     #   Result of an agent run
│   │   ├── ToolCall.java            #   Domain representation of a tool invocation
│   │   └── Message.java             #   Conversation message (role, content)
│   ├── port/                        #   PORT INTERFACES (defined by domain)
│   │   ├── in/                      #     PRIMARY PORTS (Use Cases / Inbound)
│   │   │   ├── ExecuteAgentUseCase.java    #   "Run the agent"
│   │   │   ├── StreamAgentUseCase.java     #   "Stream the agent"
│   │   │   ├── ManageMemoryUseCase.java    #   "Load/Save conversation history"
│   │   │   └── ManageKnowledgeUseCase.java #   "Ingest/Search documents"
│   │   └── out/                     #     SECONDARY PORTS (Driven / Outbound)
│   │       ├── LlmPort.java              #   "Call an LLM"
│   │       ├── ToolPort.java             #   "Execute a tool"
│   │       ├── ChatMemoryPort.java       #   "Persist/Retrieve short-term history"
│   │       ├── VectorStorePort.java      #   "Persist/Retrieve embeddings"
│   │       ├── GuardrailPort.java        #   "Validate input/output/tool args"
│   │       └── ObservabilityPort.java    #   "Record metrics, traces, logs"
│   ├── service/                     #   DOMAIN SERVICES (Pure logic, no I/O)
│   │   ├── ReActEngine.java         #   Core ReAct reasoning loop
│   │   ├── SystemPromptBuilder.java #   Constructs prompts from templates
│   │   └── TokenEstimator.java      #   Estimates token counts
│   └── exception/                   #   Domain exceptions
│       ├── AgentExecutionException.java
│       ├── GuardrailViolationException.java
│       └── TokenLimitExceededException.java
│
├── application/                     # 🔧 APPLICATION LAYER (Use Case Orchestration)
│   ├── service/                     #   Implements PRIMARY PORTS (Inbound)
│   │   ├── ExecuteAgentService.java     #   Implements ExecuteAgentUseCase
│   │   ├── StreamAgentService.java      #   Implements StreamAgentUseCase
│   │   ├── ChatMemoryService.java       #   Implements ManageMemoryUseCase
│   │   └── KnowledgeService.java        #   Implements ManageKnowledgeUseCase
│   └── config/                      #   Spring configuration for wiring adapters
│       └── DomainConfig.java
│
├── adapter/                         # 🔌 ADAPTERS (Infrastructure implementations)
│   ├── in/                          #   INBOUND ADAPTERS (Delivery mechanisms)
│   │   ├── rest/                    #     REST Controllers
│   │   │   ├── AgentController.java       #   POST /api/agent/chat
│   │   │   ├── AgentStreamingController.java # GET /api/agent/chat/stream
│   │   │   └── KnowledgeController.java   #   POST /api/agent/knowledge
│   │   │   └── dto/                 #     Request/Response DTOs (Records)
│   │   │       ├── ChatRequest.java
│   │   │       ├── ChatResponse.java
│   │   │       └── KnowledgeIngestRequest.java
│   │   └── cli/                     #     (Future) CLI adapter
│   │
│   └── out/                         #   OUTBOUND ADAPTERS (External systems)
│       ├── llm/                     #     LLM Providers
│       │   ├── SpringAiLlmAdapter.java    #   Implements LlmPort via Spring AI
│       │   └── config/              #     Spring AI ChatClient setup
│       ├── tool/                    #     Tool Execution
│       │   ├── SpringAiToolAdapter.java   #   Implements ToolPort via Spring AI @Tool
│       │   ├── registry/            #     Tool discovery & registration
│       │   │   └── ToolMetadataRegistry.java
│       │   └── implementations/     #     Concrete tool beans
│       │       ├── WeatherTool.java
│       │       └── WebSearchTool.java
│       ├── memory/                  #     Persistence Adapters
│       │   ├── InMemoryChatMemoryAdapter.java # Implements ChatMemoryPort
│       │   └── SpringAiVectorStoreAdapter.java # Implements VectorStorePort
│       ├── guardrails/              #     Safety Adapters
│       │   ├── CompositeGuardrailAdapter.java # Chains multiple guardrails
│       │   ├── InputGuardrailAdapter.java
│       │   ├── OutputGuardrailAdapter.java
│       │   └── ToolExecutionGuardrailAdapter.java
│       └── observability/           #     Observability Adapters
│           ├── MicrometerObservabilityAdapter.java # Implements ObservabilityPort
│           ├── ToolTracingAspect.java              # AOP for tool tracing
│           └── LlmCallbackHandler.java             # Spring AI callback
│
├── config/                          # ⚙️ SPRING BOOT CONFIGURATION (Wiring)
│   ├── AdapterConfig.java           #   @Configuration: binds Ports → Adapters
│   ├── LlmConfig.java               #   ChatClient, ChatModel beans
│   ├── SecurityConfig.java          #   Spring Security (if needed)
│   └── ObservabilityConfig.java     #   OpenTelemetry, Micrometer
│
└── exception/                       # 🌐 GLOBAL ERROR HANDLING (Edge)
    └── GlobalExceptionHandler.java  #   @RestControllerAdvice
```

---

## Dependency Rule

```
                    ┌─────────────────────┐
                    │     DOMAIN          │  ◄─── Pure Java, zero dependencies
                    │  (model, port, svc) │
                    └──────────┬──────────┘
                               │ implements / depends on
                               ▼
                    ┌─────────────────────┐
                    │   APPLICATION       │  ◄─── Orchestrates domain, no Spring
                    │   (use case impl)   │
                    └──────────┬──────────┘
                               │ uses
                               ▼
              ┌────────────────┴────────────────┐
              ▼                                 ▼
   ┌─────────────────────┐           ┌─────────────────────┐
   │   ADAPTER (in)      │           │   ADAPTER (out)     │  ◄─── Spring, HTTP, DB, LLM SDKs
   │   REST, CLI, ...    │           │   LLM, Tools, Mem   │
   └─────────────────────┘           └─────────────────────┘
```

**Key Rule:** Domain and Application layers **never** depend on Spring, Spring AI, or any infrastructure. They only depend on:
- `java.*` / `jakarta.*` (standard APIs)
- Project's own `domain.port.*` interfaces

Adapters implement the ports and can use any framework.

---

## Port Contracts (Key Interfaces)

### Primary Ports (Inbound / Use Cases)

```java
// domain.port.in.ExecuteAgentUseCase
public interface ExecuteAgentUseCase {
    ExecutionResult execute(ExecuteAgentCommand command);
}

// domain.port.in.StreamAgentUseCase
public interface StreamAgentUseCase {
    Flux<AgentEvent> stream(StreamAgentCommand command);
}

// domain.port.in.ManageMemoryUseCase
public interface ManageMemoryUseCase {
    List<Message> loadHistory(String sessionId);
    void saveHistory(String sessionId, List<Message> messages);
}
```

### Secondary Ports (Outbound / Driven)

```java
// domain.port.out.LlmPort
public interface LlmPort {
    LlmResponse call(LlmRequest request);
    Flux<LlmChunk> stream(LlmRequest request);
}

// domain.port.out.ToolPort
public interface ToolPort {
    ToolResult execute(ToolCall toolCall);
}

// domain.port.out.ChatMemoryPort
public interface ChatMemoryPort {
    List<Message> getMessages(String sessionId, int maxTokens);
    void appendMessages(String sessionId, List<Message> messages);
}

// domain.port.out.VectorStorePort
public interface VectorStorePort {
    void ingest(List<Document> documents);
    List<Document> search(String query, int topK);
}

// domain.port.out.GuardrailPort
public interface GuardrailPort {
    void validateInput(String content) throws GuardrailViolationException;
    String validateOutput(String content) throws GuardrailViolationException; // returns sanitized
    void validateToolArgs(Map<String, Object> args) throws GuardrailViolationException;
}
```

---

## Mapping Current Features to Hexagonal Layers

| Feature | Layer | Component |
|---------|-------|-----------|
| F001.1 Domain Models | **Domain** | `domain.model.AgentContext`, `ExecutionResult` |
| F001.2 Basic LLM | **Adapter (out)** | `adapter.out.llm.SpringAiLlmAdapter` implements `LlmPort` |
| F001.3 ReAct Loop | **Domain** | `domain.service.ReActEngine` (pure logic) |
| F001.4 Orchestrator | **Application** | `application.service.ExecuteAgentService` implements `ExecuteAgentUseCase` |
| F002.1 WeatherTool | **Adapter (out)** | `adapter.out.tool.implementations.WeatherTool` |
| F002.2 Tool Registry | **Adapter (out)** | `adapter.out.tool.registry.ToolMetadataRegistry` |
| F002.3 Real Tool Exec | **Adapter (out)** | `adapter.out.tool.SpringAiToolAdapter` implements `ToolPort` |
| F003.1 Sync REST | **Adapter (in)** | `adapter.in.rest.AgentController` |
| F003.2/3 SSE | **Adapter (in)** | `adapter.in.rest.AgentStreamingController` |
| F004.1/2/3 Memory | **Adapter (out)** | `adapter.out.memory.InMemoryChatMemoryAdapter` implements `ChatMemoryPort` |
| F005.1/2/3 RAG | **Adapter (out)** | `adapter.out.memory.SpringAiVectorStoreAdapter` implements `VectorStorePort` |
| F006.1-5 Guardrails | **Adapter (out)** | `adapter.out.guardrails.*` implements `GuardrailPort` |
| F007.1-4 Observability | **Adapter (out)** | `adapter.out.observability.*` implements `ObservabilityPort` |
| F008.1-3 Prompts | **Domain** | `domain.service.SystemPromptBuilder`, `PromptTemplateRegistry` (as domain service) |

---

## Migration Strategy (Incremental)

1. **Create `domain/` package structure** with port interfaces first
2. **Move F001.1 models** to `domain.model` (already done — verify location)
3. **Implement `ReActEngine`** in `domain.service` (F001.3 logic, no Spring)
4. **Create `ExecuteAgentService`** in `application.service` (F001.4 logic)
5. **Build `SpringAiLlmAdapter`** implementing `LlmPort` (F001.2)
6. **Wire via `AdapterConfig`** — bind ports to adapters
7. **Move controllers** to `adapter.in.rest` (F003.1, F003.2/3)
8. **Implement remaining ports** as features progress

Each feature in `feature_list.json` should be implemented as a **vertical slice** touching all layers:
- Domain: port interfaces + domain service logic
- Application: use case implementation
- Adapter (in): REST endpoint
- Adapter (out): infrastructure implementation

---

## Testing Strategy

| Layer | Test Type | Tools |
|-------|-----------|-------|
| Domain | Unit (pure logic) | JUnit, Mockito (for port mocks) |
| Application | Unit (orchestration) | JUnit, Mockito (mock ports) |
| Adapter (out) | Integration | Spring Boot Test, Testcontainers |
| Adapter (in) | WebMvcTest / WebFluxTest | Spring Boot Test |
| End-to-End | @SpringBootTest | Spring Boot Test |

**Domain tests must not start Spring context.** They test pure logic with mocked ports.