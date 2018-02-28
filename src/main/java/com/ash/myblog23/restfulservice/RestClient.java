package com.ash.myblog23.restfulservice;

import com.ash.myblog23.model.User;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author AsH
 */

@Named("RestClient")
@SessionScoped
public class RestClient implements Serializable {
        
        private String userNumberField;
        private final String endPoint = "http://localhost:8080/myblog23/webresources/";
        private final String userResource = "users/";
        
        
        public void call(){
            Client client = ClientBuilder.newClient();
           
           User  obj =  client.target(endPoint.concat(userResource).concat(userNumberField)).request().get(User.class);
            System.out.println(obj);
        }

    public String getUserNumberField() {
        return userNumberField;
    }

    public void setUserNumberField(String userNumberField) {
        this.userNumberField = userNumberField;
    }
    
}