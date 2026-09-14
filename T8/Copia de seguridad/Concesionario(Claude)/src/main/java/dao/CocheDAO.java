package dao;

import database.HibernateUtil;
import model.Coche;
import model.Combustible;
import model.Estado;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) para la entidad {@link Coche}.
 *
 * <p>Encapsula todas las operaciones CRUD y consultas sobre la tabla {@code coches}.
 * Usa HQL (Hibernate Query Language) para las consultas.</p>
 */
public class CocheDAO {

    // ===================== OPERACIONES CRUD =====================

    /**
     * Persiste un nuevo coche en la base de datos.
     *
     * @param coche El coche a guardar.
     * @return El coche con el ID asignado.
     */
    public Coche guardar(Coche coche) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(coche);
            tx.commit();
            return coche;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un coche ya existente.
     *
     * @param coche El coche con los datos modificados.
     * @return El coche actualizado (managed entity).
     */
    public Coche actualizar(Coche coche) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            Coche actualizado = session.merge(coche);
            tx.commit();
            return actualizado;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un coche de la base de datos por su ID.
     *
     * @param id El identificador del coche.
     * @return {@code true} si se eliminó, {@code false} si no se encontró.
     */
    public boolean eliminar(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            Coche coche = session.get(Coche.class, id);
            if (coche == null) {
                tx.rollback();
                return false;
            }
            session.remove(coche);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar el coche: " + e.getMessage(), e);
        }
    }

    // ===================== CONSULTAS DE BÚSQUEDA =====================

    /**
     * Busca un coche por su ID.
     */
    public Optional<Coche> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Coche coche = session.get(Coche.class, id);
            // Inicializar la marca (lazy loading) dentro de la sesión
            if (coche != null) {
                coche.getMarca().getNombre(); // fuerza inicialización
            }
            return Optional.ofNullable(coche);
        }
    }

    /**
     * Busca un coche por su matrícula (insensible a mayúsculas).
     */
    public Optional<Coche> buscarPorMatricula(String matricula) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "WHERE UPPER(c.matricula) = UPPER(:matricula)", Coche.class)
                    .setParameter("matricula", matricula)
                    .uniqueResultOptional();
        }
    }

    /**
     * Devuelve todos los coches, ordenados por marca y modelo.
     */
    public List<Coche> listarTodos() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "ORDER BY c.marca.nombre, c.modelo", Coche.class)
                    .list();
        }
    }

    /**
     * Busca todos los coches de una marca concreta (insensible a mayúsculas).
     */
    public List<Coche> buscarPorMarca(String nombreMarca) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca m " +
                                    "WHERE LOWER(m.nombre) = LOWER(:nombre) " +
                                    "ORDER BY c.modelo", Coche.class)
                    .setParameter("nombre", nombreMarca)
                    .list();
        }
    }

    /**
     * Busca todos los coches de un modelo concreto (insensible a mayúsculas).
     */
    public List<Coche> buscarPorModelo(String modelo) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "WHERE LOWER(c.modelo) = LOWER(:modelo)", Coche.class)
                    .setParameter("modelo", modelo)
                    .list();
        }
    }

    /**
     * Busca coches dentro de un rango de precio (inclusivo).
     */
    public List<Coche> buscarPorRangoPrecio(double precioMin, double precioMax) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "WHERE c.precio BETWEEN :min AND :max " +
                                    "ORDER BY c.precio", Coche.class)
                    .setParameter("min", precioMin)
                    .setParameter("max", precioMax)
                    .list();
        }
    }

    /**
     * Devuelve todos los coches con un estado de venta concreto.
     */
    public List<Coche> buscarPorEstado(Estado estado) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "WHERE c.estado = :estado " +
                                    "ORDER BY c.marca.nombre", Coche.class)
                    .setParameter("estado", estado)
                    .list();
        }
    }

    /**
     * Devuelve todos los coches con un tipo de combustible concreto.
     */
    public List<Coche> buscarPorCombustible(Combustible combustible) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "SELECT c FROM Coche c JOIN FETCH c.marca " +
                                    "WHERE c.combustible = :combustible " +
                                    "ORDER BY c.marca.nombre, c.modelo", Coche.class)
                    .setParameter("combustible", combustible)
                    .list();
        }
    }

    /**
     * Comprueba si ya existe un coche con esa matrícula en BD.
     */
    public boolean existeMatricula(String matricula) {
        try (Session session = HibernateUtil.getSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(c) FROM Coche c " +
                                    "WHERE UPPER(c.matricula) = UPPER(:matricula)", Long.class)
                    .setParameter("matricula", matricula)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}
