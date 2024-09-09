package dk.favrholdt.persistence;

import dk.favrholdt.entities.Shipment;
import dk.favrholdt.exceptions.JpaException;
import dk.favrholdt.entities.Package;
import dk.favrholdt.enums.HibernateConfigState;
import jakarta.persistence.*;

import java.util.List;

public class PackageDAO implements iDAO<Package> {

    private static PackageDAO instance;
    private static EntityManagerFactory emf;

    private PackageDAO() {
    }

    public static PackageDAO getInstance(HibernateConfigState state) {
        if (instance == null) {
            emf = HibernateConfig.getEntityManagerFactoryConfig(state, "gls");
            instance = new PackageDAO();
        }
        return instance;
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public Package create(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            if (aPackage.getId() == null) {
                em.persist(aPackage);
            } else {
                aPackage = em.merge(aPackage);
            }

     /*       //set the package for each shipment
            for (Shipment shipment : aPackage.getShipments()) {
                shipment.setShippedPackage(aPackage);

                //set the location for each shipment
                if (shipment.getSourceLocation() != null && shipment.getDestinationLocation() != null) {
                    shipment.setSourceLocation(em.merge(shipment.getSourceLocation()));
                    shipment.setDestinationLocation(em.merge(shipment.getDestinationLocation()));
                }

                //persist the shipment
                if (shipment.getId() == null) {
                    em.persist(shipment);
                } else {
                    em.merge(shipment);
                }*/

            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error creating package: " + e.getMessage());
        }
        return aPackage;
    }


    @Override
    public Package findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Package.class, id);
        } catch (Exception e) {
            throw new JpaException("Error finding package: " + e.getMessage());
        }
    }

    @Override
    public Package findByTrackingNumber(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
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

    //find by delivery status
    public Package findByDeliveryStatus(String deliveryStatus) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.deliveryStatus = :deliveryStatus", Package.class);
            query.setParameter("deliveryStatus", deliveryStatus);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by delivery status: " + e.getMessage());
        }
    }

    //find all packages
    public List<Package> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p", Package.class);
            query.setMaxResults(1);
            return query.getResultList();
        } catch (Exception e) {
            throw new JpaException("Error finding all packages: " + e.getMessage());
        }
    }

    //find by sender
    public Package findBySender(String sender) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.sender = :sender", Package.class);
            query.setParameter("sender", sender);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by sender: " + e.getMessage());
        }
    }

    //find by receiver
    public Package findByReceiver(String receiver) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.receiver = :receiver", Package.class);
            query.setParameter("receiver", receiver);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by receiver: " + e.getMessage());
        }
    }

    //find by created date time
    public Package findByCreatedDateTime(String createdDateTime) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.createdDateTime = :createdDateTime", Package.class);
            query.setParameter("createdDateTime", createdDateTime);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by created date time: " + e.getMessage());
        }
    }


}

