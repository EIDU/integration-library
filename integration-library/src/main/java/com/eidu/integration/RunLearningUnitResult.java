package com.eidu.integration;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
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

    private static final int VERSION = 2;
    private static final String VERSION_EXTRA = "version";
    private static final String RESULT_TYPE = "resultType";
    private static final String SCORE_EXTRA = "score";
    private static final String FOREGROUND_DURATION_EXTRA = "foregroundDurationInMs";
    private static final String ADDITIONAL_DATA_EXTRA = "additionalData";
    private static final String ERROR_DETAILS_EXTRA = "errorDetails";
    private static final String ITEMS_EXTRA = "items";

    public final int version;

    /** The reason why this run ended. */
    @NonNull public final ResultType resultType;

    /**
     * A score between 0.0f and 1.0f that describes how the learner did up to the point in time when
     * the unit ended. value of 0.0f should indicate that the learner gave only incorrect answers,
     * whereas a value of 1.0f should indicate the the learner gave only correct answers.
     *
     * <p>In case the nature of the learning unit was such that there was no possibility to give an
     * incorrect answer, the score must be 1.0f.
     *
     * <p>Only in case it's impossible to compute a meaningful score may this value be null.
     * Learning app developers are strongly encouraged to try hard to compute meaningful scores in
     * as many cases as possible, in order to provide the maximum amount of information for
     * personalisation and analytics.
     */
    @Nullable public final Float score;

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

    /**
     * A list of items describing the user interactions during a learning unit run in detail. See
     * the documentation of {@link ResultItem} for details.
     *
     * <p>Note that a <code>null</code> value signifies that no sensible representation of
     * interactions is available, whereas an empty list signifies that no interactions occurred.
     */
    @Nullable public final List<ResultItem> items;

    private RunLearningUnitResult(
            int version,
            @NonNull ResultType resultType,
            @Nullable Float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable String errorDetails,
            @Nullable List<ResultItem> items) {
        this.version = version;
        this.resultType = resultType;
        this.score = score;
        this.foregroundDurationInMs = foregroundDurationInMs;
        this.additionalData = additionalData;
        this.errorDetails = errorDetails;
        this.items = items;
    }

    /**
     * Creates an instance with {@link ResultType#Success}. This should be used when the learner
     * completes the learning unit, independently of their performance.
     *
     * @param score <b>Strongly encouraged</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @param items <i>Optional</i>, see {@link #items}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofSuccess(
            @Nullable Float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable List<ResultItem> items) {
        return new RunLearningUnitResult(
                VERSION,
                ResultType.Success,
                score,
                foregroundDurationInMs,
                additionalData,
                null,
                items);
    }

    /**
     * Creates an instance with {@link ResultType#Abort}. This should be used when the learner took
     * an action meant to abort the run, e.g. tapping an abort button.
     *
     * @param score <b>Strongly encouraged</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @param items <i>Optional</i>, see {@link #items}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofAbort(
            @Nullable Float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable List<ResultItem> items) {
        return new RunLearningUnitResult(
                VERSION,
                ResultType.Abort,
                score,
                foregroundDurationInMs,
                additionalData,
                null,
                items);
    }

    /**
     * Creates an instance with {@link ResultType#TimeoutInactivity}. This should be used after the
     * learner hasn't interacted with the app for {@link
     * RunLearningUnitRequest#inactivityTimeoutInMs} milliseconds of <i>foreground</i> time.
     *
     * @param score <b>Strongly encouraged</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @param items <i>Optional</i>, see {@link #items}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeoutInactivity(
            @Nullable Float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable List<ResultItem> items) {
        return new RunLearningUnitResult(
                VERSION,
                ResultType.TimeoutInactivity,
                score,
                foregroundDurationInMs,
                additionalData,
                null,
                items);
    }

    /**
     * Creates an instance with {@link ResultType#TimeUp}. This should be used after {@link
     * RunLearningUnitRequest#remainingForegroundTimeInMs} milliseconds of <i>foreground</i> time
     * passed since the start of the run.
     *
     * @param score <b>Strongly encouraged</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @param items <i>Optional</i>, see {@link #items}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofTimeUp(
            @Nullable Float score,
            long foregroundDurationInMs,
            @Nullable String additionalData,
            @Nullable List<ResultItem> items) {
        return new RunLearningUnitResult(
                VERSION,
                ResultType.TimeUp,
                score,
                foregroundDurationInMs,
                additionalData,
                null,
                items);
    }

    /**
     * Creates an instance with {@link ResultType#Error}. This should be used to indicate that a
     * technical error occurred that prevented the learner from beginning or from completing the
     * run.
     *
     * @param score <b>Strongly encouraged</b>, see {@link #score}.
     * @param foregroundDurationInMs <b>Required</b>, see {@link #foregroundDurationInMs}.
     * @param errorDetails <b>Required</b>, see {@link #errorDetails}.
     * @param additionalData <i>Optional</i>, see {@link #additionalData}.
     * @param items <i>Optional</i>, see {@link #items}.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitResult ofError(
            @Nullable Float score,
            long foregroundDurationInMs,
            @NonNull String errorDetails,
            @Nullable String additionalData,
            @Nullable List<ResultItem> items) {
        return new RunLearningUnitResult(
                VERSION,
                ResultType.Error,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails,
                items);
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

        List<ResultItem> items = parseItems(intent);

        if (type == null || foregroundDurationInMs == null)
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid result intent. A required field is missing. "
                                    + "[type: %s score: %f, foregroundDurationInMs: %d]",
                            type, score, foregroundDurationInMs));

        return new RunLearningUnitResult(
                version, type, score, foregroundDurationInMs, additionalData, errorDetails, items);
    }

    @Nullable
    private static List<ResultItem> parseItems(@NonNull Intent intent) {
        String itemsString = intent.getStringExtra(ITEMS_EXTRA);
        if (itemsString == null || itemsString.equals("null")) return null;

        try {
            JSONArray jsonArray = new JSONArray(itemsString);

            ArrayList<ResultItem> items = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++)
                items.add(ResultItem.fromJson(jsonArray.getJSONObject(i)));

            return items;
        } catch (JSONException e) {
            throw new IllegalArgumentException("Failed to parse " + ITEMS_EXTRA, e);
        }
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
                .putExtra(RESULT_TYPE, resultType.name())
                .putExtra(SCORE_EXTRA, score)
                .putExtra(FOREGROUND_DURATION_EXTRA, foregroundDurationInMs)
                .putExtra(ADDITIONAL_DATA_EXTRA, additionalData)
                .putExtra(ERROR_DETAILS_EXTRA, errorDetails)
                .putExtra(ITEMS_EXTRA, itemsJson());
    }

    @NonNull
    private String itemsJson() {
        if (items == null)
            return "null";
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < items.size(); i++)
            try {
                jsonArray.put(items.get(i).toJson());
            } catch (JSONException e) {
                throw new IllegalStateException("Failed to serialize item.", e);
            }
        return jsonArray.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunLearningUnitResult that = (RunLearningUnitResult) o;
        return version == that.version
                && resultType == that.resultType
                && Objects.equals(score, that.score)
                && foregroundDurationInMs == that.foregroundDurationInMs
                && Objects.equals(additionalData, that.additionalData)
                && Objects.equals(errorDetails, that.errorDetails)
                && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                resultType,
                score,
                foregroundDurationInMs,
                additionalData,
                errorDetails,
                items);
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
