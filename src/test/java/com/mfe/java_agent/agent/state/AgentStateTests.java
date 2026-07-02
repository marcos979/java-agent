package com.mfe.java_agent.agent.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

class AgentStateTests {

    // -- AgentContext tests --

    @Test
    void contextShouldAutoGenerateTraceId() {
        AgentContext ctx = new AgentContext();
        assertThat(ctx.getTraceId()).isNotNull();
        // traceId must be a valid UUID string
        assertThat(UUID.fromString(ctx.getTraceId())).isNotNull();
    }

    @Test
    void contextShouldGenerateUniqueTraceIds() {
        AgentContext ctx1 = new AgentContext();
        AgentContext ctx2 = new AgentContext();
        assertThat(ctx1.getTraceId()).isNotEqualTo(ctx2.getTraceId());
    }

    @Test
    void contextShouldInitializeAllFields() {
        AgentContext ctx = new AgentContext();

        assertThat(ctx.getTraceId()).isNotNull();
        assertThat(ctx.getSessionId()).isNull();
        assertThat(ctx.getMessageHistory()).isNotNull().isEmpty();
        assertThat(ctx.getToolCalls()).isNotNull().isEmpty();
        assertThat(ctx.getConversationHistory()).isNotNull().isEmpty();
        assertThat(ctx.getIterationCount()).isZero();
        assertThat(ctx.getMaxIterations()).isEqualTo(5);
    }

    @Test
    void contextGettersAndSettersShouldWork() {
        AgentContext ctx = new AgentContext();

        // sessionId
        ctx.setSessionId("session-123");
        assertThat(ctx.getSessionId()).isEqualTo("session-123");

        // messageHistory
        Message msg = new UserMessage("hello");
        ctx.setMessageHistory(List.of(msg));
        assertThat(ctx.getMessageHistory()).hasSize(1);
        assertThat(ctx.getMessageHistory().get(0).getText()).isEqualTo("hello");

        // toolCalls
        ctx.setToolCalls(Map.of("weather", "22C"));
        assertThat(ctx.getToolCalls()).hasSize(1);
        assertThat(ctx.getToolCalls().get("weather")).isEqualTo("22C");

        // iterationCount
        ctx.setIterationCount(3);
        assertThat(ctx.getIterationCount()).isEqualTo(3);
    }

    @Test
    void contextShouldAccumulateMessageHistory() {
        AgentContext ctx = new AgentContext();
        ctx.getMessageHistory().add(new UserMessage("first"));
        ctx.getMessageHistory().add(new UserMessage("second"));

        assertThat(ctx.getMessageHistory()).hasSize(2);
        assertThat(ctx.getMessageHistory().get(0).getText()).isEqualTo("first");
        assertThat(ctx.getMessageHistory().get(1).getText()).isEqualTo("second");
    }

    @Test
    void contextShouldAccumulateToolCalls() {
        AgentContext ctx = new AgentContext();
        ctx.getToolCalls().put("weather", Map.of("city", "London"));
        ctx.getToolCalls().put("database", Map.of("query", "SELECT *"));

        assertThat(ctx.getToolCalls()).hasSize(2);
    }

    // -- ExecutionResult tests --

    @Test
    void successShouldReturnCorrectValues() {
        ExecutionResult result = ExecutionResult.success("42", "trace-1", 5, 150);

        assertThat(result.finalAnswer()).isEqualTo("42");
        assertThat(result.traceId()).isEqualTo("trace-1");
        assertThat(result.iterationsUsed()).isEqualTo(5);
        assertThat(result.totalTokensConsumed()).isEqualTo(150);
        assertThat(result.terminationReason()).isEqualTo(ExecutionResult.TerminationReason.FINAL_ANSWER);
    }

    @Test
    void failureShouldReturnCorrectValues() {
        ExecutionResult result = ExecutionResult.failure("Guardrail blocked input", "trace-2", 1, 50);

        assertThat(result.finalAnswer()).isEqualTo("Guardrail blocked input");
        assertThat(result.traceId()).isEqualTo("trace-2");
        assertThat(result.iterationsUsed()).isEqualTo(1);
        assertThat(result.totalTokensConsumed()).isEqualTo(50);
        assertThat(result.terminationReason()).isEqualTo(ExecutionResult.TerminationReason.GUARDRAIL_INTERVENTION);
    }

    @Test
    void successAndFailureShouldBeDistinct() {
        ExecutionResult success = ExecutionResult.success("answer", "t1", 2, 100);
        ExecutionResult failure = ExecutionResult.failure("error", "t2", 1, 50);

        assertThat(success.terminationReason()).isNotEqualTo(failure.terminationReason());
        assertThat(success.finalAnswer()).isNotEqualTo(failure.finalAnswer());
        assertThat(success.traceId()).isNotEqualTo(failure.traceId());
    }

    @Test
    void recordShouldBeImmutable() {
        ExecutionResult result = ExecutionResult.success("immutable", "t", 1, 10);
        // Verify it's a record (canonical constructor, accessor methods)
        assertThat(result).isExactlyInstanceOf(ExecutionResult.class);
        // Compact or canonical constructor produced the right values
        assertThat(result.getClass().isRecord()).isTrue();
    }

    @Test
    void terminationReasonEnumShouldHaveExpectedValues() {
        assertThat(ExecutionResult.TerminationReason.values())
                .containsExactly(
                        ExecutionResult.TerminationReason.FINAL_ANSWER,
                        ExecutionResult.TerminationReason.MAX_ITERATIONS_REACHED,
                        ExecutionResult.TerminationReason.GUARDRAIL_INTERVENTION,
                        ExecutionResult.TerminationReason.TIMEOUT);
    }
}
