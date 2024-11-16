package com.EntregableAndres.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "OrderItems")  // Mapea esta clase a la tabla 'OrderItems'
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relación ManyToOne: Muchos artículos pueden pertenecer a un solo pedido
    @ManyToOne
    @JoinColumn(name = "OrderId")  // Referencia a la columna 'OrderId' en la tabla 'OrderItems'
    private Order order;



    // Relación ManyToOne: Muchos artículos pueden corresponder a un solo producto
    @ManyToOne
    @JoinColumn(name = "ProductId")  // Referencia a la columna 'ProductId' en la tabla 'OrderItems'
    private Product product;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "UnitPrice")
    private double unitPrice;


}
