package com.eidu.content.query;

import static com.eidu.content.query.QueryIntent.ACTION_QUERY_CONTENT_IDS;
import static com.eidu.content.query.QueryIntent.CATEGORY_QUERY_CONTENT_IDS;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.ComponentName;
import android.content.Intent;

import org.junit.jupiter.api.Test;

import java.util.Collections;

public class QueryIntentTest {

    @Test
    public void createIntent() {
        String queryAction = "ACTION";
        Intent intent = QueryIntent.createIntent(queryAction);
        assertEquals(intent.getCategories(), Collections.singleton(CATEGORY_QUERY_CONTENT_IDS));
        assertEquals(intent.getAction(), queryAction);
    }

    @Test
    public void createExplicitIntent() {
        String packageName = "com.eidu.content";
        String activity = "ContentActivity";
        Intent intent = QueryIntent.createIntent(packageName, activity);
        assertEquals(intent.getAction(), ACTION_QUERY_CONTENT_IDS);

        ComponentName expected = new ComponentName(packageName, activity);

        assertEquals(intent.getComponent(), expected);
    }
}
