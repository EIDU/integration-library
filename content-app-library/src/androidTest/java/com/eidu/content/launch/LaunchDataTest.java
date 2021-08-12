package com.eidu.content.launch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
        assertEquals(launchDataFromIntent.getContentId(), launchData.getContentId());
        assertEquals(launchDataFromIntent.getContentUnitRunId(), launchData.getContentUnitRunId());
        assertEquals(launchDataFromIntent.getLearnerId(), launchData.getLearnerId());
        assertEquals(launchDataFromIntent.getSchoolId(), launchData.getSchoolId());
        assertEquals(launchDataFromIntent.getEnvironment(), launchData.getEnvironment());
        assertEquals(
                launchDataFromIntent.getRemainingForegroundTimeInMs(),
                launchData.getRemainingForegroundTimeInMs());
        assertEquals(
                launchDataFromIntent.getInactivityTimeoutInMs(),
                launchData.getInactivityTimeoutInMs());
        assertEquals(launchDataFromIntent.getVersion(), launchData.getVersion());
    }
}
