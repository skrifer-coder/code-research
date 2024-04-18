package skrifer.github.com.docker.service;

import skrifer.github.com.docker.dto.request.KedaRbacAdmin;
import skrifer.github.com.docker.dto.response.KedaProject;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaProjectService {
    KedaProject fetchBySign(String projectSign, List<HttpCookie> httpCookies);

    List<String> fetchExistedProjectAdmins(String projectId, List<HttpCookie> httpCookies);

    List<String> batch(KedaRbacAdmin kedaRbacAdmin, List<HttpCookie> httpCookies);
}
