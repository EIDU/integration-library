package com.eidu.content.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunContentUnitRequestTest {
    String contentId = "content id";
    String contentUnitRunId = "content unit run id";
    String learnerId = "";
    String schoolId = "";
    String stage = "";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void fromPlainDataReturnsRequest() {
        RunContentUnitRequest request =
                RunContentUnitRequest.fromPlainData(
                        contentId,
                        contentUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs);

        assertEquals(contentId, request.contentId);
        assertEquals(contentUnitRunId, request.contentUnitRunId);
        assertEquals(learnerId, request.learnerId);
        assertEquals(schoolId, request.schoolId);
        assertEquals(stage, request.stage);
        assertEquals(remainingForegroundTimeInMs, request.remainingForegroundTimeInMs);
        assertEquals(inactivityTimeoutInMs, request.inactivityTimeoutInMs);
    }
}
