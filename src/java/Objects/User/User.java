/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.User;

import ExceptionsChi.DoesntExistException;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 
 * @author Moses
 */
public class User {
    private int id;
    private int userId;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private Timestamp dateRegistered;
    private int status;
    private int registerType;

    public int getRegisterType() {
        return registerType;
    }

    public User setRegisterType(int registerType) {
        this.registerType = registerType;
        return this;
    }
    
    
    

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }
    
    
    
    /**
     * do not create this object with this constructor directly unless you're creating it with another function
     */
    public User()
    {
        
    }
    
    /**
     * it automatically creates a user based on it's user Id
     * @param userId
     * @param connection
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public User(int userId,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.userId=userId;
        new AuthenticatorSQLCommands(connection).getUser(this);
    }

    public int getUserId() {
        return userId;
    }

    public User setUserId(int userId) {
        this.userId = userId;
    return this;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    return this;}

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
    return this;}

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
    return this;}

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
    return this;}

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public User setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    return this;}

    public Timestamp getDateRegistered() {
        return dateRegistered;
    }

    public User setDateRegistered(Timestamp dateRegistered) {
        this.dateRegistered = dateRegistered;
    return this;}

    public int getStatus() {
        return status;
    }

    public User setStatus(int status) {
        this.status = status;
    return this;}
    
    
}
