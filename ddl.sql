CREATE TABLE `product`
(
    `product_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(255) NOT NULL,
    `price`          BIGINT       NOT NULL,
    `stock_quantity` BIGINT       NOT NULL,
    PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `idempotency`
(
    `idempotency_key` VARCHAR(255) NOT NULL,
    `response`        TEXT,
    `create_at`       DATETIME     NOT NULL,
    PRIMARY KEY (`idempotency_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELIMITER
$$

CREATE PROCEDURE InsertProducts()
BEGIN
  DECLARE
i INT DEFAULT 1;
  WHILE
i <= 100 DO
    INSERT INTO product (name, price, stock_quantity)
    VALUES (CONCAT('Product ', i), 100, 10);
    SET
i = i + 1;
END WHILE;
END $$

DELIMITER ;


CREATE TABLE `orders` (
                       order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       order_price BIGINT
);

CREATE TABLE `order_item` (
                            order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            product_id BIGINT,
                            item_name VARCHAR(255),
                            item_unit_price BIGINT,
                            order_quantity BIGINT,
                            order_id BIGINT

);




docker exec whatap_rabbitmq1_1 rabbitmq-plugins enable rabbitmq_management
docker exec whatap_rabbitmq2_1 rabbitmq-plugins enable rabbitmq_management

docker exec whatap_rabbitmq2_1 rabbitmqctl stop_app
docker exec whatap_rabbitmq2_1 rabbitmqctl reset
docker exec whatap_rabbitmq2_1 rabbitmqctl join_cluster rabbit@rabbitmq1
docker exec whatap_rabbitmq2_1 rabbitmqctl start_app
docker exec whatap_rabbitmq1_1 rabbitmqctl set_policy ha-all ".*" '{"ha-mode":"all"}' --priority 1