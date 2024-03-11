package skrifer.github.com.selenium.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import skrifer.github.com.selenium.service.response.贝壳租房价格信息实体DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class 贝壳CrawlService extends BaseCrawlService {

    public 贝壳CrawlService (){
        super();
    }

    public 贝壳CrawlService (WebDriver driver){
        super(driver);
    }

    String unit = "元/月";

    public static void main(String[] args) {
//        Map<String, String> 爬取城市查询地址 = 爬取城市查询地址();
//        System.out.println(JSONUtil.toJsonStr(爬取城市查询地址));
        贝壳CrawlService 贝壳CrawlService = new 贝壳CrawlService();
        贝壳CrawlService.爬取租房信息("https://yichang.ke.com/");
    }


    public Map<String, String> 爬取城市查询地址() {
        Map<String, String> result = new HashMap<>();
        WebDriver webDriver = super.driver;

        String url = "https://www.ke.com/city/";

        webDriver.get(url);

        for (WebElement element : webDriver.findElements(By.cssSelector("div.city_list li a"))) {
//
            String cityName = element.getText();
            String href = element.getAttribute("href");
            result.put(href, cityName);

        }

        return result;
    }

    /**
     * 每个城市的各区域的各租金区间（两居和三居）各自获取排名前10的信息
     * <p>
     * https://su.zu.ke.com/zufang/wujiang/l1l2rp7/
     *
     * @param url
     * @return
     */
    public List<贝壳租房价格信息实体DTO> 爬取租房信息(String url) {
        List<贝壳租房价格信息实体DTO> result = new ArrayList<>();
        WebDriver webDriver = super.driver;
        webDriver.get(url);

        //切换到租房页面
        try {
            webDriver.findElement(By.cssSelector("div.nav.typeUserInfo li[data-action*='租房']")).click();
        } catch (Exception e) {
            System.err.println("与期望页面不一致:" + url);
            return null;
        }

        //关闭二维码弹窗(如有)
        try {
            webDriver.findElement(By.cssSelector("div.mask-guarantee")).click();
        } catch (Exception e) {

        }

        //区域循环(排除掉不限)
        List<WebElement> elements = webDriver.findElements(By.cssSelector("li[data-type='district']"));

        List<String> hrefs = elements.stream().skip(1).map(element -> element.findElement(By.cssSelector("a")).getAttribute("href")).collect(Collectors.toList());
        for (String href : hrefs) {
//            String baseUrl = currentUrl + href.replace("/zufang/", "");
            //户型打钩 l1l2
            //租金循环 rp1 - rp7

            //先全局查看下全价格区间是否有数据，这样可以节省一些请求量
            String nextUrl = href + "l1l2/";
            webDriver.get(nextUrl);
            if (webDriver.findElements(By.cssSelector("p.content__title")).get(0).getText().startsWith("已为您找到 0 套")) {
                continue;
            }
            for (int i = 1; i <= 7; i++) {
                nextUrl = href + "l1l2rp" + i + "/";
                System.out.println(nextUrl);
                webDriver.get(nextUrl);
                result.addAll(解析租房列表());
                ThreadUtil.safeSleep(RandomUtil.randomInt(500, 1000));
            }
//            break;//todo
        }


        return result.stream().distinct().collect(Collectors.toList());
    }

    public List<贝壳租房价格信息实体DTO> 解析租房列表() {
        WebDriver driver = super.driver;
        List<贝壳租房价格信息实体DTO> result = new ArrayList<>();
        if (driver.findElements(By.cssSelector("p.content__title")).get(0).getText().startsWith("已为您找到 0 套")) {
            return result;
        }
        List<WebElement> elements = driver.findElements(By.cssSelector("div.content__list:first-of-type div.content__list--item--main"));
        for (WebElement element : elements) {
            String name = element.findElement(By.cssSelector("p.content__list--item--title")).getText();
            String desc = element.findElement(By.cssSelector("p.content__list--item--des")).getText();
            String price = element.findElement(By.cssSelector(".content__list--item-price")).getText();
            贝壳租房价格信息实体DTO dto = new 贝壳租房价格信息实体DTO();
            result.add(dto);
            dto.setName(name);
            dto.setDesc(desc);
            dto.setUnit(unit);
            System.out.println(name + "||" + desc + "||" + price);

            if (price.contains("-")) {
                String[] split = price.split("-");
                dto.setPrice1(getPrice(split[0]));
                dto.setPrice2(getPrice(split[1]));
            } else {
                dto.setPrice1(getPrice(price));
            }


        }
        return result;
    }

    private BigDecimal getPrice(String price) {
        String clearPrice = price.replace(unit, "").trim();
        return new BigDecimal(clearPrice);
    }

}
