package me.kyrene.demo.test;

/**
 * @ProjectName: demo4Java
 * @Author: AlbertW
 * @CreateDate: 2019/3/22 10:06
 */
public class User {
    private String name;
    private Integer age;
    private Integer credits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public User(String name, Integer age, Integer credits) {
        this.name = name;
        this.age = age;
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", age=" + age + ", credits=" + credits + '}';
    }
}
