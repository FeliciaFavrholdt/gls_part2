package dk.favrholdt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

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

    // Bidirectional one-to-many relationship - SHIPMENT

    @OneToMany(mappedBy = "sourceLocation")
    @ToString.Exclude
    @Builder.Default
    private Set<Shipment> shipmentsAsSource = new HashSet<>();

    @OneToMany(mappedBy = "destinationLocation")
    @ToString.Exclude
    @Builder.Default
    private Set<Shipment> shipmentsAsDestination = new HashSet<>();

    public void addShipmentAsSource(Shipment shipment) {
        if (shipment != null) {
            shipmentsAsSource.add(shipment);
            shipment.setSourceLocation(this);
        }
    }

    public void addShipmentAsDestination(Shipment shipment) {
        if (shipment != null) {
            shipmentsAsDestination.add(shipment);
            shipment.setDestinationLocation(this);
        }
    }

    public void removeShipmentAsSource(Shipment shipment) {
        if (shipment != null) {
            shipmentsAsSource.remove(shipment);
            shipment.setSourceLocation(null);
        }
    }

    public void removeShipmentAsDestination(Shipment shipment) {
        if (shipment != null) {
            shipmentsAsDestination.remove(shipment);
            shipment.setDestinationLocation(null);
        }
    }
}