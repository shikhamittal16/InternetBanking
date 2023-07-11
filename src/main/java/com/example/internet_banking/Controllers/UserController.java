package com.example.internet_banking.Controllers;

import com.example.internet_banking.Entities.UserPersonalDetails;
import com.example.internet_banking.Services.NewUsersService;
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
import java.util.Objects;

@Controller
public class UserController {
    @Autowired
    private NewUsersService usersService;

    @RequestMapping("/newUserDetails")
    public String NewUserDetails(Model model) {
        try {
            model.addAttribute("userData", new UserPersonalDetails());
            return "NewUserDetails.html";
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequestMapping("/userAccountDetails")
    public String userAccountDetails(Model model, @ModelAttribute("userData") HashMap<String, String> userData) {
        model.addAttribute("userData", userData);
        return "NewUserDetails2.html";
    }

    @PostMapping("/saveNewUserDetails")
    public String saveNewUserDetails(UserPersonalDetails newUserDetails, RedirectAttributes redirectAttributes) {
        try {
            HashMap<String, String> map = usersService.saveUserPersonalDetails(newUserDetails);
            if (Objects.equals(map.get("status"), "error")) {
                redirectAttributes.addFlashAttribute("msg2", "Something went wrong");
                return "redirect:/newUserDetails";
            } else if(Objects.equals(map.get("status"), "account already exist")) {
                redirectAttributes.addFlashAttribute("msg2", "Account already Exist.");
                return "redirect:/newUserDetails";
            }else {
                redirectAttributes.addFlashAttribute("userData", map);
                return "redirect:/userAccountDetails";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/newUserDetails";
        }
    }

    @PostMapping("/saveUserAccountDetails")
    public String saveUserAccountDetails(@RequestParam Map<String, String> userAccountInfo, RedirectAttributes redirectAttributes) {
        try {
            HashMap<String, String> responseMap = usersService.saveUserAccountInformation(userAccountInfo);
            if (Objects.equals(responseMap.get("status"), "success")) {
                redirectAttributes.addFlashAttribute("msg", "Account Created Successfully");
                return "redirect:/login";
            } else if(Objects.equals(responseMap.get("status"), "UserName Already Exist")){
                redirectAttributes.addFlashAttribute("msg2","UserName Already Exist !!");
                redirectAttributes.addFlashAttribute("userData", responseMap);
                return "redirect:/userAccountDetails";
            } else if(Objects.equals(responseMap.get("status"), "Insufficient deposit amount to open an account")){
                redirectAttributes.addFlashAttribute("msg2","Insufficient deposit amount !! Amount should be more than 100/-");
                redirectAttributes.addFlashAttribute("userData", responseMap);
                return "redirect:/userAccountDetails";
            } else {
                redirectAttributes.addFlashAttribute("msg2", "Something went wrong !! Please Try Again");
                return "redirect:/newUserDetails";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/newUserDetails";
        }
    }
}
