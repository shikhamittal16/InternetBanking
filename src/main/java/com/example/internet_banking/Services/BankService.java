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
            UserAccountInfo account = userAccountRepo.findByAccountNumber("23917579341");
            if(account != null){
                map.put("depositBalance",account.getDepositBalance().toString());
                map.put("crnId",account.getCrnNumber());
                map.put("crnName",account.getCrnName());
                map.put("currency",account.getCurrency());
                map.put("custName",account.getUserDetails().getFullName());
                map.put("accountNo",account.getAccountNo());
                map.put("crnNo",account.getCrnNumber());
                map.put("accountType",account.getUserDetails().getAccountType());
                map.put("withdrawableBalance",account.getWithdrawableBalance()!=null ? account.getWithdrawableBalance().toString() : "");
                map.put("status","success");
            }
            else{
                map.put("status","Account Not Found");
            }
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
    public HashMap fetchUserTransactionHistory(String userAccountNumber){
       HashMap responseMap = new HashMap();
       try{
           UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(userAccountNumber);
           List<Transactions> transactionsList = transRepo.findAllTransactionsByUserAccount(userAccount);
           responseMap.put("transactionList",transactionsList);
           responseMap.put("status","success");
       }catch(Exception ex){
           responseMap.put("status","error");
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

    public HashMap updateTransactionLimit( HashMap<String,String> transactionLimitMap, String accountNo){
        HashMap responseMap = new HashMap();
        try{
            UserAccountInfo userAccount = userAccountRepo.findByAccountNumber(accountNo);
            userAccount.setTransactionLimit(new BigDecimal(transactionLimitMap.get("transactionLimit")));
            userAccountRepo.save(userAccount);
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }

    public HashMap donateToPMCareFund(HashMap<String,String> donationMoney , String accountNo){
        HashMap respMap = new HashMap();
        try{
             BigDecimal currentAmt = userAccountRepo.findDepositAmountOfUser(accountNo);
             BigDecimal donationAmt = new BigDecimal(donationMoney.get("donationAmount"));
             if(currentAmt.compareTo(donationAmt) < 0){
                 respMap.put("status","amountError");
             } else{
                UserAccountInfo userAccountInfo = userAccountRepo.findByAccountNumber(accountNo);
                userAccountInfo.setDepositBalance(currentAmt.subtract(donationAmt));
                userAccountRepo.save(userAccountInfo);
                setTransactionDetailsOfUser("withdrawal",userAccountInfo, donationAmt, "Donate to PM-Cares Fund");
                respMap.put("status","success");
             }
        }catch(Exception ex){
            respMap.put("status","error");
            ex.printStackTrace();
        }
        return respMap;
    }

    public HashMap transferMoney(HashMap transferMoney , String accountNo){
        HashMap responseMap = new HashMap();
        try{
            UserAccountInfo userAccountInfo = userAccountRepo.findByAccountNumber(accountNo);
            List<Beneficiaries> beneficiaries = beneficiaryRepo.fetchAllByUserAccount(userAccountInfo);
            for(int i=0 ; i<beneficiaries.size() ; i++){
                Long beneficiaryId = beneficiaries.get(i).getBeneficiaryId();
                if(transferMoney.containsKey("beneficiary-"+beneficiaryId) &&
                        transferMoney.get("beneficiary-"+beneficiaryId) != null &&
                        transferMoney.get("beneficiary-"+beneficiaryId) != ""){
                    BigDecimal transferAmt = new BigDecimal(transferMoney.get("beneficiary-"+beneficiaryId).toString());
                    if(transferAmt.compareTo(userAccountInfo.getDepositBalance())>0){
                        responseMap.put("status","amountError");
                    } else if(transferAmt.compareTo(userAccountInfo.getTransactionLimit())>0){
                        responseMap.put("status","limitError");
                    } else {
                        UserAccountInfo beneficiaryAcc = userAccountRepo.findByAccountNumber(beneficiaries.get(i).getBeneficiaryAccountNo());
                        if(beneficiaryAcc != null){
                            beneficiaryAcc.setDepositBalance(
                                    beneficiaryAcc.getDepositBalance().add
                                            (transferAmt));
                            userAccountRepo.save(beneficiaryAcc);
                            setTransactionDetailsOfUser("deposit",beneficiaryAcc,transferAmt,userAccountInfo.getUserDetails().getFullName());
                        }
                        userAccountInfo.setDepositBalance(userAccountInfo.getDepositBalance().
                                subtract(transferAmt));
                        userAccountInfo.setWithdrawableBalance(userAccountInfo.getWithdrawableBalance().
                                subtract(transferAmt));
                        userAccountRepo.save(userAccountInfo);
                        setTransactionDetailsOfUser("withdrawal",userAccountInfo,transferAmt,beneficiaryAcc.getUserDetails().getFullName());
                        responseMap.put("status","success");
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            responseMap.put("status","error");
        }
        return responseMap;
    }

    public void setTransactionDetailsOfUser(String transactionType , UserAccountInfo user , BigDecimal amount , String transDesc){
        try{
            Transactions transactions = new Transactions();
            transactions.setTransDescription(transDesc);
            transactions.setTransactionAmount(amount);
            transactions.setUserTransactions(user);
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(transactionType);
            transRepo.save(transactions);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public HashMap fetchUserProfileDetails(String accountNo){
        HashMap responseMap = new HashMap();
        try{
            UserAccountInfo userAccountInfo = userAccountRepo.findByAccountNumber(accountNo);
            responseMap.put("userAccountDetails", userAccountInfo);
            responseMap.put("userPersonalDetails", userAccountInfo.getUserDetails());
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }
}
