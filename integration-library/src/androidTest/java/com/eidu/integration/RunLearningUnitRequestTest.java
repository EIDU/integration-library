package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import android.content.Context;
import android.content.Intent;
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
    public void doesNotInstantiateFromIntentWithDifferentAction() {
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
                RunLearningUnitRequest.fromIntent(
                        request.toIntent("package", "ActivityClass").setAction("DIFFERENT"));

        assertNull(requestFromIntent);
    }

    @Test
    public void returnsNullValueForRemainingForegroundTime() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        null,
                        inactivityTimeoutInMs,
                        null);

        RunLearningUnitRequest requestFromIntent =
                RunLearningUnitRequest.fromIntent(request.toIntent("package", "ActivityClass"));

        assertNotNull(requestFromIntent);
        assertNull(requestFromIntent.remainingForegroundTimeInMs);
    }

    @Test
    public void returnsNullValueForInactivityTimeout() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        remainingForegroundTimeInMs,
                        null,
                        null);

        RunLearningUnitRequest requestFromIntent =
                RunLearningUnitRequest.fromIntent(request.toIntent("package", "ActivityClass"));

        assertNotNull(requestFromIntent);
        assertNull(requestFromIntent.inactivityTimeoutInMs);
    }

    @Test
    public void throwsIllegalArgumentExceptionForMissingExtra() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        null,
                        null,
                        null);

        Intent intent = request.toIntent("package", "ActivityClass");
        intent.removeExtra("learningUnitId");

        assertThrows(
                IllegalArgumentException.class, () -> RunLearningUnitRequest.fromIntent(intent));
    }

    @Test
    public void throwsIllegalArgumentExceptionForInvalidLongExtra() {
        RunLearningUnitRequest request =
                RunLearningUnitRequest.of(
                        learningUnitId,
                        learningUnitRunId,
                        learnerId,
                        schoolId,
                        stage,
                        null,
                        null,
                        null);

        Intent intent = request.toIntent("package", "ActivityClass");
        intent.putExtra("remainingForegroundTimeInMs", "INVALID");

        assertThrows(
                IllegalArgumentException.class, () -> RunLearningUnitRequest.fromIntent(intent));
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
        assertEquals(expectedContent, TestUtil.readLine(request.getAssetAsStream(context, "path")));
    }
}
