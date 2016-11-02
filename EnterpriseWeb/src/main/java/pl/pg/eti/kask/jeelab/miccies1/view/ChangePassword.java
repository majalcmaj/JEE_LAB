package pl.pg.eti.kask.jeelab.miccies1.view;

import lombok.Getter;
import lombok.Setter;
import pl.pg.eti.kask.jeelab.miccies1.ejb.UserService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * Created by mc on 01/11/16.
 */
@ManagedBean
@ViewScoped
public class ChangePassword implements Serializable {

    @Getter
    @Setter
    @ManagedProperty(value="#{userUtils}")
    private UserUtils userUtils;

    @EJB
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Getter
    @Setter
    private String password;


    public String changePassword() {
        String login = userUtils.getUsername();

        userService.changeUsersPassword(login, password);
        return "/user/change_password?faces-redirect=true";
    }
}
