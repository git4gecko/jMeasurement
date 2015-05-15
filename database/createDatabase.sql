CREATE TABLE sensor ( id SMALLINT, sensorId VARCHAR(64), active TINYINT, name VARCHAR(128));
CREATE TABLE bmp085 ( key_sensor SMALLINT, pressure DOUBLE, temperature DOUBLE, time DATETIME);

INSERT INTO sensor (id, sensorId, active, name) values(1, "0x77", 1, "bmp085 innen"); 