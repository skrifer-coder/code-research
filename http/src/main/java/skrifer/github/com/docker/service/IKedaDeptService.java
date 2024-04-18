package skrifer.github.com.docker.service;

import skrifer.github.com.docker.dto.request.KedaRbacAdmin;
import skrifer.github.com.docker.dto.response.KedaDepart;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaDeptService {
    KedaDepart fetchDeptCode(String deptCode, List<HttpCookie> httpCookies);

    List<String> fetchExistedDeptAdmins(String deptId, List<HttpCookie> httpCookies);

    List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies);
}
