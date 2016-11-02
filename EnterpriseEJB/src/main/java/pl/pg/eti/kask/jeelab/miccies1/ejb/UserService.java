package pl.pg.eti.kask.jeelab.miccies1.ejb;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.javaee.CryptUtils;
import pl.pg.eti.kask.jeelab.miccies1.entities.User;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by mc on 01/11/16.
 */
@Stateless
@LocalBean
@Log
@DeclareRoles(value = {"Admin", "User"})
public class UserService {

    @PersistenceContext
    EntityManager em;

    @Resource
    SessionContext sctx;

    @RolesAllowed({"Admin", "User"})
    public User findUser(String login) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.login = :login").setParameter("login", login);
        return (User) query.getSingleResult();
    }

    @PermitAll
    public List<User> findAllUsers() {
        return em.createNamedQuery("User.findAll").getResultList();
    }

    @RolesAllowed({"Admin", "User"})
    public boolean changeUsersPassword(String login, String newPassword) {
        if(sctx.isCallerInRole("Admin")) {
            _changeUsersPassword(login, newPassword);
        }else if(sctx.getCallerPrincipal().getName().equals(login)){
            _changeUsersPassword(login, newPassword);
        }else {
            return false;
        }
        return true;
    }

    private void _changeUsersPassword(String login, String newPassword) {
        User user = findUser(login);
        user.setPassword(CryptUtils.sha256(newPassword));
    }

    @RolesAllowed({"Admin", "User"})
    public boolean amIAdmin() {
        return sctx.isCallerInRole("Admin");
    }
}