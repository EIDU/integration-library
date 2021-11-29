package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunLearningUnitRequestTest {
    String learningUnitId = "learning unit id";
    String learningUnitRunId = "learning unit run id";
    String learnerId = "";
    String schoolId = "";
    String stage = "";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void constructsRequest() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs,
                        null);

        assertEquals(learningUnitId, request.learningUnitId);
        assertEquals(learningUnitRunId, request.learningUnitRunId);
        assertEquals(learnerId, request.learnerId);
        assertEquals(schoolId, request.schoolId);
        assertEquals(stage, request.stage);
        assertEquals(remainingForegroundTimeInMs, request.remainingForegroundTimeInMs);
        assertEquals(inactivityTimeoutInMs, request.inactivityTimeoutInMs);
    }
}
