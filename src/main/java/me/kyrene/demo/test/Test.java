package me.kyrene.demo.test;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ProjectName: demo4Java
 * @Author: wanglin
 * @CreateDate: 2018/5/7 14:43
 */
public class Test {

    @org.junit.Test
    public void test01() throws Exception {

        //创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个uri对象
        URIBuilder uriBuilder = new URIBuilder("http://localhost:8080/smsSend");
        uriBuilder.addParameter("smsCode", "SMS_133978143");
        uriBuilder.addParameter("phone", "15336692852");
        Map<String,String> msgMap1 = new HashMap<>();
        msgMap1.put("\"yongcheren\"","\""+"王林"+"\"");
        msgMap1.put("\"yongcheno\"","\""+"20180202"+"\"");
        uriBuilder.addParameter("msg", msgMap1.toString().replaceAll("=",":"));
        HttpGet get = new HttpGet(uriBuilder.build());
        //执行请求
        CloseableHttpResponse response = httpClient.execute(get);
        //取响应的结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity, "utf-8");
        System.out.println(string);
        //关闭httpclient
        response.close();
        httpClient.close();


    }
    @org.junit.Test
    public void test02()  {
        List<String> list = new ArrayList<>();
        list.add("aa");
        System.out.println(list.toString().replace("[","").replace("]",""));
    }

    @org.junit.Test
    public void test03() throws ParseException {
        List<Map<String,String>> railIDs = new ArrayList<>();
        for(int i = 0 ; i<1 ;i++){
            Map<String,String> map = new HashMap<>();
            map.put("id","188");
            map.put("rail_geom","30.23434,120.14869;30.22975,120.14933;30.23013,120.15461;30.23468,120.15328");
            map.put("rail_is_into","0");
            map.put("rail_is_exit","1");
            map.put("starte_time","01:00");
            map.put("end_time","23:00");
            railIDs.add(map);
        }

        List<Map<String,String>> carIDsByRailIDs = new ArrayList<>();
        for(int i = 0 ; i<2;i++){
            Map<String,String> map = new HashMap<>();
            map.put("id","1");
            map.put("car_id","125"+i);
            map.put("rail_id","188");
            carIDsByRailIDs.add(map);
        }

        List<Map<String,String>> gpSmsgByCarIDs = new ArrayList<>();
        for(int i = 0 ; i<2;i++){
            Map<String,String> map = new HashMap<>();
            map.put("msg_lon","120.14900");
            map.put("msg_car_id","125"+i);
            map.put("msg_lat","30.24001");
            map.put("msg_gps_time","2018-05-10 11:44:55");
            map.put("msg_place","浙江省 宁波市 余姚市 南滨江路 太平洋大酒店内,海纳小超市南68米");
            map.put("msg_state","ACC开 定位  ");
            gpSmsgByCarIDs.add(map);
        }

        Map<String,List<Map<String,String>>> dataMap = new HashMap<>();

        for(int i = 0 ; i<railIDs.size();i++){
            List<Map<String,String>> list1 = new ArrayList<>();
            Map<String, String> stringStringMap = railIDs.get(i);
            String id = stringStringMap.get("id");//Long
            String rail_geom = stringStringMap.get("rail_geom");//String
            String rail_is_into = stringStringMap.get("rail_is_into");//int
            String rail_is_exit = stringStringMap.get("rail_is_exit");//int
            String starte_time = stringStringMap.get("starte_time");//String
            String end_time = stringStringMap.get("end_time");//String
            for(int j = 0 ; j<carIDsByRailIDs.size();j++){
                Map<String, String> stringStringMap1 = carIDsByRailIDs.get(j);
                String rail_id = stringStringMap1.get("rail_id");//integer
                String car_id = stringStringMap1.get("car_id");//integer
                if(id.equals(rail_id)){
                    for(int k = 0 ; k<gpSmsgByCarIDs.size();k++){
                        Map<String, String> stringStringMap2 = gpSmsgByCarIDs.get(k);
                        String msg_car_id = stringStringMap2.get("msg_car_id");//integer
                        String msg_lon = stringStringMap2.get("msg_lon");//String
                        String msg_lat = stringStringMap2.get("msg_lat");//String
                        String msg_gps_time = stringStringMap2.get("msg_gps_time");//date
                        String msg_place = stringStringMap2.get("msg_place");//String
                        String msg_state = stringStringMap2.get("msg_state");//String
                        if(!"ACC开 定位  ".equals(msg_state)){
                            continue;
                        }
                        if(car_id.equals(msg_car_id)){
                            Map<String,String> smallMap = new HashMap<>();
                            smallMap.put("car_id",msg_car_id);
                            smallMap.put("alert_time",msg_gps_time);

                            smallMap.put("alert_state","1");
                            smallMap.put("alert_place",msg_place);
                            smallMap.put("msg_lat",msg_lat);
                            smallMap.put("msg_lon",msg_lon);
                            smallMap.put("alert_type","5");
                            starte_time =starte_time+":00";
                            end_time =end_time+":00";
//                            Date date = new Date();
//                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
//                            String dateString = formatter.format(date);
//                            String starte_timeString = dateString+starte_time;
//                            String end_timeString = dateString+end_time;
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date starte_timeDate = sdf.parse(starte_timeString);
//                            Date end_timeDate = sdf.parse(end_timeString);
//                            Date msg_gps_timeDate = sdf.parse(msg_gps_time);
                            String[] split = msg_gps_time.split(" ");
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                            Date gpsDate = formatter.parse(split[1]);
                            Date starteDate = formatter.parse(starte_time);
                            Date endDate = formatter.parse(end_time);
                            if(starteDate.before(gpsDate) && gpsDate.before(endDate)){
//                                Double latMax = 0.00;
//                                Double latMin = 0.00;
//                                Double lonMax = 0.00;
//                                Double lonMin = 0.00;
                                double msg_lonD = Double.parseDouble(msg_lon);
                                double msg_latD = Double.parseDouble(msg_lat);
//                                Map<String, Double> stringDoubleMap = test06(rail_geom);
//                                latMax=stringDoubleMap.get("latMax");
//                                latMin=stringDoubleMap.get("latMin");
//                                lonMax=stringDoubleMap.get("lonMax");
//                                lonMin=stringDoubleMap.get("lonMin");
                                List<Map<String, Double>> maps = test08(rail_geom);
                                SystemTask systemTaskJob = new SystemTask();
                                List<Point> polygon = new ArrayList<Point>();
                                for(int z=0; z<maps.size();z++){
                                    Map<String, Double> stringDoubleMap = maps.get(z);
                                    Point point1 = new Point(stringDoubleMap.get("lat"),stringDoubleMap.get("lon"));
                                    polygon.add(point1);
                                }
//                                Point point1 = new Point(4,9);
//                                Point point2 = new Point(7,10);
//                                Point point3 = new Point(8,2);
//                                Point point4 = new Point(6,8);

                                Point checkpoint = new Point(msg_latD,msg_lonD);
//                                polygon.add(point1);
//                                polygon.add(point2);
//                                polygon.add(point3);
//                                polygon.add(point4);

                                int m = systemTaskJob.InPolygon(polygon, checkpoint);
                                //待处理
                                if(rail_is_into.equals("1")){
                                    smallMap.put("alert_name","驶入报警");
                                    if(m == 0){

                                    }
//                                    if(msg_lonD>lonMin&&msg_lonD<lonMax&&msg_latD>latMin&&msg_latD<latMax){
//                                        System.out.println("aaa");
//                                    }
                                }
                                if(rail_is_exit.equals("1")){
                                    smallMap.put("alert_name","驶出报警");
                                    if(m == 2){

                                    }

                                }
                            }
                        }
                    }
                }
            }


        }



    }



    @org.junit.Test
    public void test04()  {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        String dateString = formatter.format(date);
        System.out.println(dateString);
    }
    //2018-05-10 11:44:55
    @org.junit.Test
    public void test05() throws ParseException {

        String date = "2018-05-10 11:44:55";
        String[] split = date.split(" ");
        System.out.println(split[0]);
        System.out.println(split[1]);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date parse1 = formatter.parse(split[1]);
        String small = "12:44:55";
        Date parse2 = formatter.parse(small);
        if(parse1.before(parse2)){
            System.out.println("bbbbbbbbbbbbbbbbbbbbb");
        }
        System.out.println("aaa");
    }

    //30.23434,120.14869;30.22975,120.14933;30.23013,120.15461;30.23468,120.15328
    private   Map<String,Double>  test06(String goem1) throws ParseException {
        Map<String,Double> resultMap = new HashMap<>();
        Double latMax = 0.00;
        Double latMin = 0.00;
        Double lonMax = 0.00;
        Double lonMin = 0.00;
        List<String> latList = new ArrayList<>();//30
        List<String> lonList = new ArrayList<>();//120
        String goem = goem1;
        String[] split = goem.split(";");
        for(int i = 0 ; i<split.length;i++){
            String s = split[i];
            String[] split1 = s.split(",");
            latList.add(split1[0]);
            lonList.add(split1[1]);
        }
        latMax= Double.parseDouble(latList.get(0));
        latMin= Double.parseDouble(latList.get(0));
        for(int i = 0 ; i<latList.size();i++){
            double v = Double.parseDouble(latList.get(i));
            if(v>latMax){
                latMax=v;
            }
            if(v<latMin){
                latMin=v;
            }
        }
        lonMax= Double.parseDouble(lonList.get(0));
        lonMin= Double.parseDouble(lonList.get(0));
        for(int i = 0 ; i<lonList.size();i++){
            double v = Double.parseDouble(lonList.get(i));
            if(v>lonMax){
                lonMax=v;
            }
            if(v<lonMin){
                lonMin=v;
            }
        }
        resultMap.put("latMax",latMax);
        resultMap.put("latMin",latMin);
        resultMap.put("lonMax",lonMax);
        resultMap.put("lonMin",lonMin);
        System.out.println("aaa");
        return resultMap;
    }
    @org.junit.Test
    public void test07() throws ParseException {
        Double latMax = 0.00;
        Double latMin = 0.00;
        Double lonMax = 0.00;
        Double lonMin = 0.00;
        List<String> latList = new ArrayList<>();//30
        List<String> lonList = new ArrayList<>();//120
        String goem = "30.23434,120.14869;30.22975,120.14933;30.23013,120.15461;30.23468,120.15328";
        String[] split = goem.split(";");
        for(int i = 0 ; i<split.length;i++){
            String s = split[i];
            String[] split1 = s.split(",");
            latList.add(split1[0]);
            lonList.add(split1[1]);
        }
        latMax= Double.parseDouble(latList.get(0));
        latMin= Double.parseDouble(latList.get(0));
        for(int i = 0 ; i<latList.size();i++){
            double v = Double.parseDouble(latList.get(i));
            if(v>latMax){
                latMax=v;
            }
            if(v<latMin){
                latMin=v;
            }
        }
        lonMax= Double.parseDouble(lonList.get(0));
        lonMin= Double.parseDouble(lonList.get(0));
        for(int i = 0 ; i<lonList.size();i++){
            double v = Double.parseDouble(lonList.get(i));
            if(v>lonMax){
                lonMax=v;
            }
            if(v<lonMin){
                lonMin=v;
            }
        }
        System.out.println("aaa");
    }

    public List<Map<String,Double>> test08( String goem1) throws ParseException {
        List<Map<String,Double>> list = new ArrayList<>();
        String goem = goem1;
        String[] split = goem.split(";");
        for(int i = 0 ; i<split.length;i++){
            Map<String,Double> map = new HashMap<>();
            String[] split1 = split[i].split(",");
            map.put("lat",Double.parseDouble(split1[0]));
            map.put("lon",Double.parseDouble(split1[1]));
            list.add(map);
        }

        System.out.println("aaa");
        return list;
    }

    @org.junit.Test
    public void test09(){
        Map<String,Object> map1 = new HashMap<>();
        map1.put("aa","aa");
        map1.put("bb",22);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("aa","aa");
        map2.put("bb",22);

        boolean equals = map1.equals(map2);
        System.out.println(equals);

    }

    @org.junit.Test
    public void test10(){
        String inPath="C:\\Users\\wanglin\\Desktop\\caiwudaochugeshi.xls";
        String toPath="C:\\Users\\wanglin\\Desktop\\222.xls";
        try {
            readExcel(inPath,toPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        HSSFRow row = createRow(sheet, 9);
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
     * @param rowIndex
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

    @org.junit.Test
    public void test11(){
        long long1 =123456789;
        long long2 =987654321;
        long abs = Math.abs(long1 - long2);
        System.out.println(abs);
    }

    @org.junit.Test
    public void test12(){
        long hours = 25;
        long day = hours /24;
        long hour = hours %24;
        System.out.println(hours %24);
        System.out.println("day:    "+day +"hour:   "+hour);
    }
}
