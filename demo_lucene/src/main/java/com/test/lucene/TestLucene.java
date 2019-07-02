package com.test.lucene;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * Created by 任春鹏 on 2019-06-24.
 */
public class TestLucene {

    private static FSDirectory fsDirectory = null;
    private static RAMDirectory ramDirectory = null;

    static {
        try {
            fsDirectory = FSDirectory.open(Paths.get("indexDir/"));
            ramDirectory = new RAMDirectory(fsDirectory, new IOContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     * @throws IOException
     */
    @Test
    public void indexWriterTest() throws IOException {
        long start = System.currentTimeMillis();

        Analyzer analyzer = new IKAnalyzer();

        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //创建索引写入对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        //创建索引写入配置
        IndexWriterConfig indexWriterConfig2 = new IndexWriterConfig(analyzer);
        //创建索引写入对象
        IndexWriter indexWriter2 = new IndexWriter(ramDirectory, indexWriterConfig2);

        //创建Document对象，存储索引

        Document doc = new Document();

        int id = 1;

        //将字段加入到doc中
        for(int i=0;i<3000;i++) {
            doc.add(new IntPoint("id", i));
            doc.add(new StringField("title", "Spark" + i, Field.Store.YES));
            doc.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎" + i, Field.Store.YES));
            doc.add(new StoredField("id", id));

            //将doc对象保存到索引库中
            indexWriter.addDocument(doc);
            indexWriter2.addDocument(doc);
        }

        indexWriter.commit();
        indexWriter2.commit();
        //关闭流
        indexWriter.close();
        indexWriter2.close();

        long end = System.currentTimeMillis();
        System.out.println("创建索引花费了" + (end - start) + " 毫秒");
    }

    /**
     * 按词条搜索
     * <p>
     * TermQuery是最简单、也是最常用的Query。TermQuery可以理解成为“词条搜索”，
     * 在搜索引擎中最基本的搜索就是在索引中搜索某一词条，而TermQuery就是用来完成这项工作的。
     * 在Lucene中词条是最基本的搜索单位，从本质上来讲一个词条其实就是一个名/值对。
     * 只不过这个“名”是字段名，而“值”则表示字段中所包含的某个关键字。
     *
     * @throws IOException
     */
    @Test
    public void termQueryTest() throws Exception {

        String searchField1 = "title";
        String searchField2 = "content";
        Query query1 = new TermQuery(new Term(searchField1, "Spark"));
        Query query2 = new TermQuery(new Term(searchField2, "Apache"));
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        // BooleanClause用于表示布尔查询子句关系的类，
        // 包 括：
        // BooleanClause.Occur.MUST，
        // BooleanClause.Occur.MUST_NOT，
        // BooleanClause.Occur.SHOULD。
        // 必须包含,不能包含,可以包含三种.有以下6种组合：
        //
        // 1．MUST和MUST：取得连个查询子句的交集。
        // 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
        // 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
        // 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
        // 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
        // 6．MUST_NOT和MUST_NOT：无意义，检索无结果。

        builder.add(query1, BooleanClause.Occur.SHOULD);
        builder.add(query2, BooleanClause.Occur.SHOULD);


        BooleanQuery query = builder.build();

        //执行查询，并打印查询到的记录数
        executeQuery(query);
    }

    private void executeQuery(BooleanQuery query) throws Exception  {
        //打开索引目录
        IndexReader r = DirectoryReader.open(ramDirectory);
        //创建索引查询对象
        IndexSearcher searcher = new IndexSearcher(r);

        SortField sortField = new SortField("title",SortField.Type.LONG,true);
        Sort sort = new Sort(sortField);

        //search(查询对象,符合条件的前n条记录)
        Long start = System.currentTimeMillis();
        TopDocs search = searcher.search(query, 10000);
        Long end = System.currentTimeMillis();
        System.out.println("lucene搜索结果花费:["+(end-start)+"]ms");
        System.out.println("符合条件的记录有多少个:" + search.totalHits);

        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            System.out.println("*******************************");
            //相关度的排序
            System.out.println("分数:" + scoreDocs[i].score);
            //文档编号
            int docId = scoreDocs[i].doc;
            Document document = searcher.doc(docId);
            System.out.println("文档编号 docId--->" + docId);
            System.out.println("标题标题 title:--->" + document.get("title"));
            System.out.println("标题内容 content:--->" + document.get("content"));
        }
    }

    /**
     * 删除文档
     * @throws IOException
     */
    public void deleteDocumentsTest() throws IOException {
        Analyzer analyzer = new IKAnalyzer();

        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        //创建索引写入对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        // 删除title中含有关键词“Spark”的文档
        long count = indexWriter.deleteDocuments(new Term("title", "Spark"));

        //  除此之外IndexWriter还提供了以下方法：
        // DeleteDocuments(Query query):根据Query条件来删除单个或多个Document
        // DeleteDocuments(Query[] queries):根据Query条件来删除单个或多个Document
        // DeleteDocuments(Term term):根据Term来删除单个或多个Document
        // DeleteDocuments(Term[] terms):根据Term来删除单个或多个Document
        // DeleteAll():删除所有的Document

        //使用IndexWriter进行Document删除操作时，文档并不会立即被删除，而是把这个删除动作缓存起来，当IndexWriter.Commit()或IndexWriter.Close()时，删除操作才会被真正执行。

        indexWriter.commit();
        indexWriter.close();

        System.out.println("删除完成:" + count);
    }
}
