package dk.favrholdt;

import dk.favrholdt.entities.Package;
import dk.favrholdt.enums.DeliveryStatus;
import dk.favrholdt.enums.HibernateConfigState;
import dk.favrholdt.exceptions.JpaException;
import dk.favrholdt.persistence.PackageDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PackageDAOTest {

    private static final PackageDAO packageDAO = PackageDAO.getInstance(HibernateConfigState.TEST);
    private static Package p1, p2, p3;

    @BeforeAll
    void setUpAll() {
    }

    @BeforeEach
    void setUp() {
        EntityManagerFactory emf = packageDAO.getEmf();

        // Reset table and sequence before each test
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Package").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE package_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error deleting packages: " + e.getMessage());
        }

        // Create test data
        p1 = Package.builder()
                .trackingNumber("123456789")
                .sender("Mads Hansen")
                .receiver("Victor Jensen")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();
        p2 = Package.builder()
                .trackingNumber("987654321")
                .sender("Julie Madsen")
                .receiver("Sif Jakobsen")
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .build();
        p3 = Package.builder()
                .trackingNumber("123456987")
                .sender("Karen Skjødt")
                .receiver("Villads Skov")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();
        packageDAO.create(p1);
        packageDAO.create(p2);
        packageDAO.create(p3);
    }

    @AfterAll
    static void tearDown() {
        packageDAO.close();
    }

    @Test
    void getInstance() {
        assertNotNull(packageDAO);
    }

    @Test
    void create() {
        Package p4 = Package.builder()
                            .trackingNumber("121231234")
                            .sender("Rasmus Børgesen")
                            .receiver("Kenneth B. Jensen")
                            .deliveryStatus(DeliveryStatus.PENDING)
                            .build();
        packageDAO.create(p4);
        assertEquals(4, p4.getId());
    }

    @Test
    void findById() {
        Package actual = packageDAO.findById(p1.getId());
        assertEquals(p1, actual);
    }

    @Test
    void findByTrackingNumber() {
        Package actual = packageDAO.findByTrackingNumber(p1.getTrackingNumber());
        assertEquals(p1, actual);
    }

    @Test
    void update() {
        Package updated = Package.builder()
                .id(p1.getId())
                .trackingNumber(p1.getTrackingNumber())
                .sender(p1.getSender())
                .receiver(p1.getReceiver())
                .createdDateTime(p1.getCreatedDateTime())
                .updatedDateTime(p1.getUpdatedDateTime())
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .build();
        assertNotEquals(p1.getDeliveryStatus(), updated.getDeliveryStatus());
        Package actual = packageDAO.update(updated);
        // This needs an equals method in the Package class to work
        assertEquals(updated, actual);
    }

    @Test
    void delete() {
        boolean actual = packageDAO.delete(p1);
        assertTrue(actual);
        actual = packageDAO.delete(p1);
        assertFalse(actual);
        Package deleted = packageDAO.findById(p1.getId());
        assertNull(deleted);
    }
}