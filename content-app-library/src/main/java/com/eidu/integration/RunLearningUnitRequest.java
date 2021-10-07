package com.eidu.integration;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

/**
 * A RunContentUnitRequest is what the EIDU app uses to launch content from external content apps.
 *
 * <p>The EIDU app will send an intent to your app, identified by the intent action (See {@link
 * Intent#setAction(String)}) and include all data relevant for you to identify the content to run.
 *
 * <p>The intent sent also includes references to the learner and their school.
 *
 * <p>The easiest way to obtain the data provided by the Intent sent from EIDU is {@link
 * RunLearningUnitRequest#fromIntent(Intent)}, which will automatically identify and extract all
 * information included in {@link Intent#getExtras()}.
 *
 * <p>To facilitate testing of your app, you can create your own RunContentUnitRequest with {@link
 * RunLearningUnitRequest#of(String, String, String, String, String, Long, Long)} and convert it to
 * an Intent with {@link RunLearningUnitRequest#toIntent(String, String)}.
 */
public final class RunLearningUnitRequest {

    private static final int VERSION = 1;
    private static final String VERSION_EXTRA = "version";
    private static final String CONTENT_ID_EXTRA = "contentId";
    private static final String CONTENT_UNIT_RUN_ID = "contentUnitRunId";
    private static final String LEARNER_ID_EXTRA = "learnerId";
    private static final String SCHOOL_ID_EXTRA = "schoolId";
    private static final String STAGE_EXTRA = "stage";
    private static final String REMAINING_FOREGROUND_TIME_EXTRA = "remainingForegroundTimeInMs";
    private static final String INACTIVITY_TIMEOUT_EXTRA = "inactivityTimeoutInMs";
    private static final String ACTION_LAUNCH_CONTENT =
            "com.eidu.integration.LAUNCH_CONTENT";

    public final int version;

    /** An ID, defined by the content app, that uniquely identifies the unit of content to run. */
    @NonNull public final String contentId;

    /** A unique identifier of each content unit run, which may be used for reporting purposes. */
    @NonNull public final String contentUnitRunId;

    /**
     * The obfuscated ID of the learner who is playing the content. May be used for reporting
     * purposes.
     */
    @NonNull public final String learnerId;

    /**
     * The obfuscated ID of the school at which the content is being played. May be used for
     * reporting purposes.
     */
    @NonNull public final String schoolId;

    /**
     * Identifies the stage of the EIDU app, e.g. "test", "prod". May be used for reporting
     * purposes.
     */
    @NonNull public final String stage;

    /**
     * The maximum amount of time that this run may take. The content app must end the run with
     * {@link RunContentUnitResult.ResultType#TimeUp} after this amount of <i>foreground</i> time as
     * elapsed.
     */
    @Nullable public final Long remainingForegroundTimeInMs;

    /**
     * The time of user inactivity after which the content app must end the run with {@link
     * RunContentUnitResult.ResultType#TimeoutInactivity}.
     */
    @Nullable public final Long inactivityTimeoutInMs;

    private RunLearningUnitRequest(
            int version,
            @NonNull String contentId,
            @NonNull String contentUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String stage,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs) {
        this.version = version;
        this.contentId = contentId;
        this.contentUnitRunId = contentUnitRunId;
        this.learnerId = learnerId;
        this.schoolId = schoolId;
        this.stage = stage;
        this.remainingForegroundTimeInMs = remainingForegroundTimeInMs;
        this.inactivityTimeoutInMs = inactivityTimeoutInMs;
    }

    /**
     * Creates a new RunContentUnitRequest instance.
     *
     * <p>Use this to test your content app against what the EIDU app will send to launch content.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param contentUnitRunId <b>Required</b>, see {@link #contentUnitRunId}.
     * @param learnerId <b>Required</b>, see {@link #learnerId}.
     * @param schoolId <b>Required</b>, see {@link #schoolId}.
     * @param stage <b>Required</b>, see {@link #stage}.
     * @param remainingForegroundTimeInMs <i>Optional</i>, see {@link #remainingForegroundTimeInMs}.
     * @param inactivityTimeoutInMs <i>Optional</i>, see {@link #inactivityTimeoutInMs}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitRequest of(
            @NonNull String contentId,
            @NonNull String contentUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String stage,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs) {
        return new RunLearningUnitRequest(
                VERSION,
                contentId,
                contentUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs);
    }

    /**
     * Creates a new RunContentUnitRequest instance from the provided intent.
     *
     * <p>Use this on the intent sent to your content app by EIDU to get access to the necessary
     * data.
     *
     * @param intent <b>Required</b>, the intent sent from the EIDU app to your content app.
     * @return The new instance, initialized from the provided intent.
     * @throws IllegalArgumentException If the provided intent does not contain all required data.
     */
    @NonNull
    public static RunLearningUnitRequest fromIntent(@NonNull Intent intent)
            throws IllegalArgumentException {
        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = intent.getStringExtra(CONTENT_ID_EXTRA);
        String contentUnitRunId = intent.getStringExtra(CONTENT_UNIT_RUN_ID);
        String learnerId = intent.getStringExtra(LEARNER_ID_EXTRA);
        String schoolId = intent.getStringExtra(SCHOOL_ID_EXTRA);
        String stage = intent.getStringExtra(STAGE_EXTRA);
        Long remainingForegroundTimeInMs =
                intent.hasExtra(REMAINING_FOREGROUND_TIME_EXTRA)
                        ? intent.getLongExtra(REMAINING_FOREGROUND_TIME_EXTRA, 0)
                        : null;
        Long inactivityTimeoutInMs =
                intent.hasExtra(INACTIVITY_TIMEOUT_EXTRA)
                        ? intent.getLongExtra(INACTIVITY_TIMEOUT_EXTRA, 0)
                        : null;

        for (String field : new String[] {contentId, contentUnitRunId, learnerId, schoolId, stage})
            if (field == null || field.isEmpty())
                throw new IllegalArgumentException(
                        String.format(
                                "Invalid launch intent. A required field is missing. "
                                        + "[contentId: %s, contentUnitRunId: %s, learnerId: %s, schoolId: %s, "
                                        + "stage: %s]",
                                contentId, contentUnitRunId, learnerId, schoolId, stage));

        return new RunLearningUnitRequest(
                version,
                contentId,
                contentUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs);
    }

    /**
     * Creates an implicit intent usable to launch a content app.
     *
     * <p>You can use this method, along with {@link RunLearningUnitRequest#of(String, String,
     * String, String, String, Long, Long)} to test your app.
     *
     * @param contentAppLaunchAction The action uniquely identifying your content app.
     * @return An intent for the given action, containing all the launch information.
     */
    @NonNull
    public Intent toIntent(@NonNull String contentAppLaunchAction) {
        return addExtras(new Intent(contentAppLaunchAction));
    }

    /**
     * Creates an explicit intent usable to launch a content app.
     *
     * <p>You can use this method, along with {@link RunLearningUnitRequest#of(String, String,
     * String, String, String, Long, Long)} to test your app.
     *
     * @param packageName The package name of the content app.
     * @param className The class name of the activity to launch with this intent
     * @return An intent for the given package and class, containing all the launch information.
     */
    @NonNull
    public Intent toIntent(@NonNull String packageName, @NonNull String className) {
        Intent intent = new Intent(ACTION_LAUNCH_CONTENT);
        intent.setClassName(packageName, className);
        return addExtras(intent);
    }

    @NonNull
    private Intent addExtras(@NonNull Intent intent) {
        intent.putExtra(VERSION_EXTRA, version)
                .putExtra(CONTENT_ID_EXTRA, contentId)
                .putExtra(CONTENT_UNIT_RUN_ID, contentUnitRunId)
                .putExtra(LEARNER_ID_EXTRA, learnerId)
                .putExtra(SCHOOL_ID_EXTRA, schoolId)
                .putExtra(STAGE_EXTRA, stage);
        intent.putExtra(REMAINING_FOREGROUND_TIME_EXTRA, remainingForegroundTimeInMs);
        intent.putExtra(INACTIVITY_TIMEOUT_EXTRA, inactivityTimeoutInMs);
        return intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunLearningUnitRequest that = (RunLearningUnitRequest) o;
        return version == that.version
                && contentId.equals(that.contentId)
                && contentUnitRunId.equals(that.contentUnitRunId)
                && learnerId.equals(that.learnerId)
                && schoolId.equals(that.schoolId)
                && stage.equals(that.stage)
                && Objects.equals(remainingForegroundTimeInMs, that.remainingForegroundTimeInMs)
                && Objects.equals(inactivityTimeoutInMs, that.inactivityTimeoutInMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                contentId,
                contentUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs);
    }
}
