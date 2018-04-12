package net.muroc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Brendan on 4/11/2018.
 */
public class coco_flight_aware {
    public static SessionFactory sessionFactory;

    public static void main (String[] args)
    {
        PostGRES_SessionFactory postGRES_sessionFactory = new PostGRES_SessionFactory();
        //session = postGRES_sessionFactory.createSession();
        //SessionFactory sessionFactory = postGRES_sessionFactory.createSession();
        sessionFactory = postGRES_sessionFactory.createSession();
        Flight_Aware_Harvester harvester = new Flight_Aware_Harvester();
        harvester.GetAircraftData(sessionFactory);
    }
}
