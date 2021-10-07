package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunLearningUnitRequestTest {
    String contentId = "content id";
    String contentUnitRunId = "content unit run id";
    String learnerId = "learner id";
    String schoolId = "school id";
    String stage = "stage";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void createRequestFromIntent() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        contentId,
                        contentUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs);
        RunLearningUnitRequest requestFromIntent =
                RunLearningUnitRequest.fromIntent(request.toIntent("content.app.launch.ACTION"));

        assertEquals(requestFromIntent, request);
    }
}
