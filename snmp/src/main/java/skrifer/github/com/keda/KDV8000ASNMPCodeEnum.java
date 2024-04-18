package skrifer.github.com.keda;

public enum KDV8000ASNMPCodeEnum {
    设备告警("", "");


    KDV8000ASNMPCodeEnum(String code, String desc) {
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
