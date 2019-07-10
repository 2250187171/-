package org.java;

import com.baidu.aip.ocr.AipOcr;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.java.service.A_UserService;
import org.java.util.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {


    @Autowired
    private A_UserService service;

    @Test
    public void contextLoads() throws JSONException {
    }
    @Autowired
    ServletContext cxt;
    @Test
    public void ccc() throws FileNotFoundException, ParseException {
        String hashAlgorithmName = "MD5";
        String password="123";
        int hashIterations = 3;
        String salt="accp";
        Object obj = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);
        System.out.println(obj);
    }

    //设置APPID/AK/SK
    public static final String APP_ID = "16732909";
    public static final String API_KEY = "MlN0BZw92sTTB4Zl6EYYwaiR";
    public static final String SECRET_KEY = "IPO51PQZ8MtW3WIQlrtFem6MFc2bEKVW";

    @Test
    public void idCard() throws JSONException {
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
        byte[] file = readFile("C:\\Users\\徐志坚\\Desktop\\1.jpg");
        JSONObject res = client.idcard(file, idCardSide, options);
        Map map = JsonUtil.jsonToPojo(res.get("words_result").toString(), Map.class);
        Map map1= (Map) map.get("公民身份号码");
        Map map2= (Map) map.get("姓名");
        Map m=new HashMap();
        m.put("ID", map1.get("words"));
        m.put("name", map2.get("words"));

        System.out.println(m);

    }


    @Test
    public void drivingLicence() throws JSONException {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);// 建立连接的超时时间（单位：毫秒）
        client.setSocketTimeoutInMillis(60000);// 通过打开的连接传输数据的超时时间（单位：毫秒）
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");// 是否检测图像朝向，默认不检测，即：false。
//        options.put("detect_risk", "false");// 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，默认不开启，即：false。
        // front - 份证含照片的一面(back - 身份证带国徽的一面)
//        String idCardSide = "front";
        // 参数为本地图片二进制数组
        byte[] file = readFile("C:\\Users\\徐志坚\\Desktop\\2.jpg");
        JSONObject res = client.drivingLicense(file, options);
        Map map = JsonUtil.jsonToPojo(res.get("words_result").toString(), Map.class);
        System.out.println(map);
        Map map1= (Map) map.get("姓名");
        Map map2= (Map) map.get("至");
        Map m=new HashMap();
        m.put("name", map1.get("words"));
        m.put("start", map2.get("words"));

        System.out.println(m);

    }


    //将图片转换成2进制数组
    private byte[] readFile(String path) {
        File f = new File(path);
        System.out.println(f.getPath()+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        String type=path.substring(path.lastIndexOf(".")+1,path.length());
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, type, baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            byte[] bytes = baos.toByteArray();

            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
