package com.karbaros.transvisionassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanu on 26-Apr-17.
 */

public class DBAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "empData.db";
    private static final String TABLE_NAME = "emp_details";

    private static final String COL_1 = "_id";
    private static final String COL_2 = "_name";
    private static final String COL_3 = "_address";
    private static final String COL_4 = "_age";
    private static final String COL_5 = "_phone";
    private static final String COL_6 = "_gender";
    private static final String COL_7 = "_grade";
    private static final String COL_8 = "_place";
    private static final String COL_9 = "_dob";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 +
            " TEXT, " + COL_3 + " TEXT, " + COL_4 + " INTEGER, " + COL_5 + " TEXT, " + COL_6 + " TEXT," + COL_7+ " TEXT, "+COL_8+" TEXT,"+COL_9+" TEXT)";

    private static final String DRP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBAdapter(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL(DRP_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DRP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(Employee employee) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, employee.getName());
        contentValues.put(COL_3, employee.getAddress());
        contentValues.put(COL_4, employee.getAge());
        contentValues.put(COL_5, employee.getPhone());
        contentValues.put(COL_6, employee.getGender());
        contentValues.put(COL_7, employee.getGrade());
        contentValues.put(COL_8,employee.getPlace());
        contentValues.put(COL_9,employee.getDob());

        return ((sqLiteDatabase.insert(TABLE_NAME, null, contentValues)) == -1 ? false : true);
    }

    public List<Employee> selectData() {

        List<Employee> employeeArrayList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(result.getString(0)));
                employee.setName(result.getString(1));
                employee.setAddress(result.getString(2));
                employee.setAge(Integer.parseInt(result.getString(3)));
                employee.setPhone(Long.parseLong(result.getString(4)));
                employee.setGender(result.getString(5));
                employee.setGrade(result.getString(6));
                employee.setPlace(result.getString(7));
                employee.setDob(result.getString(8));

                employeeArrayList.add(employee);
            } while (result.moveToNext());

        }
        Log.i("db", employeeArrayList.toString());
        return employeeArrayList;

    }

}

