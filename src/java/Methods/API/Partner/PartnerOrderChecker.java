/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Partner;

import Objects.Partner.Partner;
import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerReceipt;
import SQL.Commands.PartnerSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import org.json.JSONArray;

/**
 *
 * @author Moses
 */
public class PartnerOrderChecker {
    private PartnerSQLCommands partnerSQL;
    private PartnerReceipt receipt;
    private boolean valid=true;
    private LinkedList<PartnerPart> ranOuts=new LinkedList<PartnerPart>();
    private PartnerJsoner partnerJsoner;
    private Partner partner;
    public PartnerOrderChecker(PartnerReceipt receipt,Connection connection,Partner partner) throws SQLException
    {
        this.partner=partner;
        this.partnerSQL=new PartnerSQLCommands(connection);
        this.receipt=receipt;
        this.partnerJsoner=new PartnerJsoner(connection);
        isOrderValid();
    }
    
    private void isOrderValid() throws SQLException
    {
        for(PartnerPart part:receipt.getParts())
        {
            if(!this.partnerSQL.isPartOk(part.getProduct().getProductId(), part.getCount()))
            {
                this.valid=false;
                this.ranOuts.add(part);
//                partnerSQL.deletePartnerPart(part.getPartId(), receipt.getOrderId());
            }
        }
        
    }
    
    public JSONArray getRanOutResponse()
    {
        JSONArray ranoutArray=new JSONArray();
        int i=0;
        for(PartnerPart part:this.ranOuts)
        {
            ranoutArray.put(i,this.partnerJsoner.getPartnerProduct(part.getProduct(),partner));
            i++;
        }
        return ranoutArray;
    }
    
    public boolean isValid()
    {
        return this.valid;
    }
}
