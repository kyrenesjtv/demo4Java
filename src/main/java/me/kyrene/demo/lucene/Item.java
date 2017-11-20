package me.kyrene.demo.lucene;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by wanglin on 2017/11/20.
 */
public class Item {

    // 添加注解，告诉SolrJ这些字段需要添加到索引库
    @Field
    private long id;
    @Field
    private String title;
    @Field
    private long price;

    public long getId() {
        return id;
    }
    @Field("id")
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    @Field("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrice() {
        return price;
    }
    @Field("price")
    public void setPrice(long price) {
        this.price = price;
    }
    public Item(){

    }
}
