package skrifer.github.com.mybatisplus;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skrifer.github.com.mybatisplus.generate.test.entity.MpTest;
import skrifer.github.com.mybatisplus.generate.test.entity.TravelAddress;
import skrifer.github.com.mybatisplus.generate.test.entity.贝壳租房各城市查询地址;
import skrifer.github.com.mybatisplus.generate.test.mapper.MpTestMapper;
import skrifer.github.com.mybatisplus.generate.test.mapper.TravelAddressMapper;
import skrifer.github.com.mybatisplus.generate.test.mapper.贝壳租房价格信息Mapper;
import skrifer.github.com.mybatisplus.generate.test.mapper.贝壳租房各城市查询地址Mapper;
import skrifer.github.com.mybatisplus.generate.test.service.IMpTestService;
import skrifer.github.com.mybatisplus.generate.test.service.ITravelAddressService;
import skrifer.github.com.mybatisplus.generate.test.service.I贝壳租房价格信息Service;
import skrifer.github.com.mybatisplus.response.百度_Response;
import skrifer.github.com.mybatisplus.response.行政区划区域检索Response;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    MpTestMapper mpTestMapper;

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
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setName("小王" + randomKey);
//                    mpTest.setAge(20);
//                    mpTest.setScore(new BigDecimal("99.5"));
//                    mpTestService.save(mpTest);
//                }
//
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setName("小新" + randomKey);
//                    mpTest.setAge(20);
//                    mpTest.setScore(new BigDecimal("99.5"));
//                    mpTestService.saveBatch(Collections.singletonList(mpTest));
//                }
//
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setName("小白" + randomKey);
//                    mpTest.setAge(20);
//                    mpTest.setScore(new BigDecimal("99.5"));
//                    mpTestService.saveBatch(Collections.singletonList(mpTest), 2);
//                }

            }

            //新增或更新
            {
//                //TableId 注解属性值存在则更新记录，否插入一条记录
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setId(1L);// core
//                    mpTest.setScore(new BigDecimal("0"));
////                    mpTestService.saveOrUpdate(mpTest);
//                }
//
//                //根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)的逻辑方法
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setId(1L);
//                    mpTest.setAge(99);
//                    UpdateWrapper<MpTest> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("score", 0);
////                    mpTestService.saveOrUpdate(mpTest, updateWrapper);
//                }
//
//                {
//                    MpTest mpTest = new MpTest();
//                    mpTest.setId(1L);
//                    mpTest.setName("小王 变 小黑" + randomKey);
//                    mpTest.setAge(20);
//                    mpTest.setScore(new BigDecimal("99.5"));
////                    mpTestService.saveOrUpdateBatch(Collections.singletonList(mpTest));
////                mpTestService.saveOrUpdateBatch(Collections.singletonList(mpTest), 100);
//                }
            }

            //remove删除
            {
                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
                LambdaQueryWrapper<MpTest> lambda = queryWrapper.lambda();
//                lambda.
//                queryWrapper.likeLeft("name", "小新%");
//                mpTestService.remove(queryWrapper);
//
//                mpTestService.removeById(1L);
//
//                mpTestService.removeByMap(Collections.singletonMap("id", 1L));
//
//                mpTestService.removeByIds(Collections.singletonList(1L));
            }

            //update

            {
                // 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
//                UpdateWrapper<MpTest> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.setSql("name='6666',age=77");
//                updateWrapper.eq("id", 3L);
//                mpTestService.update(updateWrapper);
//                // 根据 whereWrapper 条件，更新记录
//                MpTest mpTest = new MpTest();
//                mpTest.setDelInd(true);
//                mpTestService.update(mpTest, updateWrapper);
//                // 根据 ID 选择修改
//                mpTest.setId(3L);
//                mpTest.setScore(new BigDecimal(-1));
//                mpTestService.updateById(mpTest);
//                // 根据ID 批量更新
//                mpTest.setAge(3);
//                mpTestService.updateBatchById(Collections.singletonList(mpTest));
//                mpTestService.updateBatchById(Collection<T> entityList, int batchSize);
            }

            //get 一条
            {
                // 根据 ID 查询
                MpTest byId = mpTestService.getById(1L);
//                // 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
//                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("age", 20);
//                queryWrapper.last("LIMIT 1");//防止多条报错
//                MpTest one1 = mpTestService.getOne(queryWrapper);
//                // 根据 Wrapper，查询一条记录
//                queryWrapper.last("");
//                MpTest one = mpTestService.getOne(queryWrapper, false);//此ex控制只是针对目标记录匹配到单条还是多条的控制输出（true就是默认只有一条，有多条就报错，false就是只取第一条【多条情况下会有一个警告log输出】），对其他的sql异常并没有捕捉控制仍然会报出异常
//                // 根据 Wrapper，查询一条记录
//                Map<String, Object> map = mpTestService.getMap(queryWrapper);
//                // 根据 Wrapper，查询一条记录(mapper 可配合 queryWrapper.select 用来返回指定的某列数据 减少通讯开支) 如果要查询多列可以用sql函数合并多列的值，后面在function函数中分割即可
//                queryWrapper.select("CONCAT(id,'||',name)");
//                queryWrapper.last("LIMIT 1");
//                Function<Object, TClass> mapper = name -> {
//                    TClass tClass = new TClass();
//                    tClass.setTName(name.toString());
//                    return tClass;
//                };
//
//                TClass obj = mpTestService.getObj(queryWrapper, mapper);
//<V > V getObj(Wrapper < T > queryWrapper, Function < ? super Object, V > mapper);

            }

            //list
            {
                // 查询所有
//                List<MpTest> list1 = mpTestService.list();
//                // 查询列表
//                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//                mpTestService.list(queryWrapper);
//                // 查询（根据ID 批量查询）
//                mpTestService.listByIds(Arrays.asList(3L));
//                // 查询（根据 columnMap 条件）
//                Map<String, Object> columnMap = new HashMap<>();
//                mpTestService.listByMap(columnMap);
//                // 查询所有列表
//                mpTestService.listMaps();
//                // 查询列表
//                mpTestService.listMaps(queryWrapper);
//                // 查询全部记录
//                mpTestService.listObjs();
//                // 查询全部记录
//                Function<Object, TClass> mapper = name -> {
//                    TClass tClass = new TClass();
//                    tClass.setId(Long.parseLong(name.toString()));
//                    return tClass;
//                };
//                mpTestService.listObjs(mapper);
//                // 根据 Wrapper 条件，查询全部记录
//                mpTestService.listObjs(queryWrapper);
//                // 根据 Wrapper 条件，查询全部记录
//                mpTestService.listObjs(queryWrapper, mapper);
            }

            //page
            {
                // 无条件分页查询
//                IPage<MpTest> page = new PageDTO<>();
//                page.setSize(5);
//                page.setCurrent(1);
//                mpTestService.page(page);
//
//                // 条件分页查询
//                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("id", 4);
//                mpTestService.page(page, queryWrapper);
//
//                // 无条件分页查询
//                IPage<Map<String, Object>> p = new Page<>();
//                mpTestService.pageMaps(p);
//                // 条件分页查询
//                mpTestService.pageMaps(p, queryWrapper);
            }

            //count
            {
//                mpTestService.count();
//
//                QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("id", 4);
//                mpTestService.count(queryWrapper);
            }

            //chain
            {
                //query
                {
                    // 链式查询 普通
//                    QueryChainWrapper<MpTest> query = mpTestService.query();
//                    // 链式查询 lambda 式。注意：不支持 Kotlin
//                    LambdaQueryChainWrapper<MpTest> mpTestLambdaQueryChainWrapper = mpTestService.lambdaQuery();
//
//                    // 示例：
//                    MpTest id = query.eq("id", 3L).one();
//                    List<MpTest> list = mpTestLambdaQueryChainWrapper.eq(MpTest::getId, 3L).list();
                }

                //update
                {
                    // 链式更改 普通
//                    UpdateChainWrapper<MpTest> update = mpTestService.update();
//                    // 链式更改 lambda 式。注意：不支持 Kotlin
//                    LambdaUpdateChainWrapper<MpTest> mpTestLambdaUpdateChainWrapper = mpTestService.lambdaUpdate();
//
//                    // 示例：
//                    update.eq("id", 3L).remove();
//                    mpTestLambdaUpdateChainWrapper.eq(MpTest::getId, 3L).update(entity);
                }
            }


        }

        //mapper crud

        //insert
        {
            // 插入一条记录
//            MpTest mpTest = new MpTest();
//            mpTest.setName("mapper insert");
//            mpTest.setScore(BigDecimal.ONE);
//            mpTest.setAge(100);
//            mpTestMapper.insert(mpTest);
        }

        //delete

        {
            // 根据 entity 条件，删除记录
//            QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
////            mpTestMapper.delete(queryWrapper);
//            // 删除（根据ID 批量删除）
//            Collection<Long> ids = new ArrayList<>();
//            ids.add(3L);
//            mpTestMapper.deleteBatchIds(ids);
//            // 根据 ID 删除
//            mpTestMapper.deleteById(3L);
//            // 根据 columnMap 条件，删除记录
//            Map<String, Object> columnMap = new HashMap<>();
//            columnMap.put("id", 3L);
//            mpTestMapper.deleteByMap(columnMap);
        }

        //update
        {
            // 根据 whereWrapper 条件，更新记录
//            QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//            MpTest mpTest = new MpTest();
//            mpTest.setName("mapper insert");
//            mpTest.setScore(BigDecimal.ONE);
//            mpTest.setAge(100);
//            mpTestMapper.update(mpTest, queryWrapper);
//            // 根据 ID 修改
//            mpTest.setId(3L);
//            mpTestMapper.updateById(mpTest);
        }

        //select
        {
            // 根据 ID 查询
//            mpTestMapper.selectById(3L);
//// 根据 entity 条件，查询一条记录
//            QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id", 3L);
//            mpTestMapper.selectOne(queryWrapper);
//
//// 查询（根据ID 批量查询）
//            Collection<Long> ids = new ArrayList<>();
//            ids.add(3L);
//            mpTestMapper.selectBatchIds(ids);
//// 根据 entity 条件，查询全部记录
//            mpTestMapper.selectList(queryWrapper);
//// 查询（根据 columnMap 条件）
//            Map<String, Object> columnMap = new HashMap<>();
//            columnMap.put("id", 3L);
//            mpTestMapper.selectByMap(columnMap);
//// 根据 Wrapper 条件，查询全部记录
//            mpTestMapper.selectMaps(queryWrapper);
//// 根据 Wrapper 条件，查询全部记录。注意： 只返回第一个字段的值
//            mpTestMapper.selectObjs(queryWrapper);
//
//// 根据 entity 条件，查询全部记录（并翻页）
//            IPage<MpTest> page = new PageDTO<>();
//            page.setSize(5);
//            page.setCurrent(1);
//            mpTestMapper.selectPage(page, queryWrapper);
//// 根据 Wrapper 条件，查询全部记录（并翻页）
//            IPage<Map<String, Object>> page1 = new Page<Map<String, Object>>();
//            mpTestMapper.selectMapsPage(page1, queryWrapper);
//// 根据 Wrapper 条件，查询总记录数
//            mpTestMapper.selectCount(queryWrapper);
        }

        //ActiveRecord(需要实体 extends Model<T>)
        {
//            MpTest mpTest = new MpTest();
//            mpTest.setName("ActiveRecord");
//            mpTest.setScore(BigDecimal.ONE);
//            mpTest.setAge(100);
//            mpTest.insert();
//            mpTest.selectAll();
//            mpTest.updateById();
//
//            mpTest.deleteById();
        }

        //simplequery
        {
            // 查询表内记录，封装返回为Map<属性,实体>
//            Map<Long, MpTest> longMpTestMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(), MpTest::getId, mpTest -> System.out.println(mpTest.getAge()));
//// 查询表内记录，封装返回为Map<属性,实体>，考虑了并行流的情况
//            Map<Long, MpTest> longMpTestMap1 = SimpleQuery.keyMap(Wrappers.lambdaQuery(), MpTest::getId, true, mpTest -> System.out.println(mpTest.getAge()));
//
//            // 查询表内记录，封装返回为Map<属性,属性>
//            Map<Long, String> map = SimpleQuery.map(Wrappers.lambdaQuery(), MpTest::getId, MpTest::getName, mpTest -> System.out.println(mpTest.getAge()));
//// 查询表内记录，封装返回为Map<属性,属性>，考虑了并行流的情况
//            Map<Long, String> map1 = SimpleQuery.map(Wrappers.lambdaQuery(), MpTest::getId, MpTest::getName, true, mpTest -> System.out.println(mpTest.getAge()));
//
//
//            // 查询表内记录，封装返回为Map<属性,List<实体>>
//            Map<String, List<MpTest>> group1 = SimpleQuery.group(Wrappers.lambdaQuery(), MpTest::getName, e -> System.out.println(e.getAge()));
//// 查询表内记录，封装返回为Map<属性,List<实体>>，考虑了并行流的情况
//            SimpleQuery.group(Wrappers.lambdaQuery(), MpTest::getName, true, e -> System.out.println(e.getAge()));
//// 查询表内记录，封装返回为Map<属性,分组后对集合进行的下游收集器>
//            SimpleQuery.group(Wrappers.lambdaQuery(), MpTest::getName, Collectors.mapping(MpTest::getName, Collectors.toList()), e -> System.out.println(e.getAge()));
//// 查询表内记录，封装返回为Map<属性,分组后对集合进行的下游收集器>，考虑了并行流的情况
////            M group(LambdaQueryWrapper<T> wrapper, SFunction<T, K> sFunction, Collector<? super T, A, D> downstream, boolean isParallel, Consumer<T>... peeks);
//
//
//            // 查询表内记录，封装返回为List<属性>
//            List<String> list1 = SimpleQuery.list(Wrappers.lambdaQuery(), MpTest::getName, e -> System.out.println(e.getAge()));
//// 查询表内记录，封装返回为List<属性>，考虑了并行流的情况
//            List<String> list2 = SimpleQuery.list(Wrappers.lambdaQuery(), MpTest::getName, true, e -> System.out.println(e.getAge()));

        }

        //Db(当前版本已经不存在)
        {
//            // 根据id查询
//            Db.listByIds(Arrays.asList(1L, 2L), Entity.class);
//// 根据条件构造器查询
//            Db.list(Wrappers.lambdaQuery(Entity.class));
//// 批量根据id更新
//            Db.updateBatchById(list);
        }
    }

    /**
     * 以下出现的第一个入参boolean condition表示该条件是否加入最后生成的sql中，例如：query.like(StringUtils.isNotBlank(name), Entity::getName, name) .eq(age!=null && age >= 0, Entity::getAge, age)
     * 以下代码块内的多个方法均为从上往下补全个别boolean类型的入参,默认为true
     * 以下出现的泛型Param均为Wrapper的子类实例(均具有AbstractWrapper的所有方法)
     * 以下方法在入参中出现的R为泛型,在普通wrapper中是String,在LambdaWrapper中是函数(例:Entity::getId,Entity为实体类,getId为字段id的getter Method)
     * 以下方法入参中的R column均表示数据库字段,当R具体类型为String时则为数据库字段名(字段名是数据库关键字的自己用转义符包裹!)而不是实体类数据字段名!!!
     * 以下举例均为使用普通wrapper,入参为Map和List的均以json形式表现!
     * 使用中如果入参的Map或者List为空,则不会加入最后生成的sql中!!!
     */
    private void 条件构造器() {

        QueryWrapper<MpTest> queryWrapper = new QueryWrapper<>();

        /**
         * allEq(Map<R, V> params)
         * allEq(Map<R, V> params, boolean null2IsNull)
         * allEq(boolean condition, Map<R, V> params, boolean null2IsNull)
         *
         * params : key为数据库字段名,value为字段值
         * null2IsNull : 为true则在map的value为null时调用 isNull 方法,为false时则忽略value为null的
         *
         * 例1: allEq({id:1,name:"老王",age:null})--->id = 1 and name = '老王' and age is null
         * 例2: allEq({id:1,name:"老王",age:null}, false)--->id = 1 and name = '老王'
         */

        /**
         * allEq(BiPredicate<R, V> filter, Map<R, V> params)
         * allEq(BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull)
         * allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull)
         *
         * filter : 过滤函数,是否允许字段传入比对条件中
         *
         * 例1: allEq((k,v) -> k.contains("a"), {id:1,name:"老王",age:null})--->name = '老王' and age is null
         * 例2: allEq((k,v) -> k.contains("a"), {id:1,name:"老王",age:null}, false)--->name = '老王'
         */

        /**
         * eq(R column, Object val)
         * eq(boolean condition, R column, Object val)
         *
         *  例: eq("name", "老王")--->name = '老王'
         */

        /**
         * ne(R column, Object val)
         * ne(boolean condition, R column, Object val)
         * 例: ne("name", "老王")--->name <> '老王'
         */

        /**
         * gt(R column, Object val) 、ge(R column, Object val)
         * gt(boolean condition, R column, Object val) 、ge(boolean condition, R column, Object val)
         * 例: gt("age", 18)--->age > 18 、ge("age", 18)--->age >= 18
         */

        /**
         * lt(R column, Object val) 、le(R column, Object val)
         * lt(boolean condition, R column, Object val) 、 le(boolean condition, R column, Object val)
         *  例: lt("age", 18)--->age < 18 、 le("age", 18)--->age <= 18
         */

        /**
         * between(R column, Object val1, Object val2) 、 notBetween(R column, Object val1, Object val2)
         * between(boolean condition, R column, Object val1, Object val2) 、 notBetween(boolean condition, R column, Object val1, Object val2)
         *
         * 例: between("age", 18, 30)--->age between 18 and 30 、 notBetween("age", 18, 30)--->age not between 18 and 30
         */

    }


    @Data
    static class TClass {
        private Long id;
        private String tName;
        private int tAge;
    }
}
