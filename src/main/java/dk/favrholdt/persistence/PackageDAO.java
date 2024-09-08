package dk.favrholdt.persistence;

import dk.favrholdt.exceptions.JpaException;
import dk.favrholdt.entities.Package;
import dk.favrholdt.enums.HibernateConfigState;
import jakarta.persistence.*;

public class PackageDAO implements iDAO<Package> {

    private static PackageDAO instance;
    private static EntityManagerFactory emf;

    private PackageDAO(){
    }

    public static PackageDAO getInstance(HibernateConfigState state){
        if(instance == null){
            emf = HibernateConfig.getEntityManagerFactoryConfig(state, "gls");
            instance = new PackageDAO();
        }
        return instance;
    }

    public static EntityManagerFactory getEmf(){
        return emf;
    }

    public static void close(){
        if(emf != null && emf.isOpen()){
            emf.close();
        }
    }

    @Override
    public Package create(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(aPackage);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error creating package: " + e.getMessage());
        }
        return aPackage;
    }

    @Override
    public Package findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return  em.find(Package.class, id);
        } catch (Exception e) {
            throw new JpaException("Error finding package: " + e.getMessage());
        }
    }

    @Override
    public Package findByTrackingNumber(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by tracking number: " + e.getMessage());
        }
    }

    @Override
    public Package update(Package aPackage) {
        Package updatedPackage = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedPackage = em.merge(aPackage);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error updating package: " + e.getMessage());
        }
        return updatedPackage;
    }

    @Override
    public boolean delete(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(aPackage);
            em.getTransaction().commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

