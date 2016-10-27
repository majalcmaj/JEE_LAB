package pl.pg.eti.kask.jeelab.miccies1.entities;

import lombok.*;
import pl.pg.eti.kask.jeelab.miccies1.entities.validators.GreaterThanZero;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mc on 2016-10-11.
 */

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mages")
@NamedQueries({
        @NamedQuery(name = "Mage.findAll", query = "SELECT m from Mage m"),
        @NamedQuery(name = "Mage.increaseMana", query = "UPDATE Mage m SET m.mana = m.mana + :additional_mana")
})
@DiscriminatorColumn(name="type")
@DiscriminatorValue("mage")
public class Mage implements Serializable{
    @Column
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    @GreaterThanZero
    private Integer mana;
    private Integer towerId;

    @Column
    @Enumerated(EnumType.STRING)
    private Mage.Element element;

    public static enum Element {
        FIRE,
        WATER,
        AIR,
        EARTH;

        public String value() {
            return name();
        }

        public static Element fromValue(String s) {
            return valueOf(s);
        }
    }
}
