package dk.favrholdt.persistence;

import dk.favrholdt.entities.Location;
import dk.favrholdt.entities.Package;
import dk.favrholdt.enums.HibernateConfigState;
import dk.favrholdt.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class LocationDAO implements iDAO<Location> {

    private static LocationDAO instance;
    private static EntityManagerFactory emf;

    private LocationDAO(){
    }

    public static LocationDAO getInstance(HibernateConfigState state) {
        if(instance == null){
            emf = HibernateConfig.getEntityManagerFactoryConfig(state, "gls");
            instance = new LocationDAO();
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
    public Location create(Location aLocation) {
        return aLocation;
    }

    @Override
    public Location findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return  em.find(Location.class, id);
        } catch (Exception e) {
            throw new JpaException("Error finding package: " + e.getMessage());
        }
    }

    @Override
    public Location findByTrackingNumber(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<Package> query = em.createQuery("SELECT l FROM Package l WHERE l.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by tracking number: " + e.getMessage());
        }
    }

    public Location findByAddress(String address) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l WHERE l.address = :address", Location.class);
            query.setParameter("address", address);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding location by address: " + e.getMessage());
        }
    }

    @Override
    public Location update(Location aLocation) {
        Location updatedLocation = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedLocation = em.merge(aLocation);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error updating package: " + e.getMessage());
        }
        return updatedLocation;
    }

    @Override
    public boolean delete(Location aLocation) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(aLocation);
            em.getTransaction().commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}