package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;

@Data
public class KEDAJDAPICountResponse extends KEDAJDAPIBaseResponse{




    //----------------------------请求终端在线统计数据 start-----------------------------
    private List<Statistic> statistic;

    @Data
    static class Statistic{
        private String domain_moid;
        private int xmpp_online;
        private int sip_online;
        private int monitor_online;
        private int h323_online;
        private String statistic_time;
    }

    //----------------------------请求平台域端口资源统计信息-----------------------------

    private int used_count;
}
