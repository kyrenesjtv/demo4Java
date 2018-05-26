package me.kyrene.demo.Excel.poi;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import java.io.*;

/**
 * @ProjectName: demo4Java
 * @Author: wanglin
 * @CreateDate: 2018/5/15 11:36
 */
public class ExcelExport2 {
    public static void main(String[] args) throws Exception {
        String inPath="C:\\Users\\wanglin\\Desktop\\caiwudaochugeshi.xls";
        String toPath="C:\\Users\\wanglin\\Desktop\\222.xls";
        readExcel(inPath,toPath);
    }

    private static void readExcel(String inPath,String toPath) throws Exception {
        if(toPath == null || toPath.trim().isEmpty()){
            throw new IllegalArgumentException("path is wrong");
        }
        File file = new File(inPath);
        InputStream in = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row = createRow(sheet, 10);
        for(int i = 1;i<10;i++){
            row.createCell(i).setCellValue(i);
        }

        System.out.println("aaaaa");
        File file1 = new File(toPath);
        file1.createNewFile();
        OutputStream out = new FileOutputStream(file1);
        workbook.write(out);
        //释放资源
        in.close();
        out.flush();
        out.close();
        System.out.println("bbbbb");
    }

    /**
     * 找到需要插入的行数，并新建一个POI的row对象
     * @param sheet
     *          当前sheet页的对象
     * @param rowIndex
     *          要插入的当前行数
     * @return
     */
    private static HSSFRow createRow(HSSFSheet sheet, Integer rowIndex) {
        HSSFRow row = null;
        if (sheet.getRow(rowIndex) != null) {
            int lastRowNo = sheet.getLastRowNum();
            sheet.shiftRows(rowIndex, lastRowNo, 1);
        }
        row = sheet.createRow(rowIndex);
        return row;
    }
}
