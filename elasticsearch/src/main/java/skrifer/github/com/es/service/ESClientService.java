package skrifer.github.com.es.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
//import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import skrifer.github.com.es.Enum.ESIndexEnum;
import skrifer.github.com.es.annotation.*;
import skrifer.github.com.es.dto.ESBaseDTO;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 基于 rest high level client 的自定义封装
 * <groupId>org.elasticsearch.client</groupId>
 * <artifactId>elasticsearch-rest-high-level-client</artifactId>
 * <version>7.0.1</version>
 * 版本必须统一
 *
 * @param <T>
 */
@Slf4j
public abstract class ESClientService<T extends ESBaseDTO, E> {

    ConcurrentMap<ESClientService, Class<T>> esServiceDTOMap = new ConcurrentHashMap<>();
    ConcurrentMap<ESClientService, ESIndexEnum> esServiceEnumMap = new ConcurrentHashMap<>();

    public Class<T> getType() {
        if (esServiceDTOMap.containsKey(this)) {
            return esServiceDTOMap.get(this);
        }
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) actualTypeArguments[0];
        esServiceDTOMap.put(this, clazz);
        return clazz;
    }

    public ESIndexEnum getEnum() {
        if (esServiceEnumMap.containsKey(this)) {
            return esServiceEnumMap.get(this);
        }
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<E> clazz = (Class<E>)actualTypeArguments[1];
        Assert.isTrue(clazz.isEnum(), "泛型类型不是期望的枚举");
        ESIndexEnum[] esIndexEnums = (ESIndexEnum[])clazz.getEnumConstants();
        esServiceEnumMap.put(this, esIndexEnums[0]);
        return esIndexEnums[0];
    }

    @Autowired(required = false)
    private RestHighLevelClient client;

    private final Map<String, List<String>> ES_BASE_DTO_INGORE_PROPERTIES = new ConcurrentHashMap<>();

    /**
     * 新增数据
     *
     * @param list
     * @return 返回失败的id集合
     */
    public List<String> add(List<T> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        BulkRequest bulkRequest = addConvert(list);
        Assert.notEmpty(bulkRequest.requests(), "不能添加空数据!");
        try {
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            RestStatus status = response.status();
            System.out.println("status = " + status);
            Map<String, String> failureMap = Arrays.stream(response.getItems())
                    .filter(item -> item.getFailure() != null)
                    .collect(Collectors.toMap(BulkItemResponse::getId, BulkItemResponse::getFailureMessage));
            failureMap.forEach((id, msg) -> log.error("es索引库[{}] id[{}] 插入更新数据失败:{}", getEnum().getName(), id, msg));
            return new ArrayList<>(failureMap.keySet());
        } catch (IOException e) {
            log.error("es索引库[{}] 插入数据异常:{}", getEnum().getName(), e.getMessage());
        }
        return Collections.emptyList();
    }


    /**
     * 删除数据
     *
     * @param ids
     */
    public void del(List<String> ids) {
        ids.parallelStream().forEach(id -> {
            try {
                DeleteResponse response = client.delete(new DeleteRequest(getEnum().getName(), id), RequestOptions.DEFAULT);
                RestStatus status = response.status();
                System.out.println("status = " + status);
            } catch (IOException e) {
                log.error("es索引库[{}] 删除数据异常:{}", getEnum().getName(), e.getMessage());
            }
        });
    }


    /**
     * 获取数据
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> String get(String id) {

        try {
            GetResponse response = client.get(new GetRequest(getEnum().getName(), id), RequestOptions.DEFAULT);
            return response.getSourceAsString();
        } catch (IOException e) {
            log.error("es索引库[{}] 查询数据异常:{}", getEnum().getName(), e.getMessage());
        }
        return null;
    }

    /**
     * 更新数据
     *
     * @param list
     * @param <T>
     */
    public <T> void update(List<T> list) {
        List<UpdateRequest> updateRequests = new ArrayList<>();
        Assert.notEmpty(updateRequests, "请先完善updateConvert方法");
        updateRequests.parallelStream().forEach(updateRequest -> {
            try {
                UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
                RestStatus status = update.status();
                System.out.println("status = " + status);
            } catch (IOException e) {
                log.error("es索引库[{}] 更新数据异常:{}", getEnum().getName(), e.getMessage());
            }
        });
    }


    public Map<String, String> search(AbstractQueryBuilder queryBuilder) {
        Map<String, String> result = new HashMap<>();
        //创建搜索对象，入参可以为多个索引库参数
        SearchRequest searchRequest = new SearchRequest(getEnum().getName());
        //创建查询构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        //设置查询构造器
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(100000);
        searchSourceBuilder.trackTotalHits(true);

        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            //遍历每一条记录
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                result.put(hit.getId(), sourceAsString);
            }
        } catch (Exception e) {
            log.error("es索引库[{}] 查询数据[{}] 异常:{}", getEnum().getName(), queryBuilder.toString(), e.getMessage());
        }
        return result;
    }

    public Map<String, String> search(SearchSourceBuilder searchSourceBuilder) {
        Map<String, String> result = new HashMap<>();
        //创建搜索对象，入参可以为多个索引库参数
        SearchRequest searchRequest = new SearchRequest(getEnum().getName());
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            //遍历每一条记录
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                result.put(hit.getId(), sourceAsString);
            }
        } catch (Exception e) {
            log.error("es索引库[{}] 查询数据[{}] 异常:{}", getEnum().getName(), searchSourceBuilder.query().toString(), e.getMessage());
        }
        return result;
    }

    public Map<String, String> searchAggregations(SearchRequest searchRequest) {
        Map<String, String> result = new HashMap<>();

        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            result.put("total", String.valueOf(search.getHits().getTotalHits().value));
            for (Aggregation aggregation : search.getAggregations()) {
                result.put(aggregation.getName(), String.valueOf(((NumericMetricsAggregation.SingleValue) aggregation).value()));
            }
        } catch (Exception e) {
            log.error("es索引库[{}] 异常:{}", searchRequest.indices()[0], e.getMessage());
        }
        return result;
    }

    /**
     * 默认字段都是"type": "keyword"
     * 特殊字段类型使用注解 @ESTextField @ESIntegerField @ESFloatField @ESDateTimeField
     *
     * @return
     */
    public String getESMappingConfig() {
        JSONObject result = new JSONObject();
        JSONObject properties = new JSONObject();

        result.put("properties", properties);
        Field[] fields = ReflectUtil.getFields(getType());
        for (Field field : fields) {
            if (field.isAnnotationPresent(ESIgnoreField.class)) {
                continue;
            }
            String name = field.getName();
            JSONObject fieldJson = new JSONObject();
            properties.put(name, fieldJson);
            if (field.isAnnotationPresent(ESTextField.class)) {
                fieldJson.put("type", "text");
            } else if (field.isAnnotationPresent(ESIntegerField.class)) {
                fieldJson.put("type", "integer");
            } else if (field.isAnnotationPresent(ESLongField.class)) {
                fieldJson.put("type", "long");
            } else if (field.isAnnotationPresent(ESFloatField.class)) {
                fieldJson.put("type", "float");
            } else if (field.isAnnotationPresent(ESDateTimeField.class)) {
                fieldJson.put("type", "date");
                fieldJson.put("format", field.getAnnotation(ESDateTimeField.class).format());
            } else {
                fieldJson.put("type", "keyword");
            }
        }
        return result.toString();
    }

    /**
     * refreshPolicy 一共有三种策略:
     * 1. NONE 默认策略 最省资源的策略 如果不涉及更新后立刻需要 查询获取 则优先此策略，完全交由es系统自主同步刷新索引数据
     * 2. IMMEDIATE 异步响应策略，在提交完更新后 由es 立即刷新索引数据，相比于 WAIT_UNTIL 可以提高更新接口的响应速度
     * 3. WAIT_UNTIL 同步响应测录，在提交完策略后 同时刷新es索引数据，直到索引数据刷新完毕后 返回更新响应，相对于IMMEDIATE 可以保证更新API操作完成后索引数据的同步！缺点是更新接口的响应速度会变慢
     *
     * @param list
     * @return
     */
    private BulkRequest addConvert(List<T> list) {
        BulkRequest result = new BulkRequest();
        result.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        List<String> ignoreProperties = ES_BASE_DTO_INGORE_PROPERTIES.getOrDefault(getType().getSimpleName(), null);
        if (ignoreProperties == null) {
            ignoreProperties = Arrays.stream(ReflectUtil.getFields(getType())).filter(field -> field.isAnnotationPresent(ESIgnoreField.class)).map(Field::getName).collect(Collectors.toList());
            ES_BASE_DTO_INGORE_PROPERTIES.put(getType().getSimpleName(), ignoreProperties);
        }
        List<String> ignores = ignoreProperties;
        String defaultUser = "system";
        String defaultTime = DateUtil.now();
        list.stream().map(e -> {
            if (e.getInsertBy() == null) {
                e.setInsertBy(defaultUser);
            }
            if (e.getInsertTime() == null) {
                e.setInsertTime(defaultTime);
            }
            if (e.getUpdateBy() == null) {
                e.setUpdateBy(defaultUser);
            }
            if (e.getUpdateTime() == null) {
                e.setUpdateTime(defaultTime);
            }
            if (e.getVersion() == null) {
                e.setVersion(0);
            }
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(e);
            for (String ignore : ignores) {
                stringObjectMap.remove(ignore);
            }
            return new IndexRequest(getEnum().getName()).id(e.getKey()).source(stringObjectMap);
        }).forEach(result::add);

        return result;
    }
}
