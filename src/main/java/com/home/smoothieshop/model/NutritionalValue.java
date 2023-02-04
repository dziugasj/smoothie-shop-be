package com.home.smoothieshop.model;

import com.home.smoothieshop.model.enums.MacroNutrient;
import com.home.smoothieshop.model.enums.MicroNutrient;
import com.home.smoothieshop.model.enums.NutrientType;
import com.home.smoothieshop.model.enums.NutrientUnit;

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
    @Column(nullable = false)
    private NutrientType nutrientType;

    @Enumerated(EnumType.STRING)
    private MacroNutrient macroNutrient;

    @Enumerated(EnumType.STRING)
    private MicroNutrient microNutrient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NutrientUnit nutrientUnit;

    private double nutrientValue;

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

    public NutrientType getNutrientType() {
        return nutrientType;
    }

    public void setNutrientType(NutrientType nutrientType) {
        this.nutrientType = nutrientType;
    }

    public MacroNutrient getMacroNutrient() {
        return macroNutrient;
    }

    public void setMacroNutrient(MacroNutrient macroNutrient) {
        this.macroNutrient = macroNutrient;
    }

    public MicroNutrient getMicroNutrient() {
        return microNutrient;
    }

    public void setMicroNutrient(MicroNutrient microNutrient) {
        this.microNutrient = microNutrient;
    }

    public NutrientUnit getNutrientUnit() {
        return nutrientUnit;
    }

    public void setNutrientUnit(NutrientUnit nutrientUnit) {
        this.nutrientUnit = nutrientUnit;
    }

    public double getNutrientValue() {
        return nutrientValue;
    }

    public void setNutrientValue(double nutrientValue) {
        this.nutrientValue = nutrientValue;
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
