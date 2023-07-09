package com.example.internet_banking.Controllers;
import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("/login")
    public String Login(Model model){
        model.addAttribute("loginId","");
        model.addAttribute("password","");
        return "Login.html";
    }

    @PostMapping("/validateUser")
    public String checkUserIsValidOrNot(@RequestParam HashMap<String,String> userData, RedirectAttributes ra){
        UserAccountInfo userDetails = loginService.checkUserIsValidOrNot(userData.get("loginId"), userData.get("password"));
        if (userDetails != null){
            return "redirect:/home";
        } else{
            ra.addFlashAttribute("msg2","Invalid Credentials !! Please Try Again .");
            return "redirect:/login";
        }
    }

}
