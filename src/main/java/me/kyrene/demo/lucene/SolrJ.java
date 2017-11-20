package me.kyrene.demo.lucene;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * item.sql。是本测试使用的数据库数据
 * Created by wanglin on 2017/11/20.
 */
public class SolrJ {

    @Test
    /**
     * 以document的形式创建或修改数据
     */
    public void test01() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");

        // 创建要添加的文档信息
        SolrInputDocument doc = new SolrInputDocument();
        // 添加字段
        doc.addField("id", 15L);
        doc.addField("title", "8848钛金手机，高端大气上档次");
        doc.addField("price", 199900);

        // 把文档添加到服务器
        server.add(doc);
        // 提交
        server.commit();
    }

    @Test
    /**
     * 以bean的方式添加。 注意实体类中要加@Field来告诉solrj 这个是要添加到索引库的
     */
    public void test02() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");

        //创建商品对象
        Item item = new Item();
        item.setId(16);
        item.setPrice(88990);
        item.setTitle("史上最强旗舰机");
        //添加单个
        server.addBean(item);
        //添加多个
//        server.addBeans(item);

        //提交
        server.commit();
    }

    @Test
    /**
     * 使用solrj删除索引
     */
    public void test03() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");

        //根据id删除  精准删除
        server.deleteById("16");

        //这个就是全部删除了 title: 诺基亚  就是删除title中带诺基亚的
//        server.deleteByQuery("*:*");
        //提交
        server.commit();
    }

    @Test
    /**
     * 以document的形式查询数据
     */
    public void test04() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        //获取查询
        SolrQuery query = new SolrQuery("title:华为");
        //获取响应
        QueryResponse queryResponse = server.query(query);
        //获取结果的文档集合
        SolrDocumentList documentList = queryResponse.getResults();
        System.out.println("本次查询共获得" + documentList.size() + "条数据");

        for (SolrDocument document : documentList) {
            // 取出结果
            System.out.println("id: " + document.getFieldValue("id"));
            System.out.println("title: " + document.getFieldValue("title"));
            System.out.println("price: " + document.getFieldValue("price"));

        }
    }

    @Test
    /**
     * 以bean的方式进行查询
     */
    public void test05() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        //获取查询
        SolrQuery query = new SolrQuery("title:移动");
        //获取响应
        QueryResponse queryResponse = server.query(query);
        // 解析响应，获取JavaBean集合
        List<Item> items = queryResponse.getBeans(Item.class);
        System.out.println("共搜索到" + items.size() + "条数据");
        //iter
        for (Item bean : items) {
            System.out.println("id: " + bean.getId());
            System.out.println("title: " + bean.getTitle());
            System.out.println("price: " + bean.getPrice());

        }
    }

    @Test
    /**
     * 高级查询
     */
    public void test06() throws IOException, SolrServerException {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        // 布尔查询
//		SolrQuery query = new SolrQuery("title:小米 OR title:apple");

        // 相似度查询,最大编辑距离也是2
		SolrQuery query = new SolrQuery("title:applr~");
//		SolrQuery query = new SolrQuery("title:spplr~1");

        // 数值范围查询,两侧都是包含
//        SolrQuery query = new SolrQuery("price:[139900 TO 269900]");

        // 设置查询的条数
        query.setRows(20);

        // 执行查询，获取响应
        QueryResponse response = server.query(query);
        // 解析响应，获取JavaBean集合
        List<Item> items = response.getBeans(Item.class);
        System.out.println("共搜索到" + items.size() + "条数据");
        for (Item item : items) {
            System.out.println("id: " + item.getId());
            System.out.println("title: " + item.getTitle());
            System.out.println("price: " + item.getPrice());
        }
    }

    /**
	 * 分页查询
	 */
    @Test
    public void test07() throws Exception {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        // 创建查询对象
        SolrQuery query = new SolrQuery("*:*");

        // 分页参数：
        int pageSize = 5;// 每页条数
        int pageNum = 3;// 当前页
        int start = (pageNum - 1) * pageSize;
        query.setStart(start);// 设置起始编号
        query.setRows(pageSize);// 设置每页条数

        // 执行查询，获取响应
        QueryResponse response = server.query(query);
        // 解析响应，获取JavaBean集合
        List<Item> items = response.getBeans(Item.class);
        System.out.println("共搜索到" + items.size() + "条数据");
        for (Item item : items) {
            System.out.println("id: " + item.getId());
            System.out.println("title: " + item.getTitle());
            System.out.println("price: " + item.getPrice());
        }
    }

    /**
	 * SolrJ查询索引。并且进行高亮显示
	 */
    @Test
    public void test08() throws Exception {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        // 创建查询对象
        SolrQuery query = new SolrQuery("title:手机");
        // 设置查询条数
        query.setRows(20);
        // 开启高亮显示
        query.setHighlight(true);
        query.setHighlightSimplePre("<em>");// 设置前置标签
        query.setHighlightSimplePost("</em>");// 设置后置标签
        query.addHighlightField("title");// 设置高亮的字段

        // 执行查询，获取响应
        QueryResponse response = server.query(query);
        // 解析响应，获取高亮数据
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // 获取非高亮结果
        List<Item> items = response.getBeans(Item.class);
        for (Item item : items) {
            System.out.println("id:" + item.getId());
            // 根据ID获取当前商品的所有高亮字段的Map集合，然后从集合中获取高亮字段的值，然后获取其中第1个
            System.out.println(highlighting.get(item.getId()+"").get("title").get(0));
            System.out.println("price:" + item.getPrice());
        }
//		// 先获取所有的键，其实就是所有文档的ID集合
//		Set<String> ids = highlighting.keySet();
//		// 遍历取出每一个ID
//		for (String id : ids) {
//			System.out.println("id: " + id);
//			// 根据ID取出这个文档的其它字段形成的Map集合
//			Map<String, List<String>> fields = highlighting.get(id);
//			// 从字段Map集合中，取出对应的字段,得到的是一个List，而我们只要List的第1条数据
//			System.out.println("title: " + fields.get("title").get(0));
//		}
    }

    @Test
    /**
     * solr实现排序
     */
    public void test09() throws Exception {
        // 连接Solr服务器,注意：路径中一定不要有#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/collection1");
        // 创建查询对象
        SolrQuery query = new SolrQuery("title:华为");
        // 所有的高级的查询参数和功能，都是通过SolrQuery对象来进行的

        // 排序
        query.setSort("price", SolrQuery.ORDER.desc);

        // 执行查询，获取响应
        QueryResponse response = server.query(query);
        // 解析响应，获取JavaBean集合
        List<Item> items = response.getBeans(Item.class);
        System.out.println("共搜索到" + items.size() + "条数据");
        for (Item item : items) {
            System.out.println("id: " + item.getId());
            System.out.println("title: " + item.getTitle());
            System.out.println("price: " + item.getPrice());
        }

    }
}
