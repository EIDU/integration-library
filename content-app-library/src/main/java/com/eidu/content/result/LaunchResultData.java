package com.eidu.content.result;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

public class LaunchResultData {

    public static final int VERSION = 1;
    public static final String VERSION_EXTRA = "version";
    public static final String CONTENT_ID_EXTRA = "contentId";
    public static final String RUN_CONTENT_UNIT_RESULT = "runContentUnitResult";
    public static final String SCORE_EXTRA = "score";
    public static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    public static final String ADDITIONAL_DATA_EXTRA = "additionalData";

    public final int version;
    @NonNull public final String contentId;
    @NonNull public final RunContentUnitResult runContentUnitResult;
    public final float score;
    public final long foregroundDurationInMs;
    @Nullable public final String additionalData;

    @NonNull
    public static LaunchResultData fromPlainData(
            @NonNull String contentId,
            @NonNull RunContentUnitResult runContentUnitResult,
            float score,
            long foregroundDurationInMs) {
        return fromPlainData(contentId, runContentUnitResult, score, foregroundDurationInMs, null);
    }

    @NonNull
    public static LaunchResultData fromPlainData(
            @NonNull String contentId,
            @NonNull RunContentUnitResult runContentUnitResult,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new LaunchResultData(
                VERSION,
                contentId,
                runContentUnitResult,
                score,
                foregroundDurationInMs,
                additionalData);
    }

    @NonNull
    public static LaunchResultData fromResultIntent(@NonNull Intent resultIntent) {
        int version = resultIntent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = resultIntent.getStringExtra(CONTENT_ID_EXTRA);
        RunContentUnitResult runContentUnitResult =
                RunContentUnitResult.nullableValueOf(
                        resultIntent.getStringExtra(RUN_CONTENT_UNIT_RESULT));
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

        if (contentId == null
                || contentId.isEmpty()
                || runContentUnitResult == null
                || score == null
                || foregroundDurationInMs == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. "
                                    + "[contentId: %s, runContentUnitResult: %s score: %f, foregroundDurationInMs: %d]",
                            contentId, runContentUnitResult, score, foregroundDurationInMs));
        }

        return new LaunchResultData(
                version,
                contentId,
                runContentUnitResult,
                score,
                foregroundDurationInMs,
                additionalData);
    }

    @NonNull
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
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
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

        @Nullable
        static RunContentUnitResult nullableValueOf(String value) {
            try {
                return RunContentUnitResult.valueOf(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchResultData that = (LaunchResultData) o;
        return version == that.version
                && contentId.equals(that.contentId)
                && runContentUnitResult == that.runContentUnitResult
                && score == that.score
                && foregroundDurationInMs == that.foregroundDurationInMs
                && Objects.equals(additionalData, that.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                contentId,
                runContentUnitResult,
                score,
                foregroundDurationInMs,
                additionalData);
    }
}
