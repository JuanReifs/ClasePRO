package database;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase utilitaria que gestiona el SessionFactory de Hibernate.
 * Implementa el patrón Singleton: solo existe una instancia del SessionFactory
 * durante toda la ejecución de la aplicación (es costoso de crear).
 */
@Getter
@Setter
public class HibernateUtil {

    // Se inicializa una sola vez cuando se carga la clase
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Lee hibernate.cfg.xml del classpath y construye el SessionFactory
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear el SessionFactory: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    /*
     * Devuelve el SessionFactory singleton.
     * A partir de él se abren las sesiones (Session) para operar con la BD.
     */

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra el SessionFactory al terminar la aplicación.
     * Libera todas las conexiones del pool.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}