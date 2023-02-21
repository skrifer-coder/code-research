package skrifer.github.com.http.rbac.service;

import skrifer.github.com.http.rbac.dto.response.KedaProject;
import skrifer.github.com.http.rbac.dto.request.KedaRbacAdmin;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaProjectService {
    KedaProject fetchBySign(String projectSign, List<HttpCookie> httpCookies);

    List<String> fetchExistedProjectAdmins(String projectId, List<HttpCookie> httpCookies);

    List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies);
}
