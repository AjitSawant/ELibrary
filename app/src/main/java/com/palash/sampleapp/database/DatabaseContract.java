package com.palash.sampleapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.palash.sampleapp.utilities.Constants;

public class DatabaseContract {

    public static final String DATABASE_NAME = "ELibrary.db";

    public static final int DATABASE_VERSION = 1;

    public class Sync implements BaseColumns {

        public static final String TABLE_NAME = "T_Sync";

        public static final String DATE_TIME = "DateTime";
        public static final String CURRENT_SYNC = "Current_Sync";
        public static final String IS_SYNCING = "Is_Syncing";

        private static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS "
                + Sync.TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Sync.DATE_TIME
                + " TEXT NULL, "
                + Sync.CURRENT_SYNC
                + " TEXT NULL, "
                + Sync.IS_SYNCING
                + " TEXT NULL)";
    }

    public static final class Login implements BaseColumns {

        private Login() {
        }

        public static final String TABLE_NAME = "T_Login";

        public static final String COLUMN_NAME_USER_ID = "UserId";
        public static final String COLUMN_NAME_LOGIN_NAME = "LoginName";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_FULLNAME = "FulName";
        public static final String COLUMN_NAME_LOGIN_STATUS = "LoginStatus";
        public static final String COLUMN_NAME_REMEMBER_ME = "RememberMe";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Login._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_USER_ID
                + " TEXT,"
                + COLUMN_NAME_LOGIN_NAME
                + " TEXT,"
                + COLUMN_NAME_PASSWORD
                + " TEXT,"
                + COLUMN_NAME_FULLNAME
                + " TEXT,"
                + COLUMN_NAME_LOGIN_STATUS
                + " TEXT,"
                + COLUMN_NAME_REMEMBER_ME
                + " TEXT"
                + ")";
    }

    public static final class Order implements BaseColumns {

        private Order() {
        }

        public static final String TABLE_NAME = "T_Order";

        public static final String COLUMN_NAME_ORDER_NUMBER = "OrderNumber";
        public static final String COLUMN_NAME_ORDER_REMARK = "OrderRemark";
        public static final String COLUMN_NAME_ORDER_ADDED_DATE = "OrderAddedDate";
        public static final String COLUMN_NAME_ORDER_UPDATED_DATE = "OrderUpdateDate";
        public static final String COLUMN_NAME_ADDED_BY = "AddedBy";
        public static final String COLUMN_NAME_UPDATED_BY = "UpdatedBy";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Order._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ORDER_NUMBER
                + " TEXT,"
                + COLUMN_NAME_ORDER_REMARK
                + " TEXT,"
                + COLUMN_NAME_ORDER_ADDED_DATE
                + " TEXT,"
                + COLUMN_NAME_ORDER_UPDATED_DATE
                + " TEXT,"
                + COLUMN_NAME_ADDED_BY
                + " TEXT,"
                + COLUMN_NAME_UPDATED_BY
                + " TEXT"
                + ")";
    }

    public static final class Item implements BaseColumns {

        private Item() {
        }

        public static final String TABLE_NAME = "T_Item";

        public static final String COLUMN_NAME_ITEM_ID = "ItemID";
        public static final String COLUMN_NAME_ORDER_NUMBER = "OrderNumber";
        public static final String COLUMN_NAME_CATALOG_ID = "CatalogID";
        public static final String COLUMN_NAME_CATALOG_NAME = "CatalogName";
        public static final String COLUMN_NAME_PAGE_NO = "PageNo";
        public static final String COLUMN_NAME_ORDER_IS_MOBILE_ATTACHMENT = "IsMobileAttachment";
        public static final String COLUMN_NAME_ATTACHMENT_DATA = "AttachmentData";
        public static final String COLUMN_NAME_ATTACHMENT_NAME = "AttachmentName";
        public static final String COLUMN_NAME_ATTACHMENT_FULL_PATH = "AttachmentFullPath";
        public static final String COLUMN_NAME_ITEM_REMARK = "ItemRemark";
        public static final String COLUMN_NAME_ITEM_ADDED_DATE = "ItemAddedDate";
        public static final String COLUMN_NAME_ITEM_UPDATED_DATE = "ItemUpdateDate";
        public static final String COLUMN_NAME_IS_TEMP_ADDED = "ItemIsTempAdded";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + Item._ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_NAME_ITEM_ID
                + " TEXT,"
                + COLUMN_NAME_ORDER_NUMBER
                + " TEXT,"
                + COLUMN_NAME_CATALOG_ID
                + " TEXT,"
                + COLUMN_NAME_CATALOG_NAME
                + " TEXT,"
                + COLUMN_NAME_PAGE_NO
                + " TEXT,"
                + COLUMN_NAME_ORDER_IS_MOBILE_ATTACHMENT
                + " TEXT,"
                + COLUMN_NAME_ATTACHMENT_DATA
                + " TEXT,"
                + COLUMN_NAME_ATTACHMENT_NAME
                + " TEXT,"
                + COLUMN_NAME_ATTACHMENT_FULL_PATH
                + " TEXT,"
                + COLUMN_NAME_ITEM_REMARK
                + " TEXT,"
                + COLUMN_NAME_ITEM_ADDED_DATE
                + " TEXT,"
                + COLUMN_NAME_ITEM_UPDATED_DATE
                + " TEXT,"
                + COLUMN_NAME_IS_TEMP_ADDED
                + " TEXT"
                + ")";
    }

    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseContract(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(this.context);
    }

    public SQLiteDatabase open() throws SQLException {
        sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        return sqLiteDatabase;
    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d(Constants.TAG, "Creating table 1 Sync: " + Sync.CREATE_TABLE);
                db.execSQL(Sync.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 2 Login: " + Login.CREATE_TABLE);
                db.execSQL(Login.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 3 Order: " + Order.CREATE_TABLE);
                db.execSQL(Order.CREATE_TABLE);
                Log.d(Constants.TAG, "Creating table 4 Item: " + Item.CREATE_TABLE);
                db.execSQL(Item.CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
