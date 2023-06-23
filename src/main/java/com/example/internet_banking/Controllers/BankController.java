package com.example.internet_banking.Controllers;

import com.example.internet_banking.Entities.NewUserDetails;
import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Repositories.NewUserRepo;
import com.example.internet_banking.Repositories.UserAccountRepo;
import com.example.internet_banking.Services.BankService;
import com.example.internet_banking.Services.NewUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class BankController {
    @Autowired
    private NewUsersService usersService;
    @Autowired
    private BankService bankService;

    @RequestMapping("/home")
    public String homeNavbar(){
        return "HomePage.html";
    }

    @RequestMapping("/newUserDetails")
    public String NewUserDetails(Model model){
        try{
            model.addAttribute("userData" , new NewUserDetails());
            return "NewUserDetails.html";
        }catch(Exception ex){
            throw ex;
        }
    }

    @RequestMapping("/userAccountDetails")
    public String userAccountDetails(Model model , @ModelAttribute("userData")HashMap<String,String> userData){
        userData.put("accountNo","accountNo");
        userData.put("amount","");
        userData.put("loginId","");
        userData.put("password","");
        userData.put("userId","1");
        model.addAttribute("userData",userData);
        return "NewUserDetails2.html";
    }

    @PostMapping("/saveNewUserDetails")
    public String saveNewUserDetails(NewUserDetails newUserDetails , RedirectAttributes redirectAttributes){
        HashMap<String,String> map = new HashMap<>() ;
        try{
            map = usersService.saveUserPersonalDetails(newUserDetails);
            if(map.get("status") == "error"){
                redirectAttributes.addFlashAttribute("status","Something went wrong");
                return "redirect:/newUserDetails";
            }else{
                redirectAttributes.addFlashAttribute("userData",map);
                return "redirect:/userAccountDetails";
            }
        }catch (Exception ex){
            map.put("status","error");
            ex.printStackTrace();
            return "redirect:/newUserDetails";
        }
    }

    @PostMapping("/saveUserAccountDetails")
    public String saveUserAccountDetails(@RequestParam Map<String ,String > userAccountInfo , RedirectAttributes redirectAttributes){
        HashMap<String ,String > responseMap = new HashMap<>();
        try{
            responseMap = usersService.saveUserAccountInformation(userAccountInfo);
            if(responseMap.get("status") == "success"){
                redirectAttributes.addFlashAttribute("msg","Account Created Successfully");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("msg","Something went wrong !! Please Try Again");
                return "redirect:/newUserDetails";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("/accountDetails")
    public String accountDetails(Model model){
        HashMap<String,String> responseMap = new HashMap<>();
        responseMap = bankService.fetchUserAccountDetails();
        model.addAttribute("accountDetails",responseMap);
        return "AccountDetails.html";
    }
}
