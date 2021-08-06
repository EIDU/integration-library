package com.eidu.content.launch;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

/**
 * LaunchData is what the EIDU app uses to launch content from external content apps.
 *
 * The EIDU app will send an intent to your app, identified by the intent action (See {@link Intent#setAction(String)})
 * and include all data relevant for you to identify the content to play.
 *
 * The intent sent also includes references to the learner and their school.
 *
 * The easiest way to obtain the data provided by the Intent sent from EIDU is
 * {@link LaunchData#fromLaunchIntent(Intent)}, which will automatically identify and extract all
 * information included in {@link Intent#getExtras()}.
 *
 * To facilitate testing of your app you can create your own LaunchData with
 * {@link LaunchData#fromPlainData(String, String, String, String, String, Long, Long)} and converting it to an Intent
 * with {@link LaunchData#toLaunchIntent(String)}.
 */
public class LaunchData {

    public static final int VERSION = 1;
    public static final String VERSION_EXTRA = "version";
    public static final String CONTENT_ID_EXTRA = "contentId";
    public static final String WORK_UNIT_RUN_ID_EXTRA = "workUnitRunId";
    public static final String LEARNER_ID_EXTRA = "learnerId";
    public static final String SCHOOL_ID_EXTRA = "schoolId";
    public static final String ENVIRONMENT_EXTRA = "environment";
    public static final String REMAINING_FOREGROUND_TIME_EXTRA = "remainingForegroundTimeInMs";
    public static final String INACTIVITY_TIMEOUT_EXTRA = "inactivityTimeoutInMs";

    private final int version;
    @NonNull
    private final String contentId;
    @NonNull
    private final String workUnitRunId;
    @NonNull
    private final String learnerId;
    @NonNull
    private final String schoolId;
    @NonNull
    private final String environment;
    @Nullable
    private final Long remainingForegroundTimeInMs;
    @Nullable
    private final Long inactivityTimeoutInMs;

    /**
     * Create a new LaunchData instance from plain data.
     *
     * Use this to test your content app against what the EIDU app will send to launch content.
     *
     * @param contentId <b>Required</b> uniquely identifies the content to launch in your app
     * @param workUnitRunId <b>Required</b> unique identifier of each content run
     * @param learnerId <b>Required</b> unique identifier of the learner
     * @param schoolId <b>Required</b> unique identifier of the school
     * @param environment <b>Required</b> identifies the environment of this launch, e.g. "test", "prod"
     * @param remainingForegroundTimeInMs <i>Optional</i> session time remaining for this run
     * @param inactivityTimeoutInMs <i>Optional</i> time after which your content should return when the learner is
     *                              inactive
     * @return an instance of LaunchData
     */
    @NonNull
    public static LaunchData fromPlainData(
            @NonNull String contentId,
            @NonNull String workUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String environment,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs
    ) {
        return new LaunchData(
                VERSION,
                contentId,
                workUnitRunId,
                learnerId,
                schoolId,
                environment,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs
        );
    }

    /**
     * Creates an instance of LaunchData from the provided intent's extras.
     *
     * Use this on the intent sent to your content app by EIDU to get access to the necessary data.
     *
     * @param intent <b>Required</b> the intent sent from the EIDU app to your content app
     * @return LaunchData initialized from the provided intent
     * @throws IllegalArgumentException If the provided intent does not contain all required data
     */
    @NonNull
    public static LaunchData fromLaunchIntent(@NonNull Intent intent) throws IllegalArgumentException {
        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = intent.getStringExtra(CONTENT_ID_EXTRA);
        String workUnitRunId = intent.getStringExtra(WORK_UNIT_RUN_ID_EXTRA);
        String learnerId = intent.getStringExtra(LEARNER_ID_EXTRA);
        String schoolId = intent.getStringExtra(SCHOOL_ID_EXTRA);
        String environment = intent.getStringExtra(ENVIRONMENT_EXTRA);
        Long remainingForegroundTimeInMs = null;
        if (intent.hasExtra(REMAINING_FOREGROUND_TIME_EXTRA)) {
            remainingForegroundTimeInMs = intent.getLongExtra(REMAINING_FOREGROUND_TIME_EXTRA, 0);
        }
        Long inactivityTimeoutInMs = null;
        if (intent.hasExtra(INACTIVITY_TIMEOUT_EXTRA)) {
            remainingForegroundTimeInMs = intent.getLongExtra(INACTIVITY_TIMEOUT_EXTRA, 0);
        }

        for (String field : Arrays.asList(contentId, workUnitRunId, learnerId, schoolId, environment)) {
            if (field == null || field.isEmpty()) {
                throw new IllegalArgumentException(
                        String.format("Invalid launch intent. A required field is missing. " +
                                "[contentId: %s, workUnitRunId: %s, learnerId: %s, schoolId: %s, " +
                                "environment: %s]", contentId, workUnitRunId, learnerId, schoolId, environment)
                );
            }
        }

        return new LaunchData(
                version,
                contentId,
                workUnitRunId,
                learnerId,
                schoolId,
                environment,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs
        );
    }

    /**
     * Create an intent usable to launch a content app.
     *
     * You can use this method, along with
     * {@link LaunchData#fromPlainData(String, String, String, String, String, Long, Long)} to test
     * your app.
     *
     * @param contentAppLaunchAction the action uniquely identifying your content app
     * @return An intent containing all launch data and the defined action
     */
    @NonNull
    public Intent toLaunchIntent(@NonNull String contentAppLaunchAction) {
        Intent launchIntent = new Intent(contentAppLaunchAction)
                .putExtra(VERSION_EXTRA, getVersion())
                .putExtra(CONTENT_ID_EXTRA, getContentId())
                .putExtra(WORK_UNIT_RUN_ID_EXTRA, getWorkUnitRunId())
                .putExtra(LEARNER_ID_EXTRA, getLearnerId())
                .putExtra(SCHOOL_ID_EXTRA, getSchoolId())
                .putExtra(ENVIRONMENT_EXTRA, getEnvironment());
        if (getRemainingForegroundTimeInMs() != null) {
            launchIntent.putExtra(REMAINING_FOREGROUND_TIME_EXTRA, remainingForegroundTimeInMs);
        }
        if (getInactivityTimeoutInMs() != null) {
            launchIntent.putExtra(INACTIVITY_TIMEOUT_EXTRA, inactivityTimeoutInMs);
        }
        return launchIntent;

    }

    public int getVersion() {
        return version;
    }

    @NonNull
    public String getContentId() {
        return contentId;
    }

    @NonNull
    public String getWorkUnitRunId() {
        return workUnitRunId;
    }

    @NonNull
    public String getLearnerId() {
        return learnerId;
    }

    @NonNull
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    public String getEnvironment() {
        return environment;
    }

    @Nullable
    public Long getRemainingForegroundTimeInMs() {
        return remainingForegroundTimeInMs;
    }

    @Nullable
    public Long getInactivityTimeoutInMs() {
        return inactivityTimeoutInMs;
    }

    private LaunchData(
            int version,
            @NonNull String contentId,
            @NonNull String workUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String environment,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs
    ) {
        this.version = version;
        this.contentId = contentId;
        this.workUnitRunId = workUnitRunId;
        this.learnerId = learnerId;
        this.schoolId = schoolId;
        this.environment = environment;
        this.remainingForegroundTimeInMs = remainingForegroundTimeInMs;
        this.inactivityTimeoutInMs = inactivityTimeoutInMs;
    }
}
