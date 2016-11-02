package pl.pg.eti.kask.jeelab.miccies1.view;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.pg.eti.kask.jeelab.miccies1.ejb.UserService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.Principal;

/**
 * Created by mc on 2016-10-12.
 */
@ManagedBean
@ViewScoped
@Log
public class UserUtils implements Serializable {

    @EJB
    private UserService userService;

    public boolean amIAdmin() {
        return userService.amIAdmin();
    }

    public String getUsername() {
        Principal principal = getPrincipal();

        if(principal != null) {
            return principal.getName();
        }

        return "niezalogowany";
    }

    public Principal getPrincipal() {
        HttpServletRequest request = getRequest();
        return request.getUserPrincipal();
    }

    public String logout() throws ServletException {
        HttpServletRequest request = getRequest();
        request.logout();

        return "/index.xhtml?faces-redirect=true";
    }

    private HttpServletRequest getRequest() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (HttpServletRequest) externalContext.getRequest();
    }
}
