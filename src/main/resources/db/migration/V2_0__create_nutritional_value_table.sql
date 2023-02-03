CREATE TABLE nutritional_value (
   id BIGINT AUTO_INCREMENT NOT NULL,
   product_id BIGINT,
   nutritional_value_type VARCHAR(255),
   nutritional_value_unit VARCHAR(255),
   name VARCHAR(255),
   CONSTRAINT pk_nutritional_value PRIMARY KEY (id)
);

ALTER TABLE nutritional_value ADD CONSTRAINT FK_NUTRITIONAL_VALUE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);
