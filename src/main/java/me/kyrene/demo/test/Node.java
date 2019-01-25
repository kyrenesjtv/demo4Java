package me.kyrene.demo.test;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @ProjectName: demo4Java
 * @Author: AlbertW
 * @CreateDate: 2019/1/15 16:44
 */
public class Node {

    //节点的名称，存放的是用户的ID
    public int user_id;

    //无重复的好友的数量
    public HashSet<Integer> friends = null;

    //好给定的用户节点是几度好友
    public int degree;

    //存放从不同用户出发，当前用户是几度好友
    public HashMap<Integer,Integer> degrees = null;

    //初始化
    public Node(int id) {
        this.user_id = id;
        this.friends = new HashSet<Integer>();
        this.degree = 0;
        this.degrees = new HashMap<>();
    }

    public Node() {

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public HashSet<Integer> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<Integer> friends) {
        this.friends = friends;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public HashMap<Integer, Integer> getDegrees() {
        return degrees;
    }

    public void setDegrees(HashMap<Integer, Integer> degrees) {
        this.degrees = degrees;
    }
}
