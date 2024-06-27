/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Qasim
 */
public class medicines {
    private String name;
    private String quantity;
    private String salt;
    private String price;
    private String code;
    private String branch;
    public medicines(String name, String quantity, String salt, String price, String code,String branch)
    {
        this.name=name;
        this.quantity=quantity;
        this.salt=salt;
        this.price=price;
        this.code=code;
        this.branch=branch;
    }
    public String getName()
    {
        return this.name;
    }
    public String getQuantity()
    {
        return this.quantity;
    }
    public String getSalt()
    {
        return this.salt;
    }
    public String getPrice()
    {
        return this.price;
    }
    public String getCode()
    {
        return this.code;
    }
    public String getBranch()
    {
        return this.branch;
    }
    
}
