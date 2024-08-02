package skrifer.github.com.es.init;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import skrifer.github.com.es.Enum.ESIndexEnum;
import skrifer.github.com.es.service.ESClientService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class InitEsIndex implements ApplicationRunner {

    @Autowired
    private RestHighLevelClient client;

//    @Value("${spring.elasticsearch.rest.uris}")
//    private String esUrl;

    @Value("#{'${elasticsearch.deleteIndexes:abc}'.split(',')}")
    private List<String> deleteIndexes;

    @Value("${elasticsearch.deleteAllIndexes.deadlineTime:0}")
    private long esDeleteDeadLineTime;

    @Autowired
    private List<ESClientService> esClientServices;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //按照过期时间全量删除索引
        if (esDeleteDeadLineTime > System.currentTimeMillis()) {
            for (ESIndexEnum value : ESIndexEnum.values()) {
                if(Index(value.getName())){
                    client.indices().delete(new DeleteIndexRequest(value.getName()), RequestOptions.DEFAULT);
                    log.info("delete es index :{}", value.getName());
                }
            }
        }
        //删除指定索引
        if (deleteIndexes != null || deleteIndexes.isEmpty() == false) {
            for (String deleteIndex : deleteIndexes) {
                if (StrUtil.isNotBlank(deleteIndex.trim()) && Index(deleteIndex.trim())) {
                    client.indices().delete(new DeleteIndexRequest(deleteIndex.trim()), RequestOptions.DEFAULT);
                    log.info("delete es index :{}", deleteIndex.trim());
                }
            }
        }
        //重建不存在的索引
        for (ESClientService esClientService : esClientServices) {
            ESIndexEnum anEnum = esClientService.getEnum();
            if (Index(anEnum.getName()) == false) {
                createIndex(anEnum.getName(), anEnum.getShards(), anEnum.getReplicas(), esClientService.getESMappingConfig());
            }
        }
        //重置索引index.max_inner_result_window（ES集群重启会失效!） 用来控制索引下聚合分组时 top_hits的最大反馈列表长度
        {
//            esClientServices.get(0).resetElasticsearchIndexMaxInnerResultWindow(ESIndexEnum.DATA_GATHER_VIDEO_REPORT_INFO.getName(), 1000);
        }

    }

    private void createIndex(String indexName, int shards, int replicas, String source) throws IOException {
        // 创建索引
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        //分片参数
        indexRequest.settings(Settings.builder()
                //分片数
                .put("index.number_of_shards", shards)
                // 副本数
                .put("index.number_of_replicas", replicas)
                .put("max_result_window", 100000000)
        );
        //设置映射
        indexRequest.mapping(source, XContentType.JSON);
        // 创建索引操作客户端
        IndicesClient indices = client.indices();
        // 创建响应结果
        CreateIndexResponse createIndexResponse = indices.create(indexRequest, RequestOptions.DEFAULT);
        //获取响应值
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println("acknowledged = " + acknowledged);

    }

    private boolean Index(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }
}
