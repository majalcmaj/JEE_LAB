package pl.pg.eti.kask.jeelab.miccies1.view;

import lombok.Getter;
import lombok.Setter;
import pl.pg.eti.kask.jeelab.miccies1.ejb.UserService;
import pl.pg.eti.kask.jeelab.miccies1.entities.User;
import sun.security.util.Password;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mc on 01/11/16.
 */
@ManagedBean
@ViewScoped
public class ListUsers implements Serializable{
    @EJB
    UserService userService;

    @Getter
    @Setter
    @ManagedProperty(value="#{changePassword}")
    private ChangePassword changePassword;


    public List<User> getUsers() {
        return userService.findAllUsers();
    }

    @Getter
    @Setter
    private String password;

    public String changeUsersPassword(String user) {
        userService.changeUsersPassword(user, password);
        return "/index?faces-redirect=true";
    }
}
