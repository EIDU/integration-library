package com.eidu.content.result;

import android.content.Intent;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QueryResultData {

    public static final String CONTENT_IDS_EXTRA = "contentIds";

    @NonNull private final List<String> contentIds;

    @NonNull
    public static QueryResultData fromQueryIntent(@NonNull Intent intent) {
        if (intent.hasExtra(CONTENT_IDS_EXTRA)) {
            return new QueryResultData(intent.getStringArrayListExtra(CONTENT_IDS_EXTRA));
        } else {
            return new QueryResultData(Collections.emptyList());
        }
    }

    @NonNull
    public static QueryResultData fromContentIds(@NonNull List<String> contentIds) {
        return new QueryResultData(contentIds);
    }

    @NonNull
    public Intent toResultIntent() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra(CONTENT_IDS_EXTRA, new ArrayList<>(getContentIds()));
        return resultIntent;
    }

    @NonNull
    public List<String> getContentIds() {
        return contentIds;
    }

    private QueryResultData(@NonNull List<String> contentIds) {
        this.contentIds = Collections.unmodifiableList(contentIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResultData that = (QueryResultData) o;
        return contentIds.equals(that.contentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentIds);
    }
}
