package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un Libro.
 *
 * Relaciones:
 *   - ManyToOne con Autor     → Un libro tiene UN autor, un autor puede tener MUCHOS libros.
 *   - ManyToOne con Biblioteca→ Un libro está en UNA biblioteca, una biblioteca tiene MUCHOS libros.
 */
@Entity
@Table(name = "libros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ISBN único. La restricción UNIQUE en BD impide duplicados.
     * Intentar insertar un ISBN repetido lanzará una excepción.
     */
    @Column(name = "isbn", unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "numero_paginas", nullable = false)
    private Integer numeroPaginas;

    /** Ej: "Novela", "Ensayo", "Ciencia Ficción", "Historia"... */
    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "anio_publicacion", nullable = false)
    private Integer anioPublicacion;

    /**
     * FK autor_id en la tabla libros.
     * LAZY: el autor no se carga hasta que se accede a él.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    /**
     * FK biblioteca_id en la tabla libros.
     * Garantiza que un libro solo está en UNA biblioteca al mismo tiempo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_id", nullable = false)
    private Biblioteca biblioteca;

    public Libro(String isbn, String titulo, int paginas, String tipo, int anio, Autor autor, Biblioteca biblioteca) {
    }

    // Constructores, Getters y Setters generados por Lombok

    @Override
    public String toString() {
        return String.format("Libro[id=%d, isbn='%s', titulo='%s', paginas=%d, tipo='%s', año=%d]",
                id, isbn, titulo, numeroPaginas, tipo, anioPublicacion);
    }
}
