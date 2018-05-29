package com.xiyoumobile.xiyoumobileim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SQLiteDataBaseHelper sqLiteDataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static final String databaseName = "XiyouMobileUsers.db";
    private static final String tableName = "mUser";

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLiteDataBaseHelper = sqLiteDataBaseHelper == null ? new SQLiteDataBaseHelper(this, databaseName, null, 1) : sqLiteDataBaseHelper;
        sqLiteDatabase = sqLiteDatabase == null ? sqLiteDataBaseHelper.getWritableDatabase() : sqLiteDatabase;
        getSupportActionBar().hide();
        initView();
    }

    public void initView(){
        username = findViewById(R.id.username_edit);
        password = findViewById(R.id.pass_edit);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password.setText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = charSequence.toString();
                if (findIsUserInDataBase(sqLiteDatabase, name)) {
                    password.setText(findPasswordForUserFromDataBase(sqLiteDatabase, name));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
                    return;
                }
                User.password = pass;
                User.userName = user;
                if (!findIsUserInDataBase(sqLiteDatabase, user)) {
                    writeUserInfoToDataBase(sqLiteDatabase, user, pass);
                }
                Intent intent = new Intent(MainActivity.this,ChatRoomActivity.class);
                startActivity(intent);
            }
        });
    }

    private String findPasswordForUserFromDataBase(SQLiteDatabase sqLiteDatabase, String user) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("uname"));
                if (username.equals(user)) {
                    return cursor.getString(cursor.getColumnIndex("upass"));
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
        return "";
    }

    private boolean findIsUserInDataBase(SQLiteDatabase sqLiteDatabase, String user) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "uname = ?", new String[]{user}, null, null, null);
        return cursor.moveToFirst();
    }

    private void writeUserInfoToDataBase(SQLiteDatabase sqLiteDatabase, String user, String pass) {
        ContentValues values = new ContentValues();
        values.put("uname", user);
        values.put("upass", pass);
        sqLiteDatabase.insert(tableName, null, values);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
