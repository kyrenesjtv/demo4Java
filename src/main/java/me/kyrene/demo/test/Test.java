package me.kyrene.demo.test;

import org.apache.commons.lang3.StringUtils;
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

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;


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
        Map<String, String> msgMap1 = new HashMap<>();
        msgMap1.put("\"yongcheren\"", "\"" + "王林" + "\"");
        msgMap1.put("\"yongcheno\"", "\"" + "20180202" + "\"");
        uriBuilder.addParameter("msg", msgMap1.toString().replaceAll("=", ":"));
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
    public void test02() {
        List<String> list = new ArrayList<>();
        list.add("aa");
        System.out.println(list.toString().replace("[", "").replace("]", ""));
    }

    @org.junit.Test
    public void test03() throws ParseException {
        List<Map<String, String>> railIDs = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", "188");
            map.put("rail_geom", "30.23434,120.14869;30.22975,120.14933;30.23013,120.15461;30.23468,120.15328");
            map.put("rail_is_into", "0");
            map.put("rail_is_exit", "1");
            map.put("starte_time", "01:00");
            map.put("end_time", "23:00");
            railIDs.add(map);
        }

        List<Map<String, String>> carIDsByRailIDs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", "1");
            map.put("car_id", "125" + i);
            map.put("rail_id", "188");
            carIDsByRailIDs.add(map);
        }

        List<Map<String, String>> gpSmsgByCarIDs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("msg_lon", "120.14900");
            map.put("msg_car_id", "125" + i);
            map.put("msg_lat", "30.24001");
            map.put("msg_gps_time", "2018-05-10 11:44:55");
            map.put("msg_place", "浙江省 宁波市 余姚市 南滨江路 太平洋大酒店内,海纳小超市南68米");
            map.put("msg_state", "ACC开 定位  ");
            gpSmsgByCarIDs.add(map);
        }

        Map<String, List<Map<String, String>>> dataMap = new HashMap<>();

        for (int i = 0; i < railIDs.size(); i++) {
            List<Map<String, String>> list1 = new ArrayList<>();
            Map<String, String> stringStringMap = railIDs.get(i);
            String id = stringStringMap.get("id");//Long
            String rail_geom = stringStringMap.get("rail_geom");//String
            String rail_is_into = stringStringMap.get("rail_is_into");//int
            String rail_is_exit = stringStringMap.get("rail_is_exit");//int
            String starte_time = stringStringMap.get("starte_time");//String
            String end_time = stringStringMap.get("end_time");//String
            for (int j = 0; j < carIDsByRailIDs.size(); j++) {
                Map<String, String> stringStringMap1 = carIDsByRailIDs.get(j);
                String rail_id = stringStringMap1.get("rail_id");//integer
                String car_id = stringStringMap1.get("car_id");//integer
                if (id.equals(rail_id)) {
                    for (int k = 0; k < gpSmsgByCarIDs.size(); k++) {
                        Map<String, String> stringStringMap2 = gpSmsgByCarIDs.get(k);
                        String msg_car_id = stringStringMap2.get("msg_car_id");//integer
                        String msg_lon = stringStringMap2.get("msg_lon");//String
                        String msg_lat = stringStringMap2.get("msg_lat");//String
                        String msg_gps_time = stringStringMap2.get("msg_gps_time");//date
                        String msg_place = stringStringMap2.get("msg_place");//String
                        String msg_state = stringStringMap2.get("msg_state");//String
                        if (!"ACC开 定位  ".equals(msg_state)) {
                            continue;
                        }
                        if (car_id.equals(msg_car_id)) {
                            Map<String, String> smallMap = new HashMap<>();
                            smallMap.put("car_id", msg_car_id);
                            smallMap.put("alert_time", msg_gps_time);

                            smallMap.put("alert_state", "1");
                            smallMap.put("alert_place", msg_place);
                            smallMap.put("msg_lat", msg_lat);
                            smallMap.put("msg_lon", msg_lon);
                            smallMap.put("alert_type", "5");
                            starte_time = starte_time + ":00";
                            end_time = end_time + ":00";
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
                            if (starteDate.before(gpsDate) && gpsDate.before(endDate)) {
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
                                for (int z = 0; z < maps.size(); z++) {
                                    Map<String, Double> stringDoubleMap = maps.get(z);
                                    Point point1 = new Point(stringDoubleMap.get("lat"), stringDoubleMap.get("lon"));
                                    polygon.add(point1);
                                }
                                //                                Point point1 = new Point(4,9);
                                //                                Point point2 = new Point(7,10);
                                //                                Point point3 = new Point(8,2);
                                //                                Point point4 = new Point(6,8);

                                Point checkpoint = new Point(msg_latD, msg_lonD);
                                //                                polygon.add(point1);
                                //                                polygon.add(point2);
                                //                                polygon.add(point3);
                                //                                polygon.add(point4);

                                //                                int m = systemTaskJob.InPolygon(polygon, checkpoint);
                                int m = 0;
                                //待处理
                                if (rail_is_into.equals("1")) {
                                    smallMap.put("alert_name", "驶入报警");
                                    if (m == 0) {

                                    }
                                    //                                    if(msg_lonD>lonMin&&msg_lonD<lonMax&&msg_latD>latMin&&msg_latD<latMax){
                                    //                                        System.out.println("aaa");
                                    //                                    }
                                }
                                if (rail_is_exit.equals("1")) {
                                    smallMap.put("alert_name", "驶出报警");
                                    if (m == 2) {

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
    public void test04() {
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
        if (parse1.before(parse2)) {
            System.out.println("bbbbbbbbbbbbbbbbbbbbb");
        }
        System.out.println("aaa");
    }

    //30.23434,120.14869;30.22975,120.14933;30.23013,120.15461;30.23468,120.15328
    private Map<String, Double> test06(String goem1) throws ParseException {
        Map<String, Double> resultMap = new HashMap<>();
        Double latMax = 0.00;
        Double latMin = 0.00;
        Double lonMax = 0.00;
        Double lonMin = 0.00;
        List<String> latList = new ArrayList<>();//30
        List<String> lonList = new ArrayList<>();//120
        String goem = goem1;
        String[] split = goem.split(";");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            String[] split1 = s.split(",");
            latList.add(split1[0]);
            lonList.add(split1[1]);
        }
        latMax = Double.parseDouble(latList.get(0));
        latMin = Double.parseDouble(latList.get(0));
        for (int i = 0; i < latList.size(); i++) {
            double v = Double.parseDouble(latList.get(i));
            if (v > latMax) {
                latMax = v;
            }
            if (v < latMin) {
                latMin = v;
            }
        }
        lonMax = Double.parseDouble(lonList.get(0));
        lonMin = Double.parseDouble(lonList.get(0));
        for (int i = 0; i < lonList.size(); i++) {
            double v = Double.parseDouble(lonList.get(i));
            if (v > lonMax) {
                lonMax = v;
            }
            if (v < lonMin) {
                lonMin = v;
            }
        }
        resultMap.put("latMax", latMax);
        resultMap.put("latMin", latMin);
        resultMap.put("lonMax", lonMax);
        resultMap.put("lonMin", lonMin);
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
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            String[] split1 = s.split(",");
            latList.add(split1[0]);
            lonList.add(split1[1]);
        }
        latMax = Double.parseDouble(latList.get(0));
        latMin = Double.parseDouble(latList.get(0));
        for (int i = 0; i < latList.size(); i++) {
            double v = Double.parseDouble(latList.get(i));
            if (v > latMax) {
                latMax = v;
            }
            if (v < latMin) {
                latMin = v;
            }
        }
        lonMax = Double.parseDouble(lonList.get(0));
        lonMin = Double.parseDouble(lonList.get(0));
        for (int i = 0; i < lonList.size(); i++) {
            double v = Double.parseDouble(lonList.get(i));
            if (v > lonMax) {
                lonMax = v;
            }
            if (v < lonMin) {
                lonMin = v;
            }
        }
        System.out.println("aaa");
    }

    public List<Map<String, Double>> test08(String goem1) throws ParseException {
        List<Map<String, Double>> list = new ArrayList<>();
        String goem = goem1;
        String[] split = goem.split(";");
        for (int i = 0; i < split.length; i++) {
            Map<String, Double> map = new HashMap<>();
            String[] split1 = split[i].split(",");
            map.put("lat", Double.parseDouble(split1[0]));
            map.put("lon", Double.parseDouble(split1[1]));
            list.add(map);
        }

        System.out.println("aaa");
        return list;
    }

    @org.junit.Test
    public void test09() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("aa", "aa");
        map1.put("bb", 22);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("aa", "aa");
        map2.put("bb", 22);

        boolean equals = map1.equals(map2);
        System.out.println(equals);

    }

    @org.junit.Test
    public void test10() {
        String inPath = "C:\\Users\\wanglin\\Desktop\\caiwudaochugeshi.xls";
        String toPath = "C:\\Users\\wanglin\\Desktop\\222.xls";
        try {
            readExcel(inPath, toPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readExcel(String inPath, String toPath) throws Exception {
        if (toPath == null || toPath.trim().isEmpty()) {
            throw new IllegalArgumentException("path is wrong");
        }
        File file = new File(inPath);
        InputStream in = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row = createRow(sheet, 9);
        for (int i = 1; i < 10; i++) {
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
     *
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
    public void test11() {
        long long1 = 123456789;
        long long2 = 987654321;
        long abs = Math.abs(long1 - long2);
        System.out.println(abs);
    }

    @org.junit.Test
    public void test12() {
        long hours = 25;
        long day = hours / 24;
        long hour = hours % 24;
        System.out.println(hours % 24);
        System.out.println("day:    " + day + "hour:   " + hour);
    }

    @org.junit.Test
    public void test13() {


    }

    public boolean isInside(Point e, Point... points) {//e点是汽车的点，后面是围栏的点
        Double sum = 0d;//汽车与区域的面积
        for (int i = 0, len = points.length; i < len; i++) {
            Point point1 = points[i];
            Point point2 = points[i == len - 1 ? 0 : i + 1];
            sum += area(point1, point2, e);
        }
        //区域的面积
        Double abcd = electronicFence(points);
        return sum <= abcd;
    }

    private Double electronicFence(Point... points) {
        Double sum = 0d;
        Point point = points[0];
        for (int i = 0; i < points.length - 2; i++) {
            Point point1 = points[i + 1];
            Point point2 = points[i + 2];
            sum += area(point1, point2, point);
        }
        return sum;
    }

    private double area(Point pa, Point pb, Point pc) {

        double a, b, c;

        a = lineSpace(pa.getX(), pa.getY(), pb.getX(), pb.getY());// 线段的长度

        b = lineSpace(pa.getX(), pa.getY(), pc.getX(), pc.getY());// (x1,y1)到点的距离

        c = lineSpace(pb.getX(), pb.getY(), pc.getX(), pc.getY());// (x2,y2)到点的距离

        //组成锐角三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

        return s;

    }


    // 计算两点之间的距离
    private double lineSpace(double x1, double y1, double x2, double y2) {

        double lineLength = 0;

        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)

                * (y1 - y2));

        return lineLength;


    }

    private static class Point {
        private Double x;
        private Double y;

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }


    @org.junit.Test
    public void test14() {
        int orderType = 11;


        Map<Integer, Integer> orderMap = new HashMap<>();
        orderMap.put(1, 11);
        orderMap.put(11, 1);
        orderMap.put(2, 22);
        orderMap.put(22, 2);
        orderMap.put(3, 33);
        orderMap.put(33, 3);

        orderType = orderMap.get(orderType);
        System.out.println(orderType);

    }

    @org.junit.Test
    public void test15() {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("userType", "");
        Integer userType = Integer.parseInt(objectObjectHashMap.get("userType").toString());
        System.out.println("aaa");
    }

    @org.junit.Test
    public void test16() {
        String aa = "[\"{\"中型客车\":\"20\"}\"]";
        System.out.println(aa);
        System.out.println(aa.substring(2, aa.length() - 2));
        String bb = "[" + aa.substring(2, aa.length() - 2) + "]";
        System.out.println(bb);

    }

    @org.junit.Test
    public void test17() {
        String aa = "[{轿车:1}]";
        System.out.println(aa);
        System.out.println(aa.replaceAll("}]", "").replaceAll("\\[\\{", ""));
        System.out.println(aa.replaceAll("\\[\\{", ""));
    }

    @org.junit.Test
    public void test18() throws IOException, URISyntaxException {
        String accessToken = DingTalkUtil.getAccessToken();
        System.out.println(accessToken);
    }

    @org.junit.Test
    public void test19() throws IOException, URISyntaxException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        System.out.println(list.toString());


    }

    @org.junit.Test
    public void test20() throws IOException, URISyntaxException {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String currSun = dateFm.format(date);
        System.out.println(currSun);

    }

    @org.junit.Test
    public void test21() throws IOException, URISyntaxException {
        String dataString = "15355988634$浙C91021$0$277$$$27.839413$121.152733$2018-11-2 15:42:18$$ACC关未定位$0$0$;";
        String[] dataArray = dataString.split("\\;");
        String rowDataString = "";
        for (int kp = 0; kp < dataArray.length; kp++) {
            String[] rowsDataArray = {};
            rowDataString = String.valueOf(dataArray[kp]);
            rowsDataArray = rowDataString.split("\\$");
            int rowLength = rowsDataArray.length;
            System.out.println(rowLength);
        }

        String rowDataString1 = "017758018591$浙A00003(黄)$120$316$东北$浙江省人民大会堂东南16号$30.266323$120.151306$2017-09-06 12:09:48$2740.5$ACC开定位$844.8$1201.4$";
        String[] rowsDataArray1 = {};
        rowsDataArray1 = rowDataString1.split("\\$");
        int rowLength1 = rowsDataArray1.length;
        System.out.println(rowLength1);
    }


    @org.junit.Test
    public void test22() throws IOException, URISyntaxException {
        String timeString = "2017-09-06 12:09:48";
        String tabaleName = getTabaleName(timeString);
        System.out.println(tabaleName);
    }

    private String getTabaleName(String GPSTime) {
        String start_day = GPSTime.substring(0, GPSTime.indexOf(" "));
        String s_table = "mon_" + start_day.replace("-", "_");
        String db = s_table.substring(0, s_table.lastIndexOf("_"));
        String DBandTable = db + "." + s_table;
        return DBandTable;
    }


    @org.junit.Test
    public void test23() throws IOException, URISyntaxException {
        String timeString = "renderReverse&&renderReverse({\"status\":0,\"result\":{\"location\":{\"lng\":120.15130599999994,\"lat\":30.266323052796858},\"formatted_address\":\"浙江省杭州市西湖区\",\"business\":\"西湖,黄龙,北山\",\"addressComponent\":{\"country\":\"中国\",\"country_code\":0,\"country_code_iso\":\"CHN\",\"country_code_iso2\":\"CN\",\"province\":\"浙江省\",\"city\":\"杭州市\",\"city_level\":2,\"district\":\"西湖区\",\"town\":\"\",\"adcode\":\"330106\",\"street\":\"\",\"street_number\":\"\",\"direction\":\"\",\"distance\":\"\"},\"pois\":[],\"roads\":[],\"poiRegions\":[],\"sematic_description\":\"葛岭北293米\",\"cityCode\":179}})";
        String s = timeString.replaceAll("renderReverse&&renderReverse\\(\\{", "").replaceAll("}\\)", "");
        System.out.println(timeString);
        System.out.println(s);
    }

    @org.junit.Test
    public void test24() throws IOException, URISyntaxException {
        Desktop.getDesktop().open(new File("http://120.26.85.17/sscmpapp/test.doc"));

    }

    @org.junit.Test
    public void test25() throws IOException, URISyntaxException {
        String param = "checkUsername=&userName=admin";
        StringBuffer stringBuffer = new StringBuffer("param:|");
        String s = param.replaceAll("&", "|").replaceAll("=", ":");
        System.out.println(stringBuffer.append(s).toString());
    }

    @org.junit.Test
    public void test26() throws IOException, URISyntaxException {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("insertRentLog", "");
        String insertRentLog = stringObjectHashMap.get("insertRentLog").toString();
        System.out.println("aaa");
    }

    @org.junit.Test
    public void test27() throws IOException, URISyntaxException {
        int i = 1;
        String ipRoute = "";
        if (i == 1) {
            ipRoute = ipRoute + "aaa";
        } else {
            ipRoute = ipRoute + "bbb";
        }
        System.out.println(ipRoute);
    }

    @org.junit.Test
    public void test28() throws IOException, URISyntaxException {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        Map<String, Object> map4 = new HashMap<>();
        map1.put("userPhone", "15336692852");
        map1.put("22", "1");
        map2.put("userPhone", "");
        map2.put("22", "2");
        map3.put("userPhone", "-");
        map3.put("22", "3");
        map4.put("userPhone", "00000000000");
        map4.put("22", "4");
        List<Map<String, Object>> listStringObjectMap = new ArrayList<>();
        listStringObjectMap.add(map1);
        listStringObjectMap.add(map2);
        listStringObjectMap.add(map3);
        listStringObjectMap.add(map4);
        List<Map<String, Object>> newList = listStringObjectMap.stream().filter((d) -> {
            boolean userPhone = isMobile(d.get("userPhone").toString());
            if (userPhone) {
                d.put("cityCode", "WZ");
            }
            return userPhone;
        }).collect(toList());
        System.out.println(listStringObjectMap);
        System.out.println(newList);

    }

    @org.junit.Test
    public void test29() throws IOException, URISyntaxException {
        boolean mobile = isMobile("-");
    }

    @org.junit.Test
    public void test30() throws IOException, URISyntaxException {
        List<Map<String, Object>> ipList = new ArrayList<>();
        Map<String, Object> ipListMap1 = new HashMap<>();
        Map<String, Object> ipListMap2 = new HashMap<>();
        Map<String, Object> ipListMap3 = new HashMap<>();
        ipListMap1.put("userPhone", "4444");
        ipListMap1.put("id", 1);
        ipListMap2.put("userPhone", "2222");
        ipListMap2.put("id", 2);
        ipListMap3.put("userPhone", "3333");
        ipListMap3.put("id", 3);
        ipList.add(ipListMap1);
        ipList.add(ipListMap2);
        ipList.add(ipListMap3);


        List<Map<String, Object>> ipList1 = new ArrayList<>();
        Map<String, Object> ipList1Map1 = new HashMap<>();
        //        Map<String,Object> ipList1Map2 = new HashMap<>();
        Map<String, Object> ipList1Map3 = new HashMap<>();
        ipList1Map1.put("userPhone", "1111");
        ipList1Map1.put("id", 1);
        //        ipList1Map2.put("userPhone","2222");
        ipList1.add(ipList1Map1);
        //        ipList1.add(ipList1Map2);

        ipList.removeAll(ipList1);
        System.out.println(ipList);
    }

    @org.junit.Test
    public void test31() throws IOException, URISyntaxException {
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy");
        String currSun = dateFm.format(new Date());
        System.out.println(currSun);
    }

    public static Boolean test32(int target, Result result) {
        if (target == 1) {
            if ((Math.pow((double) 2, (double) 1) - 1) == 1) {
                result.setNum(1);
                result.setNumTotal(1);
                return true;
            } else {
                return false;
            }
        } else {
            Boolean preBoolean = test32(target - 1, result);
            result.setNum(result.getNum() * 2);
            result.setNumTotal(result.getNum() + result.getNumTotal());
            Boolean currBoolean = false;
            if ((Math.pow((double) 2, (double) target) - 1) == result.getNumTotal()) {
                currBoolean = true;
            }
            if (currBoolean && preBoolean) {
                return true;
            } else {
                return false;
            }
        }
    }

    class Result {
        public long num = 0;
        public long numTotal = 0;

        public long getNum() {
            return num;
        }

        public void setNum(long num) {
            this.num = num;
        }

        public long getNumTotal() {
            return numTotal;
        }

        public void setNumTotal(long numTotal) {
            this.numTotal = numTotal;
        }
    }

    @org.junit.Test
    public void test33() throws IOException, URISyntaxException {
        Result result = new Result();
        Boolean aBoolean = test32(3, result);
        System.out.println(aBoolean);
        System.out.println(result.getNumTotal());
    }


    public static long[] params = {1, 2, 5, 10};

    public static void get(long target, ArrayList<Long> result) {

        if (target == 0) {
            System.out.println(result);
            return;
        } else {
            if (target < 0) {
                return;
            } else {
                for (int i = 0; i < params.length; i++) {
                    ArrayList<Long> currentResult = (ArrayList<Long>) result.clone();
                    currentResult.add(params[i]);
                    get((target - params[i]), currentResult);
                }
            }
        }
    }

    @org.junit.Test
    public void test34() throws IOException, URISyntaxException {
        get(10L, new ArrayList<Long>());
    }

    /**
     * @param target 需要分解的值
     * @param result 结果结合
     * @Description 使用递归调用，找出数值有多少种因子组成
     */
    public static void recursion(Long target, ArrayList<Long> result) {
        if (target == 1) {
            //确保target = target的时候 加入1
            if (!result.contains(1L)) {
                result.add(1L);
            }
            System.out.println(result);
            return;
        } else {
            for (long i = 1; i <= target; i++) {
                if ((i == 1) && result.contains(1L)) {
                    continue;
                }
                ArrayList<Long> currentResult = (ArrayList<Long>) result.clone();
                currentResult.add(Long.valueOf(i));
                //判断当前 i 是否是target的因子
                if (target % i != 0) {
                    continue;
                }
                recursion(target / i, currentResult);
            }
        }
    }

    //    public static void recursion(long total, ArrayList<Long> result) {
    //
    //        if (total == 1) {
    //            if (!result.contains(1L)){
    //                result.add(1L);
    //            }
    //            System.out.println(result);
    //            return;
    //        } else {
    //            for (long i = 1; i <= total; i++) {
    //                if ((i == 1) && result.contains(1L)) {
    //                    continue;
    //                }
    //                ArrayList<Long> newList = (ArrayList<Long>) (result.clone());
    //                newList.add(Long.valueOf(i));
    //                if (total % i != 0) {
    //                    continue;
    //                }
    //                recursion(total / i, newList);
    //            }
    //        }
    //    }


    @org.junit.Test
    public void test35() throws IOException, URISyntaxException {
        recursion(8L, new ArrayList<Long>());
    }

    public static int[] mergeSort(int[] a, int[] b) {
        if (a == null) {
            a = new int[0];
        }
        if (b == null) {
            b = new int[0];
        }

        int[] mergeSortResult = new int[a.length + b.length];
        int a1 = 0, b1 = 0, m1 = 0;

        while ((a1 < a.length) && (b1 < b.length)) {
            if (a[a1] < b[b1]) {
                mergeSortResult[m1] = a[a1];
                a1++;
            } else {
                mergeSortResult[m1] = b[b1];
                b1++;
            }
            m1++;
        }

        if (a1 < a.length) {
            for (int i = a1; i < a.length; i++) {
                mergeSortResult[m1] = a[i];
                m1++;
            }
        } else {
            for (int i = b1; i < b.length; i++) {
                mergeSortResult[m1] = b[i];
                m1++;
            }
        }
        return mergeSortResult;
    }


    public static int[] merge_sort(int[] to_sort) {

        if (to_sort == null) return new int[0];

        // 如果分解到只剩一个数，返回该数
        if (to_sort.length == 1) return to_sort;

        // 将数组分解成左右两半
        int mid = to_sort.length / 2;
        int[] left = Arrays.copyOfRange(to_sort, 0, mid);
        int[] right = Arrays.copyOfRange(to_sort, mid, to_sort.length);

        // 嵌套调用，对两半分别进行排序
        left = merge_sort(left);
        right = merge_sort(right);

        // 合并排序后的两半
        int[] merged = mergeSort(left, right);

        return merged;

    }


    @org.junit.Test
    public void test36() throws IOException, URISyntaxException {
        int[] a = {1, 3, 5, 7, 9};
        int[] b = {2, 4, 6, 8};
        int[] ints = mergeSort(a, b);
        Arrays.stream(ints).forEach(System.out::println);
    }


    @org.junit.Test
    public void test37() throws IOException, URISyntaxException {
        for (int i = 1; i < 10; i++) {
            try {
                System.out.println("current row ::" + i);
                if (i / 2 == 1) {
                    int result = 2 / (i - 2);
                    System.out.println("----------");
                }
            } catch (Exception e) {
                System.out.println("error  row  :" + i);
            }
        }
    }


    //设置齐王的马跑完所需要的啥时间
    public static HashMap<String, Double> q_horse_time = new HashMap<String, Double>() {
        {
            put("q1", 1.0);
            put("q2", 2.0);
            put("q3", 3.0);
        }
    };

    //设置田忌的马跑完所需要的啥时间
    public static HashMap<String, Double> t_horse_time = new HashMap<String, Double>() {
        {
            put("t1", 1.5);
            put("t2", 2.5);
            put("t3", 3.5);
        }
    };

    //设置齐王的马匹
    public static ArrayList<String> q_horse = new ArrayList<String>(Arrays.asList("q1", "q2", "q3"));


    public static void permutate(ArrayList<String> targetHourse, ArrayList<String> resultList) {

        if (targetHourse.size() == 0) {
            System.out.println(resultList);
            compareHorse(q_horse, resultList);
            System.out.println();
            return;
        }

        for (int i = 0; i < targetHourse.size(); i++) {
            //将选择的马添加进去
            ArrayList<String> newList = (ArrayList<String>) resultList.clone();
            newList.add(targetHourse.get(i));

            //将选好的马删除
            ArrayList<String> oldHourse = (ArrayList<String>) targetHourse.clone();
            oldHourse.remove(i);
            permutate(oldHourse, newList);
        }
    }


    public static void compareHorse(ArrayList<String> q_horse, ArrayList<String> t_horse) {

        int t_won_cnt = 0;

        for (int i = 0; i < t_horse.size(); i++) {
            System.out.println(t_horse_time.get(t_horse.get(i)) + ":" + q_horse_time.get(q_horse.get(i)));
            if (t_horse_time.get(t_horse.get(i)) < q_horse_time.get(q_horse.get(i))) t_won_cnt++;
        }
        if (t_won_cnt > (q_horse.size() / 2)) {
            System.out.println("田忌获胜");
        } else {
            System.out.println("齐王获胜");
        }
    }

    @org.junit.Test
    public void test38() throws IOException, URISyntaxException {
        ArrayList<String> q_horse = new ArrayList<String>(Arrays.asList("t1", "t2", "t3"));
        permutate(q_horse, new ArrayList<String>());
    }


    /**
     * 暴力破解密码
     *
     * @param pwdLength  密码长度
     * @param pwdString  密码组成的字符
     * @param resultList 最终密码
     */
    public static void BruteforcePassword(int pwdLength, ArrayList<String> pwdString, ArrayList<String> resultList) {

        if (resultList.size() == pwdLength) {
            System.out.println(resultList);
            return;
        }

        for (int i = 0; i < pwdString.size(); i++) {
            ArrayList<String> newResult = (ArrayList<String>) resultList.clone();
            newResult.add(pwdString.get(i));
            BruteforcePassword(pwdLength, pwdString, newResult);
        }

    }

    @org.junit.Test
    public void test39() throws IOException, URISyntaxException {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        BruteforcePassword(4, strings, new ArrayList<String>());
    }

    /**
     * 在n个元素里面取m个排列，并且要求元素不重复
     *
     * @param source     n个元素
     * @param resultList 结果集合
     * @param num        取m个
     */
    public static void combination(ArrayList<String> source, ArrayList<String> resultList, int num) {

        if (resultList.size() == num) {
            System.out.println(resultList);
            return;
        }

        for (int i = 0; i < source.size(); i++) {
            //从剩下的队伍中，取出一个队伍放入已经参战的队伍中
            ArrayList<String> newResult = (ArrayList<String>) resultList.clone();
            newResult.add(source.get(i));

            //将出过场的队伍剔除出去
            ArrayList<String> lastsource = new ArrayList<>(source.subList(i + 1, source.size()));//包头不包尾
            combination(lastsource, newResult, num);
        }
    }

    @org.junit.Test
    public void test40() throws IOException, URISyntaxException {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("10", "9", "8", "7", "6", "5", "4", "3", "2", "1"));
        //        combination(strings,new ArrayList<String>(),1);

        int i = combinationTotal(10, 6);//10 , 36 , 35  12600
        System.out.println(i);
    }


    /**
     * 求组合总数
     *
     * @param source n个元素
     * @param num    取m个
     */
    public static int combinationTotal(int source, int num) {
        if (num > source) {
            return 0;
        }
        int index = source - num;
        int result = 1;
        while (source > index) {
            result *= source--;
        }
        //剔除重复排列的
        while (num > 1) {
            result /= num--;
        }
        return result;
    }

    //    private int[][] winPeople = new int[10][1,2,3];

    /**
     * 求中奖人数有多少个组合
     *
     * @param source n个元素
     * @param nums   分别奖项对应的人数 new ArrayList<Integer>(Arrays.asList(1, 2, 3))
     */
    public static long winCombinationTotal(int source, ArrayList<Integer> nums) {

        if (nums.size() == 0) {
            return 0;
        }

        long result = 1;

        for (int i = 0; i < nums.size(); i++) {
            int num = nums.get(i);
            if (num > source) {
                return 0;
            }
            int index = source - num;
            int newResult = 1;
            while (source > index) {
                result *= source--;
            }
            //剔除重复排列的
            while (num > 1) {
                result /= num--;
            }
            result *= newResult;
        }
        return result;
    }

    /**
     * @param teams- 目前还剩多少队伍没有参与组合，result- 保存当前已经组合的队伍
     * @return void
     * @Description: 使用函数的递归（嵌套）调用，找出所有可能的队伍组合
     */

    public static void combine(ArrayList<String> teams, ArrayList<String> result, ArrayList<Integer> nums, int winMan) {

//        if (winMan == 1) {
//            System.out.println(result);
//            return;
//        } else if () {
//
//        }

        //循环中奖人数
        for (int i = 0; i < teams.size(); i++) {
            //循环奖项
            for (int j = 0; j < nums.size(); j++) {

                //添加中奖人员
                ArrayList<String> newResult = (ArrayList<String>) result.clone();
                newResult.add(teams.get(i));

                //剔除中奖人员
                ArrayList<String> newTeams = new ArrayList<String>(teams.subList(i + 1, teams.size()));

                //获得当前奖项的种类
                int newWinMan = nums.get(j);

                //剔除奖项的种类
                ArrayList<Integer> newNums = new ArrayList<Integer>(nums.subList(j + 1, nums.size()));

                combine(newTeams, newResult, newNums, newWinMan);
            }

        }
    }

    @org.junit.Test
    public void test41() throws IOException, URISyntaxException {
        //        long result = winCombinationTotal(10, new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        //        System.out.println(result);

    }

    /**
     *
     * @param resultF  一等奖的集合
     * @param resultS  二等奖的集合
     * @param resultT  三等奖的集合
     * @param remain
     * @param remainList
     * @param f 一等奖人数
     * @param s 二等奖人数
     * @param t 三等奖人数
     */
    public static void lottery(ArrayList<Integer> resultF, ArrayList<Integer> resultS, ArrayList<Integer> resultT, ArrayList<Integer> remain, ArrayList<Integer> remainList, int f, int s, int t) {
        if (resultT.size() == t) {
            remain = (ArrayList<Integer>) remainList.clone();
            remain.removeAll(resultT);
            if (resultS.size() == s) {
                remain = (ArrayList<Integer>) remainList.clone();
                remain.removeAll(resultT);
                remain.removeAll(resultS);
                if (resultF.size() == f) {
                    System.out.print("三等奖:" + resultT + ";");
                    System.out.print("二等奖:" + resultS + ";");
                    System.out.println("一等奖:" + resultF);
                    return;
                } else {
                    for (int i = 0; i < remain.size(); i++) {
                        ArrayList<Integer> newResult = (ArrayList<Integer>) resultF.clone();
                        newResult.add(remain.get(i));
                        ArrayList<Integer> newRemain = new ArrayList<Integer>(remain.subList(i + 1, remain.size()));
                        lottery(newResult, resultS, resultT, newRemain, remainList, f, s, t);
                    }
                }
            } else {
                for (int i = 0; i < remain.size(); i++) {
                    ArrayList<Integer> newResult = (ArrayList<Integer>) resultS.clone();
                    newResult.add(remain.get(i));
                    ArrayList<Integer> newRemain = new ArrayList<Integer>(remain.subList(i + 1, remain.size()));
                    lottery(resultF, newResult, resultT, newRemain, remainList, f, s, t);
                }
            }
        } else {
            for (int i = 0; i < remain.size(); i++) {
                ArrayList<Integer> newResult = (ArrayList<Integer>) resultT.clone();
                newResult.add(remain.get(i));
                ArrayList<Integer> newRemain = new ArrayList<Integer>(remain.subList(i + 1, remain.size()));
                lottery(resultF, resultS, newResult, newRemain, remainList, f, s, t);
            }
        }
    }

    @org.junit.Test
    public void test42() throws IOException, URISyntaxException {
        ArrayList<Integer> integers = new ArrayList<>();
        ArrayList<Integer> integers1 = new ArrayList<>();
        ArrayList<Integer> integers2 = new ArrayList<>();
        ArrayList<Integer> integers3 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        ArrayList<Integer> integers4 = new ArrayList<>();
        lottery(integers,integers1,integers2,integers3,integers4,1,2,3);
    }

    /**
     *  获取到2个字符串的偏移距离 除汉字
     * @param str1 字符串1
     * @param str2 字符串2
     * @return -1表示有参数为null , 其他为偏移距离
     */
    public static int getStrDistance(String str1 ,String str2){

        //为空返回-1
        if (str1 == null || str2 == null) {
            return -1;
        }

        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        //填充第0位
        for (int i = 0; i <= str2.length(); i++) {
            distance[0][i] = i;
        }

        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }

        //填充其他位
        for (int i = 0; i < str1.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {

                //判断字符是否相等
                int temp = 0;
                if(str1.charAt(i) != str2.charAt(j)){
                    temp= 1;
                }

                //为什么要加1 因为i或者j = 0的时候，必会出现0
                int firstCharDistance = distance[i][j+1]+1;
                int secondDistance = distance[i+1][j]+1;
                //获取到交点值
                int replace = distance[i][j];
                int min = Math.min(firstCharDistance, secondDistance);
                distance[i+1][j+1] =Math.min(min,replace);
            }
        }

        return distance[str1.length()][str2.length()];
    }


    @org.junit.Test
    public void test43() throws IOException, URISyntaxException {
        int strDistance = getStrDistance("abcde", "abcd");
        System.out.println(strDistance);
    }

    /**
     * 获取多种纸币最小组合数来等于给定金额
     * @param moneys 纸币组合
     * @param target 额定金额
     * @return
     */
    public static int getMinCount(int[] moneys , int target ){

        //获取到对应总额的最小组合数
        int[] moneyCom = new int[target+1];
        //临时最小组合数

        for(int i = 1 ; i<= target ; i++){
            for(int j = 0 ; j<moneys.length ; j++){
                if(i - moneys[j] >=0 ){
                    //当前的等于上一次+1
                    moneyCom[i] = moneyCom[i-moneys[j]]+1;
                }
            }
        }
        return  moneyCom[target];
    }

    public static int getMinMoney(int[] c, int[] value) {
        int[] t = new int[3];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < value.length; j++) {
                if (i - value[j] >= 0) {
                    t[j] = c[i - value[j]] + 1;
                }
            }
            int min = Math.min(t[0], t[1]);
            min = Math.min(min, t[2]);
            c[i] = min;
        }
        return c[c.length - 1];
    }

    @org.junit.Test
    public void test44() throws IOException, URISyntaxException {
        int minCount = getMinCount(new int[]{1,2, 3, 4}, 5);
        System.out.println(minCount);

//        int minMoney = getMinMoney(new int[100], new int[]{2, 3, 7});
//        System.out.println(minMoney);
    }

    /**
     * 深度查找字典树
     * @param node 根节点
     */
    public static void dfsByStack(TreeNode node){

        Stack<TreeNode> stack = new Stack<>();

        stack.push(node);

        while(!stack.isEmpty()){

            TreeNode currentNode = stack.pop();

            //没有子节点
            if(currentNode.sons.size() == 0){
                System.out.println(currentNode.prefix+currentNode.label);
            }else {

                //迭代
                Iterator<Map.Entry<Character, TreeNode>> iterator = currentNode.sons.entrySet().iterator();

                while(iterator.hasNext()){
                    //放入栈中。  但是顺序打印出来是跟放入的相反
                    stack.push(iterator.next().getValue());
                }
            }
        }
    }

    /**
     * 创建字典树
     * @param rootNode 字典root
     * @param str 未收入的单词
     * @return 字典root
     */
    public static TreeNode createDictionary(TreeNode rootNode,String str){

        TreeNode treeNode = new TreeNode();
        TreeNode tempNode = rootNode;
        for(int i = 0 ; i<str.length() ; i++){
            if(tempNode.sons.containsKey(str.charAt(i))){
                //有这个子节点
                treeNode = tempNode.sons.get(str.charAt(i));
            }else {
                //没有这个子节点 则收录
                 String pre =  tempNode.getPrefix()+tempNode.getLabel();
                treeNode = new TreeNode(str.charAt(i),pre,"");
                tempNode.sons.put(str.charAt(i),treeNode);
                tempNode = treeNode;
            }

        }

        return rootNode;
    }

    @org.junit.Test
    public void test45() throws IOException, URISyntaxException {

    }

    /**
     * 创建人际网
     * @param huamnLength 总共有多少人
     * @return 人际网
     */
    public static Node[] createHuman (int huamnLength , int relation_num ){

        //用来生成随机用户
        Random random = new Random(huamnLength);
        //人的node数组
        Node[] user_nodes = new Node[huamnLength];

        //生成所有表示用户的节点
        for(int i = 0 ; i <huamnLength ; i++){
            user_nodes[i] = new Node(i);
        }

        //创建好友关系
        for(int i = 0 ; i<relation_num ; i++){
            int humanA_id = random.nextInt(huamnLength);
            int humanB_id = random.nextInt(huamnLength);

            //自己跟自己不能是好友
            if(humanA_id == humanB_id){
                continue;
            }

            //创建关系
            Node humanA = user_nodes[humanA_id];
            Node humanB = user_nodes[humanB_id];

            humanA.friends.add(humanB_id);
            humanB.friends.add(humanA_id);
        }

        return user_nodes;
    }

    /**
     * 查找某一个人的关系网
     * @param nodes 好友关系
     * @param user_id 对应的人
     */
    public static void  bfs(Node[] nodes , int user_id){

        //防止数组越界
        if(user_id > nodes.length){
            return;
        }

        //用于广度搜索的队列
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(user_id);
        //用于存放已经被访问过的点
        HashSet<Integer> visited = new HashSet<>();
        visited.add(user_id);

        while(!queue.isEmpty()){
            int current_user_id = queue.poll();
            //判断这个人是否存在node中
            if(nodes[current_user_id] == null){
                continue;
            }
            for(int friend_id :nodes[current_user_id].friends){
                //排除不存在的
                if (nodes[friend_id] == null) {
                    continue;
                }
                //排除已经访问过的
                if(visited.contains(friend_id)){
                    continue;
                }
                //加入
                queue.offer(friend_id);
                visited.add(friend_id);

                //判断是几度好友
                nodes[friend_id].degree =  nodes[current_user_id].degree +1;
                System.out.println(String.format("\t%d 度好友：%d",nodes[friend_id].degree,friend_id));
            }

        }

    }



    @org.junit.Test
    public void test46() throws IOException, URISyntaxException {
        Node[] human = createHuman(20, 40);
        bfs(human,10);
        System.out.println("11111");
    }

    /**
     *  深度查找文件名
     * @param filePath
     * @return 文件名list
     */
    public static List<String> dfsDir(String filePath){

        List<String> fileNmaeList = new ArrayList<>();

        Stack<File> fileStack = new Stack<>();

        File file = new File(filePath);
        fileStack.push(file);
        while(!fileStack.isEmpty()){
            File currentFile = fileStack.pop();
            //添加到name list
            fileNmaeList.add(currentFile.getName());
            //判断是否是文件夹
            if(currentFile.isDirectory()){
                for(File chileFile : currentFile.listFiles()){
                    fileStack.push(chileFile);
                }
            }
        }
        return fileNmaeList;
    }

    /**
     *  广度查找文件名
     * @param filePath
     * @return 文件名list
     */
    public static List<String> bfsDir(String filePath){

        List<String> fileNmaeList = new ArrayList<>();

        Queue<File> fileStack = new LinkedList<>();

        File file = new File(filePath);
        fileStack.offer(file);
        while(!fileStack.isEmpty()){
            File currentFile = fileStack.poll();
            //添加到name list
            fileNmaeList.add(currentFile.getName());
            //判断是否是文件夹
            if(currentFile.isDirectory()){
                for(File chileFile : currentFile.listFiles()){
                    fileStack.offer(chileFile);
                }
            }
        }
        return fileNmaeList;
    }

    @org.junit.Test
    public void test47() throws IOException, URISyntaxException {
        List<String> strings = bfsDir("C:\\Users\\wanglin\\Desktop\\11");
        System.out.println("111");
    }

    /**
     * 双向广度查找两个用户之间的广度距离
     * @param user_nodes 好友关系网
     * @param user_id_a 用户a的ID
     * @param user_id_b 用户b的ID
     * @return 用户a b 的广度距离  -1表示超出距离或者没有找到，0表示a和b是相同用户
     */
    public static int bi_bfs(Node[] user_nodes , int user_id_a , int user_id_b){

        //用户id超出关系网长度
        if(user_id_a > user_nodes.length || user_id_b>user_nodes.length){
            return -1;
        }
        //用户相同
        if(user_id_a == user_id_b){
            return 0;
        }

        //队列a
        LinkedList<Integer> quene_a = new LinkedList<>();
        //队列b
        LinkedList<Integer> quene_b = new LinkedList<>();

        //a放入队列
        quene_a.offer(user_id_a);
        //a 访问过的人
        HashSet<Integer> set_a = new HashSet<>();
        set_a.add(user_id_a);

        //b放入队列
        quene_b.offer(user_id_b);
        //b 访问过的人
        HashSet<Integer> set_b = new HashSet<>();
        set_b.add(user_id_b);

        //max_degree 防止死循环
        int degree_a = 0 ,degree_b = 0 ,max_degree =20;

        while((degree_a +degree_b) <max_degree){
            degree_a++;
            //沿着a的方向，继续广度朝赵degree +1 的好友
            getNextDegreeeFriend(user_id_a,user_nodes,quene_a,set_a,degree_a);
            //判断是否有交集
            if(hasOverlap(set_a,set_b)){
                return degree_a +degree_b;
            }

            degree_b++;
            //沿着a的方向，继续广度朝赵degree +1 的好友
            getNextDegreeeFriend(user_id_b,user_nodes,quene_b,set_b,degree_b);
            //判断是否有交集
            if(hasOverlap(set_a,set_b)){
                return degree_a +degree_b;
            }
        }
        return  -1 ;
    }

    /**
     *  判断2个hashSet是否有交集
     * @param set_a
     * @param set_b
     * @return true有交集， false没有
     */
    private static boolean hasOverlap(HashSet<Integer> set_a, HashSet<Integer> set_b) {

        if(set_a.isEmpty() || set_b.isEmpty()){
            return false;
        }

        for (Integer num : set_a){
            if(set_b.contains(num)){
                return true;
            }
        }
        return false;
    }

    /**
     *  找到当前用户的下一度好友
     * @param user_id_a 当前用户的id
     * @param user_nodes 人际关系网
     * @param quene_a 队列，将要被查找的人的id
     * @param set_a 已被探测到跟a有关系的人
     * @param degree_a 几度好友
     */
    private static void getNextDegreeeFriend(int user_id_a, Node[] user_nodes, LinkedList<Integer> quene_a, HashSet<Integer> set_a, int degree_a) {
        if(user_nodes[user_id_a] == null){
            return;
        }

        Node current_node = user_nodes[user_id_a];
        //获取到当前用户的好友
        HashSet<Integer> friends = current_node.friends;
        if(friends.isEmpty()){
            return;
        }
        HashMap<Integer, Integer> degrees = current_node.degrees;
        //填充数据
        for(Integer frindID : friends){
            quene_a.offer(frindID);
            set_a.add(frindID);
            degrees.put(frindID,degree_a);
        }

    }











    /**
     *  初始化数据
     * @return 数据源
     */
    public static DijkNode initData(){
        DijkNode start = new DijkNode("s");
        DijkNode a = new DijkNode("a");
        DijkNode b = new DijkNode("b");
        DijkNode c = new DijkNode("c");
        DijkNode d = new DijkNode("d");
        DijkNode e = new DijkNode("e");
        DijkNode f = new DijkNode("f");
        DijkNode g = new DijkNode("g");
        DijkNode h = new DijkNode("h");

        List<DijkNode> children = new ArrayList<>();
        children.add(a);
        children.add(b);
        children.add(c);
        children.add(d);
        Map<String, Double> weights = new HashMap<>();
        weights.put("a", 0.5);
        weights.put("b", 0.3);
        weights.put("c", 0.2);
        weights.put("d", 0.4);
        start.children = children;
        start.weights = weights;

        children = new ArrayList<>();
        children.add(e);
        weights = new HashMap<>();
        weights.put("e", 0.3);
        a.children = children;
        a.weights = weights;

        children = new ArrayList<>();
        children.add(a);
        children.add(f);
        weights = new HashMap<>();
        weights.put("a", 0.2);
        weights.put("f", 0.1);
        b.children = children;
        b.weights = weights;

        children = new ArrayList<>();
        children.add(f);
        children.add(h);
        weights = new HashMap<>();
        weights.put("f", 0.4);
        weights.put("h", 0.8);
        c.children = children;
        c.weights = weights;

        children = new ArrayList<>();
        children.add(c);
        children.add(h);
        weights = new HashMap<>();
        weights.put("c", 0.1);
        weights.put("h", 0.6);
        d.children = children;
        d.weights = weights;

        children = new ArrayList<>();
        children.add(g);
        weights = new HashMap<>();
        weights.put("g", 0.1);
        e.children = children;
        e.weights = weights;

        children = new ArrayList<>();
        children.add(e);
        children.add(h);
        weights = new HashMap<>();
        weights.put("e", 0.1);
        weights.put("h", 0.2);
        f.children = children;
        f.weights = weights;

        children = new ArrayList<>();
        children.add(g);
        weights = new HashMap<>();
        weights.put("g", 0.4);
        h.children = children;
        h.weights = weights;

        return start;
    }

    /**
     *  获取最小权重
     * @param mw 未剔除的节点集
     * @return 最小节点
     */
    public static String findGeoWithMinWeight(Map<String,Double> mw){
        double min = Double.MAX_VALUE;
        String label = "";
        for(Map.Entry<String, Double> entry : mw.entrySet()){
            if(entry.getValue() < min){
                min = entry.getValue();
                label = entry.getKey();
            }
        }
        return label;
    }

    /**
     * 更新最小权重
     * @param key 当前节点名称
     * @param value 当前节点到初始节点的加起来的值
     * @param result_mw 每个点到初始节点的值
     */
    public static void updateWeight(String key, Double value, Map<String, Double> result_mw) {
        if(result_mw.containsKey(key)){
            //判断value大小
            if(value<result_mw.get(key)){
                result_mw.put(key,value);
            }
        }else {
            result_mw.put(key,value);
        }
    }

    /**
     *  获取到最小节点
     * @param l 访问过的节点
     * @param label 节点名称
     * @return DijkNode
     */
    public static DijkNode getMinDijkNode(List<DijkNode> l , String label){

        if(l != null && l.size() >0){
            for(DijkNode d : l){
                if(d.lable.equals(label)){
                    return d;
                }
            }
        }
        return null;
    }


        public static Map<String, Double> dijkstraArithmetic(DijkNode start , Map<String, Double> mw ,Map<String, Double> result_mw){

            //子节点
            List<DijkNode> children = start.children;
            //子节点的weight
            Map<String, Double> weights = start.weights;

            //子节点放入
            for(Map.Entry<String, Double> entry :weights.entrySet()){
                mw.put(entry.getKey(),entry.getValue());
            }

            while(mw.size() != 0){
                String minLabel = findGeoWithMinWeight(mw);
                //在result_mw更新minLabel
                updateWeight(minLabel,mw.get(minLabel),result_mw);
                DijkNode minDijkNode = getMinDijkNode(children, minLabel);
                List<DijkNode> minChildrens = minDijkNode.children;
                //更新最小节点下一级节点到起点的weight
                if(minChildrens != null && minChildrens.size() >0){
                    children.addAll(minChildrens);
                    for(DijkNode minNode : minChildrens){
                        //bigDecimal 控制精度
                        mw.put(minNode.lable,BigDecimal.valueOf(minDijkNode.weights.get(minNode.lable)).add(BigDecimal.valueOf(result_mw.get(minLabel))).doubleValue());
                    }
                }
                mw.remove(minLabel);
            }
            return result_mw;
        }

    @org.junit.Test
    public void test48() throws IOException, URISyntaxException {
        Map<String, Double> stringDoubleMap = dijkstraArithmetic(initData(), new HashMap<String, Double>(), new HashMap<String, Double>());
        System.out.println(stringDoubleMap);
    }

    @org.junit.Test
    public void test49() throws IOException, URISyntaxException {
        HashMap<String, Object> rootMap = new HashMap<>();
        rootMap.put("id",111);
        rootMap.put("name","浙江省");
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        rootMap.put("child",hashMaps);



        HashMap<String, Object> rootMap1 = new HashMap<>();
        rootMap1.put("id",112);
        rootMap1.put("name","浙江省本级");
        hashMaps.add(rootMap1);

        HashMap<String, Object> rootMap2 = new HashMap<>();
        rootMap2.put("id",112);
        rootMap2.put("name","杭州市");
        ArrayList<HashMap<String, Object>> hashMaps1 = new ArrayList<>();
        rootMap2.put("child",hashMaps1);
        hashMaps.add(rootMap2);


        HashMap<String, Object> rootMap3 = new HashMap<>();
        rootMap3.put("id",112);
        rootMap3.put("name","杭州市本级");
        hashMaps1.add(rootMap3);

        HashMap<String, Object> rootMap4 = new HashMap<>();
        rootMap4.put("id",112);
        rootMap4.put("name","富阳区");
        hashMaps1.add(rootMap4);

        System.out.println(rootMap);

    }


    private static boolean isMobile(String str) {
        boolean matches = Pattern.matches("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", str);
        System.out.println(matches);
        return matches;
    }
}
