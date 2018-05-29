package com.xiyoumobile.xiyoumobileim;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lance
 * on 2018/5/29.
 */

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String CREATE_USER_DATABASE = "create table mUser ("
            + "uid integer primary key autoincrement, "
            + "uname text, "
            + "upass text)";

    private static final String DROP_EXISTS_USER = "drop table if exists mUser";

    public SQLiteDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_EXISTS_USER);
        sqLiteDatabase.execSQL(CREATE_USER_DATABASE);
    }
}
