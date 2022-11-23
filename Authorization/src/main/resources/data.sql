CREATE TABLE register (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    administrator_id BIGINT,
	contents varchar(100),
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	'FOREIGN KEY(administrator_id) REFERENCES user(id)'
);
CREATE TABLE register_personnel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    email varchar(100),
    id_card varchar(100),
    phone varchar(100),
    photo varchar(100),
    register_id BIGINT,
    status int(4) NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	check_man varchar(100) NOT NULL,
	handbook_use int(4),
	handbook_modifier varchar(500),
	handbook_update_time timestamp,
	handbook_template_id BIGINT,
	'FOREIGN KEY(register_id) REFERENCES register(id)'
);
CREATE TABLE handbook_template_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title varchar(500),
	content text(2000),
	able int(4),
    template_id int(4),
	reference int(4),
	reference_id varchar (500),
	sort int(4),
	update_time timestamp,
    modifier varchar (500)
);
CREATE TABLE handbook_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name varchar(500),
	creator varchar (500),
    is_default int(4),
    create_time timestamp,
	update_time timestamp,
	modifier varchar (500)
);
CREATE TABLE handbook_reference (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name varchar(500),
	reference_api varchar (4000),
	reference_id varchar (500)
);
CREATE TABLE handbook_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    register_id BIGINT,
	user_id BIGINT
);

CREATE TABLE handbook_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    table_name varchar (500),
	operation_type varchar (500),
    operation_content varchar (500),
    operation_time varchar (500),
	operation_user varchar (500),
	operation_model varchar (500)
);
CREATE TABLE handbook_content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title varchar (500),
	content text (2000),
    register_id BIGINT,
    template_id int (4),
	able varchar (500),
	modifier varchar (500),
	reference int (4),
    reference_id varchar (500),
    sort int (4),
	upload int (4),
	AffixID BIGINT,
	creator varchar (50),
	update_time timestamp,
	create_time timestamp
);
set foreign_key_checks=0;
INSERT INTO user_role values (1, 1);
INSERT INTO role_authority(role_id,authority_id) values (1, 1);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 2);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 3);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 4);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 5);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 6);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 7);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 8);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 9);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 10);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 11);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 12);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 13);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 14);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 15);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 16);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 17);
INSERT INTO role_authority(role_id,authority_id) VALUES (1, 18);
INSERT INTO role (id,name,code) values(1,'系统管理员','ADMIN');
INSERT INTO user(id,username,password,email,phone,`status`,create_Time,enabled) values (1,'admin', '$2a$10$y4dap5NfUnwAYG1vwC0RguJL/3wdL/SgJ7mDtA3yqr9yQdmCWfC1S','550610344@qq.com','18815476963',1,NOW(),1);
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('1', '0', '系统管理', '', 'sys:manage', '', '0', 'el-icon-s-operation', '1',NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('2', '1', '用户管理', '/sys/users', 'sys:user:list', 'sys/user', '1', 'el-icon-s-custom', '1', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('3', '1', '角色管理', '/sys/roles', 'sys:role:list', 'sys/role', '1', 'el-icon-rank', '2', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('4', '1', '菜单管理', '/sys/menus', 'sys:menu:list', 'sys/authority', '1', 'el-icon-menu', '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('5', '0', '系统工具', '', 'sys:tools', null, '0', 'el-icon-s-tools', '2', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('6', '5', '数字字典', '/sys/dicts', 'sys:dict:list', 'sys/Dict', '1', 'el-icon-s-order', '1', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('7', '3', '添加角色', '', 'sys:role:save', '', '2', '', '1', NOW(), null, '0');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('8', '2', '添加用户', null, 'sys:user:save', null, '2', null, '1', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('9', '2', '修改用户', null, 'sys:user:update', null, '2', null, '2', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('10', '2', '删除用户', null, 'sys:user:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('11', '2', '分配角色', null, 'sys:user:role', null, '2', null, '4', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('12', '2', '重置密码', null, 'sys:user:repass', null, '2', null, '5', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('13', '3', '修改角色', null, 'sys:role:update', null, '2', null, '2', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('14', '3', '删除角色', null, 'sys:role:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('15', '3', '分配权限', null, 'sys:role:perm', null, '2', null, '5', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('16', '4', '添加菜单', null, 'sys:menu:save', null, '2', null, '1', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('17', '4', '修改菜单', null, 'sys:menu:update', null, '2', null, '2', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('18', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('19', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('20', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('21', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', NOW(), null, '1');
INSERT INTO `authority`( `id`,`parent_id`,`name`, `path`, `perms`, `component`,`type`,`icon`,`order_Num`,`create_Time`,`change_Time`,`status`) VALUES ('22', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', NOW(), null, '1');
set foreign_key_checks=1;
CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);