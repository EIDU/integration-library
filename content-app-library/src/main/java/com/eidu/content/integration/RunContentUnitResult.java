package com.eidu.content.integration;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

public class RunContentUnitResult {

    public static final int VERSION = 1;
    public static final String VERSION_EXTRA = "version";
    public static final String CONTENT_ID_EXTRA = "contentId";
    public static final String RESULT_TYPE = "resultType";
    public static final String SCORE_EXTRA = "score";
    public static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    public static final String ADDITIONAL_DATA_EXTRA = "additionalData";
    public static final String ERROR_DETAILS_EXTRA = "errorDetails";

    public final int version;
    @NonNull public final String contentId;
    @NonNull public final ResultType resultType;
    public final float score;
    public final long foregroundDurationInMs;
    @Nullable public final String additionalData;
    @Nullable public final String errorDetails;

    @NonNull
    public static RunContentUnitResult ofSuccess(
            @NonNull String contentId,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunContentUnitResult(
                VERSION,
                contentId,
                ResultType.Success,
                score,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    @NonNull
    public static RunContentUnitResult ofAbort(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunContentUnitResult(
                VERSION,
                contentId,
                ResultType.Abort,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    @NonNull
    public static RunContentUnitResult ofTimeoutInactivity(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunContentUnitResult(
                VERSION,
                contentId,
                ResultType.TimeoutInactivity,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    @NonNull
    public static RunContentUnitResult ofTimeUp(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunContentUnitResult(
                VERSION,
                contentId,
                ResultType.TimeUp,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    @NonNull
    public static RunContentUnitResult ofError(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @NonNull String errorDetails,
            @Nullable String additionalData) {
        return new RunContentUnitResult(
                VERSION,
                contentId,
                ResultType.Error,
                0,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    @NonNull
    public static RunContentUnitResult fromIntent(@NonNull Intent intent) {
        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = intent.getStringExtra(CONTENT_ID_EXTRA);
        ResultType type =
                RunContentUnitResult.ResultType.nullableValueOf(intent.getStringExtra(RESULT_TYPE));
        Float score = intent.hasExtra(SCORE_EXTRA) ? intent.getFloatExtra(SCORE_EXTRA, 0.f) : null;
        Long foregroundDurationInMs =
                intent.hasExtra(FOREGROUND_DURATION_EXTRA)
                        ? intent.getLongExtra(FOREGROUND_DURATION_EXTRA, 0)
                        : null;
        String additionalData = intent.getStringExtra(ADDITIONAL_DATA_EXTRA);
        String errorDetails = intent.getStringExtra(ERROR_DETAILS_EXTRA);

        if (contentId == null
                || contentId.isEmpty()
                || type == null
                || score == null
                || foregroundDurationInMs == null)
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. "
                                    + "[contentId: %s, type: %s score: %f, foregroundDurationInMs: %d]",
                            contentId, type, score, foregroundDurationInMs));

        return new RunContentUnitResult(
                version,
                contentId,
                type,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    @NonNull
    public Intent toIntent() {
        return new Intent()
                .putExtra(VERSION_EXTRA, version)
                .putExtra(CONTENT_ID_EXTRA, contentId)
                .putExtra(RESULT_TYPE, resultType.name())
                .putExtra(SCORE_EXTRA, score)
                .putExtra(FOREGROUND_DURATION_EXTRA, foregroundDurationInMs)
                .putExtra(ADDITIONAL_DATA_EXTRA, additionalData)
                .putExtra(ERROR_DETAILS_EXTRA, errorDetails);
    }

    private RunContentUnitResult(
            int version,
            @NonNull String contentId,
            @NonNull ResultType resultType,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable String errorDetails) {
        this.version = version;
        this.contentId = contentId;
        this.resultType = resultType;
        this.score = score;
        this.foregroundDurationInMs = foregroundDurationInMs;
        this.additionalData = additionalData;
        this.errorDetails = errorDetails;
    }

    public enum ResultType {
        Success,
        Abort,
        Error,
        TimeoutInactivity,
        TimeUp;

        @Nullable
        static ResultType nullableValueOf(String value) {
            try {
                return RunContentUnitResult.ResultType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunContentUnitResult that = (RunContentUnitResult) o;
        return version == that.version
                && contentId.equals(that.contentId)
                && resultType == that.resultType
                && score == that.score
                && foregroundDurationInMs == that.foregroundDurationInMs
                && Objects.equals(additionalData, that.additionalData)
                && Objects.equals(errorDetails, that.errorDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                contentId,
                resultType,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }
}
