package skrifer.github.com.http.rbac.service;

import skrifer.github.com.http.rbac.dto.request.KedaRbacAdmin;
import skrifer.github.com.http.rbac.dto.response.KedaDepart;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaDeptService {
    KedaDepart fetchDeptCode(String deptCode, List<HttpCookie> httpCookies);

    List<String> fetchExistedDeptAdmins(String deptId, List<HttpCookie> httpCookies);

    List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies);
}
