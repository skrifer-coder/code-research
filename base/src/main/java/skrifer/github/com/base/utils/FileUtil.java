package skrifer.github.com.base.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
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

    /**
     * 方法一：使用类加载器的getResource().getPath()获取全路径再拼接文件名，最后根据文件路径获取文件流
     * 备注：jar包不可用，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public BufferedReader readFile1(String fileName) throws FileNotFoundException {

        //   /Users/zunf/code/read-resource/target/classes/
        String path = this.getClass().getClassLoader().getResource("").getPath();
        //   /Users/zunf/code/read-resource/target/classes/测试.txt
        String filePath = path + fileName;

        return new BufferedReader(new FileReader(filePath));
    }

    /**
     * 方法二：使用类加载器的getResource().getPath()，传参直接获取文件路径，再根据文件路径获取文件流
     * 备注：jar包不可用，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public BufferedReader readFile2(String fileName) throws IOException {

        //   /Users/zunf/code/read-resource/target/classes/%e6%b5%8b%e8%af%95.txt
        String filePath = this.getClass().getClassLoader().getResource(fileName).getPath();

        //可以看到上面读取到路径的中文被URLEncoder编码了，所以需要先用URLDecoder解码一下，恢复中文
        filePath = URLDecoder.decode(filePath, "UTF-8");

        return new BufferedReader(new FileReader(filePath));
    }

    /**
     * 方法三：使用类加载器的getResourceAsStream()，直接获取文件流
     * 备注：jar包可用
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public BufferedReader readFile3(String fileName) throws IOException {
        //getResourceAsStream(fileName) 等价于getResource(fileName).openStream()
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        }
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * 方法四：使用Class对象的getResourceAsStream()获取文件流
     * 备注：jar包可用
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public BufferedReader readFile4(String fileName) throws IOException {
        //getResourceAsStream(fileName) 等价于getResource(fileName).openStream()
        InputStream inputStream = this.getClass().getResourceAsStream("/" + fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        }
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * 方法五：使用Spring提供的ClassPathResource获取
     * 备注：jar包可用
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public BufferedReader readFile5(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

//    /**
//     * 方法六：使用Hutool的ResourceUtil
//     * 备注：jar包可用
//     *
//     * @param fileName
//     * @return
//     * @throws IOException
//     */
//    public BufferedReader readFile6(String fileName) throws IOException {
//        List<URL> resources = ResourceUtil.getResources(fileName);
//        URL resource = resources.get(0);
//        return new BufferedReader(new InputStreamReader(resource.openStream()));
//    }

    //Jar包启动时根据传入的不同funcation值来选择调用哪个方法测试
//    @Value("${function}")
    private int function;


//    @GetMapping("/test")
    public String test() throws IOException {
        //需要在resource里读取的文件
        String fileName = "测试.txt";

        //调用方法
        System.out.println("调用function" + function);
        BufferedReader bufferedReader = null;
        switch (function) {
            case 1:
                bufferedReader = readFile1(fileName);
                break;
            case 2:
                bufferedReader = readFile2(fileName);
                break;
            case 3:
                bufferedReader = readFile3(fileName);
                break;
            case 4:
                bufferedReader = readFile4(fileName);
                break;
            case 5:
                bufferedReader = readFile5(fileName);
                break;
//            case 6:
//                bufferedReader = readFile6(fileName);
//                break;
            default:
        }

        //读取并输出
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        System.out.println(sb);
        return sb.toString();
    }



}
