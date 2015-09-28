package com.bluechilli.racingreminders.providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.bluechilli.racingreminders.models.Entity;
import com.bluechilli.racingreminders.models.SearchCriteria;
import com.bluechilli.racingreminders.stores.EntityStore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by monishi on 2/07/15.
 */

public class EntitySuggestionProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selection, final String[] selectionArgs, String sortOrder) {
        String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
        };

        MatrixCursor cursor = new MatrixCursor(columns);
        Collection<Entity> entities = new ArrayList<>();

        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            // the entered text can be found in selectionArgs[0]
            // return a cursor with appropriate data
            final String query = selectionArgs[0];
            entities = EntityStore.getInstance().searchEntities(new SearchCriteria() {{
                page = 1;
                pageSize = 50;
                searchText = query;
            }});
        }
        else {
            entities = EntityStore.getInstance().searchEntities(new SearchCriteria() {{
                page = 1;
                pageSize = 50;
            }});
        }

        for(Entity entity : entities) {
            cursor.addRow(new Object[] {
                entity.getId(),
                entity.name,
                String.format("%s - %s", entity.summary, entity.description)
            });
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}