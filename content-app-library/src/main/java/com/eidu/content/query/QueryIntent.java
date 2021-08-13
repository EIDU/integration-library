package com.eidu.content.query;

import android.content.Intent;
import androidx.annotation.NonNull;

public final class QueryIntent {

    public static final String ACTION_QUERY_CONTENT_IDS =
            "com.eidu.content.query.QUERY_CONTENT_IDS";
    public static final String CATEGORY_QUERY_CONTENT_IDS =
            "com.eidu.content.query.QUERY_CONTENT_IDS";

    @NonNull
    public static Intent createIntent(@NonNull String queryAction) {
        Intent intent = new Intent(queryAction);
        intent.addCategory(CATEGORY_QUERY_CONTENT_IDS);
        return intent;
    }

    @NonNull
    public static Intent createIntent(@NonNull String packageName, @NonNull String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setAction(ACTION_QUERY_CONTENT_IDS);
        return intent;
    }

    private QueryIntent() {}
}
