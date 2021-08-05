package com.eidu.content.launch;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

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
