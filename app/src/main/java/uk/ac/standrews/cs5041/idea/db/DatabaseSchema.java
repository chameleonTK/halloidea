package uk.ac.standrews.cs5041.idea.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseSchema extends SQLiteOpenHelper {
    public static final String DATABASE = "memos.db";
    public static final int VERSION = 12;
    public static final String MEMO_TABLE = "memos";
    public static final String TAG_TABLE = "memos";
    public static final String CATEGORY_TABLE = "memos";

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
    public DatabaseSchema(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createMemo(db);
        this.createTag(db);
        this.createCategory(db);
    }

    public void createMemo(SQLiteDatabase db) {
        String sql = "";
        sql += "CREATE TABLE IF NOT EXISTS "+MEMO_TABLE+"(";
        sql += "    memo_id INTEGER PRIMARY KEY, ";
        sql += "    title TEXT, ";
        sql += "    detail TEXT, ";
        sql += "    longitude REAL, ";
        sql += "    latitude REAL, ";
        sql += "    tags TEXT, ";
        sql += "    category INTEGER, ";
        sql += "    image BLOB, ";
        sql += "    video TEXT, ";
        sql += "    datetime TEXT, ";
        sql += "    is_archive INTEGER, ";
        sql += "    created_at TEXT, ";
        sql += "    updated_at TEXT, ";
        sql += "    connected_index INTEGER, ";
        sql += "    connected_order INTEGER";
        sql += ");";

        db.execSQL(sql);
    }

    public void createTag(SQLiteDatabase db) {
        String sql = "";
        sql += "CREATE TABLE IF NOT EXISTS "+TAG_TABLE+"(";
        sql += "    tag_id INTEGER PRIMARY KEY, ";
        sql += "    text TEXT, ";
        sql += "    slug TEXT";
        sql += ");";

        db.execSQL(sql);
    }

    public void createCategory(SQLiteDatabase db) {
        String sql = "";
        sql += "CREATE TABLE IF NOT EXISTS "+CATEGORY_TABLE+"(";
        sql += "    tag_id INTEGER PRIMARY KEY, ";
        sql += "    text TEXT, ";
        sql += "    icon TEXT, ";
        sql += "    colur TEXT, ";
        sql += "    slug TEXT";
        sql += ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);

        onCreate(db);
    }
}