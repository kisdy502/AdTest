package cn.fm.adtest;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import cn.fm.adtest.esc.ELog;
import cn.fm.adtest.tool.AssetHeler;

/**
 * Created by Administrator on 2018/11/8.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String logDir = AssetHeler.getRootPath(this, "log");
        Log.d("App", "logDir:" + logDir);
        if (!TextUtils.isEmpty(logDir)) {
            ELog.init(logDir);
        }
        ELog.enableDebug();
        ELog.setTagLevel(ELog.LEVEL_D);
    }
}
