DROP TABLE `folder`;
DROP TABLE `folderholder`;
DROP TABLE `filmreel`;
DROP TABLE `filmreelholder`;
DROP TABLE `inbox`;
ALTER TABLE `category` RENAME TO `folder` ;
ALTER TABLE `categoryholder` RENAME TO `folderholder` ;
ALTER TABLE `imageversion` RENAME TO `mediaobject` ;
ALTER TABLE `pinpic` RENAME TO `pin` ;
ALTER TABLE `pinpicholder` RENAME TO `pinholder` ;
ALTER TABLE `pinholder` CHANGE COLUMN `pinpicid` `pinid` INT(11) NOT NULL DEFAULT '0' ;
ALTER TABLE `pin` CHANGE COLUMN `pinpicid` `pinid` INT(11) NOT NULL , CHANGE COLUMN `pinpictitle` `pintitle` VARCHAR(100) NOT NULL , CHANGE COLUMN `pinpicname` `pinname` VARCHAR(100) NOT NULL ;
ALTER TABLE `mediaobject` CHANGE COLUMN `imagenumber` `medianumber` VARCHAR(255) NOT NULL DEFAULT '' ;
ALTER TABLE `mediaobject` DROP COLUMN `imageid`, DROP COLUMN `version`, DROP INDEX `imageid_index` ;
ALTER TABLE `imagemetadata` RENAME TO  `mediametadata` ;
ALTER TABLE `lightbox` RENAME TO  `fav` ;
ALTER TABLE `pin` MODIFY pinid INTEGER NOT NULL AUTO_INCREMENT;
ALTER TABLE `aclimage` RENAME TO  `aclmedia`;
ALTER TABLE `folder`
CHANGE COLUMN `categoryid` `folderid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `cattitle` `title` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `catname` `name` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `imagecount` `mediacount` INT(11) NOT NULL DEFAULT '0' ,
CHANGE COLUMN `cattitlelng1` `titlelng1` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `cattitlelng2` `titlelng2` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `categorydate` `folderdate` DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' ;
ALTER TABLE `folder` CHANGE COLUMN `imagecounts` `mediacounts` INT(11) NOT NULL DEFAULT '0' ;
ALTER TABLE `folderholder` CHANGE COLUMN `categoryid` `folderid` INT(10) UNSIGNED NOT NULL DEFAULT '0' ;
DELETE FROM appconfig WHERE appkey = 'folderLatestOnRoot';
UPDATE appconfig SET appkey = 'folderLatestOnRoot' WHERE appkey = 'categoryLatestOnRoot';
DELETE FROM appconfig WHERE appkey = 'sortByFolder';
UPDATE appconfig SET appkey = 'sortByFolder' WHERE appkey = 'sortByCategory';
DELETE FROM appconfig WHERE appkey = 'orderByFolder';
UPDATE appconfig SET appkey = 'orderByFolder' WHERE appkey = 'orderByCategory';
DELETE FROM appconfig WHERE appkey = 'wording';
UPDATE appconfig SET appkey = 'wording' WHERE appkey = 'mediaHandling';
DELETE FROM appconfig WHERE appkey = 'folderSort';
UPDATE appconfig SET appkey = 'folderSort' WHERE appkey = 'categorySort';
DELETE FROM appconfig WHERE appkey = 'homeFolderId';
UPDATE appconfig SET appkey = 'homeFolderId' WHERE appkey = 'homeCategoryId';
DELETE FROM appconfig WHERE appkey = 'homeFolderAsRoot';
UPDATE appconfig SET appkey = 'homeFolderAsRoot' WHERE appkey = 'homeCategoryAsRoot';
DELETE FROM appconfig WHERE appkey = 'homeFolderAutocreate';
UPDATE appconfig SET appkey = 'homeFolderAutocreate' WHERE appkey = 'homeCategoryAutocreate';
DELETE FROM appconfig WHERE appkey = 'folderDefaultViewOnRoot';
UPDATE appconfig SET appkey = 'folderDefaultViewOnRoot' WHERE appkey = 'categoryDefaultViewOnRoot';
ALTER TABLE `appuser` CHANGE COLUMN `homecategoryid` `homefolderid` INT(11) NOT NULL DEFAULT '-1' ;
