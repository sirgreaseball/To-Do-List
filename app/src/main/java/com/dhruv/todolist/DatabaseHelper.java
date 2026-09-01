package com.dhruv.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodoDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tasks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_STATUS = "status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK + " TEXT NOT NULL, "
                + COLUMN_STATUS + " INTEGER DEFAULT 0)";

        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    // ADD TASK
    public long addTask(String task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK, task);
        values.put(COLUMN_STATUS, 0);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }


    // GET ALL TASKS
    public Cursor getAllTasks() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );
    }


    // DELETE TASK
    public void deleteTask(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                TABLE_NAME,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }


    // UPDATE TASK STATUS
    public void updateTaskStatus(int id, boolean completed) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_STATUS,
                completed ? 1 : 0
        );

        db.update(
                TABLE_NAME,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }


    // UPDATE TASK NAME
    public void updateTaskName(int id, String newName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK, newName);

        db.update(
                TABLE_NAME,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }
}