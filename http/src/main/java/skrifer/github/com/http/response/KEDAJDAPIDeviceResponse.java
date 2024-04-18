package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;

@Data
public class KEDAJDAPIDeviceResponse extends KEDAJDAPIBaseResponse {
    //----------------------------请求指定平台域下的物理服务器设备列表 start -----------------------------
    private List<Physical> physicals;

    @Data
    static class Physical {
        private String domainMoid;
        private String online;
        private String guid;
        private String machineRoomMoid;
        private String disk;
        private String moid;
        private String cardPos;
        private String ip;
        private String warningLevel;
        private String memory;
        private String portout;
        private String name;
        private String cpu;
        private String location;
        private String portin;
        private String type;
        private String temperature;
        private long isFrame;
        private String company;
        private List<Netcard> netcards;
        private List<LogicalPort> logicalPorts;
    }

    @Data
    static class Netcard {
        private String name;
        private String sendkbps;
        private String recvkbps;
        private long isInUse;
    }

    @Data
    static class LogicalPort {
        private String serverType;
        private String ports;
    }

    //----------------------------请求物理服务器上的逻辑服务器设备列表 && 请求指定平台域下的逻辑服务器设备列表 start -----------------------------

    private List<Logical> logicals;

    @Data
    static class Logical {
        private String moid;
        private String guid;
        private String p_server_moid;
        private String domain_moid;
        private String machine_room_moid;
        private String name;
        private String type;
        private String backup_state;
        private String ip;
        private String warning_level;
        private String online;
    }

    //----------------------------请求指定用户域的终端设备列表 start -----------------------------
    private List<Terminal> terminals;

    @Data
    static class Terminal {
        private String domain_moid;
        private String name;
        private String moid;
        private String e164;
        private String ip;
        private String online;
        private List<Detail> detail;
    }

    @Data
    static class Detail {
        private String type;
        private String ip;
        private String cpu_userate;
        private String mem_userate;
        private String sendkbps;
        private String recvkbps;
        private String company;
        private List<Netcard> netcards;

        private List<Sign> audio_input_sign;
        private List<Sign> audio_output_sign;
        private List<Sign> video_input_sign;
        private List<Sign> video_output_sign;
    }

    @Data
    static class Sign {
        private int status;
        private String type;
    }

    //----------------------------请求指定物理服务器的详细信息 start-----------------------------
    private String domain_moid;
    private String online;
    private String guid;
    private String disk;
    private String moid;
    private String domain_name;
    private String machine_room_moid;
    private String machine_room_name;
    private String card_pos;
    private String ip;
    private String memory;
    private String portout;
    private String name;
    private String warning_level;
    private String location;
    private String cpu;
    private String portin;
    private String type;
    private String temperature;
    private int is_frame;
    private List<Netcard> netcards;
    private List<LogicalPort> logicalPorts;

    //----------------------------请求非受管终端设备列表 start-----------------------------

    private List<OldTerminal> old_terminals;
    @Data
    static class OldTerminal {
        private String e164;
        private String type;
        private String version;
        private String ip;
        private String online;
    }

}
