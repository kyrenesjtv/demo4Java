package me.kyrene.demo.test;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: demo4Java
 * @Author: AlbertW
 * @CreateDate: 2019/1/24 16:36
 */
public class DijkNode {

    //当前节点的名字
    public String lable;

    //子节点
    public List<DijkNode> children ;

    //子节点对应的重量
    public Map<String,Double> weights;


    public DijkNode(String lable) {
        this.lable = lable;
    }
}
