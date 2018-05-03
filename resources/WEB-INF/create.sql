CREATE TABLE `aclimage` (
  `ivid` int(10) unsigned NOT NULL auto_increment,
  `objectid` int(10) unsigned NOT NULL,
  `objecttype` int(10) unsigned NOT NULL,
  `principalid` int(10) unsigned NOT NULL,
  `principaltype` int(10) unsigned NOT NULL,
  `permission` varchar(45) NOT NULL,
  PRIMARY KEY  (`ivid`,`objectid`,`objecttype`,`principaltype`,`principalid`,`permission`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `appconfig` (
  `appkey` varchar(255) NOT NULL default '',
  `appproperty` text NOT NULL,
  PRIMARY KEY  (`appkey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `appuser` (
  `userid` int(11) NOT NULL auto_increment,
  `username` varchar(100) NOT NULL default '',
  `firstname` varchar(50) NOT NULL default '',
  `lastname` varchar(50) NOT NULL default '',
  `email` varchar(100) NOT NULL default '',
  `phone` varchar(100) NOT NULL default '',
  `cellphone` varchar(100) NOT NULL default '',
  `fax` varchar(100) NOT NULL default '',
  `company` varchar(100) NOT NULL default '',
  `companytype` varchar(100) NOT NULL default '',
  `street` varchar(100) NOT NULL default '',
  `city` varchar(100) NOT NULL default '',
  `zipcode` varchar(7) NOT NULL default '',
  `country` varchar(100) NOT NULL default '',
  `password` varchar(254) NOT NULL default '',
  `passLastChange` datetime NOT NULL default '1970-01-01 00:00:00',
  `role` int(11) NOT NULL default '0',
  `enabled` int(11) NOT NULL default '0',
  `credits` int(11) NOT NULL default '-1',
  `securitygroup` int(11) NOT NULL default '0',
  `homecategoryid` int(11) NOT NULL default '-1',
  `digestpassword` varchar(254) NOT NULL default '',
  `oldusername` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `category` (
  `categoryid` int(10) unsigned NOT NULL auto_increment,
  `cattitle` varchar(100) NOT NULL,
  `catname` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  `parent` int(10) unsigned NOT NULL default '0',
  `changeddate` datetime NOT NULL default '1970-01-01 00:00:00',
  `mediacount` int(11) NOT NULL default '0',
  `imagecounts` int(11) NOT NULL default '0',
  `cattitlelng1` varchar(100) NOT NULL,
  `cattitlelng2` varchar(100) NOT NULL,
  `descriptionlng1` varchar(100) NOT NULL,
  `descriptionlng2` varchar(100) NOT NULL,
  `icon` varchar(100) NOT NULL,
  `sortby` int(10) unsigned NOT NULL default '0',
  `orderby` int(10) unsigned NOT NULL default '0',
  `defaultview` int(10) unsigned NOT NULL default '1',
  PRIMARY KEY  (`categoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

CREATE TABLE `categoryholder` (
  `chid` int(10) unsigned NOT NULL auto_increment,
  `categoryid` int(10) unsigned NOT NULL default '0',
  `ivid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`chid`),
  KEY `idx_categoryid` (`categoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `customlist` (
  `clid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `titlelng1` varchar(255) NOT NULL,
  `titlelng2` varchar(255) NOT NULL,
  PRIMARY KEY  (`clid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customlistholder` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `clid` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `titlelng1` varchar(255) NOT NULL,
  `titlelng2` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `downloadlogger` (
  `dllid` int(11) NOT NULL auto_increment,
  `userid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  `downloaddate` datetime NOT NULL default '1970-01-01 00:00:00',
  `formatx` int(10) unsigned NOT NULL default '0',
  `formaty` int(10) unsigned NOT NULL default '0',
  `downloadtype` int(10) unsigned NOT NULL default '0',
  `ip` varchar(45) NOT NULL default '',
  `dns` varchar(255) NOT NULL default '',
  `bytes` int(10) unsigned NOT NULL default '0',
  `pinid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`dllid`)
) ENGINE=MyISAM AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

CREATE TABLE `filmreel` (
  `filmreelid` int(11) NOT NULL auto_increment,
  `deploymentdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `deployuserid` int(11) NOT NULL default '0',
  `filmreeltitle` varchar(100) NOT NULL default '',
  `filmreelname` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`filmreelid`),
  KEY `new_index` (`deployuserid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `filmreelholder` (
  `frhid` int(11) NOT NULL auto_increment,
  `filmreelid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  `pos` int(11) NOT NULL default '0',
  PRIMARY KEY  (`frhid`),
  KEY `new_index` (`filmreelid`),
  KEY `ivid_index` (`ivid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `folder` (
  `folderid` int(11) NOT NULL auto_increment,
  `foldertitle` varchar(100) NOT NULL,
  `foldername` varchar(100) NOT NULL,
  `createdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `createuserid` int(11) NOT NULL default '0',
  `primaryivid` int(11) NOT NULL default '0',
  `foldersubtitle` varchar(100) NOT NULL,
  `folderdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `foldertitlelng1` varchar(100) NOT NULL,
  `foldertitlelng2` varchar(100) NOT NULL,
  `foldersubtitlelng1` varchar(100) NOT NULL,
  `foldersubtitlelng2` varchar(100) NOT NULL,
  `sortby` int(10) unsigned NOT NULL default '0',
  `orderby` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`folderid`),
  KEY `userid_index` (`createuserid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `folderholder` (
  `fhid` int(11) NOT NULL auto_increment,
  `folderid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  `imagetitlealias` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`fhid`),
  KEY `folder_index` (`folderid`),
  KEY `ivid_index` (`ivid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `imagemetadata` (
  `imdid` int(11) NOT NULL auto_increment,
  `imageid` int(11) NOT NULL default '0',
  `versionid` int(11) NOT NULL default '0',
  `metakey` varchar(254) NOT NULL,
  `metavalue` text NOT NULL,
  `exiftag` int(1) NOT NULL default '0',
  `lang` varchar(6) NOT NULL,
  `ivid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`imdid`),
  KEY `ivid_index` (`ivid`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `imageversion` (
  `ivid` int(11) NOT NULL auto_increment,
  `version` int(11) NOT NULL default '0',
  `versiontitle` varchar(255) NOT NULL default '',
  `versionname` varchar(255) NOT NULL default '',
  `imageid` int(11) NOT NULL default '0',
  `createdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `creatoruserid` int(11) NOT NULL default '0',
  `note` text NOT NULL,
  `versionsubtitle` varchar(255) NOT NULL default '',
  `kb` int(11) NOT NULL default '0',
  `width` int(11) NOT NULL default '0',
  `height` int(11) NOT NULL default '0',
  `dpi` int(11) NOT NULL default '0',
  `flag` int(11) NOT NULL default '0',
  `versiontitlelng1` varchar(255) NOT NULL default '',
  `versiontitlelng2` varchar(255) NOT NULL default '',
  `versionsubtitlelng1` varchar(255) NOT NULL default '',
  `versionsubtitlelng2` varchar(255) NOT NULL default '',
  `photographeralias` varchar(255) NOT NULL default '',
  `photographeruserid` int(11) NOT NULL default '0',
  `byline` varchar(255) NOT NULL default '',
  `photographdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `imagenumber` varchar(255) NOT NULL default '',
  `restrictions` varchar(255) NOT NULL default '',
  `lastdatachange` datetime NOT NULL default '1970-01-01 00:00:00',
  `keywords` text NOT NULL,
  `type` varchar(255) NOT NULL default '',
  `people` varchar(255) NOT NULL default '',
  `site` varchar(255) NOT NULL default '',
  `info` text NOT NULL,
  `orientation` int(11) NOT NULL default '0',
  `perspective` int(11) NOT NULL default '0',
  `motive` int(11) NOT NULL default '0',
  `gesture` int(11) NOT NULL default '0',
  `infolng1` text NOT NULL,
  `infolng2` text NOT NULL,
  `sitelng1` varchar(255) NOT NULL default '',
  `sitelng2` varchar(255) NOT NULL default '',
  `notelng1` text NOT NULL,
  `notelng2` text NOT NULL,
  `restrictionslng1` varchar(255) NOT NULL default '',
  `restrictionslng2` varchar(255) NOT NULL default '',
  `mimetype` varchar(255) NOT NULL default '' COMMENT 'Mimetype of the image',
  `extention` varchar(45) NOT NULL default '' COMMENT 'Fileextention of the original image',
  `customlist1` int(11) unsigned NOT NULL,
  `customlist2` int(11) unsigned NOT NULL,
  `customlist3` int(11) unsigned NOT NULL,
  `duration` int(10) unsigned NOT NULL default '0',
  `artist` varchar(255) NOT NULL default '',
  `album` varchar(255) NOT NULL default '',
  `genre` varchar(255) NOT NULL default '',
  `bitrate` int(11) unsigned NOT NULL default '0',
  `channels` int(11) unsigned NOT NULL default '0',
  `samplerate` int(11) unsigned NOT NULL default '0',
  `videocodec` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`ivid`),
  KEY `imageid_index` (`imageid`),
  KEY `userid_index` (`creatoruserid`),
  KEY `Index_imagenumber` (`imagenumber`),
  FULLTEXT KEY `versionsubtitle` (`versionsubtitle`,`versiontitle`),
  FULLTEXT KEY `super_fulltextidx` (`versionsubtitle`,`versiontitle`,`versiontitlelng1`,`versiontitlelng2`),
  FULLTEXT KEY `keywords` (`keywords`,`byline`,`site`,`info`,`people`),
  FULLTEXT KEY `site` (`site`),
  FULLTEXT KEY `people` (`people`),
  FULLTEXT KEY `FTIDX` (`versionsubtitle`,`versiontitle`,`versiontitlelng1`,`versiontitlelng2`,`keywords`,`byline`,`site`,`sitelng1`,`sitelng2`,`info`,`infolng1`,`infolng2`,`people`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `inbox` (
  `inboxid` int(11) NOT NULL auto_increment,
  `ivid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`inboxid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `lightbox` (
  `liid` int(11) NOT NULL auto_increment,
  `userid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`liid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `menu` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `type` int(10) unsigned NOT NULL default '0',
  `position` int(10) unsigned NOT NULL default '0',
  `visibleforrole` int(10) unsigned NOT NULL default '0',
  `title` varchar(45) NOT NULL,
  `titlelng1` varchar(45) NOT NULL,
  `titlelng2` varchar(45) NOT NULL,
  `metadata` text NOT NULL,
  `openas` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

INSERT INTO `menu` VALUES  (12,1,0,0,'','Login','Login','1;/login',0),
 (13,1,0,0,'','Registrieren','Register','1;/register',0),
 (14,1,0,0,'','Pin-Download','Pin-Download','1;/pin',0),
 (15,1,0,0,'','Kontakt','Contact','1;/index/popup/contact',2);

CREATE TABLE `pinpic` (
  `pinpicid` int(11) NOT NULL auto_increment,
  `pinpictitle` varchar(100) NOT NULL,
  `pinpicname` varchar(100) NOT NULL,
  `pin` varchar(10) NOT NULL,
  `note` text NOT NULL,
  `used` int(11) NOT NULL default '0',
  `createdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `maxuse` int(11) NOT NULL default '0',
  `enabled` int(11) NOT NULL default '0',
  `enddate` datetime NOT NULL default '1970-01-01 00:00:00',
  `startdate` datetime NOT NULL default '1970-01-01 00:00:00',
  `directdownload` tinyint(1) NOT NULL default '0',
  `autodelete` tinyint(1) NOT NULL default '0',
  `creatoruserid` int(10) unsigned NOT NULL,
  `uploadenabled` int(11) NOT NULL default '0',
  `defaultview` int(10) unsigned NOT NULL default '0',
  `emailnotification` varchar(100) NOT NULL,
  PRIMARY KEY  (`pinpicid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `pinpicholder` (
  `phid` int(11) NOT NULL auto_increment,
  `pinpicid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`phid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `searchtmp` (
  `stid` int(11) NOT NULL auto_increment,
  `suid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  `searchtime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `score` int(11) NOT NULL default '0',
  PRIMARY KEY  (`stid`),
  KEY `suid_index` (`suid`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

CREATE TABLE `securitygroup` (
  `securitygroupid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY  (`securitygroupid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `securitygroup` VALUES  (1,'Security Group A'),
 (2,'Security Group B'),
 (3,'Security Group C');

CREATE TABLE `shoppingcart` (
  `liid` int(11) NOT NULL auto_increment,
  `userid` int(11) NOT NULL default '0',
  `ivid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`liid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `appuser` ADD COLUMN `mandant` INTEGER NOT NULL DEFAULT -1 AFTER `oldusername`;
ALTER TABLE `appuser` ADD COLUMN `activatecode` VARCHAR(45) NOT NULL DEFAULT '' AFTER `mandant`;
ALTER TABLE `category` ADD COLUMN `createdate` DATETIME NOT NULL DEFAULT '1970-01-01' AFTER `defaultview`,
 ADD COLUMN `primaryivid` INTEGER NOT NULL DEFAULT 0 AFTER `createdate`,
 ADD COLUMN `categorydate` DATETIME NOT NULL DEFAULT '1970-01-01' AFTER `primaryivid`,
 ADD COLUMN `creatoruserid` INTEGER UNSIGNED NOT NULL DEFAULT 0 AFTER `categorydate`;
ALTER TABLE `category` ADD COLUMN `fid` VARCHAR(255), ADD UNIQUE `fiduniquec`(`fid`);
ALTER TABLE `imageversion` ADD COLUMN `fid` VARCHAR(255), ADD UNIQUE `fiduniquem`(`fid`);

ALTER TABLE `imageversion` ADD COLUMN `customstr1` VARCHAR(255) NOT NULL DEFAULT '' AFTER `fid`,
 ADD COLUMN `customstr2` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr1`,
 ADD COLUMN `customstr3` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr2`,
 ADD COLUMN `customstr4` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr3`,
 ADD COLUMN `customstr5` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr4`;
ALTER TABLE `imageversion` ADD INDEX `createdate` (`createdate` ASC);

ALTER TABLE `imageversion` ADD COLUMN `customstr6` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr5`,
 ADD COLUMN `customstr7` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr6`,
 ADD COLUMN `customstr8` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr7`,
 ADD COLUMN `customstr9` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr8`,
 ADD COLUMN `customstr10` VARCHAR(255) NOT NULL DEFAULT '' AFTER `customstr9`;
ALTER TABLE `imageversion` ADD COLUMN `price` FLOAT NOT NULL DEFAULT 0 AFTER `customstr10`;
ALTER TABLE `appuser` ADD COLUMN `vatp` INTEGER NOT NULL DEFAULT -1 AFTER `activatecode`;
ALTER TABLE `downloadlogger` ADD COLUMN `name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `pinid`,
 ADD COLUMN `paytransactionid` VARCHAR(255) NOT NULL DEFAULT '' AFTER `name`;
ALTER TABLE `shoppingcart` ADD COLUMN `paytransactionid` VARCHAR(255) NOT NULL DEFAULT '' AFTER `ivid`;
ALTER TABLE `appuser` ADD COLUMN `uploadnotif` BOOLEAN NOT NULL DEFAULT true AFTER `vatp`;
ALTER TABLE `appconfig` MODIFY COLUMN `appproperty` MEDIUMTEXT  CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `appuser` MODIFY COLUMN `credits` FLOAT NOT NULL DEFAULT -1;

ALTER TABLE `category` ADD COLUMN `public` BOOLEAN NOT NULL DEFAULT false AFTER `fid`,
 ADD COLUMN `inheritacl` BOOLEAN NOT NULL DEFAULT false AFTER `public`;

ALTER TABLE `imageversion` ADD COLUMN `masterdataid` VARCHAR(45) NOT NULL DEFAULT '' AFTER `price`;

ALTER TABLE `appuser` ADD COLUMN `autologinkey` VARCHAR(255) NOT NULL DEFAULT '' AFTER `uploadnotif`;
ALTER TABLE `category` MODIFY COLUMN `inheritacl` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, ADD COLUMN `protected` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 AFTER `inheritacl`;

ALTER TABLE `imageversion` ADD COLUMN `licvalid` DATETIME AFTER `masterdataid`;

ALTER TABLE `pinpic` ADD COLUMN `password` VARCHAR(100) NOT NULL DEFAULT '' AFTER `emailnotification`;