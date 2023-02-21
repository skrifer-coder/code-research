package skrifer.github.com.designpattern.resource;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Resource implements Serializable {

    //----------------------------资源构造-----------------------------

    public Resource(String name) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Resource name is not allowed blank!");
        }
        this.name = name;
        this.resourceType = SimpleResourcePool.DEFAULT_RESOURCE_TYPE;
        this.historyCapacity = 5;
    }

    public Resource(String name, int historyCapacity) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Resource name is not allowed blank!");
        }
        this.name = name;
        this.resourceType = SimpleResourcePool.DEFAULT_RESOURCE_TYPE;
        this.historyCapacity = historyCapacity <= 0 ? 1 : historyCapacity;
    }

    public Resource(String name, String resourceType) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Resource name is not allowed blank!");
        }
        this.name = name;
        if (resourceType != null && resourceType.trim().equals("") == false) {
            this.resourceType = resourceType;
        } else {
            this.resourceType = SimpleResourcePool.DEFAULT_RESOURCE_TYPE;
        }
        this.historyCapacity = 5;
    }

    public Resource(String name, String resourceType, int historyCapacity) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Resource name is not allowed blank!");
        }
        this.name = name;
        if (resourceType != null && resourceType.trim().equals("") == false) {
            this.resourceType = resourceType;
        } else {
            this.resourceType = SimpleResourcePool.DEFAULT_RESOURCE_TYPE;
        }
        this.historyCapacity = historyCapacity <= 0 ? 1 : historyCapacity;
    }

    //----------------------------资源系统属性字段-----------------------------

    @Getter
    //资源名称(important!)
    private final String name;

    //资源类型(资源类型隔离)
    @Getter
    private final String resourceType;

    //资源历史容量
    private final int historyCapacity;

    //使用历史时间
    @Getter
    private final List<LocalDateTime> usedHistoryTimes = new ArrayList<>();

    //时间
    @Getter
    private LocalDateTime createdDateTime = LocalDateTime.now();

    //最后更新时间
    @Getter
    @Setter
    private LocalDateTime updatedDateTime = LocalDateTime.now();

    //----------------------------自定义业务扩展字段-----------------------------
    @Getter
    @Setter
    //资源业务标识,请求资源或释资源放时尽量使用来标识业务间的隔离,防止不同的业务使用统一资源时出现相互干扰:释放了对方业务内的资源
    private String bizField;
    @Getter
    @Setter
    private String account;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String ip;
    @Getter
    @Setter
    private Integer port;


    //----------------------------新增历史-----------------------------

    public void addHistoryTime(LocalDateTime dateTime) {
        synchronized (this) {
            if (usedHistoryTimes.size() == historyCapacity) {
                usedHistoryTimes.remove(0);
            }
            usedHistoryTimes.add(dateTime);
        }
    }

    public void addHistoryTime() {
        synchronized (this) {
            if (usedHistoryTimes.size() == historyCapacity) {
                usedHistoryTimes.remove(0);
            }
            usedHistoryTimes.add(LocalDateTime.now());
        }
    }

    //----------------------------刷新更新时间------------------------------
    public void refreshUpateTime(LocalDateTime dateTime) {
        this.updatedDateTime = dateTime;
    }

    public void refreshUpateTime() {
        refreshUpateTime(LocalDateTime.now());
    }

    //----------------------------重写比较方法-----------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(name, resource.name) && Objects.equals(bizField, resource.bizField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bizField);
    }

    //----------------------------序列化-----------------------------


    @Override
    public String toString() {
        return "资源类型:" + this.resourceType + " | 业务字段:" + this.bizField + " | 资源名称:" + this.name + " | 创建时间:" + this.createdDateTime.toString() + " | 最后更新时间:" + this.updatedDateTime.toString() +
                " | 历史执行时间:" + this.usedHistoryTimes.stream().map(LocalDateTime::toString).collect(Collectors.joining(" | "));
    }
}
