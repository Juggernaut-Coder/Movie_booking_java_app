package validityChecks;

import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import connect_mysql.Mysql;

public class ValidityChecks extends Mysql {

    public ValidityChecks() {}

    public boolean check_if_unique(String email, String phone, String username) throws Exception {
        String mysql_unique = "SELECT * from booking_login where email = ? or phone = ? or username = ?";
        try{
            connect = connect_db();
            preparedQuery(mysql_unique,email,phone,username);
            if(result.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public boolean check_if_unique(String str, String columnName) throws Exception {
        String mysql_unique;
        switch(columnName) {
            case "phone":
                mysql_unique = "SELECT * from booking_login where phone = ?";
                break;
            case "email":
                mysql_unique = "SELECT * from booking_login where email = ?";
                break;
            default:
                throw new IllegalArgumentException("ColumnName not valid");
        }
        try {
            connect = connect_db();
            preparedQuery(mysql_unique,str);
            if(result.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public boolean check_if_string_is_int(String phone) {
        try {
            Integer.parseInt(phone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean is_email_valid(String email) {
        //regex string taken from OWASP Validation Regex repository
        final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                                  "[a-zA-Z0-9_+&*-]+)*@" +
                                  "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                  "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }
    
}
