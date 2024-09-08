package dk.favrholdt.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipped_package_id", nullable = false, length = 10)
    private Package shippedPackage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_location_id", nullable = false, length = 10)
    private Location sourceLocation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_location_id", nullable = false, length = 10)
    private Location destinationLocation;

    @Column(name = "shipment_date_time", nullable = false)
    private LocalDateTime shipmentDateTime;

    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;

    @PrePersist
    protected void onCreate() {
        createdDateTime = LocalDateTime.now();
        updatedDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDateTime = LocalDateTime.now();
    }

    public void addSourceLocation(Location location) {
        if (location != null) {
            this.sourceLocation = location;
            location.getShipmentsAsSource().add(this);
        }
    }

    public void addDestinationLocation(Location location) {
        if (location != null) {
            this.destinationLocation = location;
            location.getShipmentsAsDestination().add(this);
        }
    }
}