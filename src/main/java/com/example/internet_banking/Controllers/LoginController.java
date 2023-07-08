package com.example.internet_banking.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String Login(){
        try{
            return "Login.html";
        }catch(Exception ex){
            throw ex;
        }
    }

//    @PostMapping("/validateUser")
//    public String checkUserValidOrNot(@ModelAttribute("userData")HashMap<String,String> userData){
//
//    }

}
