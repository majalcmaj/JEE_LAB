package pl.pg.eti.kask.jeelab.miccies1.view;

import pl.pg.eti.kask.jeelab.miccies1.ejb.TowerService;
import pl.pg.eti.kask.jeelab.miccies1.entities.Mage;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mc on 2016-10-12.
 */
@ManagedBean
@ViewScoped
public class ListTowersAndMages implements Serializable{

    @EJB
    private TowerService towerService;

    private List<Tower> towers = null;

    public void setTowerService(TowerService towerService) { this.towerService = towerService; }
    public List<Tower> getTowers() {
        if(towers == null) {
            towers = towerService.findAllTowers();
        }
        return towers;
    }

    public void removeTower(Tower tower) {
        towerService.removeTower(tower.getId());
        towers.remove(tower);
    }

    public void removeMage(Mage mage) {
        towerService.removeMage(mage.getId());
    }
}
