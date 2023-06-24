package com.example.internet_banking.Services;

import com.example.internet_banking.Entities.Beneficiaries;
import com.example.internet_banking.Entities.Transactions;
import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Repositories.BeneficiaryRepo;
import com.example.internet_banking.Repositories.NewUserRepo;
import com.example.internet_banking.Repositories.TransactionsRepo;
import com.example.internet_banking.Repositories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Configuration
public class BankService{
    @Autowired
    private NewUserRepo userDetailsRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private TransactionsRepo transRepo;
    @Autowired
    private BeneficiaryRepo beneficiaryRepo;

    public HashMap<String , String> fetchUserAccountDetails(){
        HashMap<String,String> map = new HashMap<>();
        try{
            UserAccountInfo account = userAccountRepo.findByAccountNumber("sndd28302");
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

    public HashMap saveUserTransaction(String userAccountNumber , HashMap transDetails){
        Transactions transactions = new Transactions();
        HashMap responseMap = new HashMap<>();
        try{
            transactions.setTransactionAmount((BigDecimal) transDetails.get("transactionAmount"));
            transactions.setTransactionDate(new Date());
            transactions.setTransDescription((String) transDetails.get("description"));
            UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(userAccountNumber);
            transactions.setUserTransactions(userAccount);
            transactions = transRepo.save(transactions);
            responseMap.put("status","success");
            responseMap.put("transactionId",transactions.getTransactionId());
        }catch (Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }
    public HashMap<String,List> fetchUserTransactionHistory(String userAccountNumber){
       HashMap<String,List> responseMap = new HashMap<>();
       try{
           UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(userAccountNumber);
           List<Transactions> transactionsList = transRepo.findAllTransactionsByUserAccount(userAccount);
           responseMap.put("transactionList",transactionsList);
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return responseMap;
    }

    public HashMap<String ,String> addBeneficiary(String userAccountNo, HashMap<String,String> beneficiaryMap){
        Beneficiaries beneficiary = new Beneficiaries();
        HashMap<String,String> responseMap = new HashMap<>();
        try{
            UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(userAccountNo);
            beneficiary.setBeneficiaryName(beneficiaryMap.get("beneficiaryName"));
            beneficiary.setBeneficiaryAccountNo(beneficiaryMap.get("beneficiaryAccountNo"));
            beneficiary.setUserAccounts(userAccount);
            beneficiary = beneficiaryRepo.save(beneficiary);
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return  responseMap;
    }

    public HashMap fetchAllBeneficiaries(String userAccountNumber){
        List<Beneficiaries> beneficiaries = new ArrayList<>();
        HashMap responseMap = new HashMap<>();
        try{
            UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(userAccountNumber);
            beneficiaries = beneficiaryRepo.fetchAllByUserAccount(userAccount);
            responseMap.put("beneficiaries", beneficiaries);
            responseMap.put("status","success");
        }catch (Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }
}
