package com.karbaros.transvisionassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanu on 27-Apr-17.
 */

public class DBAdapterMap extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "map.db";
    private static final String TABLE_NAME = "rate_chart";

    private static final String COL_1 = "_id";
    private static final String COL_2 = "_unit";
    private static final String COL_3 = "_price";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 +
            " TEXT, " + COL_3 + " INTEGER )";

    private static final String DRP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBAdapterMap(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DRP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public Cursor selectData() {

        List<Employee> employeeArrayList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return result;
    }

    public boolean insertData(String unit, int price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, unit);
        contentValues.put(COL_3, price);


        return ((sqLiteDatabase.insert(TABLE_NAME, null, contentValues)) == -1 ? false : true);
    }

    public int selectPrice(String unit) {
        int price = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_NAME, new String[]{COL_3}, COL_2 + "=" + unit, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            price = mCursor.getInt(0);
        }
        return price;
    }
}
