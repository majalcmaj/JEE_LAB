package pl.pg.eti.kask.jeelab.miccies1.ejb;

import lombok.extern.java.Log;
import org.apache.commons.collections.CollectionUtils;
import pl.pg.eti.kask.jeelab.miccies1.entities.Mage;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mc on 2016-10-11.
 */
@Stateless
@LocalBean
@Log
public class TowerService implements Serializable {
    @PersistenceContext
    EntityManager em;

    public TowerService() { }

    public List<Mage> findAllMages() {
        return em.createNamedQuery("Mage.findAll").getResultList();
    }

    public Mage findMage(int id) {
        return em.find(Mage.class, id);
    }

    public void removeMage(Mage mage) {
        mage = em.merge(mage);
        Tower tower = findTower(mage.getTowerId());
        tower = em.merge(tower);
        tower.getMages().remove(mage);
        em.remove(mage);
    }

    public void removeMage(int id) {
        removeMage(findMage(id));
    }

    public List<Tower> findAllTowers() {
        return em.createNamedQuery("Tower.findAll").getResultList();
    }

    public Tower findTower(int id) {
        return em.find(Tower.class, id);
    }

    public void removeTower(Tower tower) {
        tower = em.merge(tower);
        em.remove(tower);
    }
    public void removeTower(int id) {
        removeTower(findTower(id));
    }


    public void saveTower(Tower tower) throws Exception{
        if (tower.getId() == null) {
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
                managedMage.setTowerId(mage.getTowerId());
                Tower oldTower = findTower(managedMage.getTowerId());
                oldTower.getMages().remove(managedMage);
                Tower newTower = findTower(mage.getTowerId());
                newTower.getMages().add(mage);
                modifiedTowers.add(oldTower);
                modifiedTowers.add(newTower);
            }
            managedMage.setElement(mage.getElement());
            managedMage.setName(mage.getName());
            managedMage.setMana(mage.getMana());
        }
        for(Tower modifiedTower : modifiedTowers)
            saveTower(modifiedTower);
    }
}
