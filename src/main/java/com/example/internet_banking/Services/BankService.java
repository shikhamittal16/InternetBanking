package com.example.internet_banking.Services;

import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Repositories.NewUserRepo;
import com.example.internet_banking.Repositories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class BankService{
    @Autowired
    private NewUserRepo userDetailsRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;

    public HashMap<String , String> fetchUserAccountDetails(){
        HashMap<String,String> map = new HashMap<>();
        try{
            UserAccountInfo account = userAccountRepo.findById("sndd28302").orElse(null) ;
            map.put("depositBalance",account.getDepositBalance().toString());
            map.put("crnId",account.getCrnNumber());
            map.put("crnName",account.getCrnName());
            map.put("currency",account.getCurrency());
            map.put("custName",account.getUserDetails().getFullName());
            map.put("accountNo","sndd28302");
            map.put("accountType",account.getUserDetails().getAccountType());
            map.put("withdrawableBalance",account.getWithdrawableBalance()!=null ? account.getWithdrawableBalance().toString() : "");
            map.put("status","success");
        }catch(Exception ex){
            map.put("status","error");
            ex.printStackTrace();
        }
        return map ;
    }
}
