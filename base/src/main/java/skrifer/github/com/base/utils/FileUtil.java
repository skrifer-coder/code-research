package skrifer.github.com.base.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FileUtil {

    /**
     * 根据byte数组，生成文件
     * filePath  文件路径
     * fileName  文件名称（需要带后缀，如*.jpg、*.java、*.xml）
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl      文件的URL
     * @param destFilePath 本地保存路径
     * @return 下载成功返回true，失败返回false
     */
    public static boolean downloadFile(String fileUrl, String destFilePath) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            // 设置连接超时时间
            httpURLConnection.setConnectTimeout(5000);

            // 读取数据到文件
            int responseCode = httpURLConnection.getResponseCode();

            // 总是检查HTTP响应码
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 输入流从网络获取数据
                InputStream inputStream = httpURLConnection.getInputStream();

                // 文件输出流写入到本地文件
                FileOutputStream fileOutputStream = new FileOutputStream(destFilePath);

                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();
                inputStream.close();

                return true;
            } else {
                System.out.println("GET请求未成功: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 项目临时文件的存储地址
     *
     * @return
     */
    public static String getTempFilePath() {
        String path = "/mnt/tmp/";//默认的磁盘路径或云存储路径
        return new File(path).exists() ? path : System.getProperty("java.io.tmpdir");//不存在则使用系统默认路径
    }

    public static String getTempFileNamePath(String fileFormat) {
        return getTempFilePath() + UUID.randomUUID() + "." + fileFormat;
    }


    /**
     * file.deleteOnExit() 经常会因一些机制问题导致删除失败(特别是文件被多个功能使用之后 inputstream读取等等。。。)
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file != null && file.exists()) {
            return file.delete();
        }
        return true;
    }
}
