package com.EntregableAndres.Entity;

import com.EntregableAndres.Entity.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Orders")  // Mapea esta clase a la tabla 'Orders'
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")  // Cambiado para coincidir con la BD
    private int id;
    @Column(name = "OrderDate")  // Cambiado para coincidir con la BD
    @Temporal(TemporalType.DATE) // Esto le indica a JPA que solo guarde la parte de la fecha
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Añadir el formato esperado de la fecha
    private Date orderDate;
    @Column(name = "TotalAmount")  // Cambiado para coincidir con la BD
    private double totalAmount;

    @Column(name = "CustomerId")  // Cambiado para coincidir con la BD
    private int customerId;

    // Usamos fetch = FetchType.LAZY para habilitar la carga perezosa
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;


}
