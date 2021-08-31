package com.eidu.content.launch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LaunchDataTest {
    String contentId = "content id";
    String contentUnitRunId = "content unit run id";
    String learnerId = "learner id";
    String schoolId = "school id";
    String environment = "env";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void createLaunchDataFromCreatedLaunchIntent() {
        LaunchData launchData =
                LaunchData.fromPlainData(
                        contentId,
                        contentUnitRunId,
                        learnerId,
                        schoolId,
                        environment,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs);
        LaunchData launchDataFromIntent =
                LaunchData.fromLaunchIntent(launchData.toLaunchIntent("content.app.launch.ACTION"));

        assertEquals(launchDataFromIntent, launchData);
    }
}
