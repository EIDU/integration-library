package com.eidu.content.result;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class QueryResultDataTest {
    List<String> contentIds = Arrays.asList("content id 1", "content id 2");

    @Test
    public void createQueryResultDataFromCreatedIntent() {
        QueryResultData queryResultData = QueryResultData.fromContentIds(contentIds);
        assertEquals(contentIds, queryResultData.getContentIds());
    }
}
