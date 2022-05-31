package com.eidu.integration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RunLearningUnitRequest is what the EIDU app uses to launch a learning unit from integrated
 * learning apps.
 *
 * <p>The EIDU app will send an intent to your app, identified by the intent action (See {@link
 * Intent#setAction(String)}) and include all data relevant for you to identify the learning unit to
 * run.
 *
 * <p>The intent sent also includes references to the learner and their school.
 *
 * <p>The easiest way to obtain the data provided by the Intent sent from EIDU is {@link
 * RunLearningUnitRequest#fromIntent(Intent)}, which will automatically identify and extract all
 * information included in {@link Intent#getExtras()}.
 *
 * <p>Use {@link RunLearningUnitRequest#getAssetAsStream(Context, String)}, {@link
 * RunLearningUnitRequest#getAssetAsFileDescriptor(Context, String)}, or {@link
 * RunLearningUnitRequest#getAssetAsUri(String)} to retrieve any assets required by the requested
 * learning unit.
 *
 * <p>To facilitate testing of your app, you can create your own RunLearningUnitRequest with {@link
 * RunLearningUnitRequest#of(String, String, String, String, String, Long, Long, Uri)} and convert
 * it to an Intent with {@link RunLearningUnitRequest#toIntent(String, String)}.
 */
public final class RunLearningUnitRequest {

    private static final int VERSION = 1;
    private static final String VERSION_EXTRA = "version";
    private static final String LEARNING_UNIT_ID_EXTRA = "learningUnitId";
    private static final String LEARNING_UNIT_RUN_ID = "learningUnitRunId";
    private static final String LEARNER_ID_EXTRA = "learnerId";
    private static final String SCHOOL_ID_EXTRA = "schoolId";
    private static final String STAGE_EXTRA = "stage";
    private static final String REMAINING_FOREGROUND_TIME_EXTRA = "remainingForegroundTimeInMs";
    private static final String INACTIVITY_TIMEOUT_EXTRA = "inactivityTimeoutInMs";
    private static final String ACTION_LAUNCH_LEARNING_UNIT =
            "com.eidu.integration.LAUNCH_LEARNING_UNIT";

    public final int version;

    /** An ID, defined by the learning app, that uniquely identifies the learning unit to run. */
    @NonNull public final String learningUnitId;

    /** A unique identifier of each learning unit run, which may be used for reporting purposes. */
    @NonNull public final String learningUnitRunId;

    /**
     * The obfuscated ID of the learner who is playing the learning unit. May be used for reporting
     * purposes.
     */
    @NonNull public final String learnerId;

    /**
     * The obfuscated ID of the school at which the learning unit is being played. May be used for
     * reporting purposes.
     */
    @NonNull public final String schoolId;

    /**
     * Identifies the stage of the EIDU app, e.g. "test", "prod". May be used for reporting
     * purposes.
     */
    @NonNull public final String stage;

    /**
     * <i>Optional.</i> The maximum amount of time that this run may take. The learning app must end
     * the run with {@link RunLearningUnitResult.ResultType#TimeUp} after this amount of
     * <i>foreground</i> time has elapsed.
     */
    @Nullable public final Long remainingForegroundTimeInMs;

    /**
     * <i>Optional.</i> The time of user inactivity after which the learning app must end the run
     * with {@link RunLearningUnitResult.ResultType#TimeoutInactivity}.
     */
    @Nullable public final Long inactivityTimeoutInMs;

    @Nullable private final Uri assetsBaseUri;

    private RunLearningUnitRequest(
            int version,
            @NonNull String learningUnitId,
            @NonNull String learningUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String stage,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs,
            @Nullable Uri assetsBaseUri) {
        this.version = version;
        this.learningUnitId = learningUnitId;
        this.learningUnitRunId = learningUnitRunId;
        this.learnerId = learnerId;
        this.schoolId = schoolId;
        this.stage = stage;
        this.remainingForegroundTimeInMs = remainingForegroundTimeInMs;
        this.inactivityTimeoutInMs = inactivityTimeoutInMs;
        this.assetsBaseUri = assetsBaseUri;
    }

    /**
     * Creates a new RunLearningUnitRequest instance.
     *
     * <p>Use this to test your learning app against what the EIDU app will send to launch a
     * learning unit.
     *
     * @param learningUnitId <b>Required</b>, see {@link #learningUnitId}.
     * @param learningUnitRunId <b>Required</b>, see {@link #learningUnitRunId}.
     * @param learnerId <b>Required</b>, see {@link #learnerId}.
     * @param schoolId <b>Required</b>, see {@link #schoolId}.
     * @param stage <b>Required</b>, see {@link #stage}.
     * @param remainingForegroundTimeInMs <i>Optional</i>, see {@link #remainingForegroundTimeInMs}.
     * @param inactivityTimeoutInMs <i>Optional</i>, see {@link #inactivityTimeoutInMs}.
     * @param assetsBaseUri <i>Optional</i>, the base content URI from which to retrieve assets.
     * @return The new instance.
     */
    @NonNull
    public static RunLearningUnitRequest of(
            @NonNull String learningUnitId,
            @NonNull String learningUnitRunId,
            @NonNull String learnerId,
            @NonNull String schoolId,
            @NonNull String stage,
            @Nullable Long remainingForegroundTimeInMs,
            @Nullable Long inactivityTimeoutInMs,
            @Nullable Uri assetsBaseUri) {
        return new RunLearningUnitRequest(
                VERSION,
                learningUnitId,
                learningUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs,
                assetsBaseUri);
    }

    /**
     * Creates a new RunLearningUnitRequest instance from the provided intent.
     *
     * <p>Use this on the intent sent to your learning app by EIDU to get access to the necessary
     * data.
     *
     * @param intent <b>Required</b>, the intent sent from the EIDU app to your learning app.
     * @return The new RunLearningUnitRequest instance, initialized from the provided intent, or
     *     null if intent was not a request to run a learning unit
     * @throws IllegalArgumentException If the provided intent does not contain all required data.
     */
    @Nullable
    public static RunLearningUnitRequest fromIntent(@NonNull Intent intent)
            throws IllegalArgumentException {
        if (!ACTION_LAUNCH_LEARNING_UNIT.equals(intent.getAction())) return null;

        int version = intent.getIntExtra(VERSION_EXTRA, VERSION);
        String learningUnitId = intent.getStringExtra(LEARNING_UNIT_ID_EXTRA);
        String learningUnitRunId = intent.getStringExtra(LEARNING_UNIT_RUN_ID);
        String learnerId = intent.getStringExtra(LEARNER_ID_EXTRA);
        String schoolId = intent.getStringExtra(SCHOOL_ID_EXTRA);
        String stage = intent.getStringExtra(STAGE_EXTRA);
        Long remainingForegroundTimeInMs = getLongExtra(intent, REMAINING_FOREGROUND_TIME_EXTRA);
        Long inactivityTimeoutInMs = getLongExtra(intent, INACTIVITY_TIMEOUT_EXTRA);

        Uri assetsBaseUri = intent.getData();

        for (String field :
                new String[] {learningUnitId, learningUnitRunId, learnerId, schoolId, stage})
            if (field == null || field.isEmpty())
                throw new IllegalArgumentException(
                        String.format(
                                "Invalid launch intent. A required field is missing. "
                                        + "[learningUnitId: %s, learningUnitRunId: %s, learnerId: %s, schoolId: %s, "
                                        + "stage: %s]",
                                learningUnitId, learningUnitRunId, learnerId, schoolId, stage));

        return new RunLearningUnitRequest(
                version,
                learningUnitId,
                learningUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs,
                assetsBaseUri);
    }

    /**
     * Creates an explicit intent usable to launch a learning app.
     *
     * <p>You can use this method, along with {@link RunLearningUnitRequest#of(String, String,
     * String, String, String, Long, Long, Uri)} to test your app.
     *
     * @param packageName The package name of the learning app.
     * @param className The class name of the activity to launch with this intent
     * @return An intent for the given package and class, containing all the launch information.
     */
    @NonNull
    public Intent toIntent(@NonNull String packageName, @NonNull String className) {
        Intent intent = new Intent(ACTION_LAUNCH_LEARNING_UNIT);
        intent.setClassName(packageName, className);
        intent.setData(assetsBaseUri);
        return addExtras(intent);
    }

    @NonNull
    private Intent addExtras(@NonNull Intent intent) {
        intent.putExtra(VERSION_EXTRA, version)
                .putExtra(LEARNING_UNIT_ID_EXTRA, learningUnitId)
                .putExtra(LEARNING_UNIT_RUN_ID, learningUnitRunId)
                .putExtra(LEARNER_ID_EXTRA, learnerId)
                .putExtra(SCHOOL_ID_EXTRA, schoolId)
                .putExtra(STAGE_EXTRA, stage);
        intent.putExtra(REMAINING_FOREGROUND_TIME_EXTRA, remainingForegroundTimeInMs);
        intent.putExtra(INACTIVITY_TIMEOUT_EXTRA, inactivityTimeoutInMs);
        return intent;
    }

    /**
     * Retrieves the contents of an asset as an {@link InputStream}. <b>It is the caller's
     * responsibility to close this stream after use.</b>
     *
     * @param context The current context. May be an application context.
     * @param path The path of the asset to retrieve.
     * @return An {@link InputStream} from which the asset's contents can be read.
     * @throws FileNotFoundException In case an asset by that name cannot be found for the learning
     *     unit being run, the request does not support asset loading, or EIDU has crashed.
     */
    @NonNull
    public InputStream getAssetAsStream(@NonNull Context context, @NonNull String path)
            throws FileNotFoundException {
        ParcelFileDescriptor descriptor = getAssetAsFileDescriptor(context, path);
        return new FileInputStream(descriptor.getFileDescriptor()) {
            @Override
            public void close() throws IOException {
                super.close();
                descriptor.close();
            }
        };
    }

    @NonNull
    private ParcelFileDescriptor getAssetAsFileDescriptor(
            @NonNull Context context, @NonNull String path) throws FileNotFoundException {
        ParcelFileDescriptor descriptor =
                context.getContentResolver().openFileDescriptor(getAssetAsUri(path), "r");
        if (descriptor == null)
            throw new FileNotFoundException("Unable to retrieve asset because EIDU has crashed.");
        return descriptor;
    }

    /**
     * Retrieves a content {@link Uri} for an asset, which can be used, for example, with {@link
     * android.media.MediaPlayer}.
     *
     * <p><b>At this point, it is not guaranteed that the {@link Uri} points to a valid asset.</b>
     *
     * @param path The path of the asset to retrieve.
     * @return A {@link Uri} from which the asset's contents can be read.
     * @throws FileNotFoundException In case the request does not support asset loading.
     */
    @NonNull
    public Uri getAssetAsUri(@NonNull String path) throws FileNotFoundException {
        if (assetsBaseUri == null) throw new FileNotFoundException();

        return assetsBaseUri.buildUpon().appendPath(path).build();
    }

    @Nullable
    private static Long getLongExtra(@NonNull Intent intent, @NonNull String extra) {
        if (intent.hasExtra(extra)) {
            Serializable value = intent.getSerializableExtra(extra);
            if (value == null) {
                return null;
            }
            if (value.getClass().equals(Long.class)) {
                return (Long) value;
            } else
                throw new IllegalArgumentException(
                        "Extra " + extra + " was not a valid Long value");
        } else {
            throw new IllegalArgumentException("Extra " + extra + " not found in intent!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunLearningUnitRequest that = (RunLearningUnitRequest) o;
        return version == that.version
                && learningUnitId.equals(that.learningUnitId)
                && learningUnitRunId.equals(that.learningUnitRunId)
                && learnerId.equals(that.learnerId)
                && schoolId.equals(that.schoolId)
                && stage.equals(that.stage)
                && Objects.equals(remainingForegroundTimeInMs, that.remainingForegroundTimeInMs)
                && Objects.equals(inactivityTimeoutInMs, that.inactivityTimeoutInMs)
                && Objects.equals(assetsBaseUri, that.assetsBaseUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                learningUnitId,
                learningUnitRunId,
                learnerId,
                schoolId,
                stage,
                remainingForegroundTimeInMs,
                inactivityTimeoutInMs,
                assetsBaseUri);
    }
}
