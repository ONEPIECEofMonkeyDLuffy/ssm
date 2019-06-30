package com.itheima.ssm.controller;

import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.IUserService;
import com.itheima.ssm.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("userList", userService.findAll());
        mav.setViewName("user-list");
        return mav;
    }

    @RequestMapping("/findById.do")
    public ModelAndView findById(String id) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", userService.findById(id));
        mav.setViewName("user-show1");
        return mav;
    }

    @RequestMapping("/save.do")
    public String save(UserInfo userInfo) throws Exception {
        userService.save(userInfo);
        return "redirect:findAll.do";
    }

    @RequestMapping("/findUserByIdAndAllRole.do")
    public ModelAndView findUserByIdAndAllRole(@RequestParam(name = "id") String userId) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", userService.findById(userId));
        mav.addObject("roleList", userService.findOtherRoles(userId));
        mav.setViewName("user-role-add");
        return mav;
    }

    @RequestMapping("/addRoleToUser.do")
    public String addRoleToUser(@RequestParam(name ="ids") String[] roleIds,@RequestParam(name ="userId")String userId) {
        userService.addRoleToUser(userId, roleIds);
        return "redirect:findAll.do";
    }
}
