package cn.fm.adtest.tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2018/11/14.
 */

public class FileHelper {

    public static void fileCopy_Channel(String srcPath, String destPath) {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(srcPath).getChannel();
            output = new FileOutputStream(destPath).getChannel();
            /**
             * Transfers bytes into this channel's file from the given readable byte channel.
             *  @param  src
                 *         The source channel
                 *
                 * @param  position
                 *         The position within the file at which the transfer is to begin;
                 *         must be non-negative
                 *
                 * @param  count
                 *         The maximum number of bytes to be transferred; must be
                 *         non-negative
             */
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
