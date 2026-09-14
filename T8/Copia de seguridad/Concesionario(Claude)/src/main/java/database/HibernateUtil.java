package database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    /** Instancia única del SessionFactory (Singleton). */
    private static SessionFactory sessionFactory;

    // Constructor privado para evitar instanciación
    private HibernateUtil() {}


    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .buildSessionFactory();
                System.out.println("[Hibernate] SessionFactory inicializado correctamente.");
            } catch (Exception e) {
                System.err.println("[Hibernate] Error al inicializar el SessionFactory: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    /**
     * Abre y devuelve una nueva sesión de Hibernate.
     *
     * @return Una sesión activa lista para operar.
     */
    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    /**
     * Cierra el SessionFactory y libera todos los recursos de la conexión.
     * Debe llamarse al finalizar la aplicación.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("[Hibernate] SessionFactory cerrado correctamente.");
        }
    }
}
