# AGENTS.md — Navigation map for AI agents

> This file is the **entry point** for any agent working in this repository.
> It is NOT a rule bible: it is a **map**. Read only what you need when you
> need it (progressive disclosure).

---

## 1. Before starting (mandatory)

1. Run `./init.sh` and verify it exits without errors. If it fails, **stop**
   and fix the environment before touching any code.
2. Read `progress/current.md` to understand the state of the last session.
3. Read `feature_list.json` and pick **one** task with status `pending`. Never
   work on more than one at a time.

## 2. Repository map

| File / folder                | What it contains                                              | When to read it |
|------------------------------|---------------------------------------------------------------|-----------------|
| `feature_list.json`          | Task list with status (pending / in_progress / done)          | Always, at the start |
| `progress/current.md`        | Current session state                                         | Always, at the start |
| `progress/history.md`        | Append-only log of previous sessions                          | If you need historical context |
| `docs/architecture.md`       | What "doing a good job" means in this project                 | Before implementing |
| `docs/conventions.md`        | Style rules, naming, structure                                | Before writing code |
| `CHECKPOINTS.md`             | Objective criteria for "correct final state"                  | For self-evaluation |
| `.claude/agents/`            | Subagent definitions (leader, implementer, reviewer)           | If orchestrating work |
| `src/main/java/`             | Application source code                                       | To implement |
| `src/test/java/`             | Automated tests                                               | To verify |
| `src/main/resources/prompts/`| Externalized prompt templates (.st files)                     | When modifying system prompts |

## 3. Hard rules (non-negotiable)

- **One feature at a time.** Don't mix changes from multiple tasks in the same session.
- **Don't declare a task `done` without green tests.** Run `./init.sh` and
  make sure the test block passes at 100%.
- **Document what you do** in `progress/current.md` while you work, not at the end.
- **Leave the repository clean** before closing the session (see §5).
- **If you don't know something, look in `docs/`** before inventing it.

## 4. How to pick a task

```
1. Open feature_list.json
2. Filter by status == "pending"
3. Pick the one with the lowest "id"
4. Change its status to "in_progress" and save
5. Note in progress/current.md: feature, start time, brief plan
```

## 5. Session close (lifecycle)

Before finishing:

1. Run `./init.sh` — all green.
2. Run `./mvnw test` — all tests pass.
3. If the task is finished: mark `status: "done"` in `feature_list.json`.
4. Move the summary from `progress/current.md` to the end of `progress/history.md`.
5. Empty `progress/current.md` leaving only the template.
6. Don't leave temporary files, `System.out.println()` debug statements, or
   contextless TODOs behind.

## 6. If you get stuck

- Re-read the relevant section of `docs/`.
- If the tool doesn't do what you expect, **don't invent a workaround**:
  document the blockage in `progress/current.md` and stop the session.
