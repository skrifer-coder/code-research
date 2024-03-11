package skrifer.github.com.mybatisplus;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skrifer.github.com.mybatisplus.generate.test.entity.MpTest;
import skrifer.github.com.mybatisplus.generate.test.entity.TravelAddress;
import skrifer.github.com.mybatisplus.generate.test.entity.贝壳租房各城市查询地址;
import skrifer.github.com.mybatisplus.generate.test.mapper.TravelAddressMapper;
import skrifer.github.com.mybatisplus.generate.test.mapper.贝壳租房价格信息Mapper;
import skrifer.github.com.mybatisplus.generate.test.mapper.贝壳租房各城市查询地址Mapper;
import skrifer.github.com.mybatisplus.generate.test.service.IMpTestService;
import skrifer.github.com.mybatisplus.generate.test.service.ITravelAddressService;
import skrifer.github.com.mybatisplus.generate.test.service.I贝壳租房价格信息Service;
import skrifer.github.com.mybatisplus.response.百度_Response;
import skrifer.github.com.mybatisplus.response.行政区划区域检索Response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class MyApplicationTest {

    @Autowired
    private TravelAddressMapper travelAddressMapper;

    @Autowired
    private 贝壳租房各城市查询地址Mapper bkMapper;

    @Autowired
    private 贝壳租房价格信息Mapper bkzfMapper;

    @Autowired
    ITravelAddressService travelAddressService;

    @Autowired
    I贝壳租房价格信息Service i贝壳租房价格信息Service;

    @Autowired
    IMpTestService mpTestService;

//    Semaphore semaphore = new Semaphore(5);

    //        @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        QueryWrapper<TravelAddress> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("city", Arrays.asList("北京市", "南京市", "徐州市", "海口市", "南宁市", "上海市", "厦门市", "杭州市", "广州市", "重庆市", "呼和浩特市","石家庄市"));
//        queryWrapper.in("city", Arrays.asList("东莞市","中山市"));
//        queryWrapper.isNull("district");
        queryWrapper.orderByAsc("id");
        List<TravelAddress> travelAddresses = travelAddressMapper.getTravelsByCustomSql();
//        List<TravelAddress> travelAddresses = travelAddressMapper.selectList(queryWrapper);
//                .stream().filter(x -> x.getId() == 2295).collect(Collectors.toList());
        for (TravelAddress travelAddress : travelAddresses) {
            if (travelAddress.getCity().contains("海外")) {
                continue;
            }
            System.out.println("当前ID进度:" + travelAddress.getId());
//            queryTengXunAPI(travelAddress);
            queryBaiduAPI(travelAddress);
            ThreadUtil.safeSleep(150);
        }
    }

    //        String ak = "3i1gItOdoMT2bnk49JZs43jml0LYPNx9"; //sj
//        String ak = "AOOw3GVGH2LsH7ls9xcaDCQPte68gvlL"; //zyf
//    String ak = "nwE1cZ90xpEAoWO36bxnNlWFZnr86alc"; //lxd
//    String ak = "96K05a4l89WmWGFosUpacxAjVux3PzPM"; //fanbin
    String ak = "z1Krm0BArQpTGIK1IBXj8QDyaqfJlqp4"; //zls

    private void queryBaiduAPI(TravelAddress travelAddress) {
        String queryUrl = String.format("https://api.map.baidu.com/place/v2/search?query=%s&tag=酒店&region=%s&output=json&ak=" + ak, travelAddress.getAddress(), travelAddress.getCity());
        String reponse = HttpUtil.get(queryUrl);
        行政区划区域检索Response 行政区划区域检索Response = JSONUtil.toBean(reponse, 行政区划区域检索Response.class);

        if (行政区划区域检索Response.getStatus() != 0 || 行政区划区域检索Response.getMessage().equals("ok") == false || 行政区划区域检索Response.getResult_type().equals("city_type")) {
            System.err.println(JSONUtil.toJsonStr(travelAddress) + " reason:" + 行政区划区域检索Response.getMessage());
            return;
        }

        if (行政区划区域检索Response.getResults().isEmpty()) {
            System.out.println(JSONUtil.toJsonStr(travelAddress) + " reason:未查询到结果");
            return;
        }
        travelAddress.setDistrict(行政区划区域检索Response.getResults().get(0).getArea());
        travelAddress.setProvince(行政区划区域检索Response.getResults().get(0).getProvince());
        travelAddress.setCity(行政区划区域检索Response.getResults().get(0).getCity());
        travelAddress.setLat(String.valueOf(行政区划区域检索Response.getResults().get(0).getLocation().getLat()));
        travelAddress.setLng(String.valueOf(行政区划区域检索Response.getResults().get(0).getLocation().getLng()));
        travelAddressService.updateById(travelAddress);
    }

    String tengxunAK = "SE3BZ-WEFW5-ICFIF-IRN7E-CF626-GJF5Y";//SJ
//    String tengxunAK = "6GBBZ-KLTWG-72FQ2-QTQCD-4ZHUK-KHFC4";//lxd

    private void queryTengXunAPI(TravelAddress travelAddress) {
        String queryUrl = String.format("https://apis.map.qq.com/ws/geocoder/v1/?key=%s&region=%s&address=%s", tengxunAK, travelAddress.getCity(), travelAddress.getAddress());
        String reponse = HttpUtil.get(queryUrl);

        百度_Response response = JSONUtil.toBean(reponse, 百度_Response.class);
        if (response.getStatus() != 0) {
            System.err.println(JSONUtil.toJsonStr(travelAddress) + " 查询失败, reason:" + response.getMessage());
            return;
        }
        travelAddress.setProvince(response.getResult().getAddress_components().getProvince());
        travelAddress.setCity(response.getResult().getAddress_components().getCity());
        travelAddress.setDistrict(response.getResult().getAddress_components().getDistrict());
        travelAddress.setLat(String.valueOf(response.getResult().getLocation().getLat()));
        travelAddress.setLng(String.valueOf(response.getResult().getLocation().getLng()));
        travelAddressService.updateById(travelAddress);

    }

    //    @Test
    public void 存储贝壳城市url() {
        String str = "{\"https://zmd.ke.com/\":\"驻马店\",\"https://mianyang.ke.com/\":\"绵阳\",\"https://jl.ke.com/\":\"吉林\",\"https://yz.fang.ke.com/\":\"扬州\",\"https://rg.ke.com/\":\"如皋\",\"https://chengde.ke.com/\":\"承德\",\"https://dl.ke.com/\":\"大连\",\"https://xg.ke.com/\":\"孝感\",\"https://yibin.ke.com/\":\"宜宾\",\"https://fz.ke.com/\":\"福州\",\"https://zhuzhou.ke.com/\":\"株洲\",\"https://lz.ke.com/\":\"兰州\",\"https://huangshi.ke.com/\":\"黄石\",\"https://zhangzhou.ke.com/\":\"漳州\",\"https://xz.ke.com/\":\"徐州\",\"https://xc.ke.com/\":\"许昌\",\"https://wzs.fang.ke.com/\":\"五指山\",\"https://gy.ke.com/\":\"贵阳\",\"https://pds.ke.com/\":\"平顶山\",\"https://neijiang.ke.com/\":\"内江\",\"https://changde.ke.com/\":\"常德\",\"https://sr.ke.com/\":\"上饶\",\"https://weihai.ke.com/\":\"威海\",\"https://ty.ke.com/\":\"太原\",\"https://ly.fang.ke.com/\":\"龙岩\",\"https://zb.ke.com/\":\"淄博\",\"https://hf.ke.com/\":\"合肥\",\"https://ks.ke.com/\":\"昆山\",\"https://liangshan.ke.com/\":\"凉山彝族自治州\",\"https://jdz.ke.com/\":\"景德镇\",\"https://guangyuan.ke.com/\":\"广元\",\"https://yc.ke.com/\":\"盐城\",\"https://su.ke.com/\":\"苏州\",\"https://dt.ke.com/\":\"大同\",\"https://hui.ke.com/\":\"惠州\",\"https://changzhou.ke.com/\":\"常州\",\"https://zunyi.ke.com/\":\"遵义\",\"https://wh.ke.com/\":\"武汉\",\"https://wz.ke.com/\":\"温州\",\"https://linyi.ke.com/\":\"临沂\",\"https://quzhou.fang.ke.com/\":\"衢州\",\"https://lyg.ke.com/\":\"连云港\",\"https://yy.ke.com/\":\"岳阳\",\"https://pingxiang.ke.com/\":\"萍乡\",\"https://jx.ke.com/\":\"嘉兴\",\"https://ez.ke.com/\":\"鄂州\",\"https://qhd.ke.com/\":\"秦皇岛\",\"https://fuzhou.ke.com/\":\"抚州\",\"https://jingzhou.ke.com/\":\"荆州\",\"https://qd.ke.com/\":\"青岛\",\"https://ls.ke.com/\":\"陵水\",\"https://leshan.ke.com/\":\"乐山\",\"https://nn.ke.com/\":\"南宁\",\"https://liuzhou.ke.com/\":\"柳州\",\"https://bj.ke.com/\":\"北京\",\"https://wn.fang.ke.com/\":\"万宁\",\"https://hy.ke.com/\":\"衡阳\",\"https://aq.ke.com/\":\"安庆\",\"https://sn.ke.com/\":\"遂宁\",\"https://xt.fang.ke.com/\":\"邢台\",\"https://ha.ke.com/\":\"淮安\",\"https://sy.ke.com/\":\"沈阳\",\"https://hhht.ke.com/\":\"呼和浩特\",\"https://lf.ke.com/\":\"廊坊\",\"https://ta.ke.com/\":\"泰安\",\"https://dazhou.ke.com/\":\"达州\",\"https://zhanjiang.ke.com/\":\"湛江\",\"https://cd.ke.com/\":\"成都\",\"https://yongzhou.ke.com/\":\"永州\",\"https://fy.ke.com/\":\"阜阳\",\"https://qn.fang.ke.com/\":\"黔南\",\"https://xiantao.ke.com/\":\"仙桃\",\"https://jian.ke.com/\":\"吉安\",\"https://jz.ke.com/\":\"晋中\",\"https://cs.ke.com/\":\"长沙\",\"https://pzh.ke.com/\":\"攀枝花\",\"https://zjk.ke.com/\":\"张家口\",\"https://nt.ke.com/\":\"南通\",\"https://byne.ke.com/\":\"巴彦淖尔\",\"https://nj.ke.com/\":\"南京\",\"https://hg.ke.com/\":\"黄冈\",\"https://luzhou.ke.com/\":\"泸州\",\"https://baotou.ke.com/\":\"包头\",\"https://xsbn.ke.com/\":\"西双版纳傣族自治州\",\"https://ny.fang.ke.com/\":\"南阳\",\"https://zj.ke.com/\":\"镇江\",\"https://hz.ke.com/\":\"杭州\",\"https://xm.ke.com/\":\"厦门\",\"https://fcg.ke.com/\":\"防城港\",\"https://jiyuan.fang.ke.com/\":\"济源\",\"https://baoji.ke.com/\":\"宝鸡\",\"https://haian.ke.com/\":\"海安\",\"https://mas.ke.com/\":\"马鞍山\",\"https://hd.ke.com/\":\"邯郸\",\"https://weinan.fang.ke.com/\":\"渭南\",\"https://luohe.fang.ke.com/\":\"漯河\",\"https://xianyang.ke.com/\":\"咸阳\",\"https://jh.ke.com/\":\"金华\",\"https://hrb.ke.com/\":\"哈尔滨\",\"https://py.ke.com/\":\"濮阳\",\"https://hk.ke.com/\":\"海口\",\"https://nb.ke.com/\":\"宁波\",\"https://yinchuan.ke.com/\":\"银川\",\"https://sx.ke.com/\":\"绍兴\",\"https://taizhou.ke.com/\":\"台州\",\"https://quanzhou.ke.com/\":\"泉州\",\"https://haimen.ke.com/\":\"海门\",\"https://zs.ke.com/\":\"中山\",\"https://gl.ke.com/\":\"桂林\",\"https://xx.ke.com/\":\"湘西土家族苗族自治州\",\"https://yaan.ke.com/\":\"雅安\",\"https://ts.ke.com/\":\"唐山\",\"https://tongliao.ke.com/\":\"通辽\",\"https://ziyang.ke.com/\":\"资阳\",\"https://dy.ke.com/\":\"德阳\",\"https://sh.ke.com/\":\"上海\",\"https://luoyang.ke.com/\":\"洛阳\",\"https://jy.ke.com/\":\"江阴\",\"https://xn.fang.ke.com/\":\"咸宁\",\"https://jn.ke.com/\":\"济南\",\"https://bd.ke.com/\":\"保定\",\"https://qxn.fang.ke.com/\":\"黔西南布依族苗族自治州\",\"https://huzhou.ke.com/\":\"湖州\",\"https://bh.ke.com/\":\"北海\",\"https://heze.ke.com/\":\"菏泽\",\"https://yw.ke.com/\":\"义乌\",\"https://tianshui.ke.com/\":\"天水\",\"https://yt.ke.com/\":\"烟台\",\"https://kf.ke.com/\":\"开封\",\"https://jr.ke.com/\":\"句容\",\"https://ms.ke.com/\":\"眉山\",\"https://zk.ke.com/\":\"周口\",\"https://nanchong.ke.com/\":\"南充\",\"https://km.ke.com/\":\"昆明\",\"https://wf.ke.com/\":\"潍坊\",\"https://xa.ke.com/\":\"西安\",\"https://zjg.ke.com/\":\"张家港\",\"https://qidong.ke.com/\":\"启东\",\"https://nc.ke.com/\":\"南昌\",\"https://dg.ke.com/\":\"东莞\",\"https://cc.ke.com/\":\"长春\",\"https://shangqiu.fang.ke.com/\":\"商丘\",\"https://xy.ke.com/\":\"襄阳\",\"https://dali.ke.com/\":\"大理\",\"https://ych.ke.com/\":\"宜春\",\"https://fushun.ke.com/\":\"抚顺\",\"https://xiangtan.ke.com/\":\"湘潭\",\"https://cf.ke.com/\":\"赤峰\",\"https://dezhou.fang.ke.com/\":\"德州\",\"https://lj.fang.ke.com/\":\"丽江\",\"https://fs.ke.com/\":\"佛山\",\"https://jining.ke.com/\":\"济宁\",\"https://wlmq.ke.com/\":\"乌鲁木齐\",\"https://jiaozuo.fang.ke.com/\":\"焦作\",\"https://sjz.ke.com/\":\"石家庄\",\"https://wx.ke.com/\":\"无锡\",\"https://ganzhou.ke.com/\":\"赣州\",\"https://changshu.ke.com/\":\"常熟\",\"https://xinxiang.ke.com/\":\"新乡\",\"https://ay.fang.ke.com/\":\"安阳\",\"https://ld.fang.ke.com/\":\"乐东\",\"https://yuncheng.ke.com/\":\"运城\",\"https://gz.ke.com/\":\"广州\",\"https://zz.ke.com/\":\"郑州\",\"https://bz.ke.com/\":\"巴中\",\"https://zq.fang.ke.com/\":\"肇庆\",\"https://yichang.ke.com/\":\"宜昌\",\"https://cm.ke.com/\":\"澄迈\",\"https://tj.ke.com/\":\"天津\",\"https://jiangmen.ke.com/\":\"江门\",\"https://zhoushan.fang.ke.com/\":\"舟山\",\"https://qy.ke.com/\":\"清远\",\"https://dd.ke.com/\":\"丹东\",\"https://cq.ke.com/\":\"重庆\",\"https://jiujiang.ke.com/\":\"九江\",\"https://sz.ke.com/\":\"深圳\",\"https://dz.fang.ke.com/\":\"儋州\",\"https://san.ke.com/\":\"三亚\",\"https://wuhu.ke.com/\":\"芜湖\",\"https://danyang.ke.com/\":\"丹阳\",\"https://zh.ke.com/\":\"珠海\",\"https://hanzhong.ke.com/\":\"汉中\"}";
        JSONObject jsonObject = JSONUtil.parseObj(str);
        for (String 城市url : jsonObject.keySet()) {
            String 城市名 = (String) jsonObject.get(城市url);
            贝壳租房各城市查询地址 entity = new 贝壳租房各城市查询地址();
            entity.setCity(城市名);
            entity.setUrl(城市url);
            bkMapper.insert(entity);
        }
    }


    //    @Test
    public void 爬取贝壳租房信息() {

        QueryWrapper<贝壳租房各城市查询地址> queryWrapper = new QueryWrapper<>();
        List<贝壳租房各城市查询地址> 贝壳租房各城市查询地址s = bkMapper.selectList(queryWrapper);
//        贝壳CrawlService crawlService = new 贝壳CrawlService();
//
//        for (贝壳租房各城市查询地址 city : 贝壳租房各城市查询地址s) {
//            List<贝壳租房价格信息实体DTO> 爬取租房信息 = crawlService.爬取租房信息(city.getUrl());
//            if (爬取租房信息.isEmpty() == false) {
//                List<贝壳租房价格信息> collect = 爬取租房信息.stream().map(x -> {
//                    贝壳租房价格信息 entity = new 贝壳租房价格信息();
//                    entity.setCityName(city.getCity());
//                    entity.setDesc(x.getName() + "||" + x.getDesc());
//                    entity.setPrice1(x.getPrice1());
//                    entity.setPrice2(x.getPrice2());
//                    entity.setUnit(x.getUnit());
//                    return entity;
//                }).collect(Collectors.toList());
//                i贝壳租房价格信息Service.saveBatch(collect);
//            }
//        }
    }

    @Test
    public void MybatisPlusTest() {
        // service  curd操作
        String randomKey = "_" + RandomUtil.randomString(32);
        {
            //新增插入
            {
                MpTest mpTest = new MpTest();
                mpTest.setName("小王" + randomKey);
                mpTest.setAge(20);
                mpTest.setScore(new BigDecimal("99.5"));
                mpTestService.save(mpTest);
            }

            {
                MpTest mpTest = new MpTest();
                mpTest.setName("小新" + randomKey);
                mpTest.setAge(20);
                mpTest.setScore(new BigDecimal("99.5"));
                mpTestService.saveBatch(Collections.singletonList(mpTest));
//                mpTestService.saveBatch(Collections.singletonList(mpTest), 2);
            }


            //新增或更新(TableId 注解属性值存在则更新记录，否插入一条记录)
            {
                MpTest mpTest = new MpTest();
                mpTest.setId(1L);// core
                mpTest.setScore(new BigDecimal("0"));
                mpTestService.saveOrUpdate(mpTest);
            }

            //根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)的逻辑方法
            {
                MpTest mpTest = new MpTest();
                mpTest.setId(1L);
                mpTest.setAge(99);
                UpdateWrapper<MpTest> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("score", 0);
                mpTestService.saveOrUpdate(mpTest, updateWrapper);
            }

            {
                MpTest mpTest = new MpTest();
                mpTest.setId(1L);
                mpTest.setName("小王 变 小黑" + randomKey);
                mpTest.setAge(20);
                mpTest.setScore(new BigDecimal("99.5"));
                mpTestService.saveOrUpdateBatch(Collections.singletonList(mpTest));
//                mpTestService.saveOrUpdateBatch(Collections.singletonList(mpTest), 100);
            }

            //remove删除
            {
                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
                queryWrapper.likeLeft("name", "小新%");
                mpTestService.remove(queryWrapper);

                mpTestService.removeById(1L);

                mpTestService.removeByMap(Collections.singletonMap("id", 1L));

                mpTestService.removeByIds(Collections.singletonList("1L"));
            }


        }
    }


}
