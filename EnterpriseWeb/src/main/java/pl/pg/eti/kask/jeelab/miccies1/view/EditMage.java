package pl.pg.eti.kask.jeelab.miccies1.view;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.pg.eti.kask.jeelab.miccies1.ejb.TowerService;
import pl.pg.eti.kask.jeelab.miccies1.entities.Mage;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by mc on 2016-10-12.
 */
@ManagedBean
@ViewScoped
@Log
public class EditMage implements Serializable {

    @EJB
    private TowerService towerService;
    public void setTowerService(TowerService towerService) { this.towerService = towerService; }

    @Setter
    @Getter
    private Mage mage;

    @Getter
    @Setter
    private Integer mageId;

    @Getter
    @Setter
    private Tower newTower;

    private List<SelectItem> towersAsSelectItems;
    private List<SelectItem> elementsAsSelectItems;

    public void init() {
        if(mage == null) {
            if (mageId != null) {
                mage = towerService.findMage(mageId);
            } else if (mageId == null) {
                mage = new Mage();
            } else {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("error/404.xhtml");
                } catch (IOException e) {
                    log.log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public String saveMage() {
        try {
            this.mage.setTowerId(newTower.getId());
            towerService.saveMage(this.mage);
        }catch(Exception e) {
            // TODO
        }finally {
            return "/user/list_towers_and_mages?faces-redirect=true";
        }
    }

    public List<SelectItem> towersAsSelectItems() {
        if(towersAsSelectItems == null) {
            towersAsSelectItems = new ArrayList<>();
            for(Tower t : towerService.findAllTowers()) {
                towersAsSelectItems.add(new SelectItem(t, "Tower with height:" + t.getHeight()));
            }
        }
        return towersAsSelectItems;
    }

    public List<SelectItem> elementsAsSelectItems() {
        if(elementsAsSelectItems == null) {
            elementsAsSelectItems=new ArrayList<>();
            for(Mage.Element element : Mage.Element.values()) {
                elementsAsSelectItems.add(new SelectItem(element, element.name().toLowerCase()));
            }
        }
        return elementsAsSelectItems;
    }
}
