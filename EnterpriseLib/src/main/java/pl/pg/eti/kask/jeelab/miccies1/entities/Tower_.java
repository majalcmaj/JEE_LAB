package pl.pg.eti.kask.jeelab.miccies1.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by mc on 01/11/16.
 */
@StaticMetamodel(Tower.class)
public class Tower_ {
    public static volatile SingularAttribute<Tower, User> owner;
}
