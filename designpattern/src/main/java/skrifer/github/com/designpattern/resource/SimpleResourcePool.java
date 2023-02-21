package skrifer.github.com.designpattern.resource;

import skrifer.github.com.base.utils.CloneUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 资源池模式
 * Object Pool Design Pattern
 * <p>
 * Intent
 * <p>
 * Object pooling can offer a significant performance boost; it is most effective in situations where the cost of initializing a class instance is high, the rate of instantiation of a class is high, and the number of instantiations in use at any one time is low.
 * <p>
 * Problem
 * <p>
 * Object pools (otherwise known as resource pools) are used to manage the object caching. A client with access to a Object pool can avoid creating a new Objects by simply asking the pool for one that has already been instantiated instead. Generally the pool will be a growing pool, i.e. the pool itself will create new objects if the pool is empty, or we can have a pool, which restricts the number of objects created.
 * <p>
 * It is desirable to keep all Reusable objects that are not currently in use in the same object pool so that they can be managed by one coherent policy. To achieve this, the Reusable Pool class is designed to be a singleton class.
 * <p>
 * Discussion
 * <p>
 * The Object Pool lets others "check out" objects from its pool, when those objects are no longer needed by their processes, they are returned to the pool in order to be reused.
 * <p>
 * However, we don't want a process to have to wait for a particular object to be released, so the Object Pool also instantiates new objects as they are required, but must also implement a facility to clean up unused objects periodically.
 * <p>
 * Structure
 * <p>
 * The general idea for the Connection Pool pattern is that if instances of a class can be reused, you avoid creating instances of the class by reusing them.
 * <p>
 * Reusable - Instances of classes in this role collaborate with other objects for a limited amount of time, then they are no longer needed for that collaboration.
 * <p>
 * Client - Instances of classes in this role use Reusable objects.
 * <p>
 * ReusablePool - Instances of classes in this role manage Reusable objects for use by Client objects.
 * <p>
 * Usually, it is desirable to keep all Reusable objects that are not currently in use in the same object pool so that they can be managed by one coherent policy. To achieve this, theReusablePool class is designed to be a singleton class. Its constructor(s) are private, which forces other classes to call its getInstance method to get the one instance of the ReusablePoolclass.
 * <p>
 * A Client object calls a ReusablePool object's acquireReusable method when it needs aReusable object. A ReusablePool object maintains a collection of Reusable objects. It uses the collection of Reusable objects to contain a pool of Reusable objects that are not currently in use.
 * <p>
 * If there are any Reusable objects in the pool when the acquireReusable method is called, it removes a Reusable object from the pool and returns it. If the pool is empty, then theacquireReusable method creates a Reusable object if it can. If the acquireReusable method cannot create a new Reusable object, then it waits until a Reusable object is returned to the collection.
 * <p>
 * Client objects pass a Reusable object to a ReusablePool object's releaseReusable method when they are finished with the object. The releaseReusable method returns a Reusableobject to the pool of Reusable objects that are not in use.
 * <p>
 * In many applications of the Object Pool pattern, there are reasons for limiting the total number of Reusable objects that may exist. In such cases, the ReusablePool object that creates Reusable objects is responsible for not creating more than a specified maximum number of Reusable objects. If ReusablePool objects are responsible for limiting the number of objects they will create, then the ReusablePool class will have a method for specifying the maximum number of objects to be created. That method is indicated in the above diagram as setMaxPoolSize.
 * <p>
 * Example
 * <p>
 * Do you like bowling? If you do, you probably know that you should change your shoes when you getting the bowling club. Shoe shelf is wonderful example of Object Pool. Once you want to play, you'll get your pair (aquireReusable) from it. After the game, you'll return shoes back to the shelf (releaseReusable).
 * <p>
 * Check list
 * <p>
 * Create ObjectPool class with private array of Objects inside
 * <p>
 * Create acquare and release methods in ObjectPool class
 * <p>
 * Make sure that your ObjectPool is Singleton
 * <p>
 * Rules of thumb
 * <p>
 * The Factory Method pattern can be used to encapsulate the creation logic for objects. However, it does not manage them after their creation, the object pool pattern keeps track of the objects it creates.
 * <p>
 * Object Pools are usually implemented as Singletons.
 */
public class SimpleResourcePool {

    public final static String DEFAULT_RESOURCE_TYPE = "default";

    //默认每种资源类型被分配后24会强制超时
    private final static Long RESOURCE_TYPE_EXPIRED_TIME = 60 * 60 * 24L;
    //资源类型超时时间映射
    private final static Map<String, Long> RESOURCE_EXPIRED_MAP = new HashMap<>();

    //<资源类型, 资源列表>
    private final static ConcurrentMap<String, CopyOnWriteArrayList<Resource>> availableResourcePool = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, CopyOnWriteArrayList<Resource>> lockedResourcePool = new ConcurrentHashMap<>();

    static {
        //释放锁定超时的资源
        lockedResourcePool.forEach((k, v) -> {

        });
    }

    /**
     * 每个资源类型仅仅支持一次初始化操作
     *
     * @param resources
     * @return
     */
    public static boolean init(Collection<Resource> resources) {
        if (resources != null && resources.isEmpty() == false) {
            String insertResourceType = resources.iterator().next().getResourceType();
            CopyOnWriteArrayList<Resource> insertResourceList = availableResourcePool.getOrDefault(insertResourceType, null);
            if (insertResourceList == null) {
                insertResourceList = new CopyOnWriteArrayList<>();
            }
            if (insertResourceList.isEmpty()) {
                availableResourcePool.put(insertResourceType, insertResourceList);
                for (Resource resource : resources) {
                    insertResourceList.add(CloneUtil.clone(resource));
                }
                return insertResourceList.isEmpty() == false;
            }

        }
        return false;
    }

    /**
     * 往资源池中添加资源
     *
     * @param resource
     * @return
     */
    public static boolean addResource(Resource resource) {
        CopyOnWriteArrayList<Resource> existedResourceList = availableResourcePool.getOrDefault(resource.getResourceType(), new CopyOnWriteArrayList<>());
        if (existedResourceList.contains(resource)) {
            return false;
        }

        return existedResourceList.add(resource);
    }

    /**
     * 从资源池中删除资源
     *
     * @param resource
     * @return
     */
    public static boolean removeResource(Resource resource) {
        synchronized ((SimpleResourcePool.class.getCanonicalName() + resource.getResourceType()).intern()) {
            CopyOnWriteArrayList<Resource> existedResourceList = availableResourcePool.getOrDefault(resource.getResourceType(), new CopyOnWriteArrayList<>());
            if (existedResourceList.contains(resource)) {
                return existedResourceList.remove(resource);
            }
        }
        return false;
    }

    /**
     * 从资源池中请求默认类型资源(默认根据usedHistoryTimes使用频率来负载均衡)
     *
     * @param biz 业务隔离字段
     * @param num 请求数量
     * @return
     */
    public static Resource[] acquireResources(String biz, int num) {
        return acquireResources(num, (o1, o2) -> {
            int score1 = getScoreByUsedHistory(o1).intValue();
            int score2 = getScoreByUsedHistory(o2).intValue();
            return Integer.compare(score1, score2);
        }, biz);
    }

    /**
     * 从资源池中请求自定义类型资源(默认根据usedHistoryTimes使用频率来负载均衡)
     *
     * @param num          请求资源数量
     * @param resourceType 请求资源类型
     * @param biz          业务隔离字段
     * @return
     */
    public static Resource[] acquireResources(int num, String resourceType, String biz) {
        return acquireResources(num, resourceType, biz, (o1, o2) -> {
            int score1 = getScoreByUsedHistory(o1).intValue();
            int score2 = getScoreByUsedHistory(o2).intValue();
            return Integer.compare(score1, score2);
        });
    }

    /**
     * 从资源池中请求资源
     *
     * @param num        请求资源数量
     * @param comparator 自定义资源评分排序(分数越高越容易被使用)
     * @param biz        业务隔离字段
     * @return
     */
    public static Resource[] acquireResources(int num, Comparator<Resource> comparator, String biz) {
        return acquireResources(num, DEFAULT_RESOURCE_TYPE, biz, comparator);
    }


    /**
     * 从资源池中请求资源
     *
     * @param num          请求资源数量
     * @param resourceType 请求资源类型
     * @param biz          业务隔离字段
     * @param comparator   自定义竞价排名(分数越大越靠前)
     * @return
     */
    public static Resource[] acquireResources(int num, String resourceType, String biz, Comparator<Resource> comparator) {
        Resource[] result = null;
        CopyOnWriteArrayList<Resource> existedResourceList = availableResourcePool.getOrDefault(resourceType, new CopyOnWriteArrayList<>());
        if (num > 0 && num <= existedResourceList.size()) {
            synchronized ((SimpleResourcePool.class.getCanonicalName() + resourceType).intern()) {
                //DCL
                if (num <= existedResourceList.size()) {
                    result = new Resource[num];
                    existedResourceList.sort(comparator);
                    List<Resource> changedResources = new ArrayList<>();
                    for (int i = 0; i < num; i++) {
                        changedResources.add(existedResourceList.get(i));
                        changedResources.get(i).addHistoryTime();
                        result[i] = CloneUtil.clone(changedResources.get(i));
                        result[i].setBizField(biz);
                    }
                    CopyOnWriteArrayList<Resource> existedLockedResourceList = lockedResourcePool.getOrDefault(resourceType, null);
                    if (existedLockedResourceList == null) {
                        existedLockedResourceList = new CopyOnWriteArrayList<>();
                        lockedResourcePool.put(resourceType, existedLockedResourceList);
                    }
                    existedResourceList.removeAll(changedResources);
                    changedResources.forEach(resource -> resource.setBizField(biz));
                    existedLockedResourceList.addAll(changedResources);
                }
            }
        }
        //资源不够
        return result;
    }

    /**
     * 释放资源
     *
     * @param resourceType 释放资源类型
     * @param resources    释放资源列表
     * @return
     */
    public static boolean releaseResources(String resourceType, Resource... resources) {
        if (resources != null) {
            synchronized ((SimpleResourcePool.class.getCanonicalName() + resourceType).intern()) {
                CopyOnWriteArrayList<Resource> existedLockedResourceList = lockedResourcePool.getOrDefault(resourceType, new CopyOnWriteArrayList<>());
                for (Resource resource : resources) {
                    //从lockedResourcePool中获取资源对象(不能直接使用方法resource入参)
                    if (existedLockedResourceList.contains(resource)) {
                        resource = existedLockedResourceList.get(existedLockedResourceList.indexOf(resource));
                        existedLockedResourceList.remove(resource);
                        //移除业务痕迹
                        resource.setBizField(null);
                        addResource(resource);
                    }
                }
            }
        }
        return true;
    }


    /**
     * 释放资源(默认资源类型)
     *
     * @param resources 资源列表
     * @return
     */
    public static boolean releaseResources(Resource... resources) {
        if (resources != null) {
            return Arrays.stream(resources).filter(resource -> releaseResources(resource.getResourceType(), resource)).count() == resources.length;
        }
        return false;
    }

    /**
     * 获取已经分配的资源
     *
     * @param biz 业务隔离字段
     * @return
     */
    public static Resource[] getAssignedResources(String biz) {
        return getAssignedResources(DEFAULT_RESOURCE_TYPE, biz);
    }

    public static Resource[] getAssignedResources(String resourceType, String biz) {
        synchronized ((SimpleResourcePool.class.getCanonicalName() + resourceType).intern()) {
            CopyOnWriteArrayList<Resource> lockedResources = lockedResourcePool.getOrDefault(resourceType, new CopyOnWriteArrayList<>());
            if (lockedResources.isEmpty()) {
                return null;
            }
            List<Resource> existedResources = lockedResources.stream().filter(resource -> biz.equals(resource.getBizField())).collect(Collectors.toList());
            if (existedResources.isEmpty()) {
                return null;
            }
            return existedResources.toArray(new Resource[0]);
        }
    }

    /**
     * 获取锁定的资源列表
     *
     * @param resourceType 资源类型
     * @return
     */
    public static Resource[] getLockedResources(String resourceType) {
        if (resourceType == null || resourceType.trim().equals("")) {
            resourceType = DEFAULT_RESOURCE_TYPE;
        }
        return lockedResourcePool.getOrDefault(resourceType, new CopyOnWriteArrayList<>()).toArray(new Resource[0]);
    }

    /**
     * 获取默认资源类型的资源列表
     *
     * @return
     */
    public static Resource[] getLockedResources() {
        return getLockedResources(DEFAULT_RESOURCE_TYPE);
    }


    /**
     * 根据使用历史计算分数
     *
     * @param resource
     * @return 评分越高越靠前越容易被使用
     */
    private static Number getScoreByUsedHistory(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("resource is null");
        }

        int score = 0;
        List<LocalDateTime> usedHistory = resource.getUsedHistoryTimes();
        LocalDateTime localDateTime = LocalDateTime.now();
        //按使用时间计分(辅助)
        for (int i = 0; i < usedHistory.size(); i++) {
            long secondsGap = Duration.between(usedHistory.get(i), localDateTime).getSeconds();

            if (secondsGap >= 60 * 8) {
                score += 7;
            } else if (secondsGap >= 60 * 4) {
                score += 6;
            } else if (secondsGap >= 60 * 2) {
                score += 5;
            } else if (secondsGap >= 60) {
                score += 4;
            } else if (secondsGap >= 30) {
                score += 3;
            } else if (secondsGap >= 15) {
                score += 2;
            } else if (secondsGap >= 5) {
                score += 1;
            } else {
                score += 0;
            }
        }
        //按使用次数计分(主要),用主要分数减去辅助分数实现分数倒转(分值大的靠前,分值大表示时间越旧)
        score = usedHistory.size() * 100 - score;
        return score;
    }


    public static void main(String[] args) throws Exception {
        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Resource r = new Resource("r" + i, 5);
            resources.add(r);
        }

        List<Resource> resources2 = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
            Resource r = new Resource("r" + i, "市委", 5);
            resources2.add(r);
        }


        init(resources);
        init(resources2);

        String resultType = "市委";


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    Resource[] res = acquireResources(8, resultType, "thread1");
                    System.out.println("/*----------------------------thread1当前获取资源-----------------------------*/");
                    for (Resource re : res) {
                        System.out.println(re);
                    }
//                    System.out.println("/*----------------------------当前资源库-----------------------------*/");
//                    for (Resource resource : availableResourcePool.get(resultType)) {
//                        System.out.println(resource);
//                    }
//                    System.out.println("/*----------------------------当前锁定库-----------------------------*/");
//                    for (Resource resource : lockedResourcePool.get(resultType)) {
//                        System.out.println(resource);
//                    }
                    System.out.println("/*----------------------------thread1查询已经分配资源-----------------------------*/");
                    for (Resource resource : getAssignedResources(resultType, "thread1")) {
                        System.out.println(resource);
                    }

                    releaseResources(res);

                    System.out.println("/*------------------------------------------------------------------------------------分隔符-------------------------------------------------------------------------------------*/");

                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {

                    }

                } while (true);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    Resource[] res = acquireResources(2, resultType, "thread2");
                    System.out.println("/*----------------------------thread2当前占用资源-----------------------------*/");
                    for (Resource re : res) {
                        System.out.println(re);
                    }
//                    System.out.println("/*----------------------------当前资源库-----------------------------*/");
//                    for (Resource resource : availableResourcePool.get(resultType)) {
//                        System.out.println(resource);
//                    }
//                    System.out.println("/*----------------------------当前锁定库-----------------------------*/");
//                    for (Resource resource : lockedResourcePool.get(resultType)) {
//                        System.out.println(resource);
//                    }
                    System.out.println("/*----------------------------thread2查询已经分配资源-----------------------------*/");
                    for (Resource resource : getAssignedResources(resultType, "thread2")) {
                        System.out.println(resource);
                    }

                    releaseResources(res);

                    System.out.println("/*------------------------------------------------------------------------------------分隔符-------------------------------------------------------------------------------------*/");

                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {

                    }
                } while (true);
            }
        });

        thread1.start();
        thread2.start();

    }


}
