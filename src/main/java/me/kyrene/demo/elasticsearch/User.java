package me.kyrene.demo.elasticsearch;

/**
 * Created by wanglin on 2018/1/20.
 */
public class User {
    private Long id;
    private String name;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", title='" + title + '\'' + '}';
    }
}
