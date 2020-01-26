CREATE DATABASE gitstatsdb;
CREATE USER 'gitstatsapidbuser' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON gitstatsdb TO 'gitstatsapidbuser';