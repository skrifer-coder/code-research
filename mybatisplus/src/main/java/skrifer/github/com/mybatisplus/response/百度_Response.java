package skrifer.github.com.mybatisplus.response;

import lombok.Data;

@Data
public class 百度_Response {
    private int status;
    private String message;
    private Result result;


    @Data
    public static class Result {
        private String title;
        private Location location;
        private Ad_info ad_info;
        private Address_components address_components;
        private double similarity;
        private int deviation;
        private int reliability;
        private int level;
    }

    @Data
    public static class Ad_info {
        private String adcode;
    }

    @Data
    public static class Address_components {
        private String province;
        private String city;
        private String district;
        private String street;
        private String street_number;
    }


}
