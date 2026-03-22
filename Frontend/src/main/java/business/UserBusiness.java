package business;

import helper.UserInfo;
import persistence.User_CRUD;

public class UserBusiness {

    public UserInfo login(String username, String password) {
        return User_CRUD.login(username, password);
    }

    public boolean register(String username, String password, String email) {
        return User_CRUD.register(username, password, email);
    }
}
