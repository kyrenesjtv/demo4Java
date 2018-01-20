package me.kyrene.demo.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by wanglin on 2018/1/20.
 */
public class Test {
    TransportClient client;
    //索引库名
    String index = "blog";
    //类型名称
    String type = "article";

    @Before
    public void init() throws Exception {
        Settings settings = Settings.builder()
                // .put("cluster.name", "my-application") // 指定集群名称
                //.put("client.transport.sniff", true) // 启动嗅探功能
                .build();
        // 创建client
        client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.01"), 9300));

        // 查看集群信息
        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode node : connectedNodes) {
            System.out.println(node.getHostAddress());
        }
    }

    /**
     * 添加json
     */
    @org.junit.Test
    public void testAddMap() {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("name", "linzai444");
        infoMap.put("title", "无敌");
        infoMap.put("createTime", new Date());
        infoMap.put("count", 444);
        IndexResponse response = client.prepareIndex(index, type, "444").setSource(infoMap).execute().actionGet();
        System.out.println("ID：" + response.getId());
        System.out.println("index：" + response.getIndex());
        System.out.println("type：" + response.getType());
        System.out.println("source：" + response.getResult());
        System.out.println("ShardId：" + response.getShardId());
        System.out.println("ShardInfo：" + response.getShardInfo());
        System.out.println("Version：" + response.getVersion());
    }

    /**
     * 添加实体类,但是有一个key
     */
    @org.junit.Test
    public void testAddObject() {
        User user = new User();
        user.setId(11L);
        user.setName("linzai222");
        user.setTitle("头发少");
        IndexResponse response = client.prepareIndex(index, type, "200").setSource("user", user).execute().actionGet();
        System.out.println("ID：" + response.getId());
        System.out.println("index：" + response.getIndex());
        System.out.println("type：" + response.getType());
        System.out.println("source：" + response.getResult());
        System.out.println("ShardId：" + response.getShardId());
        System.out.println("ShardInfo：" + response.getShardInfo());
        System.out.println("Version：" + response.getVersion());

    }

    /**
     * 通过XContentFactory添加
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testAddField() throws IOException {
        XContentBuilder source = XContentFactory.jsonBuilder().startObject().field("title", "Hello, World! ").field("content", "头发都要掉光了").endObject();
        IndexResponse response = client.prepareIndex(index, type, "300").setSource(source).execute().actionGet();
        System.out.println("ID：" + response.getId());
        System.out.println("index：" + response.getIndex());
        System.out.println("type：" + response.getType());
        System.out.println("source：" + response.getResult());
        System.out.println("ShardId：" + response.getShardId());
        System.out.println("ShardInfo：" + response.getShardInfo());
        System.out.println("Version：" + response.getVersion());
    }

    /**
     * 通过id获取
     */
    @org.junit.Test
    public void testFindByID() {
        GetResponse resp1 = client.prepareGet(index, type, "100").get();//上下是一样的
        //        GetResponse resp1 = client.prepareGet(index, type, "100").execute().actionGet();
        GetResponse resp2 = client.prepareGet(index, type, "200").get();
        GetResponse resp3 = client.prepareGet(index, type, "300").get();
        System.out.println(resp1.getSourceAsString());
        System.out.println(resp2.getSourceAsString());
        System.out.println(resp3.getSourceAsString());
    }

    /**
     * 通过id更新
     */
    @org.junit.Test
    public void testUpdateByID() {
        GetResponse resp1 = client.prepareGet(index, type, "100").get();
        System.out.println(resp1.getSourceAsString());
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("name", "林仔222");
        infoMap.put("title", "无敌2222");
        infoMap.put("createTime", new Date());
        infoMap.put("count", 1022);
        UpdateResponse get = client.prepareUpdate(index, type, "100").setDoc(infoMap).execute().actionGet();
        GetResponse resp2 = client.prepareGet(index, type, "100").get();
        System.out.println(resp2.getSourceAsString());
    }

    /**
     * 通过id删除
     */
    @org.junit.Test
    public void testDeleteByID() {
        client.prepareDelete(index, type, "101").execute().actionGet();
        //        GetResponse resp2 = client.prepareGet(index, type, "101").get();
        //        System.out.println(resp2.getSourceAsString());
    }

    /**
     * 删除所有
     */
    @org.junit.Test
    public void testDeleteAll() {
        //        client.prepareDelete().execute().actionGet();
        //        GetResponse resp2 = client.prepareGet(index, type, "200").get();
        //        System.out.println(resp2.getSourceAsString());
    }

    /**
     * 用bulk 进行批处理
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBulk() throws IOException {
        //生成bulk
        BulkRequestBuilder bulk = client.prepareBulk();
        //新增
        IndexRequest add = new IndexRequest(index, type, "1");
        add.source(XContentFactory.jsonBuilder().startObject().field("name", "Henrry").field("age", 30).endObject());

        //删除
        DeleteRequest del = new DeleteRequest(index, type, "1");
        //修改
        XContentBuilder source = XContentFactory.jsonBuilder().startObject().field("name", "jack_1").field("age", 19).endObject();
        UpdateRequest update = new UpdateRequest(index, type, "1");
        update.doc(source);

        bulk.add(del);
        bulk.add(add);
        bulk.add(update);

        //执行批处理
        BulkResponse bulkResponse = bulk.get();
        if (bulkResponse.hasFailures()) {
            BulkItemResponse[] items = bulkResponse.getItems();
            for (BulkItemResponse item : items) {
                System.out.println(item.getFailureMessage());
            }
        } else {
            System.out.println("全部执行成功！");
        }
    }

    /**
     * 多种查询
     */
    @org.junit.Test
    public void testSearch() {
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                //.setQuery(QueryBuilders.matchAllQuery())//查询所有 1
                // .setQuery(QueryBuilders.matchQuery("name", "linzai111").operator(Operator.AND)) //根据tom分词查询name,默认or 1
                .setQuery(QueryBuilders.multiMatchQuery("linzai111", "count", "count")) //指定查询的字段 1
                // .setQuery(QueryBuilders.queryStringQuery("name:linzai111 AND count:[0 TO 222]")) //根据条件查询,支持通配符大于等于0小于等于19 1
                //   .setQuery(QueryBuilders.termQuery("name", "linzai111"))//查询时不分词 1
                .setSearchType(SearchType.QUERY_THEN_FETCH).setFrom(0).setSize(10)//分页
                .addSort("count", SortOrder.DESC)//排序 1
                .get();
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();//有多少个数据
        System.out.println(total);
        SearchHit[] searchHits = hits.getHits();//获取到所有数据
        for (SearchHit s : searchHits) {
            System.out.println(s.getSourceAsString());
        }
    }

    /**
     * 多索引，多类型查询
     */
    @org.junit.Test
    public void testSearchsAndTimeout() {
        SearchResponse searchResponse = client.prepareSearch("shb01", "shb02").setTypes("stu", "tea").setQuery(QueryBuilders.matchAllQuery()).setSearchType(SearchType.QUERY_THEN_FETCH).setTimeout(new TimeValue(3000)).get();

        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);
        SearchHit[] hits2 = hits.getHits();
        for (SearchHit h : hits2) {
            System.out.println(h.getSourceAsString());
        }
    }

    /**
     * 过滤，
     * lt 小于
     * gt 大于
     * lte 小于等于
     * gte 大于等于
     */
    @org.junit.Test
    public void testFilter() {
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.matchAllQuery()) //查询所有
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                //               .setPostFilter(FilterBuilders.rangeFilter("age").from(0).to(19)
                //                      .includeLower(true).includeUpper(true))
                //                .setPostFilter(FilterBuilders.rangeFilter("age").gte(18).lte(22))
                .setPostFilter(QueryBuilders.rangeQuery("count").gte(100).lte(300)).setExplain(true) //explain为true表示根据数据相关度排序，和关键字匹配最高的排在前面
                .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s : searchHits) {
            System.out.println(s.getSourceAsString());
        }
    }

    /**
     * 高亮
     */
    @org.junit.Test
    public void testHighLight() {
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<h2>");
        hiBuilder.postTags("</h2>");
        hiBuilder.field("name");
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                //  .setQuery(QueryBuilders.matchQuery("name", "wanglin111")) //查询所有
                .setQuery(QueryBuilders.queryStringQuery("name:linzai111"))
                //              .setSearchType(SearchType.QUERY_THEN_FETCH)
                //              .addHighlightedField("name")
                // .setHighlighterPreTags("<font color='red'>")
                //.setHighlighterPostTags("</font>")
                .highlighter(hiBuilder).get();

        SearchHits hits = searchResponse.getHits();
        System.out.println("sum:" + hits.getTotalHits());

        SearchHit[] hits2 = hits.getHits();
        for (SearchHit s : hits2) {
            Map<String, HighlightField> highlightFields = s.getHighlightFields();
            HighlightField highlightField = highlightFields.get("name");
            if (null != highlightField) {
                Text[] fragments = highlightField.fragments();
                System.out.println(fragments[0]);
            }
            System.out.println(s.getSourceAsString());
        }
    }

    /**
     * 分组
     */
    @org.junit.Test
    public void testGroupBy() {
        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("name-term")
                        .field("name")//这里面的field要有重复的才可以分组
                        .size(0))//根据age分组，默认返回10，size(0)也是10
                .get();

        Terms terms = searchResponse.getAggregations().get("name-term");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bt : buckets) {
            System.out.println(bt.getKey() + " " + bt.getDocCount());
        }
    }

    /**
     * 聚合函数,本例之编写了sum，其他的聚合函数也可以实现
     */
    @org.junit.Test
    public void testMethod() {
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("group_name")
                        .field("name").subAggregation(AggregationBuilders
                                .sum("sum_age")
                                .field("age")))
                .get();

        Terms terms = searchResponse.getAggregations().get("group_name");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bt : buckets) {
            Sum sum = bt.getAggregations().get("sum_age");
            System.out.println(bt.getKey() + "  " + bt.getDocCount() + " " + sum.getValue());
        }
    }
}
