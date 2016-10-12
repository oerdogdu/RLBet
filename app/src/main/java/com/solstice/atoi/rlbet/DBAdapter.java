package com.solstice.atoi.rlbet;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.Face;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.password;
import static java.security.AccessController.getContext;

/**
 * Created by Atoi on 13.09.2016.
 */
public class DBAdapter {
    private DatabaseHelper DBHelper;
    public static SQLiteDatabase db;
    public Context context;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "email";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHOTOURL = "url";
    private static final String TAG = "DBAdapter";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String DATABASE_NAME = "googleaccounts";
    private static final String DATABASE_NAME2 = "bets";
    private static final String DATABASE_TABLE1 = "accounts";
    private static final String DATABASE_TABLE2 = "accountsFacebook";
    private static final String DATABASE_TABLE3 = "accountsRegister";
    private static final String DATABASE_TABLE4 = "schedule";
    private static final String COLUMN_TEAM1 = "team1";
    private static final String COLUMN_TEAM2 = "team2";
    private static final String COLUMN_X1 = "x1";
    private static final String COLUMN_X2 = "x2";
    private static final int DATABASE_VERSION = 1;

    private ArrayList<Bets> betList = new ArrayList<Bets>();

    private static final String DATABASE_CREATE = "create table accounts (_id integer primary key autoincrement, "
            + "name text, email text, url text);";

    private static final String DATABASE_CREATE2 = "create table accountsFacebook (_id integer primary key autoincrement, "
            + "name text, email text)";

    private static final String DATABASE_CREATE3 = "create table accountsRegister (_id integer primary key autoincrement, "
            + "email text, password text)";

    private static final String DATABASE_CREATE4 = "create table schedule (_id integer primary key autoincrement, "
            + "team1 text, team2 text, x1 real, x2 real)";

    // Constructor
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public Context c;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            c = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("Create", "Creating the database");

            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE2);
                db.execSQL(DATABASE_CREATE3);
                db.execSQL(DATABASE_CREATE4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS accounts");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }


    public long create(GoogleAccount g) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, g.getName());
        cv.put(KEY_EMAIL, g.getEmail());
        cv.put(KEY_PHOTOURL, g.getPhotoUrl());
        return DBHelper.getReadableDatabase().insert(DATABASE_TABLE1, null, cv);
    }

    public long createFacebook(FacebookAccount f) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, f.getFirstname());
        cv.put(KEY_LASTNAME, f.getLastname());
        cv.put(KEY_EMAIL, f.getEmail());
        return DBHelper.getReadableDatabase().insert(DATABASE_TABLE2, null, cv);
    }

    public long createRegister(RegisteredAccount r) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_EMAIL, r.getEmail());
        cv.put(KEY_PASSWORD, r.getPassword());
        return DBHelper.getReadableDatabase().insert(DATABASE_TABLE3, null, cv);
    }

    public boolean checkUser(String TableName, String email, String password) {
        SQLiteDatabase sqldb = DBHelper.getReadableDatabase();
        Log.d("f", password);
        String Query = "Select * from " + TableName + " where email='" + email + "'";// AND password='"+password+"'"
        Log.d("q", Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor getCursor() {
        String[] columns = new String[]{
                COLUMN_TEAM1,
                COLUMN_X1,
                COLUMN_TEAM2,
                COLUMN_X2,
        };
        return DBHelper.getReadableDatabase().query(DATABASE_TABLE4, columns, null, null, null, null,
                null);
    }

    public void addUser(String email, String uid, String created_at) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(DATABASE_TABLE3, null, values);
        db.close();
    }

    public ArrayList<Bets> getSchedule() {
        String tag_string_req = "req_sql";
        String url = AppConfig.URL_SQL;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("lala", "lalatata");
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject o = (JSONObject) arr.get(0);
                            // Check for error node in json
                            Log.d("trial", o.getString("team1")+"");
                            Log.d("deb", "deb");

                            String team1 = o.getString("team1");
                            String team2 = o.getString("team2");
                            Double x1 = o.getDouble("x1");
                            Double x2 = o.getDouble("x2");

                            Bets b = new Bets(team1, team2, x1, x2);
                            betList.add(b);

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        AppControler.getInstance().addToRequestQueue(context, jsObjRequest, tag_string_req);

        Log.d("size", betList.size()+"");
        return betList;
    }
}
