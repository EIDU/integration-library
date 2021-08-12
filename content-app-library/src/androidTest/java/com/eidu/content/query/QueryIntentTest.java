package com.eidu.content.query;

import static com.eidu.content.query.QueryIntent.CATEGORY_QUERY_CONTENT_IDS;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import java.util.Collections;
import org.junit.Test;

public class QueryIntentTest {

    @Test
    public void createIntent() {
        String queryAction = "ACTION";
        Intent intent = QueryIntent.createIntent(queryAction);
        assertEquals(intent.getCategories(), Collections.singleton(CATEGORY_QUERY_CONTENT_IDS));
        assertEquals(intent.getAction(), queryAction);
    }
}
