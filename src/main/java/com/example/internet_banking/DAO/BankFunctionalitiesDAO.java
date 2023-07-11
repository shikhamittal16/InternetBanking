package com.example.internet_banking.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BankFunctionalitiesDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public Boolean checkWhetherUserNameExist(String userName){
        Session sx = sessionFactory.getCurrentSession();
        Boolean isExist = false;
        try{
            List userAccounts = sx.createQuery("SELECT u.id FROM UserAccountInfo u WHERE u.loginUserName =:loginUser")
                    .setParameter("loginUser",userName).setMaxResults(1).list();
            if(userAccounts != null && userAccounts.size() > 0){
                isExist = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return isExist;
    }

    public Boolean checkWhetherAccountNoExist(String accountNo){
        Session sx = sessionFactory.getCurrentSession();
        Boolean isExist = false;
        try{
            List userAccounts = sx.createQuery("SELECT u.id FROM UserAccountInfo u WHERE u.accountNo =:accountNo")
                    .setParameter("accountNo",accountNo).setMaxResults(1).list();
            if(userAccounts != null && userAccounts.size() > 0){
                isExist = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return isExist;
    }

    public Boolean checkWhetherAccountAlreadyExist(String aadharNo , String accountType){
        Session sx = sessionFactory.getCurrentSession();
        Boolean isExist = false;
        try{
            List userAccounts = sx.createQuery("SELECT u.id FROM UserAccountInfo u WHERE u.userDetails.aadhar =:aadhar and u.userDetails.AccountType =:accountType")
                    .setParameter("aadhar",aadharNo).setParameter("accountType",accountType)
                    .setMaxResults(1).list();
            if(userAccounts != null && userAccounts.size() > 0){
                isExist = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return isExist;
    }
}
