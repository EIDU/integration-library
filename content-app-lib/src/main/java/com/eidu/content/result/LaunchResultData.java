package com.eidu.content.result;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchResultData {

    public static final int VERSION = 1;
    public static final String VERSION_EXTRA = "version";
    public static final String CONTENT_ID_EXTRA = "contentId";
    public static final String RUN_WU_RESULT_EXTRA = "runWuResult";
    public static final String SCORE_EXTRA = "score";
    public static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    public static final String ADDITIONAL_DATA_EXTRA = "additionalData";

    private final int version;
    @NonNull
    private final String contentId;
    @NonNull
    private final RunWuResult runWuResult;
    @NonNull
    private final Float score;
    @NonNull
    private final Long foregroundDurationInMs;
    @NonNull
    private final ArrayList<String> additionalData;

    public static LaunchResultData fromPlainData(
            @NonNull String contentId,
            @NonNull RunWuResult runWuResult,
            @NonNull Float score,
            @NonNull Long foregroundDurationInMs,
            @NonNull ArrayList<String> additionalData
    ) {
        return new LaunchResultData(
                VERSION,
                contentId,
                runWuResult,
                score,
                foregroundDurationInMs,
                additionalData
        );
    }

    public static LaunchResultData fromResultIntent(@NonNull Intent resultIntent) {
        int version = resultIntent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = resultIntent.getStringExtra(CONTENT_ID_EXTRA);
        RunWuResult runWuResult = RunWuResult.nullableValueOf(resultIntent.getStringExtra(RUN_WU_RESULT_EXTRA));
        Float score = null;
        if (resultIntent.hasExtra(SCORE_EXTRA)) {
            score = resultIntent.getFloatExtra(SCORE_EXTRA, 0.f);
        }
        Long foregroundDurationInMs = null;
        if (resultIntent.hasExtra(FOREGROUND_DURATION_EXTRA)) {
            foregroundDurationInMs = resultIntent.getLongExtra(FOREGROUND_DURATION_EXTRA, 0);
        }
        ArrayList<String> additionalData = new ArrayList<>();
        if (resultIntent.hasExtra(ADDITIONAL_DATA_EXTRA)) {
            additionalData = resultIntent.getStringArrayListExtra(ADDITIONAL_DATA_EXTRA);
        }

        if (contentId == null || contentId.isEmpty() || runWuResult == null || score == null
                || foregroundDurationInMs == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. " +
                                    "[contentId: %s, runWuResult: %s score: %f, foregroundDurationInMs: %d]",
                            contentId, runWuResult, score, foregroundDurationInMs
                    )
            );
        }

        return new LaunchResultData(
                version,
                contentId,
                runWuResult,
                score,
                foregroundDurationInMs,
                additionalData
        );
    }

    public Intent toResultIntent() {
        return new Intent()
            .putExtra(VERSION_EXTRA, version)
            .putExtra(CONTENT_ID_EXTRA, contentId)
            .putExtra(RUN_WU_RESULT_EXTRA, runWuResult.name())
            .putExtra(SCORE_EXTRA, score)
            .putExtra(FOREGROUND_DURATION_EXTRA, foregroundDurationInMs)
            .putStringArrayListExtra(ADDITIONAL_DATA_EXTRA, additionalData);
    }

    private LaunchResultData(
            int version,
            @NonNull String contentId,
            @NonNull RunWuResult runWuResult,
            @NonNull Float score,
            @NonNull Long foregroundDurationInMs,
            @NonNull ArrayList<String> additionalData
    ) {
        this.version = version;
        this.contentId = contentId;
        this.runWuResult = runWuResult;
        this.score = score;
        this.foregroundDurationInMs = foregroundDurationInMs;
        this.additionalData = new ArrayList<>(additionalData);
    }

    public enum RunWuResult {
        Success,
        Abort,
        Error,
        TimeoutInactivity,
        TimeUp;

        static RunWuResult nullableValueOf(String value) {
            try {
                return RunWuResult.valueOf(value);
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
    public RunWuResult getRunWuResult() {
        return runWuResult;
    }

    @NonNull
    public Float getScore() {
        return score;
    }

    @NonNull
    public Long getForegroundDurationInMs() {
        return foregroundDurationInMs;
    }

    @NonNull
    public List<String> getAdditionalData() {
        return Collections.unmodifiableList(additionalData);
    }
}
