package uk.ac.standrews.cs5041.idea.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Memo implements Serializable {
    private Date date;
    public String title, detail, tags;
    public byte[] imageBase64;
    public int id, category;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm aaa");


    public Memo(String title, String detail) {
        this.id = (int)((new Date()).getTime());
        this.date = new Date();
        this.title = title;
        this.detail = detail;
    }

    public Memo(int id, String title, String detail) {
        this.id = id;
        this.date = new Date();
        this.title = title;
        this.detail = detail;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public void save(DatabaseAccess db) {

        ContentValues values = new ContentValues();
        values.put("memo_id", this.id);
        values.put("title", this.title);
        values.put("detail", this.detail);
        values.put("longitude", 0);
        values.put("latitude", 0);
        values.put("image", this.imageBase64);
        values.put("tags", this.tags);
        values.put("category", this.category);
        values.put("video", "https://www.youtube.com/watch?v=JxHfSwpddCU");
        values.put("datetime", DatabaseSchema.getCurrentDate());
        values.put("is_archive", 0);
        values.put("created_at", DatabaseSchema.getCurrentDate());
        values.put("updated_at", DatabaseSchema.getCurrentDate());
        values.put("connected_index", 0);
        values.put("connected_order", 0);
        db.insert(DatabaseSchema.MEMO_TABLE, values);
    }

    public void update(DatabaseAccess db) {
        ContentValues values = new ContentValues();
        values.put("title", this.title);
        values.put("detail", this.detail);
        values.put("longitude", 0);
        values.put("latitude", 0);
        values.put("image", this.imageBase64);
        values.put("tags", this.tags);
        values.put("category", this.category);
        values.put("video", "https://www.youtube.com/watch?v=JxHfSwpddCU");
        values.put("datetime", DatabaseSchema.getCurrentDate());
        values.put("is_archive", 0);
        values.put("updated_at", DatabaseSchema.getCurrentDate());
        values.put("connected_index", 0);
        values.put("connected_order", 0);
        db.update(DatabaseSchema.MEMO_TABLE, values, "memo_id = ?", new String[]{id+""});
    }

    public void delete(DatabaseAccess db) {
        db.delete(DatabaseSchema.MEMO_TABLE, "memo_id = ?", new String[]{(id+"")});
    }

    public static List getAllMemos(DatabaseAccess db) {
        List memos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * From memos ORDER BY created_at DESC", null);
        cursor.moveToFirst();

        while (cursor!=null && cursor.getCount() > 0 && !cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String detail = cursor.getString(2);
            String tags = cursor.getString(5);
            int category = cursor.getInt(6);
            byte[] imageUri = cursor.getBlob(7);

            Memo m = new Memo(id, title, detail);
            m.tags = tags;
            m.imageBase64 = imageUri;
            m.category = category;
            memos.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return memos;
    }

    @Override
    public String toString() {
        return this.title;
    }
}