--
-- Delete data in the tables and restart id seq with 1
--
DELETE FROM user_roles;

DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;

