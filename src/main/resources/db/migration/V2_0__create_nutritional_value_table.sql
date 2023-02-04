CREATE TABLE nutritional_value (
   id BIGINT AUTO_INCREMENT NOT NULL,
   product_id BIGINT,
   nutrient_type VARCHAR(255) NOT NULL,
   macro_nutrient VARCHAR(255) NOT NULL,
   micro_nutrient VARCHAR(255) NOT NULL,
   nutrient_unit VARCHAR(255) NOT NULL,
   nutrient_value DOUBLE NOT NULL,
   CONSTRAINT pk_nutritional_value PRIMARY KEY (id)
);

ALTER TABLE nutritional_value ADD CONSTRAINT FK_NUTRITIONAL_VALUE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);
