package cn.fm.adtest.esc;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by SDT13292 on 2017/4/27.
 */
public class ELog {

    private static final String prefix = "MT:";
    public final static int LEVEL_V = 1;
    public final static int LEVEL_D = 2;
    public final static int LEVEL_I = 3;
    public final static int LEVEL_W = 4;
    public final static int LEVEL_E = 5;

    private static int tagLevel = LEVEL_D;
    private static boolean isDebug = true;  //默认开启
    private static ECS esc;

    public static void init(String filePath) {
        isDebug = true;
        if (null != esc) {
            return;
        }
        esc = new ECS(filePath);
    }

    public static void enableDebug() {
        isDebug = true;
    }

    public static void setTagLevel(int level) {
        tagLevel = level;
    }


    public static void v(String msg) {
        if (isDebug && tagLevel <= LEVEL_V) {
            if (esc != null) {
                esc.v(generateTag(), msg);
            } else {
                Log.v(generateTag(), msg);
            }
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug && tagLevel <= LEVEL_V) {
            String newTag = generateTag(tag);
            if (esc != null) {
                esc.v(newTag, msg);
            } else {
                Log.v(newTag, msg);
            }
        }
    }

    public static void d(String msg) {
        if (isDebug && tagLevel <= LEVEL_D) {
            if (esc != null) {
                esc.d(generateTag(), msg);
            } else {
                Log.d(generateTag(), msg);
            }
        }

    }

    public static void d(String tag, String msg) {
        if (isDebug && tagLevel <= LEVEL_D) {
            String newTag = generateTag(tag);
            if (esc != null) {
                esc.d(newTag, msg);
            } else {
                Log.d(newTag, msg);
            }
        }
    }

    public static void i(String msg) {
        if (isDebug && tagLevel <= LEVEL_I) {
            if (esc != null) {
                esc.i(generateTag(), msg);
            } else {
                Log.i(generateTag(), msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug && tagLevel <= LEVEL_I) {
            String newTag = generateTag(tag);
            if (esc != null) {
                esc.i(newTag, msg);
            } else {
                Log.i(newTag, msg);
            }
        }
    }

    public static void w(String msg) {
        if (isDebug && tagLevel <= LEVEL_W) {
            if (esc != null) {
                esc.w(generateTag(), msg);
            } else {
                Log.w(generateTag(), msg);
            }
        }
    }


    public static void w(String tag, String msg) {
        if (isDebug && tagLevel <= LEVEL_W) {
            String newTag = generateTag(tag);
            if (esc != null) {
                esc.w(newTag, msg);
            } else {
                Log.w(newTag, msg);
            }
        }
    }

    public static void e(String msg) {
        if (isDebug && tagLevel <= LEVEL_E) {
            if (esc != null) {
                esc.e(generateTag(), msg);
            } else {
                Log.e(generateTag(), msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug && tagLevel <= LEVEL_E) {
            String newTag = generateTag(tag);
            if (esc != null) {
                esc.e(newTag, msg);
            } else {
                Log.e(newTag, msg);
            }
        }
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s[%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(prefix) ? tag : prefix.concat(tag);
        return tag;
    }

    private static String generateTag(String mTag) {
        if (!TextUtils.isEmpty(mTag)) {
            return prefix.concat(mTag);
        } else {
            return generateTag();
        }
    }

}
