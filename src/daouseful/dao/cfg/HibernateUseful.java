package daouseful.dao.cfg;

import daouseful.dto.AliasSearch;
import daouseful.useful.Useful;
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
    private static Properties properties = null;

    /**
     * 
     * @param updateTables
     * @return 
     */
    public static synchronized SessionFactory getSessionFactory(boolean updateTables) {
        if (sessionFactory == null) {
            Configuration configuration = null;
            configuration = new AnnotationConfiguration().configure("hibernate.cfg.xml");

            if (properties != null) {
                configuration.addProperties(properties);
            }
            if(updateTables) {
                configuration.setProperty(Environment.HBM2DDL_AUTO, "update");
                new SchemaUpdate(configuration).execute(true, true);
                new SchemaExport(configuration).execute(true  /* script */,
                                                true  /* export */,
                                                false /* justDrop */,
                                                true  /* justCreate */);
            }
            try {
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                Useful.exceptionMessageConsole(e);
            }
        }
        return sessionFactory;
    }

    public static void main(String[] args) {
        getSessionFactory(true);
    }
}