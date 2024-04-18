package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;

@Data
public class KEDAJDAPIMeetingResponse extends KEDAJDAPIBaseResponse {
    //----------------------------请求服务域正在召开的会议信息-----------------------------

    private List<Meeting> meeting;

    @Data
    static class Meeting {
        private int conf_type;
        private String name;
        private String e164;
        private int bitrate;
        private String domain_moid;
        private String start_time;
        private String end_time;
        private List<MeetingTerminal> meeting_terminals;
        private List<String> telephone_terminals;
        private List<Cascade> cascades;
        private List<String> ip_e164;
    }

    @Data
    static class MeetingTerminal {
        private String domain_moid;
        private String name;
        private String version;
        private String type;
        private String moid;
        private String e164;
        private String ip;
    }

    @Data
    static class Cascade {
        private int type;
        private String e164;
        private String name;
    }

    //----------------------------请求会议终端信息详情-----------------------------

    List<Terminal> terminals;

    @Data
    static class Terminal {
        private int isp;
        private String nat_ip;
        private String ip;
        private String version;
        private String type;
        private String name;
        private String e164;
        private int bitrate;
        private List<Audio> primary_video;
        private List<Video> dual_video;
    }

    @Data
    static class Video {
        private int chan_id;
        private int up_or_down;
        private String video_format;
        private int video_framerate;
        private int video_up_bitrate;
        private int video_down_bitrate;
        private int video_packets_lose;
    }

    @Data
    static class Audio extends Video {
        private String audio_format;
        private int audio_up_bitrate;
        private int audio_packets_lose;
        private int audio_packets_loserate;
    }
}
