package skrifer.github.com.http;

import cn.hutool.core.date.DateUtil;
import skrifer.github.com.http.response.KEDAJDAPIBaseResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 域信息
 */
public class KEDAJDMCUAPIAreaService extends KEDAJDMCUAPIBaseService {

    public KEDAJDAPIBaseResponse getArea(String token, String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        return get(url, params);
    }

//    public static void main(String[] args) {
//        System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
//        //2016-04-20T15:00:00+08:00
//    }
}
