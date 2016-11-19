package com.hfut.buaa.data.manager.utils;

import org.apache.hadoop.conf.Configuration;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtils {
    public static Configuration configuration = new Configuration();

    /**
     * 对文件进行拷贝
     *
     * @param src
     * @param dest
     */
    public static void fileCopy(String src, String dest) {
        File sourceFile = new File(src);
        File destFile = new File(dest);
        fileCopy(sourceFile, destFile);
    }

    public static void fileCopy(File src, File dest) {
        if (src.isDirectory() || dest.isDirectory()) {
            throw new MyException("不是文件，无法进行读取");
        }
        InputStream in = null;
        OutputStream os = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] car = new byte[68];
            int len = 0;
            while (-1 != (len = in.read(car))) {
                os.write(car, 0, len);
                os.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll(os, in);
        }
    }

    /**
     * 对文件夹进行拷贝
     *
     * @param src
     * @param dest
     */
    public static void fileFolderCopy(String src, String dest) {
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (srcFile.isDirectory()) {
            File tempFile = new File(destFile, srcFile.getName());
            if (destFile.getAbsolutePath().contains(srcFile.getAbsolutePath())) {
                System.out.println("父目录不能拷贝到子目录");
                return;
            }
        }
        copyFileAndFileFolder(srcFile, destFile);
    }

    public static void copyFileAndFileFolder(File srcFile, File destFile) {
        if (srcFile.isFile()) {
            FileUtils.fileCopy(srcFile, destFile);
        } else if (srcFile.isDirectory()) {
            destFile.mkdirs();
            for (File tempFile : srcFile.listFiles()) {
                copyFileAndFileFolder(tempFile, new File(destFile, tempFile.getName()));
            }
        }
    }

    /**
     * 对文件进行拷贝以字符流
     *
     * @param src
     * @param dest
     */
    public static void fileCopyByChar(String src, String dest) {
        File srcFile = new File(src);
        File destFile = new File(dest);
        fileCopy(srcFile, destFile);
    }

    public static void fileCopyByChar(File srcFile, File destFile) {
        if (srcFile.isDirectory() || destFile.isDirectory()) {
            throw new MyException("不是文件，无法进行复制");
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(srcFile));
            writer = new BufferedWriter(new FileWriter(destFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            while (null != (line = reader.readLine())) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll(reader, writer);
        }
    }

    public static <E extends Closeable> void closeAll(E... e) {
        for (E obj : e) {
            if (obj != null) {
                try {
                    obj.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String covFile2String(String filePath) {
        File file = new File(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            byte[] bytes = new byte[1024 * 512];
            inputStream = new FileInputStream(file);
            while (inputStream.read(bytes) != -1) {
                stringBuilder.append(new String(bytes, "utf-8"));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
