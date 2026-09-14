package model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**

 * Entidad principal del sistema: representa un coche en el concesionario.
 * Mantiene una relación ManyToOne con la entidad Marca.
 */
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "Estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @OneToMany(mappedBy = "estadp")
    private List<Coche> listaCoches;

    public Estado(String nombre) {
        this.nombre = nombre;
    }
}