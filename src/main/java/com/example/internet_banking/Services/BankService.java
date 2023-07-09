package com.example.internet_banking.Services;

import com.example.internet_banking.DAO.LoginDAO;
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
    @Autowired
    private LoginDAO loginDAO;
    private Long currentUserId;
    private UserAccountInfo currentUserAccount;

    public HashMap fetchUserAccountDetails(){
        HashMap map = new HashMap();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            if(currentUserAccount != null){
                map.put("accountDetails",fetchCurrentAccountDetails());
                List allAccountsList = usersDAO.fetchTotalAccountDetailsOfCurrentUser(currentUserAccount.getUserDetails().getAadhar());
                for (int i=0 ; i<allAccountsList.size() ; i++){
                    HashMap tempMap = new HashMap();
                    UserAccountInfo currentAccount = (UserAccountInfo) allAccountsList.get(i);
                    tempMap.put("depositBalance",currentAccount.getDepositBalance().toString());
                    tempMap.put("crnId",currentAccount.getCrnNumber());
                    tempMap.put("crnName",currentAccount.getCrnName());
                    tempMap.put("currency",currentAccount.getCurrency());
                    tempMap.put("custName",currentAccount.getUserDetails().getFullName());
                    tempMap.put("accountNo",currentAccount.getAccountNo());
                    tempMap.put("crnNo",currentAccount.getCrnNumber());
                    tempMap.put("accountType",currentAccount.getUserDetails().getAccountType());
                    tempMap.put("withdrawableBalance",currentAccount.getWithdrawableBalance()!=null ? currentAccount.getWithdrawableBalance().toString() : "");
                    map.put(currentAccount.getUserDetails().getAccountType(), tempMap);
                }
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

    public HashMap saveUserTransaction(HashMap transDetails){
        Transactions transactions = new Transactions();
        HashMap responseMap = new HashMap<>();
        try{
            transactions.setTransactionAmount((BigDecimal) transDetails.get("transactionAmount"));
            transactions.setTransactionDate(new Date());
            transactions.setTransDescription((String) transDetails.get("description"));
            transactions.setUserTransactions(currentUserAccount);
            usersDAO.saveUserTransactions(transactions);
            responseMap.put("status","success");
            responseMap.put("transactionId",transactions.getTransactionId());
        }catch (Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }
    public HashMap fetchUserTransactionHistory(){
       HashMap responseMap = new HashMap();
       try{
           if(currentUserAccount == null){
               currentUserId = loginDAO.fetchCurrentLoginUser();
               currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
           }
           List<Transactions> transactionsList = usersDAO.findAllTransactionsByUserAccountNo(currentUserAccount);
           responseMap.put("transactionList",transactionsList);
           responseMap.put("status","success");
       }catch(Exception ex){
           responseMap.put("status","error");
           ex.printStackTrace();
       }
       return responseMap;
    }

    public HashMap<String ,String> addBeneficiary(HashMap<String,String> beneficiaryMap){
        Beneficiaries beneficiary = new Beneficiaries();
        HashMap<String,String> responseMap = new HashMap<>();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            beneficiary.setBeneficiaryName(beneficiaryMap.get("beneficiaryName"));
            beneficiary.setBeneficiaryAccountNo(beneficiaryMap.get("beneficiaryAccountNo"));
            beneficiary.setUserAccounts(currentUserAccount);
            usersDAO.saveUserBeneficiaryDetails(beneficiary);
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return  responseMap;
    }

    public HashMap fetchAllBeneficiaries(){
        HashMap responseMap = new HashMap<>();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            List<Beneficiaries> beneficiaries = usersDAO.fetchAllBeneficiaries(currentUserAccount);
            responseMap.put("beneficiaries", beneficiaries);
            responseMap.put("status","success");
        }catch (Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }

    public HashMap updateTransactionLimit( HashMap<String,String> transactionLimitMap){
        HashMap responseMap = new HashMap();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            currentUserAccount.setTransactionLimit(new BigDecimal(transactionLimitMap.get("transactionLimit")));
            usersDAO.saveUserAccountInformation(currentUserAccount);
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }

    public HashMap donateToPMCareFund(HashMap<String,String> donationMoney){
        HashMap respMap = new HashMap();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            BigDecimal currentAmt = currentUserAccount.getDepositBalance();
            BigDecimal donationAmt = new BigDecimal(donationMoney.get("donationAmount"));
            if(currentAmt.compareTo(donationAmt) < 0){
                respMap.put("status","amountError");
            } else{
                currentUserAccount.setDepositBalance(currentAmt.subtract(donationAmt));
                usersDAO.saveUserAccountInformation(currentUserAccount);
                setTransactionDetailsOfUser("withdrawal",currentUserAccount, donationAmt, "Donate to PM-Cares Fund");
                respMap.put("status","success");
             }
        }catch(Exception ex){
            respMap.put("status","error");
            ex.printStackTrace();
        }
        return respMap;
    }

    public HashMap transferMoney(HashMap transferMoney){
        HashMap responseMap = new HashMap();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            List<Beneficiaries> beneficiaries = usersDAO.fetchAllBeneficiaries(currentUserAccount);
            for(int i=0 ; i<beneficiaries.size() ; i++){
                Long beneficiaryId = beneficiaries.get(i).getBeneficiaryId();
                if(transferMoney.containsKey("beneficiary-"+beneficiaryId) &&
                        transferMoney.get("beneficiary-"+beneficiaryId) != null &&
                        transferMoney.get("beneficiary-"+beneficiaryId) != ""){
                    BigDecimal transferAmt = new BigDecimal(transferMoney.get("beneficiary-"+beneficiaryId).toString());
                    if(transferAmt.compareTo(currentUserAccount.getDepositBalance())>0){
                        responseMap.put("status","amountError");
                    } else if(transferAmt.compareTo(currentUserAccount.getTransactionLimit())>0){
                        responseMap.put("status","limitError");
                    } else {
                        UserAccountInfo beneficiaryAcc = usersDAO.getUserAccountDetailsByAccountNumber(beneficiaries.get(i).getBeneficiaryAccountNo());
                        if(beneficiaryAcc != null){
                            beneficiaryAcc.setDepositBalance(
                                    beneficiaryAcc.getDepositBalance().add
                                            (transferAmt));
                            usersDAO.saveUserAccountInformation(beneficiaryAcc);
                            setTransactionDetailsOfUser("deposit",beneficiaryAcc,transferAmt,currentUserAccount.getUserDetails().getFullName());
                        }
                        currentUserAccount.setDepositBalance(currentUserAccount.getDepositBalance().
                                subtract(transferAmt));
                        currentUserAccount.setWithdrawableBalance(currentUserAccount.getWithdrawableBalance().
                                subtract(transferAmt));
                        usersDAO.saveUserAccountInformation(currentUserAccount);
                        setTransactionDetailsOfUser("withdrawal",currentUserAccount,transferAmt,beneficiaryAcc.getUserDetails().getFullName());
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

    public HashMap fetchUserProfileDetails(){
        HashMap responseMap = new HashMap();
        try{
            if(currentUserAccount == null){
                currentUserId = loginDAO.fetchCurrentLoginUser();
                currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
            }
            responseMap.put("userAccountDetails", currentUserAccount);
            responseMap.put("userPersonalDetails", currentUserAccount.getUserDetails());
            responseMap.put("status","success");
        }catch(Exception ex){
            responseMap.put("status","error");
            ex.printStackTrace();
        }
        return responseMap;
    }

    public BigDecimal findTransactionLimitOfUser(){
        if(currentUserAccount == null){
            currentUserId = loginDAO.fetchCurrentLoginUser();
            currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
        }
        return currentUserAccount.getTransactionLimit();
    }

    public BigDecimal findDepositAmountOfUser(){
        if(currentUserAccount == null){
            currentUserId = loginDAO.fetchCurrentLoginUser();
            currentUserAccount = usersDAO.getUserAccountDetailsByAccountId(currentUserId);
        }
        return currentUserAccount.getDepositBalance();
    }

    public HashMap fetchCurrentAccountDetails(){
        HashMap map = new HashMap();
        map.put("currency",currentUserAccount.getCurrency());
        map.put("depositBalance",currentUserAccount.getDepositBalance());
        map.put("custName",currentUserAccount.getUserDetails().getFullName());
        map.put("crnNo",currentUserAccount.getCrnNumber());
        map.put("crnName",currentUserAccount.getCrnName());
        return map;
    }
}
