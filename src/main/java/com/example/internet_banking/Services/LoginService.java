package com.example.internet_banking.Services;

import com.example.internet_banking.DAO.LoginDAO;
import com.example.internet_banking.DAO.UsersDAO;
import com.example.internet_banking.Entities.LoginUser;
import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginService {
    @Autowired
    private UsersDAO usersDao;
    @Autowired
    private LoginDAO loginDAO;

    public UserAccountInfo checkUserIsValidOrNot(String loginId , String password){
        UserAccountInfo userAccountInfo = usersDao.findUserByLoginIdAndPassword(loginId,password);
        if(userAccountInfo != null){
            LoginUser loginUser = new LoginUser();
            loginUser.setPassword(password);
            loginUser.setLoginUserName(loginId);
            loginUser.setLoginUserAccountId(userAccountInfo.getUserAccountId());
            loginDAO.saveLoginUserDetails(loginUser);
        }
        return userAccountInfo;
    }
}
