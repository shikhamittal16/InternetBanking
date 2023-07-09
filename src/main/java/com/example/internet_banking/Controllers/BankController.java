package com.example.internet_banking.Controllers;

import com.example.internet_banking.Entities.Beneficiaries;
import com.example.internet_banking.Services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//th:field = agar hum direct object m value pass kra rhe hai to jb use krenge like saveNewUserDetails m
//th:value = jb hum @requestParam use kr rhe hai to hashmap m value ayegi
//name is sending as a key in map from view to backend
@Controller
public class BankController {
    @Autowired
    private BankService bankService;

    @RequestMapping("/home")
    public String homeNavbar() {
        return "HomePage.html";
    }

    @RequestMapping("/accountDetails")
    public String accountDetails(Model model) {
        HashMap<String, String> responseMap = bankService.fetchUserAccountDetails();
        if (Objects.equals(responseMap.get("status"), "Account Not Found")) {
            return "redirect:/login";
        } else if(Objects.equals(responseMap.get("status"), "error")){
            return "redirect:/home";
        } else {
            model.addAttribute("accountDetails", responseMap.get("accountDetails"));
            model.addAttribute("CurrentAccount", responseMap.get("CurrentAccount"));
            model.addAttribute("SavingAccount", responseMap.get("SavingAccount"));
            model.addAttribute("FixedDepositAccount", responseMap.get("FixedDepositAccount"));
            model.addAttribute("RecurringDepositAccount", responseMap.get("RecurringDepositAccount"));
            return "AccountDetails.html";
        }
    }

    @RequestMapping("/userStatements")
    public String userStatements(Model model) {
        try {
            HashMap map = bankService.fetchUserTransactionHistory();
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
        Map responseMap = bankService.fetchAllBeneficiaries();
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

            HashMap map = bankService.addBeneficiary(beneficiaryData);
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
        model.addAttribute("transactionLimit",bankService.findTransactionLimitOfUser());
        return "TransactionLimit.html";
    }

    @PostMapping("/updateTransactionLimit")
    public String updateTransactionLimit(@RequestParam HashMap transactionLimit, RedirectAttributes ra) {
        try {
            HashMap responseMap = bankService.updateTransactionLimit(transactionLimit);
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
        model.addAttribute("currentBalance", bankService.findDepositAmountOfUser());
        return "DonateMoney.html";

    }

    @PostMapping("/sendDonation")
    public String donateToPMCareFund(@RequestParam HashMap<String,String> donationAmount, RedirectAttributes ra){
        try{
            HashMap responseMap = bankService.donateToPMCareFund(donationAmount);
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
            HashMap responseMap = bankService.transferMoney(transferMoney);
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

        HashMap responseMap = bankService.fetchUserProfileDetails();
        model.addAttribute("userAccountDetails", responseMap.get("userAccountDetails"));
        model.addAttribute("userPersonalDetails", responseMap.get("userPersonalDetails"));
        return "Profile.html";

    }

    @RequestMapping("/investments")
    public String investments(){
        return "Investments.html";
    }
}
