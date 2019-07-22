package org.java.web;

import org.apache.tomcat.jni.Mmap;
import org.java.service.ClientService;
import org.java.util.CookieUtil;
import org.java.util.DuanXinApi;
import org.java.util.IdentificationCard;
import org.java.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Controller
public class ClientController {

    @Autowired
    private IdentificationCard card;

    @Autowired
    private ClientService service;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @RequestMapping("clientLogin") //客户端登录
    @ResponseBody
    public Map clientLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam Map map){

        Map returnMap = new HashMap();


        String IsTrue = (String) map.get("IsTrue");


      Map m = service.clientLogin(map.get("PhoneNumber").toString(), map.get("Password").toString());


      if (m != null){ //表示该客户存在


          System.err.println("当前登录的用户信息:"+m);


          String json = JsonUtils.objectToJson(m);

          //把用户信息存入到redis中
          redisTemplate.opsForValue().set(m.get("customID").toString(), json, 30, TimeUnit.MINUTES);



          if(IsTrue != null && "true".equals(IsTrue)){ //表示当前客户选中记住密码



              CookieUtil.setCookie("CustomCOOKIEPHONE", map.get("PhoneNumber").toString(),
                      request, response, 24 * 60 * 60);
              CookieUtil.setCookie("CustomCOOKIEPASSWORD",map.get("Password").toString(),
                      request, response, 24 * 60 * 60);
              CookieUtil.setCookie("CustomCOOKIETRUE", IsTrue,
                      request, response, 24 * 60 * 60);

          }else{ //表示当前客户只是登录,没有选中记住密码


              CookieUtil.destroyCookie("CustomCOOKIEPHONE", request,
                      response);
              CookieUtil.destroyCookie("CustomCOOKIEPASSWORD",
                      request, response);
              CookieUtil.destroyCookie("CustomCOOKIETRUE", request,
                      response);

          }

          returnMap.put("type", "true");
          returnMap.put("customID", m.get("customID").toString());

          return returnMap;
      }else{
          returnMap.put("type", "false");
          return returnMap;
      }

    }


    @RequestMapping("forward/{method}")
    public String forward(@PathVariable("method") String method){

        return "/"+method;
    }

    @RequestMapping("cancellation") //客户注销
    public String cancellation(HttpSession session,String customID){

        if(customID !=null && !"".equals(customID)){
            session.removeAttribute("map");
            //从redis中移除数据
            redisTemplate.delete(customID);
        }

        return "HomePage";
    }


    @RequestMapping("homepage")   //返回主页
    public String homepage(HttpSession session,String customID){

        if(customID !=null && !"".equals(customID)){

            //从数据库中取出用户数据
            String json = redisTemplate.opsForValue().get(customID);

            Map<String, Object> map = JsonUtils.jsonToMap(json);

            session.setAttribute("map", map);

        }


        return "HomePage";

    }


    @RequestMapping("customlogin") //进入登录页面
    public String customlogin(Model model,HttpServletRequest request){
        System.out.println("********************");
        model.addAttribute("PhoneNumber", CookieUtil.getCookie("CustomCOOKIEPHONE", request));
        model.addAttribute("Password", CookieUtil.getCookie("CustomCOOKIEPASSWORD", request));
        model.addAttribute("ISTrue", CookieUtil.getCookie("CustomCOOKIETRUE", request));

            return "CustomLogin";
    }

    @RequestMapping("checkPhone") //验证客户注册的手机号是否已经存在
    @ResponseBody
    public String checkPhone(String phone){

        Map map = service.checkPhone(phone);

        if (map != null){
            return "false";
        }else{
            return "true";
        }
    }

    @RequestMapping("faSongYanZhengMa") //发送验证码
    @ResponseBody
    public String faSongYanZhengMa(String phone,HttpSession session){


       String code = DuanXinApi.mobileQuery(phone); //返回验证码

        session.setAttribute("phonecode", code); //验证码存到session中
        session.setMaxInactiveInterval(2 * 60); //设置时间为两分钟

       return "true";
    }

    @RequestMapping("checkfaSongYanZhengMa") //效验验证码
    @ResponseBody
    public String checkfaSongYanZhengMa(String phoneCode,HttpSession session){

        String code = (String) session.getAttribute("phonecode"); //取出验证码

        if (phoneCode.equals(code)){ //判断验证码是否正确
            return "true";
        }else{
            return  "false";
        }
    }
    @RequestMapping("insertClient") //添加客户
    @ResponseBody
    public String insertClient(@RequestParam Map map){
        service.insertClient(map);

        return "true";
    }
    @RequestMapping("findPhone") //查询用户要修改密码的手机号是否存在
    @ResponseBody
    public String findPhone(String phone){

        Map map = service.checkPhone(phone);

        if (map == null){ //表示不存在
            return "false";
        }else{ //表示存在
            return "true";
        }

    }

    @RequestMapping("setNewPassword")
    @ResponseBody
    public String setNewPassword(String phone,String newPwd){

        int row = service.setNewPassword(phone, newPwd);

        if(row>0){  //返回受影响的行数
            return "true";
        }else{
            return "false";
        }
    }

    @RequestMapping("personalCenter") //进入个人中心,判断用户是否已经登录
    public String personalCenter(HttpSession session,String customID){

        System.out.println("进入了personalCenter方法");


        if(customID !=null && !"".equals(customID)){
            //从redis中取得信息
            String json = redisTemplate.opsForValue().get(customID);

            Map<String, Object> map = JsonUtils.jsonToMap(json);

            if(map.get("customerTypeID").toString().equals("1")){

                System.out.println("个体商户");

                session.setAttribute("map", map);

                //表示个体商户
                return "CustomerPersonalCenter";
            }else {
                //企业商户
                System.out.println("企业商户");

                session.setAttribute("map", map);

                return "EnterpriseInformation";
            }
        }



        return "";
    }

    @RequestMapping("online") //在线下单,判断用户是否已经登录
    public String online(HttpSession session,String customID){

        System.out.println("进入online方法");

        if(customID !=null && !"".equals(customID)){
            //从redis中获取数据
            String json = redisTemplate.opsForValue().get(customID);

            Map map = JsonUtils.jsonToMap(json);

            session.setAttribute("map", map);

        }

        return "OnlineOrder";
    }


    @RequestMapping("updatePersonal")  //修改个体商户的基本信息
    @ResponseBody
    public String  updatePersonal(@RequestParam Map map, HttpServletRequest request) throws IOException {

        System.err.println("@@@@@@@@@@@@@@@@"+map);

        //判断要修改的身份证号码
        int idCard = service.checkCustomIDNumber(map.get("customID").toString(), map.get("customidnumber").toString());
        //判断修改的手机号码
        int phondId = service.checkPhoneUpdate(map.get("customID").toString(), map.get("customphonenumber").toString());

        if(idCard >=1){
            return "该身份证号码已被注册!";
        }else if(phondId >=1){
            return "该手机号码已被注册!";
        }

        //获得项目根路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/upimages";
        //获得文件上传的请求
        MultipartHttpServletRequest re= (MultipartHttpServletRequest) request;
        //获得所有名称为picture的文件
        List<MultipartFile> list = re.getFiles("picture");

        if(list.size()!=0) {//判断是否为空
            System.out.println("上传文件不为空====================================="+list.size());
            for (MultipartFile f : list) {
                System.out.println(f.getOriginalFilename()+"-"+map.get("fileName"));
                //判断身份证是否是重新上传的
                if (f.getOriginalFilename().equals(map.get("fileName"))) {
                    //将文件转换成字节数组
                    byte[] IDNumberpath = f.getBytes();
                    //解析身份证图片获得图片的身份证信息
                    Map IDNumber = card.idCard(IDNumberpath);
                    //判断身份证
                    if (!IDNumber.get("ID").equals(map.get("customidnumber"))) {
                        return "身份证号码和输入号码不一致";
                    } else if (!IDNumber.get("name").equals(map.get("customname"))) {
                        return "身份证姓名和输入姓名不一致";
                    }
                    map.put("fileName", map.get("customphonenumber").toString() + map.get("fileName"));
                }

            }
            //上传文件
            for (MultipartFile f : list) {
                //获得文件名称
                String fileName = map.get("customphonenumber") + f.getOriginalFilename();

                //创建文件
                File newfile = new File(path, fileName);
                //判断文件的父级目录是否存在，不存在创建
                if (!newfile.getParentFile().exists()) {
                    newfile.getParentFile().mkdirs();
                }
                f.transferTo(newfile);
            }

        }else {
            System.out.println(path+"/"+map.get("customidnumber").toString());
            //根据身份证图片路径解析
            Map IDNumber = card.idCard2(path+"/"+map.get("fileName").toString());

            System.out.println("身份证解析："+IDNumber);
            //判断身份证
            if (!IDNumber.get("ID").equals(map.get("customidnumber"))) {
                return "身份证号码和输入号码不一致";
            } else if (!IDNumber.get("name").equals(map.get("customname"))) {
                return "身份证姓名和输入姓名不一致";
            }
        }

        service.updateClientIndividual(map);


        return "true";
    }

    @RequestMapping("updateClientEnterprise")  //修改企业商户的基本信息
    @ResponseBody
    public String updateClientEnterprise(@RequestParam Map map, HttpServletRequest request) throws IOException {
        System.err.println("@@@@@@@@@@@@@@@@"+map);
        //判断要修改的手机号码是否存在
        int phondId = service.checkPhoneUpdate(map.get("customID").toString(), map.get("customphonenumber").toString());
        int telePhoneId = service.checkTelePhoneNumber(map.get("customID").toString(), map.get("telePhoneNumber").toString());
        int registerId = service.checkRegisterCard(map.get("customID").toString(), map.get("businesslicensenumber").toString());

        if(phondId >=1 ){
            return "手机号码已被注册!";
        }else if(telePhoneId >=1 ){
            return "电话号码已被注册!";
        }else if(registerId >=1 ){
            return "注册号已存在!";
        }

        //获得项目根路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/upimages";

        //获得文件上传的请求
        MultipartHttpServletRequest re= (MultipartHttpServletRequest) request;
        //获得所有名称为picture的文件
        List<MultipartFile> list = re.getFiles("picture");

        if(list.size()!=0) {//判断是否为空
            System.out.println("上传文件不为空====================================="+list.size());
            for (MultipartFile f : list) {
                System.out.println(f.getOriginalFilename()+"-"+map.get("fileName"));
                //判断身份证是否是重新上传的
                if (f.getOriginalFilename().equals(map.get("fileName"))) {
                    //将文件转换成字节数组
                    byte[] business = f.getBytes();

                    Map<String, Object> busMap = IdentificationCard.parseLicence(business);
                    //Map busMap = (Map) IdentificationCard.parseLicence(business);
                    System.err.println("返回值是:"+busMap);

//                    Map<String, Object> businessMap = IdentificationCard.parseLicence(map.get("picture").toString());
                    //判断输入的注册号和营业执照的注册号是否一致
                    if(!busMap.get("registerCard").equals(map.get("businesslicensenumber"))){
                        return "输入的注册号和营业执照的注册号不一致!";
                    }
                    //判断输入的企业名称和营业执照的企业名称是否一致
                    if(!busMap.get("enterpriseName").equals(map.get("customname"))){
                        return "输入的企业名称和营业执照的企业名称不一致!";
                    }
                    map.put("fileName", map.get("customphonenumber").toString() + map.get("fileName"));
                }

            }
            //上传文件
            for (MultipartFile f : list) {
                //获得文件名称
                String fileName = map.get("customphonenumber") + f.getOriginalFilename();

                //创建文件
                File newfile = new File(path, fileName);
                //判断文件的父级目录是否存在，不存在创建
                if (!newfile.getParentFile().exists()) {
                    newfile.getParentFile().mkdirs();
                }
                f.transferTo(newfile);
            }

        }else{
            //根据身份证图片路径解析
            Map<String, Object> businessMap =  IdentificationCard.parseLicence(path+"/"+map.get("fileName").toString());

            System.out.println("营业执照解析："+businessMap);
            //判断输入的注册号和营业执照的注册号是否一致
            if(!businessMap.get("registerCard").equals(map.get("businesslicensenumber"))){
                return "输入的注册号和营业执照的注册号不一致!";
            }
            //判断输入的企业名称和营业执照的企业名称是否一致
            if(!businessMap.get("enterpriseName").equals(map.get("customname"))){
                return "输入的企业名称和营业执照的企业名称不一致!";
            }
        }
        //执行修改操作
        service.updateClientEnterprise(map);

        return "true";
    }



    @RequestMapping("updateClientPwd")  //修改客户端的密码
    @ResponseBody
    public String updateClientPwd(@RequestParam Map map){

        int row = service.updateClientPwd(map);

        System.out.println(row);

        if (row == 1){
            return "true";
        }else{
            return "false";
        }
        

    }
    @RequestMapping("updatePwdLogin") //修改密码之后进入登录页面
    public String updatePwdLogin(HttpSession session,String customID){

        if(customID !=null && !"".equals(customID)){

            session.removeAttribute("map");
            //移除redis中的数据
            redisTemplate.delete(customID);
        }

        return "CustomLogin";
    }
}
