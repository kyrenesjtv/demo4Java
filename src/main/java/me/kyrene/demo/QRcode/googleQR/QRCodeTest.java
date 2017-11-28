package me.kyrene.demo.QRcode.googleQR;

import java.awt.*;
import java.io.File;

/**
 * Created by wanglin on 2017/11/28.
 */
public class QRCodeTest {
    public static void main(String[] args) {

        String text = QRUtil.generateNumCode(12);  //随机生成的12位验证码
        System.out.println("随机生成的12位验证码为： " + text);
        int width = 100;    //二维码图片的宽
        int height = 100;   //二维码图片的高
        String format = "png";  //二维码图片的格式

        try {
            //生成二维码图片，并返回图片路径
            String pathName = QRUtil.generateQRCode(text, width, height, format);
            System.out.println("生成二维码的图片路径： " + pathName);

            //打开文件
            File file = new File(pathName);
            Desktop.getDesktop().open(file);

            String content = QRUtil.parseQRCode(pathName);
            System.out.println("解析出二维码的图片的内容为： " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
