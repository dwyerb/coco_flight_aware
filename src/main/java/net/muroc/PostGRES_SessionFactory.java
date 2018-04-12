package net.muroc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by Brendan on 4/11/2018.
 */
public class PostGRES_SessionFactory {
    public SessionFactory createSession()
    {
        //return session factory so each transaction can create it own session
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
        registry.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        //Session session = sessionFactory.openSession();
        return sessionFactory;
    }
}
