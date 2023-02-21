package skrifer.github.com.http.rbac.service;

import skrifer.github.com.http.rbac.dto.request.KedaRbacUser;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaLoginService {

    List<HttpCookie> loginAndFetchCookie(KedaRbacUser user);
}
