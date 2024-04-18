package skrifer.github.com.docker.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import skrifer.github.com.docker.dto.request.KedaRbacUser;
import skrifer.github.com.docker.service.IKedaLoginService;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 科达统一权限登录
 */
public class KedaLoginServiceImpl implements IKedaLoginService {

    private String rbacPreUrl = "https://dispatch.testdolphin.com/cloud-rbac";

    @Override
    public List<HttpCookie> loginAndFetchCookie(KedaRbacUser user) {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>() {{
            add(new HttpCookie("route", (System.currentTimeMillis() / 1000) + ".63.301392.916796|" + UUID.randomUUID().toString().replaceAll("-", "")));
            add(new HttpCookie("_bl_usercode", "rbac"));
        }};
        String eagleId = fetchEagledId(cookies);

        submitUserInfo(user, eagleId, cookies);

        cookies.add(fetchSession(eagleId, cookies));

        return cookies;

    }

    //mfa接口获取eagleid
    private String fetchEagledId(List<HttpCookie> cookies) {
        HttpResponse httpResponse = HttpRequest.get(rbacPreUrl + "/authentication/mfa?loginPolicy=weak")
                .cookie(cookies)
                .execute();
        String responseBodyStr = httpResponse.body();
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        JSONObject resultJson = responseJson.getJSONObject("result");
        if (resultJson == null || resultJson.isEmpty()) {
            throw new RuntimeException("/authentication/mfa接口请求失败");
        }

        String eagleId = resultJson.getStr("eagleid");
        if (eagleId == null || "".equals(eagleId.trim())) {
            throw new RuntimeException("eagleId获取失败");
        }

        return eagleId;
    }

    //login接口提交用户信息
    private void submitUserInfo(KedaRbacUser user, String eagleId, List<HttpCookie> cookies) {
        user.setPassword(SecureUtil.sha256(user.getPassword()));
        HttpResponse httpResponse = HttpRequest.post(rbacPreUrl + "/up/login")
                .cookie(cookies)
                .header("accept", "application/json, text/plain, */*")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("content-sha-256", "true")
                .header("content-type", "application/json;charset=UTF-8")
                .header("eagleid", eagleId)
                .header("origin", "https://dispatch.testdolphin.com")
                .header("referer", "https://dispatch.testdolphin.com/cloud-rbac/")
                .header("tenantid", "RBAC")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                .body(JSONUtil.toJsonStr(user)).execute();

        String responseBodyStr = httpResponse.body();
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        JSONObject resultJson = responseJson.getJSONObject("result");
        if (resultJson == null || resultJson.isEmpty()) {
            throw new RuntimeException("/up/login接口请求失败");
        }

    }


    //无参访问login获取session
    private HttpCookie fetchSession(String eagleId, List<HttpCookie> cookies) {
        HttpResponse httpResponse = HttpRequest.post(rbacPreUrl + "/authentication/login")
                .cookie(cookies)
                .header("accept", "application/json, text/plain, */*")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("content-sha-256", "true")
                .header("content-type", "application/json;charset=UTF-8")
                .header("eagleid", eagleId)
                .header("origin", "https://dispatch.testdolphin.com")
                .header("referer", "https://dispatch.testdolphin.com/cloud-rbac/")
                .header("tenantid", "RBAC")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                .execute();
        return httpResponse.getCookie("SESSION");
    }

}
