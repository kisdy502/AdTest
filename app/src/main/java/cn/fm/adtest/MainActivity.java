package cn.fm.adtest;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.fm.adtest.esc.ECS;
import cn.fm.adtest.esc.ELog;
import cn.fm.adtest.tool.AssetHeler;
import cn.fm.adtest.tool.FileHelper;
import cn.fm.adtest.tool.ShellUtil;
import cn.fm.adtest.widget.ToastManager;

public class MainActivity extends AppCompatActivity {

    final static String AD_NAME = "bootvideo.ts";
    final static String AD_FILE_PATH = "/data/local/bootvideo.ts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File advertFile = new File(getFilesDir(), AD_NAME);
        if (!advertFile.exists()) {
            ELog.d("not exist:" + advertFile.getPath());
            AssetHeler.assetsFile2Disk(getResources(), AD_NAME, getFilesDir());
        } else {
            ELog.d("exist:" + advertFile.getPath());
        }


        findViewById(R.id.test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File advertFile = new File(getFilesDir(), AD_NAME);
                if (advertFile.exists()) {
                    ToastManager.show(advertFile.getAbsolutePath() + ":exist");
                } else {
                    ToastManager.show(advertFile.getAbsolutePath() + ":not exist");
                }
            }
        });

        findViewById(R.id.test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
//                        cpFileToSystem();
                        File advertFile = new File(getFilesDir(), AD_NAME);
                        String soPath = advertFile.getPath();
                        FileHelper.fileCopy_Channel(soPath, AD_FILE_PATH);
                    }
                }.start();
            }
        });


        findViewById(R.id.test3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String result = disconnectAdb();
                        ToastManager.showAsync("disconnectAdb:" + result);
                    }
                }.start();

            }
        });


        findViewById(R.id.test4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String result = systemAdFileExist();
                        ToastManager.showAsync("systemAdFileExist:" + result);
                    }
                }.start();
            }
        });

        findViewById(R.id.test5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String result = delSystemAdFile();
                        ToastManager.showAsync("delSystemAdFile:" + result);
                    }
                }.start();

            }
        });

        findViewById(R.id.test6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logDir = AssetHeler.getRootPath(App.getInstance(), "log");
                File logFile = new File(logDir, ECS.LOG_FILE_NAME);
                if (logFile.exists()) {
                    String allLog = AssetHeler.readTextFromLog(logFile);
                    EditText txtLog = findViewById(R.id.loginfo);
                    if (!TextUtils.isEmpty(allLog)) {
                        txtLog.setText(allLog);
                    }
                }
            }
        });


        String model = Build.MODEL;
        ELog.d("model:" + model);
        try {
            String encodeStr = URLEncoder.encode(model, "utf-8");
            ELog.d("encodeStr:" + encodeStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    void cpToSystemDir() {
//                String cmd0 = "adb connect 127.0.0.1:5555";
//                String cmd1 = "adb shell su root";
//                String cmd2 = "adb shell chmod -R 777 /data/local";
//                String cmd3 = "adb shell ls -la";
//                String cmd4 = "adb shell cp " + testFile.getAbsolutePath() + " /data/local";
//                String[] commonds = new String[]{cmd0, cmd1, cmd2, cmd3, cmd4};
//                ShellUtils.execCommand(commonds, true, true);
    }


    String disconnectAdb() {
        String result = ShellUtil.execRootCMD("adb disconnect 127.0.0.1");
        ELog.i(result);
        return result;
    }

    //adb -s %s shell su -c 'chmod 777 /data/local'
    //adb -s %s shell su -c 'cp %s /data/local'
    void cpFileToSystem() {
        String device = "127.0.0.1:5555";
        ELog.i(ShellUtil.execRootCMD("adb connect 127.0.0.1"));
        ELog.e("test", ShellUtil.execRootCMD(String.format("adb -s %s shell chmod 777 /data/local", device)));
        File advertFile = new File(getFilesDir(), AD_NAME);
        String soPath = advertFile.getPath();
        String cp = String.format("adb -s %s shell cp %s /data/local", device, soPath);
        ELog.e("test", ShellUtil.execRootCMD(cp));
    }

    String delSystemAdFile() {
        String device = "127.0.0.1:5555";
        ELog.i(ShellUtil.execRootCMD("adb connect 127.0.0.1"));
        //adb -s %s shell su -c 'rm /data/local/bootvideo.ts'"
        String cmd = "adb -s %s shell rm /data/local/bootvideo.ts";
        String result = ShellUtil.execRootCMD(String.format(cmd, device));
        ELog.e("test", result);
        return result;
    }

    String systemAdFileExist() {
        String device = "127.0.0.1:5555";
        ELog.i(ShellUtil.execRootCMD("adb devices"));
        ELog.i(ShellUtil.execRootCMD("adb connect 127.0.0.1"));
        //adb -s %s shell su -c 'ls -la /data/local'
        String cmd = "adb -s %s shell ls -la /data/local";
        String result = ShellUtil.execRootCMD(String.format(cmd, device));
        ELog.e("test", result);
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
