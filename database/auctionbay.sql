-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema auctionbay
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `auctionbay` ;

-- -----------------------------------------------------
-- Schema auctionbay
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `auctionbay` DEFAULT CHARACTER SET utf8 ;
USE `auctionbay` ;

-- -----------------------------------------------------
-- Table `auctionbay`.`Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Address` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Address` (
  `AddressID` INT NOT NULL,
  `City` VARCHAR(45) NOT NULL,
  `Street` VARCHAR(45) NOT NULL,
  `Region` VARCHAR(45) NOT NULL,
  `ZipCode` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`AddressID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`User` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`User` (
  `Username` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `AddressID` INT NULL,
  `FirstName` VARCHAR(45) NULL,
  `Lastname` VARCHAR(45) NULL,
  `Email` VARCHAR(45) NULL,
  `TRN` VARCHAR(45) NULL COMMENT 'Tax Registration Number',
  `PhoneNumber` VARCHAR(45) NULL,
  `BidderRating` INT NULL,
  `SellerRating` INT NULL,
  PRIMARY KEY (`Username`),
  INDEX `fk_User_Address1_idx` (`AddressID` ASC),
  CONSTRAINT `fk_User_Address1`
    FOREIGN KEY (`AddressID`)
    REFERENCES `auctionbay`.`Address` (`AddressID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`RegisteredUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`RegisteredUser` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`RegisteredUser` (
  `Username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Username`),
  INDEX `fk_RegisteredUser_User1_idx` (`Username` ASC),
  CONSTRAINT `fk_RegisteredUser_User1`
    FOREIGN KEY (`Username`)
    REFERENCES `auctionbay`.`User` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`Item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Item` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Item` (
  `ItemID` INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Location` VARCHAR(45) NOT NULL,
  `Description` TEXT(2048) NOT NULL,
  `Latitude` DOUBLE NULL,
  `Longitute` DOUBLE NULL,
  PRIMARY KEY (`ItemID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`Auction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Auction` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Auction` (
  `AuctionID` INT NOT NULL,
  `ItemID` INT NOT NULL,
  `Seller` VARCHAR(45) NOT NULL,
  `Title` VARCHAR(45) NOT NULL,
  `BuyPrice` FLOAT NOT NULL,
  `FirstBid` FLOAT NOT NULL,
  `StartTime` DATETIME NOT NULL,
  `EndTime` DATETIME NOT NULL,
  PRIMARY KEY (`AuctionID`),
  INDEX `fk_Auction_Product1_idx` (`ItemID` ASC),
  INDEX `fk_Auction_RegisteredUser1_idx` (`Seller` ASC),
  CONSTRAINT `fk_Auction_Product1`
    FOREIGN KEY (`ItemID`)
    REFERENCES `auctionbay`.`Item` (`ItemID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Auction_RegisteredUser1`
    FOREIGN KEY (`Seller`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`Message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Message` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Message` (
  `MessageID` INT NOT NULL,
  `FromUser` VARCHAR(45) NOT NULL,
  `ToUser` VARCHAR(45) NOT NULL,
  `Subject` VARCHAR(45) NULL,
  `DateCreated` DATETIME NULL,
  `isRead` TINYINT(1) NULL,
  `MessageText` TEXT(2048) NULL,
  PRIMARY KEY (`MessageID`),
  INDEX `fk_Message_RegisteredUser1_idx` (`FromUser` ASC),
  INDEX `fk_Message_RegisteredUser2_idx` (`ToUser` ASC),
  CONSTRAINT `fk_Message_RegisteredUser1`
    FOREIGN KEY (`FromUser`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Message_RegisteredUser2`
    FOREIGN KEY (`ToUser`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`Category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Category` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Category` (
  `CategoryID` INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`CategoryID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`PendingUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`PendingUser` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`PendingUser` (
  `Username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Username`),
  CONSTRAINT `fk_Administrator_User10`
    FOREIGN KEY (`Username`)
    REFERENCES `auctionbay`.`User` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`RegisteredUser_bidsIn_Auction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`RegisteredUser_bidsIn_Auction` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`RegisteredUser_bidsIn_Auction` (
  `Bidder_Username` VARCHAR(45) NOT NULL,
  `AuctionID` INT NOT NULL,
  `BidPrice` FLOAT NOT NULL,
  `BidTime` DATETIME NULL,
  PRIMARY KEY (`Bidder_Username`, `AuctionID`),
  INDEX `fk_RegisteredUser_has_Auction_Auction1_idx` (`AuctionID` ASC),
  INDEX `fk_RegisteredUser_has_Auction_RegisteredUser1_idx` (`Bidder_Username` ASC),
  CONSTRAINT `fk_RegisteredUser_has_Auction_RegisteredUser1`
    FOREIGN KEY (`Bidder_Username`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RegisteredUser_has_Auction_Auction1`
    FOREIGN KEY (`AuctionID`)
    REFERENCES `auctionbay`.`Auction` (`AuctionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`AuctionHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`AuctionHistory` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`AuctionHistory` (
  `Username` VARCHAR(45) NOT NULL,
  `ItemID` INT NOT NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`MailBox`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`MailBox` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`MailBox` (
  `id` INT NOT NULL,
  `RegisteredUser` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `MessageID` INT NOT NULL,
  PRIMARY KEY (`id`, `RegisteredUser`, `MessageID`),
  INDEX `fk_MailBox_RegisteredUser1_idx` (`RegisteredUser` ASC),
  INDEX `fk_MailBox_Message1_idx` (`MessageID` ASC),
  CONSTRAINT `fk_MailBox_RegisteredUser1`
    FOREIGN KEY (`RegisteredUser`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MailBox_Message1`
    FOREIGN KEY (`MessageID`)
    REFERENCES `auctionbay`.`Message` (`MessageID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`Item_has_Category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`Item_has_Category` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`Item_has_Category` (
  `ItemID` INT NOT NULL,
  `CategoryID` INT NOT NULL,
  PRIMARY KEY (`ItemID`, `CategoryID`),
  INDEX `fk_Product_has_Category_Category1_idx` (`CategoryID` ASC),
  INDEX `fk_Product_has_Category_Product1_idx` (`ItemID` ASC),
  CONSTRAINT `fk_Product_has_Category_Product1`
    FOREIGN KEY (`ItemID`)
    REFERENCES `auctionbay`.`Item` (`ItemID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Product_has_Category_Category1`
    FOREIGN KEY (`CategoryID`)
    REFERENCES `auctionbay`.`Category` (`CategoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`SellerRating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`SellerRating` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`SellerRating` (
  `SellerRatingID` INT NOT NULL,
  `Username` VARCHAR(45) NOT NULL,
  `Rate` INT NOT NULL,
  PRIMARY KEY (`SellerRatingID`, `Username`),
  INDEX `fk_SellerRating_RegisteredUser1_idx` (`Username` ASC),
  CONSTRAINT `fk_SellerRating_RegisteredUser1`
    FOREIGN KEY (`Username`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auctionbay`.`BidderRating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `auctionbay`.`BidderRating` ;

CREATE TABLE IF NOT EXISTS `auctionbay`.`BidderRating` (
  `BidderRatingID` INT NOT NULL,
  `Username` VARCHAR(45) NOT NULL,
  `Rate` INT NOT NULL,
  PRIMARY KEY (`BidderRatingID`, `Username`),
  INDEX `fk_BidderRating_RegisteredUser1_idx` (`Username` ASC),
  CONSTRAINT `fk_BidderRating_RegisteredUser1`
    FOREIGN KEY (`Username`)
    REFERENCES `auctionbay`.`RegisteredUser` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
