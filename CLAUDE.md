# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
./mvnw clean package          # Full build
./mvnw test                   # Run all tests
./mvnw test -Dtest=ClassName  # Run a single test class
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Java 25 is required. The Maven wrapper (`mvnw`) is committed — always use it instead of the system `mvn`.

## Environment Validation

`./init.sh` runs a full environment check and must pass before any task is considered done. It verifies:
- Java >= 25 and Maven are available
- Required files exist: `README.md`, `pom.xml`, `.gitignore`, `docs/architecture.md`, `docs/conventions.md`, `CHECKPOINTS.md`
- `pom.xml` is valid XML with required Maven properties
- All tests pass

If any of these fail, the session should not advance until they are fixed.

## Architecture

Spring Boot 4.1.0 project. Package: `com.mfe.java_agent` (underscore required by Maven — the logical name `java-agent` uses a hyphen).

**Current state**: the project is in its earliest stage — only the generated `JavaAgentApplication.java` and a single `contextLoads` test exist. There are no additional dependencies beyond `spring-boot-starter`.

**Intended architecture** (per README): the project will grow into a ReAct-pattern AI agent platform with these layers:

| Layer | Package | Role |
|-------|---------|------|
| API | `api/` | REST controllers (JSON + SSE streaming) |
| Agent Core | `agent/core/` | ReAct loop / state machine |
| Tools | `tools/` | Pluggable tool registry + `@Tool` implementations |
| Memory | `memory/` | Short-term conversation history + long-term vector store (RAG) |
| Guardrails | `guardrails/` | Input/output/tool-execution safety checks |
| Observability | `observability/` | OpenTelemetry, Micrometer, structured logging |
| Prompts | `prompts/` | Externalized StringTemplate `.st` files |

Additional architecture doc:
docs/architecture.md

The README is the design document at this stage.

## Conventions

- Package naming uses underscores (`java_agent`, not `java-agent`) per Maven requirements
- Use `./mvnw` (the wrapper), never the system `mvn`
- `HELP.md` is the Spring Initializr generated file — keep it for reference but don't treat it as project documentation

## Guidelines Clean Code

### Clean Code

- **Readability over cleverness.** Code is read more than written.
- **Small, single-responsibility units.** A method does one thing. If it needs section comments, it is doing more than one thing — split it.
- **One level of abstraction per method.** High-level policy on top, low-level detail in helpers. The body of a method should not mix `Files.readAllBytes(path)` with "build the response."
- **Keep methods short enough to read top-to-bottom.** Anything pushing past ~20 lines is a smell.
- **Limit arguments.** Zero is ideal, then one, then two. Three or more usually means the method is doing too much — bundle related args into a parameter object.
- **No flag arguments** (`render(s, true)`). Split into two methods: `renderAscii(s)` and `render(s)`.
- **No side effects behind innocent names.** A method called `checkPassword()` must not also start a session.
- **Prefer pure methods.** Push I/O, time, and randomness to the edges so the core stays testable. A service should orchestrate effects; the decision logic should be a pure method it calls.

### Comments

- **Good code is its own best documentation.** Refactor instead of commenting.
- **Keep the few comments that earn their place: the why, not the what.** Intent, constraint, non-obvious decision. If you can delete the comment and the code still surprises no one, delete it.
- **Use `// Why: ...` for non-obvious decisions** (constraint, perf, compatibility, workaround). Not JSDoc, not block headers.
- **No journal-style comments** Useless noise.


### SOLID, briefly

- **Single responsibility** — a class has one reason to change.
- **Open/closed** — open to extension, closed to modification. Add behavior by adding code, not by editing stable code.
- **Liskov substitution** — subtypes must be substitutable for their base types. Don't weaken postconditions or strengthen preconditions in a subclass or adapter.
- **Interface segregation** — many small interfaces beat one fat one. Don't force a client to depend on methods it doesn't use.
- **Dependency inversion** — depend on abstractions. High-level classes depend on interfaces in `domain/ports/`; infrastructure implements them. `Logger` (SLF4J) is the canonical example.
