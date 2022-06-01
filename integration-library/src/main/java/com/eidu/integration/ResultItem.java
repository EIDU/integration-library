package com.eidu.integration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * In order to provide more detail about learning unit runs - which enables better analytics and
 * personalisation - learning app developers are strongly encouraged to provide a list of {@link
 * ResultItem} instances to describe each significant user interaction during a learning unit run.
 *
 * <p>Where it makes sense, all items that were <i>presented</i> to the user should be included,
 * even if the user did not provide a response to the item due to the unit being cut short, e.g. by
 * a timeout. See {@link #completed} for details.
 *
 * <p>Since different learning units vary greatly in nature, there is no one-size-fits-all solution
 * to capturing their results. This is why all fields in this class are optional: each should be
 * provided only if it will hold sensible values.
 *
 * <p>There are learning units that cannot described in terms of a list of {@link ResultItem}
 * instances at all. If you encounter such a unit, you are invited to contact EIDU to see if the
 * data model can be extended to cover your use case.
 */
public class ResultItem {

    private static final String ID = "id";
    private static final String CHALLENGE = "challenge";
    private static final String COMPLETED = "completed";
    private static final String GIVEN_RESPONSE = "givenResponse";
    private static final String CORRECT_RESPONSE = "correctResponse";
    private static final String SCORE = "score";
    private static final String DURATION_IN_MS = "durationInMs";
    private static final String TIME_TO_FIRST_ACTION_IN_MS = "timeToFirstActionInMs";

    /**
     * <i>Optional.</i> Identifies the item within the learning unit being played. The ID _must_ be
     * stable and unique, i.e. in all runs of the learning unit, it must refer to the same challenge
     * and that challenge only. Depending on the structure of the learning unit, this may be an
     * index ("0", "1", "2", ...) or the same as {@link #challenge} or something else.
     */
    @Nullable public final String id;

    /**
     * <i>Optional.</i> If true, indicates that the learner provided a complete response to the item
     * (independently of whether it was correct or not). If false, indicates that the unit ended
     * before the learner was able to provide a complete response (e.g. due to a unit abort or
     * timeout). In a typical unit that ended with {@link RunLearningUnitResult.ResultType#Success},
     * all <code>ResultItems</code> should have <code>completed == true</code>, whereas typically,
     * if the unit ended with one of the other result types, the last item would have <code>
     * completed == false</code>.
     */
    @Nullable public final Boolean completed;

    /** <i>Optional.</i> A description of the challenge, e.g. "2 + 4". */
    @Nullable public final String challenge;

    /** <i>Optional.</i> The response given by the learner, e.g. "5". */
    @Nullable public final String givenResponse;

    /** <i>Optional.</i> The correct response to the challenge, e.g. "6". */
    @Nullable public final String correctResponse;

    /**
     * <i>Optional.</i> A score between 0.0f and 1.0f that describes how the learner did in the
     * challenge.
     */
    @Nullable public final Float score;

    /**
     * <i>Optional.</i> The time it took the learner to give a response after being presented with
     * the challenge.
     */
    @Nullable public final Long durationInMs;

    /**
     * <i>Optional.</i> The time it took until the learner began responding to the challenge after
     * being presented with it. This is important because it can be a good predictor of the
     * learner's engagement.
     */
    @Nullable public final Long timeToFirstActionInMs;

    /**
     * Creates a new ResultItem instance.
     *
     * @param id <i>Optional,</i> see {@link #id}.
     * @param completed <i>Optional,</i> see {@link #completed}.
     * @param challenge <i>Optional,</i> see {@link #challenge}.
     * @param givenResponse <i>Optional,</i> see {@link #givenResponse}.
     * @param correctResponse <i>Optional,</i> see {@link #correctResponse}.
     * @param score <i>Optional,</i> see {@link #score}.
     * @param durationInMs <i>Optional,</i> see {@link #durationInMs}.
     * @param timeToFirstActionInMs <i>Optional,</i> see {@link #timeToFirstActionInMs}.
     */
    public ResultItem(
            @Nullable String id,
            @Nullable Boolean completed,
            @Nullable String challenge,
            @Nullable String givenResponse,
            @Nullable String correctResponse,
            @Nullable Float score,
            @Nullable Long durationInMs,
            @Nullable Long timeToFirstActionInMs) {
        this.id = id;
        this.completed = completed;
        this.challenge = challenge;
        this.givenResponse = givenResponse;
        this.correctResponse = correctResponse;
        this.score = score;
        this.durationInMs = durationInMs;
        this.timeToFirstActionInMs = timeToFirstActionInMs;
    }

    @NonNull
    public static ResultItem fromJson(@NonNull JSONObject json) {
        return new ResultItem(
                json.isNull(ID) ? null : json.optString(ID),
                json.isNull(COMPLETED) ? null : json.optBoolean(COMPLETED),
                json.isNull(CHALLENGE) ? null : json.optString(CHALLENGE),
                json.isNull(GIVEN_RESPONSE) ? null : json.optString(GIVEN_RESPONSE),
                json.isNull(CORRECT_RESPONSE) ? null : json.optString(CORRECT_RESPONSE),
                json.isNull(SCORE) ? null : (float) json.optDouble(SCORE),
                json.isNull(DURATION_IN_MS) ? null : json.optLong(DURATION_IN_MS),
                json.isNull(TIME_TO_FIRST_ACTION_IN_MS)
                        ? null
                        : json.optLong(TIME_TO_FIRST_ACTION_IN_MS));
    }

    @NonNull
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        if (id != null) obj.put(ID, id);
        if (completed != null) obj.put(COMPLETED, completed);
        if (challenge != null) obj.put(CHALLENGE, challenge);
        if (givenResponse != null) obj.put(GIVEN_RESPONSE, givenResponse);
        if (correctResponse != null) obj.put(CORRECT_RESPONSE, correctResponse);
        if (score != null) obj.put(SCORE, score);
        if (durationInMs != null) obj.put(DURATION_IN_MS, durationInMs);
        if (timeToFirstActionInMs != null)
            obj.put(TIME_TO_FIRST_ACTION_IN_MS, timeToFirstActionInMs);
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultItem that = (ResultItem) o;
        return Objects.equals(id, that.id)
                && Objects.equals(completed, that.completed)
                && Objects.equals(challenge, that.challenge)
                && Objects.equals(givenResponse, that.givenResponse)
                && Objects.equals(correctResponse, that.correctResponse)
                && Objects.equals(score, that.score)
                && Objects.equals(durationInMs, that.durationInMs)
                && Objects.equals(timeToFirstActionInMs, that.timeToFirstActionInMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                completed,
                challenge,
                givenResponse,
                correctResponse,
                score,
                durationInMs,
                timeToFirstActionInMs);
    }
}
