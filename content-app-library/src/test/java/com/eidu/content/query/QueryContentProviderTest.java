package com.eidu.content.query;

import static com.eidu.content.query.QueryContentProvider.AVAILABLE_UNIT_IDS_COLUMN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.database.Cursor;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class QueryContentProviderTest {
    List<String> contentIds = Arrays.asList("content id 1", "content id 2", "content id 3");

    @Test
    public void getContentIds() {
        Cursor cursor = mockCursor();
        assertEquals(contentIds, QueryContentProvider.getContentIds(cursor));
    }

    private Cursor mockCursor() {
        Cursor mockCursor = mock(Cursor.class);
        int columnIndex = 0;

        when(mockCursor.getColumnIndex(AVAILABLE_UNIT_IDS_COLUMN)).thenReturn(columnIndex);

        when(mockCursor.moveToFirst()).thenReturn(true);

        when(mockCursor.getString(columnIndex))
                .thenReturn(contentIds.get(0))
                .thenReturn(contentIds.get(1))
                .thenReturn(contentIds.get(2));

        when(mockCursor.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);

        return mockCursor;
    }
}
