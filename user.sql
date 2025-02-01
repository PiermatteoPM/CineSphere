-- MySQL Script generated by MySQL Workbench
-- Wed Nov 13 23:19:59 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema user
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema user
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `user` DEFAULT CHARACTER SET utf8mb3 ;
USE `user` ;

-- -----------------------------------------------------
-- Table `user`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user`.`user` (
  `email` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `supervisor` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`email`, `username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  INDEX `idx_email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `user`.`generi_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user`.`generi_user` (
  `Azione` TINYINT NULL DEFAULT NULL,
  `Avventura` TINYINT NULL DEFAULT NULL,
  `Animazione` TINYINT NULL DEFAULT NULL,
  `Biografico` TINYINT NULL DEFAULT NULL,
  `Commedia` TINYINT NULL DEFAULT NULL,
  `Poliziesco` TINYINT NULL DEFAULT NULL,
  `Documentario` TINYINT NULL DEFAULT NULL,
  `Drammatico` TINYINT NULL DEFAULT NULL,
  `PerFamiglie` TINYINT NULL DEFAULT NULL,
  `Fantastico` TINYINT NULL DEFAULT NULL,
  `Noir` TINYINT NULL DEFAULT NULL,
  `GiocoAPremiTelevisivo` TINYINT NULL DEFAULT NULL,
  `Storico` TINYINT NULL DEFAULT NULL,
  `Horror` TINYINT NULL DEFAULT NULL,
  `Musica` TINYINT NULL DEFAULT NULL,
  `Musical` TINYINT NULL DEFAULT NULL,
  `Giallo` TINYINT NULL DEFAULT NULL,
  `Telegiornale` TINYINT NULL DEFAULT NULL,
  `Reality` TINYINT NULL DEFAULT NULL,
  `Sentimentale` TINYINT NULL DEFAULT NULL,
  `Fantascienza` TINYINT NULL DEFAULT NULL,
  `Cortometraggio` TINYINT NULL DEFAULT NULL,
  `Sportivo` TINYINT NULL DEFAULT NULL,
  `TalkShow` TINYINT NULL DEFAULT NULL,
  `Thriller` TINYINT NULL DEFAULT NULL,
  `Guerra` TINYINT NULL DEFAULT NULL,
  `Western` TINYINT NULL DEFAULT NULL,
  `email` VARCHAR(45) NOT NULL,
  INDEX `fk_generi_user_user1_idx` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_generi_user_user1`
    FOREIGN KEY (`email`)
    REFERENCES `user`.`user` (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `user`.`notifiche`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user`.`notifiche` (
  `body` VARCHAR(512) NULL DEFAULT NULL,
  `email` VARCHAR(45) NOT NULL,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  INDEX `fk_notifiche_user1` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_notifiche_user1`
    FOREIGN KEY (`email`)
    REFERENCES `user`.`user` (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `user`.`collection_utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user`.`collection_utente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nameCollection` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `link` VARCHAR(512) NOT NULL,
  `approved` INT NULL DEFAULT '0',
  `Azione` TINYINT NULL DEFAULT NULL,
  `Avventura` TINYINT NULL DEFAULT NULL,
  `Animazione` TINYINT NULL DEFAULT NULL,
  `Biografico` TINYINT NULL DEFAULT NULL,
  `Commedia` TINYINT NULL DEFAULT NULL,
  `Poliziesco` TINYINT NULL DEFAULT NULL,
  `Documentario` TINYINT NULL DEFAULT NULL,
  `Drammatico` TINYINT NULL DEFAULT NULL,
  `PerFamiglie` TINYINT NULL DEFAULT NULL,
  `Fantastico` TINYINT NULL DEFAULT NULL,
  `Noir` TINYINT NULL DEFAULT NULL,
  `GiocoAPremiTelevisivo` TINYINT NULL DEFAULT NULL,
  `Storico` TINYINT NULL DEFAULT NULL,
  `Horror` TINYINT NULL DEFAULT NULL,
  `Musica` TINYINT NULL DEFAULT NULL,
  `Musical` TINYINT NULL DEFAULT NULL,
  `Giallo` TINYINT NULL DEFAULT NULL,
  `Telegiornale` TINYINT NULL DEFAULT NULL,
  `Reality` TINYINT NULL DEFAULT NULL,
  `Sentimentale` TINYINT NULL DEFAULT NULL,
  `Fantascienza` TINYINT NULL DEFAULT NULL,
  `Cortometraggio` TINYINT NULL DEFAULT NULL,
  `Sportivo` TINYINT NULL DEFAULT NULL,
  `TalkShow` TINYINT NULL DEFAULT NULL,
  `Thriller` TINYINT NULL DEFAULT NULL,
  `Guerra` TINYINT NULL DEFAULT NULL,
  `Western` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `nameCollection`, `link`),
  INDEX `fk_Collection_Utente_user1_idx` (`email` ASC) VISIBLE,
  INDEX `collection_utente_user_idx` (`username` ASC) VISIBLE,
  CONSTRAINT `collection_utente_user`
    FOREIGN KEY (`username`)
    REFERENCES `user`.`user` (`username`))
ENGINE = InnoDB
AUTO_INCREMENT = 170
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
