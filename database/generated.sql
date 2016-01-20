-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema measurement
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema measurement
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `measurement` DEFAULT CHARACTER SET utf8 ;
USE `measurement` ;

-- -----------------------------------------------------
-- Table `measurement`.`sensor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `measurement`.`sensor` (
  `sensorId` VARCHAR(45) NOT NULL,
  `active` TINYINT(1) NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `type` INT NOT NULL,
  PRIMARY KEY (`sensorId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `measurement`.`DS18B20`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `measurement`.`DS18B20` (
  `idDS18B20` INT NOT NULL,
  `value` FLOAT NOT NULL,
  `at` TIMESTAMP NOT NULL,
  `sensor_sensorId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDS18B20`),
  INDEX `fk_DS18B20_sensor_idx` (`sensor_sensorId` ASC),
  CONSTRAINT `fk_DS18B20_sensor`
    FOREIGN KEY (`sensor_sensorId`)
    REFERENCES `measurement`.`sensor` (`sensorId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE USER 'measure' IDENTIFIED BY '6E8AUsK1P9WeboQX95EZ';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
