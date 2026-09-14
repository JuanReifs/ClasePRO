package model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
/**
 * Entidad principal del sistema: representa un coche en el concesionario.
 * Mantiene una relación ManyToOne con la entidad Marca.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "coches")
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula", nullable = false, unique = true, length = 20)
    private String matricula;

    // ── Relación ManyToOne con Marca ──────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @Column(name = "color", length = 50)
    private String color;


    @Enumerated(EnumType.STRING)
    @Column(name = "combustible", nullable = false, length = 30)
    private Combustible combustible;

    @Column(name = "año", nullable = false)
    private int year;

    @Column(name = "kilometros", nullable = false)
    private int kilometros;

    @Column(name = "precio", nullable = false)
    private double precio;

    @ManyToOne
    @JoinColumn(name = "estado_venta")
    private Estado estado_venta;

    // ──────────────── Constructores ────────────────

    public Coche(String matricula, Marca marca, String modelo, String color,
                 Combustible combustible, int year, int kilometros, double precio, Estado estado) {
        this.matricula   = matricula;
        this.marca       = marca;
        this.modelo      = modelo;
        this.color       = color;
        this.combustible = combustible;
        this.year = year;
        this.kilometros  = kilometros;
        this.precio      = precio;
        this.estado_venta = estado;
    }





    public void mostrarDatos(){
        System.out.println("id = " + id);
        System.out.println("matricula = " + matricula);
        String marca1 = marca.getMarca();
        System.out.println("marca = " + marca1);
        System.out.println("modelo = " + modelo);
        System.out.println("color = " + color);
        String combustible1 = combustible.getDescripcion();
        System.out.println("combustible = " + combustible1);
        System.out.println("año de fabricación = " + year);
        System.out.println("kilometros = " + kilometros);
        System.out.println("precio = " + precio);
        String estado1 = estado_venta.getNombre();
        System.out.println("estado_venta = " + estado1);
    }


}
