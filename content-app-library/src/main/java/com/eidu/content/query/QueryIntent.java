package com.eidu.content.query;

import android.content.Intent;

import androidx.annotation.NonNull;

public final class QueryIntent {

    public static final String CONTENT_IDS_QUERY_CATEGORY = "contentIdsQuery";

    public static Intent createIntent(@NonNull String queryAction) {
        Intent intent = new Intent(queryAction);
        intent.addCategory(CONTENT_IDS_QUERY_CATEGORY);
        return intent;
    }

    private QueryIntent() {
    }
}
