package com.lven.retrofit.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.lven.retrofit.api.RestConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建目录:默认在内存卡Download
 */

public class RestFileUtils {

    // 文件
    public static final String DIR_FILE = "file";
    private static Context mContext = RestConfig.getInstance().getContext();

    /**
     * 创建文件目录
     *
     * @param dirName 目录名称
     */
    public static File createDir(String dirName) {
        if (mContext == null) {
            throw new RuntimeException("请在Application初始化 RestConfig.getInstance().setContext()");
        }
        if (TextUtils.isEmpty(dirName)) {
            dirName = DIR_FILE;
        }
        File file = null;
        if (checkSDExist()) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dirName);
        } else {
            file = new File(mContext.getFilesDir(), dirName);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.exists()) {
            return mContext.getFilesDir();
        }
        return file;
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFileDir(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.deleteOnExit(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFileDir(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    // 检查是否SDK准备好
    private static boolean checkSDExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static File createFile(String dirName, String fileName) {
        File dir = createDir(dirName);
        if (TextUtils.isEmpty(fileName)) {
            fileName = "file_" + System.currentTimeMillis();
        }
        File file = new File(dir, fileName);
        return file;
    }

    /**
     * 将文件写到SD卡
     *
     * @param is         文件流
     * @param dir        文件目录
     * @param name
     * @param onProgress
     * @return
     */
    public static File writeToDisk(InputStream is, String dir, String name, OnProgress onProgress) {
        final File file = createFile(dir, name);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte data[] = new byte[1024 * 4];
            long current = 0;
            int count;
            while ((count = bis.read(data)) != -1) {
                current += count;
                if (onProgress != null) {
                    onProgress.onProgress(current);
                }
                bos.write(data, 0, count);
            }
            bos.flush();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bos);
            close(fos);
            close(bis);
            close(is);
        }

        return file;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnProgress {
        void onProgress(long current);
    }
}
