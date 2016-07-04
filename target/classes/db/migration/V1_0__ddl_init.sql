CREATE TABLE equities (
  symbol varchar(45) NOT NULL,
  name_of_company varchar(45) NOT NULL,
  series varchar(45) DEFAULT NULL,
  paid_up_value int(11) DEFAULT NULL,
  market_lot int(11) DEFAULT NULL,
  isin_number varchar(45) NOT NULL,
  face_value int(11) DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1551 DEFAULT CHARSET=utf8;

CREATE TABLE investments (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  stock_name varchar(45) DEFAULT NULL,
  stock_symbol varchar(45) DEFAULT NULL,
  investment_price double NOT NULL,
  purchase_date varchar(45) DEFAULT NULL,
  isin_number varchar(45) NOT NULL,
  quantity int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  first_name varchar(45) NOT NULL,
  last_name varchar(45) NOT NULL,
  email varchar(45) NOT NULL,
  phone_number varchar(45) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;





