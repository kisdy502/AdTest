package cn.fm.adtest.tool;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import cn.fm.adtest.esc.ELog;

public class CMD {
    private final static String TAG = "CMD";
    private static CMD Instance = null;
    public static boolean isadb = true;
    private static Runtime localRuntime = null;
    private static String localUrl = null;
    public static boolean iscde = false;

    public static CMD Instance() {
        if (Instance == null)
            Instance = new CMD();
        return Instance;
    }

    public CMD() {
        try {
            if (localRuntime == null) {
                localRuntime = Runtime.getRuntime();
            }
        } catch (Throwable localThrowable) {
            System.out.println("Create CMD service failure");
        }
    }

    private String Get_adbUrl() {
        if (localUrl == null) {
            localUrl = "127.0.0.1:5555";
            try {
                Process devProcess = localRuntime.exec("adb devices");
                devProcess.waitFor();
                new Thread(new InputStreamRunnable(devProcess.getErrorStream()))
                        .start();
                BufferedReader localBufferedReader = new BufferedReader(
                        new InputStreamReader(devProcess.getInputStream()));
                String adbstr = "";
                String readLine = null;
                while ((readLine = localBufferedReader.readLine()) != null) {
                    adbstr = adbstr + readLine;
                }
                ELog.i(TAG, "adbstr:" + adbstr);
                devProcess.destroy();
                if ((!"".equals(adbstr)) && (adbstr.length() > 7)) {
                    if (adbstr.indexOf("emulator-5554") <= -1) {
                        String cmdstr = "";
                        Process adbProcess = localRuntime
                                .exec("adb connect 127.0.0.1");
                        adbProcess.waitFor();
                        new Thread(new InputStreamRunnable(
                                adbProcess.getErrorStream())).start();
                        BufferedReader adbReader = new BufferedReader(
                                new InputStreamReader(
                                        adbProcess.getInputStream()));
                        readLine = null;
                        while ((readLine = adbReader.readLine()) != null) {
                            cmdstr = cmdstr + readLine;
                        }
                        ELog.i(TAG, "cmdstr:" + cmdstr);
                        if (!cmdstr.equals("") && cmdstr.length() > 10) {
                            if (cmdstr.startsWith("already connected to")
                                    || cmdstr.startsWith("connected to")
                                    || checkProt(5555)) {
                                isadb = true;
                            }
                        }
                        adbProcess.destroy();

                    } else {
                        isadb = true;
//						localUrl = "emulator-5554";
                    }
                }
            } catch (Exception e) {
                System.out.println("adb connected failure");
            }
        }
        return localUrl;
    }

    public boolean checkProt(int paramInt) {
        try {
            new Socket(InetAddress.getLocalHost(), paramInt);
            ELog.d(TAG, "checkProt:" + true);
            return true;
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return false;
    }


    public String exec(boolean isto, String[] str) {
        String result = "";
        try {
            if (localRuntime == null)
                return result;
            Process localProcess = null;
            for (int i = 0; i < str.length; i++) {
                localProcess = localRuntime.exec(str[i]);
                localProcess.waitFor();
            }

            if ((!isto) || (localProcess == null))
                return result;
            new Thread(new InputStreamRunnable(localProcess.getErrorStream()))
                    .start();
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localProcess.getInputStream()));
            String readLine = null;
            while ((readLine = localBufferedReader.readLine()) != null) {
                result = result + readLine + "\r\n";
            }

        } catch (Exception e) {
            System.out.println("run exec failure");
        }
        return result;
    }

    public boolean system_install(String path, String system) {
        if (!isadb)
            return false;
        try {
            String localhost = Get_adbUrl();
            if (localhost.contains("127.0.0.1")) {
                localRuntime.exec("adb connect 127.0.0.1");

            }
            localRuntime.exec(
                    String.format(
                            "adb -s %s shell mount -o remount rw /system",
                            new Object[]{localhost})).waitFor();
            Process localProcess = localRuntime.exec(String.format(
                    "adb -s %s push %s %s", new Object[]{localhost, path,
                            system}));
            localProcess.waitFor();
            BufferedReader systemReader = new BufferedReader(
                    new InputStreamReader(localProcess.getInputStream()));
            String str = "";
            String readLine = null;
            while ((readLine = systemReader.readLine()) != null) {
                str = str + readLine;

            }

            if (str.contains("KB")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean uninstall(String paramString) {
        try {
            if (!isadb)
                return false;
            String str1 = Get_adbUrl();
            if (str1.contains("127.0.0.1"))
                localRuntime.exec("adb connect 127.0.0.1");
            Process localProcess = localRuntime.exec(String.format(
                    "adb -s %s uninstall %s",
                    new Object[]{str1, paramString}));
            localProcess.waitFor();
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localProcess.getInputStream()));
            String str = "";
            String readLine = null;
            while ((readLine = localBufferedReader.readLine()) != null) {
                str = readLine + str;
            }
            if (str.equals("") || !str.toLowerCase().contains("success"))
                return false;
            return true;

        } catch (Throwable localThrowable) {
        }
        return false;
    }

    public String w(String paramString) {
        return w(false, paramString);
    }

    public String w(boolean paramBoolean, String paramString) {
        if (isadb) {
            String str = Get_adbUrl();
            ELog.i(TAG, "str:" + str);
            String[] arrayOfString = new String[1];
            arrayOfString[0] = String.format("adb shell %s",
                    new Object[]{paramString});//str,  -s %s
            ELog.i(TAG, "real cmdString:" + arrayOfString[0]);
            return exec(paramBoolean, arrayOfString);
        }
        return exec(paramBoolean, new String[]{paramString});
    }

    private void isDirectory(File paramFile) {
        if (!paramFile.isDirectory())
            w(String.format("mkdir %s",
                    new Object[]{paramFile.getAbsolutePath()}));
        if (!paramFile.isDirectory())
            return;
        w(String.format("chmod 777 %s",
                new Object[]{paramFile.getAbsolutePath()}));
    }

    class InputStreamRunnable implements Runnable {
        BufferedReader bReader = null;

        public InputStreamRunnable(InputStream localInputStream) {
            try {
                this.bReader = new BufferedReader(new InputStreamReader(
                        new BufferedInputStream(localInputStream), "UTF-8"));
            } catch (Throwable localThrowable) {
            }
        }

        public void run() {
            String msg = "";
            try {
                String readline = null;
                while ((readline = this.bReader.readLine()) != null) {
                    msg += readline;
                }

                if (this.bReader != null)
                    this.bReader.close();
                if (!TextUtils.isEmpty(msg))
                    System.out.println("adb error = " + msg);
            } catch (Throwable localThrowable) {
            }
        }
    }
}