/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Partner;

import Objects.Partner.Partner;
import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerReceipt;
import Objects.Payment.Payment;
import Objects.Ship.Ship;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.OrderSQLCommands;
import SQL.Commands.PartnerSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class PartnerCompleteOrder {
    private PartnerSQLCommands partnerSQL;
    private OrderHistorySQLCommands orderHisSQL;
    private OrderSQLCommands orderSQL;
    private Partner partner;
    public PartnerCompleteOrder(Connection connection,Partner partner)
    {
        this.partner=partner;
        this.partnerSQL=new PartnerSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.orderSQL=new OrderSQLCommands(connection);
    }
    
    public void completeOrder(PartnerReceipt receipt,Ship ship,Payment payment) throws SQLException
    {
        int price=setPrices(receipt);
        int shipId=orderSQL.addShip(ship.getShipType().getShipTypeId(), ship.getAddress().getAddressId());
        int payStatus=0;
        if(payment.getPaymentType().getPaymentTypeId()==2)
        {
            payStatus=0;
        }else if(payment.getPaymentType().getPaymentTypeId()==3)
        {
            payStatus=1;
        }
        
        int paymentId=orderSQL.addPayment(price, payment.getPaymentType().getPaymentTypeId(), "", payStatus);
        this.partnerSQL.completeOrder(receipt.getOrderId(), shipId, paymentId);
        decreaseCounts(receipt);
        this.partnerSQL.runOut();
        
    }
    
    private void decreaseCounts(PartnerReceipt receipt) throws SQLException
    {
        for(PartnerPart part:receipt.getParts())
        {
            this.partnerSQL.reduceCount(part.getProduct().getProductId(), part.getCount());
        }
    }
    
    private int setPrices(PartnerReceipt receipt) throws SQLException
    {
        int wholePrice=0;
        for(PartnerPart part:receipt.getParts())
        {
            int price=part.getProduct().getPrice()*part.getCount();
            price*=this.partner.getSeries().getEffect()/100;
            this.partnerSQL.setPartFinalPrice(part.getPartId(), price);
            wholePrice+=price;
        }
        return wholePrice;
    }
}
