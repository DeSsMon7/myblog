package com.ash.myblog23.login;

import com.ash.myblog23.control.ShaConverter;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.ash.myblog23.login.dao.LoginDAO;
import com.ash.myblog23.login.beans.SessionUtils;
import com.ash.myblog23.control.UserFacade;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

@SessionScoped
@Named
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    private String password;
    private String msg;
    private String username;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dateNow = new Date();

    @Inject
    UserFacade ufacade;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //validate login
    public String validateUsernamePassword() {
        System.out.println("Button login pressed!");
        System.out.println("User logged in: " + username);
        System.out.println("Date login: " + dateNow);
        System.out.println(" ");
        System.out.println("-----");
        String passwordHash = ShaConverter.generateHash(password);
        boolean valid = LoginDAO.validate(username, passwordHash);
        Number resultUserId = ufacade.findLoggedUserId(username);
        if (resultUserId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "User not found!"));
            return null;
        }
        if (resultUserId.longValue() >= 0) {
            System.out.println("Validating username and password> Setting up User Id for the session...: " + resultUserId.longValue());
        } else {
            System.out.println("resultList error");
        }   
        if (valid) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("userid", resultUserId);
            session.setAttribute("username", username);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Greetings,",
                            username));
            return "index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Passowrd",
                            "Please enter correct username and Password"));
            return "welcome";
        }
    }

    //logout event, invalidate session
    public void logout(AjaxBehaviorEvent e) throws IOException {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Вие излязохте!",
                        "Няма да бъдете пренасочени!"));
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
    }

//    public String exitSession(ActionEvent event){
//        System.out.println("logout button Pressed!");
//        return "index?faces-redirect=true";
//    }
    public void captcha() {
        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Браво", "А сега се логнете!");
        FacesContext.getCurrentInstance().addMessage(null, msg1);
    }

}
