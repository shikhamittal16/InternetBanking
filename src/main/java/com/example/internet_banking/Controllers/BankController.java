package com.example.internet_banking.Controllers;

import com.example.internet_banking.Entities.Beneficiaries;
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

//th:field = isse agar hum direct object m value pass kra rhe hai to jb use krenege like saveNewUserDetails m
//th:value = jb hum @requestParam use kr rhe hai to hashmap m value ayegi
//name is sending as a key in map from view to backend
@Controller
public class BankController {
    @Autowired
    private NewUsersService usersService;
    @Autowired
    private BankService bankService;
    @Autowired
    private UserAccountRepo userAccountRepo;

    @RequestMapping("/home")
    public String homeNavbar() {
        return "HomePage.html";
    }

    @RequestMapping("/newUserDetails")
    public String NewUserDetails(Model model) {
        try {
            model.addAttribute("userData", new NewUserDetails());
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
    public String saveNewUserDetails(NewUserDetails newUserDetails, RedirectAttributes redirectAttributes) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map = usersService.saveUserPersonalDetails(newUserDetails);
            if (map.get("status") == "error") {
                redirectAttributes.addFlashAttribute("status", "Something went wrong");
                return "redirect:/newUserDetails";
            } else {
                redirectAttributes.addFlashAttribute("userData", map);
                return "redirect:/userAccountDetails";
            }
        } catch (Exception ex) {
            map.put("status", "error");
            ex.printStackTrace();
            return "redirect:/newUserDetails";
        }
    }

    @PostMapping("/saveUserAccountDetails")
    public String saveUserAccountDetails(@RequestParam Map<String, String> userAccountInfo, RedirectAttributes redirectAttributes) {
        HashMap<String, String> responseMap = new HashMap<>();
        try {
            responseMap = usersService.saveUserAccountInformation(userAccountInfo);
            if (responseMap.get("status") == "success") {
                redirectAttributes.addFlashAttribute("msg", "Account Created Successfully");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("msg", "Something went wrong !! Please Try Again");
                return "redirect:/newUserDetails";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/newUserDetails";
        }
    }

    @RequestMapping("/accountDetails")
    public String accountDetails(Model model, RedirectAttributes ra) {
        HashMap responseMap = bankService.fetchUserAccountDetails();
        if (responseMap.get("status") == "Account Not Found") {
            ra.addFlashAttribute("msg", "User Account Not Found");
            return "redirect:/home";
        } else {
            model.addAttribute("accountDetails", responseMap);
            return "AccountDetails.html";
        }
    }

    @RequestMapping("/userStatements")
    public String userStatements(Model model) {
        try {
            HashMap map = bankService.fetchUserTransactionHistory("23917579341");
            if (map.get("status") == "success") {
                model.addAttribute("statements", map.get("transactionList"));
            }else{
                model.addAttribute("msg2","Something went wrong while fetching the statements . Please Try Again");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Statements.html";
    }

    @RequestMapping("/addBeneficiary")
    public String addNewBeneficiary(Model model) {
        model.addAttribute("beneficiary", new Beneficiaries());
        return "NewBeneficiary.html";
    }

    @RequestMapping("/beneficiaryList")
    public String beneficiariesList(Model model) {
        Map responseMap = bankService.fetchAllBeneficiaries("23917579341");
        if (responseMap.get("status") == "success") {
            model.addAttribute("beneficiariesList", responseMap);
            return "BeneficiariesList.html";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/saveNewBeneficiary")
    public String saveNewBeneficiary(RedirectAttributes redirectAttributes, @RequestParam HashMap beneficiaryData) {
        try {
            HashMap map = bankService.addBeneficiary("23917579341", beneficiaryData);
            if (map.get("status") == "success") {
                redirectAttributes.addFlashAttribute("msg", "Beneficiary Added Successfully");
                return "redirect:/beneficiaryList";
            } else {
                redirectAttributes.addFlashAttribute("msg", "Something went wrong !! Please try again");
                return "redirect:/addBeneficiary";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/addBeneficiary";
        }
    }

    @RequestMapping("/transactionLimit")
    public String transactionLimitTemplate(Model model){
        model.addAttribute("transactionLimit",userAccountRepo.findTransactionLimitOfUser("23917579341"));
        return "TransactionLimit.html";
    }

    @PostMapping("/updateTransactionLimit")
    public String updateTransactionLimit(@RequestParam HashMap transactionLimit, RedirectAttributes ra) {
        try {
            HashMap responseMap = bankService.updateTransactionLimit(transactionLimit, "23917579341");
            if(responseMap.get("status") == "success"){
                ra.addFlashAttribute("msg","Your Transaction Limit has been updated successfully");
            }else{
                ra.addFlashAttribute("msg2","Something went wrong !! Please try again");
            }
            return "redirect:/transactionLimit";
        }catch(Exception ex){
            ex.printStackTrace();
            return "redirect:/home";
        }
    }

    @RequestMapping("/donateMoney")
    public String donateMoney(Model model){
        model.addAttribute("currentBalance", userAccountRepo.findDepositAmountOfUser("23917579341"));
        return "DonateMoney.html";

    }

    @PostMapping("/sendDonation")
    public String donateToPMCareFund(@RequestParam HashMap<String,String> donationAmount, RedirectAttributes ra){
        try{
            HashMap responseMap = bankService.donateToPMCareFund(donationAmount,"23917579341");
            if(responseMap.get("status") == "success"){
                ra.addFlashAttribute("msg","Thank You For Your Contribution");
            } else if(responseMap.get("status") == "amountError"){
                ra.addFlashAttribute("msg2","Your current account doesn't have enough money to donate");
            }else{
                ra.addFlashAttribute("msg2","Something went wrong !! Please try again");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "redirect:/donateMoney";
    }

    @PostMapping("/transferMoney")
    public String transferMoney(@RequestParam HashMap transferMoney, RedirectAttributes ra){
        try{
            HashMap responseMap = bankService.transferMoney(transferMoney, "23917579341");
            if(responseMap.get("status") == "success"){
                ra.addFlashAttribute("msg","Transaction Successful");
            }else if(responseMap.get("status") == "amountError"){
                ra.addFlashAttribute("msg2","Your current account doesn't have enough money to transfer");
            }else if(responseMap.get("status") == "limitError"){
                ra.addFlashAttribute("msg2","Please upgrade your transaction Limit");
            }else{
                ra.addFlashAttribute("msg2","Something went wrong !! Please try again.");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            ra.addFlashAttribute("msg2","Something went wrong !! Please try again.");
        }
        return "redirect:/beneficiaryList";
    }

    @RequestMapping("/recharge")
    public String rechargeAndPay(){
        return "Recharge.html";
    }

    @RequestMapping("/userProfile")
    public String userProfile(Model model){

        HashMap responseMap = bankService.fetchUserProfileDetails("23917579341");
        model.addAttribute("userAccountDetails", responseMap.get("userAccountDetails"));
        model.addAttribute("userPersonalDetails", responseMap.get("userPersonalDetails"));
        return "Profile.html";

    }

    @RequestMapping("/investments")
    public String investments(){
        return "Investments.html";
    }
}
