package com.home.smoothieshop.model;

import com.home.smoothieshop.model.enums.NutritionalValueType;
import com.home.smoothieshop.model.enums.NutritionalValueUnit;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "nutritional_value")
public class NutritionalValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(EnumType.STRING)
    private NutritionalValueType nutritionalValueType;

    @Enumerated(EnumType.STRING)
    private NutritionalValueUnit nutritionalValueUnit;

    private String name;

    public NutritionalValue() {
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public NutritionalValueType getNutritionalValueType() {
        return nutritionalValueType;
    }

    public void setNutritionalValueType(NutritionalValueType nutritionalValueType) {
        this.nutritionalValueType = nutritionalValueType;
    }

    public NutritionalValueUnit getNutritionalValueUnit() {
        return nutritionalValueUnit;
    }

    public void setNutritionalValueUnit(NutritionalValueUnit nutritionalValueUnit) {
        this.nutritionalValueUnit = nutritionalValueUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutritionalValue that = (NutritionalValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
