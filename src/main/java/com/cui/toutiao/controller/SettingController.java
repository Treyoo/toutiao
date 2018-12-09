package com.cui.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author:CuiWJ
 * Date:2018/11/8
 * Created with IDEA
 */
@Controller
public class SettingController {

    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        return "只有已登录的用户才看得到我";
    }
}
