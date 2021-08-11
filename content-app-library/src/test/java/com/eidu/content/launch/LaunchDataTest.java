package com.eidu.content.launch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LaunchDataTest {

    @Test
    public void fromPlainDataReturnsLaunchData() {
        String contentId = "content id";
        String contentUnitRunId = "content unit run id";
        String learnerId = "";
        String schoolId = "";
        String environment = "";
        Long remainingForegroundTimeInMs = 1L;
        Long inactivityTimeoutInMs = 2L;

        LaunchData launchData = LaunchData.fromPlainData(
            contentId,
            contentUnitRunId,
            learnerId,
            schoolId,
            environment,
            remainingForegroundTimeInMs,
            inactivityTimeoutInMs
        );

        assertEquals(contentId, launchData.getContentId());
        assertEquals(contentUnitRunId, launchData.getContentUnitRunId());
        assertEquals(learnerId, launchData.getLearnerId());
        assertEquals(schoolId, launchData.getSchoolId());
        assertEquals(environment, launchData.getEnvironment());
        assertEquals(remainingForegroundTimeInMs, launchData.getRemainingForegroundTimeInMs());
        assertEquals(inactivityTimeoutInMs, launchData.getInactivityTimeoutInMs());
    }
}