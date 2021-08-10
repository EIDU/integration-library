package com.eidu.content.result;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LaunchResultData {

    public static final int VERSION = 1;
    public static final String VERSION_EXTRA = "version";
    public static final String CONTENT_ID_EXTRA = "contentId";
    public static final String RUN_CONTENT_UNIT_RESULT = "runContentUnitResult";
    public static final String SCORE_EXTRA = "score";
    public static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    public static final String ADDITIONAL_DATA_EXTRA = "additionalData";

    private final int version;
    @NonNull
    private final String contentId;
    @NonNull
    private final RunContentUnitResult runContentUnitResult;
    @NonNull
    private final Float score;
    @NonNull
    private final Long foregroundDurationInMs;
    @Nullable
    private final String additionalData;

    public static LaunchResultData fromPlainData(
            @NonNull String contentId,
            @NonNull RunContentUnitResult runContentUnitResult,
            @NonNull Float score,
            @NonNull Long foregroundDurationInMs,
            @Nullable String additionalData
    ) {
        return new LaunchResultData(
                VERSION,
                contentId,
                runContentUnitResult,
                score,
                foregroundDurationInMs,
                additionalData
        );
    }

    public static LaunchResultData fromResultIntent(@NonNull Intent resultIntent) {
        int version = resultIntent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = resultIntent.getStringExtra(CONTENT_ID_EXTRA);
        RunContentUnitResult runContentUnitResult =
                RunContentUnitResult.nullableValueOf(resultIntent.getStringExtra(RUN_CONTENT_UNIT_RESULT));
        Float score = null;
        if (resultIntent.hasExtra(SCORE_EXTRA)) {
            score = resultIntent.getFloatExtra(SCORE_EXTRA, 0.f);
        }
        Long foregroundDurationInMs = null;
        if (resultIntent.hasExtra(FOREGROUND_DURATION_EXTRA)) {
            foregroundDurationInMs = resultIntent.getLongExtra(FOREGROUND_DURATION_EXTRA, 0);
        }
        String additionalData = null;
        if (resultIntent.hasExtra(ADDITIONAL_DATA_EXTRA)) {
            additionalData = resultIntent.getStringExtra(ADDITIONAL_DATA_EXTRA);
        }

        if (contentId == null || contentId.isEmpty() || runContentUnitResult == null || score == null
                || foregroundDurationInMs == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. " +
                                    "[contentId: %s, runContentUnitResult: %s score: %f, foregroundDurationInMs: %d]",
                            contentId, runContentUnitResult, score, foregroundDurationInMs
                    )
            );
        }

        return new LaunchResultData(
                version,
                contentId,
                runContentUnitResult,
                score,
                foregroundDurationInMs,
                additionalData
        );
    }

    public Intent toResultIntent() {
        return new Intent()
            .putExtra(VERSION_EXTRA, version)
            .putExtra(CONTENT_ID_EXTRA, contentId)
            .putExtra(RUN_CONTENT_UNIT_RESULT, runContentUnitResult.name())
            .putExtra(SCORE_EXTRA, score)
            .putExtra(FOREGROUND_DURATION_EXTRA, foregroundDurationInMs)
            .putExtra(ADDITIONAL_DATA_EXTRA, additionalData);
    }

    private LaunchResultData(
            int version,
            @NonNull String contentId,
            @NonNull RunContentUnitResult runContentUnitResult,
            @NonNull Float score,
            @NonNull Long foregroundDurationInMs,
            @Nullable String additionalData
    ) {
        this.version = version;
        this.contentId = contentId;
        this.runContentUnitResult = runContentUnitResult;
        this.score = score;
        this.foregroundDurationInMs = foregroundDurationInMs;
        this.additionalData = additionalData;
    }

    public enum RunContentUnitResult {
        Success,
        Abort,
        Error,
        TimeoutInactivity,
        TimeUp;

        static RunContentUnitResult nullableValueOf(String value) {
            try {
                return RunContentUnitResult.valueOf(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public int getVersion() {
        return version;
    }

    @NonNull
    public String getContentId() {
        return contentId;
    }

    @NonNull
    public RunContentUnitResult getRunContentUnitResult() {
        return runContentUnitResult;
    }

    @NonNull
    public Float getScore() {
        return score;
    }

    @NonNull
    public Long getForegroundDurationInMs() {
        return foregroundDurationInMs;
    }

    @Nullable
    public String getAdditionalData() {
        return additionalData;
    }
}
