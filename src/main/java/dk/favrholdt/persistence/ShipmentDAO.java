package dk.favrholdt.persistence;

import dk.favrholdt.entities.Location;
import dk.favrholdt.entities.Shipment;
import dk.favrholdt.enums.HibernateConfigState;
import dk.favrholdt.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ShipmentDAO implements iDAO<Shipment> {

    private static ShipmentDAO instance;
    private static EntityManagerFactory emf;

    private ShipmentDAO() {
    }

    public static ShipmentDAO getInstance(HibernateConfigState state) {
        if(instance == null) {
            emf = HibernateConfig.getEntityManagerFactoryConfig(state, "gls");
            instance = new ShipmentDAO();
        }
        return instance;
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static void close() {
        if(emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public Shipment create(Shipment aShipment) {
        return aShipment;
    }

    @Override
    public Shipment findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return  em.find(Shipment.class, id);
        } catch (Exception e) {
            throw new JpaException("Error finding shipment: " + e.getMessage());
        }
    }

    @Override
    public Shipment findByTrackingNumber(String trackingNumber) {
        return null;
    }

    @Override
    public Shipment update(Shipment aShipment) {
        Shipment updatedShipment = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedShipment = em.merge(aShipment);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error updating package: " + e.getMessage());
        }
        return updatedShipment;
    }

    @Override
    public boolean delete(Shipment aShipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(aShipment);
            em.getTransaction().commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //find all shipments
    public List<Shipment> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("SELECT s FROM Shipment s", Shipment.class);
            query.setMaxResults(1);
            return query.getResultList();
        } catch (Exception e) {
            throw new JpaException("Error finding all shipments: " + e.getMessage());
        }
    }

    //find by source location
    public Shipment findBySourceLocation(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("SELECT s FROM Shipment s WHERE s.sourceLocation = :location", Shipment.class);
            query.setParameter("location", location);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding shipment by source location: " + e.getMessage());
        }
    }

    //find by destination location
    public Shipment findByDestinationLocation(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("SELECT s FROM Shipment s WHERE s.destinationLocation = :location", Shipment.class);
            query.setParameter("location", location);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding shipment by destination location: " + e.getMessage());
        }
    }

    //find by date
    public Shipment findByDate(String date) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("SELECT s FROM Shipment s WHERE s.shipmentDateTime = :date", Shipment.class);
            query.setParameter("date", date);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding shipment by date: " + e.getMessage());
        }
    }

    //find by sender

    //find by receiver

    //find by package



}
