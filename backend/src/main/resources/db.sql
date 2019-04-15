/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : pig

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '广东农信', null, '2018-01-22 19:00:23', '2019-04-01 09:27:15', '0', '0');
INSERT INTO `sys_dept` VALUES ('2', '沙县国际', null, '2018-01-22 19:00:38', '2018-09-13 01:46:30', '0', '0');
INSERT INTO `sys_dept` VALUES ('3', '东莞农信', null, '2018-01-22 19:00:44', '2019-04-01 09:27:36', '0', '1');
INSERT INTO `sys_dept` VALUES ('4', '松山湖农信', null, '2018-01-22 19:00:52', '2019-04-01 09:27:43', '0', '3');
INSERT INTO `sys_dept` VALUES ('5', '院校农信', null, '2018-01-22 19:00:57', '2018-10-06 10:42:51', '0', '4');
INSERT INTO `sys_dept` VALUES ('6', '潍院农信', null, '2018-01-22 19:01:06', '2019-01-09 10:58:18', '1', '5');
INSERT INTO `sys_dept` VALUES ('7', '山东沙县', null, '2018-01-22 19:01:57', '2018-09-13 01:46:42', '0', '2');
INSERT INTO `sys_dept` VALUES ('8', '潍坊沙县', null, '2018-01-22 19:02:03', '2018-09-13 01:46:43', '0', '7');
INSERT INTO `sys_dept` VALUES ('9', '高新沙县', null, '2018-01-22 19:02:14', '2018-09-13 01:46:44', '1', '8');
INSERT INTO `sys_dept` VALUES ('11', '院校沙县', null, '2018-12-10 21:19:26', null, '0', '8');

-- ----------------------------
-- Table structure for `sys_dept_relation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`),
  KEY `idx1` (`ancestor`),
  KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='部门关系表';

-- ----------------------------
-- Records of sys_dept_relation
-- ----------------------------
INSERT INTO `sys_dept_relation` VALUES ('1', '1');
INSERT INTO `sys_dept_relation` VALUES ('1', '3');
INSERT INTO `sys_dept_relation` VALUES ('1', '4');
INSERT INTO `sys_dept_relation` VALUES ('1', '5');
INSERT INTO `sys_dept_relation` VALUES ('2', '2');
INSERT INTO `sys_dept_relation` VALUES ('2', '7');
INSERT INTO `sys_dept_relation` VALUES ('2', '8');
INSERT INTO `sys_dept_relation` VALUES ('2', '11');
INSERT INTO `sys_dept_relation` VALUES ('3', '3');
INSERT INTO `sys_dept_relation` VALUES ('3', '4');
INSERT INTO `sys_dept_relation` VALUES ('3', '5');
INSERT INTO `sys_dept_relation` VALUES ('4', '4');
INSERT INTO `sys_dept_relation` VALUES ('4', '5');
INSERT INTO `sys_dept_relation` VALUES ('5', '5');
INSERT INTO `sys_dept_relation` VALUES ('7', '7');
INSERT INTO `sys_dept_relation` VALUES ('7', '8');
INSERT INTO `sys_dept_relation` VALUES ('7', '11');
INSERT INTO `sys_dept_relation` VALUES ('8', '8');
INSERT INTO `sys_dept_relation` VALUES ('8', '11');
INSERT INTO `sys_dept_relation` VALUES ('10', '10');
INSERT INTO `sys_dept_relation` VALUES ('11', '11');
INSERT INTO `sys_dept_relation` VALUES ('16', '16');

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `keyy` bigint(20) NOT NULL COMMENT '键',
  `valuee` varchar(100) NOT NULL COMMENT '值',
  `field_name` varchar(100) NOT NULL COMMENT '字段名称',
  `table_name` varchar(100) NOT NULL COMMENT '表名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '0', '正常', 'type', 'sys_log', '2018-07-09 06:16:14', '2019-03-31 15:39:48', '0');
INSERT INTO `sys_dict` VALUES ('2', '1', '异常', 'type', 'sys_log', '2018-07-09 06:15:40', '2019-03-31 15:39:51', '0');

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `time` mediumtext COMMENT '执行时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('51', '0', '添加角色', 'test', 'admin', '2019-01-24 20:56:43', null, '0:0:0:0:0:0:0:1', 'PostmanRuntime/7.6.0', '/role', 'POST', 'Authorization=%5B%5D', '65', '0', null);
INSERT INTO `sys_log` VALUES ('247', '0', '新增菜单', 'test', 'admin', '2019-04-03 13:56:33', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/menu', 'POST', '', '48', '0', null);
INSERT INTO `sys_log` VALUES ('248', '0', '更新菜单', 'test', 'admin', '2019-04-03 16:19:45', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/menu', 'PUT', '', '150', '0', null);
INSERT INTO `sys_log` VALUES ('249', '0', '新增菜单', 'test', 'admin', '2019-04-03 16:32:56', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/menu', 'POST', '', '29', '0', null);
INSERT INTO `sys_log` VALUES ('250', '0', '新增菜单', 'test', 'admin', '2019-04-03 16:34:23', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/menu', 'POST', '', '37', '0', null);
INSERT INTO `sys_log` VALUES ('251', '0', '删除菜单', 'test', 'admin', '2019-04-03 16:41:44', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/menu/4001', 'DELETE', '', '13', '0', null);
INSERT INTO `sys_log` VALUES ('252', '0', '删除角色', 'test', 'admin', '2019-04-03 21:08:03', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role/2', 'DELETE', '', '222', '0', null);
INSERT INTO `sys_log` VALUES ('253', '0', '添加角色', 'test', 'admin', '2019-04-04 10:38:27', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role', 'POST', '', '49', '0', null);
INSERT INTO `sys_log` VALUES ('254', '0', '修改角色', 'test', 'admin', '2019-04-04 10:39:36', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role', 'PUT', '', '29', '0', null);
INSERT INTO `sys_log` VALUES ('255', '0', '修改角色', 'test', 'admin', '2019-04-04 10:41:37', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role', 'PUT', '', '28', '0', null);
INSERT INTO `sys_log` VALUES ('256', '0', '修改角色', 'test', 'admin', '2019-04-04 10:48:33', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role', 'PUT', '', '25', '0', null);
INSERT INTO `sys_log` VALUES ('257', '0', '更新角色菜单', 'test', 'admin', '2019-04-04 11:49:06', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role/menu', 'PUT', 'roleId=%5B3%5D&menuIds=%5B%5D', '28', '0', null);
INSERT INTO `sys_log` VALUES ('258', '0', '更新角色菜单', 'test', 'admin', '2019-04-04 11:49:44', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role/menu', 'PUT', 'roleId=%5B1%5D&menuIds=%5B2600%2C2500%2C2400%2C2200%2C2100%2C1400%2C1300%2C2000%2C1200%2C1000%2C3100%2C1100%2C3000%2C2201%2C1202%2C2203%2C1302%2C1304%2C1401%2C1403%2C1101%2C1103%2C1201%2C2202%2C1203%2C1301%2C1303%2C1402%2C1102%2C2101%2C4001%5D', '293', '0', null);
INSERT INTO `sys_log` VALUES ('259', '0', '更新角色菜单', 'test', 'admin', '2019-04-04 11:49:58', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/role/menu', 'PUT', 'roleId=%5B1%5D&menuIds=%5B2600%2C2500%2C2400%2C2200%2C2100%2C1400%2C1300%2C2000%2C1200%2C1100%2C1000%2C3100%2C3000%2C1402%2C1102%2C2101%2C2201%2C1202%2C2203%2C1302%2C1304%2C1401%2C1403%2C1101%2C1103%2C1201%2C2202%2C1203%2C1301%2C1303%5D', '70', '0', null);
INSERT INTO `sys_log` VALUES ('260', '0', '添加用户', 'test', 'admin', '2019-04-09 11:00:59', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'POST', '', '141', '0', null);
INSERT INTO `sys_log` VALUES ('261', '0', '删除用户信息', 'test', 'admin', '2019-04-09 11:18:50', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user/4', 'DELETE', '', '101', '0', null);
INSERT INTO `sys_log` VALUES ('262', '0', '添加用户', 'test', 'admin', '2019-04-09 11:21:31', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'POST', '', '167', '0', null);
INSERT INTO `sys_log` VALUES ('263', '0', '删除用户信息', 'test', 'admin', '2019-04-09 11:21:38', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user/5', 'DELETE', '', '187', '0', null);
INSERT INTO `sys_log` VALUES ('264', '0', '添加用户', 'test', 'admin', '2019-04-09 11:27:32', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'POST', '', '165', '0', null);
INSERT INTO `sys_log` VALUES ('265', '0', '删除用户信息', 'test', 'admin', '2019-04-09 11:27:35', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user/6', 'DELETE', '', '74', '0', null);
INSERT INTO `sys_log` VALUES ('266', '0', '添加用户', 'test', 'admin', '2019-04-09 11:29:31', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'POST', '', '150', '0', null);
INSERT INTO `sys_log` VALUES ('267', '0', '更新用户信息', 'test', 'admin', '2019-04-09 11:59:38', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '299', '0', null);
INSERT INTO `sys_log` VALUES ('268', '0', '更新用户信息', 'test', 'admin', '2019-04-09 11:59:45', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '178', '0', null);
INSERT INTO `sys_log` VALUES ('269', '0', '更新用户信息', 'test', 'admin', '2019-04-09 12:00:05', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '187', '0', null);
INSERT INTO `sys_log` VALUES ('270', '0', '更新用户信息', 'test', 'admin', '2019-04-09 14:25:45', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '209', '0', null);
INSERT INTO `sys_log` VALUES ('271', '0', '更新用户信息', 'test', 'admin', '2019-04-09 14:29:55', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '178', '0', null);
INSERT INTO `sys_log` VALUES ('272', '0', '更新用户信息', 'test', 'admin', '2019-04-09 14:31:29', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '310', '0', null);
INSERT INTO `sys_log` VALUES ('273', '0', '更新用户信息', 'test', 'admin', '2019-04-09 14:31:49', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '309', '0', null);
INSERT INTO `sys_log` VALUES ('274', '0', '更新用户信息', 'test', 'admin', '2019-04-09 14:33:23', null, '127.0.0.1', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36', '/api/user', 'PUT', '', '273', '0', null);

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive` char(1) DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1000', '权限管理', null, '/upms', '-1', 'setting', 'Layout', '1', '0', '0', '2018-09-28 08:29:53', '2019-03-19 11:16:26', '0');
INSERT INTO `sys_menu` VALUES ('1100', '用户管理', null, 'usermgr', '1000', 'user', 'views/admin/user/Index', '1', '0', '0', '2017-11-02 22:24:37', '2019-04-04 16:50:29', '0');
INSERT INTO `sys_menu` VALUES ('1101', '用户新增', 'sys_user_add', null, '1100', null, null, null, '0', '1', '2017-11-08 09:52:09', '2018-09-28 09:06:34', '0');
INSERT INTO `sys_menu` VALUES ('1102', '用户修改', 'sys_user_edit', null, '1100', null, null, null, '0', '1', '2017-11-08 09:52:48', '2018-09-28 09:06:37', '0');
INSERT INTO `sys_menu` VALUES ('1103', '用户删除', 'sys_user_del', null, '1100', null, null, null, '0', '1', '2017-11-08 09:54:01', '2018-09-28 09:06:42', '0');
INSERT INTO `sys_menu` VALUES ('1200', '菜单管理', null, 'menu', '1000', 'menu', 'views/admin/menu/Index', '2', '0', '0', '2017-11-08 09:57:27', '2019-03-31 16:33:54', '0');
INSERT INTO `sys_menu` VALUES ('1201', '菜单新增', 'sys_menu_add', null, '1200', null, null, null, '0', '1', '2017-11-08 10:15:53', '2018-09-28 09:07:16', '0');
INSERT INTO `sys_menu` VALUES ('1202', '菜单修改', 'sys_menu_edit', null, '1200', null, null, null, '0', '1', '2017-11-08 10:16:23', '2018-09-28 09:07:18', '0');
INSERT INTO `sys_menu` VALUES ('1203', '菜单删除', 'sys_menu_del', null, '1200', null, null, null, '0', '1', '2017-11-08 10:16:43', '2018-09-28 09:07:22', '0');
INSERT INTO `sys_menu` VALUES ('1300', '角色管理', null, 'role', '1000', 'contacts', 'views/admin/role/Index', '3', '0', '0', '2017-11-08 10:13:37', '2019-03-31 16:20:18', '0');
INSERT INTO `sys_menu` VALUES ('1301', '角色新增', 'sys_role_add', null, '1300', null, null, null, '0', '1', '2017-11-08 10:14:18', '2018-09-28 09:07:46', '0');
INSERT INTO `sys_menu` VALUES ('1302', '角色修改', 'sys_role_edit', null, '1300', null, null, null, '0', '1', '2017-11-08 10:14:41', '2018-09-28 09:07:49', '0');
INSERT INTO `sys_menu` VALUES ('1303', '角色删除', 'sys_role_del', null, '1300', null, null, null, '0', '1', '2017-11-08 10:14:59', '2018-09-28 09:07:53', '0');
INSERT INTO `sys_menu` VALUES ('1304', '分配权限', 'sys_role_perm', null, '1300', null, null, null, '0', '1', '2018-04-20 07:22:55', '2018-09-28 09:13:23', '0');
INSERT INTO `sys_menu` VALUES ('1400', '部门管理', null, 'dept', '1000', 'gold', 'views/admin/dept/Index', '4', '0', '0', '2018-01-20 13:17:19', '2019-03-31 16:33:57', '0');
INSERT INTO `sys_menu` VALUES ('1401', '部门新增', 'sys_dept_add', null, '1400', null, null, null, '0', '1', '2018-01-20 14:56:16', '2018-09-28 09:08:13', '0');
INSERT INTO `sys_menu` VALUES ('1402', '部门修改', 'sys_dept_edit', null, '1400', null, null, null, '0', '1', '2018-01-20 14:56:59', '2018-09-28 09:08:16', '0');
INSERT INTO `sys_menu` VALUES ('1403', '部门删除', 'sys_dept_del', null, '1400', null, null, null, '0', '1', '2018-01-20 14:57:28', '2018-09-28 09:08:18', '0');
INSERT INTO `sys_menu` VALUES ('2000', '系统管理', null, '/admin', '-1', 'tool', 'Layout', '2', '0', '0', '2017-11-07 20:56:00', '2019-03-19 11:16:35', '0');
INSERT INTO `sys_menu` VALUES ('2100', '日志管理', null, 'log', '2000', 'file-text', 'views/admin/log/Index', '5', '0', '0', '2017-11-20 14:06:22', '2019-03-31 16:24:40', '0');
INSERT INTO `sys_menu` VALUES ('2101', '日志删除', 'sys_log_del', null, '2100', null, null, null, '0', '1', '2017-11-20 20:37:37', '2018-09-28 09:08:44', '0');
INSERT INTO `sys_menu` VALUES ('2200', '字典管理', null, 'dict', '2000', 'book', 'views/admin/dict/Index', '6', '0', '0', '2017-11-29 11:30:52', '2019-03-28 14:56:40', '0');
INSERT INTO `sys_menu` VALUES ('2201', '字典删除', 'sys_dict_del', null, '2200', null, null, null, '0', '1', '2017-11-29 11:30:11', '2018-09-28 09:09:10', '0');
INSERT INTO `sys_menu` VALUES ('2202', '字典新增', 'sys_dict_add', null, '2200', null, null, null, '0', '1', '2018-05-11 22:34:55', '2018-09-28 09:09:12', '0');
INSERT INTO `sys_menu` VALUES ('2203', '字典修改', 'sys_dict_edit', null, '2200', null, null, null, '0', '1', '2018-05-11 22:36:03', '2018-09-28 09:09:16', '0');
INSERT INTO `sys_menu` VALUES ('2400', '请求追踪', '', 'trace', '2000', 'rise', 'views/admin/trace/Index', '9', '0', '0', '2018-01-20 13:17:19', '2019-03-28 10:01:00', '0');
INSERT INTO `sys_menu` VALUES ('2500', 'Redis监控', null, 'redisinfo', '2000', 'monitor', 'views/admin/redis/Index', '10', '0', '0', '2018-06-26 10:50:32', '2019-03-28 20:59:07', '0');
INSERT INTO `sys_menu` VALUES ('2600', '系统信息', null, 'sysinfo', '2000', 'info', 'views/admin/sysinfo/Index', '11', '0', '0', '2018-09-04 05:58:41', '2019-03-27 20:28:19', '0');
INSERT INTO `sys_menu` VALUES ('3000', '仪表盘', null, '/dashboard', '-1', 'home', 'dashboard/workplace', '0', '0', '0', '2019-01-17 17:05:19', '2019-03-24 17:06:00', '0');
INSERT INTO `sys_menu` VALUES ('3100', '分析页', 'sys_dashboard', 'dashboard/workplace', '3000', 'dashboard', 'views/dashboard/Workplace', '1', '0', '0', '2019-03-19 11:14:47', '2019-03-24 17:04:39', '0');
INSERT INTO `sys_menu` VALUES ('5000', '一级菜单', null, '/crud', '-1', 'icon-caidanguanli', '', '4', '0', '0', '2018-08-28 01:50:22', '2018-09-28 08:58:20', '0');
INSERT INTO `sys_menu` VALUES ('5001', '一级菜单', null, 'index', '5000', 'icon-caidanguanli', 'views/crud/index', '1', '0', '0', '2018-08-28 01:50:48', '2018-11-21 17:48:19', '1');
INSERT INTO `sys_menu` VALUES ('5002', '二级菜单', null, 'crud', '5001', 'icon-caidanguanli', 'views/crud/index', '1', '0', '0', '2018-08-28 01:51:23', '2018-11-21 17:47:40', '1');
INSERT INTO `sys_menu` VALUES ('5003', '二级菜单', null, '', '5000', 'icon-caidanguanli', '', '1', '0', '0', '2018-11-21 17:49:18', '2018-11-21 17:53:25', '0');
INSERT INTO `sys_menu` VALUES ('5004', '二级菜单', null, 'index', '5003', 'icon-caidanguanli', 'views/crud/index', '1', '0', '0', '2018-11-21 17:53:51', '2018-12-20 14:26:53', '1');

-- ----------------------------
-- Table structure for `sys_oauth_client_details`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `client_id` varchar(32) NOT NULL DEFAULT '',
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
INSERT INTO `sys_oauth_client_details` VALUES ('app', null, 'app', 'server', 'password,refresh_token', null, null, null, null, null, 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('daemon', null, 'daemon', 'server', 'password,refresh_token', null, null, null, null, null, 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('gen', null, 'gen', 'server', 'password,refresh_token', null, null, null, null, null, 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('pig', null, 'pig', 'server', 'password,refresh_token,authorization_code,client_credentials', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login', null, null, null, null, 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('test', null, 'test', 'ui', 'password,refresh_token', null, null, null, null, null, 'true');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_idx1_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', 'ROLE_ADMIN', '管理员', '2017-10-29 15:45:51', '2019-03-31 17:18:14', '0');
INSERT INTO `sys_role` VALUES ('2', 'ROLE_CQQ', 'ROLE_CQQ', 'ROLE_CQQ', '2018-11-11 19:42:26', '2019-04-03 21:08:23', '0');

-- ----------------------------
-- Table structure for `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` int(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与部门对应关系';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES ('1', '1', '8');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1000');
INSERT INTO `sys_role_menu` VALUES ('1', '1100');
INSERT INTO `sys_role_menu` VALUES ('1', '1101');
INSERT INTO `sys_role_menu` VALUES ('1', '1102');
INSERT INTO `sys_role_menu` VALUES ('1', '1103');
INSERT INTO `sys_role_menu` VALUES ('1', '1200');
INSERT INTO `sys_role_menu` VALUES ('1', '1201');
INSERT INTO `sys_role_menu` VALUES ('1', '1202');
INSERT INTO `sys_role_menu` VALUES ('1', '1203');
INSERT INTO `sys_role_menu` VALUES ('1', '1300');
INSERT INTO `sys_role_menu` VALUES ('1', '1301');
INSERT INTO `sys_role_menu` VALUES ('1', '1302');
INSERT INTO `sys_role_menu` VALUES ('1', '1303');
INSERT INTO `sys_role_menu` VALUES ('1', '1304');
INSERT INTO `sys_role_menu` VALUES ('1', '1400');
INSERT INTO `sys_role_menu` VALUES ('1', '1401');
INSERT INTO `sys_role_menu` VALUES ('1', '1402');
INSERT INTO `sys_role_menu` VALUES ('1', '1403');
INSERT INTO `sys_role_menu` VALUES ('1', '2000');
INSERT INTO `sys_role_menu` VALUES ('1', '2100');
INSERT INTO `sys_role_menu` VALUES ('1', '2101');
INSERT INTO `sys_role_menu` VALUES ('1', '2200');
INSERT INTO `sys_role_menu` VALUES ('1', '2201');
INSERT INTO `sys_role_menu` VALUES ('1', '2202');
INSERT INTO `sys_role_menu` VALUES ('1', '2203');
INSERT INTO `sys_role_menu` VALUES ('1', '2400');
INSERT INTO `sys_role_menu` VALUES ('1', '2500');
INSERT INTO `sys_role_menu` VALUES ('1', '2600');
INSERT INTO `sys_role_menu` VALUES ('1', '3000');
INSERT INTO `sys_role_menu` VALUES ('1', '3100');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lock_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，9-锁定',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  `wx_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信openid',
  `qq_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ openid',
  PRIMARY KEY (`user_id`),
  KEY `user_wx_openid` (`wx_openid`),
  KEY `user_qq_openid` (`qq_openid`),
  KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$kmvwL5Jjbrq7X9u5Z3mlD.9iDcF.bryxXsnjJyt5pOfINHZFV5FAK', null, '13168923723', '496b3ace787342f7954b7045b8b06804.jpeg', '1', '2018-04-20 07:15:18', '2019-04-02 14:05:42', '0', '0', 'o_0FT0uyg_H1vVy2H0JpSwlVGhWQ', null);
INSERT INTO `sys_user` VALUES ('5', 'test', '$2a$10$YTGKLJhHCHeGUTTS/se6eu4n48uZ.sBdyUOAXIvYVYMz0sJDj1XZC', null, null, null, '1', '2019-04-09 11:21:31', '2019-04-09 11:21:38', '0', '1', null, null);
INSERT INTO `sys_user` VALUES ('6', 'test', '$2a$10$I3CcJHxvJ5uf0J4U7O0kEuAPsEuUE105A06NSGTGqOQz9L9OpOSU6', null, null, null, '1', '2019-04-09 11:27:32', '2019-04-09 11:27:35', '0', '1', null, null);

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2');
