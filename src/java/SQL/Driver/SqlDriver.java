/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class SqlDriver {
    private final String DATA_BASE_NAME;
    private String USER_NAME;
    private final String PASSWORD;
    private final String DATA_BASE_LINK;
    private final String DRIVER;
    private final String CHARSET;
    
//    private final String DRIVER_TYPE = "jdbc";
//    private final String TYPE_DATA_BASE = "mysql";
//    private final String SLASHES = "://";
//    private final boolean useSSL = false;
//    private final String CLASS_DRIVER = "com.mysql.cj.jdbc.Driver";
//    private  String USER_NAME = "root";
//    private final String USER_PASS = "garphild2017";
//    private final String DB_SCHEME = "macaronba";
//    private final String DB_PORT = "47010";
//    private final String DB_LINK = "127.0.0.1:"+DB_PORT+"/";
//    private final String COMMANDS = "?allowPublicKeyRetrieval=true&zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=UTF-8&useSSL="+useSSL;
    
            
//    public Connection getConnection(){;
//        try {
//        Class.forName(CLASS_DRIVER);
//        return  DriverManager.getConnection(DRIVER_TYPE+":"+TYPE_DATA_BASE+SLASHES+DB_LINK+DB_SCHEME+COMMANDS, USER_NAME, USER_PASS);
//        } catch (ClassNotFoundException | SQLException ex) {
////            System.err.println(ex.getMessage());
//            System.err.println("----------------=======SQL is not Connected!=========----------------");
//            return null;
//        }
//    }
//    
    public SqlDriver(String dataBaseName,String dataBaseLink)
    {
        this.DATA_BASE_LINK=dataBaseLink;
        this.USER_NAME=Parameters.SQLParameters.DATA_BASE_USERNAME;
        this.PASSWORD=Parameters.SQLParameters.DATA_BASE_PASSWORD;
        this.DATA_BASE_NAME=dataBaseName;
        this.DRIVER="com.mysql.jdbc.Driver";
        this.CHARSET="useUnicode=true&characterEncoding=UTF-8";
//         
    }
    
    public Connection getConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(DRIVER);
        return DriverManager.getConnection(this.DATA_BASE_LINK+DATA_BASE_NAME+"?"+CHARSET, USER_NAME, PASSWORD);
    }
}
