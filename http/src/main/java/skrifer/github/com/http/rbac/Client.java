package skrifer.github.com.http.rbac;

import cn.hutool.core.util.ReUtil;
import skrifer.github.com.http.rbac.service.IKedaRbacWebOperationService;
import skrifer.github.com.http.rbac.service.impl.KedaRbacWebOperationServiceImpl;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client {

    public static void main(String[] args) {

        IKedaRbacWebOperationService kedaRbacWebOperationService = new KedaRbacWebOperationServiceImpl();

        kedaRbacWebOperationService.removeProjectDeptAdmin("2c9435877bf6c65b017c775df4933207");

    }



}
