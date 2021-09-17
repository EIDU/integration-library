package com.eidu.content.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunContentUnitRequestTest {
    String contentId = "content id";
    String contentUnitRunId = "content unit run id";
    String learnerId = "learner id";
    String schoolId = "school id";
    String stage = "stage";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void createRequestFromCreatedLaunchIntent() {
        RunContentUnitRequest request =
                RunContentUnitRequest.fromPlainData(
                        contentId,
                        contentUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs);
        RunContentUnitRequest requestFromIntent =
                RunContentUnitRequest.fromLaunchIntent(request.toLaunchIntent("content.app.launch.ACTION"));

        assertEquals(requestFromIntent, request);
    }
}
