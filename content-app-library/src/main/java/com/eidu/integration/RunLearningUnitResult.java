package com.eidu.integration;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

/**
 * This class represents the result of a content unit run and should be used to deliver the result
 * to the EIDU app.
 *
 * <p>Depending on how the run ended, this class can be instantiated with one of the methods {@link
 * #ofSuccess}, {@link #ofAbort}, {@link #ofTimeoutInactivity}, {@link #ofTimeUp}, {@link #ofError}.
 *
 * <p>Then, {@link #toIntent()} should be used to instantiate an {@link Intent} to be passed to
 * {@link android.app.Activity#setResult(int, Intent)} before finishing the activity.
 */
public class RunLearningUnitResult {

    private static final int VERSION = 1;
    private static final String VERSION_EXTRA = "version";
    private static final String CONTENT_ID_EXTRA = "contentId";
    private static final String RESULT_TYPE = "resultType";
    private static final String SCORE_EXTRA = "score";
    private static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    private static final String ADDITIONAL_DATA_EXTRA = "additionalData";
    private static final String ERROR_DETAILS_EXTRA = "errorDetails";

    public final int version;

    /** The unique ID of the unit that was played in this run. */
    @NonNull public final String contentId;

    /** The reason why this run ended. */
    @NonNull public final ResultType resultType;

    /**
     * If {@link #resultType} is {@link ResultType#Success}, a score between 0.0f and 1.0f that
     * described how the learner did. A value of 0.0f should indicate that the learner only gave
     * incorrect answers, whereas a value of 1.0f should indicate the the learner only gave correct
     * answers.
     *
     * <p>In case the nature of the content unit was such that there was no possibility to give an
     * incorrect answer, the score must be 1.0f.
     */
    public final float score;

    /**
     * The amount of time that the user spent with the content at the end of the run. This
     * <i>must</i> exclude any amount of time that the content app was in the background (because
     * the user navigated away from it, for example using the Android home button or because a
     * different app forced itself into the foreground), and it <i>should</i> exclude time spent for
     * loading and any transition animations.
     */
    public final long foregroundDurationInMs;

    /**
     * An arbitrary string that the content app would like to associate with the usage data that the
     * EIDU app reports. It will be made available to the content app manufacturer for data analysis
     * purposes. It must not contain any sensitive data (e.g. device identifiers).
     *
     * <p>This is useful because content apps should not rely on (and should not attempt to take
     * advantage of) Internet connectivity.
     */
    @Nullable public final String additionalData;

    /**
     * If {@link #resultType} is {@link ResultType#Error}, this <i>should</i> contain any available
     * diagnostic information, e.g. an exception with a stack trace.
     *
     * <p>This information will be reported to EIDU for diagnostic purposes. It must not contain any
     * sensitive data (e.g. device identifiers).
     */
    @Nullable public final String errorDetails;

    private RunLearningUnitResult(
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

    /**
     * Creates an instance with {@link ResultType#Success}. This should be used when the learner
     * completes the unit of content, independently of their performance.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param score <b>Required</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofSuccess(
            @NonNull String contentId,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                contentId,
                ResultType.Success,
                score,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    /**
     * Creates an instance with {@link ResultType#Abort}. This should be used when the learner took
     * an action meant to abort the run, e.g. tapping an abort button.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofAbort(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                contentId,
                ResultType.Abort,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    /**
     * Creates an instance with {@link ResultType#TimeoutInactivity}. This should be used after the
     * learner hasn't interacted with the app for {@link
     * RunLearningUnitRequest#inactivityTimeoutInMs} milliseconds of <i>foreground</i> time.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeoutInactivity(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                contentId,
                ResultType.TimeoutInactivity,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    /**
     * Creates an instance with {@link ResultType#TimeUp}. This should be used after {@link
     * RunLearningUnitRequest#remainingForegroundTimeInMs} milliseconds of <i>foreground</i> time
     * have passed since the start of the run.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeUp(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                contentId,
                ResultType.TimeUp,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    /**
     * Creates an instance with {@link ResultType#Error}. This should be used to indicate that a
     * technical error occurred that prevented the learner from beginning or from completing the
     * run.
     *
     * @param contentId <b>Required</b>, see {@link #contentId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param errorDetails <b>Required</b>, see {@link #errorDetails}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofError(
            @NonNull String contentId,
            long foregroundDurationInMs,
            @NonNull String errorDetails,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                contentId,
                ResultType.Error,
                0,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    /**
     * Parses an {@link Intent} into a new RunContentUnitResult instance.
     *
     * @param intent The intent to parse.
     * @return The new instance.
     * @throws IllegalArgumentException If the intent contains incomplete or invalid data.
     */
    @NonNull
    public static RunLearningUnitResult fromIntent(@NonNull Intent intent) {
        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String contentId = intent.getStringExtra(CONTENT_ID_EXTRA);
        ResultType type =
                RunLearningUnitResult.ResultType.nullableValueOf(intent.getStringExtra(RESULT_TYPE));
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

        return new RunLearningUnitResult(
                version,
                contentId,
                type,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    /**
     * Converts this instance of RunContentUnitResult to an {@link Intent} which may then be passed
     * to {@link android.app.Activity#setResult(int, Intent)} in order to inform the EIDU app of the
     * result of a content unit run.
     *
     * @return An intent that contains all information of this instance of RunContentUnitResult.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunLearningUnitResult that = (RunLearningUnitResult) o;
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

    /** An enum describing the reason why a content unit run has ended. */
    public enum ResultType {
        /** @see RunLearningUnitResult#ofSuccess */
        Success,
        /** @see RunLearningUnitResult#ofAbort */
        Abort,
        /** @see RunLearningUnitResult#ofError */
        Error,
        /** @see RunLearningUnitResult#ofTimeoutInactivity */
        TimeoutInactivity,
        /** @see RunLearningUnitResult#ofTimeUp */
        TimeUp;

        @Nullable
        static ResultType nullableValueOf(String value) {
            try {
                return RunLearningUnitResult.ResultType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
