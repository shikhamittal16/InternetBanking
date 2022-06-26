package com.example.internet_banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BankController {
    @Autowired
    private NewUserRepo repo;

    private static final String SUCCESSFUL_FLAG = "success";
    private static final String UNSUCCESSFUL_FLAG = "fail";

    @RequestMapping("/login")
    public String Login(){
        try{
            return "Login.html";
        }catch(Exception ex){
            throw ex;
        }
    }

    @RequestMapping("/newUserDetails")
    public String NewUserDetails(Model model){
        try{
            model.addAttribute("data" , new NewUserDetails());
            return "NewUserDetails.html";
        }catch(Exception ex){
            throw ex;
        }
    }

    @RequestMapping("/newUserDetails2")
    public String NewUserDetails2(Model model){
        try{
            model.addAttribute("data" , repo.getLastCreatedUserDetails());
            return "NewUserDetails2.html";
        }catch(Exception ex){
            throw ex;
        }
    }

    @PostMapping("/saveNewUserDetails")
    @ResponseBody
    public String SaveNewUserDetails(NewUserDetails newUserDetails){
        try{
                repo.save(newUserDetails);
                System.out.println("hi");
                return SUCCESSFUL_FLAG;
        }catch (Exception ex){
            throw ex;
        }
    }
}
