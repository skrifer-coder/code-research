package skrifer.github.com.docker.service;

import skrifer.github.com.docker.dto.request.KedaRbacUser;

import java.net.HttpCookie;
import java.util.List;

public interface IKedaLoginService {

    List<HttpCookie> loginAndFetchCookie(KedaRbacUser user);
}
