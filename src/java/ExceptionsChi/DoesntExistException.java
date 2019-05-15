/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExceptionsChi;

/**
 *
 * @author Moses
 */
public class DoesntExistException extends Exception {
    public DoesntExistException(String message)
    {
        super(message);
    }
    
}
