package net.muroc;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;


import static net.muroc.coco_flight_aware.*;

/**
 * Created by Brendan on 4/11/2018.
 */
public class PostGRES_DataWriter implements DatabaseWriter
{
    public void write_data_to_database(List<FlightAwarePointType> FAPoints, org.hibernate.SessionFactory sessionFactory)
    {

        Transaction tx = null;
        Session session = sessionFactory.openSession();
        List<FlightAwareTrackType> fatList = session.createCriteria(FlightAwareTrackType.class).list();
        ///get time
        XMLGregorianCalendar currentXMLTime = dateTimeToXMLGregorianCalendar(DateTime.now());
        try {
            tx = session.beginTransaction();
            for (FlightAwarePointType point: FAPoints)
            {
                if (point.getHex()!=null && point.getFlight()!=null && point.getSquawk()!=null)
                {
                    //set current timestamp
                    point.setTimestamp(currentXMLTime);
                    //SearchCriteria search = SearchCriteria.getInstance(point.getHex(),point.getFlight(),point.getSquawk());
                    //List<FlightAwareTrackType> selectedTracks = fatList.stream().filter(search.getCriteria("allFlights")).forEach(FlightAwareTrackType::setFlightAwarePoint(point));
                    //List<FlightAwareTrackType> testSelect = fatList.stream()
                    //        .filter(t -> point.getFlight().equals(t.getFlight().trim()))
                    //       .collect(Collectors.toList());
                    //List<FlightAwareTrackType> selectedTracks = fatList.stream().filter(t -> t.getSquawk()==point.getSquawk() && t.getFlight()==point.getFlight() && t.getHex()==point.getHex()).collect(Collectors.toList());
                    List<FlightAwareTrackType> selectedTracks = fatList.stream()
                            .filter(t -> point.getSquawk().trim().equals(t.getSquawk().trim())
                                    && point.getFlight().trim().equals(t.getFlight().trim())
                                    && point.getHex().trim().equals(t.getHex().trim())).collect(Collectors.toList());
                    if (selectedTracks.isEmpty())
                    {
                        FlightAwareTrackType flightAwareTrackType = new FlightAwareTrackType();
                        flightAwareTrackType.setSquawk(point.getSquawk());
                        flightAwareTrackType.setHex(point.getHex());
                        flightAwareTrackType.setFlight(point.getFlight());
                        flightAwareTrackType.setTimestamp(currentXMLTime);
                        List<FlightAwarePointType> trackPointList = new ArrayList<FlightAwarePointType>();
                        flightAwareTrackType.setFlightAwarePoint(trackPointList);
                        flightAwareTrackType.flightAwarePoint.add(point);
                        session.save(flightAwareTrackType);
                    }
                    else
                    {
                        FlightAwareTrackType flightAwareTrackType = selectedTracks.get(0);
                        flightAwareTrackType.flightAwarePoint.add(point);
                        session.save(flightAwareTrackType);
                    }
                    session.save(point);
                    System.out.println("test");

                }

            }
            tx.commit();
        }
        catch (HibernateException e)
        {
            if(tx!=null) tx.rollback();
            System.out.println("There has been an error");
            e.printStackTrace();
        }
        finally
        {
            List<FlightAwarePointType> pointList = session.createCriteria(FlightAwarePointType.class).list();
            session.close();
            //System.out.println("session closed");
        }
    }

    private static XMLGregorianCalendar dateTimeToXMLGregorianCalendar(DateTime dateTime) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
            xmlGregorianCalendar = dataTypeFactory.newXMLGregorianCalendar(dateTime.toGregorianCalendar());
        }
        catch (Exception e) {
            System.out.println("Exception in conversion of DateTime to XMLGregorianCalendar" + e);
        }
        return xmlGregorianCalendar;
    }

}
