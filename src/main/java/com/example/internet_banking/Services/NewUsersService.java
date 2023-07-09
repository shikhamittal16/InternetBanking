package com.example.internet_banking.Services;

import com.example.internet_banking.DAO.BankFunctionalitiesDAO;
import com.example.internet_banking.DAO.UsersDAO;
import com.example.internet_banking.Entities.UserPersonalDetails;
import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.*;

@Configuration
public class NewUsersService {
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private BankFunctionalitiesDAO bankFunctionalitiesDAO;

    public HashMap<String ,String > saveUserPersonalDetails(UserPersonalDetails newUserDetails){
        HashMap<String , String> responseMap = new HashMap<>();
        Random random = new Random();
        try{
            usersDAO.saveUserPersonalDetails(newUserDetails);
            responseMap.put("status","success");
            responseMap.put("userId",newUserDetails.getId().toString());
            String accountNo = (random.nextInt(900000) + 100000 )+ newUserDetails.getAadhar().substring(0,5);
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
            Boolean isUserNameExist = bankFunctionalitiesDAO.checkWhetherUserNameExist(userAccountInfo.get("loginUserName"));
            if(!isUserNameExist){
                UserPersonalDetails userDetails = usersDAO.getUserPersonalDetails(Long.parseLong(userAccountInfo.get("userId")));
                if(userDetails != null && userDetails.getId() != null){
                    accountInfo.setAccountNo(userAccountInfo.get("accountNo"));
                    accountInfo.setUserDetails(userDetails);
                    accountInfo.setPassword(userAccountInfo.get("password"));
                    accountInfo.setLoginUserName(userAccountInfo.get("loginUserName"));
                    accountInfo.setDepositBalance(new BigDecimal(userAccountInfo.get("depositBalance")));
                    accountInfo.setCurrency(userAccountInfo.get("currency"));
                    if(Objects.equals(userDetails.getAccountType(), "CurrentAccount")){
                        accountInfo.setWithdrawableBalance(new BigDecimal(userAccountInfo.get("depositBalance")));
                    }
                    else if(Objects.equals(userDetails.getAccountType(), "SavingAccount")){
                        accountInfo.setWithdrawableBalance(new BigDecimal(25000));
                    }
                    else {
                        accountInfo.setWithdrawableBalance(new BigDecimal(0));
                    }
                    accountInfo.setCrnName(userDetails.getFullName());
                    accountInfo.setCrnNumber(random.nextInt(90000) + 10000 + userDetails.getAadhar().substring(4,7));
                    accountInfo.setTransactionLimit(BigDecimal.valueOf(5000));
                    usersDAO.saveUserAccountInformation(accountInfo);
                    userDetails.setAccountInfo(accountInfo);
                    usersDAO.saveUserPersonalDetails(userDetails);
                    responseMap.put("status","success");
                } else{
                    responseMap.put("status","Cant find userDetails");
                }
            }else{
                responseMap.put("loginId","");
                responseMap.put("password","");
                responseMap.put("currency",userAccountInfo.get("currency"));
                responseMap.put("userId",userAccountInfo.get("userId"));
                responseMap.put("accountNo",userAccountInfo.get("accountNo"));
                responseMap.put("amount",userAccountInfo.get("depositBalance"));
                responseMap.put("status","UserName Already Exist");
            }
        }catch (Exception ex){
           ex.printStackTrace();
           responseMap.put("status","error");
        }
        return responseMap;
    }
}
