package com.palash.sampleapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.entiry.ELOrder;
import com.palash.sampleapp.entiry.ELSync;

import java.util.ArrayList;

public class DatabaseAdapter {

    DatabaseContract databaseContract = null;

    public DatabaseAdapter(DatabaseContract contract) {
        databaseContract = contract;
    }

    public void close() {
        if (databaseContract != null) {
            databaseContract.close();
        }
    }

    public class SyncAdapter {

        private String[] projection = {
                DatabaseContract.Sync.DATE_TIME,
                DatabaseContract.Sync.CURRENT_SYNC,
                DatabaseContract.Sync.IS_SYNCING
        };

        public long Add(ELSync ELSync) {
            long res = 0;
            try {
                ContentValues values = SyncToContentValues(ELSync);
                if (values != null) {
                    if (CountByID("1") == 0) {
                        res = databaseContract.open().insert(DatabaseContract.Sync.TABLE_NAME, null, values);
                    } else {
                        Update(ELSync);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return res;
        }

        public long Update(ELSync ELSync) {
            long res = 0;
            try {
                ContentValues values = SyncToContentValues(ELSync);
                if (values != null) {
                    String WhereClause = DatabaseContract.Sync._ID + "=1";
                    res = databaseContract.open().update(DatabaseContract.Sync.TABLE_NAME, values, WhereClause, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return res;
        }

        private ArrayList<ELSync> CursorToArrayList(Cursor result) {
            ArrayList<ELSync> objELSyncs = null;
            if (result != null) {
                objELSyncs = new ArrayList<ELSync>();
                while (result.moveToNext()) {
                    ELSync objELSync = new ELSync();
                    objELSync.setDateTime(result.getString(result.getColumnIndex(DatabaseContract.Sync.DATE_TIME)));
                    objELSync.setCurrentSync(result.getString(result.getColumnIndex(DatabaseContract.Sync.CURRENT_SYNC)));
                    objELSync.setIsSyncing(result.getString(result.getColumnIndex(DatabaseContract.Sync.IS_SYNCING)));
                    objELSyncs.add(objELSync);
                }
            }
            return objELSyncs;
        }

        public ContentValues SyncToContentValues(ELSync ELSync) {
            ContentValues values = null;
            if (ELSync != null) {
                values = new ContentValues();
                values.put(DatabaseContract.Sync.DATE_TIME, ELSync.getDateTime());
                values.put(DatabaseContract.Sync.CURRENT_SYNC, ELSync.getCurrentSync());
                values.put(DatabaseContract.Sync.IS_SYNCING, ELSync.getIsSyncing());
            }
            return values;
        }

        public ArrayList<ELSync> listAll() {
            ArrayList<ELSync> objELSyncs = null;
            Cursor result = null;
            try {
                result = databaseContract.open().query(DatabaseContract.Sync.TABLE_NAME, projection, null, null, null, null, null);
                objELSyncs = CursorToArrayList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objELSyncs;
        }

        public ELSync GetSync() {
            ArrayList<ELSync> ELSyncArrayList = null;
            ELSync ELSync = null;
            Cursor result = null;
            try {
                ELSyncArrayList = listAll();
                if (ELSyncArrayList != null && ELSyncArrayList.size() > 0) {
                    ELSync = ELSyncArrayList.get(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ELSync;
        }

        public ArrayList<ELSync> listAll(String SyncID) {
            ArrayList<ELSync> objELSyncs = new ArrayList<ELSync>();
            Cursor result = null;
            try {
                String WhereClause = DatabaseContract.Sync._ID + "='" + SyncID + "'";
                result = databaseContract.open().query(DatabaseContract.Sync.TABLE_NAME,
                        projection, WhereClause,
                        null, null,
                        null, null);
                objELSyncs = CursorToArrayList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objELSyncs;
        }

        public ArrayList<ELSync> Find(String SyncID) {
            ArrayList<ELSync> objELSyncs = new ArrayList<ELSync>();
            Cursor result = null;
            try {
                String WhereClause = DatabaseContract.Sync._ID + "='" + SyncID + "'";
                result = databaseContract.open().query(DatabaseContract.Sync.TABLE_NAME,
                        projection, WhereClause,
                        null, null,
                        null, null);
                objELSyncs = CursorToArrayList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objELSyncs;
        }

        public int Count() {
            int res = 0;
            ArrayList<ELSync> list = listAll();
            if (list != null) {
                res = list.size();
            }
            return res;
        }

        public int CountByID(String ID) {
            int res = 0;
            ArrayList<ELSync> list = Find(ID);
            if (list != null) {
                res = list.size();
            }
            return res;
        }

        public void Delete() {
            try {
                databaseContract.open().delete(DatabaseContract.Sync.TABLE_NAME, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoginAdapter {

        String[] projection = {
                DatabaseContract.Login.COLUMN_NAME_USER_ID,
                DatabaseContract.Login.COLUMN_NAME_LOGIN_NAME,
                DatabaseContract.Login.COLUMN_NAME_PASSWORD,
                DatabaseContract.Login.COLUMN_NAME_FULLNAME,
                DatabaseContract.Login.COLUMN_NAME_LOGIN_STATUS,
                DatabaseContract.Login.COLUMN_NAME_REMEMBER_ME
        };

        private ContentValues LoginToContentValues(ELLogin elLogin) {
            ContentValues values = null;
            try {
                if (elLogin != null) {
                    values = new ContentValues();
                    values.put(DatabaseContract.Login.COLUMN_NAME_USER_ID, elLogin.getUserId());
                    values.put(DatabaseContract.Login.COLUMN_NAME_LOGIN_NAME, elLogin.getLoginName());
                    values.put(DatabaseContract.Login.COLUMN_NAME_PASSWORD, elLogin.getPassword());
                    values.put(DatabaseContract.Login.COLUMN_NAME_FULLNAME, elLogin.getFullName());
                    values.put(DatabaseContract.Login.COLUMN_NAME_LOGIN_STATUS, elLogin.getLoginStatus());
                    values.put(DatabaseContract.Login.COLUMN_NAME_REMEMBER_ME, elLogin.getRememberMe());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELLogin> CursorToArrayList(Cursor result) {
            ArrayList<ELLogin> listLogin = null;
            try {
                if (result != null) {
                    listLogin = new ArrayList<ELLogin>();
                    while (result.moveToNext()) {
                        ELLogin elLogin = new ELLogin();
                        elLogin.setUserId(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_USER_ID)));
                        elLogin.setLoginName(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_LOGIN_NAME)));
                        elLogin.setPassword(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_PASSWORD)));
                        elLogin.setFullName(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_FULLNAME)));
                        elLogin.setLoginStatus(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_LOGIN_STATUS)));
                        elLogin.setRememberMe(result.getString(result.getColumnIndex(DatabaseContract.Login.COLUMN_NAME_REMEMBER_ME)));
                        listLogin.add(elLogin);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listLogin;
        }

        public long create(ELLogin elLogin) {
            long rowId = -1;
            try {
                CheckLogin();
                ContentValues values = LoginToContentValues(elLogin);
                if (values != null) {
                    SQLiteDatabase db = databaseContract.open();
                    rowId = db.insert(DatabaseContract.Login.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        private void CheckLogin() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Login.TABLE_NAME, null, null);
                databaseContract.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public long updatePassword(ELLogin elLogin) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Login.COLUMN_NAME_PASSWORD, elLogin.getPassword());
                String whereClause = null;
                whereClause = DatabaseContract.Login.COLUMN_NAME_USER_ID + " = " + elLogin.getUserId();
                rowId = databaseContract.open().update(
                        DatabaseContract.Login.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateStatus(ELLogin profile) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Login.COLUMN_NAME_LOGIN_STATUS, profile.getLoginStatus());
                String whereClause = null;
                whereClause = DatabaseContract.Login.COLUMN_NAME_USER_ID + " = " + profile.getUserId();
                rowId = databaseContract.open().update(
                        DatabaseContract.Login.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELLogin> listAll() {
            ArrayList<ELLogin> listLogin = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Login.TABLE_NAME,
                        projection, null,
                        null, null, null, null);
                listLogin = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listLogin;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Login.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class OrderAdapter {

        String[] projection = {
                DatabaseContract.Order.COLUMN_NAME_ORDER_NUMBER,
                DatabaseContract.Order.COLUMN_NAME_ORDER_REMARK,
                DatabaseContract.Order.COLUMN_NAME_ORDER_ADDED_DATE,
                DatabaseContract.Order.COLUMN_NAME_ORDER_UPDATED_DATE,
                DatabaseContract.Order.COLUMN_NAME_ADDED_BY,
                DatabaseContract.Order.COLUMN_NAME_UPDATED_BY
        };

        private ContentValues OrderToContentValues(ELOrder elOrder) {
            ContentValues values = null;
            try {
                if (elOrder != null) {
                    values = new ContentValues();
                    values.put(DatabaseContract.Order.COLUMN_NAME_ORDER_NUMBER, elOrder.getOrderNumber());
                    values.put(DatabaseContract.Order.COLUMN_NAME_ORDER_REMARK, elOrder.getOrderRemark());
                    values.put(DatabaseContract.Order.COLUMN_NAME_ORDER_ADDED_DATE, elOrder.getOrderAddedDate());
                    values.put(DatabaseContract.Order.COLUMN_NAME_ORDER_UPDATED_DATE, elOrder.getOrderUpdateDate());
                    values.put(DatabaseContract.Order.COLUMN_NAME_ADDED_BY, elOrder.getAddedBy());
                    values.put(DatabaseContract.Order.COLUMN_NAME_UPDATED_BY, elOrder.getUpdatedBy());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELOrder> CursorToArrayList(Cursor result) {
            ArrayList<ELOrder> listOrder = null;
            try {
                if (result != null) {
                    listOrder = new ArrayList<ELOrder>();
                    while (result.moveToNext()) {
                        ELOrder elOrder = new ELOrder();
                        elOrder.setOrderNumber(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_ORDER_NUMBER)));
                        elOrder.setOrderRemark(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_ORDER_REMARK)));
                        elOrder.setOrderAddedDate(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_ORDER_ADDED_DATE)));
                        elOrder.setOrderUpdateDate(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_ORDER_UPDATED_DATE)));
                        elOrder.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_ADDED_BY)));
                        elOrder.setUpdatedBy(result.getString(result.getColumnIndex(DatabaseContract.Order.COLUMN_NAME_UPDATED_BY)));
                        listOrder.add(elOrder);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listOrder;
        }

        public long create(ELOrder elOrder) {
            long rowId = -1;
            try {
                ContentValues values = OrderToContentValues(elOrder);
                if (values != null) {
                    if (CountByID(elOrder.getOrderNumber()) == 0) {
                        rowId = databaseContract.open().insert(DatabaseContract.Order.TABLE_NAME, null, values);
                    } else {
                        Update(elOrder);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long Update(ELOrder elOrder) {
            long res = 0;
            try {
                ContentValues values = OrderToContentValues(elOrder);
                if (values != null) {
                    String WhereClause = DatabaseContract.Order.COLUMN_NAME_ORDER_NUMBER + "=" + elOrder.getOrderNumber();
                    res = databaseContract.open().update(DatabaseContract.Order.TABLE_NAME, values, WhereClause, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return res;
        }

        public int CountByID(String ID) {
            int res = 0;
            ArrayList<ELOrder> list = listAllID(ID);
            if (list != null) {
                res = list.size();
            }
            return res;
        }

        public ArrayList<ELOrder> listAll() {
            ArrayList<ELOrder> listOrder = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Order.TABLE_NAME, projection, null, null, null, null, null);
                listOrder = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listOrder;
        }

        public ArrayList<ELOrder> listAllID(String OrderID) {
            ArrayList<ELOrder> objELOrders = new ArrayList<ELOrder>();
            Cursor result = null;
            try {
                String WhereClause = DatabaseContract.Order.COLUMN_NAME_ORDER_NUMBER + "='" + OrderID + "'";
                result = databaseContract.open().query(DatabaseContract.Order.TABLE_NAME, projection, WhereClause, null, null, null, null);
                objELOrders = CursorToArrayList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objELOrders;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Order.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class ItemAdapter {

        String[] projection = {
                DatabaseContract.Item.COLUMN_NAME_ITEM_ID,
                DatabaseContract.Item.COLUMN_NAME_ORDER_NUMBER,
                DatabaseContract.Item.COLUMN_NAME_CATALOG_ID,
                DatabaseContract.Item.COLUMN_NAME_CATALOG_NAME,
                DatabaseContract.Item.COLUMN_NAME_PAGE_NO,
                DatabaseContract.Item.COLUMN_NAME_ORDER_IS_MOBILE_ATTACHMENT,
                DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_DATA,
                DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_NAME,
                DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_FULL_PATH,
                DatabaseContract.Item.COLUMN_NAME_ITEM_REMARK,
                DatabaseContract.Item.COLUMN_NAME_ITEM_ADDED_DATE,
                DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED,
                DatabaseContract.Item.COLUMN_NAME_ITEM_UPDATED_DATE
        };

        private ContentValues ItemToContentValues(ELItem elItem) {
            ContentValues values = null;
            try {
                if (elItem != null) {
                    values = new ContentValues();
                    values.put(DatabaseContract.Item.COLUMN_NAME_ITEM_ID, elItem.getItemID());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ORDER_NUMBER, elItem.getOrderNumber());
                    values.put(DatabaseContract.Item.COLUMN_NAME_CATALOG_ID, elItem.getCatalogID());
                    values.put(DatabaseContract.Item.COLUMN_NAME_CATALOG_NAME, elItem.getCatalogName());
                    values.put(DatabaseContract.Item.COLUMN_NAME_PAGE_NO, elItem.getPageNo());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ORDER_IS_MOBILE_ATTACHMENT, elItem.getIsMobileAttachment());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_DATA, elItem.getAttachmentData());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_NAME, elItem.getAttachmentName());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_FULL_PATH, elItem.getAttachmentFullPath());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ITEM_REMARK, elItem.getItemRemark());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ITEM_ADDED_DATE, elItem.getItemAddedDate());
                    values.put(DatabaseContract.Item.COLUMN_NAME_ITEM_UPDATED_DATE, elItem.getItemUpdateDate());
                    values.put(DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED, elItem.getItemIsTempAdded());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELItem> CursorToArrayList(Cursor result) {
            ArrayList<ELItem> listItem = null;
            try {
                if (result != null) {
                    listItem = new ArrayList<ELItem>();
                    while (result.moveToNext()) {
                        ELItem elItem = new ELItem();
                        elItem.setItemID(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ITEM_ID)));
                        elItem.setOrderNumber(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ORDER_NUMBER)));
                        elItem.setCatalogID(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_CATALOG_ID)));
                        elItem.setCatalogName(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_CATALOG_NAME)));
                        elItem.setPageNo(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_PAGE_NO)));
                        elItem.setIsMobileAttachment(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ORDER_IS_MOBILE_ATTACHMENT)));
                        elItem.setAttachmentData(result.getBlob(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_DATA)));
                        elItem.setAttachmentName(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_NAME)));
                        elItem.setAttachmentFullPath(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ATTACHMENT_FULL_PATH)));
                        elItem.setItemRemark(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ITEM_REMARK)));
                        elItem.setItemAddedDate(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ITEM_ADDED_DATE)));
                        elItem.setItemUpdateDate(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_ITEM_UPDATED_DATE)));
                        elItem.setItemIsTempAdded(result.getString(result.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED)));
                        listItem.add(elItem);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listItem;
        }

        public long create(ELItem elItem) {
            long rowId = -1;
            try {
                ContentValues values = ItemToContentValues(elItem);
                if (values != null) {
                    if (CountByID(elItem.getItemID()) == 0) {
                        rowId = databaseContract.open().insert(DatabaseContract.Item.TABLE_NAME, null, values);
                    } else {
                        Update(elItem);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long Update(ELItem elItem) {
            long res = 0;
            try {
                ContentValues values = ItemToContentValues(elItem);
                if (values != null) {
                    String WhereClause = DatabaseContract.Item.COLUMN_NAME_ITEM_ID + "=" + elItem.getItemID();
                    res = databaseContract.open().update(DatabaseContract.Item.TABLE_NAME, values, WhereClause, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return res;
        }

        public long UpdateByOrderID(String orderNumber) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED, "0");
                String whereClause = DatabaseContract.Item.COLUMN_NAME_ORDER_NUMBER + " = " + orderNumber;
                rowId = databaseContract.open().update(
                        DatabaseContract.Item.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public int CountByID(String ID) {
            int res = 0;
            ArrayList<ELItem> list = listAllID(ID);
            if (list != null) {
                res = list.size();
            }
            return res;
        }

        // list on add order screen
        public ArrayList<ELItem> listAll() {
            ArrayList<ELItem> listItem = null;
            Cursor result = null;
            try {
                String WhereClause = DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED + "= '" + "1" + "'";;
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Item.TABLE_NAME, projection, WhereClause, null, null, null, null);
                listItem = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listItem;
        }

        // list on displat all order list
        public ArrayList<ELItem> listAllID(String OrderID) {
            ArrayList<ELItem> objELItems = new ArrayList<ELItem>();
            Cursor result = null;
            try {
                String WhereClause = DatabaseContract.Item.COLUMN_NAME_ORDER_NUMBER + "='" + OrderID + "'" +"AND " +
                DatabaseContract.Item.COLUMN_NAME_IS_TEMP_ADDED + "= '" + "0" + "'";;
                result = databaseContract.open().query(DatabaseContract.Item.TABLE_NAME, projection, WhereClause, null, null, null, null);
                objELItems = CursorToArrayList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objELItems;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Item.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteItem(String ItemID) {
            try {
                String WhereClause = DatabaseContract.Item.COLUMN_NAME_ITEM_ID + "='" + ItemID + "'";
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Item.TABLE_NAME, WhereClause, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}