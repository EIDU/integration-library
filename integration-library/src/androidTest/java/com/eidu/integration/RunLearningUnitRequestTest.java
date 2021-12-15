package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Context;
import android.net.Uri;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class RunLearningUnitRequestTest {
    String learningUnitId = "learning unit id";
    String learningUnitRunId = "learning unit run id";
    String learnerId = "learner id";
    String schoolId = "school id";
    String stage = "stage";
    Long remainingForegroundTimeInMs = 1L;
    Long inactivityTimeoutInMs = 2L;

    @Test
    public void createRequestFromIntent() {
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
        RunLearningUnitRequest requestFromIntent =
                RunLearningUnitRequest.fromIntent(request.toIntent("package", "ActivityClass"));

        assertEquals(request, requestFromIntent);
    }

    @Test
    public void retrievesAsset() throws IOException {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        inactivityTimeoutInMs,
                        Uri.parse("content://authority/assets?unitId=5"));

        Uri expectedUri = Uri.parse("content://authority/assets/path?unitId=5");
        String expectedContent = "content";

        Context context =
                TestUtil.contextWithMockResolver("authority", expectedUri, expectedContent);

        assertEquals(expectedUri, request.getAssetAsUri("path"));
        assertEquals(
                expectedContent,
                TestUtil.readLine(request.getAssetAsFileDescriptor(context, "path")));
        assertEquals(expectedContent, TestUtil.readLine(request.getAssetAsStream(context, "path")));
    }
}
