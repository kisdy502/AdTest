package cn.fm.adtest.tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import cn.fm.adtest.esc.IOUtil;

/**
 * Created by Administrator on 2018/11/9.
 */

public class AssetHeler {
    public static final int BUFFER_SIZE = 8192;

    //asset读取保存到指定目录下
    public static void assetsFile2Disk(Resources resources, String fileName, File targetDir) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = resources.getAssets().open(fileName);
            File destFile = new File(targetDir, fileName);
            copyFile(inputStream, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    public static void copyFile(InputStream inputStream, File destFile) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = inputStream.read(buffer);
            while (len > 0) {
                fileOutputStream.write(buffer, 0, len);
                len = inputStream.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(inputStream);
            IOUtil.closeQuietly(fileOutputStream);
        }
    }


    public static String readTextFromLog(File logFile) {
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(logFile));//构造一个BufferedReader类来读取文件
            String str = null;
            while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(str);
                result.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(br);
        }
        return result.toString();
    }

    public static String getRootPath(Context context, String dirName) {
        String rootDir;
        rootDir = sdcardDirectory(context, dirName);
        if (TextUtils.isEmpty(rootDir)) {
            rootDir = appDataDirectory(context, dirName);
        }
        return rootDir;
    }

    private static String appDataDirectory(Context context, String dirName) {
        File dataFile = context.getCacheDir().getParentFile();
        File f = new File(dataFile, dirName);
        if (!f.exists() && !f.mkdirs()) {
            return null;
        }

        if (!f.isDirectory() || !f.canWrite()) {
            return null;
        }
        return f.getAbsolutePath();
    }

    private static String sdcardDirectory(Context context, String dirName) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File pkfile = new File(dir);
            if (!pkfile.exists() && !pkfile.mkdirs()) {
                return null;
            }

            if (!pkfile.isDirectory() || !pkfile.canWrite()) {
                return null;
            }

            dir += File.separator + context.getPackageName();
            File f = new File(dir, dirName);
            if (!f.exists() && !f.mkdirs()) {
                return null;
            }

            if (!f.isDirectory() || !f.canWrite()) {
                return null;
            }
            return f.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
