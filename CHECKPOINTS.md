# CHECKPOINTS — Final State Evaluation

> In multi-agent systems you don't evaluate the journey, you evaluate the destination.
> These are the objective checkpoints that a judge (human or AI) can use
> to decide whether the project is healthy.

## C1 — The harness is complete

- [ ] The 4 base files exist: `CLAUDE.md`, `init.sh`, `feature_list.json`,
      `progress/current.md`.
- [ ] The 3 docs exist: `docs/architecture.md`, `docs/conventions.md`,
      `CHECKPOINTS.md`.
- [ ] The 3 agents exist: `.claude/agents/leader.md`,
      `.claude/agents/implementer.md`, `.claude/agents/reviewer.md`.
- [ ] `./init.sh` exits with code 0.

## C2 — State is coherent

- [ ] At most one feature in `in_progress` in `feature_list.json`.
- [ ] Every `done` feature has associated tests that pass.
- [ ] `progress/current.md` is empty or describes the active session
      (does not contain garbage from previous sessions).

## C3 — Code respects the architecture

- [ ] `src/main/java/com/mfe/java_agent/` only contains the packages defined
      in `docs/architecture.md` (`api`, `agent`, `tools`, `memory`, `guardrails`,
      `observability`, `prompts`, `domain`, `exception`, `config`).
- [ ] Dependency direction is correct: `api → agent → tools/memory`,
      `guardrails` wraps input/output, `observability` is cross-cutting but
      not coupled to business logic.
- [ ] No business logic in controllers or DTOs.
- [ ] No new external dependencies in `pom.xml` without justification in
      `docs/architecture.md` or in the feature plan.
- [ ] No stray `System.out.println()` for debug, no contextless TODOs,
      no commented-out blocks.

## C4 — Verification is real

- [ ] `src/test/java/` has at least one test per module in `src/main/java/`
      that was created or modified.
- [ ] Tests are deterministic, fast, and test behavior (not implementation).
      If filesystem is used, `@TempDir` or equivalent is used;
      no static mocking of `Files` or `Paths`.
- [ ] `./mvnw test` shows > 0 tests and all green.

## C5 — The session closed cleanly

- [ ] No suspicious untracked files (`*.tmp`, `*.log`, `*.class`,
      `target/` outside `.gitignore`).
- [ ] `progress/history.md` has an entry for the last session.
- [ ] The last worked feature is reflected in its correct state
      in `feature_list.json`.

---

**How to use this file:** a reviewer agent (`.claude/agents/reviewer.md`)
goes through each checkbox, marks `[x]` or `[ ]`, and rejects the session close
if any boxes remain empty in C1-C5.
