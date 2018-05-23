package com.ash.myblog23.pres;

import com.ash.myblog23.model.Post;
import com.ash.myblog23.pres.util.JsfUtil;
import com.ash.myblog23.pres.util.JsfUtil.PersistAction;
import com.ash.myblog23.control.PostFacade;
import com.ash.myblog23.control.UserFacade;
import com.ash.myblog23.login.beans.SessionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Named("postController")
@SessionScoped
public class PostController implements Serializable {

    @EJB
    private com.ash.myblog23.control.PostFacade ejbFacade;
    private List<Post> items = null;
    private List<Post> userPosts = null;
    private Post selected;
    private HttpSession session;

    @Inject
    UserFacade userFacade; 
    
    
    public PostController() {
    }

    public Post getSelected() {
        return selected;
    }

    public void setSelected(Post selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PostFacade getFacade() {
        return ejbFacade;
    }

    public Post prepareCreate() {
        selected = null;
        selected = new Post();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/msgs_EN").getString("PostCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/msgs_EN").getString("PostUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/msgs_EN").getString("PostDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Post> getItems() {
        items = null;
        if (items == null) {
            items = getFacade().findAllOrdered();
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

    public List<Post> getUserPosts() {
        userPosts = new ArrayList();
        session = SessionUtils.getSession();
        Integer usersId = (Integer) session.getAttribute("userid");
        System.out.println("User ID" + usersId);
        userPosts = getFacade().findPostByUserId(usersId);
        return userPosts;
    }

    public Post getPost(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Post> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Post> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    @FacesConverter(forClass = Post.class)
    public static class PostControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PostController controller = (PostController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "postController");
            return controller.getPost(getKey(value));
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
            if (object instanceof Post) {
                Post o = (Post) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Post.class.getName()});
                return null;
            }
        }

    }

}
