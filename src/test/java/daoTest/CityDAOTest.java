package daoTest;

import com.javarush.dao.CityDAO;
import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class CityDAOTest {
    private static SessionFactory sessionFactory;
    private CityDAO cityDAO;

    @BeforeAll
    static void setUpAll() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .addProperties(properties)
                .buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        cityDAO = new CityDAO(sessionFactory);
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void testGetById() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            City city = cityDAO.getById(1);
            session.getTransaction().commit();

            assertNotNull(city);
            assertNotNull(city.getName());
            assertNotNull(city.getCountry());
        }
    }

    @Test
    void testGetItems() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<City> cities = cityDAO.getItems(0, 10);
            session.getTransaction().commit();

            assertNotNull(cities);
            assertEquals(10, cities.size());
        }
    }

    @Test
    void testGetTotalCount() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            int count = cityDAO.getTotalCount();
            session.getTransaction().commit();

            assertTrue(count > 0);
        }
    }
}
