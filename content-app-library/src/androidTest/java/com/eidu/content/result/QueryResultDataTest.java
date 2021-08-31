package com.eidu.content.result;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Intent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class QueryResultDataTest {
    List<String> contentIds = Arrays.asList("content id 1", "content id 2");

    @Test
    public void createQueryResultDataFromCreatedIntent() {
        QueryResultData queryResultData = QueryResultData.fromContentIds(contentIds);
        Intent intent = queryResultData.toResultIntent();
        QueryResultData queryResultDataFromIntent = QueryResultData.fromQueryIntent(intent);

        assertEquals(queryResultData.getContentIds(), queryResultDataFromIntent.getContentIds());
    }
}
