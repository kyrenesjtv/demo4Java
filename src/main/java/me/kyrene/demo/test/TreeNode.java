package me.kyrene.demo.test;

import java.util.HashMap;

/**
 * @ProjectName: demo4Java
 * @Author: AlbertW
 * @CreateDate: 2019/1/13 20:47
 */
public class TreeNode {


    public char label;  // 结点的名称，在前缀树里是单个字母
    public HashMap<Character, TreeNode> sons = null; // 使用哈希映射存放子结点。哈希便于确认是否已经添加过某个字母对应的结点。  key是下一个字母
    public String prefix = null;   // 从树的根到当前结点这条通路上，全部字母所组成的前缀。例如通路 b->o->y，对于字母 o 结点而言，前缀是 b；对于字母 y 结点而言，前缀是 bo
    public String explanation = null;  // 词条的解释

    // 初始化结点
    public TreeNode(char l, String pre, String exp) {
        label = l;
        prefix = pre;
        explanation = exp;
        sons = new HashMap<>();

    }

    public TreeNode() {

    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
