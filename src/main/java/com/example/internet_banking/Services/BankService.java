package com.example.internet_banking.Services;

import com.example.internet_banking.DAO.UsersDAO;
import com.example.internet_banking.Entities.Beneficiaries;
import com.example.internet_banking.Entities.Transactions;
import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Configuration
public class BankService{
    @Autowired
    private UsersDAO usersDAO;

    public HashMap<String , String> fetchUserAccountDetails(){
        HashMap<String,String> map = new HashMap<>();
        try{
            UserAccountInfo account = usersDAO.getUserAccountDetailsByAccountNumber("95352479341");
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
            UserAccountInfo userAccount = usersDAO.getUserAccountDetailsByAccountNumber(userAccountNumber);
            transactions.setUserTransactions(userAccount);
            usersDAO.saveUserTransactions(transactions);
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
           UserAccountInfo userAccount = usersDAO.getUserAccountDetailsByAccountNumber(userAccountNumber);
           List<Transactions> transactionsList = usersDAO.findAllTransactionsByUserAccountNo(userAccount);
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
            UserAccountInfo userAccount = usersDAO.getUserAccountDetailsByAccountNumber(userAccountNo);
            beneficiary.setBeneficiaryName(beneficiaryMap.get("beneficiaryName"));
            beneficiary.setBeneficiaryAccountNo(beneficiaryMap.get("beneficiaryAccountNo"));
            beneficiary.setUserAccounts(userAccount);
            usersDAO.saveUserBeneficiaryDetails(beneficiary);
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return  responseMap;
    }

    public HashMap fetchAllBeneficiaries(String userAccountNumber){
        HashMap responseMap = new HashMap<>();
        try{
            UserAccountInfo userAccount = usersDAO.getUserAccountDetailsByAccountNumber(userAccountNumber);
            List<Beneficiaries> beneficiaries = usersDAO.fetchAllBeneficiaries(userAccount);
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
            UserAccountInfo userAccount = usersDAO.getUserAccountDetailsByAccountNumber(accountNo);
            userAccount.setTransactionLimit(new BigDecimal(transactionLimitMap.get("transactionLimit")));
            usersDAO.saveUserAccountInformation(userAccount);
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
            UserAccountInfo userAccountInfo = usersDAO.getUserAccountDetailsByAccountNumber(accountNo);
            BigDecimal currentAmt = userAccountInfo.getDepositBalance();
            BigDecimal donationAmt = new BigDecimal(donationMoney.get("donationAmount"));
            if(currentAmt.compareTo(donationAmt) < 0){
                respMap.put("status","amountError");
            } else{
                userAccountInfo.setDepositBalance(currentAmt.subtract(donationAmt));
                usersDAO.saveUserAccountInformation(userAccountInfo);
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
            UserAccountInfo userAccountInfo = usersDAO.getUserAccountDetailsByAccountNumber(accountNo);
            List<Beneficiaries> beneficiaries = usersDAO.fetchAllBeneficiaries(userAccountInfo);
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
                        UserAccountInfo beneficiaryAcc = usersDAO.getUserAccountDetailsByAccountNumber(beneficiaries.get(i).getBeneficiaryAccountNo());
                        if(beneficiaryAcc != null){
                            beneficiaryAcc.setDepositBalance(
                                    beneficiaryAcc.getDepositBalance().add
                                            (transferAmt));
                            usersDAO.saveUserAccountInformation(beneficiaryAcc);
                            setTransactionDetailsOfUser("deposit",beneficiaryAcc,transferAmt,userAccountInfo.getUserDetails().getFullName());
                        }
                        userAccountInfo.setDepositBalance(userAccountInfo.getDepositBalance().
                                subtract(transferAmt));
                        userAccountInfo.setWithdrawableBalance(userAccountInfo.getWithdrawableBalance().
                                subtract(transferAmt));
                        usersDAO.saveUserAccountInformation(userAccountInfo);
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
            usersDAO.saveUserTransactions(transactions);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public HashMap fetchUserProfileDetails(String accountNo){
        HashMap responseMap = new HashMap();
        try{
            UserAccountInfo userAccountInfo = usersDAO.getUserAccountDetailsByAccountNumber(accountNo);
            responseMap.put("userAccountDetails", userAccountInfo);
            responseMap.put("userPersonalDetails", userAccountInfo.getUserDetails());
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }

    public BigDecimal findTransactionLimitOfUser(String accountNo){
        UserAccountInfo userAccountInfo = usersDAO.getUserAccountDetailsByAccountNumber(accountNo);
        return userAccountInfo.getTransactionLimit();
    }

    public BigDecimal findDepositAmountOfUser(String accountNO){
        UserAccountInfo userAccountInfo = usersDAO.getUserAccountDetailsByAccountNumber(accountNO);
        return userAccountInfo.getDepositBalance();
    }
}
