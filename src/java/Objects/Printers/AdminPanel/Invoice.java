/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Panel.Buttons;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.OrderSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Moses
 */
public class Invoice {
    private OrderSQLCommands orderSQL;
    private OrderHistorySQLCommands orderHisSQL;
    private Orders orders;
    
    public Invoice(Connection connection)
    {
        this.orderSQL=new OrderSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.orders=new Orders(connection);
    }
    
    public String getPaymentTypeRows() throws SQLException
    {
        LinkedList<PaymentType> types=this.orderSQL.getPaymentTypes();
        String rows="";
        Buttons buttons=new Buttons();
        for(PaymentType type:types)
        {
            rows+="<tr>"
                    + "<td>"+type.getName()+"</td>"
                    + "<td>"+type.getDescription()+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"UpdatePaymentType\" style=\"display:inline;\" >"
                    + "<input type=\"hidden\" name=\"paymentTypeId\" value=\""+type.getPaymentTypeId()+"\" >";
            if(type.getStatus()==0)
            {
                rows+= "<input type=\"hidden\" name=\"status\" value=\"1\" >"
                    +buttons.btn_Normal_Success("فعال سازی", "submit", "", false, false);
                    
            }
            if(type.getStatus()==1)
            {
                rows+="<input type=\"hidden\" name=\"status\" value=\"0\" >"
                    +buttons.btn_Normal_Danger("غیرفعال کردن", "submit", "", false, false);
                    
            }
            
           rows+="</form>"
                   +"</td>"
               + "</tr>";
        }
        return rows;
    }
    
    public String getPaymentRows(HttpServletRequest request) throws SQLException, DoesntExistException
    {
        LinkedList<Payment> payments=this.orderHisSQL.getPayments();
        String rows="";
        for(Payment payment:payments)
        {
            rows+="<tr>"
                    + "<td>"+payment.getPrice()+"</td>"
                    + "<td>"+payment.getDate().toString()+"</td>"
                    + "<td>"+payment.getPaymentType().getName()+"</td>"
                    + "<td>"+payment.getApprove()+"</td>"
                    + "<td>"+this.orders.getPaymentStatus(payment)+"</td>"
                    + "</tr>";
        }
        return rows;
    }
}
