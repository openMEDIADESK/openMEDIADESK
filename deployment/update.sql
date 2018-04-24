DROP TABLE `folder`;
DROP TABLE `folderholder`;
DROP TABLE `filmreel`;
DROP TABLE `filmreelholder`;
DROP TABLE `inbox`;
ALTER TABLE `category` RENAME TO `folder` ;
ALTER TABLE `categoryholder` RENAME TO `folderholder` ;
ALTER TABLE `imageversion` RENAME TO `mediaobject` ;