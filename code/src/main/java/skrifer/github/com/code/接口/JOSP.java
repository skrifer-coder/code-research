package skrifer.github.com.code.接口;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JOSP {

    public static void main(String[] args) throws Exception {
        StringBuilder html = new StringBuilder();

        try {
            // 指定要读取的文本文件路径
            String filePath = "/Users/junshen/Desktop/html.rtf";

            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                html.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(html.toString()); // 将HTML字符串转换为Jsoup的Document对象



        for (Element pre : doc.getElementsByTag("pre")) {
            String html1 = pre.html();
            String tab = "";
            if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\"><span class")) {

            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">  <span class")) {
                tab = "    ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">    <span class")) {
                tab = "      ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">      <span class")) {
                tab = "        ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">        <span class")) {
                tab = "          ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">          <span class")) {
                tab = "            ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">              <span class")) {
                tab = "              ";
            } else if (html1.startsWith("<span role=\"presentation\" style=\"padding-right: 0.1px;\">                <span class")) {
                tab = "                ";
            }
            System.out.println(tab + pre.text());
        }
//        Element contentDiv = doc.getElementById("content"); // 根据id获取元素
//        System.out.println(contentDiv);
//        System.out.println(contentDiv.text()); // 输出内容 "Hello World!"

//        Elements links = doc.getElementsByTag("a"); // 获取所有链接标签
//        for (Element link : links) {
//            System.out.println(link.attr("href")); // 输出每个链接的URL属性值
//        }
        ;
    }
}
