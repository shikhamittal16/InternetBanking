package com.example.internet_banking.DAO;

import com.example.internet_banking.Entities.LoginUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void saveLoginUserDetails(LoginUser loginUser){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(loginUser);
        session.flush();
        session.getTransaction().commit();
    }

    public Long fetchCurrentLoginUser(){
        Session sx = sessionFactory.getCurrentSession();
        Long id = (Long) sx.createQuery("SELECT u.loginUserAccountId from LoginUser u").setMaxResults(1).uniqueResult();
        return id;
    }
}
