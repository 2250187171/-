package org.java.web;

import org.java.service.A_AuthorityService;
import org.java.service.A_RoleService;
import org.java.service.A_SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorityController {

    @Autowired
    private A_RoleService roleServic;

    @Autowired
    private A_SectionService sectionService;
    @Autowired
    private A_AuthorityService authorityService;
    //进入权限管理页面
    @RequestMapping("/userManage/authorityShow")
    public String authorityShow(Model model){
        //获得所有部门
        List<Map> sections = sectionService.findSectionAll();
        model.addAttribute("sections", sections);
        return "/userManage/authorityManage";
    }

    //获得所有权限
    @RequestMapping("/userManage/authorityAll")
    @ResponseBody
    public Map findAll(){
        Map m=new HashMap();
        m.put("code", 0);
        m.put("msg", "ok");
        m.put("data", authorityService.findAll());
        return m;
    }

    //根据角色Id和模块id查询
    @RequestMapping("/userManage/findByRoleIDAndPermID")
    @ResponseBody
    public int findByRoleIDAndPermID(int roleID,int permID,int sectionID){
        return authorityService.findByRoleIDAndPermID(roleID, permID,sectionID);
    }

    //修改角色权限
    @RequestMapping("/userManage/updateRole_Perm")
    @ResponseBody
    public int updateRole_Perm(Integer roleID,String perm,int sectionID){
        System.out.println("进入修改操作==========================================="+roleID+"--"+perm);
        //获得所有选中模块
        String [] perms=perm.split(",");
        //删除角色权限
        authorityService.deleteRole_Perm(roleID,sectionID);
        //新增角色权限
        for (String id : perms) {
            authorityService.addRole_Perm(roleID, Integer.parseInt(id),sectionID);
        }
        System.out.println("执行修改操作=======================================");
        return 1;
    }
    //根据部门查询角色
    @RequestMapping("/userManage/findRoleBySectionID")
    @ResponseBody
    public List<Map> findRoleBySectionID(int sectionID){
        List<Map> list = roleServic.findRoleBySectionID(sectionID);
        System.out.println(list);
        return list;
    }
}
