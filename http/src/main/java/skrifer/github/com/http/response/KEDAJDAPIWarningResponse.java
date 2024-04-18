package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;

@Data
public class KEDAJDAPIWarningResponse extends KEDAJDAPIBaseResponse{

    //----------------------------请求服务器未修复告警信息 && 请求终端未修复告警信息-----------------------------
    private List<UnrepairedWarning> unrepaired_warnings;

    @Data
    static class UnrepairedWarning{
        private String device_moid;
        private String device_name;
        private String device_type;
        private String device_ip;
        private String domain_moid;
        private String domain_name;
        private String machine_room_moid;
        private String machine_room_name;
        private String warning_level;
        private String description;
        private String start_time;
        private String device_e164;
    }

}
