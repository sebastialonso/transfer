package com.sebastialonso.transfer.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sebastialonso.transfer.models.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seba on 30-07-16.
 */
public class AccountHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 2;
    // Database name
    private static final String DATABASE_NAME = "transfer_db";
    // Accounts table name
    private static final String ACCOUNTS_TABLE = "accounts";
    // Accounts table columns
    private static final String KEY_ID = "id";
    private static final String KEY_BANK_NAME = "bank";
    private static final String KEY_ACCONT_NO = "number";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_RUT = "rut";
    private static final String KEY_TYPE = "type";

    // Logs
    private static final String TAG = "AccountHandler";

    public AccountHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + ACCOUNTS_TABLE + "(" +
                KEY_ID + " TEXT PRIMARY KEY," + KEY_BANK_NAME + " TEXT," +
                KEY_ACCONT_NO + " TEXT," + KEY_OWNER + " TEXT," +
                KEY_RUT + " TEXT," + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE);
        onCreate(db);
    }

    // Count records
    public int countRecords() {
        String selectQuery = "select * from " + ACCOUNTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Create account
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, account.getAccountId());
        values.put(KEY_BANK_NAME, account.getBankName());
        values.put(KEY_ACCONT_NO, account.getAccountNumber());
        values.put(KEY_OWNER, account.getOwnerName());
        values.put(KEY_RUT, account.getOwnerRut());
        values.put(KEY_TYPE, account.getAccountType());

        db.insert(ACCOUNTS_TABLE, null, values);
        db.close();
    }

    // Get all account
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        String selectQuery = "SELECT * FROM " + ACCOUNTS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setId(cursor.getString(0));
                Log.d(TAG, cursor.getString(0));
                account.setBankName(cursor.getString(1));
                Log.d(TAG, cursor.getString(1));
                account.setAccountNumber(cursor.getString(2));
                Log.d(TAG, cursor.getString(2));
                account.setOwnerName(cursor.getString(3));
                Log.d(TAG, cursor.getString(3));
                account.setOwnerRut(cursor.getString(4));
                Log.d(TAG, cursor.getString(4));
                account.setAccountType(cursor.getString(5));
                Log.d(TAG, cursor.getString(5));

                accounts.add(account);
            } while (cursor.moveToNext());
        }

        return accounts;
    }
    // Remove account
    public void removeAccount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNTS_TABLE, KEY_ID + " = ?", new String[] { id});
        db.close();
    }

    public void removeAccounts(String ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNTS_TABLE, KEY_ID + " = ?", new String[] { ids});
        db.close();
    }

    // Get latest account
    public Account last() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ACCOUNTS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToPosition(cursor.getCount() - 1);

        Account account = new Account();
        account.setId(cursor.getString(0));
        account.setBankName(cursor.getString(1));
        account.setAccountNumber(cursor.getString(2));
        account.setOwnerName(cursor.getString(3));
        account.setOwnerRut(cursor.getString(4));
        account.setAccountType(cursor.getString(5));

        return account;
    }
}
