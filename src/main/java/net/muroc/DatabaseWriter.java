package net.muroc;
import org.hibernate.SessionFactory;
import java.util.List;

import java.util.List;
/**
 * Created by Brendan on 4/11/2018.
 */
public interface DatabaseWriter {
    public void write_data_to_database(List<FlightAwarePointType> FAPoints, SessionFactory sessionFactory);
}
