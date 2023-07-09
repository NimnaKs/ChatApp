package model;

import dto.Profile;
import util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public static boolean checkName(String userName) throws SQLException {

        ResultSet resultSet= CrudUtil.execute("SELECT * FROM profile WHERE f_name=?",userName);
        return resultSet.next();
    }

    public static boolean getActiveStatus(String userName) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM profile WHERE f_name=? and activeStatus=?",userName,false);
        return resultSet.next();
    }

    public static Profile getProfile(String userName) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT f_name,l_name,profilePhoto FROM profile WHERE f_name=?",userName);
        CrudUtil.execute("UPDATE profile SET activeStatus=? WHERE f_name=?",true,userName);
        if (resultSet.next()){
            return new Profile(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return null;
    }

    public static boolean userDeactivate(String fName) throws SQLException {
        return CrudUtil.execute("UPDATE profile SET activeStatus=false WHERE f_name=?",fName);
    }
}
