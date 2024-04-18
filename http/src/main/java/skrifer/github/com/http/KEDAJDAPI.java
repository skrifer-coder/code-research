package skrifer.github.com.http;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import skrifer.github.com.http.response.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KEDAJDAPI {

    static String user = "sysmgr";
    static String password = "Keda_8888";
    static String key = "iE9at2iD4bK4";
    static String secret = "arjcA3bB5yGp";
    static String ip = "172.16.237.9";

    static String format_url = "http://{domain|ip}/api/{version}/{app}/{resources}";//{rc_id}";
    static String version = "v1";
    static String token_resource = "token";
    static String login_resource = "login";
    static String heartbeat_resource = "heartbeat";
    static String domains_resource = "domains";
    static String platform_domains_resource = "platform_domains";
    static String user_domains_resource = "user_domains";
    static String physicals_resource = "physicals";
    static String old_terminals_resource = "old_terminals";
    static String service_domains_resource = "service_domains";
    static String servers_resource = "servers";
    static String terminals_resource = "terminals";
    static String meetings_resource = "meetings";

    static enum APP {
        VC("vc", "会议业务"),
        MC("mc", "会管业务"),
        AMC("amc", "帐号系统"),
        NMS("nms", "网管"),
        VRS("vrs", "录播"),
        SYSTEM("system", "系统(登录用)");

        APP(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private String code;
        private String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    static enum AreaType {
        KERNEL("kernel", "顶级"),
        MACHINE_ROOM("machine_room", "默认机房"),
        PLATFORM("platform", "默认平台域"),
        USER("user", "默认用户域"),
        SERVICE("service", "默认服务域");

        AreaType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private String code;
        private String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static void main(String[] args) {
        String url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.SYSTEM.getCode())
                .replace("{resources}", token_resource);

        System.out.println(url);

        KEDAJDMCUAPILoginService loginService = new KEDAJDMCUAPILoginService();
        String token = loginService.getToken(key, secret, url);
        System.out.println("gettoken:" + token);
//
//
        url = url.replace(token_resource, login_resource);
        System.out.println(url);
        boolean login = loginService.login(token, user, password, url, "");
        System.out.println("login result:" + login);
//
//
        url = url.replace(login_resource, heartbeat_resource);
        System.out.println(url);
//
        boolean b = loginService.keepHeartbeat(token, url);
        System.out.println("keep result:" + b);

//        String token = "1ea511a8b9c0958076889310dc8d6643";

        //获取所有域信息
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", domains_resource);
        System.out.println(url);
        KEDAJDMCUAPIAreaService areaService = new KEDAJDMCUAPIAreaService();
        KEDAJDAPIAreaResponse area = (KEDAJDAPIAreaResponse) areaService.getArea(token, url);
        System.out.println(JSONUtil.toJsonStr(area));

        //设备信息
        KEDAJDMCUAPIDeviceService deviceService = new KEDAJDMCUAPIDeviceService();
        //1.请求指定平台域下的物理服务器设备列表
        KEDAJDAPIAreaResponse.Domain targetDomain = area.getDomains().stream().filter(domain -> AreaType.PLATFORM.getCode().equals(domain.getType())).findAny().orElse(null);

        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", platform_domains_resource)
                + "/" +targetDomain.getMoid() + "/physicals";
        KEDAJDAPIDeviceResponse kedajdapiDeviceResponse = deviceService.getDomainPhysicalDeviceList(url, token);
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));

        //2.请求物理服务器上的逻辑服务器设备列表
        targetDomain = area.getDomains().stream().filter(domain -> AreaType.SERVICE.getCode().equals(domain.getType())).findAny().orElse(null);
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", physicals_resource)
                + "/" +targetDomain.getMoid() + "/logicals";
        kedajdapiDeviceResponse = deviceService.getPhysicalServerLogicalDeviceList(url, token);
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));


        //3.请求指定平台域下的逻辑服务器设备列表
        targetDomain = area.getDomains().stream().filter(domain -> AreaType.PLATFORM.getCode().equals(domain.getType())).findAny().orElse(null);
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", platform_domains_resource)
                + "/" +targetDomain.getMoid() + "/logicals";

        kedajdapiDeviceResponse = deviceService.getDomainLogicalDeviceList(url, token);
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));

        //4.请求指定用户域的终端设备列表
        targetDomain = area.getDomains().stream().filter(domain -> AreaType.USER.getCode().equals(domain.getType())).findAny().orElse(null);
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", user_domains_resource)
                + "/" +targetDomain.getMoid() + "/terminals";

        Map<String, Integer> params = new HashMap<>();
        params.put("start", 0);
        params.put("count", 3);
        kedajdapiDeviceResponse = deviceService.getUserDomainDeviceList(url, token, JSONUtil.toJsonStr(params));
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));

        //5.请求指定物理服务器的详细信息
        targetDomain = area.getDomains().stream().filter(domain -> AreaType.SERVICE.getCode().equals(domain.getType())).findAny().orElse(null);
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", physicals_resource)
                + "/" +targetDomain.getMoid() + "/detail";

        kedajdapiDeviceResponse = deviceService.getSinglePhysicalDeviceDetail(url, token);
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));

        //6.请求非受管终端设备列表
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", old_terminals_resource);

        kedajdapiDeviceResponse = deviceService.getUnControlDeviceList(url, token, 0, 10);
        System.out.println(JSONUtil.toJsonStr(kedajdapiDeviceResponse));



        //统计信息
        KEDAJDMCUAPICountService countService = new KEDAJDMCUAPICountService();
        //1.请求终端在线统计数据
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("period", "lastweek");//统计时间区域，枚举类型：lastweek：最近一周,lastmonth：最近一月，lastthreemonth：最近三个月，lasthalfyear：最近半年，lastyear：最近一年
//        queryParams.put("start_time", "2024-04-01T11:16:21.00+08:00");
//        queryParams.put("start_time", DateUtil.format(new Date(1711941381000L), "yyyy-MM-dd'T'HH:mm:ssXXX"));
//        queryParams.put("end_time", "2024-04-01T11:16:21.00+08:00");
//        queryParams.put("end_time", DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
        queryParams.put("start", 1);
        queryParams.put("count", 3);

        targetDomain = area.getDomains().stream().filter(domain -> AreaType.SERVICE.getCode().equals(domain.getType())).findAny().orElse(null);
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", service_domains_resource)
                + "/" +targetDomain.getMoid() + "/terminal_online_statistic";
        KEDAJDAPICountResponse countResponse = countService.getOnlineTerminalCountInfo(url, token, JSONUtil.toJsonStr(queryParams));
        System.out.println(JSONUtil.toJsonStr(countResponse));


        //2.请求平台域端口资源统计信息
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", platform_domains_resource)
                + "/" +targetDomain.getMoid() + "/mediaresources";

        countResponse = countService.getOnlineTerminalCountInfo(url, token, null);
        System.out.println(JSONUtil.toJsonStr(countResponse));


        //告警信息
        //1.请求服务器未修复告警信息
        KEDAJDMCUAPIWarningService warningService = new KEDAJDMCUAPIWarningService();
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", servers_resource)
                + "/" +targetDomain.getMoid() + "/unrepaired_warnings";
        KEDAJDAPIWarningResponse warningResponse = warningService.getServerUnrepairedWarnings(url, token);
        System.out.println(JSONUtil.toJsonStr(warningResponse));


        //2.请求终端未修复告警信息
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", terminals_resource)
                + "/" +targetDomain.getMoid() + "/unrepaired_warnings";
        warningResponse = warningService.getServerUnrepairedWarnings(url, token);
        System.out.println(JSONUtil.toJsonStr(warningResponse));


        //会议信息
        KEDAJDMCUAPIMeetingService meetingService = new KEDAJDMCUAPIMeetingService();
        //1.请求服务域正在召开的会议信息
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", service_domains_resource)
                + "/" +targetDomain.getMoid() + "/meetings";
        KEDAJDAPIMeetingResponse meetingResponse = meetingService.getServerUnrepairedWarnings(url, token);
        System.out.println(JSONUtil.toJsonStr(meetingResponse));

        //2.请求会议终端信息详情
        url = format_url.replace("{domain|ip}", ip)
                .replace("{version}", version)
                .replace("{app}", APP.NMS.getCode())
                .replace("{resources}", meetings_resource)
                + "/" +targetDomain.getMoid() + "/terminal_detail";
        meetingResponse = meetingService.getTerminalUnrepairedWarnings(url, token);
        System.out.println(JSONUtil.toJsonStr(meetingResponse));
    }
}
