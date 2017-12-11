package me.kyrene.demo.Excel.poi;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglin on 2017/12/7.
 */
public class ExcelImport {

    public static void main(String[] args) throws Exception {
        String toPath = "e:\\2017918.xls";
        import1(toPath);
    }

    public static void import1(String toPath) throws IOException {

        if(toPath == null || toPath.trim().isEmpty()){
            throw new IllegalArgumentException("path is wrong");
        }
        File file = new File(toPath);
        InputStream in = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        //获取到title
        List<String> list =  getTitle(workbook);
        //获取data
        List<Map<String,String>> list2 = getData(workbook,list);
        System.out.println("success");
    }

    private static List<Map<String,String>> getData(HSSFWorkbook workbook, List<String> list) {
        List<Map<String,String>> result = new ArrayList<>();
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i = 1; i<rows;i++){
            Map<String, String> map = new HashMap<>();
            Row row = sheet.getRow(i);
            for(int j = 0 ; j < list.size();j++){
                map.put(list.get(j),row.getCell(j).getStringCellValue());
            }
            result.add(map);
        }
        return result;
    }

    private static List<String> getTitle(HSSFWorkbook workbook) {
        List<String> list = new ArrayList<>();
        HSSFSheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        //列数
        int sizes = row.getPhysicalNumberOfCells();
        for (int i = 0; i < sizes; i++) {
            list.add(row.getCell(i).getStringCellValue());
        }
        return list;
    }
}
