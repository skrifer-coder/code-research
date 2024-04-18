package skrifer.github.com.docker.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import skrifer.github.com.docker.ResponseMessage;
import skrifer.github.com.docker.dto.request.KedaRbacAdmin;
import skrifer.github.com.docker.dto.response.KedaProject;
import skrifer.github.com.docker.service.IKedaProjectService;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KedaProjectServiceImpl implements IKedaProjectService {

    private String rbacPreUrl = "https://dispatch.testdolphin.com/cloud-rbac";

    @Override
    public KedaProject fetchBySign(String projectSign, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.get(rbacPreUrl + "/client?f_eq_sign=" + projectSign).cookie(httpCookies).execute();
        String responseStr = httpResponse.body();
        ResponseMessage responseMessage = JSONUtil.toBean(responseStr, ResponseMessage.class);
        JSONArray responseMessageArray = (JSONArray) ((JSONObject) responseMessage.getResult()).get("data");
        if (responseMessageArray.isEmpty() == false) {
            return JSONUtil.toBean(responseMessageArray.get(0).toString(), KedaProject.class);
        }
        throw new RuntimeException("获取项目信息失败!");
    }

    @Override
    public List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.put(rbacPreUrl + "/charge_relation/batch").body(JSONUtil.toJsonStr(kedaRbacAdmin)).cookie(httpCookies).execute();
        ResponseMessage responseMessage = JSONUtil.toBean(httpResponse.body(), ResponseMessage.class);
        if (responseMessage.getStatus() == 200) {
            return ((JSONArray) responseMessage.getResult()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        throw new RuntimeException("更新项目管理员失败!");
    }

    @Override
    public List<String> fetchExistedProjectAdmins(String projectId, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.get(rbacPreUrl + "/charge_relation/v1/list?paging=false&f_eq_chargeType=1&f_eq_idTo=" + projectId).cookie(httpCookies).execute();
        String responseStr = httpResponse.body();
        ResponseMessage responseMessage = JSONUtil.toBean(responseStr, ResponseMessage.class);
        JSONArray responseMessageArray = (JSONArray) ((JSONObject) responseMessage.getResult()).get("data");
        if (responseMessageArray.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> idFromList = responseMessageArray.stream().map(o -> ((JSONObject) o).getStr("idFrom")).collect(Collectors.toList());
        return idFromList;
    }
}
