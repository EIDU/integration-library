package com.eidu.integration;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

/**
 * This class represents the result of a learning unit run and should be used to deliver the result
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
    private static final String LEARNING_UNIT_ID = "learningUnitId";
    private static final String RESULT_TYPE = "resultType";
    private static final String SCORE_EXTRA = "score";
    private static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    private static final String ADDITIONAL_DATA_EXTRA = "additionalData";
    private static final String ERROR_DETAILS_EXTRA = "errorDetails";

    public final int version;

    /** The unique ID of the learning unit that was played in this run. */
    @NonNull public final String learningUnitId;

    /** The reason why this run ended. */
    @NonNull public final ResultType resultType;

    /**
     * If {@link #resultType} is {@link ResultType#Success}, a score between 0.0f and 1.0f that
     * described how the learner did. A value of 0.0f should indicate that the learner only gave
     * incorrect answers, whereas a value of 1.0f should indicate the the learner only gave correct
     * answers.
     *
     * <p>In case the nature of the learning unit was such that there was no possibility to give an
     * incorrect answer, the score must be 1.0f.
     */
    public final float score;

    /**
     * The amount of time that the user spent with the learning unit at the end of the run. This
     * <i>must</i> exclude any amount of time that the learning app was in the background (because
     * the user navigated away from it, for example using the Android home button or because a
     * different app forced itself into the foreground), and it <i>should</i> exclude time spent for
     * loading and any transition animations.
     */
    public final long foregroundDurationInMs;

    /**
     * An arbitrary string that the learning app would like to associate with the usage data that
     * the EIDU app reports. It will be made available to the learning app manufacturer for data
     * analysis purposes. It must not contain any sensitive data (e.g. device identifiers).
     *
     * <p>This is useful because learning apps should not rely on (and should not attempt to take
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
            @NonNull String learningUnitId,
            @NonNull ResultType resultType,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable String errorDetails) {
        this.version = version;
        this.learningUnitId = learningUnitId;
        this.resultType = resultType;
        this.score = score;
        this.foregroundDurationInMs = foregroundDurationInMs;
        this.additionalData = additionalData;
        this.errorDetails = errorDetails;
    }

    /**
     * Creates an instance with {@link ResultType#Success}. This should be used when the learner
     * completes the learning unit, independently of their performance.
     *
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param score <b>Required</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofSuccess(
            @NonNull String learningUnitId,
            float score,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                learningUnitId,
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
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofAbort(
            @NonNull String learningUnitId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                learningUnitId,
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
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeoutInactivity(
            @NonNull String learningUnitId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                learningUnitId,
                ResultType.TimeoutInactivity,
                0,
                foregroundDurationInMs,
                additionalData,
                null);
    }

    /**
     * Creates an instance with {@link ResultType#TimeUp}. This should be used after {@link
     * RunLearningUnitRequest#remainingForegroundTimeInMs} milliseconds of <i>foreground</i> time
     * passed since the start of the run.
     *
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeUp(
            @NonNull String learningUnitId,
            long foregroundDurationInMs,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                learningUnitId,
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
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param errorDetails <b>Required</b>, see {@link #errorDetails}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofError(
            @NonNull String learningUnitId,
            long foregroundDurationInMs,
            @NonNull String errorDetails,
            @Nullable String additionalData) {
        return new RunLearningUnitResult(
                VERSION,
                learningUnitId,
                ResultType.Error,
                0,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    /**
     * Parses an {@link Intent} into a new RunLearningUnitResult instance.
     *
     * @param intent The intent to parse.
     * @return The new instance.
     * @throws IllegalArgumentException If the intent contains incomplete or invalid data.
     */
    @NonNull
    public static RunLearningUnitResult fromIntent(@NonNull Intent intent) {
        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String learningUnitId = intent.getStringExtra(LEARNING_UNIT_ID);
        ResultType type =
                RunLearningUnitResult.ResultType.nullableValueOf(
                        intent.getStringExtra(RESULT_TYPE));
        Float score = intent.hasExtra(SCORE_EXTRA) ? intent.getFloatExtra(SCORE_EXTRA, 0.f) : null;
        Long foregroundDurationInMs =
                intent.hasExtra(FOREGROUND_DURATION_EXTRA)
                        ? intent.getLongExtra(FOREGROUND_DURATION_EXTRA, 0)
                        : null;
        String additionalData = intent.getStringExtra(ADDITIONAL_DATA_EXTRA);
        String errorDetails = intent.getStringExtra(ERROR_DETAILS_EXTRA);

        if (learningUnitId == null
                || learningUnitId.isEmpty()
                || type == null
                || score == null
                || foregroundDurationInMs == null)
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. "
                                    + "[learningUnitId: %s, type: %s score: %f, foregroundDurationInMs: %d]",
                            learningUnitId, type, score, foregroundDurationInMs));

        return new RunLearningUnitResult(
                version,
                learningUnitId,
                type,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    /**
     * Converts this instance of RunLearningUnitResult to an {@link Intent} which may then be passed
     * to {@link android.app.Activity#setResult(int, Intent)} in order to inform the EIDU app of the
     * result of a learning unit run.
     *
     * @return An intent that contains all information of this instance of RunLearningUnitResult.
     */
    @NonNull
    public Intent toIntent() {
        return new Intent()
                .putExtra(VERSION_EXTRA, version)
                .putExtra(LEARNING_UNIT_ID, learningUnitId)
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
                && learningUnitId.equals(that.learningUnitId)
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
                learningUnitId,
                resultType,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails);
    }

    /** An enum describing the reason why a learning unit run has ended. */
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
