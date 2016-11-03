package pl.pg.eti.kask.jeelab.miccies1.ejb;

import lombok.extern.java.Log;
import org.apache.commons.collections.CollectionUtils;
import pl.pg.eti.kask.jeelab.miccies1.entities.Mage;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower_;
import pl.pg.eti.kask.jeelab.miccies1.entities.User;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mc on 2016-10-11.
 */
@Stateless
@LocalBean
@DeclareRoles(value = {"Admin", "User"})
@Log
public class TowerService implements Serializable {
    @PersistenceContext
    EntityManager em;

    @Resource
    SessionContext sctx;

    @EJB
    UserService userService;

    public TowerService() { }

    @RolesAllowed({"Admin", "User"})
    public List<Mage> findAllMages() {
        if(sctx.isCallerInRole("Admin"))
            return em.createNamedQuery("Mage.findAll").getResultList();
        else {
            List<Tower> towers = findAllTowers();
            List<Mage> mages = new ArrayList<>();
            for(Tower t : towers)
                mages.addAll(t.getMages());
            return mages;
        }
    }

    @RolesAllowed({"Admin", "User"})
    public Mage findMage(int id) {
        Mage m = em.find(Mage.class, id);
        if(!sctx.isCallerInRole("Admin")) {
            if(!findTower(m.getTowerId()).getOwner().getLogin().
                    equals(sctx.getCallerPrincipal().getName()))
                return null;
        }
        return m;
    }

    @RolesAllowed({"Admin", "User"})
    public void removeMage(Mage mage) {
        mage = em.merge(mage);
        Tower tower = findTower(mage.getTowerId());
        tower = em.merge(tower);
        tower.getMages().remove(mage);
        em.remove(mage);
    }

    @RolesAllowed({"Admin", "User"})
    public void removeMage(int id) {
        removeMage(findMage(id));
    }

    @RolesAllowed({"Admin", "User"})
    public List<Tower> findAllTowers() {
        if(sctx.isCallerInRole("Admin"))
            return em.createNamedQuery("Tower.findAll").getResultList();
        else {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tower> cq = cb.createQuery(Tower.class);
            User owner = userService.findUser(sctx.getCallerPrincipal().getName());
            Root<Tower> tower = cq.from(Tower.class);
            return em.createQuery(cq.where(cb.equal(tower.get(Tower_.owner), owner))).getResultList();
        }
    }

    @RolesAllowed({"Admin", "User"})
    public Tower findTower(int id) {
        return em.find(Tower.class, id);
    }

    @RolesAllowed({"Admin", "User"})
    public void removeTower(Tower tower) {
        tower = em.merge(tower);
        em.remove(tower);
    }
    @RolesAllowed({"Admin", "User"})
    public void removeTower(int id) {
        removeTower(findTower(id));
    }


    @RolesAllowed({"Admin", "User"})
    public void saveTower(Tower tower) throws Exception{
        if (tower.getId() == null) {
            String login = sctx.getCallerPrincipal().getName();
            if(!login.equals("anonymous"))
                tower.setOwner(userService.findUser(login));
            em.persist(tower);
        } else {
            Tower managedTower = findTower(tower.getId());
            managedTower.setHeight(tower.getHeight());
            Collection<Mage> removedMages = CollectionUtils.subtract(managedTower.getMages(), tower.getMages());
            Collection<Mage> addedMages = CollectionUtils.subtract(tower.getMages(), managedTower.getMages());
            for(Mage mage : removedMages) {
                managedTower.getMages().remove(mage);
                removeMage(mage);
            }
            for(Mage mage : addedMages) {
                Mage managedMage = findMage(mage.getId());
                managedTower.getMages().add(mage);
                mage.setTowerId(managedTower.getId());
            }
        }
    }

    @RolesAllowed({"Admin", "User"})
    public void saveMage(Mage mage) throws Exception{
        List<Tower> modifiedTowers = new ArrayList<>();
        if (mage.getId() == null) {
            em.persist(mage);
            Tower modifiedTower = findTower(mage.getTowerId());
            modifiedTower.getMages().add(mage);
            modifiedTowers.add(modifiedTower);
        }else {
            Mage managedMage = findMage(mage.getId());
            if(!mage.getTowerId().equals(managedMage.getTowerId())) {
                Tower oldTower = findTower(managedMage.getTowerId());
                managedMage.setTowerId(mage.getTowerId());
                oldTower.getMages().remove(managedMage);
                Tower newTower = findTower(mage.getTowerId());
                newTower.getMages().add(mage);
                modifiedTowers.add(oldTower);
                modifiedTowers.add(newTower);
            }
            managedMage.setElement(mage.getElement());
            managedMage.setName(mage.getName());
            managedMage.setMana(mage.getMana());
            em.persist(managedMage);
        }
        for(Tower modifiedTower : modifiedTowers)
            saveTower(modifiedTower);
    }

    @RolesAllowed({"Admin", "User"})
    public void increaseMana(Integer by) {
        em.createNamedQuery("Mage.increaseMana").setParameter("additional_mana", by).executeUpdate();
    }
}
