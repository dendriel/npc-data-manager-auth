INSERT INTO user (active, email, login, name, password, service)
SELECT * FROM (
    SELECT true, 'root@configcreator.com', 'root', 'Root User', '$2a$10$MZSes9FxQlaNdCD7mRZa3estMcNBzJW58oGTL0xUuU2IoxzRYUZN2', false
) AS tmp
WHERE NOT EXISTS(SELECT * FROM user WHERE login='root') LIMIT 1;

INSERT INTO user (active, email, login, name, password, service)
SELECT * FROM (
    SELECT true, 'service-user01@configcreator.com', 'service.user01', 'Service User 01', '$2a$10$y1uB08hzzLs3UmwsmTQYFOTPhnFpA8XZNzyJt29Tt9ZzR5RzDCJPC', true as service
) AS tmp
WHERE NOT EXISTS(SELECT * FROM user WHERE login='service.user01') LIMIT 1;

INSERT INTO user (active, email, login, name, password, service)
SELECT * FROM (
    SELECT true, 'service-user02@configcreator.com', 'service.user02', 'Service User 02', '$2a$10$/W6glRkIueRFTzbiIS9Nmu326ag.tFZOZN5ttHgmq/3tsX8CJxWCa', true as service
) AS tmp
WHERE NOT EXISTS(SELECT * FROM user WHERE login='service.user02') LIMIT 1;
