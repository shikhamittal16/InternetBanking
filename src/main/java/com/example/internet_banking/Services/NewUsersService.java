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
import java.util.Random;

@Configuration
public class NewUsersService {
    @Autowired
    private NewUserRepo userDetailsRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;

    public HashMap<String ,String > saveUserPersonalDetails(NewUserDetails newUserDetails){
        HashMap<String , String> responseMap = new HashMap<>();
        Random random = new Random();
        try{
            NewUserDetails details = userDetailsRepo.save(newUserDetails);
            responseMap.put("status","success");
            responseMap.put("userId",details.getId().toString());
            String accountNo = (random.nextInt(900000) + 100000 )+ details.getAadhar().substring(0,5);
            responseMap.put("accountNo",accountNo);
            responseMap.put("amount","");
            responseMap.put("loginId","");
            responseMap.put("password","");
            responseMap.put("currency","");
        }catch (Exception ex){
            ex.printStackTrace();
            responseMap.put("status","error");
        }
        return responseMap;
    }

    public HashMap<String, String > saveUserAccountInformation(Map<String ,String > userAccountInfo){
        HashMap<String , String> responseMap = new HashMap<>();
        UserAccountInfo accountInfo = new UserAccountInfo();
        Random random = new Random();
        try{
            Optional<NewUserDetails> userDetailsOpt = userDetailsRepo.findById(Long.parseLong(userAccountInfo.get("userId")));
            NewUserDetails userDetails = userDetailsOpt.orElse(null);
            accountInfo.setAccountNo(userAccountInfo.get("accountNo"));
            accountInfo.setUserDetails(userDetails);
            accountInfo.setPassword(userAccountInfo.get("password"));
            accountInfo.setLoginUserName(userAccountInfo.get("loginUserName"));
            accountInfo.setDepositBalance(new BigDecimal(userAccountInfo.get("depositBalance")));
            accountInfo.setCurrency(userAccountInfo.get("currency"));
            accountInfo.setWithdrawableBalance(new BigDecimal(userAccountInfo.get("depositBalance")));
            accountInfo.setCrnName(userDetails.getFullName());
            accountInfo.setCrnNumber(random.nextInt(90000) + 10000 + userDetails.getAadhar().substring(4,7));
            accountInfo.setTransactionLimit(BigDecimal.valueOf(5000));
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
