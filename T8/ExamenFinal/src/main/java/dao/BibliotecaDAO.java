package dao;

import database.HibernateUtil;
import model.Biblioteca;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO para la entidad Biblioteca.
 * Gestiona todas las operaciones de acceso a datos de bibliotecas.
 */
public class BibliotecaDAO {

    private final SessionFactory sessionFactory;

    public BibliotecaDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Persiste una nueva biblioteca en la base de datos.
     *
     * @param biblioteca Objeto Biblioteca a guardar
     * @throws Exception si hay error de BD
     */
    public void guardarBiblioteca(Biblioteca biblioteca) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(biblioteca);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Busca una biblioteca por su ID.
     *
     * @param id ID de la biblioteca
     * @return La Biblioteca encontrada, o null
     */
    public Biblioteca buscarPorId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Biblioteca.class, id);
        }
    }

    /**
     * Devuelve todas las bibliotecas registradas.
     *
     * @return Lista de todas las bibliotecas
     */
    public List<Biblioteca> obtenerTodas() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Biblioteca", Biblioteca.class).list();
        }
    }

    /**
     * Actualiza los datos de una biblioteca existente.
     *
     * @param biblioteca Biblioteca con los nuevos valores
     * @throws Exception si hay error de BD
     */
    public void actualizarBiblioteca(Biblioteca biblioteca) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(biblioteca);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Elimina una biblioteca por su ID.
     * Precaución: por el cascade, también eliminará sus libros asociados.
     *
     * @param id ID de la biblioteca a eliminar
     * @throws Exception si hay error de BD
     */
    public void eliminarBiblioteca(Long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Biblioteca bib = session.get(Biblioteca.class, id);
            if (bib != null) {
                session.remove(bib);
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
