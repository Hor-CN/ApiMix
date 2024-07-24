-- 数据库初始化
-- 创建库
create
database if not exists apimix;
-- 切换库
use
apimix;

-- ----------------------------
-- 用户信息表
-- ----------------------------
drop table if exists `user`;
CREATE TABLE `user`
(
    `id`          bigint(20) NOT NULL COMMENT '用户ID',
    `user_name`   varchar(20) NOT NULL DEFAULT '' COMMENT '用户名称',
    `nick_name`   varchar(30)          DEFAULT '' COMMENT '用户昵称',
    `type`        tinyint(1) DEFAULT '2' COMMENT '用户类型（1系统用户 2注册用户）',
    `email`       varchar(50)          DEFAULT '' COMMENT '用户邮箱',
    `gender`      tinyint(1) DEFAULT '3' COMMENT '用户性别(1男 2女 3未知)',
    `avatar`      varchar(256)         DEFAULT '' COMMENT '用户头像',
    `status`      tinyint(1) DEFAULT '1' COMMENT '用户状态(1启用 0禁用)',
    `description` varchar(500)         DEFAULT '' COMMENT '用户描述',
    `salt`        varchar(32) NOT NULL DEFAULT '' COMMENT '密码盐值',
    `password`    varchar(32) NOT NULL DEFAULT '' COMMENT 'MD5密码',
    `is_deleted`  tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志（0代表存在 1代表删除）',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- 用户信息表插入数据
-- Records of user
-- ----------------------------
INSERT INTO `apimix`.`user` (`id`, `user_name`, `nick_name`, `type`, `email`, `gender`, `avatar`, `status`,
                             `description`, `salt`, `password`, `is_deleted`, `create_time`, `update_time`)
VALUES (18265100971000199, 'admin', '管理员', 2, 'hor@itbk.cn', 1,
        'https://q1.qlogo.cn/g?b=qq&nk=2786773385&s=100&t=1547904810', 1, '系统初始用户',
        '7b8e837dc9da29525c7f9bf89b5bc179', '809846069fcd2172754a5db0bb17f17e', 0, '2023-10-30 22:39:20',
        '2023-12-04 23:31:30');
INSERT INTO `apimix`.`user` (`id`, `user_name`, `nick_name`, `type`, `email`, `gender`, `avatar`, `status`,
                             `description`, `salt`, `password`, `is_deleted`, `create_time`, `update_time`)
VALUES (18525250253000139, 'test', '测试用户', 2, 'horcn@qq.com', 2,
        'https://q1.qlogo.cn/g?b=qq&nk=2786773385&s=100&t=1547904810', 1, '测试用户',
        '6f53435e7199103469bc397973f23673', 'c4db6af2f1079cf5f69e73a2957f4ff1', 0, '2023-11-02 22:55:10',
        '2023-12-06 20:47:02');


-- ----------------------------
-- 角色表
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `code`        varchar(255) NOT NULL DEFAULT '' COMMENT '角色编码',
    `name`        varchar(255) NOT NULL COMMENT '角色名称',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '角色状态(1启用 0禁用)',
    `type`        tinyint(1) NOT NULL DEFAULT '2' COMMENT '角色类型(1内置 2自定义)',
    `description` varchar(255)          DEFAULT NULL COMMENT '角色描述',
    `is_delete`   tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_code_unIndex` (`code`)
) AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- 角色表插入数据
-- Records of role
-- ----------------------------
INSERT INTO `apimix`.`role` (`id`, `code`, `name`, `status`, `type`, `description`, `is_delete`, `create_time`)
VALUES (1, '*', '上帝', 0, 1, '系统初始角色，拥有无上权力', 0, '2022-03-06 14:16:04');
INSERT INTO `apimix`.`role` (`id`, `code`, `name`, `status`, `type`, `description`, `is_delete`, `create_time`)
VALUES (2, 'admin', '管理员', 0, 1, '拥有管理员权限', 0, '2022-03-06 14:16:34');
INSERT INTO `apimix`.`role` (`id`, `code`, `name`, `status`, `type`, `description`, `is_delete`, `create_time`)
VALUES (3, 'developer', '开发者', 0, 1, '开发者，拥有上传接口管理自建接口的权限', 0, '2022-03-06 14:21:04');
INSERT INTO `apimix`.`role` (`id`, `code`, `name`, `status`, `type`, `description`, `is_delete`, `create_time`)
VALUES (4, 'user', '普通用户', 0, 2, '普通用户', 0, '2023-11-02 22:09:17');
INSERT INTO `apimix`.`role` (`id`, `code`, `name`, `status`, `type`, `description`, `is_delete`, `create_time`)
VALUES (5, 'test', '测试角色', 1, 2, '用于测试才创建角色', 0, '2023-12-05 20:28:04');


-- ----------------------------
-- 用户和角色关联表
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
    `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`)
) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户和角色关联表';

-- ----------------------------
-- 用户和角色关联表插入数据
-- Records of user_role
-- ----------------------------
INSERT INTO `apimix`.`user_role` (`id`, `user_id`, `role_id`)
VALUES (1, 18265100971000199, 1);
INSERT INTO `apimix`.`user_role` (`id`, `user_id`, `role_id`)
VALUES (2, 18265100971000199, 2);
INSERT INTO `apimix`.`user_role` (`id`, `user_id`, `role_id`)
VALUES (3, 18525250253000139, 4);


-- ----------------------------
-- 菜单权限表
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   bigint(20) DEFAULT '0' COMMENT '父菜单ID',
    `path`        varchar(255) DEFAULT '' COMMENT '路由地址',
    `component`   varchar(255) DEFAULT NULL COMMENT '组件路径',
    `redirect`    varchar(255) DEFAULT '' COMMENT '路由重定向',
    `type`        tinyint(1) NOT NULL DEFAULT '1' COMMENT '菜单类型（1目录 2菜单 3按钮）',
    `title`       varchar(255) NOT NULL COMMENT '菜单名称',
    `svgIcon`     varchar(100) DEFAULT '' COMMENT '自定义图标 (比icon优先级高)',
    `icon`        varchar(100) DEFAULT '' COMMENT '菜单图标',
    `keepAlive`   tinyint(1) DEFAULT '0' COMMENT '是否缓存，默认false，页面的name要跟路由的name保持一致才能缓',
    `hidden`      tinyint(1) DEFAULT '0' COMMENT '是否隐藏（0否 1是）',
    `status`      tinyint(1) DEFAULT '1' COMMENT '菜单状态（0停用 1启用）',
    `showInTabs`  tinyint(1) DEFAULT '1' COMMENT '默认true，如果为false，则不会显示在页签中',
    `alwaysShow`  tinyint(1) DEFAULT '0' COMMENT '总是显示',
    `affix`       tinyint(1) DEFAULT '0' COMMENT '默认false，如果设置为true，它则会固定在Tab栏中，例如首页',
    `sort`        tinyint(10) DEFAULT 0 COMMENT '排序',
    `activeMenu`  varchar(255) DEFAULT NULL COMMENT '置了该属性进入路由时，则会高亮activeMenu属性对应的侧边栏',
    `breadcrumb`  tinyint(1) DEFAULT '1' COMMENT '默认true，如果设置为false，则不会在面包屑中显示',
    `query`       varchar(255) DEFAULT NULL COMMENT '路由参数',
    `permission`  varchar(100) DEFAULT NULL COMMENT '权限标识',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';


-- ----------------------------
-- 菜单权限表插入数据
-- Records of menu
-- ----------------------------
-- 一级菜单
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (1, 0, '/analyse', 'Layout', '/analyse/index', 1, '分析页', '', '', 0, 0, 1, 1, 0, 0, 2, NULL, 1, NULL, NULL,
        '2023-12-07 21:36:54');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (2, 0, '/data', 'Layout', '/data/index', 1, '数据管理', 'menu-data', 'icon-list', 1, 0, 1, 1, 0, 0, 3, NULL, 1,
        NULL, NULL, '2023-12-07 21:39:16');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (3, 0, '/file', 'Layout', '/file/index', 1, '文件管理', 'menu-file', 'icon-folder', 1, 0, 1, 1, 0, 0, 4, NULL, 1,
        NULL, NULL, '2023-12-07 21:40:23');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (4, 0, '/system', 'Layout', 'noRedirect', 1, '系统管理', 'menu-system', '', 0, 0, 1, 1, 0, 0, 5, NULL, 1, NULL,
        NULL, '2023-12-07 21:42:49');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (5, 0, '/test', 'Layout', 'noRedirect', 1, '权限测试', 'menu-test', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, NULL,
        '2023-12-07 21:51:52');
-- 二级菜单
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (101, 1, '/analyse/index', 'analyse/index', '', 2, '分析页', 'menu-chart', '', 0, 0, 1, 1, 0, 1, 1, NULL, 0,
        NULL, NULL, '2023-12-07 21:46:58');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (102, 4, '/system/user', 'system/user/index', '', 2, '用户管理', '', 'icon-user', 0, 0, 1, 1, 0, 0, 0, NULL, 1,
        NULL, NULL, '2023-12-07 21:49:23');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (103, 4, '/system/role', 'system/role/index', '', 2, '角色管理', '', 'icon-common', 0, 0, 1, 1, 0, 0, 0, NULL, 1,
        NULL, NULL, '2023-12-07 21:50:25');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (105, 5, '/test/page1', 'test/page1/index', '', 2, '测试页面1', '', 'icon-menu', 0, 0, 1, 1, 0, 0, 0, NULL, 1,
        NULL, NULL, '2023-12-07 21:53:27');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
VALUES (106, 5, '/test/page2', 'test/page2/index', '', 2, '测试页面2', '', 'icon-menu', 0, 0, 1, 1, 0, 0, 0, NULL, 1,
        NULL, NULL, '2023-12-07 21:53:47');
INSERT INTO `apimix`.`menu` (`id`, `parent_id`, `path`, `component`, `redirect`, `type`, `title`, `svgIcon`, `icon`,
                             `keepAlive`, `hidden`, `status`, `showInTabs`, `alwaysShow`, `affix`, `sort`, `activeMenu`,
                             `breadcrumb`, `query`, `permission`, `create_time`)
-- 按钮
VALUES (201, 106, '', '', '', 3, '按钮新增', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'user:btn:add',
        '2023-12-07 21:55:11');



-- ----------------------------
-- 角色和菜单关联表     角色1-N菜单
-- Table structure for role_menu
-- ----------------------------
CREATE TABLE `role_menu`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
    `menu_id` int(20) DEFAULT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`)
) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '角色和菜单关联表';

-- ----------------------------
-- 角色和菜单关联表数据
-- Records of role_menu
-- ----------------------------
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (1, 4, 1);
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (2, 4, 2);
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (3, 4, 3);
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (4, 4, 5);
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (5, 4, 106);
INSERT INTO `apimix`.`role_menu` (`id`, `role_id`, `menu_id`) VALUES (6, 4, 201);




-- 接口信息
create table if not exists `interface_info`
(
    `api_id`
    int
    NOT
    NULL
    auto_increment
    comment
    '主键ID',
    `api_name`
    varchar
(
    256
) NOT NULL COMMENT '接口名称',
    `api_url` varchar
(
    512
) NOT NULL COMMENT '接口地址',
    `api_description` varchar
(
    256
) NULL comment '接口描述',
    `api_method` varchar
(
    20
) NOT NULL COMMENT '请求类型：GET,PUT,POST,DELETE',
    `api_schema` mediumtext NULL COMMENT '接口的请求/响应数据结构',
    `api_sample` mediumtext NULL COMMENT '请求/响应/请求头样本数据',
    `api_status` varchar
(
    4
) NOT NULL COMMENT '状态：-1-删除, 0-草稿，1-发布，2-有变更，3-禁用',
    `user_id` char
(
    32
) NOT NULL COMMENT '创建人',
    `create_time` datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY
(
    api_id
),
    UNIQUE KEY uk_interface_info
(
    api_url
)
    ) CHARACTER SET = utf8
    COLLATE = utf8_general_ci comment 'API接口信息';


INSERT INTO `horapi`.`interface_info`
VALUES (1, '每日早报', 'https://hub.onmicrosoft.cn/public/news',
        '每日早报接口，有 网易新闻、知乎源：60s读懂世界文字版 两个接口，最多支持100天回调。', 'GET,POST',
        '', NULL, '0', 'b26c375b9473489aa8a334170ec58b99', '2023-10-21 22:44:42', '2023-10-21 22:44:42');


-- 用户调用接口关系表
create table if not exists `user_interface_relation`
(
    `id`
    bigint
    not
    null
    auto_increment
    comment
    '主键'
    primary
    key,
    `user_id`
    char
(
    32
) not null comment '用户 id',
    `api_id` bigint not null comment '接口 id',
    `api_key` char
(
    32
) not null comment 'API key',
    `total_calls` int default 0 not null comment '总调用次数',
    `used_calls` bigint default 0 not null comment '已调用次数',
    `status` boolean default 1 not null comment '1-正常，0-禁用',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
    ) CHARACTER SET = utf8
    COLLATE = utf8_general_ci comment '用户调用接口关系';

--
-- -- ----------------------------
-- -- 权限表
-- -- Table structure for permissions
-- -- ----------------------------
-- CREATE TABLE if not exists `permissions`
-- (
--     `id` int
-- (
--     11
-- ) NOT NULL AUTO_INCREMENT,
--     `perms` varchar
-- (
--     50
-- ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
--     `name` varchar
-- (
--     50
-- ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
--     `description` varchar
-- (
--     256
-- ) null comment '权限描述',
--     `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY
-- (
--     `id`
-- ) USING BTREE,
--     UNIQUE INDEX `permissions_id_unIndex`
-- (
--     `id`
-- )
--   USING BTREE
--     ) ENGINE = MyISAM
--     AUTO_INCREMENT = 1
--     CHARACTER SET = utf8
--     COLLATE = utf8_general_ci COMMENT = '权限表'
--     ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- 权限表插入数据
-- -- Records of permissions
-- -- ----------------------------
-- #
-- INSERT INTO `permissions`
-- # VALUES (1, '*', '上帝权力', '拥有至高无上的权力,可以通过任何权限检验.', '2022-03-06 23:14:07');
-- #
-- INSERT INTO `permissions`
-- # VALUES (2, 'user.*', '用户管理权', '拥有完整的用户管理权,添加,,删除,修改,禁用', '2022-03-06 23:20:57');
-- #
-- INSERT INTO `permissions`
-- # VALUES (3, 'user.add', '增加用户权', '可以增加用户', '2022-03-06 23:20:57');
-- #
-- INSERT INTO `permissions`
-- # VALUES (4, 'user.delete', '删除用户权', '可以删除用户', '2022-03-06 23:20:57');
-- #
-- INSERT INTO `permissions`
-- # VALUES (5, 'user.update', '修改用户权', '可以修改用户', '2022-03-06 23:20:57');
-- #
-- INSERT INTO `permissions`
-- # VALUES (6, 'user.get', '查看用户权', '可以查看用户', '2022-03-06 23:20:57');



