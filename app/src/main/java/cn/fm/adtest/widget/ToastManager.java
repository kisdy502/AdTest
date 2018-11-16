package cn.fm.adtest.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import cn.fm.adtest.App;
import cn.fm.adtest.R;

/**
 * Created by Administrator on 2018/11/9.
 */

public class ToastManager {
    private static ToastManager mInstance;
    private Toast mToast;
    private Context mCtx;
    private TextView txtToast;

    private ToastManager() {
        mCtx = App.getInstance();
        mToast = new Toast(mCtx);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        LayoutInflater inflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        txtToast = (TextView) inflater.inflate(
                R.layout.toast_manager_layout, null);
        mToast.setView(txtToast);
    }

    private static void newInstance() {
        if (mInstance == null) {
            mInstance = new ToastManager();
        }
    }

    public static void show(String txt) {
        newInstance();
        mInstance.showTxt(txt);
    }

    public static void show(int resId) {
        newInstance();
        mInstance.showInt(resId);
    }

    public static void showAsync(final String txt) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                show(txt);
            }
        });
    }

    public static void showAsync(final int resId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                show(resId);
            }
        });
    }

    public static void cancel() {
        newInstance();
        mInstance.cancelToast();
    }

    private void showTxt(String txt) {
        txtToast.setText(txt);
        mToast.show();
    }

    private void showInt(int id) {
        String txt = mCtx.getResources().getString(id);
        txtToast.setText(txt);
        mToast.show();
    }

    private void cancelToast() {
        mToast.cancel();
    }
}
