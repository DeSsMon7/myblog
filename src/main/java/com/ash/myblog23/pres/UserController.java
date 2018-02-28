package com.ash.myblog23.pres;

import com.ash.myblog23.control.ShaConverter;
import com.ash.myblog23.model.User;
import com.ash.myblog23.pres.util.JsfUtil;
import com.ash.myblog23.pres.util.JsfUtil.PersistAction;
import com.ash.myblog23.control.UserFacade;
import com.ash.myblog23.login.beans.SessionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private com.ash.myblog23.control.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;
    private Integer userId;
    private String username;
    private static User userRecovery;
    private User userItems;

    // Email Settings (Google)
    private String to;
    private String subject;
    private String msg;
    private String userEmail;
    private String userEmailPassword;
    private List<User> userEmails;

    public UserController() {
    }

    @PostConstruct
    public void init() {

        userEmail = "mercurysoftware701@gmail.com";
        userEmailPassword = "b3foreiforg3t";
        userEmails = null;
        try {
            userEmails = ejbFacade.findAll();
        } catch (NullPointerException e) {
            System.out.println("Error at User Controller userEmails = userFacade.findAll: " + e);
        }
        HttpSession session = SessionUtils.getSession();
        userId = (Integer) session.getAttribute("userid");
        username = (String) session.getAttribute("username");
    }

    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public void clicking() {
        System.out.println("Clicking on THIS?!?!");
    }

    public void recoverPassword() {
        System.out.println("Clicking on THIS ?!!?!?");
        User user = new User();
        List<User> findAll = ejbFacade.findAll();
        boolean userExists = false;
        for (int i = 0; i < findAll.size(); i++) {
            String dbUser = findAll.get(i).getUsername();
            if (dbUser == null ? (selected.getUsername()) == null : dbUser.toLowerCase().equals(selected.getUsername().toLowerCase())) {
                System.out.println(dbUser + " == " + selected.getUsername());
                userExists = true;
                user.setUsername(findAll.get(i).getUsername());
                user.setUserEmail(findAll.get(i).getUserEmail());
                user.setPassword(findAll.get(i).getPassword());
                System.out.println("RecoveryPassword User EMAIL: " + selected.getUserEmail());
            }

        }

        if (!userExists) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Няма потребител с това име: " + selected.getUsername() + "!",
                            "Моля, опитайте с друго име!"));
            selected = null;
        } else {
            System.out.println("Потребителят иска да си смени паролата, изпращам Email до: " + user.getUserEmail());
            to = user.getUserEmail();
            subject = "Възстановяване на забравена парола";
            msg += "Здравейте " + user.getUsername() + "! \n";
            msg = "AsH C0d3 BloG\n На посоченият линк, ще може да въведете нова парола. "
                    + "\n http://192.168.0.101:8080/myblog23/faces/recovery/crypted?username=" + user.getUsername() + "&hash=" + user.getPassword();
            msg += "\n"
                    + "Системата все още се разработва. Възможно е да има несъответствия в данните.";

            System.out.println("SEND EMAIL to " + user.getUserEmail());
            try {
                sendEmail(to, subject, msg, userEmail, userEmailPassword);
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Заявката за възстановяване бе изпратена успешно!",
                                "Благодарим ви."));
                selected = new User();
            } catch (Exception e) {
                System.out.println("Caching Exception when send email for recover the password." + e);
                System.out.println("Email-a не може да бъде изпратен :( ");
                System.out.println("Възможно е потребителят да е посочил невалиден Email!");
                System.out.println("Email на потребителя: " + user.getUserEmail());
                selected = new User();
            }
        }
    }

    private void sendEmail(String to, String sub, String msg, final String user, final String pass) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(sub);
            message.setText(msg);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setSelectedForRecovery() {
        userRecovery = new User();
    }

    public User prepareRecovery() {
        System.out.println("Mnogo stranno! Idvam li tuk??");
        selected = new User();
        return selected;
    }

    // This method is used by Recovery Restfull for setting up option flag to true
    public static void setRecoveryOption(String username) {
        UserController uc = new UserController();
        uc.prepareRecovery();
        userRecovery.setUsername(username);
        System.out.println("userRecovery: " + userRecovery.getUsername());

    }

    public User prepareCreate(ActionEvent event) {
        System.out.println("Идвам ли в userController.prepareCreate?");
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        String userPassword = selected.getPassword();
        selected.setPassword(ShaConverter.generateHash(userPassword));
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/msgs_EN").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/msgs_EN").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/msgs_EN").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/msgs_EN").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/msgs_EN").getString("PersistenceErrorOccured"));
            }
        }
    }

    public UserFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(UserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static User getUserRecovery() {
        return userRecovery;
    }

    public static void setUserRecovery(User userRecovery) {
        UserController.userRecovery = userRecovery;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmailPassword() {
        return userEmailPassword;
    }

    public void setUserEmailPassword(String userEmailPassword) {
        this.userEmailPassword = userEmailPassword;
    }

    public List<User> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(List<User> userEmails) {
        this.userEmails = userEmails;
    }

    public User getUserItems() {
        return userItems;
    }

    public void setUserItems(User userItems) {
        this.userItems = userItems;
    }

    public User getUser(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}
