package com.clicklab.sdk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.clicklab.sdk.model.event.Event;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DbEventRepository extends SQLiteOpenHelper implements EventRepository {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "com.clicklab.sdk";
    private static final int QUERY_LIMIT = 100;

    private static final String TAG = DbEventRepository.class.getName();

    private static final String EVENT_TABLE_NAME = "events";
    private static final String ID_FIELD = "id";
    private static final String EVENT_FIELD = "event";

    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + EVENT_TABLE_NAME + " (" + ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT_FIELD + " TEXT);";

    private File file;

    protected DbEventRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        file = context.getDatabasePath(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public boolean isEmpty() {
        return getEventCountFromTable() == 0;
    }

    @Override
    public void addEvent(String event) {
        addEventToTable(event);
    }

    @Override
    public void updateEvent(Event event) {
        updateEvent(event.getId(), event.getEventString());
    }

    @Override
    public List<Event> getEvents() {
        return getEventsFromTable();
    }

    @Override
    public void removeEvent(Event event) {
        removeEventFromTable(event);
    }

    private synchronized void addEventToTable(String event) {
        try {
            long result = -1;
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(EVENT_FIELD, event);
            result = db.insert(EVENT_TABLE_NAME, null, contentValues);
            if (result == -1) {
                Log.w(TAG, "Insert failed");
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "addEvent failed", e);
            // Hard to recover from SQLiteExceptions, just start fresh
            delete();
        } catch (StackOverflowError e) {
            Log.e(TAG, "addEvent failed", e);
            // potential stack overflow error when getting database on custom Android versions
            delete();
        } finally {
            close();
        }
    }

    public synchronized void updateEvent(long id, String event) {
        try {
            long result = -1;
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_FIELD, id);
            contentValues.put(EVENT_FIELD, event);
            result = db.insertWithOnConflict(
                    EVENT_TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            if (result == -1) {
                Log.w(TAG, "update failed");
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "update failed", e);
            // Hard to recover from SQLiteExceptions, just start fresh
            delete();
        } catch (StackOverflowError e) {
            Log.e(TAG, "update failed", e);
            // potential stack overflow error when getting database on custom Android versions
            delete();
        } finally {
            close();
        }
    }


    private void delete() {
        try {
            close();
            file.delete();
        } catch (SecurityException e) {
            Log.e(TAG, "delete failed", e);
        }
    }

    private synchronized void removeEventFromTable(Event event) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            db.delete(EVENT_TABLE_NAME, ID_FIELD + "=?", new String[]{String.valueOf(event.getId())});
        } catch (SQLiteException e) {
            Log.e(TAG, "remove failed", e);
            // Hard to recover from SQLiteExceptions, just start fresh
            delete();
        } catch (StackOverflowError e) {
            Log.e(TAG, "remove failed", e);
            // potential stack overflow error when getting database on custom Android versions
            delete();
        } finally {
            close();
        }
    }

    private synchronized long getEventCountFromTable() {
        long numberRows = 0;
        SQLiteStatement statement = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT COUNT(*) FROM " + EVENT_TABLE_NAME;
            statement = db.compileStatement(query);
            numberRows = statement.simpleQueryForLong();
        } catch (SQLiteException e) {
            Log.e(TAG, "getNumberRows failed", e);
            // Hard to recover from SQLiteExceptions, just start fresh
            delete();
        } catch (StackOverflowError e) {
            Log.e(TAG, "getNumberRows failed", e);
            // potential stack overflow error when getting database on custom Android versions
            delete();
        } finally {
            if (statement != null) {
                statement.close();
            }
            close();
        }
        return numberRows;
    }

    protected synchronized List<Event> getEventsFromTable() {
        List<Event> events = new LinkedList<Event>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = queryDb(
                    db, EVENT_TABLE_NAME, new String[]{ID_FIELD, EVENT_FIELD},
                    null, null, null, null,
                    ID_FIELD + " ASC", String.valueOf(QUERY_LIMIT)
            );

            while (cursor.moveToNext()) {
                events.add(new Event(cursor.getLong(0), cursor.getString(1)));
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getEvents failed", e);
            // Hard to recover from SQLiteExceptions, just start fresh
            delete();
        } catch (StackOverflowError e) {
            Log.e(TAG, "getEvents failed", e);
            // potential stack overflow error when getting database on custom Android versions
            delete();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }
        return events;
    }

    Cursor queryDb(SQLiteDatabase db, String table, String[] columns,
                   String selection, String[] selectionArgs, String groupBy,
                   String having, String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
