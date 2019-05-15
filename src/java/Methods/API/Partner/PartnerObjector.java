/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Partner;

import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerProduct;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class PartnerObjector {
    public PartnerPart getPartnerPart(JSONObject partJson)
    {
        PartnerPart part=new PartnerPart();
        part.setCount(partJson.optInt("count"));
        part.setProduct(getPartnerProduct(partJson.optJSONObject("product")));
        return part;
    }
    
    public PartnerProduct getPartnerProduct(JSONObject productJson)
    {
        PartnerProduct product=new PartnerProduct();
        product.setProductId(productJson.optInt("productId"));
        return product;
    }
}
