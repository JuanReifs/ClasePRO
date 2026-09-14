package dao;

import database.HibernateUtil;
import model.Libro;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO para la entidad Libro.
 * Incluye consultas filtradas por biblioteca y por autor.
 */
public class LibroDAO {

    private final SessionFactory sessionFactory;

    public LibroDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Persiste un nuevo libro en la base de datos.
     *
     * Si el ISBN ya existe, Hibernate lanzará una ConstraintViolationException
     * que el Controller capturará y gestionará.
     *
     * @param libro Objeto Libro a guardar
     * @throws Exception si hay error de BD (p.ej. ISBN duplicado)
     */
    public void guardarLibro(Libro libro) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(libro);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;  // Relanzamos para gestión en el Controller
        } finally {
            session.close();
        }
    }

    /**
     * Busca un libro por su ISBN.
     *
     * @param isbn ISBN a buscar
     * @return El Libro encontrado, o null
     */
    public Libro buscarPorIsbn(String isbn) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Libro l WHERE l.isbn = :isbn", Libro.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
        }
    }

    /**
     * Devuelve todos los libros de una biblioteca concreta.
     * Usa JOIN FETCH para cargar el autor en la misma consulta (evita N+1 queries).
     *
     * @param bibliotecaId ID de la biblioteca
     * @return Lista de libros de esa biblioteca
     */
    public List<Libro> obtenerLibrosPorBiblioteca(Long bibliotecaId) {
        try (Session session = sessionFactory.openSession()) {
            // JOIN FETCH: trae el autor y la biblioteca en una sola query
            return session.createQuery(
                            "SELECT l FROM Libro l " +
                                    "JOIN FETCH l.autor " +
                                    "JOIN FETCH l.biblioteca " +
                                    "WHERE l.biblioteca.id = :bibId",
                            Libro.class)
                    .setParameter("bibId", bibliotecaId)
                    .list();
        }
    }

    /**
     * Devuelve todos los libros escritos por un autor concreto.
     * Se usa para la exportación a CSV.
     *
     * @param autorId ID del autor
     * @return Lista de libros de ese autor
     */
    public List<Libro> obtenerLibrosPorAutor(Long autorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT l FROM Libro l " +
                                    "JOIN FETCH l.autor " +
                                    "JOIN FETCH l.biblioteca " +
                                    "WHERE l.autor.id = :autorId",
                            Libro.class)
                    .setParameter("autorId", autorId)
                    .list();
        }
    }

    /**
     * Devuelve todos los libros de la base de datos.
     *
     * @return Lista de todos los libros
     */
    public List<Libro> obtenerTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT l FROM Libro l JOIN FETCH l.autor JOIN FETCH l.biblioteca",
                            Libro.class)
                    .list();
        }
    }

    /**
     * Actualiza los datos de un libro existente.
     *
     * @param libro Libro con los nuevos valores
     * @throws Exception si hay error de BD
     */
    public void actualizarLibro(Libro libro) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(libro);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Elimina un libro por su ID.
     *
     * @param id ID del libro a eliminar
     * @throws Exception si hay error de BD
     */
    public void eliminarLibro(Long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Libro libro = session.find(Libro.class, id);
            if (libro != null) {
                session.remove(libro);
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
