package dk.favrholdt.entities;

import dk.favrholdt.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "package", indexes = @Index(name = "idx_tracking_number", columnList = "tracking_number"))
public class Package {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "tracking_number", nullable = false, unique = true, length = 10)
    private String trackingNumber;

    @Column(name = "sender", nullable = false, length = 40)
    private String sender;

    @Column(name = "receiver", nullable = false, length = 40)
    private String receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @ToString.Exclude
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;

    @OneToMany(mappedBy = "shippedPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private Set<Shipment> shipments = new HashSet<>();


    @PrePersist
    public void prePersist() {
        if (createdDateTime == null) {
            createdDateTime = LocalDateTime.now();
        }
        if (updatedDateTime == null) {
            updatedDateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDateTime = LocalDateTime.now();
    }

    // Bidirectional one-to-many relationship - SHIPMENT
    public void addShipment(Shipment shipment) {
        if (shipment != null) {
            this.shipments.add(shipment);
            shipment.setShippedPackage(this);
        }
    }

    public void removeShipment(Shipment shipment) {
        if (shipment != null) {
            this.shipments.remove(shipment);
            shipment.setShippedPackage(null);
        }
    }
}
