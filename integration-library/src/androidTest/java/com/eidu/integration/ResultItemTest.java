package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

public class ResultItemTest {
    @Test
    public void serializesAndDeserializesNonNullValues() throws JSONException {
        ResultItem expected =
                new ResultItem(
                        "id1",
                        true,
                        "challenge",
                        "givenResponse",
                        "correctResponse",
                        1f,
                        1000L,
                        500L);
        ResultItem actual = ResultItem.fromJson(expected.toJson());
        assertEquals(expected, actual);
    }

    @Test
    public void serializesAndDeserializesNullValues() throws JSONException {
        ResultItem expected = new ResultItem(null, null, null, null, null, null, null, null);
        ResultItem actual = ResultItem.fromJson(expected.toJson());
        assertEquals(expected, actual);
    }
}
