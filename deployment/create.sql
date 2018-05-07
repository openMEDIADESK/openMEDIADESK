DROP TABLE IF EXISTS `aclimage`;
CREATE TABLE `aclimage` (
  `ivid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `objectid` int(10) unsigned NOT NULL,
  `objecttype` int(10) unsigned NOT NULL,
  `principalid` int(10) unsigned NOT NULL,
  `principaltype` int(10) unsigned NOT NULL,
  `permission` varchar(45) NOT NULL,
  PRIMARY KEY (`ivid`,`objectid`,`objecttype`,`principaltype`,`principalid`,`permission`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `appconfig`;
CREATE TABLE `appconfig` (
  `appkey` varchar(255) NOT NULL DEFAULT '',
  `appproperty` mediumtext,
  PRIMARY KEY (`appkey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `appuser`;
CREATE TABLE `appuser` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '',
  `firstname` varchar(50) NOT NULL DEFAULT '',
  `lastname` varchar(50) NOT NULL DEFAULT '',
  `email` varchar(100) NOT NULL DEFAULT '',
  `phone` varchar(100) NOT NULL DEFAULT '',
  `cellphone` varchar(100) NOT NULL DEFAULT '',
  `fax` varchar(100) NOT NULL DEFAULT '',
  `company` varchar(100) NOT NULL DEFAULT '',
  `companytype` varchar(100) NOT NULL DEFAULT '',
  `street` varchar(100) NOT NULL DEFAULT '',
  `city` varchar(100) NOT NULL DEFAULT '',
  `zipcode` varchar(7) NOT NULL DEFAULT '',
  `country` varchar(100) NOT NULL DEFAULT '',
  `password` varchar(254) NOT NULL DEFAULT '',
  `passLastChange` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `role` int(11) NOT NULL DEFAULT '0',
  `enabled` int(11) NOT NULL DEFAULT '0',
  `credits` float NOT NULL DEFAULT '-1',
  `securitygroup` int(11) NOT NULL DEFAULT '0',
  `homecategoryid` int(11) NOT NULL DEFAULT '-1',
  `digestpassword` varchar(254) NOT NULL DEFAULT '',
  `oldusername` varchar(100) NOT NULL DEFAULT '',
  `mandant` int(11) NOT NULL DEFAULT '-1',
  `activatecode` varchar(45) NOT NULL DEFAULT '',
  `vatp` int(11) NOT NULL DEFAULT '-1',
  `uploadnotif` tinyint(1) NOT NULL DEFAULT '1',
  `autologinkey` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `customlist`;
CREATE TABLE `customlist` (
  `clid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `titlelng1` varchar(255) NOT NULL,
  `titlelng2` varchar(255) NOT NULL,
  PRIMARY KEY (`clid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `customlistholder`;
CREATE TABLE `customlistholder` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `clid` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `titlelng1` varchar(255) NOT NULL,
  `titlelng2` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `downloadlogger`;
CREATE TABLE `downloadlogger` (
  `dllid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL DEFAULT '0',
  `ivid` int(11) NOT NULL DEFAULT '0',
  `downloaddate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `formatx` int(10) unsigned NOT NULL DEFAULT '0',
  `formaty` int(10) unsigned NOT NULL DEFAULT '0',
  `downloadtype` int(10) unsigned NOT NULL DEFAULT '0',
  `ip` varchar(45) NOT NULL DEFAULT '',
  `dns` varchar(255) NOT NULL DEFAULT '',
  `bytes` int(10) unsigned NOT NULL DEFAULT '0',
  `pinid` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `paytransactionid` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`dllid`)
) ENGINE=MyISAM AUTO_INCREMENT=273 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
  `categoryid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cattitle` varchar(100) NOT NULL,
  `catname` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  `parent` int(10) unsigned NOT NULL DEFAULT '0',
  `changeddate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `mediacount` int(11) NOT NULL DEFAULT '0',
  `imagecounts` int(11) NOT NULL DEFAULT '0',
  `cattitlelng1` varchar(100) NOT NULL,
  `cattitlelng2` varchar(100) NOT NULL,
  `descriptionlng1` varchar(100) NOT NULL,
  `descriptionlng2` varchar(100) NOT NULL,
  `icon` varchar(100) NOT NULL,
  `sortby` int(10) unsigned NOT NULL DEFAULT '0',
  `orderby` int(10) unsigned NOT NULL DEFAULT '0',
  `defaultview` int(10) unsigned NOT NULL DEFAULT '1',
  `createdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `primaryivid` int(11) NOT NULL DEFAULT '0',
  `categorydate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `creatoruserid` int(10) unsigned NOT NULL DEFAULT '0',
  `fid` varchar(255) DEFAULT NULL,
  `public` tinyint(1) NOT NULL DEFAULT '0',
  `inheritacl` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `protected` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`categoryid`),
  UNIQUE KEY `fiduniquec` (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `folderholder`;
CREATE TABLE `folderholder` (
  `chid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `categoryid` int(10) unsigned NOT NULL DEFAULT '0',
  `ivid` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`chid`),
  KEY `idx_categoryid` (`categoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
DROP TABLE IF EXISTS `imagemetadata`;
CREATE TABLE `imagemetadata` (
  `imdid` int(11) NOT NULL AUTO_INCREMENT,
  `imageid` int(11) NOT NULL DEFAULT '0',
  `versionid` int(11) NOT NULL DEFAULT '0',
  `metakey` varchar(254) NOT NULL,
  `metavalue` text NOT NULL,
  `exiftag` int(1) NOT NULL DEFAULT '0',
  `lang` varchar(6) NOT NULL,
  `ivid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`imdid`),
  KEY `ivid_index` (`ivid`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `imageversion`;
CREATE TABLE `imageversion` (
  `ivid` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `versiontitle` varchar(255) NOT NULL DEFAULT '',
  `versionname` varchar(255) NOT NULL DEFAULT '',
  `imageid` int(11) NOT NULL DEFAULT '0',
  `createdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `creatoruserid` int(11) NOT NULL DEFAULT '0',
  `note` text NOT NULL,
  `versionsubtitle` varchar(255) NOT NULL DEFAULT '',
  `kb` int(11) NOT NULL DEFAULT '0',
  `width` int(11) NOT NULL DEFAULT '0',
  `height` int(11) NOT NULL DEFAULT '0',
  `dpi` int(11) NOT NULL DEFAULT '0',
  `flag` int(11) NOT NULL DEFAULT '0',
  `versiontitlelng1` varchar(255) NOT NULL DEFAULT '',
  `versiontitlelng2` varchar(255) NOT NULL DEFAULT '',
  `versionsubtitlelng1` varchar(255) NOT NULL DEFAULT '',
  `versionsubtitlelng2` varchar(255) NOT NULL DEFAULT '',
  `photographeralias` varchar(255) NOT NULL DEFAULT '',
  `photographeruserid` int(11) NOT NULL DEFAULT '0',
  `byline` varchar(255) NOT NULL DEFAULT '',
  `photographdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `imagenumber` varchar(255) NOT NULL DEFAULT '',
  `restrictions` varchar(255) NOT NULL DEFAULT '',
  `lastdatachange` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `keywords` text NOT NULL,
  `type` varchar(255) NOT NULL DEFAULT '',
  `people` varchar(255) NOT NULL DEFAULT '',
  `site` varchar(255) NOT NULL DEFAULT '',
  `info` text NOT NULL,
  `orientation` int(11) NOT NULL DEFAULT '0',
  `perspective` int(11) NOT NULL DEFAULT '0',
  `motive` int(11) NOT NULL DEFAULT '0',
  `gesture` int(11) NOT NULL DEFAULT '0',
  `infolng1` text NOT NULL,
  `infolng2` text NOT NULL,
  `sitelng1` varchar(255) NOT NULL DEFAULT '',
  `sitelng2` varchar(255) NOT NULL DEFAULT '',
  `notelng1` text NOT NULL,
  `notelng2` text NOT NULL,
  `restrictionslng1` varchar(255) NOT NULL DEFAULT '',
  `restrictionslng2` varchar(255) NOT NULL DEFAULT '',
  `mimetype` varchar(255) NOT NULL DEFAULT '' COMMENT 'Mimetype of the image',
  `extention` varchar(45) NOT NULL DEFAULT '' COMMENT 'Fileextention of the original image',
  `customlist1` int(11) unsigned NOT NULL,
  `customlist2` int(11) unsigned NOT NULL,
  `customlist3` int(11) unsigned NOT NULL,
  `duration` int(10) unsigned NOT NULL DEFAULT '0',
  `artist` varchar(255) NOT NULL DEFAULT '',
  `album` varchar(255) NOT NULL DEFAULT '',
  `genre` varchar(255) NOT NULL DEFAULT '',
  `bitrate` int(11) unsigned NOT NULL DEFAULT '0',
  `channels` int(11) unsigned NOT NULL DEFAULT '0',
  `samplerate` int(11) unsigned NOT NULL DEFAULT '0',
  `videocodec` varchar(25) NOT NULL DEFAULT '',
  `fid` varchar(255) DEFAULT NULL,
  `customstr1` varchar(255) NOT NULL DEFAULT '',
  `customstr2` varchar(255) NOT NULL DEFAULT '',
  `customstr3` varchar(255) NOT NULL DEFAULT '',
  `customstr4` varchar(255) NOT NULL DEFAULT '',
  `customstr5` varchar(255) NOT NULL DEFAULT '',
  `customstr6` varchar(255) NOT NULL DEFAULT '',
  `customstr7` varchar(255) NOT NULL DEFAULT '',
  `customstr8` varchar(255) NOT NULL DEFAULT '',
  `customstr9` varchar(255) NOT NULL DEFAULT '',
  `customstr10` varchar(255) NOT NULL DEFAULT '',
  `price` float NOT NULL DEFAULT '0',
  `masterdataid` varchar(45) NOT NULL DEFAULT '',
  `licvalid` datetime DEFAULT NULL,
  PRIMARY KEY (`ivid`),
  UNIQUE KEY `fiduniquem` (`fid`),
  KEY `imageid_index` (`imageid`),
  KEY `userid_index` (`creatoruserid`),
  KEY `Index_imagenumber` (`imagenumber`),
  KEY `createdate` (`createdate`),
  FULLTEXT KEY `versionsubtitle` (`versionsubtitle`,`versiontitle`),
  FULLTEXT KEY `super_fulltextidx` (`versionsubtitle`,`versiontitle`,`versiontitlelng1`,`versiontitlelng2`),
  FULLTEXT KEY `keywords` (`keywords`,`byline`,`site`,`info`,`people`),
  FULLTEXT KEY `site` (`site`),
  FULLTEXT KEY `people` (`people`),
  FULLTEXT KEY `FTIDX` (`versionsubtitle`,`versiontitle`,`versiontitlelng1`,`versiontitlelng2`,`keywords`,`byline`,`site`,`sitelng1`,`sitelng2`,`info`,`infolng1`,`infolng2`,`people`)
) ENGINE=MyISAM AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `lightbox`;
CREATE TABLE `lightbox` (
  `liid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL DEFAULT '0',
  `ivid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`liid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(10) unsigned NOT NULL DEFAULT '0',
  `position` int(10) unsigned NOT NULL DEFAULT '0',
  `visibleforrole` int(10) unsigned NOT NULL DEFAULT '0',
  `title` varchar(45) NOT NULL,
  `titlelng1` varchar(45) NOT NULL,
  `titlelng2` varchar(45) NOT NULL,
  `metadata` text NOT NULL,
  `openas` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `pinpic`;
CREATE TABLE `pinpic` (
  `pinpicid` int(11) NOT NULL AUTO_INCREMENT,
  `pinpictitle` varchar(100) NOT NULL,
  `pinpicname` varchar(100) NOT NULL,
  `pin` varchar(10) NOT NULL,
  `note` text NOT NULL,
  `used` int(11) NOT NULL DEFAULT '0',
  `createdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `maxuse` int(11) NOT NULL DEFAULT '0',
  `enabled` int(11) NOT NULL DEFAULT '0',
  `enddate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `startdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `directdownload` tinyint(1) NOT NULL DEFAULT '0',
  `autodelete` tinyint(1) NOT NULL DEFAULT '0',
  `creatoruserid` int(10) unsigned NOT NULL,
  `uploadenabled` int(11) NOT NULL DEFAULT '0',
  `defaultview` int(10) unsigned NOT NULL DEFAULT '0',
  `emailnotification` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`pinpicid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `pinpicholder`;
CREATE TABLE `pinpicholder` (
  `phid` int(11) NOT NULL AUTO_INCREMENT,
  `pinpicid` int(11) NOT NULL DEFAULT '0',
  `ivid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`phid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `searchtmp`;
CREATE TABLE `searchtmp` (
  `stid` int(11) NOT NULL AUTO_INCREMENT,
  `suid` int(11) NOT NULL DEFAULT '0',
  `ivid` int(11) NOT NULL DEFAULT '0',
  `searchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`stid`),
  KEY `suid_index` (`suid`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `securitygroup`;
CREATE TABLE `securitygroup` (
  `securitygroupid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`securitygroupid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `shoppingcart`;
CREATE TABLE `shoppingcart` (
  `liid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL DEFAULT '0',
  `ivid` int(11) NOT NULL DEFAULT '0',
  `paytransactionid` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`liid`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

ALTER TABLE `imageversion` RENAME TO `mediaobject` ;
ALTER TABLE `pinpic` RENAME TO `pin` ;
ALTER TABLE `pinpicholder` RENAME TO `pinholder` ;
ALTER TABLE `pinholder` CHANGE COLUMN `pinpicid` `pinid` INT(11) NOT NULL DEFAULT '0' ;
ALTER TABLE `pin` CHANGE COLUMN `pinpicid` `pinid` INT(11) NOT NULL , CHANGE COLUMN `pinpictitle` `pintitle` VARCHAR(100) NOT NULL , CHANGE COLUMN `pinpicname` `pinname` VARCHAR(100) NOT NULL ;
ALTER TABLE `mediaobject` CHANGE COLUMN `imagenumber` `medianumber` VARCHAR(255) NOT NULL DEFAULT '' ;
ALTER TABLE `mediaobject` DROP COLUMN `imageid`, DROP COLUMN `version`, DROP INDEX `imageid_index` ;
ALTER TABLE `imagemetadata` RENAME TO  `mediametadata` ;
ALTER TABLE `lightbox` RENAME TO  `fav` ;