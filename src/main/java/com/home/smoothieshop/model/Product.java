package com.home.smoothieshop.model;

import com.home.smoothieshop.model.enums.ProductType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private String name;

    private String basicDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutritionalValue> nutritionalValues = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientOrder> clientOrders = new ArrayList<>();

    public Product() {
    }

    public void addNutritionalValue(NutritionalValue nutritionalValue) {
        nutritionalValues.add(nutritionalValue);
        nutritionalValue.setProduct(this);
    }

    public void removeNutritionalValue(NutritionalValue nutritionalValue) {
        nutritionalValues.remove(nutritionalValue);
        nutritionalValue.setProduct(null);
    }

    public void addOrder(ClientOrder clientOrder) {
        clientOrders.add(clientOrder);
        clientOrder.setProduct(this);
    }

    public void removeOrder(ClientOrder clientOrder) {
        clientOrders.remove(clientOrder);
        clientOrder.setProduct(null);
    }

    public Long getId() {
        return id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasicDetails() {
        return basicDetails;
    }

    public void setBasicDetails(String basicDetails) {
        this.basicDetails = basicDetails;
    }
}
