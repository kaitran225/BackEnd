USE defaultdb;
-- USE SWP391Healthy;
DELIMITER //

CREATE PROCEDURE CreateIndexIfNotExists(
    IN tableName VARCHAR(64),
    IN indexName VARCHAR(64),
    IN indexDefinition VARCHAR(255)
)
BEGIN
    DECLARE indexExists INT DEFAULT 0;

    SELECT COUNT(*)
    INTO indexExists
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
    AND table_name = tableName
    AND index_name = indexName;

    IF indexExists = 0 THEN
        SET @query = CONCAT('CREATE INDEX ', indexName, ' ON ', tableName, ' ', indexDefinition);
        PREPARE stmt FROM @query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END //

DELIMITER ;