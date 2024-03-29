package pl.pg.eti.kask.jeelab.miccies1.view.converter;

import lombok.Setter;
import lombok.extern.java.Log;
import pl.pg.eti.kask.jeelab.miccies1.ejb.TowerService;
import pl.pg.eti.kask.jeelab.miccies1.entities.Tower;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.logging.Level;

/**
 * Created by mc on 2016-10-12.
 */
@ManagedBean
@RequestScoped
@Log
public class TowerConverter implements Converter{
    @EJB
    @Setter
    private TowerService towerService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        log.log(Level.INFO, "GAO: " + s);
        if("---".equals(s))
            return null;
        return towerService.findTower(Integer.parseInt(s));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        log.log(Level.INFO, "GAS: " + (o == null ? "NULL" : o.toString()));
        if(o==null) {
            return "---";
        }
        return String.valueOf(((Tower)o).getId());
    }
}
