package com.itheima.ssm.controller;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.service.IRoleService;
import org.apache.ibatis.javassist.bytecode.annotation.MemberValueVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("roleList", roleService.findAll());
        mav.setViewName("role-list");
        return mav;
    }

    @RequestMapping("/save.do")
    public String save(Role role) throws Exception {
        roleService.save(role);
        return "redirect:findAll.do";
    }

    @RequestMapping("/findById")
    public ModelAndView findById(String id)throws Exception{
        ModelAndView mav = new ModelAndView();
        mav.addObject("role", roleService.findById(id));
        mav.setViewName("role-show");
        return mav;
    }
}
