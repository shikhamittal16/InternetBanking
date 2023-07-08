package com.example.internet_banking.DAO;

import com.example.internet_banking.Entities.Beneficiaries;
import com.example.internet_banking.Entities.Transactions;
import com.example.internet_banking.Entities.UserAccountInfo;
import com.example.internet_banking.Entities.UserPersonalDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void saveUserPersonalDetails(UserPersonalDetails userPersonalDetails){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(userPersonalDetails);
        session.flush();
        session.getTransaction().commit();
    }

    public void saveUserAccountInformation(UserAccountInfo userAccountInfo){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(userAccountInfo);
        session.flush();
        session.getTransaction().commit();
    }

    public UserPersonalDetails getUserPersonalDetails(Long userId){
        Session session = sessionFactory.getCurrentSession();
        UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
        try{
            userPersonalDetails = (UserPersonalDetails) session.createQuery("SELECT u FROM UserPersonalDetails u WHERE u.id =:userId")
                    .setParameter("userId",userId).uniqueResult();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return userPersonalDetails;
    }

    public UserAccountInfo getUserAccountDetailsByAccountNumber(String accountNo){
        Session session = sessionFactory.getCurrentSession();
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        try{
            userAccountInfo = (UserAccountInfo) session.createQuery("SELECT u FROM UserAccountInfo u WHERE u.accountNo =:accountNo")
                    .setParameter("accountNo",accountNo).uniqueResult();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return userAccountInfo;
    }

    public void saveUserTransactions(Transactions transactions){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(transactions);
        session.flush();
        session.getTransaction().commit();
    }

    public List<Transactions> findAllTransactionsByUserAccountNo(UserAccountInfo accountInfo){
        Session session = sessionFactory.getCurrentSession();
        List transactions = new ArrayList<>();
        try{
            transactions = session.createQuery("SELECT t FROM Transactions t WHERE t.userTransactions =:userAccount")
                    .setParameter("userAccount",accountInfo).list();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return transactions;
    }

    public void saveUserBeneficiaryDetails(Beneficiaries beneficiary){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(beneficiary);
        session.flush();
        session.getTransaction().commit();
    }

    public List fetchAllBeneficiaries(UserAccountInfo userAccountInfo){
        Session sx = sessionFactory.getCurrentSession();
        List beneficiaries = new ArrayList();
        try{
            beneficiaries = sx.createQuery("SELECT b FROM Beneficiaries b WHERE b.userAccounts =:userAccount")
                    .setParameter("userAccount",userAccountInfo).list();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return beneficiaries;
    }
}
