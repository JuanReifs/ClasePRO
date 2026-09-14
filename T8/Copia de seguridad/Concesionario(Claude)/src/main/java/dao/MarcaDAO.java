package dao;

import database.HibernateUtil;
import model.Marca;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) para la entidad {@link Marca}.
 *
 * <p>Encapsula todas las operaciones CRUD sobre la tabla {@code marcas}.</p>
 */
public class MarcaDAO {

    /**
     * Guarda una nueva marca en la base de datos.
     *
     * @param marca La marca a persistir.
     * @return La marca con el ID generado asignado.
     */
    public Marca guardar(Marca marca) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(marca);
            tx.commit();
            return marca;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar la marca: " + e.getMessage(), e);
        }
    }

    /**
     * Busca una marca por su ID.
     *
     * @param id El identificador de la marca.
     * @return Un Optional con la marca encontrada, o vacío si no existe.
     */
    public Optional<Marca> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return Optional.ofNullable(session.get(Marca.class, id));
        }
    }

    /**
     * Busca una marca por su nombre (insensible a mayúsculas).
     *
     * @param nombre El nombre de la marca a buscar.
     * @return Un Optional con la marca encontrada, o vacío si no existe.
     */
    public Optional<Marca> buscarPorNombre(String nombre) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "FROM Marca m WHERE LOWER(m.nombre) = LOWER(:nombre)", Marca.class)
                    .setParameter("nombre", nombre)
                    .uniqueResultOptional();
        }
    }

    /**
     * Devuelve todas las marcas registradas.
     *
     * @return Lista de todas las marcas.
     */
    public List<Marca> listarTodas() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Marca ORDER BY nombre", Marca.class)
                    .list();
        }
    }

    /**
     * Actualiza los datos de una marca existente.
     *
     * @param marca La marca con los datos actualizados.
     * @return La marca actualizada.
     */
    public Marca actualizar(Marca marca) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            Marca actualizada = session.merge(marca);
            tx.commit();
            return actualizada;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar la marca: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una marca por su ID.
     *
     * @param id El identificador de la marca a eliminar.
     */
    public void eliminar(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            Marca marca = session.get(Marca.class, id);
            if (marca != null) {
                session.remove(marca);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar la marca: " + e.getMessage(), e);
        }
    }

    /**
     * Busca o crea una marca por nombre.
     * Si ya existe en BD devuelve la existente; si no, la crea.
     *
     * @param nombreMarca El nombre de la marca.
     * @return La marca existente o la nueva creada.
     */
    public Marca buscarOCrear(String nombreMarca) {
        return buscarPorNombre(nombreMarca)
                .orElseGet(() -> guardar(new Marca(nombreMarca)));
    }
}
