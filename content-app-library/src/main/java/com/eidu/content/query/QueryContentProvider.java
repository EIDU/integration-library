package com.eidu.content.query;

import android.database.Cursor;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QueryContentProvider {

    public static final String AVAILABLE_UNIT_IDS_COLUMN = "availableUnitIds";

    public static List<String> getContentIds(@NonNull Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(AVAILABLE_UNIT_IDS_COLUMN);
        if (columnIndex == -1) {
            return Collections.emptyList();
        }

        if (cursor.moveToFirst()) {
            List<String> contentIds = new ArrayList<>();
            do {
                contentIds.add(cursor.getString(columnIndex));
            } while (cursor.moveToNext());
            return contentIds;
        }
        return Collections.emptyList();
    }

    private QueryContentProvider() {}
}
