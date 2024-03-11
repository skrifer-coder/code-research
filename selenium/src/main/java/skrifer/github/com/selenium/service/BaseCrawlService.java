package skrifer.github.com.selenium.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class BaseCrawlService {

    public BaseCrawlService() {
        System.setProperty("webdriver.chrome.driver", "/Users/junshen/Downloads/chromedriver");
        ChromeOptions options = new ChromeOptions();

        options.addArguments("–incognito");//启动无痕/隐私模式

        driver = new ChromeDriver(options);

        //页面加载超时时间设置为 5s
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        //定位对象时给 10s 的时间, 如果 10s 内还定位不到则抛出异常
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //异步脚本的超时时间设置成 3s
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
    }

    public BaseCrawlService(WebDriver driver) {
        this.driver = driver;
    }

    final WebDriver driver;

    public WebDriver getDriver() {
        return this.driver;
    }
}
