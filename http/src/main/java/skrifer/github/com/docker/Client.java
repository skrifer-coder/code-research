package skrifer.github.com.docker;

import skrifer.github.com.docker.service.IKedaRbacWebOperationService;
import skrifer.github.com.docker.service.impl.KedaRbacWebOperationServiceImpl;

public class Client {

    public static void main(String[] args) {

        IKedaRbacWebOperationService kedaRbacWebOperationService = new KedaRbacWebOperationServiceImpl();

        kedaRbacWebOperationService.removeProjectDeptAdmin("2c9435877bf6c65b017c775df4933207");

    }



}
