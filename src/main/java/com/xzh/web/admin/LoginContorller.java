package com.xzh.web.admin;

import com.xzh.po.User;
import com.xzh.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginContorller {
    @Autowired
    UserServiceImpl userService;
    @GetMapping
    public String toLogin(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username")  String username, @RequestParam("password") String password, HttpSession session, RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user!=null) {
            user.setPassword(null);
            session.setAttribute("userInfo",user);
            return "admin/index";
        }
        else {
             attributes.addFlashAttribute("message","用户名密码不匹配");
             return"redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
      session.removeAttribute("userInfo");
        return"redirect:/admin";
    }
}
