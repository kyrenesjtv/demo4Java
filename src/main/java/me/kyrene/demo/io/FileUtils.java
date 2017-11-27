package me.kyrene.demo.io;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin on 2017/11/24.
 */
public class FileUtils {

    /**
     * 单个文件的复制操作
     * @param sourFile 要被复制的文件对象
     * @param destDir   复制后文件存放的目标路径
     */
    public static void fileCopy(File sourFile, File destDir){
        if(sourFile == null || destDir == null) return;

        //在目标路径下创建被复制的文件
        File destFile = null;
        if(destDir.isDirectory()) {
            destFile = new File(destDir, sourFile.getName());
        }else{
            destFile=destDir;
        }
        //使用字节流完成任何文件的复制操作
        //创建字节输入流对象
        BufferedInputStream in = null;
        //创建字节输出流对象
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(sourFile));
            out = new BufferedOutputStream(new FileOutputStream(destFile));

            //使用字节数组开始频繁的读写操作
            byte[] by = new byte[1024];
            int len;
            while((len = in.read(by)) != -1){
                out.write(by, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            //释放资源
            closeFileResource(in, out);
        }
    }

    /**
     * 释放文件操作的资源
     * @param in 字节输入流对象
     * @param out 字节输出流对象
     */
    private static void closeFileResource(InputStream in, OutputStream out){
        try {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用递归算法遍历多级文件夹下的所有文件
     * @param folder 要求遍历的文件夹
     * @param fileList 用来存放文件夹中所有文件的列表集合
     */
    public static void listAllFile_D(File folder, ArrayList<File> fileList){
        if(folder == null || fileList == null) return;

        //遍历某一级文件目录的所有文件目录, 如果是是文件,则添加到列表中, 否则继续递归遍历
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.isFile()){
                fileList.add(file);
            }else{
                listAllFile_D(file,fileList);
            }
        }
    }

    /**
     * 使用文件队列算法遍历多级文件夹下的所有文件
     * @param folder 要求遍历的文件夹
     * @return 返回存放所有文件的列表集合
     */
    public static List<File> listAllFile_Q(File folder){
        if(folder == null) return null;

        //创建文件的列表集合, 用来存放文件, 并作为返回值返回
        ArrayList<File> fileList = new ArrayList<File>();
        //创建文件队列, 用来存放各级文件目录
        MyQueue<File> fileQueue = new MyQueue<File>();
        //首先把第一级文件目录添加到文件队列中
        fileQueue.add(folder);
        //遍历文件队列
        while(!fileQueue.isQueueEmpty()){
            //从文件队列中取出一个文件目录
            File dir = fileQueue.remove();
            //遍历该级文件目录
            File[] files = dir.listFiles();
            for(File file : files){
                if(file.isFile()){
                    //如果是文件, 则添加到文件列表中
                    fileList.add(file);
                }else{
                    //如果是文件夹, 则添加到文件队列中
                    fileQueue.add(file);
                }
            }
        }
        //返回文件列表
        return fileList;
    }

    /**
     * 使用递归算法删除多级文件夹
     * @param folder 要被删除的文件夹
     */
    public static void deleteFolder_D(File folder){
        if(folder == null) return;

        //遍历文件目录
        File[] files = folder.listFiles();

        for(File file : files){
            if(file.isFile()){
                //如果是文件, 直接删除
                file.delete();
            }else{
                //如果是文件夹, 则递归删除
                deleteFolder_D(file);
            }
        }
        folder.delete();
    }

    /**
     * 使用文件队列算法删除多级文件夹
     * @param folder 要被删除的文件夹
     */
    public static void deleteFolder_Q(File folder){
        if(folder == null) return;

        //创建文件队列, 将第一级文件夹添加到文件队列中
        MyQueue<File> fileQueue = new MyQueue<File>();
        fileQueue.add(folder);
        //遍历文件队列
        while(!(fileQueue.isQueueEmpty())){
            //从文件队列中取出一个文件夹
            File dir = fileQueue.remove();
            //遍历文件目录
            File[] files = dir.listFiles();
            //如果该目录为空,直接删除, 否则重新添加到队列中
            if(files.length == 0){
                dir.delete();//目录为空时,直接删除文件夹
            }else{
                fileQueue.add(dir);//不为空时,再次添加到队列中,并继续遍历
                for(File file : files){
                    if(file.isFile()){
                        file.delete();
                    }else{
                        fileQueue.add(file);
                    }
                }
            }
        }
    }

    /**
     * 使用递归算法复制文件夹
     * @param sourFolder 要被复制的文件夹
     * @param destDir   复制后存放的目标路径
     */
    public static void copyFolder_D(File sourFolder, File destDir){
        if(sourFolder == null || destDir == null) return;

        //创建目标路径下的单级文件夹
        File destfolder = null;
        if(destDir.isDirectory()) {
            destfolder = new File(destDir, sourFolder.getName());
            destfolder.mkdir();
        }else{
            destfolder=destDir;
        }

        //遍历某一级文件夹下的每一个文件目录, 如果是文件,则复制,如果是文件夹,则继续调用copyFolder()方法
        File[] files = sourFolder.listFiles();
        for(File file : files){
            if(file.isFile()){
                fileCopy(file, destfolder);
            }else{
                copyFolder_D(file, destfolder);
            }
        }
    }

    /**
     * 使用文件队列算法复制多级文件夹
     * @param sourFolder 要被复制的文件夹
     * @param destDir 复制后存放的目标路径
     */
    public static void copyFolder_Q(File sourFolder, File destDir){
        if(sourFolder == null || destDir == null) return;

        // 创建文件队列
        MyQueue<File> fileQueue = new MyQueue<File>();
        // 将被复制的文件添加到文件队列中
        fileQueue.add(sourFolder);

        // 获取要被复制文件夹的路径中除去文件夹名称之外的字符串
        // 如文件夹路径"E:\MyJavaStudy\JavaFileTest", 处理之后获得的字符串为"E:\MyJavaStudy\"
        int length = sourFolder.getAbsolutePath().length() - sourFolder.getName().length();
        String s = sourFolder.getAbsolutePath().substring(0, length);

        // 遍历文件队列
        while (!fileQueue.isQueueEmpty()) {
            // 从文件队列中拿出一个文件夹
            File dir = fileQueue.remove();

            // 确定目标文件夹的路径
            // 目标文件夹路径的字符串形式为      destPath的文件路径名 + dir的路径全名减去字符串s
            File path = new File(destDir.getAbsolutePath()+"\\".concat(dir.getAbsolutePath().substring(s.length())));
            // 创建目标文件夹路径
            path.mkdirs();

            // 遍历文件夹, 获取所有的文件及文件夹
            File[] files = dir.listFiles();
            for (File file : files) {
                // 如果是文件,直接将文件复制到目标文件夹路径中
                if (file.isFile()) {
                    fileCopy(file, path);// 将文件file复制到路径path下
                } else {
                    // 如果是文件夹,则将该文件夹加入到文件队列中
                    fileQueue.add(file);
                }
            }
        }
    }

    /**
     * 根据url下载图片 downLoadPath要根据这个建文件夹
     * @param imgurl downLoadPath
     */
    public static void downloadPicture(String imgurl,String downLoadPath) {
        URL url = null;
        int imageNumber = 0;
        DataInputStream dataInputStream= null;
        FileOutputStream fileOutputStream =null;
        try {
            url = new URL(imgurl);
            dataInputStream = new DataInputStream(url.openStream());

            //String imageName =  downLoadPath;
            File file = new File(downLoadPath);
            file.mkdirs();
            String downIMG = imgurl.substring(imgurl.lastIndexOf("/") + 1);
            fileOutputStream = new FileOutputStream(new File(downLoadPath+"\\"+downIMG));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeFileResource(dataInputStream,fileOutputStream);
        }
    }
}
