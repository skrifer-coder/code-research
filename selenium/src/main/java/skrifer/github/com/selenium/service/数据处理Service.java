package skrifer.github.com.selenium.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skrifer.github.com.selenium.jpadao.BKBUSINESSDATARepository;
import skrifer.github.com.selenium.jpadao.BKZFDATARepository;
import skrifer.github.com.selenium.jpadao.BKZFINFORepository;
import skrifer.github.com.selenium.jpadao._58BUSINESSDATARepository;
import skrifer.github.com.selenium.jpaentity.BKBUSINESSDATA;
import skrifer.github.com.selenium.jpaentity.BKZFDATA;
import skrifer.github.com.selenium.jpaentity.BKZFINFO;
import skrifer.github.com.selenium.jpaentity._58BUSINESSDATA;
import skrifer.github.com.selenium.service.response.贝壳租房价格信息实体DTO;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Service
public class 数据处理Service {

    @Autowired
    BKZFDATARepository bkzfdataRepository;

    @Autowired
    BKZFINFORepository bkzfinfoRepository;

    @Autowired
    BKBUSINESSDATARepository bkbusinessdataRepository;

    @Autowired
    _58BUSINESSDATARepository _58BusinessdataRepository;

    @PostConstruct
    public void init() {

//        贝壳CrawlService crawlService = new 贝壳CrawlService();
//
//        for (BKZFINFO bkzfinfo : bkzfinfoRepository.findAll().stream().filter(x -> x.getId() >=5).collect(Collectors.toList())) {
//            System.out.println("开始处理:" + bkzfinfo.getCity());
//            List<贝壳租房价格信息实体DTO> 爬取租房信息 = crawlService.爬取租房信息(bkzfinfo.getUrl());
//            if (爬取租房信息 == null) {
//                continue;
//            }
//            List<BKZFDATA> collect = 爬取租房信息.stream().map(x -> {
//                BKZFDATA info = new BKZFDATA();
//                info.setCityName(bkzfinfo.getCity());
//                info.setDistrict(x.getDesc().split("-")[0]);
//                info.setDesc(x.getName() + " |我是分割线| " + x.getDesc());
//                info.setPrice1(x.getPrice1());
//                info.setPrice2(Optional.ofNullable(x.getPrice2()).orElse(BigDecimal.ZERO));
//                info.setUnit(x.getUnit());
//                return info;
//            }).collect(Collectors.toList());
//            bkzfdataRepository.saveAll(collect);
//            System.out.println("结束处理:" + bkzfinfo.getCity());
//        }

        //处理异常数据
//        List<BKZFDATA> byDistrictLike = bkzfdataRepository.findByDistrictLike("%/%");
//
//        List<BKZFDATA> process = new ArrayList<>();
//        for (BKZFDATA bkzfdata : byDistrictLike) {
//            Long id = bkzfdata.getId();
//            List<BKZFDATA> allById = bkzfdataRepository.findAllById(Arrays.asList(id - 1, id + 3));
//
//            if (allById.get(0).getDistrict().equals(allById.get(1).getDistrict())) {
//                bkzfdata.setDistrict(allById.get(0).getDistrict());
//                process.add(bkzfdata);
//            } else {
//                System.out.println("不能判断");
//            }
//        }
//        bkzfdataRepository.saveAll(process);
//        爬取商业写字楼数据();
//        提取几厅几卫();
//        爬取商业写字楼数据();
//        爬取58同城写字楼数据();
    }

    static Map<String, String> 商业租赁列表 = new HashMap<>();

    static {
        商业租赁列表.put("北京", "https://shangye.ke.com/bj/xzl_rent.html");
        商业租赁列表.put("重庆", "https://shangye.ke.com/cq/xzl_rent.html");
        商业租赁列表.put("广州", "https://shangye.ke.com/gz/xzl_rent.html");
        商业租赁列表.put("深圳", "https://shangye.ke.com/sz/xzl_rent.html");
        商业租赁列表.put("武汉", "https://shangye.ke.com/wh/xzl_rent.html");
//        商业租赁列表.put("苏州", "https://shangye.ke.com/su/xzl/rent/mlist");
        商业租赁列表.put("上海", "https://shangye.ke.com/sh/xzl_rent.html");
        商业租赁列表.put("成都", "https://shangye.ke.com/cd/xzl_rent.html");
    }

    private void 爬取58同城写字楼数据() {

        //起始页
        String url = "https://hf.58.com/zhaozu/pve_1092_0/?PGTID=0d200001-0034-5ae1-b576-ace3d87f1423&ClickID=2";
        _58同程CrawlService crawlService = new _58同程CrawlService();
        WebDriver driver = crawlService.getDriver();
        driver.get(url);

        List<String> citys = Arrays.asList(
//                "天津",
//                "石家庄",
//                "太原",
//                "呼和浩特",
//                "沈阳",
//                "大连",
//                "长春",
//                "黑龙江",
//                "哈尔滨",
//                "南京",
//                "苏州",
//                "无锡",
//                "常州",
//                "南通",
//                "徐州",
//                "嘉兴",
//                "扬州",
//                "盐城",
//                "杭州",
//                "宁波",
//                "合肥",
//                "皖南",
//                "福州",
//                "南昌",
//                "济南",
//                "青岛",
//                "烟台",
//                "郑州",
//                "长沙",
//                "东莞",
//                "佛山",
//                "广西",
//                "柳州",
//                "南宁",
//                "海南",
//                "海口",
//                "贵阳",
//                "昆明",
//                "拉萨",
//                "西安",
//                "兰州",
//                "西宁",
//                "银川",
//                "乌鲁木齐",
                "北京",
                "重庆",
                "广州",
                "深圳",
                "武汉",
                "上海",
                "成都"
        );


        for (int i = 0; i < citys.size(); i++) {
            System.out.println("----------------------------Start-----------------------------");
            String city = citys.get(i);
            System.out.println("当前城市:" + city + " 索引:" + i);

            //切换城市
            if (driver.getCurrentUrl().contains("changecity") == false) {
                driver.findElement(By.cssSelector("#commonTopbar_ipconfig a")).click();
                _58同程验证处理(driver);
            }

            //定位城市
            List<WebElement> hotCitys = driver.findElements(By.cssSelector("a.content-city"));
            List<WebElement> normalCitys = driver.findElements(By.cssSelector("a.hot-city"));
            List<WebElement> totalCitys = new ArrayList<>();
            totalCitys.addAll(hotCitys);
            totalCitys.addAll(normalCitys);
            Optional<WebElement> any = totalCitys.stream().filter(x -> x.getText().equals(city)).findAny();
            if (any.isPresent()) {
                any.get().click();
                _58同程验证处理(driver);
            } else {
                System.err.println("当前城市:" + city + " 定位失败!");
                continue;
            }
            int page = 1;
            do {
                List<_58BUSINESSDATA> businessdataList = new ArrayList<>();
                //爬取数据
                System.out.println("当前城市:" + city + " 数据页面:" + page);
                List<WebElement> dataLis = driver.findElements(By.cssSelector("ul.list-main-style.house-list-wrap li"));
                for (WebElement dataLi : dataLis) {
                    _58BUSINESSDATA businessdata = new _58BUSINESSDATA();
                    businessdata.setCityName(city);
                    businessdata.setDesc(dataLi.findElement(By.cssSelector("div.list-info")).getText());
                    {
                        List<WebElement> elements = dataLi.findElements(By.cssSelector("div.list-info p.baseinfo span.withI"));
                        if (elements.size() > 0) {
                            String text = elements.get(0).getText();
                            if (text.contains("-")) {
                                businessdata.setDistrict(text.split("-")[0].trim());
                            } else {
                                businessdata.setDistrict(text.trim());
                            }
                            if (elements.size() > 1) {
                                businessdata.setAddress(text.split("-")[1].trim() + elements.get(1).getText().trim());
                            }
                        }
                    }
                    String price = null;
                    try {
                        price = dataLi.findElement(By.cssSelector("div.price p.up .num")).getText();//1.9、面议【放弃】
                        if (NumberUtil.isNumber(price) == false) {
                            System.out.println("价格:" + city + " 不是期望的数字，放弃!");
                            continue;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    businessdata.setAvgPrice(new BigDecimal(price));
                    businessdata.setAvgPriceUnit(dataLi.findElement(By.cssSelector("div.price p.up .unit")).getText());// 元/㎡/天
                    String monthText = dataLi.findElement(By.cssSelector("div.price p.down")).getText();//1.14万/月、8898元/月
                    String monthPrice = ReUtil.get("([0-9]+([.]{1}[0-9]+){0,1})", monthText, 0);
                    if (monthPrice != null) {
                        businessdata.setTotalPrice(new BigDecimal(monthPrice));
                        businessdata.setTotalPriceUnit(monthText.replace(monthPrice, ""));
                    }
                    String areaText = null;
                    try {
                        areaText = dataLi.findElement(By.cssSelector("div.area .num span")).getText();//200、500~1300【取上下】
                    } catch (Exception e) {
                        //可能是整栋出租 没有面价参数 暂时放弃
                        continue;
                    }
                    businessdataList.add(businessdata);
                    if (areaText.contains("~")) {
                        businessdata.setArea(new BigDecimal(areaText.split("~")[1].trim()));
                    } else {
                        businessdata.setArea(new BigDecimal(areaText.trim()));
                    }
//                    dataLi.findElement(By.cssSelector("div.area .num .unit")).getText(); //㎡
//                    dataLi.findElement(By.cssSelector("div.area .down")).getText();//建筑面积
                }
                //下一页
                if (businessdataList.isEmpty()) {
                    break;
                }
                try {
                    driver.findElement(By.cssSelector("a.next")).click();
                    _58同程验证处理(driver);
                } catch (NoSuchElementException e) {
                    //没有下一页了
                    break;
                }
                _58BusinessdataRepository.saveAll(businessdataList);
                page++;
            } while (true);
            System.out.println("当前城市:" + city + " 处理完毕");
            System.out.println("----------------------------End-----------------------------");
        }


    }

    private void 爬取商业写字楼数据() {
        String url = "https://shangye.ke.com/api/c/house/list?platform=1&device=1&page=0&city_id=510100&size=20&big_bizcircle_id=&district_id=510181&subway_id=&resblock_id=&query_content=&bigbizcircle_switch=1&business_type=2"
                .replace("size=20", "size=100");

        for (int i = 0; ; i++) {

            String requestUrl = url.replace("page=0", "page=" + i);
            String body = HttpRequest.get(requestUrl).addHeaders(headers()).execute().body();
            爬取商业写字楼数据Resp resp = JSONUtil.toBean(body, 爬取商业写字楼数据Resp.class);
            if (resp.code == 100000 && resp.getData() != null && resp.getData().getDocs().isEmpty() == false) {
                List<BKBUSINESSDATA> collect = resp.getData().getDocs().stream().map(x -> {
                    BKBUSINESSDATA result = new BKBUSINESSDATA();
                    result.setCityName(x.getCity_name());
                    result.setDistrict(x.getDistrict_name());
                    result.setAddress(x.getStreet_name());
                    result.setDesc(x.getTitle());
                    result.setArea(BigDecimal.valueOf(x.getArea()));
                    result.setAvgPrice(BigDecimal.valueOf(x.getRent_price_unit_value()));
                    result.setAvgPriceUnit(x.getRent_price_unit_desc());
                    result.setTotalPrice(BigDecimal.valueOf(x.getRent_price()));
                    result.setTotalPriceUnit("元/月");
                    if (x.getGeo() != null) {
                        result.setLat(x.getGeo().split(",")[0]);
                        result.setLng(x.getGeo().split(",")[1]);
                    }
                    result.setDataSource(JSONUtil.toJsonStr(x));
                    return result;
                }).collect(Collectors.toList());
                bkbusinessdataRepository.saveAll(collect);
//                ThreadUtil.safeSleep(RandomUtil.randomInt(1000, 3000));
            } else {
                break;
            }
        }


    }

    private void _58同程验证处理(WebDriver driver) {
        ThreadUtil.safeSleep(RandomUtil.randomInt(500, 800));
        if (driver.getTitle().startsWith("请输入验证码")) {
            driver.findElement(By.cssSelector("#btnSubmit")).click();
        }
    }

    private Map<String, String> headers() {
        Map<String, String> result = new HashMap<>();
        result.put("Accept", "application/json, text/plain, */*");
        result.put("Accept-Encoding", "gzip, deflate, br");
        result.put("Accept-Language", "zh-CN,zh;q=0.9");
        result.put("Cookie", "lianjia_uuid=6c3df187-fb7b-4724-ae7d-9f8a0f7e9711; digv_extends=%7B%22utmTrackId%22%3A%22%22%7D; ke_uuid=9f3f788bfc382d57ea56f35c182cefd2; _ga=GA1.2.1049270149.1709283130; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2218bd65a98861b7c-0d6a24b721e96-123b6650-2073600-18bd65a988718ff%22%2C%22%24device_id%22%3A%2218bd65a98861b7c-0d6a24b721e96-123b6650-2073600-18bd65a988718ff%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E4%BB%98%E8%B4%B9%E5%B9%BF%E5%91%8A%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.baidu.com%2Fother.php%22%2C%22%24latest_referrer_host%22%3A%22www.baidu.com%22%2C%22%24latest_search_keyword%22%3A%22%E8%B4%9D%E5%A3%B3%22%2C%22%24latest_utm_source%22%3A%22baidu%22%2C%22%24latest_utm_medium%22%3A%22pinzhuan%22%2C%22%24latest_utm_campaign%22%3A%22wysuzhou%22%2C%22%24latest_utm_content%22%3A%22biaotimiaoshu%22%2C%22%24latest_utm_term%22%3A%22biaoti%22%7D%7D; Hm_lvt_9dc54bdf1695ef4c9a8176708bb4279e=1709607080; Hm_lvt_e1055dad054c1f113994ec71b2fc5882=1709607132; _gid=GA1.2.1342139291.1709607671; Qs_lvt_200116=1709607870; Qs_pv_200116=4180486521326394400; select_city=330500; crosSdkDT2019DeviceId=jossd2-qkxuof-005fg30jullwi7y-9kcs257bc; Hm_lpvt_9dc54bdf1695ef4c9a8176708bb4279e=1709608125; lianjia_ssid=cff6a310-a787-4f14-bb13-7e9d2ff9fd9c; digData=%7B%22key%22%3A%22star_list_pc%22%7D; Hm_lpvt_e1055dad054c1f113994ec71b2fc5882=1709690927");
        result.put("Host", "shangye.ke.com");
        result.put("Referer", "https://shangye.ke.com/cq/xzl_rent.html");
        result.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36");
        return result;
    }


    private void 提取几厅几卫() {
        int maxId = 110977;
        int step = 100;
        for (int i = 1; ; i++) {
            System.out.println("当前进度:" + i);
            LongStream ls = LongStream.range(i * step + 1, i * step + step + 1);
            List<Long> collect = ls.boxed().collect(Collectors.toList());
//            System.out.println(collect);
            List<BKZFDATA> allById = bkzfdataRepository.findAllById(collect);
            if (allById.isEmpty()) {
                break;
            } else {
                for (BKZFDATA bkzfdata : allById) {
                    //提取厅
                    String ting = ReUtil.get("([0-9])厅", bkzfdata.getDesc(), 0);
                    if (StrUtil.isNotBlank(ting)) {
                        bkzfdata.setHall(Integer.parseInt(ting.replace("厅", "")));
                    }
                    //提取卫
                    String weisheng = ReUtil.get("([0-9])卫", bkzfdata.getDesc(), 0);
                    if (StrUtil.isNotBlank(weisheng)) {
                        bkzfdata.setToilet(Integer.parseInt(weisheng.replace("卫", "")));
                    }
                    //提取面积
                    String mianji = ReUtil.get("([0-9]+([.]{1}[0-9]+){0,1})㎡", bkzfdata.getDesc(), 0);
                    if (StrUtil.isNotBlank(mianji)) {
                        bkzfdata.setArea(new BigDecimal(mianji.replace("㎡", "").trim()));
                    }
                }
                bkzfdataRepository.saveAll(allById);
            }

//            break;
        }


    }

    @Data
    static class 爬取商业写字楼数据Resp {
        private int code;
        private String msg;
        private 爬取商业写字楼数据Resp_Data data;
    }

    @lombok.Data
    static class 爬取商业写字楼数据Resp_Data {
        private int total;
        private List<爬取商业写字楼数据Resp_Data_Doc> docs;

    }

    @Data
    static class 爬取商业写字楼数据Resp_Data_Doc {
        private String district_name;
        private String title;
        private String geo;
        private String city_name;
        private String street_name;
        private double area;//面积 （m²）
        private double rent_price;//万元/月
        private String rent_price_unit_desc;
        private double rent_price_unit_value;//元/m²/天
    }

}
