package pl.pg.eti.kask.jeelab.miccies1.entities;

import lombok.*;
import pl.pg.eti.kask.jeelab.miccies1.entities.validators.GreaterThanZero;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mc on 2016-10-11.
 */

@ToString(of = "height")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tower")
@NamedQuery(name="Tower.findAll", query="SELECT t FROM Tower t")
public class Tower implements Serializable{
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @GreaterThanZero
    private Integer height;

    @OneToMany
    private List<Mage> mages = new ArrayList<>();
}
