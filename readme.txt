# Description

## MySQL Schema

```
CREATE DATABASE movie_booking;
USE movie_booking;
CREATE TABLE booking_login (
	customerID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	phone INT NOT NULL,
	username VARCHAR(127) NOT NULL,
	password VARCHAR(127) NOT NULL
);
INSERT INTO booking_login (name, email, phone, username, password)
VALUES('dummy', 'dummy@dummy.com', '123456789', 'dummyuser', 'securedummy')
SELECT * from booking_login;
CREATE TABLE movieList (
	movieName VARCHAR(255) NOT NULL,
	theatreID INT,
	screen DATETIME
);
CREATE TABLE reservedSeat (
	movieName VARCHAR(255) NOT NULL,
	theatreID INT NOT NULL,
	date DATE,
	time TIME,
	seatID INT,
	cutomerID INT
);
``
## To  compile:
Ensure you have all dependencies installed -> MySQL (Create Schema), Java SDK, JavaFx
```
run ./comp_run.sh with proper arguments. (Check sh file for more details)
```
