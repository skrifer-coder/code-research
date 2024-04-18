package skrifer.github.com.docker.service;

public interface IKedaRbacWebOperationService {
    boolean addProjectAdmin(String userId);

    boolean removeProjectAdmin(String userId);

    boolean addDeptAdmin(String userId);

    boolean removeDeptAdmin(String userId);

    boolean addProjectDeptAdmin(String userId);

    boolean removeProjectDeptAdmin(String userId);

}
