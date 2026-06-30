# Java AI Agent

A Spring Boot-based AI agent platform with ReAct pattern orchestration, tool integration, and observability.

## Overview

<!-- Brief description of what this project does and why it exists -->

## Features

- 🧠 **ReAct Agent** — reasoning-and-acting loop with tool orchestration
- 🛠️ **Pluggable Tools** — extensible tool registry for LLM function calling
- 📝 **Externalized Prompts** — template-based prompt management with StringTemplate
- 🛡️ **Guardrails** — input/output/tool execution safety checks
- 📊 **Observability** — OpenTelemetry tracing, Micrometer metrics, structured logging
- 🧠 **Memory Management** — short-term conversation history and long-term vector store (RAG)
- 🌐 **REST + SSE** — standard JSON endpoints and Server-Sent Events for streaming

## Prerequisites

- Java 21+
- Maven 4.0+
- (Optional) Docker for local LLM or vector store

## Getting Started

### Clone the repository

```bash
git clone <repo-url>
cd java-agent
```

### Build

```bash
./mvnw clean package
```

### Run

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The application will be available at `http://localhost:8080`.

## Project Structure

```
src/main/java/com/yourcompany/aiagent/
├── config/                     # Configuration & Beans
├── api/                        # REST controllers and DTOs
├── agent/                      # Core agent orchestration (ReAct loop)
│   ├── core/
│   ├── state/
│   └── planner/
├── tools/                      # Tool registry and implementations
│   ├── registry/
│   └── implementations/
├── memory/                     # Conversation history and vector store
├── prompts/                    # Prompt template management
├── guardrails/                 # Security and safety checks
├── observability/              # Metrics, logs, and tracing
├── domain/                     # Internal domain models
└── exception/                  # Global error handling
```

## Configuration

Configuration is managed via Spring profiles:

| Profile | File | Purpose |
|---------|------|---------|
| `dev` | `application-dev.yml` | Local development (mock tools, local LLM) |
| `prod` | `application-prod.yml` | Production (real LLM, strict security) |

Key configuration areas:

- **LLM** — model provider, API keys, temperature, token limits
- **Security** — OAuth2 client registration, CORS settings
- **Observability** — OpenTelemetry exporter endpoint, sampling rate

## API Endpoints

<!-- Document the main REST endpoints here -->

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/agent/chat` | Send a chat message (JSON) |
| `GET` | `/api/agent/chat/stream` | Send a chat message (SSE streaming) |

## Tools

<!-- List available tools and their descriptions -->

## Testing

```bash
./mvnw test
```

## Deployment

<!-- Deployment instructions (Docker, Kubernetes, etc.) -->

## Contributing

<!-- Contribution guidelines -->

## License

<!-- License information -->
