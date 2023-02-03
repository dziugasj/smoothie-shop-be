CREATE TABLE client_order (
   id BIGINT AUTO_INCREMENT NOT NULL,
   product_id BIGINT,
   created_time date,
   quantity INT NOT NULL,
   CONSTRAINT pk_order PRIMARY KEY (id)
);

ALTER TABLE client_order ADD CONSTRAINT FK_ORDER_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);
