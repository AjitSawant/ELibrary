package com.palash.sampleapp.utilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.sampleapp.R;
import com.palash.sampleapp.entiry.ELCatalog;
import com.palash.sampleapp.task.SynchronizationTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LocalSetting {

    private static final String TAG = "LocalSetting";

    //progress dialog for background task
    public TransparentProgressDialog showDialog(Context context) {
        TransparentProgressDialog progressDialog = null;
        try {
            progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
            // progressDialog.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    //hide currently showing progress dialog
    public void hideDialog(TransparentProgressDialog progressDialog) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RunScheduler(Context context) {
        try {
            SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //encodes string
    public static String encodeString(String password) {
        try {
            byte[] data = password.getBytes("UTF-8");
            password = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStirng(Date arg0, String formate) {
        SimpleDateFormat dateFormat = null;
        String result = null;
        try {
            dateFormat = new SimpleDateFormat(formate);
            result = dateFormat.format(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public void showErrorAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }

    public void showSuccessAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }

    public void showWarningAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }

    public ArrayList<ELCatalog> returnCatalog() {
        ArrayList<ELCatalog> ELCatalogArrayList = new ArrayList<>();
        ELCatalog ELCatalog1 = new ELCatalog();
        ELCatalog1.setID("0");
        ELCatalog1.setDescription("Catalog1");

        ELCatalog ELCatalog2 = new ELCatalog();
        ELCatalog2.setID("1");
        ELCatalog2.setDescription("Catalog2");

        ELCatalogArrayList.add(ELCatalog1);
        ELCatalogArrayList.add(ELCatalog2);

        return ELCatalogArrayList;
    }

    public String returnCurrentDate() {
        String mDate;
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        mDate = format.format(new Date());
        return mDate;
    }

    public String fileNameFromURI(Uri uri, Context context) {
        try {
            String uriString = uri.toString();
            String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = uri.getLastPathSegment();
            }
            return displayName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Boolean checkExtension(String fileName) {
        Boolean flag = false;
        fileName = fileName.substring(fileName.lastIndexOf("."));
        String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
        for (String extension : okFileExtensions) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return flag;
    }
}
