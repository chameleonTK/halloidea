package uk.ac.standrews.cs5041.idea.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseSchema schema;
    private static volatile DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.schema = new DatabaseSchema(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = schema.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void insert(String tableName, ContentValues values) {
        database.insert(tableName, null, values);
    }


    public Cursor rawQuery(String sql, String[] args) {
        return database.rawQuery("SELECT * From memos ORDER BY created_at DESC", args);
    }

    public void delete(String tableName, String whereClause, String[] args)  {
        database.delete(tableName, whereClause, args);
    }

    public void update(String tableName, ContentValues values, String whereClause, String[] args)  {
        database.update(tableName, values, whereClause, args);
    }
}