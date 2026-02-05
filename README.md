# EIDU Integration Library

![Maven Central](https://img.shields.io/maven-central/v/com.eidu/integration-library)

This library helps you to get your Android app ready to integrate with the EIDU platform.

## Usage

### 1. Add the Gradle dependency

Add the integration library to your project:

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.eidu:integration-library:<version>")
}
```

### 2. Configure AndroidManifest.xml

Declare an Activity in your `AndroidManifest.xml` that will handle learning unit launches from the EIDU platform. The activity must include an intent-filter for the `com.eidu.integration.LAUNCH_LEARNING_UNIT` action and must be exported:

```xml
<activity
    android:name=".YourLearningUnitActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="com.eidu.integration.LAUNCH_LEARNING_UNIT" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

**Important:** 
- Replace `.YourLearningUnitActivity` with your actual Activity class name
- The `android:exported="true"` attribute is **required** for the EIDU platform to launch your activity (mandatory for Android 12+)
- The `<category android:name="android.intent.category.DEFAULT" />` is required for implicit intent handling

### 3. Handle the Launch Intent

In your Activity, use `RunLearningUnitRequest.fromIntent()` to extract the learning unit information:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    RunLearningUnitRequest request = RunLearningUnitRequest.fromIntent(getIntent());
    if (request != null) {
        // Launch the learning unit specified by request.learningUnitId
        // Access other request properties as needed:
        // - request.learningUnitRunId
        // - request.learnerId
        // - request.schoolId
        // - request.stage
        // - request.remainingForegroundTimeInMs
        // - request.inactivityTimeoutInMs
    }
}
```

### 4. Return Results

When the learning unit completes, create a `RunLearningUnitResult` and return it to the EIDU platform:

```java
RunLearningUnitResult result = RunLearningUnitResult.ofSuccess(
    score,                    // Float between 0.0 and 1.0, or null
    foregroundDurationInMs,   // long
    additionalData,           // String or null
    items                     // List<ResultItem> or null
);

Intent resultIntent = result.toIntent();
setResult(RESULT_OK, resultIntent);
finish();
```

### Additional Resources

Please consult [dev.eidu.com](https://dev.eidu.com) for detailed instructions and the
[Javadoc documentation](https://dl.eidu.com/dev/integration-library/latest/javadoc/) for the
complete API documentation.

For a complete example, see the [sample-learning-app](https://github.com/EIDU/sample-learning-app).

## Support

Please contact EIDU at [dev@eidu.com](mailto:dev@eidu.com) if you need help.

## Contributing

We are excited about your contributions. See
[our current contribution guidelines](https://dev.eidu.com/contributing/overview) to get started.

## License

This library is published under the [MIT License](https://github.com/EIDU/integration-library/blob/main/LICENSE).
