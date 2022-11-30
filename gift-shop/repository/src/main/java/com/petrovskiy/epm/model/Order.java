package com.petrovskiy.epm.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_cost", nullable = false)
    /*@Pattern(regexp = "^(\\d+|[\\.\\,]?\\d+){1,2}$")*/
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinTable(name="order_has_products"
            ,joinColumns = @JoinColumn(name = "orders_id", referencedColumnName = "id")
            ,inverseJoinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id"))
    private List<Product> productList = new ArrayList<>();


    @PrePersist
    private void prePersist(){
        orderDate = LocalDateTime.now();
        cost =  productList.stream().map(Product::getPrice).reduce(BigDecimal.ONE,BigDecimal::add);
    }
}
