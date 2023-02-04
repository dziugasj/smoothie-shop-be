CREATE TABLE product (
   id BIGINT AUTO_INCREMENT NOT NULL,
   product_type VARCHAR(255) NOT NULL,
   name VARCHAR(255),
   basic_details VARCHAR(255),
   CONSTRAINT pk_product PRIMARY KEY (id)
);

