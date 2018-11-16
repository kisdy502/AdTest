package cn.fm.adtest.tool;

/**
 * Created by Administrator on 2018/11/5.
 */

public class AdHelper {
    static {
        System.loadLibrary("AdHelper");
    }
    public static native void testC(String cstr);
}
