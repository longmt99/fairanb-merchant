USE fairanb_merchant;

CREATE TABLE merchant (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(50) NOT NULL,
	phone VARCHAR(100) ,
	domain_name VARCHAR(50),
	name VARCHAR(100) ,
  logo VARCHAR(100),
  country_id BIGINT(20) NOT NULL,
  address_id BIGINT(20) NOT NULL,
	currency_id BIGINT(20) NOT NULL,
	size_unit VARCHAR(100) ,
	weight_unit VARCHAR(10) ,
	business_since TIMESTAMP,
	status VARCHAR(10) ,
	deleted TINYINT (1) ,
	deleted_at TIMESTAMP NOT NULL DEFAULT NOW(),
	active TINYINT (1),
    created_by BIGINT(20),
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
     modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),

	UNIQUE KEY (email),
	UNIQUE KEY (phone)
)ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;

LOCK TABLES `merchant` WRITE;
INSERT INTO `merchant` VALUES (1,'merchant01@fairanb.com','+1444-333-3333','fairanb.com','merchant01','image/logo.jpg',91,1,2,'KB','LB','2018-02-22 11:01:51','ACTIVE',0,'2018-02-22 11:01:30',1,1,'2018-02-22 11:01:30',1,'2018-02-22 11:01:30');
UNLOCK TABLES;



CREATE TABLE language (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
		active TINYINT (1),
   created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	UNIQUE KEY (name)
)ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;

LOCK TABLES `language` WRITE;
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(1,'en',true,1,now(),2,now());
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(2,'fr',true,1,now(),2,now());
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(3,'es',true,1,now(),2,now());
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(4,'vi',true,1,now(),2,now());
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(5,'zh',true,1,now(),2,now());
INSERT INTO language(id, name,active,created_by,created_date,modified_by,modified_date) VALUES(6,'ru',true,1,now(),2,now());
UNLOCK TABLES;

CREATE TABLE merchant_language (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   merchant_id BIGINT(20) NOT NULL,
   language_id BIGINT(11) NOT NULL,
  active TINYINT (1),
   created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),

   FOREIGN KEY (language_id) REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE
 )ENGINE=InnoDB AUTO_INCREMENT = 100000 DEFAULT CHARSET=utf8;

LOCK TABLES `merchant_language` WRITE;
INSERT INTO `merchant_language` VALUES (1,1,1,1,1,'2018-02-22 05:04:37',1,'2018-02-22 05:04:37');
UNLOCK TABLES;

   CREATE TABLE merchant_config(
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
  `merchant_id` BIGINT(20) NOT NULL,
  `config_key` VARCHAR(255) NULL,
  `type` VARCHAR(255) NULL,
  `value` LONGTEXT NULL,
  `status` VARCHAR(40) NULL,
  active TINYINT (1),
   created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
   FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE
 )ENGINE=InnoDB AUTO_INCREMENT = 100000 DEFAULT CHARSET=utf8;

LOCK TABLES `merchant_config` WRITE;
INSERT INTO `merchant_config` VALUES (1,1,'CONFIG','CONFIG','{\"protocol\":\"smtp\",\"password\":\"\",\"smtpAuth\":true}','ACTIVE',1,1,'2018-02-22 11:08:13',1,'2018-02-22 11:08:13');
UNLOCK TABLES;

CREATE TABLE country (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(200) DEFAULT NULL,
	iso_code_2 VARCHAR(100) ,
	iso_code_3 VARCHAR(100),
  address VARCHAR(300),
  post_code TINYINT (1),
  status  VARCHAR(100) NOT NULL,
	active TINYINT (1),
  created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW(),
	UNIQUE KEY (name),
	UNIQUE KEY (iso_code_2),
	UNIQUE KEY (iso_code_3)
)ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;

LOCK TABLES `country` WRITE;
INSERT INTO `fairanb_merchant`.`country` (`id`, `name`, `iso_code_2`, `iso_code_3`, `post_code`, `status`, `active`, `created_by`, `modified_by`) VALUES ('1', 'Aaland Islands', 'AX', 'ALA', '0', 'Enabled', '1', '1', '1');
INSERT INTO `fairanb_merchant`.`country` (`id`, `name`, `iso_code_2`, `iso_code_3`, `post_code`, `status`, `active`, `created_by`, `modified_by`) VALUES ('2', 'India', 'IN', 'IND', '0', 'Enabled', '1', '1', '1');
INSERT INTO `fairanb_merchant`.`country` (`id`, `name`, `iso_code_2`, `iso_code_3`, `post_code`, `status`, `active`, `created_by`, `modified_by`) VALUES ('3', 'Bangladesh', 'BD', 'BGD', '0', 'Enabled', '1', '1', '1');
UNLOCK TABLES;

CREATE TABLE `country_description` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_id` bigint(20) NOT NULL,
  `language_id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `native_name` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NOW(),
  `modified_date` timestamp NULL DEFAULT NOW(),
  `created_by` bigint(20) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `zone` (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	country_id bigint(20) ,
	code VARCHAR(40) NOT NULL,
	name VARCHAR(255) NOT NULL,
	active TINYINT (1),
  created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	UNIQUE KEY (name),
  FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;

LOCK TABLES `zone` WRITE;
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AD', 'Europe/Andorra');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AE', 'Asia/Dubai');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AF', 'Asia/Kabul');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AG', 'America/Antigua');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AI', 'America/Anguilla');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AL', 'Europe/Tirane');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AM', 'Asia/Yerevan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AO', 'Africa/Luanda');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/McMurdo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Casey');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Davis');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/DumontDUrville');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Mawson');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Palmer');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Rothera');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Syowa');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Troll');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AQ', 'Antarctica/Vostok');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Buenos_Aires');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Cordoba');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Salta');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Jujuy');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Tucuman');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Catamarca');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/La_Rioja');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/San_Juan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Mendoza');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/San_Luis');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Rio_Gallegos');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AR', 'America/Argentina/Ushuaia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AS', 'Pacific/Pago_Pago');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AT', 'Europe/Vienna');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Lord_Howe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Antarctica/Macquarie');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Hobart');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Currie');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Melbourne');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Sydney');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Broken_Hill');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Brisbane');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Lindeman');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Adelaide');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Darwin');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Perth');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AU', 'Australia/Eucla');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AW', 'America/Aruba');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AX', 'Europe/Mariehamn');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('AZ', 'Asia/Baku');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BA', 'Europe/Sarajevo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BB', 'America/Barbados');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BD', 'Asia/Dhaka');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BE', 'Europe/Brussels');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BF', 'Africa/Ouagadougou');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BG', 'Europe/Sofia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BH', 'Asia/Bahrain');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BI', 'Africa/Bujumbura');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BJ', 'Africa/Porto-Novo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BL', 'America/St_Barthelemy');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BM', 'Atlantic/Bermuda');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BN', 'Asia/Brunei');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BO', 'America/La_Paz');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BQ', 'America/Kralendijk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Noronha');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Belem');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Fortaleza');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Recife');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Araguaina');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Maceio');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Bahia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Sao_Paulo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Campo_Grande');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Cuiaba');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Santarem');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Porto_Velho');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Boa_Vista');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Manaus');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Eirunepe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BR', 'America/Rio_Branco');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BS', 'America/Nassau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BT', 'Asia/Thimphu');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BW', 'Africa/Gaborone');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BY', 'Europe/Minsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('BZ', 'America/Belize');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/St_Johns');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Halifax');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Glace_Bay');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Moncton');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Goose_Bay');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Blanc-Sablon');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Toronto');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Nipigon');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Thunder_Bay');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Iqaluit');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Pangnirtung');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Atikokan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Winnipeg');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Rainy_River');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Resolute');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Rankin_Inlet');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Regina');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Swift_Current');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Edmonton');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Cambridge_Bay');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Yellowknife');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Inuvik');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Creston');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Dawson_Creek');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Fort_Nelson');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Vancouver');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Whitehorse');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CA', 'America/Dawson');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CC', 'Indian/Cocos');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CD', 'Africa/Kinshasa');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CD', 'Africa/Lubumbashi');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CF', 'Africa/Bangui');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CG', 'Africa/Brazzaville');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CH', 'Europe/Zurich');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CI', 'Africa/Abidjan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CK', 'Pacific/Rarotonga');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CL', 'America/Santiago');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CL', 'America/Punta_Arenas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CL', 'Pacific/Easter');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CM', 'Africa/Douala');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CN', 'Asia/Shanghai');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CN', 'Asia/Urumqi');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CO', 'America/Bogota');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CR', 'America/Costa_Rica');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CU', 'America/Havana');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CV', 'Atlantic/Cape_Verde');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CW', 'America/Curacao');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CX', 'Indian/Christmas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CY', 'Asia/Nicosia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CY', 'Asia/Famagusta');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('CZ', 'Europe/Prague');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DE', 'Europe/Berlin');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DE', 'Europe/Busingen');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DJ', 'Africa/Djibouti');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DK', 'Europe/Copenhagen');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DM', 'America/Dominica');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DO', 'America/Santo_Domingo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('DZ', 'Africa/Algiers');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('EC', 'America/Guayaquil');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('EC', 'Pacific/Galapagos');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('EE', 'Europe/Tallinn');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('EG', 'Africa/Cairo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('EH', 'Africa/El_Aaiun');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ER', 'Africa/Asmara');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ES', 'Europe/Madrid');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ES', 'Africa/Ceuta');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ES', 'Atlantic/Canary');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ET', 'Africa/Addis_Ababa');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FI', 'Europe/Helsinki');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FJ', 'Pacific/Fiji');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FK', 'Atlantic/Stanley');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FM', 'Pacific/Chuuk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FM', 'Pacific/Pohnpei');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FM', 'Pacific/Kosrae');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FO', 'Atlantic/Faroe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('FR', 'Europe/Paris');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GA', 'Africa/Libreville');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GB', 'Europe/London');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GD', 'America/Grenada');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GE', 'Asia/Tbilisi');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GF', 'America/Cayenne');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GG', 'Europe/Guernsey');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GH', 'Africa/Accra');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GI', 'Europe/Gibraltar');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GL', 'America/Godthab');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GL', 'America/Danmarkshavn');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GL', 'America/Scoresbysund');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GL', 'America/Thule');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GM', 'Africa/Banjul');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GN', 'Africa/Conakry');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GP', 'America/Guadeloupe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GQ', 'Africa/Malabo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GR', 'Europe/Athens');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GS', 'Atlantic/South_Georgia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GT', 'America/Guatemala');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GU', 'Pacific/Guam');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GW', 'Africa/Bissau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('GY', 'America/Guyana');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('HK', 'Asia/Hong_Kong');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('HN', 'America/Tegucigalpa');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('HR', 'Europe/Zagreb');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('HT', 'America/Port-au-Prince');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('HU', 'Europe/Budapest');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ID', 'Asia/Jakarta');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ID', 'Asia/Pontianak');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ID', 'Asia/Makassar');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ID', 'Asia/Jayapura');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IE', 'Europe/Dublin');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IL', 'Asia/Jerusalem');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IM', 'Europe/Isle_of_Man');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IN', 'Asia/Kolkata');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IO', 'Indian/Chagos');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IQ', 'Asia/Baghdad');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IR', 'Asia/Tehran');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IS', 'Atlantic/Reykjavik');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('IT', 'Europe/Rome');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('JE', 'Europe/Jersey');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('JM', 'America/Jamaica');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('JO', 'Asia/Amman');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('JP', 'Asia/Tokyo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KE', 'Africa/Nairobi');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KG', 'Asia/Bishkek');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KH', 'Asia/Phnom_Penh');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KI', 'Pacific/Tarawa');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KI', 'Pacific/Enderbury');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KI', 'Pacific/Kiritimati');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KM', 'Indian/Comoro');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KN', 'America/St_Kitts');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KP', 'Asia/Pyongyang');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KR', 'Asia/Seoul');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KW', 'Asia/Kuwait');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KY', 'America/Cayman');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Almaty');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Qyzylorda');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Aqtobe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Aqtau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Atyrau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('KZ', 'Asia/Oral');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LA', 'Asia/Vientiane');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LB', 'Asia/Beirut');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LC', 'America/St_Lucia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LI', 'Europe/Vaduz');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LK', 'Asia/Colombo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LR', 'Africa/Monrovia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LS', 'Africa/Maseru');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LT', 'Europe/Vilnius');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LU', 'Europe/Luxembourg');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LV', 'Europe/Riga');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('LY', 'Africa/Tripoli');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MA', 'Africa/Casablanca');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MC', 'Europe/Monaco');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MD', 'Europe/Chisinau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ME', 'Europe/Podgorica');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MF', 'America/Marigot');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MG', 'Indian/Antananarivo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MH', 'Pacific/Majuro');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MH', 'Pacific/Kwajalein');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MK', 'Europe/Skopje');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ML', 'Africa/Bamako');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MM', 'Asia/Yangon');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MN', 'Asia/Ulaanbaatar');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MN', 'Asia/Hovd');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MN', 'Asia/Choibalsan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MO', 'Asia/Macau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MP', 'Pacific/Saipan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MQ', 'America/Martinique');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MR', 'Africa/Nouakchott');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MS', 'America/Montserrat');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MT', 'Europe/Malta');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MU', 'Indian/Mauritius');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MV', 'Indian/Maldives');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MW', 'Africa/Blantyre');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Mexico_City');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Cancun');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Merida');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Monterrey');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Matamoros');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Mazatlan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Chihuahua');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Ojinaga');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Hermosillo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Tijuana');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MX', 'America/Bahia_Banderas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MY', 'Asia/Kuala_Lumpur');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MY', 'Asia/Kuching');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('MZ', 'Africa/Maputo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NA', 'Africa/Windhoek');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NC', 'Pacific/Noumea');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NE', 'Africa/Niamey');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NF', 'Pacific/Norfolk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NG', 'Africa/Lagos');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NI', 'America/Managua');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NL', 'Europe/Amsterdam');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NO', 'Europe/Oslo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NP', 'Asia/Kathmandu');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NR', 'Pacific/Nauru');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NU', 'Pacific/Niue');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NZ', 'Pacific/Auckland');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('NZ', 'Pacific/Chatham');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('OM', 'Asia/Muscat');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PA', 'America/Panama');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PE', 'America/Lima');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PF', 'Pacific/Tahiti');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PF', 'Pacific/Marquesas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PF', 'Pacific/Gambier');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PG', 'Pacific/Port_Moresby');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PG', 'Pacific/Bougainville');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PH', 'Asia/Manila');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PK', 'Asia/Karachi');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PL', 'Europe/Warsaw');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PM', 'America/Miquelon');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PN', 'Pacific/Pitcairn');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PR', 'America/Puerto_Rico');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PS', 'Asia/Gaza');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PS', 'Asia/Hebron');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PT', 'Europe/Lisbon');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PT', 'Atlantic/Madeira');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PT', 'Atlantic/Azores');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PW', 'Pacific/Palau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('PY', 'America/Asuncion');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('QA', 'Asia/Qatar');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RE', 'Indian/Reunion');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RO', 'Europe/Bucharest');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RS', 'Europe/Belgrade');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Kaliningrad');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Moscow');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Simferopol');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Volgograd');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Kirov');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Astrakhan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Saratov');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Ulyanovsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Europe/Samara');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Yekaterinburg');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Omsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Novosibirsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Barnaul');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Tomsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Novokuznetsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Krasnoyarsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Irkutsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Chita');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Yakutsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Khandyga');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Vladivostok');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Ust-Nera');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Magadan');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Sakhalin');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Srednekolymsk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Kamchatka');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RU', 'Asia/Anadyr');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('RW', 'Africa/Kigali');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SA', 'Asia/Riyadh');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SB', 'Pacific/Guadalcanal');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SC', 'Indian/Mahe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SD', 'Africa/Khartoum');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SE', 'Europe/Stockholm');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SG', 'Asia/Singapore');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SH', 'Atlantic/St_Helena');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SI', 'Europe/Ljubljana');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SJ', 'Arctic/Longyearbyen');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SK', 'Europe/Bratislava');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SL', 'Africa/Freetown');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SM', 'Europe/San_Marino');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SN', 'Africa/Dakar');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SO', 'Africa/Mogadishu');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SR', 'America/Paramaribo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SS', 'Africa/Juba');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ST', 'Africa/Sao_Tome');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SV', 'America/El_Salvador');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SX', 'America/Lower_Princes');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SY', 'Asia/Damascus');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('SZ', 'Africa/Mbabane');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TC', 'America/Grand_Turk');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TD', 'Africa/Ndjamena');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TF', 'Indian/Kerguelen');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TG', 'Africa/Lome');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TH', 'Asia/Bangkok');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TJ', 'Asia/Dushanbe');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TK', 'Pacific/Fakaofo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TL', 'Asia/Dili');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TM', 'Asia/Ashgabat');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TN', 'Africa/Tunis');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TO', 'Pacific/Tongatapu');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TR', 'Europe/Istanbul');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TT', 'America/Port_of_Spain');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TV', 'Pacific/Funafuti');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TW', 'Asia/Taipei');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('TZ', 'Africa/Dar_es_Salaam');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UA', 'Europe/Kiev');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UA', 'Europe/Uzhgorod');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UA', 'Europe/Zaporozhye');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UG', 'Africa/Kampala');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UM', 'Pacific/Midway');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UM', 'Pacific/Wake');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/New_York');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Detroit');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Kentucky/Louisville');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Kentucky/Monticello');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Indianapolis');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Vincennes');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Winamac');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Marengo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Petersburg');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Vevay');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Chicago');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Tell_City');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Indiana/Knox');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Menominee');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/North_Dakota/Center');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/North_Dakota/New_Salem');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/North_Dakota/Beulah');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Denver');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Boise');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Phoenix');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Los_Angeles');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Anchorage');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Juneau');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Sitka');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Metlakatla');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Yakutat');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Nome');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'America/Adak');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('US', 'Pacific/Honolulu');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UY', 'America/Montevideo');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UZ', 'Asia/Samarkand');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('UZ', 'Asia/Tashkent');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VA', 'Europe/Vatican');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VC', 'America/St_Vincent');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VE', 'America/Caracas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VG', 'America/Tortola');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VI', 'America/St_Thomas');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VN', 'Asia/Ho_Chi_Minh');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('VU', 'Pacific/Efate');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('WF', 'Pacific/Wallis');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('WS', 'Pacific/Apia');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('YE', 'Asia/Aden');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('YT', 'Indian/Mayotte');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ZA', 'Africa/Johannesburg');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ZM', 'Africa/Lusaka');
INSERT INTO `fairanb_merchant`.`zone` (`code`, `name`) VALUES ('ZW', 'Africa/Harare');
UNLOCK TABLES;

CREATE TABLE zone_description (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	zone_id  bigint(20) NOT NULL,
	language_id  bigint(20) NOT NULL,
	name VARCHAR(120) NOT NULL,
	description VARCHAR(255) NOT NULL,
	active TINYINT (1),
  created_by BIGINT(20),
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by BIGINT(20),
	modified_date TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	UNIQUE KEY (name),
  FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (language_id) REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;

CREATE TABLE `geo_zone` (
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(40) NOT NULL,
   `name` VARCHAR(255) NOT NULL,
  `merchant_id` BIGINT(20) NOT NULL,
	`active` tinyint(1) DEFAULT NULL,
   `created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	UNIQUE KEY (code),
  FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

 CREATE TABLE `geozone_description` (
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`geozone_id` BIGINT(20) NOT NULL,
	`language_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `title` VARCHAR(40) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
	`active` tinyint(1) DEFAULT NULL,
   `created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	UNIQUE KEY (name),
  FOREIGN KEY (geozone_id) REFERENCES geo_zone(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (language_id) REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;


CREATE TABLE `geozone_zones` (
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`geozone_id` BIGINT(20) NOT NULL,
	`country_id` BIGINT(20) NOT NULL,
	`zone_id` BIGINT(20) NOT NULL,
	`title` VARCHAR(40) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
	`active` tinyint(1) DEFAULT NULL,
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
  FOREIGN KEY (geozone_id) REFERENCES geo_zone(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE `return_type` (
	`id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(150) NOT NULL,
	`merchant_id` BIGINT(20) NOT NULL,
	`active` TINYINT (1),
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

LOCK TABLES `return_type` WRITE;
INSERT INTO `fairanb_merchant`.`return_type` (`name`, `merchant_id`) VALUES ('Cancel', 1);
INSERT INTO `fairanb_merchant`.`return_type` (`name`, `merchant_id`) VALUES ('Money back', 1);
INSERT INTO `fairanb_merchant`.`return_type` (`name`, `merchant_id`) VALUES ('Replacement', 1);
UNLOCK TABLES;

CREATE TABLE `return_policy` (
	`id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(255) NOT NULL,
	`description` VARCHAR(255) DEFAULT NULL,
	`value` VARCHAR(255) NOT NULL,
	`is_default` TINYINT(1) DEFAULT 0,
	`merchant_id` BIGINT(20) NOT NULL,
	`active` TINYINT(1),
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 100000 DEFAULT CHARSET=utf8;

CREATE TABLE `promo_code` (
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(20) NOT NULL,
	`start_date` timestamp NULL,
	`end_date` timestamp NULL,
	`status` VARCHAR(10) DEFAULT NULL,
	`active` tinyint(1) DEFAULT NULL,
    `created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW()
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

LOCK TABLES `promo_code` WRITE;
INSERT INTO `promo_code` (`code`, `start_date`, `end_date`, `active`, `status`) VALUES ('XVXV', '2018-03-01 05:04:37', '2018-03-30 05:04:37', 1, 'ACTIVE');
INSERT INTO `promo_code` (`code`, `start_date`, `end_date`, `active`, `status`) VALUES ('YYY', '2018-03-01 05:04:37', '2018-03-10 05:04:37', 1, 'ACTIVE');
INSERT INTO `promo_code` (`code`, `start_date`, `end_date`, `active`, `status`) VALUES ('ZZX', '2018-03-01 05:04:37', '2018-03-20 05:04:37', 1, 'ACTIVE');
UNLOCK TABLES;


CREATE TABLE `gift_card` (
	`id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
	`code` VARCHAR(50) NOT NULL,
	`merchant_id` BIGINT(20) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`description` VARCHAR(255) DEFAULT NULL,
	`start_date` TIMESTAMP NULL DEFAULT NULL,
	`end_date` TIMESTAMP NULL DEFAULT NULL,
	`balance` DECIMAL(10, 2) DEFAULT 0,
	`purchase_amount` DECIMAL(10, 2) DEFAULT 0,
	`security_code` VARCHAR(30) NOT NULL,
	`status` VARCHAR(40) DEFAULT NULL,
	`active` TINYINT(1),
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (merchant_id) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE,
	INDEX USING BTREE (code)

) ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8;
	

CREATE TABLE `discount_definition` (                                                                     
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(100) NULL,        
	`message` VARCHAR(100) NULL, 
	`priority` SMALLINT(5) NULL,
	`merchant_id` BIGINT(20) NOT NULL,
	`start_date` timestamp NULL,
	`end_date` timestamp NULL,
	`status` VARCHAR(10) DEFAULT NULL,     
	`active` tinyint(1) DEFAULT NULL,                                                           
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (merchant_id) REFERENCES merchant(id)
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;




LOCK TABLES `discount_definition` WRITE;
INSERT INTO `discount_definition` (`name`, `description`, `message`, `merchant_id`, `priority`, `start_date`, `end_date`, `active`, `status`) VALUES ('promotional', '20% off on initial buy', 'Initial logged in users promotion', '1', '2', '2018-03-01 05:04:37', '2018-03-30 05:04:37', 1, 'ACTIVE');
UNLOCK TABLES;

CREATE TABLE `discount_level` (                                                                     
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
	`name` VARCHAR(20) NOT NULL
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;
	
LOCK TABLES `discount_level` WRITE;
INSERT INTO `discount_level` (`name`) VALUES ('Item');
INSERT INTO `discount_level` (`name`) VALUES ('Order');
UNLOCK TABLES;



CREATE TABLE `discount_condition` (                                                                     
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
	`discount_level_id` BIGINT(5) NULL,
	`discount_definition_id` BIGINT(20) NOT NULL,
	`amount` DECIMAL(10,2) NULL,
	`status` VARCHAR(10) DEFAULT NULL,     
	`active` tinyint(1) DEFAULT NULL,                                                           
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (discount_level_id) REFERENCES discount_level(id),
	FOREIGN KEY (discount_definition_id) REFERENCES discount_definition(id)
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

LOCK TABLES `discount_condition` WRITE;
INSERT INTO `fairanb_merchant`.`discount_condition` (`discount_level_id`, `discount_definition_id`, `amount`, `status`, `active`, `created_by`, `modified_by`) VALUES ('1', '1','200.00','ACTIVE', '1', '1', '1');
UNLOCK TABLES;

CREATE TABLE `discount_result` (                                                                     
	`id` BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
	`discount_level_id` BIGINT(5) NULL,
	`discount_definition_id` BIGINT(20) NOT NULL,
	`amount_off` DECIMAL(19,2) NULL DEFAULT 0,
	`percent_off` DECIMAL(19,2) NULL,
	`is_free_gift` TINYINT NULL  DEFAULT 0,
	`active` tinyint(1) DEFAULT NULL,                                                           
	`created_by` BIGINT(20),
	`created_date` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modified_by` BIGINT(20),
	`modified_date` TIMESTAMP DEFAULT NOW() DEFAULT NOW(),
	FOREIGN KEY (discount_level_id) REFERENCES discount_level(id),
	FOREIGN KEY (discount_definition_id) REFERENCES discount_definition(id)
)ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

LOCK TABLES `discount_result` WRITE;
INSERT INTO `fairanb_merchant`.`discount_result` (`discount_level_id`, `discount_definition_id`, `amount_off`, `percent_off`, `is_free_gift`, `active`, `created_by`, `modified_by`) VALUES ('1', '1','200.00', '100.00', '0', '1', '1', '1');
UNLOCK TABLES;

CREATE TABLE `manufacturer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `merchant_id` bigint(45) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `sort_order` int(5) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `manufacturer` WRITE;
INSERT INTO `manufacturer` (`id`,`name`,`merchant_id`,`customer_id`,`sort_order`,`image_url`,`status`,`created_by`,`created_date`,`modified_by`,`modified_date`) VALUES (1,'Samsung',1,1,1,'https://s3.abc.png','1',1,'2018-03-15 21:11:35',1,'2018-03-15 21:11:35');
INSERT INTO `manufacturer` (`id`,`name`,`merchant_id`,`customer_id`,`sort_order`,`image_url`,`status`,`created_by`,`created_date`,`modified_by`,`modified_date`) VALUES (2,'Dell',1,1,1,'https://s3.def.png','1',1,'2018-03-15 21:21:15',1,'2018-03-15 21:21:15');
UNLOCK TABLES;

CREATE TABLE `manufacturer_description` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `manufacturer_id` bigint(20) NOT NULL,
  `language_id` bigint(5) NOT NULL,
  `name` varchar(120) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `manufacturer_url` varchar(45) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `manufacturer_description` WRITE;
INSERT INTO `manufacturer_description` (`id`,`manufacturer_id`,`language_id`,`name`,`title`,`description`,`manufacturer_url`,`created_date`,`created_by`,`modified_by`,`modified_date`) VALUES (1,1,1,'Samsung name','Samsung title','Samsung descriuption','http://samsung.com','2018-03-15 21:12:23',1,1,'2018-03-15 21:12:23');
INSERT INTO `manufacturer_description` (`id`,`manufacturer_id`,`language_id`,`name`,`title`,`description`,`manufacturer_url`,`created_date`,`created_by`,`modified_by`,`modified_date`) VALUES (2,2,1,'Dell','Dell title','dell description','http://dell.com','2018-03-15 21:21:43',1,1,'2018-03-15 21:21:43');
UNLOCK TABLES;
