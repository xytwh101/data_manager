package com.hfut.buaa.data.manager.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.*;


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
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        try {
            byte[] bytes = new byte[1024];
            int len;
            inputStream = new FileInputStream(file);
            while ((len = inputStream.read(bytes)) != -1) {
                stringBuffer.append(bytesToString(bytes));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static void writeFile(String fileString, String path) {
        byte[] bytes = new byte[1024];
        File file = new File(path);
        OutputStream outputStream;
        try {
            bytes = stringToBytes(fileString);
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToString(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < src.length; i++) {
            // 之所以用byte和0xff相与，是因为int是32位，与0xff相与后就舍弃前面的24位，只保留后8位
            String str = Integer.toHexString(src[i] & 0xff);
            if (str.length() < 2) { // 不足两位要补0
                stringBuffer.append(0);
            }
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public static byte[] stringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }
}
