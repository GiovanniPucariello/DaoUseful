package daouseful.dao.cfg;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 *
 * @author Daniel Röhers Moura
 */
public class HibernateUseful {

    private static SessionFactory sessionFactory = null;
    private static Properties prop = null;

    public static synchronized SessionFactory getSessionFactory(boolean updateTables) {
        if (sessionFactory == null) {
            Configuration cfg = null;
            cfg = new AnnotationConfiguration().configure("hibernate.cfg.xml");

            if (prop != null) {
                cfg.addProperties(prop);
            }
            
            if(updateTables) {
                cfg.setProperty(Environment.HBM2DDL_AUTO, "update");
                new SchemaUpdate(cfg).execute(true, true);
                new SchemaExport(cfg).execute(true  /* script */,
                                                true  /* export */,
                                                false /* justDrop */,
                                                true  /* justCreate */);
            }
            try {
                sessionFactory = cfg.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionFactory;
    }

    public static void main(String[] args) {
        getSessionFactory(true);
    }
}