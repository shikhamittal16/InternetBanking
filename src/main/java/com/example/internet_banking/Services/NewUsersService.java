package com.example.internet_banking.Services;

import com.example.internet_banking.Entities.NewUserDetails;
import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Repositories.NewUserRepo;
import com.example.internet_banking.Repositories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class NewUsersService {
    @Autowired
    private NewUserRepo userDetailsRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;

    public HashMap<String ,String > saveUserPersonalDetails(NewUserDetails newUserDetails){
        HashMap<String , String> responseMap = new HashMap<>();
        try{
            NewUserDetails details = userDetailsRepo.save(newUserDetails);
            responseMap.put("status","success");
            responseMap.put("userId",details.getId().toString());
            String accountNo = details.getBankName().substring(0,3)+details.getPanCardNumber().substring(0,4)+details.getDOB().substring(8,10);
            responseMap.put("accountNo",accountNo);
            responseMap.put("amount","");
            responseMap.put("loginId","");
            responseMap.put("password","");
        }catch (Exception ex){
            ex.printStackTrace();
            responseMap.put("status","error");
        }
        return responseMap;
    }

    public HashMap<String, String > saveUserAccountInformation(Map<String ,String > userAccountInfo){
        HashMap<String , String> responseMap = new HashMap<>();
        UserAccountInfo accountInfo = new UserAccountInfo();
        try{
            Optional<NewUserDetails> userDetailsOpt = userDetailsRepo.findById(Long.parseLong(userAccountInfo.get("userId")));
            NewUserDetails userDetails = userDetailsOpt.orElse(null);
            accountInfo.setAccountNo(userAccountInfo.get("accountNo"));
            accountInfo.setUserDetails(userDetails);
            accountInfo.setPassword(userAccountInfo.get("password"));
            accountInfo.setLoginUserName(userAccountInfo.get("loginUserName"));
            accountInfo.setDepositBalance(new BigDecimal(userAccountInfo.get("depositBalance")));
            accountInfo.setCrnName(userDetails.getFullName());
            userAccountRepo.save(accountInfo);
            userDetails.setAccountInfo(accountInfo);
            userDetailsRepo.save(userDetails);
            responseMap.put("status","success");
        }catch (Exception ex){
           ex.printStackTrace();
           responseMap.put("status","error");
        }
        return responseMap;
    }
}
