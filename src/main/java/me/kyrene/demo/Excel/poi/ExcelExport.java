package me.kyrene.demo.Excel.poi;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by wanglin on 2017/12/7.
 */
public class ExcelExport {
    public static void main(String[] args) throws Exception {
        String toPath = "e:\\2017918.xls";
        export(toPath);
    }

    private static void export(String toPath) throws Exception {
        File file = new File(toPath);
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet ws = workbook.createSheet("Test Object Identification");
        // 制造数据(title)
        List<String> makeTitle = makeTitle();
        // 制造数据(内容)
        List<Map<String, String>> makeData = makeData();
        // 设置数据(title)
        setTitle(makeTitle, ws);
        // 设置数据(内容)
        setDate(makeData, ws, makeTitle);
        // 写入Exel工作表
        workbook.write(out);


        //释放资源
        out.flush();
        out.close();
        System.out.println("success");
    }

    private static void setDate(List<Map<String, String>> data, HSSFSheet ws, List<String> title) {
        if (title == null || title.size() == 0 || data == null || data.size() == 0) {
            throw new IllegalArgumentException("title( or data)'s list is null ");
        }

        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            Row row = ws.createRow(1 + i);
            //java8 lambda没有提供索引？
            //title.forEach(titleName -> row.createCell(j).setCellValue(map.get(titleName)));
            for (int j = 0; j < title.size(); j++) {
                row.createCell(j).setCellValue(map.get(title.get(j)));
            }
        }
    }

    private static void setTitle(List<String> title, HSSFSheet ws) throws Exception {
        if (title == null || title.size() == 0) {
            throw new IllegalArgumentException("title's list is null ");
        }
        Row row = ws.createRow(0);
        for (int i = 0; i < title.size(); i++) {
            row.createCell(i).setCellValue(title.get(i));
        }
    }

    private static List<String> makeTitle() {
        List<String> list = Arrays.asList("Name", "Sex", "Age", "Addr", "Phone");
        return list;
    }

    private static List<Map<String, String>> makeData() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("Name", "zhangsan" + i);
            if (i % 2 == 0) {
                map.put("Sex", "man");
            } else {
                map.put("Sex", "woman");
            }
            map.put("Age", "" + i);
            map.put("Addr", "Hangzhou" + i);
            map.put("Phone", "" + i);
            list.add(map);
        }

        return list;
    }
}
