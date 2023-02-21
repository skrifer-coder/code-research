package skrifer.github.com.http.rbac.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import skrifer.github.com.http.rbac.ResponseMessage;
import skrifer.github.com.http.rbac.dto.request.KedaRbacAdmin;
import skrifer.github.com.http.rbac.dto.response.KedaDepart;
import skrifer.github.com.http.rbac.service.IKedaDeptService;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KedaDeptServiceImpl implements IKedaDeptService {

    private String tenantId = "RBAC";

    private String rbacPreUrl = "https://dispatch.testdolphin.com/cloud-rbac";

    @Override
    public KedaDepart fetchDeptCode(String deptCode, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.get(rbacPreUrl + "/department?f_eq_code=" + deptCode).cookie(httpCookies).execute();
        String responseStr = httpResponse.body();
        ResponseMessage responseMessage = JSONUtil.toBean(responseStr, ResponseMessage.class);
        JSONArray responseMessageArray = (JSONArray) ((JSONObject) responseMessage.getResult()).get("data");
        if (responseMessageArray.isEmpty() == false) {
            return JSONUtil.toBean(responseMessageArray.get(0).toString(), KedaDepart.class);
        }
        throw new RuntimeException("获取项目信息失败!");
    }

    @Override
    public List<String> fetchExistedDeptAdmins(String deptId, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.get(rbacPreUrl + "/charge_relation/v1/list?paging=false&f_eq_chargeType=3&f_eq_tenantId=" + tenantId + "&f_eq_idTo=" + deptId).cookie(httpCookies).execute();
        String responseStr = httpResponse.body();
        ResponseMessage responseMessage = JSONUtil.toBean(responseStr, ResponseMessage.class);
        JSONArray responseMessageArray = (JSONArray) ((JSONObject) responseMessage.getResult()).get("data");
        if (responseMessageArray.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> idFromList = responseMessageArray.stream().map(o -> ((JSONObject) o).getStr("idFrom")).collect(Collectors.toList());
        return idFromList;
    }

    @Override
    public List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies) {
        HttpResponse httpResponse = HttpRequest.put(rbacPreUrl + "/charge_relation/batch").body(JSONUtil.toJsonStr(kedaRbacAdmin)).cookie(httpCookies).execute();
        ResponseMessage responseMessage = JSONUtil.toBean(httpResponse.body(), ResponseMessage.class);
        if (responseMessage.getStatus() == 200) {
            return ((JSONArray) responseMessage.getResult()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        throw new RuntimeException("更新部门管理员失败!");
    }
}
