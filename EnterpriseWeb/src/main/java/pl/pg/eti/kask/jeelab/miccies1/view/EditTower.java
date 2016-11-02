package pl.pg.eti.kask.jeelab.miccies1.view;

import pl.pg.eti.kask.jeelab.miccies1.ejb.TowerService;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mc on 2016-10-12.
 */
@ManagedBean
@ViewScoped
public class EditTower implements Serializable {

    private static final Logger log = Logger.getLogger(EditTower.class.getName());
    @EJB
    private TowerService towerService;
    public void setTowerService(TowerService towerService) { this.towerService = towerService; }

    private Tower tower;

    private int towerId;

    public void init() {
        if(tower == null) {
            if(towerId != 0) {
                tower = towerService.findTower(towerId);
            }else if(towerId == 0) {
                tower = new Tower();
            }else {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("error/404.xhtml");
                } catch (IOException e) {
                    log.log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public String saveTower() {
        try {
            towerService.saveTower(this.tower);
            return "/user/list_towers_and_mages?faces-redirect=true";
        }catch(Exception ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("error/404.xhtml");
            } catch (IOException e) {
                log.log(Level.SEVERE, null, ex);
            }finally {
                return "/user/list_towers_and_mages?faces-redirect=true";
            }
        }
    }

    public Tower getTower() {
        return this.tower;
    }

    public int getTowerId() {
        return this.towerId;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public void setTowerId(int towerId) {
        this.towerId = towerId;
    }
}
