package dao;

import database.HibernateUtil;
import model.Autor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Autor.
 * Centraliza todas las operaciones de BD relacionadas con autores.
 * El Controller llama a este DAO; aquí no hay lógica de negocio.
 */
public class AutorDAO {

    private final SessionFactory sessionFactory;

    public AutorDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Persiste un nuevo autor en la base de datos.
     *
     * Si el DNI ya existe, Hibernate lanzará una excepción de constraint
     * que debe capturarse en el nivel superior (Controller).
     *
     * @param autor Objeto Autor a guardar
     * @throws Exception si hay error de BD (p.ej. DNI duplicado)
     */
    public void guardarAutor(Autor autor) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(autor);   // INSERT en BD
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;  // Se relanza para que el Controller la gestione
        } finally {
            session.close();
        }
    }

    /**
     * Busca un autor por su DNI.
     *
     * @param dni DNI a buscar
     * @return El Autor encontrado, o null si no existe
     */
    public Autor buscarPorDni(String dni) {
        try (Session session = sessionFactory.openSession()) {
            // HQL (Hibernate Query Language): parecido a SQL pero con nombres de clase/campo Java
            return session.createQuery(
                            "FROM Autor a WHERE a.dni = :dni", Autor.class)
                    .setParameter("dni", dni)
                    .uniqueResult();
        }
    }

    /**
     * Busca un autor por su ID (clave primaria).
     *
     * @param id ID del autor
     * @return El Autor encontrado, o null
     */
    public Autor buscarPorId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Autor.class, id);
        }
    }

    /**
     * Devuelve todos los autores de la base de datos.
     *
     * @return Lista con todos los autores
     */
    public List<Autor> obtenerTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Autor", Autor.class).list();
        }
    }

    /**
     * Actualiza los datos de un autor existente.
     *
     * @param autor Autor con los nuevos valores (debe tener ID ya asignado)
     * @throws Exception si hay error de BD
     */
    public void actualizarAutor(Autor autor) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(autor);  // UPDATE en BD
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Elimina un autor por su ID.
     *
     * @param id ID del autor a eliminar
     * @throws Exception si hay error de BD
     */
    public void eliminarAutor(Long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Autor autor = session.get(Autor.class, id);
            if (autor != null) {
                session.remove(autor);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
