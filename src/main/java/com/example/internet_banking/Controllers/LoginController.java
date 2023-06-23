package com.example.internet_banking.Controllers;

import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Repositories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.Path;
import java.util.HashMap;

@Controller
@Path("/login")
public class LoginController {
    @Autowired
    private UserAccountRepo userAccountRepo;

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
