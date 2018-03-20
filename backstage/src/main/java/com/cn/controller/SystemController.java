package com.cn.controller;

import com.cn.ManagerService;
import com.cn.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 后台客服管理员 控制类
 *
 * @author chen
 * @date 2018-01-02 18:25
 */
@Controller
@RequestMapping("/sys")
public class SystemController {
    @Autowired
    ManagerService managerService;

    /***
     * 下面两个底层实现一样  唯一区别就是hasRole默认添加 ROLE_ 前缀
     * @PreAuthorize("hasAuthority('ROLE_admin')")
     * @PreAuthorize("hasRole('admin')") 方法调用前判断是否有权限
     * @PreAuthorize("hasPermission('','sys:user')") 判断自定义权限标识符
     * @PostAuthorize("returnObject.id%2==0") 方法调用完后判断 若为false则无权限  基本用不上
     * @PostFilter("filterObject.id%2==0") 对返回结果进行过滤  filterObject内置为返回对象
     * @PreFilter(filterTarget="ids", value="filterObject%2==0") 对方法参数进行过滤 如有多参则指定参数 ids为其中一个参数
     */

    /**
     * 管理员列表页
     */
    @RequestMapping("/adminList")
    public String managerList(@PageableDefault(sort = { "createTime" }, direction = Sort.Direction.DESC)
                                          Pageable pageable, Model model, Manager manager){
        Page<Manager> managerList = managerService.getManagersList(pageable,manager);
        model.addAttribute("managerList",managerList);
        return "manager/managerList";
    }

    /**
     * 管理员编辑页
     */
    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/adminAlt")
    public String altManager(@RequestParam(required = false)String managerId,Model model){

        return "manager/managerAlt";
    }

    /**
     * 新增或修改管理员信息
     */
    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    @RequestMapping("/adminSave")
    public String register(@Valid Manager manager){
        String msg;
        try {
            msg = managerService.saveManager(manager);
        }catch (Exception e){
            msg = "保存失败";
        }
        return msg;
    }

    /**
     * 角色列表页
     */
    @RequestMapping("/roleList")
    public String roleList(@PageableDefault(value = 1, sort = { "createTime" }, direction = Sort.Direction.DESC)
                                       Pageable pageable,Model model){
        return "role/roleList";
    }

    /**
     * 菜单列表页
     */
    @RequestMapping("/menuList")
    public String menuList(Model model){
        return "menu/menuList";
    }
}