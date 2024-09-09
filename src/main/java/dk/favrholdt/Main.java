package dk.favrholdt;

import dk.favrholdt.entities.Location;
import dk.favrholdt.entities.Package;
import dk.favrholdt.entities.Shipment;
import dk.favrholdt.enums.DeliveryStatus;
import dk.favrholdt.enums.HibernateConfigState;
import dk.favrholdt.persistence.PackageDAO;

public class Main {
    public static void main(String[] args) {
  /*      System.out.println("GLS Package System");
        PackageDAO packageDAO = PackageDAO.getInstance(HibernateConfigState.NORMAL);

        Package aPackage = Package.builder()
                .trackingNumber("123456789")
                .sender("Mads Hansen")
                .receiver("Victor Jensen")
                .deliveryStatus(DeliveryStatus.PENDING)
                .createdDateTime(null)
                .build();

        System.out.println("Før: \n" + aPackage);
        aPackage = packageDAO.create(aPackage);
        System.out.println("Efter: \n" + aPackage);

        Package foundPackage = packageDAO.findById(1L);
        System.out.println("Found package: \n" + foundPackage);

        Package foundPackageByTrackingNumber = packageDAO.findByTrackingNumber("123456789");
        System.out.println("Found package by tracking number: \n" + foundPackageByTrackingNumber);

        boolean deleted = packageDAO.delete(foundPackageByTrackingNumber);
        System.out.println("Deleted: " + deleted);
        deleted = packageDAO.delete(foundPackageByTrackingNumber);
        System.out.println("Deleted: " + deleted);

        try {
            Package updatedPackage = Package.builder()
                .id(foundPackageByTrackingNumber.getId())
                .trackingNumber(foundPackageByTrackingNumber.getTrackingNumber())
                .sender("Benny Balle")
                .receiver(foundPackageByTrackingNumber.getReceiver())
                .createdDateTime(foundPackageByTrackingNumber.getCreatedDateTime())
                .deliveryStatus(DeliveryStatus.DELIVERED).build();

            foundPackageByTrackingNumber = packageDAO.update(updatedPackage);
            System.out.println("Updated: \n" + foundPackageByTrackingNumber);
        }
        catch (JpaException e) {
            System.out.println("Den gik sgu ikke granberg\n" + e.getMessage());
        }*/

        System.out.println("GLS Package System part 2");
        PackageDAO packageDAO = PackageDAO.getInstance(HibernateConfigState.NORMAL);

        Package aPackage = Package.builder()
                .trackingNumber("123456789")
                .sender("Mads Hansen")
                .receiver("Victor Jensen")
                .deliveryStatus(DeliveryStatus.PENDING)
                .createdDateTime(null)
                .build();

        Location sourceLocation = Location.builder()
                .latitude(11.11)
                .longitude(22.22)
                .address("København")
                .build();

        Location destinationLocation = Location.builder()
                .latitude(33.33)
                .longitude(44.44)
                .address("Aarhus")
                .build();

        Shipment shipment = Shipment.builder()
                .shippedPackage(aPackage)
                .shipmentDateTime(null)
                .build();

        shipment.addSourceLocation(sourceLocation);
        shipment.addDestinationLocation(destinationLocation);
        aPackage.addShipment(shipment);
        packageDAO.create(aPackage);
    }
}