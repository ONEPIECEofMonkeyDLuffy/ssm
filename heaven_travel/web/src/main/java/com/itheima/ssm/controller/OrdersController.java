package com.itheima.ssm.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.ssm.domain.Orders;
import com.itheima.ssm.service.IOrdersService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;
    @RequestMapping("/findAll.do")
    public ModelAndView findALl(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "4") Integer size)throws Exception {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("ordersList", new PageInfo(ordersService.findAll(page,size)));
//        mav.setViewName("orders-list");
//        return mav;
        ModelAndView mv = new ModelAndView();
        List<Orders> ordersList = ordersService.findAll(page, size);
        //PageInfo就是一个分页Bean
        PageInfo pageInfo = new PageInfo(ordersList);
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("orders-page-list");
        return mv;
    }
    @RequestMapping("/findById")
    public ModelAndView findById(String id/*@RequestParam(name="id")String ordersId*/) throws Exception{
        ModelAndView mav = new ModelAndView();
        mav.addObject("orders", ordersService.findById(id));
        mav.setViewName("orders-show");
        return mav;
    }

}
