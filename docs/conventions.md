# Conventions

## Project Structure

- Package naming uses underscores (`java_agent`, not `java-agent`) per Maven requirements
- Use `./mvnw` (the wrapper), never the system `mvn`
- `HELP.md` is the Spring Initializr generated file — keep it for reference but don't treat it as project documentation

---

## Hexagonal Architecture Conventions

### Package Layout Rules

| Layer | Package Prefix | Spring Allowed? | Depends On |
|-------|----------------|-----------------|------------|
| Domain | `com.mfe.java_agent.domain` | ❌ No | `java.*`, `jakarta.*`, own `domain.port.*` |
| Application | `com.mfe.java_agent.application` | ❌ No | `domain.*` |
| Adapter (in) | `com.mfe.java_agent.adapter.in` | ✅ Yes | `application.port.in.*`, `domain.model.*` |
| Adapter (out) | `com.mfe.java_agent.adapter.out` | ✅ Yes | `domain.port.out.*`, `domain.model.*` |
| Config | `com.mfe.java_agent.config` | ✅ Yes | All layers (wiring only) |
| Exception (edge) | `com.mfe.java_agent.exception` | ✅ Yes | `domain.exception.*` |

### Dependency Direction

```
DOMAIN ◄── APPLICATION ◄── ADAPTER (in)
  ▲                              │
  │                              ▼
  └────────── ADAPTER (out) ────┘
```

- **Domain** has zero external dependencies
- **Application** depends only on Domain
- **Adapters** implement Domain ports and may use any framework
- **Config** wires everything together (the only place that knows concrete adapter classes)

### Port Naming

- **Primary Ports (Inbound)**: `*UseCase` suffix in `domain.port.in`
  - `ExecuteAgentUseCase`, `StreamAgentUseCase`, `ManageMemoryUseCase`
- **Secondary Ports (Outbound)**: `*Port` suffix in `domain.port.out`
  - `LlmPort`, `ToolPort`, `ChatMemoryPort`, `VectorStorePort`, `GuardrailPort`, `ObservabilityPort`

### Adapter Naming

- **Inbound Adapters**: `*Controller`, `*Handler` in `adapter.in.<protocol>`
  - `AgentController`, `AgentStreamingController`, `KnowledgeController`
- **Outbound Adapters**: `*Adapter` suffix in `adapter.out.<technology>`
  - `SpringAiLlmAdapter`, `InMemoryChatMemoryAdapter`, `SpringAiVectorStoreAdapter`
  - Tool implementations: `*Tool` in `adapter.out.tool.implementations`

---

## Clean Code Guidelines

### Readability over cleverness

Code is read more than written.

### Small, single-responsibility units

A method does one thing. If it needs section comments, it is doing more than one thing — split it.

### One level of abstraction per method

High-level policy on top, low-level detail in helpers. The body of a method should not mix `Files.readAllBytes(path)` with "build the response."

### Keep methods short enough to read top-to-bottom

Anything pushing past ~20 lines is a smell.

### Limit arguments

Zero is ideal, then one, then two. Three or more usually means the method is doing too much — bundle related args into a parameter object.

### No flag arguments

`render(s, true)` → Split into `renderAscii(s)` and `render(s)`.

### No side effects behind innocent names

A method called `checkPassword()` must not also start a session.

### Prefer pure methods

Push I/O, time, and randomness to the edges so the core stays testable. A service should orchestrate effects; the decision logic should be a pure method it calls.

### Domain Layer Purity

- **No Spring annotations** (`@Service`, `@Component`, `@Autowired`, `@Value`, etc.)
- **No Spring types** (`ApplicationContext`, `Environment`, `Resource`, etc.)
- **No framework types** (Spring AI `ChatClient`, `ChatModel`, Reactor `Flux`/`Mono` in domain logic — use domain-specific types instead)
- **Constructors only** for dependencies (no field injection)
- **Records** for immutable data (DTOs, commands, results)
- **Interfaces** for all ports (enables mocking in tests without Spring)

### Application Layer

- Implements primary ports (`*UseCase` interfaces)
- Orchestrates domain services and secondary ports
- **No Spring annotations** (except `@Transactional` if needed for DB)
- **Constructor injection** of ports (interfaces)
- Thin — delegates to domain services for logic

### Adapter Layer

- **Inbound**: Controllers, handlers — translate HTTP/CLI → Use Case commands
- **Outbound**: Implement secondary ports — translate Port calls → Framework APIs
- **Full Spring power allowed** — `@Component`, `@Configuration`, `@Autowired`, etc.
- **Map domain exceptions** to infrastructure responses (HTTP status codes, etc.)

---

## Comments

- **Good code is its own best documentation.** Refactor instead of commenting.
- **Keep the few comments that earn their place: the why, not the what.** Intent, constraint, non-obvious decision. If you can delete the comment and the code still surprises no one, delete it.
- **Use `// Why: ...` for non-obvious decisions** (constraint, perf, compatibility, workaround). Not JSDoc, not block headers.
- **No journal-style comments** — useless noise.

---

## SOLID (Hexagonal Context)

| Principle | Hexagonal Application |
|-----------|----------------------|
| **Single Responsibility** | Each port = one use case. Each adapter = one external system. |
| **Open/Closed** | Add new adapters (e.g., new LLM provider) without touching domain. |
| **Liskov Substitution** | Any `LlmPort` implementation must be swappable (OpenAI, Anthropic, local). |
| **Interface Segregation** | Many small ports (`LlmPort`, `ToolPort`, `ChatMemoryPort`) not one `AgentPort`. |
| **Dependency Inversion** | Domain defines ports. Adapters implement them. Config binds them. |

---

## Testing Conventions

| Layer | Test Style | Spring Context? |
|-------|------------|-----------------|
| Domain | Pure unit tests, mock ports | ❌ No |
| Application | Unit tests, mock ports | ❌ No |
| Adapter (out) | Integration tests | ✅ Yes (`@SpringBootTest` or `@DataJpaTest`) |
| Adapter (in) | WebMvcTest / WebFluxTest | ✅ Yes (slice test) |
| E2E | Full `@SpringBootTest` | ✅ Yes |

**Rule:** Domain and Application tests must run without starting Spring. They test logic, not wiring.

---

## Build & Run

```bash
./mvnw clean package          # Full build
./mvnw test                   # Run all tests
./mvnw test -Dtest=ClassName  # Run a single test class
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Java 25 is required. The Maven wrapper (`mvnw`) is committed — always use it instead of the system `mvn`.

---

## Environment Validation

`./init.sh` runs a full environment check and must pass before any task is considered done. It verifies:
- Java >= 25 and Maven are available
- Required files exist: `README.md`, `pom.xml`, `.gitignore`, `docs/architecture.md`, `docs/conventions.md`, `CHECKPOINTS.md`
- `pom.xml` is valid XML with required Maven properties
- All tests pass

If any of these fail, the session should not advance until they are fixed.