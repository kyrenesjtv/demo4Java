package me.kyrene.demo.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wanglin on 2017/11/19
 *   resources文件夹下面的IKAnalyzer.cfg.xml为自定的扩展词典和停止词字典
 */
public class LuceneTest {

    @Test
    /**
     * 创建索引 使用lucene自带分词
     */
    public void test01() throws IOException {
        //创建文档对象
        Document document = new Document();
        //添加字段，这里字段的参数:字段的名称，字段的值，是否存储  yes存储，no不存储
        document.add(new StringField("id","1", Field.Store.YES));
        //StringField会创建索引，但是不做分词，TextField会创建索引并且进行分词
        document.add(new TextField("titlle","谷歌之父跳槽Facebook", Field.Store.YES));
        //创建索引目录对象
        FSDirectory dir = FSDirectory.open(new File("indexDIR"));
        //创建分词对象
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //创建索引写出的配置类
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        //创建索引写出工具
        IndexWriter writer = new IndexWriter(dir, config);
        //将文档添加到索引写出工具
        writer.addDocument(document);
        //提交
        writer.commit();
        //关闭
        writer.close();
    }
    @Test
    /**
     * 创建索引 使用ik分词器
     */
    public void test02() throws IOException {
        //创建文档对象

        Document document = new Document();
        //添加字段，这里字段的参数:字段的名称，字段的值，是否存储  yes存储，no不存储
        document.add(new StringField("id","1", Field.Store.YES));
        //StringField会创建索引，但是不做分词，TextField会创建索引并且进行分词
        document.add(new TextField("titlle","谷歌之父跳槽Facebook", Field.Store.YES));
        //创建索引目录对象
        FSDirectory dir = FSDirectory.open(new File("indexDIR"));
        //创建分词对象
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        IKAnalyzer analyzer = new IKAnalyzer();
        //创建索引写出的配置类
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        //创建索引时 覆盖。create先清空索引再创建,append 是追加，每次添加索引都是添加末尾
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建索引写出工具
        IndexWriter writer = new IndexWriter(dir, config);
        //将文档添加到索引写出工具
        writer.addDocument(document);
        //提交
        writer.commit();
        //关闭
        writer.close();
    }

    @Test
    /**
     * 批量创建索引
     */
    public void test03()throws IOException{
        // 创建文档的集合
        Collection<Document> docs = new ArrayList<>();
        // 创建文档对象
        Document document1 = new Document();
        document1.add(new StringField("id", "1", Field.Store.YES));
//        document1.add(new LongField("id", 1L, Field.Store.YES));
        document1.add(new TextField("title", "谷歌地图之父跳槽FaceBook呵呵", Field.Store.YES));
        docs.add(document1);
        Document document2 = new Document();
        document2.add(new StringField("id", "2", Field.Store.YES));
//        document2.add(new LongField("id", 2L, Field.Store.YES));
        document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Field.Store.YES));
        docs.add(document2);
        Document document3 = new Document();
        document3.add(new StringField("id", "3", Field.Store.YES));
//        document3.add(new LongField("id", 3L, Field.Store.YES));
        TextField field = new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook啊", Field.Store.YES);
        //激励因子，改变排序，(一般是查询的匹配度越高排名越前，这个直接就改变了)
        field.setBoost(10.0f);
        document3.add(field);
        docs.add(document3);
        Document document4 = new Document();
        document4.add(new StringField("id", "4", Field.Store.YES));
//        document4.add(new LongField("id", 4L, Field.Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关111", Field.Store.YES));
        docs.add(document4);
        Document document5 = new Document();
        document5.add(new StringField("id", "5", Field.Store.YES));
//        document5.add(new LongField("id", 5L, Field.Store.YES));
        document5.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES));
        docs.add(document5);

        // 创建索引目录对象
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建分词器对象
        Analyzer analyzer = new IKAnalyzer();

        // 创建索引写出工具的配置类
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
        // 设置打开方式：默认是OpenMode.APPEND，代表每次添加索引都追加到末尾；OpenMode.CREATE代表先清空索引，再添加
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 添加文档集合 到索引写出工具
        writer.addDocuments(docs);
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }
    @Test
    public  void test04()throws Exception {
        //创建索引目录对象
        FSDirectory dir = FSDirectory.open(new File("indexDir"));
        //创建索引读取工具
        DirectoryReader reader = DirectoryReader.open(dir);
        //索引的搜索工具类
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建查询解析器。 参数默认查询的字段，分词器对象
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        //创建查询对象
        Query query = parser.parse("跳槽");
        // 执行Query对象，搜索数据。参数：查询对象Query，查询结果的前N条数据
        // 返回的是：相关度最高的前N名的文档信息（包含文档的编号以及查询到的总数量）
        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("本次共搜索到" + topDocs.totalHits + "条数据");
        //获取数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 遍历
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取一个文档的编号
            int docID = scoreDoc.doc;
            // 根据编号获取文档
            Document document = reader.document(docID);
            System.out.println("id: " + document.get("id"));
            System.out.println("title: " + document.get("title"));
            // 打印得分
            System.out.println("得分： " + scoreDoc.score);
        }
    }

    /**
     * 抽取出来的
     * @param query
     * @throws IOException
     */
    public void search( Query query) throws IOException {
        //创建索引目录对象
        FSDirectory dir = FSDirectory.open(new File("indexDir"));
        //创建索引读取工具
        DirectoryReader reader = DirectoryReader.open(dir);
        //索引的搜索工具类
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建查询解析器。 参数默认查询的字段，分词器对象
//        QueryParser parser = new QueryParser("title", new IKAnalyzer());
//        //创建查询对象
//        Query query = parser.parse("跳槽");
        // 执行Query对象，搜索数据。参数：查询对象Query，查询结果的前N条数据
        // 返回的是：相关度最高的前N名的文档信息（包含文档的编号以及查询到的总数量）
        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("本次共搜索到"+topDocs.totalHits+"条数据");
        //获取数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 遍历
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取一个文档的编号
            int docID = scoreDoc.doc;
            // 根据编号获取文档
            Document document = reader.document(docID);
            System.out.println("id: " + document.get("id"));
            System.out.println("title: " + document.get("title"));
            // 打印得分
            System.out.println("得分： " + scoreDoc.score);
        }
    }
    @Test
    /**
     * 多样查询
     */
    public void test05(){
        try {
            //普通查询
//            Query query = new QueryParser("title", new IKAnalyzer()).parse("跳槽");
            //词条查询
            Query query = new TermQuery(new Term("id", "1"));
            //模糊查询  必须要带*，前后看你自己了
//            Query query = new WildcardQuery(new Term("title", "*斯*"));
            //相似度查询
            //编辑距离默认是2  即修改两次，也可以自己手动修改编辑距离，但是距离在0-2之间
//            Query query = new FuzzyQuery(new Term("title", "facebookl"));
//            Query query = new FuzzyQuery(new Term("title", "facebookl"),1);
            //数字边界查询,参数 ：字段名称，最小值，最大值，是否包括，是否包括
//            Query query = NumericRangeQuery.newLongRange("id", 1L, 2L, true, true);
            //组合查询 must 必须成立  must_not必须不成立 则结果只有id为1L的
//            Query query1 = NumericRangeQuery.newLongRange("id", 1L, 3L, true, true);
//            Query query2 = NumericRangeQuery.newLongRange("id", 2L, 4L, true, true);
//            BooleanQuery query = new BooleanQuery();
//            query.add(query1, BooleanClause.Occur.MUST);
//            query.add(query2, BooleanClause.Occur.MUST_NOT);
            search(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
	 * 演示：修改索引。
	 * 一般情况下，我们修改索引，一定是要精确修改某一个，因此一般会根据ID字段进行修改。
	 * 但是在Lucene中，词条查询要求字段必须是字符串类型，所以，我们的ID也必须是字符串
     * 如果ID为数值类型，要修改一个指定ID的文档。我们可以先删除，再添加。
	 */
    @Test
    public  void test06()throws Exception{
        // 创建目录
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 创建索引写出类
        IndexWriter writer = new IndexWriter(directory, conf);

        // 创建新的文档对象
        Document doc = new Document();
        doc.add(new StringField("id","1", Field.Store.YES));
        doc.add(new TextField("title", "谷歌地图之父跳槽FaceBook 加入mircosoft 屌爆了", Field.Store.YES));

        // 修改文档，两个参数：一个词条，通过词条精确匹配一个要修改的文档;要修改的新的文档数据
        writer.updateDocument(new Term("id","1"), doc);
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }
    /**
     * 演示：删除索引。
     * 1）一次删除一个：
     * 	一般情况下，我们删除索引，一定是要精确删除某一个，因此一般会根据ID字段进行删除。
     * 	但是在Lucene中，词条查询要求字段必须是字符串类型，所以，我们的ID也必须是字符串
     * 2）删除所有
     * 	deleteAll()
     */
    @Test
    public  void test07()throws Exception{
        // 创建目录
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 创建索引写出类
        IndexWriter writer = new IndexWriter(directory, conf);

        // 根据词条删除索引,一次删1条,要求ID必须是字符串
         writer.deleteDocuments(new Term("id", "1"));

        // 如果ID为数值类型，那么无法根据Term删除，那么怎么办？

        // 根据Query删除索引,我们用NumericRangeQuery精确锁定一条 指定ID的文档
        //		Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
        //		writer.deleteDocuments(query);
        // 删除所有
//        writer.deleteAll();
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }
    @Test
    /**
     * 查询结果高亮显示
     */
    public  void test08()throws Exception{
        // 创建索引目录对象、
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 创建搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        // 查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        Query query = parser.parse("谷歌地图");
        //创建格式化工具
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
        QueryScorer scorer = new QueryScorer(query);
        //创建高亮显示的工具
        Highlighter highlighter = new Highlighter(formatter, scorer);
        // 执行查询，获取前N名的 文档信息
        TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
        int totalHits = topDocs.totalHits;
        System.out.println("本次搜索共" + totalHits + "条数据");
        // 获取ScoreDoc（文档的得分及编号）的数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc: scoreDocs) {
            //获取编号
            int docID = scoreDoc.doc;
            //根据文档找编号
            Document document = reader.document(docID);
            System.out.println("id: " + document.get("id"));
            // 获取原始结果
            String title = document.get("title");
            // 使用高亮工具把原始结果变成高亮结果：三个参数：分词器，要高亮的字段名称，原始结果
            String highTitle = highlighter.getBestFragment(new IKAnalyzer(), "title", title);
            System.out.println("title: " + highTitle);
            // 获取得分
            System.out.println("得分：" + scoreDoc.score);
        }
    }
    @Test
    /**
     * 实现结果排序
     */
    public  void test09()throws Exception{
        // 创建索引目录对象、
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 创建搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        // 查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        Query query = parser.parse("谷歌地图");

        // 创建排序的对象,然后接收排序的字段。参数：字段名称，字段类型，是否反转。false升序，true降序
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG, true));
        // 执行查询，获取前N名的 文档信息
        TopDocs topDocs = searcher.search(query, 10, sort);

        // 获取总条数
        int totalHits = topDocs.totalHits;
        System.out.println("本次搜索共" + totalHits + "条数据");
        // 获取ScoreDoc（文档的得分及编号）的数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取编号
            int docID = scoreDoc.doc;
            // 根据编号找文档
            Document document = reader.document(docID);
            System.out.println("id: " + document.get("id"));
            System.out.println("title: " + document.get("title"));
        }
    }
    @Test
    /**
     * Lucene实现分页查询 Lucene本身不提供分页功能。因此，要实现分页，我们必须自己来完成。也就是逻辑分页
     * 先查询全部，然后返回需要的那一页数据。
     */
    public  void test010()throws Exception{
        // 准备分页参数：
        int pageSize = 5;// 每页条数
        int pageNum = 1;// 当前页
        int start = (pageNum - 1) * pageSize;// 起始角标
        int end = start + pageSize;// 结束角标

        // 创建索引目录对象、
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 创建搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        // 查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        Query query = parser.parse("谷歌");
        // 创建排序的对象,然后接收排序的字段。参数：字段名称，字段类型，是否反转。false升序，true降序
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG, false));
        // 执行查询，获取的是0~end之间的数据
        TopDocs topDocs = searcher.search(query, end, sort);

        // 获取总条数
        int totalHits = topDocs.totalHits;
        // 获取总页数
        int totalPages = (totalHits + pageSize - 1) / pageSize;

        System.out.println("本次搜索共" + totalHits + "条数据,共" + totalPages + "页，当前是第" + pageNum + "页");
        // 获取ScoreDoc（文档的得分及编号）的数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = start; i < end; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 获取编号
            int docID = scoreDoc.doc;
            // 根据编号找文档
            Document document = reader.document(docID);
            System.out.println("id: " + document.get("id"));
            System.out.println("title: " + document.get("title"));
        }

    }
}
