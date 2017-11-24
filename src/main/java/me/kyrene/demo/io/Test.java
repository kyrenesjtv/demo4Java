package me.kyrene.demo.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin on 2017/11/24.
 */
public class Test {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\Dell\\Desktop\\aaa");
        File file1 = new File("C:\\Users\\Dell\\Desktop\\bbb");
       // FileUtils.fileCopy(file,file1);

        //ArrayList<File> list = new ArrayList<>();
       // FileUtils.listAllFile_D(file,list);
        //List<File> files = FileUtils.listAllFile_Q(file);
       // FileUtils.deleteFolder_D(file);
        //FileUtils.deleteFolder_Q(file);
        FileUtils.copyFolder_D(file,file1);
        System.out.println("aaaa");
    }
}
