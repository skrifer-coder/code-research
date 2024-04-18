package skrifer.github.com.docker.service.impl;

import skrifer.github.com.docker.dto.request.KedaRbacAdmin;
import skrifer.github.com.docker.dto.request.KedaRbacUser;
import skrifer.github.com.docker.dto.response.KedaProject;
import skrifer.github.com.docker.service.IKedaDeptService;
import skrifer.github.com.docker.service.IKedaLoginService;
import skrifer.github.com.docker.service.IKedaRbacWebOperationService;
import skrifer.github.com.docker.dto.response.KedaDepart;
import skrifer.github.com.docker.service.IKedaProjectService;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KedaRbacWebOperationServiceImpl implements IKedaRbacWebOperationService {

    private String projectSign = "jcy-kras-back";

    private String superUserName = "rbac";

    private String superUserPassword = "123456";

    private String tenantId = "RBAC";

    private IKedaLoginService kedaLoginService = new KedaLoginServiceImpl();

    private IKedaProjectService kedaProjectService = new KedaProjectServiceImpl();

    private IKedaDeptService kedaDeptService = new KedaDeptServiceImpl();

    private final static Map<String, String> projectSignIdMap = new HashMap<>();

    private final static Map<String, String> deptIdMap = new HashMap<>();

    @Override
    public boolean addProjectAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        //获得项目Id
        String projectId = projectSignIdMap.getOrDefault(projectSign, null);
        if (projectId == null) {
            KedaProject kedaProject = kedaProjectService.fetchBySign(projectSign, logedCookies);
            projectId = kedaProject.getId();
            projectSignIdMap.put(projectSign, projectId);
        }

        //获取项目当前的管理员列表
        List<String> existedUserIds = kedaProjectService.fetchExistedProjectAdmins(projectId, logedCookies);
        existedUserIds.add(userId);

        //封装请求数据
        KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
        for (String existedUserId : existedUserIds) {
            kedaRbacAdmin.addchargeRelation(existedUserId, projectId);
        }

        //提交设置项目管理员
        return kedaProjectService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
    }

    @Override
    public boolean removeProjectAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        //获得项目Id
        String projectId = projectSignIdMap.getOrDefault(projectSign, null);
        if (projectId == null) {
            KedaProject kedaProject = kedaProjectService.fetchBySign(projectSign, logedCookies);
            projectId = kedaProject.getId();
            projectSignIdMap.put(projectSign, projectId);
        }

        //获取项目当前的管理员列表
        List<String> existedUserIds = kedaProjectService.fetchExistedProjectAdmins(projectId, logedCookies);
        existedUserIds.remove(userId);

        //封装请求数据
        KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
        for (String existedUserId : existedUserIds) {
            kedaRbacAdmin.addchargeRelation(existedUserId, projectId);
        }

        //提交设置项目管理员
        return kedaProjectService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
    }

    @Override
    public boolean addDeptAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        //获得部门Id
        String deptId = deptIdMap.getOrDefault(projectSign, null);
        if (deptId == null) {
            KedaDepart kedaDepart = kedaDeptService.fetchDeptCode(projectSign, logedCookies);
            deptId = kedaDepart.getId();
            deptIdMap.put(projectSign, deptId);
        }

        //获取部门当前的管理员列表
        List<String> existedUserIds = kedaDeptService.fetchExistedDeptAdmins(deptId, logedCookies);
        existedUserIds.add(userId);

        //封装请求数据
        KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
        kedaRbacAdmin.setChargeTypeEnum("DEPART_ADMIN");
        for (String existedUserId : existedUserIds) {
            kedaRbacAdmin.addchargeRelation(existedUserId, deptId, "DEPARTMENT");
        }

        //提交设置部门管理员
        return kedaDeptService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
    }

    @Override
    public boolean removeDeptAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        //获得部门Id
        String deptId = deptIdMap.getOrDefault(projectSign, null);
        if (deptId == null) {
            KedaDepart kedaDepart = kedaDeptService.fetchDeptCode(projectSign, logedCookies);
            deptId = kedaDepart.getId();
            deptIdMap.put(projectSign, deptId);
        }

        //获取部门当前的管理员列表
        List<String> existedUserIds = kedaDeptService.fetchExistedDeptAdmins(deptId, logedCookies);
        existedUserIds.remove(userId);

        //封装请求数据
        KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
        kedaRbacAdmin.setChargeTypeEnum("DEPART_ADMIN");
        for (String existedUserId : existedUserIds) {
            kedaRbacAdmin.addchargeRelation(existedUserId, deptId, "DEPARTMENT");
        }

        //提交设置部门管理员
        return kedaDeptService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
    }

    @Override
    public boolean addProjectDeptAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        boolean projectFlag;
        {
            //获得项目Id
            String projectId = projectSignIdMap.getOrDefault(projectSign, null);
            if (projectId == null) {
                KedaProject kedaProject = kedaProjectService.fetchBySign(projectSign, logedCookies);
                projectId = kedaProject.getId();
                projectSignIdMap.put(projectSign, projectId);
            }

            //获取项目当前的管理员列表
            List<String> existedUserIds = kedaProjectService.fetchExistedProjectAdmins(projectId, logedCookies);
            existedUserIds.add(userId);

            //封装请求数据
            KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
            for (String existedUserId : existedUserIds) {
                kedaRbacAdmin.addchargeRelation(existedUserId, projectId);
            }

            //提交设置项目管理员
            projectFlag = kedaProjectService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
        }

        boolean deptFlag;
        {
            //获得部门Id
            String deptId = deptIdMap.getOrDefault(projectSign, null);
            if (deptId == null) {
                KedaDepart kedaDepart = kedaDeptService.fetchDeptCode(projectSign, logedCookies);
                deptId = kedaDepart.getId();
                deptIdMap.put(projectSign, deptId);
            }

            //获取部门当前的管理员列表
            List<String> existedUserIds = kedaDeptService.fetchExistedDeptAdmins(deptId, logedCookies);
            existedUserIds.add(userId);

            //封装请求数据
            KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
            kedaRbacAdmin.setChargeTypeEnum("DEPART_ADMIN");
            for (String existedUserId : existedUserIds) {
                kedaRbacAdmin.addchargeRelation(existedUserId, deptId, "DEPARTMENT");
            }

            //提交设置部门管理员
            deptFlag = kedaDeptService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
        }

        return projectFlag && deptFlag;
    }

    @Override
    public boolean removeProjectDeptAdmin(String userId) {
        //登录获取cookie
        KedaRbacUser superUser = new KedaRbacUser(superUserPassword, tenantId, superUserName, projectSign);
        List<HttpCookie> logedCookies = kedaLoginService.loginAndFetchCookie(superUser);

        boolean projectFlag;
        {
            //获得项目Id
            String projectId = projectSignIdMap.getOrDefault(projectSign, null);
            if (projectId == null) {
                KedaProject kedaProject = kedaProjectService.fetchBySign(projectSign, logedCookies);
                projectId = kedaProject.getId();
                projectSignIdMap.put(projectSign, projectId);
            }

            //获取项目当前的管理员列表
            List<String> existedUserIds = kedaProjectService.fetchExistedProjectAdmins(projectId, logedCookies);
            existedUserIds.remove(userId);

            //封装请求数据
            KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
            for (String existedUserId : existedUserIds) {
                kedaRbacAdmin.addchargeRelation(existedUserId, projectId);
            }

            //提交设置项目管理员
            projectFlag = kedaProjectService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
        }

        boolean deptFlag;
        {
            //获得部门Id
            String deptId = deptIdMap.getOrDefault(projectSign, null);
            if (deptId == null) {
                KedaDepart kedaDepart = kedaDeptService.fetchDeptCode(projectSign, logedCookies);
                deptId = kedaDepart.getId();
                deptIdMap.put(projectSign, deptId);
            }

            //获取部门当前的管理员列表
            List<String> existedUserIds = kedaDeptService.fetchExistedDeptAdmins(deptId, logedCookies);
            existedUserIds.remove(userId);

            //封装请求数据
            KedaRbacAdmin kedaRbacAdmin = new KedaRbacAdmin();
            kedaRbacAdmin.setChargeTypeEnum("DEPART_ADMIN");
            for (String existedUserId : existedUserIds) {
                kedaRbacAdmin.addchargeRelation(existedUserId, deptId, "DEPARTMENT");
            }

            //提交设置部门管理员
            deptFlag = kedaDeptService.batch(kedaRbacAdmin, logedCookies).size() == existedUserIds.size();
        }

        return projectFlag && deptFlag;

    }

}
