package org.java.util;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class IdentificationCard {
    //设置APPID/AK/SK
    public static final String APP_ID = "16732909";
    public static final String API_KEY = "MlN0BZw92sTTB4Zl6EYYwaiR";
    public static final String SECRET_KEY = "IPO51PQZ8MtW3WIQlrtFem6MFc2bEKVW";

    //获得照片上的身份证信息
    public Map idCard(byte[] file){
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);// 建立连接的超时时间（单位：毫秒）
        client.setSocketTimeoutInMillis(60000);// 通过打开的连接传输数据的超时时间（单位：毫秒）
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");// 是否检测图像朝向，默认不检测，即：false。
        options.put("detect_risk", "false");// 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，默认不开启，即：false。
        // front - 份证含照片的一面(back - 身份证带国徽的一面)
        String idCardSide = "front";
        // 参数为本地图片二进制数组
//        byte[] file = readFile(path);
        JSONObject res = client.idcard(file, idCardSide, options);
        Map map = JsonUtils.jsonToPojo(res.get("words_result").toString(), Map.class);
        Map map1= (Map) map.get("公民身份号码");
        Map map2= (Map) map.get("姓名");
        Map m=new HashMap();
        m.put("ID", map1.get("words"));
        m.put("name", map2.get("words"));
        return m;
    }
    //获得驾驶证上的信息
    public Map drivingLicence(byte[] file){
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        JSONObject res = client.drivingLicense(file, options);
        Map map = JsonUtils.jsonToPojo(res.get("words_result").toString(), Map.class);
        Map map1= (Map) map.get("姓名");
        Map map2= (Map) map.get("至");
        Map m=new HashMap();
        m.put("name", map1.get("words"));
        m.put("endDate", map2.get("words"));
        //返回驾驶证信息
        return m;
    }


    //将图片转换成2进制数组
    public byte[] readFile(String path) {
        File f = new File(path);
        String type=path.substring(path.lastIndexOf(".")+1,path.length());
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            byte[] bytes = baos.toByteArray();

            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    //获得照片上的身份证信息
    public Map idCard2(String path){
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);// 建立连接的超时时间（单位：毫秒）
        client.setSocketTimeoutInMillis(60000);// 通过打开的连接传输数据的超时时间（单位：毫秒）
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");// 是否检测图像朝向，默认不检测，即：false。
        options.put("detect_risk", "false");// 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，默认不开启，即：false。
        // front - 份证含照片的一面(back - 身份证带国徽的一面)
        String idCardSide = "front";
        // 参数为本地图片二进制数组
        byte[] file = readFile(path);
        JSONObject res = client.idcard(file, idCardSide, options);
        Map map = JsonUtils.jsonToPojo(res.get("words_result").toString(), Map.class);
        Map map1= (Map) map.get("公民身份号码");
        Map map2= (Map) map.get("姓名");
        Map m=new HashMap();
        m.put("ID", map1.get("words"));
        m.put("name", map2.get("words"));
        return m;
    }
    //获得照片上营业照上的信息
    public static   Map<String,Object> parseLicence(String path) {
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        JSONObject res2 = client.basicGeneral(path, new HashMap<String, String>());
        JSONArray res = (org.json.JSONArray) res2.get("words_result");
        Map<String,Object> result = new HashMap<>();
        if (res.length() > 0) {
            for (int key = 0; key < res.length(); key++) {
                result.put("code","0");
                JSONObject object = (JSONObject) res.get(key);
                String value = object.getString("words");

                if(value.startsWith("企业名称")){
                    result.put("enterpriseName",value.substring(4,value.length()));
                }
                if(value.startsWith("法定代表人")){
                    result.put("name",value.substring(5,value.length()));
                }
                if(value.trim().startsWith("名称")){
                    result.put("companyName",value.substring(2,value.length()));
                }else if(value.trim().startsWith("称")){
                    result.put("companyName",value.substring(1,value.length()));
                }
                if(value.startsWith("统一社会信用代码")){
                    result.put("creditCode",value.substring(8,value.length()));
                }
                if(value.startsWith("注册号")){
                    result.put("registerCard",value.substring(3,value.length()));
                }

            }
            return result;
        }else{
            result.put("code","-1");
            result.put("creditCode","");
        }
        return result;
    }

    public static   Map<String,Object> parseLicence(byte[] file){

        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        JSONObject res2 = client.basicGeneral(file, new HashMap<>());
        JSONArray res = (org.json.JSONArray) res2.get("words_result");
        Map<String,Object> result = new HashMap<>();
        if (res.length() > 0) {
            for (int key = 0; key < res.length(); key++) {
                result.put("code","0");
                JSONObject object = (JSONObject) res.get(key);
                String value = object.getString("words");

                if(value.startsWith("企业名称")){
                    result.put("enterpriseName",value.substring(4,value.length()));
                }
                if(value.startsWith("法定代表人")){
                    result.put("name",value.substring(5,value.length()));
                }
                if(value.trim().startsWith("名称")){
                    result.put("companyName",value.substring(2,value.length()));
                }else if(value.trim().startsWith("称")){
                    result.put("companyName",value.substring(1,value.length()));
                }
                if(value.startsWith("统一社会信用代码")){
                    result.put("creditCode",value.substring(8,value.length()));
                }
                if(value.startsWith("注册号")){
                    result.put("registerCard",value.substring(3,value.length()));
                }

            }
            return result;
        }else{
            result.put("code","-1");
            result.put("creditCode","");
        }
        return result;
    }

}
