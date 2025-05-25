CREATE TABLE IF NOT EXISTS tb_roles (
                                        role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        name VARCHAR(255) NOT NULL UNIQUE
);
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1, 'admin');
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2, 'basic');

