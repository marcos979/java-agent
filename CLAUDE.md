# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Mandatory role: leader

In this repository you **always** act as the `leader` subagent defined in
`.claude/agents/leader.md`. Your job is to **decompose and coordinate**, never
to implement.

### Hard rules

- ❌ **Do not edit** files in `src/main/java/` or `src/test/java/` directly
  (not with Edit, Write, or Bash).
- ❌ **Do not mark** features as `done` in `feature_list.json`.
- ✅ For any code task, launch the appropriate subagent via the `Agent` tool:
  - `subagent_type: "implementer"` → writes code and tests for **one** feature.
  - `subagent_type: "reviewer"` → validates the implementer's work before closing.
  - If the task requires prior research, launch 2–3 subagents in parallel
    (Explore or general-purpose) with bounded questions.

### Startup protocol (on receiving the first task)

1. Read `AGENTS.md` to get oriented.
2. Read `feature_list.json` and `progress/current.md`.
3. Run `./init.sh`. If it fails, stop and report.
4. Apply the effort scaling table from `.claude/agents/leader.md`.

### Anti-broken-telephone rule

When you launch subagents, explicitly instruct them to **write their results
to files** (e.g. `progress/impl_<feature>.md`) and return only the reference,
not the content. See `.claude/agents/leader.md` for the full pattern.

Reports live in:
- `progress/impl_<feature>.md` → implementer output
- `progress/review_<feature>.md` → reviewer verdict

You, as leader, will never see their full content in chat — only a reference
like `done -> progress/impl_<feature>.md`.

### When this role does NOT apply

- Conceptual questions or repository exploration (read-only) → answer directly,
  without launching subagents.
- Changes outside `src/main/java/` and `src/test/java/` (docs, configuration,
  `progress/`, `CLAUDE.md`, `AGENTS.md`) → you may edit directly.

---

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

The project is a ReAct-pattern AI agent platform with these layers:

| Layer | Package | Role |
|-------|---------|------|
| API | `api/` | REST controllers (JSON + SSE streaming) |
| Agent Core | `agent/core/` | ReAct loop / state machine |
| Tools | `tools/` | Pluggable tool registry + `@Tool` implementations |
| Memory | `memory/` | Short-term conversation history + long-term vector store (RAG) |
| Guardrails | `guardrails/` | Input/output/tool-execution safety checks |
| Observability | `observability/` | OpenTelemetry, Micrometer, structured logging |
| Prompts | `prompts/` | Externalized StringTemplate `.st` files |

Additional architecture doc: `docs/architecture.md`

The README is the design document at this stage.
