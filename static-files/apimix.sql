/*
 Navicat Premium Data Transfer

 Source Server         : apimix
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : apimix

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 30/08/2024 20:35:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：冻结）',
  `amount` bigint(20) NOT NULL DEFAULT 0 COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, 18265100971000199, 0, 0);
INSERT INTO `account` VALUES (2, 18525250253000139, 0, 0);
INSERT INTO `account` VALUES (3, 22333370819000179, 0, 0);
INSERT INTO `account` VALUES (4, 22497038442000130, 0, 0);
INSERT INTO `account` VALUES (5, 36151022656000161, 0, 0);

-- ----------------------------
-- Table structure for api_example
-- ----------------------------
DROP TABLE IF EXISTS `api_example`;
CREATE TABLE `api_example`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `api_id` bigint(20) NOT NULL COMMENT 'api',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '示例名称',
  `code` int(11) NOT NULL COMMENT '状态码',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_example
-- ----------------------------
INSERT INTO `api_example` VALUES (1, 36846298423000181, '200', 200, 'application/json;charset=utf-8', '{\n	\"code\": 200,\n	\"msg\": \"请求成功\",\n	\"data\": {\n		\"copywriting\": \"男朋友跟我分手了，我心碎了，决定见他一面把事情说清楚，如果他非要分手我也无话可说。我买了去上海的机票，坐了两个小时的飞机，到了之后却因为疫情被封小区了他出不来。我心如死灰在大街上游荡，打开手机看到了我们的恩爱时光泪流满面，忽然注意到手机上显示的时间：今天肯德基疯狂星期四，谁请我吃?\"\n	}\n}', '2024-06-02 00:05:58', '2024-08-26 23:06:38');
INSERT INTO `api_example` VALUES (2, 36894326755000135, '200', 200, 'text/html; charset=utf-8', '{\n	\"code\": 200,\n	\"msg\": \"success\",\n	\"data\": {\n		\"prov\": \"湖北\",\n		\"p0\": \"7.66\",\n		\"p89\": \"0\",\n		\"p92\": \"8.01\",\n		\"p95\": \"8.58\",\n		\"p98\": \"9.96\",\n		\"time\": \"2024-06-02 09:00:05.513\"\n	}\n}', '2024-06-02 13:26:26', '2024-06-02 13:26:26');
INSERT INTO `api_example` VALUES (3, 37086108003000155, '200', 200, 'application/json;', '{\n	\"code\": 200,\n	\"text\": \"解析成功\",\n	\"type\": \"歌曲解析\",\n	\"now\": \"2024-06-04 18:41:42\",\n	\"data\": {\n		\"name\": \"起风了\",\n		\"singername\": \"周深\",\n		\"duration\": null,\n		\"file_size\": null,\n		\"song_url\": \"http://aqqmusic.tc.qq.com/C400002J3L3S3suxlD.m4a?guid=8600000870&vkey=8E007CFC5AA1309DECCE782C36C146F697D7F5F07E91A32B378B25B8EBA4A6EB32888F0D8E85459B7C2B107E1AB0C4651580A0D7247B7375&uin=&fromtag=123032\",\n		\"mv_url\": null,\n		\"mid\": \"002w57E00BGzXn\",\n		\"media_mid\": \"002J3L3S3suxlD\",\n		\"album_img\": \"https://y.qq.com/music/photo_new/T002R300x300M000000CVCqK4aEW0M_2.jpg\"\n	}\n}', '2024-06-04 18:42:48', '2024-06-04 18:42:48');

-- ----------------------------
-- Table structure for api_info
-- ----------------------------
DROP TABLE IF EXISTS `api_info`;
CREATE TABLE `api_info`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键ID',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口名称',
  `proxy` tinyint(1) NULL DEFAULT 0 COMMENT '是否代理',
  `url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口地址',
  `logo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口图标地址',
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口描述',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接口介绍 Markdown',
  `return_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回类型',
  `method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求类型：GET,PUT,POST,DELETE',
  `status` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态：0-下线，1-上线',
  `is_paid` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否收费',
  `user_id` bigint(20) NOT NULL COMMENT '创建人',
  `is_delete` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '逻辑删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_interface_info`(`url`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'API接口信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of api_info
-- ----------------------------
INSERT INTO `api_info` VALUES (36846298423000181, '肯德基V50文案', 0, 'https://api.ahfi.cn/api/kfcv50', 'https://img2.baidu.com/it/u=599376659,816888892&fm=253&fmt=auto&app=138&f=JPEG?w=285&h=285', '疯狂星期四文案', '### 产品功能\n当你要KFC文案时，但是不知道、也想不到想的时候，本API 就可以很好帮你解决这一难题。\n', 'JSON', 'GET', '1', 0, 18265100971000199, 0, '2024-06-02 00:05:58', '2024-06-18 20:46:23');
INSERT INTO `api_info` VALUES (36894326755000135, '今日油价', 1, 'https://api.qqsuu.cn/api/dm-oilprice', 'https://img1.baidu.com/it/u=3859563011,321443097&fm=253&fmt=auto&app=138&f=PNG?w=243&h=243', '根据国家公布的油价，次日更新，可查询全国31个省份的具体油价，及油价变动情况。', '### 产品亮点\n1、根据国家公布的油价，次日更新，可查询全国31个省份的具体油价，及油价变动情况。\n2、官方权威数据，仅供高质接口，实时校验结果，可支持高并发，24h技术专家在线对接。\n3、为上万家企事业提供年均超100亿次调用服务。\n\n### 产品说明\n产品介绍：根据国家公布的油价，次日更新，可查询全国31个省份的具体油价，及油价变动情况。\n\n使用场景：广泛应用于车主服务APP、公众号/小程序、汽车网站等应用场景。', 'JSON', 'GET', '1', 1, 18265100971000199, 0, '2024-06-02 13:26:26', '2024-08-09 22:35:18');
INSERT INTO `api_info` VALUES (37086108003000155, 'QQ音乐', 0, 'http://api.mrgnb.cn/API/qqmusic.php', 'https://y.gtimg.cn/music/photo_new/T011R1000x1000M000000GGEmm3ejFv2.png?maxage=2592000', '输入歌曲名，获取对应QQ音乐歌曲列表或，并且可以获取对应歌曲下载链接', '输入歌曲名，获取对应QQ音乐歌曲列表或，并且可以获取对应歌曲下载链接', 'JSON', 'GET', '1', 0, 18265100971000199, 0, '2024-06-04 18:42:48', '2024-06-10 21:12:29');

-- ----------------------------
-- Table structure for api_log
-- ----------------------------
DROP TABLE IF EXISTS `api_log`;
CREATE TABLE `api_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `request_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求id',
  `target_server` bigint(20) NOT NULL COMMENT '目标服务',
  `target_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标名称',
  `request_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求路径',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `origin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '第三方应用' COMMENT '来源',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求IP',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未知' COMMENT '城市',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `request_headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求头',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `request_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求体',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应状态',
  `response_headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应头',
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应体',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `response_time` datetime NOT NULL COMMENT '响应时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_log
-- ----------------------------
INSERT INTO `api_log` VALUES (1, 'd555a370-2', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '192.168.0.119', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"s8e5947504b846cc9293129e68acc7ss\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"192.168.0.119:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=t0qecpkqck9unj6b864p0spvvh\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Sat, 10 Aug 2024 08:27:13 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Vary\": [\n        \"Accept-Encoding\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-10 08:27:13', '2024-08-10 08:27:13', '2024-08-10 08:27:13', '2024-08-10 08:27:13', '2024-08-10 16:27:13');
INSERT INTO `api_log` VALUES (2, 'd555a370-3', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '192.168.0.119', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"s8e5947504b846cc9293129e68acc7ss\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"192.168.0.119:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=t0qecpkqck9unj6b864p0spvvh\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Sat, 10 Aug 2024 08:29:57 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Vary\": [\n        \"Accept-Encoding\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-10 08:29:57', '2024-08-10 08:29:57', '2024-08-10 08:29:57', '2024-08-10 08:29:57', '2024-08-10 16:29:57');
INSERT INTO `api_log` VALUES (3, 'd555a370-4', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '192.168.0.119', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"s8e5947504b846cc9293129e68acc7ss\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"192.168.0.119:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=t0qecpkqck9unj6b864p0spvvh\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Sat, 10 Aug 2024 08:42:54 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Vary\": [\n        \"Accept-Encoding\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-10 08:42:54', '2024-08-10 08:42:55', '2024-08-10 08:42:55', '2024-08-10 08:42:55', '2024-08-10 16:42:54');
INSERT INTO `api_log` VALUES (4, 'd555a370-5', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '192.168.0.119', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"s8e5947504b846cc9293129e68acc7ss\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"192.168.0.119:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=t0qecpkqck9unj6b864p0spvvh\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Sat, 10 Aug 2024 09:12:15 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Vary\": [\n        \"Accept-Encoding\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-10 09:12:15', '2024-08-10 09:12:15', '2024-08-10 09:12:15', '2024-08-10 09:12:15', '2024-08-10 17:12:15');
INSERT INTO `api_log` VALUES (5, 'a1d4eaa0-10', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"s8e5947504b846cc9293129e68acc7ss\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 02:16:38 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=e7mthoq7768bomemtvj79st6fa; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 02:16:37', '2024-08-30 02:16:39', '2024-08-30 02:16:37', '2024-08-30 02:16:39', '2024-08-30 10:16:39');
INSERT INTO `api_log` VALUES (6, '330d641a-12', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 04:55:17 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=bhfucb9nov4e33ab85rnepgkvk; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 04:55:17', '2024-08-30 04:55:18', '2024-08-30 04:55:18', '2024-08-30 04:55:18', '2024-08-30 12:55:18');
INSERT INTO `api_log` VALUES (7, '330d641a-13', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 04:55:24 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=09gfq9h2h64pffn0k57li7drc3; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 04:55:25', '2024-08-30 04:55:26', '2024-08-30 04:55:25', '2024-08-30 04:55:26', '2024-08-30 12:55:25');
INSERT INTO `api_log` VALUES (8, '330d641a-14', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 04:55:30 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=3ph1puq2athr4vtsam1vvf1nl4; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 04:55:31', '2024-08-30 04:55:31', '2024-08-30 04:55:31', '2024-08-30 04:55:31', '2024-08-30 12:55:31');
INSERT INTO `api_log` VALUES (9, '330d641a-15', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 04:57:27 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=fufmpmhp96ecrh04ghscnah77i; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 04:57:29', '2024-08-30 04:57:29', '2024-08-30 04:57:29', '2024-08-30 04:57:29', '2024-08-30 12:57:28');
INSERT INTO `api_log` VALUES (10, '330d641a-17', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 04:59:20 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=tln69cb3s683cnh8feqjd9kncf; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 04:59:21', '2024-08-30 04:59:21', '2024-08-30 04:59:21', '2024-08-30 04:59:21', '2024-08-30 12:59:21');
INSERT INTO `api_log` VALUES (11, 'a81fb4c0-21', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=e7mthoq7768bomemtvj79st6fa\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:00:54 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:00:55', '2024-08-30 05:00:55', '2024-08-30 05:00:55', '2024-08-30 05:00:55', '2024-08-30 13:00:55');
INSERT INTO `api_log` VALUES (12, 'a81fb4c0-22', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"User-Agent\": [\n        \"Apifox/1.0.0 (https://apifox.com)\"\n    ],\n    \"Accept\": [\n        \"*/*\"\n    ],\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"Cookie\": [\n        \"PHPSESSID=e7mthoq7768bomemtvj79st6fa\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:01:09 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:01:10', '2024-08-30 05:01:10', '2024-08-30 05:01:10', '2024-08-30 05:01:10', '2024-08-30 13:01:10');
INSERT INTO `api_log` VALUES (13, '330d641a-23', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:01:32 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:01:33', '2024-08-30 05:01:33', '2024-08-30 05:01:33', '2024-08-30 05:01:33', '2024-08-30 13:01:33');
INSERT INTO `api_log` VALUES (14, 'a81fb4c0-24', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"*\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:02:22 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:02:23', '2024-08-30 05:02:24', '2024-08-30 05:02:23', '2024-08-30 05:02:24', '2024-08-30 13:02:23');
INSERT INTO `api_log` VALUES (15, '330d641a-25', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:02:59 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:03:00', '2024-08-30 05:03:01', '2024-08-30 05:03:00', '2024-08-30 05:03:01', '2024-08-30 13:03:00');
INSERT INTO `api_log` VALUES (16, '04b01c58-26', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:13:16 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=2lpp0svei54jv0gcadh77kau0m; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:13:16', '2024-08-30 05:13:18', '2024-08-30 05:13:16', '2024-08-30 05:13:18', '2024-08-30 13:13:17');
INSERT INTO `api_log` VALUES (17, '04b01c58-27', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:15:59 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=i9q087pg785cb8stm618dqtm2m; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:16:00', '2024-08-30 05:16:01', '2024-08-30 05:16:00', '2024-08-30 05:16:01', '2024-08-30 13:16:00');
INSERT INTO `api_log` VALUES (18, 'bdf314e3-1', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:22:39 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=gj2vjgnfcbs3r8oaho2dp4chsj; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:22:40', '2024-08-30 05:22:40', '2024-08-30 05:22:40', '2024-08-30 05:22:40', '2024-08-30 13:22:40');
INSERT INTO `api_log` VALUES (19, 'e31d2a4f-2', 36894326755000135, '今日油价', '/api/36894326755000135', 'GET', '第三方应用', '0:0:0:0:0:0:0:1', '未知', 18265100971000199, '{\n    \"Host\": [\n        \"localhost:2786\"\n    ],\n    \"Connection\": [\n        \"keep-alive\"\n    ],\n    \"X-APIMix-Token\": [\n        \"c8e5947504b846cc9293129e68acc72e\"\n    ],\n    \"Accept\": [\n        \"application/json, text/plain, */*\"\n    ],\n    \"sec-ch-ua\": [\n        \"\\\"Chromium\\\";v=\\\"128\\\", \\\"Not;A=Brand\\\";v=\\\"24\\\", \\\"Google Chrome\\\";v=\\\"128\\\"\"\n    ],\n    \"sec-ch-ua-mobile\": [\n        \"?0\"\n    ],\n    \"User-Agent\": [\n        \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36\"\n    ],\n    \"sec-ch-ua-platform\": [\n        \"\\\"Windows\\\"\"\n    ],\n    \"Origin\": [\n        \"http://localhost:5173\"\n    ],\n    \"Sec-Fetch-Site\": [\n        \"same-site\"\n    ],\n    \"Sec-Fetch-Mode\": [\n        \"cors\"\n    ],\n    \"Sec-Fetch-Dest\": [\n        \"empty\"\n    ],\n    \"Referer\": [\n        \"http://localhost:5173/\"\n    ],\n    \"Accept-Encoding\": [\n        \"gzip, deflate, br, zstd\"\n    ],\n    \"Accept-Language\": [\n        \"zh-CN,zh;q=0.9\"\n    ]\n}', '{\n    \"prov\": [\n        \"湖北\"\n    ]\n}', '无', '成功', '{\n    \"transfer-encoding\": [\n        \"chunked\"\n    ],\n    \"Vary\": [\n        \"Origin\",\n        \"Access-Control-Request-Method\",\n        \"Access-Control-Request-Headers\",\n        \"Accept-Encoding\"\n    ],\n    \"Access-Control-Allow-Origin\": [\n        \"http://localhost:5173\",\n        \"*\"\n    ],\n    \"Access-Control-Allow-Credentials\": [\n        \"true\"\n    ],\n    \"Server\": [\n        \"nginx\"\n    ],\n    \"Access-Control-Allow-Methods\": [\n        \"GET,POST\"\n    ],\n    \"Pragma\": [\n        \"no-cache\"\n    ],\n    \"Access-Control-Allow-Headers\": [\n        \"x-requested-with,content-type\"\n    ],\n    \"Date\": [\n        \"Fri, 30 Aug 2024 05:29:46 GMT\"\n    ],\n    \"Strict-Transport-Security\": [\n        \"max-age=31536000\"\n    ],\n    \"Cache-Control\": [\n        \"no-store, no-cache, must-revalidate\"\n    ],\n    \"Set-Cookie\": [\n        \"PHPSESSID=82m4tkcakilogv2gvsfk4vvipk; path=/; HttpOnly\"\n    ],\n    \"Expires\": [\n        \"Thu, 19 Nov 1981 08:52:00 GMT\"\n    ],\n    \"Content-Type\": [\n        \"text/html;charset=utf-8\"\n    ]\n}', '只记录类型为application/json的内容', '2024-08-30 05:29:47', '2024-08-30 05:29:47', '2024-08-30 05:29:47', '2024-08-30 05:29:47', '2024-08-30 13:29:47');

-- ----------------------------
-- Table structure for api_param
-- ----------------------------
DROP TABLE IF EXISTS `api_param`;
CREATE TABLE `api_param`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父ID',
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  `in` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'query' COMMENT 'query|body|header',
  `part` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'request' COMMENT 'response|request',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'string' COMMENT 'string|int',
  `is_required` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否必须',
  `order` tinyint(3) NULL DEFAULT 0 COMMENT '参数排序',
  `example` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例值',
  `explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_param
-- ----------------------------
INSERT INTO `api_param` VALUES (1, 0, 36846298423000181, 'query', 'request', 'type', 'string', 1, 1, 'json', '返回json的格式数据', '2024-06-02 00:05:58', '2024-06-02 19:28:07');
INSERT INTO `api_param` VALUES (2, 0, 36894326755000135, 'query', 'request', 'prov', 'string', 1, 2, '湖北', '查询的地区', '2024-06-02 13:26:26', '2024-06-02 19:28:10');
INSERT INTO `api_param` VALUES (3, 0, 37086108003000155, 'query', 'request', 'msg', 'string', 1, NULL, '起风了', '歌曲名', '2024-06-04 18:42:48', '2024-06-04 18:42:48');
INSERT INTO `api_param` VALUES (4, 0, 37086108003000155, 'query', 'request', 'n', 'string', 1, NULL, '1', '选择获取下载链接的序号', '2024-06-04 18:42:48', '2024-06-04 18:42:48');
INSERT INTO `api_param` VALUES (6, 0, 38443509231000159, 'query', 'request', 'abc', 'string', 0, NULL, NULL, NULL, '2024-06-20 11:46:09', '2024-06-20 11:46:09');

-- ----------------------------
-- Table structure for api_token
-- ----------------------------
DROP TABLE IF EXISTS `api_token`;
CREATE TABLE `api_token`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `token_id` bigint(20) NOT NULL COMMENT 'Token ID',
  `api_id` bigint(20) NOT NULL COMMENT 'API ID',
  `total_quota` bigint(20) NOT NULL COMMENT '分配的额度',
  `used_quota` bigint(20) NOT NULL COMMENT '使用的额度',
  `is_unlimited` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否是不受额度限制的 Token',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_token
-- ----------------------------

-- ----------------------------
-- Table structure for audit
-- ----------------------------
DROP TABLE IF EXISTS `audit`;
CREATE TABLE `audit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_no` bigint(20) NOT NULL COMMENT '业务ID',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '审核类型 （1.api审核，2.开发者认证审核）',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '审核进度（1.待审核，2.通过，3.驳回，4.撤销）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `flow_no`(`flow_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of audit
-- ----------------------------
INSERT INTO `audit` VALUES (1, 36846298423000181, 1, 2);
INSERT INTO `audit` VALUES (2, 36894326755000135, 1, 2);
INSERT INTO `audit` VALUES (3, 37086108003000155, 1, 2);
INSERT INTO `audit` VALUES (4, 22333370819000179, 2, 2);

-- ----------------------------
-- Table structure for audit_record
-- ----------------------------
DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `audit_id` bigint(20) NOT NULL COMMENT '审核表ID',
  `user_id` bigint(20) NULL DEFAULT 0 COMMENT '审核人ID',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '审核进度（1.待审核，2.通过，3.驳回，4.撤销）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of audit_record
-- ----------------------------
INSERT INTO `audit_record` VALUES (1, 1, 0, 1, '用户上传接口', '2024-06-03 22:15:02');
INSERT INTO `audit_record` VALUES (2, 2, 0, 1, '用户上传接口', '2024-06-03 22:15:48');
INSERT INTO `audit_record` VALUES (3, 2, 18265100971000199, 3, '驳回审核', '2024-06-03 23:42:41');
INSERT INTO `audit_record` VALUES (4, 1, 18265100971000199, 2, '通过审核', '2024-06-04 16:55:58');
INSERT INTO `audit_record` VALUES (5, 2, 18265100971000199, 2, '通过审核', '2024-06-04 17:00:19');
INSERT INTO `audit_record` VALUES (15, 3, 0, 1, '用户上传接口', '2024-06-04 18:56:46');
INSERT INTO `audit_record` VALUES (17, 3, 18265100971000199, 2, '接口不错，允许通过。', '2024-06-06 14:31:34');
INSERT INTO `audit_record` VALUES (18, 4, 0, 1, '用户提交', '2024-08-28 21:55:34');
INSERT INTO `audit_record` VALUES (19, 4, 18265100971000199, 2, '通过审核。', '2024-08-29 14:48:37');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ' 主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单图标',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序值',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '生活服务', 'IconCalendar', 0, 0, '2024-05-31 22:16:31', '2024-05-31 22:16:31');
INSERT INTO `category` VALUES (2, '数据智能', 'IconRelation', 1, 0, '2024-06-01 12:59:35', '2024-06-01 12:59:35');
INSERT INTO `category` VALUES (3, '影音娱乐', 'IconLiveBroadcast', 2, 0, '2024-06-01 13:05:01', '2024-06-01 13:05:01');
INSERT INTO `category` VALUES (4, '实用工具', 'IconTool', 3, 0, '2024-06-01 13:06:36', '2024-06-01 13:06:36');
INSERT INTO `category` VALUES (5, '应用开发', 'IconApps', 4, 0, '2024-06-01 14:45:04', '2024-06-01 14:45:04');
INSERT INTO `category` VALUES (6, '图片壁纸', 'IconImage', 5, 0, '2024-06-01 15:22:56', '2024-06-01 15:22:56');
INSERT INTO `category` VALUES (7, '其他', 'IconCommon', 6, 0, '2024-06-01 15:23:36', '2024-06-01 15:23:36');

-- ----------------------------
-- Table structure for category_api
-- ----------------------------
DROP TABLE IF EXISTS `category_api`;
CREATE TABLE `category_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category_api
-- ----------------------------
INSERT INTO `category_api` VALUES (1, 1, 36846298423000181);
INSERT INTO `category_api` VALUES (2, 1, 36894326755000135);
INSERT INTO `category_api` VALUES (3, 3, 37086108003000155);
INSERT INTO `category_api` VALUES (4, 1, 38443509231000159);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '组件路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由重定向',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '菜单类型（1目录 2菜单 3按钮）',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `svgIcon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '自定义图标 (比icon优先级高)',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单图标',
  `keepAlive` tinyint(1) NULL DEFAULT 0 COMMENT '是否缓存，默认false，页面的name要跟路由的name保持一致才能缓',
  `hidden` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏（0否 1是）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '菜单状态（1正常 0禁用）',
  `showInTabs` tinyint(1) NULL DEFAULT 1 COMMENT '默认true，如果为false，则不会显示在页签中',
  `alwaysShow` tinyint(1) NULL DEFAULT 0 COMMENT '总是显示',
  `affix` tinyint(1) NULL DEFAULT 0 COMMENT '默认false，如果设置为true，它则会固定在Tab栏中，例如首页',
  `sort` tinyint(10) NULL DEFAULT 0 COMMENT '排序',
  `activeMenu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '置了该属性进入路由时，则会高亮activeMenu属性对应的侧边栏',
  `breadcrumb` tinyint(1) NULL DEFAULT 1 COMMENT '默认true，如果设置为false，则不会在面包屑中显示',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 247 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 0, '/console/analyse', 'Layout', '/console/analyse/index', 1, '仪表盘', '', 'icon-dashboard', 0, 0, 1, 1, 1, 0, 1, '', 1, NULL, NULL, '2023-12-07 21:36:54');
INSERT INTO `menu` VALUES (2, 0, '/console/account', 'Layout', '', 1, '账户管理', '', 'IconUser', 0, 0, 1, 1, 1, 0, 5, '', 1, NULL, NULL, '2023-12-07 21:39:16');
INSERT INTO `menu` VALUES (4, 0, '/console/system', 'Layout', 'noRedirect', 1, '系统管理', '', 'icon-settings', 0, 0, 1, 1, 0, 0, 7, '', 1, NULL, NULL, '2023-12-07 21:42:49');
INSERT INTO `menu` VALUES (101, 1, '/console/analyse/index', 'console/analyse/index', '', 2, '统计', '', 'icon-computer', 0, 0, 1, 1, 0, 0, 0, '', 0, NULL, NULL, '2023-12-16 12:59:00');
INSERT INTO `menu` VALUES (102, 4, '/console/system/user', 'console/system/user/index', '', 2, '用户管理', '', 'icon-user', 0, 0, 1, 1, 0, 0, 0, '', 1, NULL, NULL, '2023-12-07 21:49:23');
INSERT INTO `menu` VALUES (103, 4, '/console/system/role', 'console/system/role/index', '', 2, '角色管理', '', 'icon-common', 0, 0, 1, 1, 0, 0, 0, '', 1, NULL, NULL, '2023-12-07 21:50:25');
INSERT INTO `menu` VALUES (104, 4, '/console/system/menu', 'console/system/menu/index', '', 2, '菜单管理', '', 'icon-menu', 0, 0, 1, 1, 0, 0, 0, '', 1, NULL, NULL, '2023-12-09 11:04:42');
INSERT INTO `menu` VALUES (202, 104, '', '', '', 3, '菜单查询', '', '', 0, 0, 1, 1, 0, 0, 0, '', 1, NULL, 'sys:menu:list', '2023-12-09 11:06:10');
INSERT INTO `menu` VALUES (207, 0, '/console/visit-controll', 'Layout', '/console/visit-controll/index', 1, '访问控制', '', '', 0, 0, 1, 1, 0, 0, 3, '', 0, NULL, '', '2023-12-16 15:17:40');
INSERT INTO `menu` VALUES (208, 207, '/console/visit-controll/index', 'console/visit-controll/index', '', 2, '访问控制', '', 'IconThunderbolt', 0, 0, 1, 0, 0, 0, 0, '', 0, NULL, '', '2023-12-16 15:18:57');
INSERT INTO `menu` VALUES (216, 0, '/console/data', 'Layout', '/console/data/index', 1, '数据中心', '', 'IconLayers', 0, 0, 1, 1, 1, 0, 2, NULL, 1, NULL, '', '2023-12-18 21:24:58');
INSERT INTO `menu` VALUES (217, 216, '/console/data/index', 'console/data/api', '', 2, '我的API', '', 'IconRelation', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2023-12-18 21:36:35');
INSERT INTO `menu` VALUES (218, 2, '/console/account/info', 'console/account/info/index', '', 2, '个人中心', '', 'IconUser', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2023-12-18 22:08:21');
INSERT INTO `menu` VALUES (219, 2, '/console/account/authentication', 'console/account/developer/index', '', 2, '开发者认证', '', 'IconSafe', 0, 0, 1, 1, 0, 0, 1, NULL, 1, NULL, '', '2023-12-18 22:27:10');
INSERT INTO `menu` VALUES (221, 0, '/console/dev-center', 'Layout', '', 1, '开发者中心', '', 'IconCodeSandbox', 1, 0, 1, 1, 0, 0, 4, NULL, 1, NULL, '', '2023-12-25 13:04:01');
INSERT INTO `menu` VALUES (223, 221, '/console/dev-center/index', 'console/dev-center/index', '', 2, '开发者中心', '', '', 1, 0, 1, 1, 0, 0, 0, NULL, 0, NULL, '', '2023-12-25 17:45:54');
INSERT INTO `menu` VALUES (224, 102, '', '', '', 3, '用户查询', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:user:list', '2024-01-01 19:53:16');
INSERT INTO `menu` VALUES (225, 103, '', '', '', 3, '角色查询', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:role:list', '2024-01-01 19:57:12');
INSERT INTO `menu` VALUES (226, 102, '', '', '', 3, '用户添加', '', '', 0, 0, 1, 1, 0, 0, 1, NULL, 1, NULL, 'sys:user:add', '2024-01-01 20:14:49');
INSERT INTO `menu` VALUES (227, 102, '', '', '', 3, '重置密码', '', '', 0, 0, 1, 1, 0, 0, 3, NULL, 1, NULL, 'sys:user:resetPwd', '2024-01-01 20:26:38');
INSERT INTO `menu` VALUES (228, 102, '', '', '', 3, '用户删除', '', '', 0, 0, 1, 1, 0, 0, 2, NULL, 1, NULL, 'sys:user:del', '2024-01-04 22:53:22');
INSERT INTO `menu` VALUES (229, 102, '', '', '', 3, '用户修改', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:user:edit', '2024-01-12 17:50:25');
INSERT INTO `menu` VALUES (230, 222, '', '', '', 3, '添加接口', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'dev:api:add', '2024-03-16 19:22:19');
INSERT INTO `menu` VALUES (231, 222, '', '', '', 3, '修改接口', '', '', 0, 0, 1, 1, 0, 0, 1, NULL, 1, NULL, 'dev:api:edit', '2024-03-16 21:02:51');
INSERT INTO `menu` VALUES (232, 4, '/console/system/notice', 'console/system/notice/index', '', 2, '通知管理', '', 'IconNotification', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2024-05-25 23:36:18');
INSERT INTO `menu` VALUES (233, 0, '/console/audit-center', 'Layout', '', 1, '审核中心', '', 'IconCodepen', 0, 0, 1, 1, 1, 0, 6, NULL, 1, NULL, '', '2024-05-26 18:34:50');
INSERT INTO `menu` VALUES (234, 233, '/console/audit-center/api', 'console/audit-center/api/index', '', 2, '接口审核', '', 'IconComputer', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2024-05-26 19:27:58');
INSERT INTO `menu` VALUES (235, 232, '', '', '', 3, '通知添加', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:notice:add', '2024-05-29 20:06:08');
INSERT INTO `menu` VALUES (236, 232, '', '', '', 3, '通知修改', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:notice:edit', '2024-05-29 20:06:31');
INSERT INTO `menu` VALUES (237, 232, '', '', '', 3, '通知删除', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:notice:del', '2024-05-29 20:06:48');
INSERT INTO `menu` VALUES (238, 232, '', '', '', 3, '通知查询', '', '', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, 'sys:notice:list', '2024-05-29 20:07:04');
INSERT INTO `menu` VALUES (239, 4, '/console/system/category', 'console/system/category/index', '', 2, '分类管理', '', 'IconApps', 0, 0, 1, 1, 0, 0, 5, NULL, 1, NULL, '', '2024-05-31 18:45:30');
INSERT INTO `menu` VALUES (241, 233, '/console/audit-center/developer', 'console/audit-center/developer/index', '', 2, '开发者认证审核', '', 'IconCodeSquare', 0, 0, 1, 1, 0, 0, 1, NULL, 1, NULL, '', '2024-08-28 22:03:58');
INSERT INTO `menu` VALUES (243, 0, '/console/balanceRecharge', 'Layout', '', 1, '余额充值', '', 'IconGift', 0, 0, 1, 1, 0, 0, 4, NULL, 1, NULL, '', '2024-08-28 22:47:44');
INSERT INTO `menu` VALUES (244, 243, '/console/balanceRecharge/index', 'console/balanceRecharge/index', '', 2, '余额充值', '', 'IconGift', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2024-08-28 22:48:49');
INSERT INTO `menu` VALUES (245, 0, '/console/order', 'Layout', '', 1, '订单', '', 'IconMobile', 0, 0, 1, 1, 0, 0, 4, NULL, 1, NULL, '', '2024-08-28 22:52:14');
INSERT INTO `menu` VALUES (246, 245, '/console/order/index', 'console/order/index', '', 2, '订单', '', 'IconMobile', 0, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, '', '2024-08-28 22:52:59');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `type` tinyint(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `status` tinyint(1) NOT NULL COMMENT '公告状态（1正常 0关闭）',
  `create_By` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (2, '欢迎使用ApiMix平台', 2, '欢迎使用ApiMix平台', 1, 18265100971000199, '2024-05-26 15:29:23', '2024-05-26 15:29:23');
INSERT INTO `notice` VALUES (4, '测试通知', 1, '测试测试测试测试', 1, 18265100971000199, '2024-05-26 15:32:43', '2024-05-26 15:32:43');
INSERT INTO `notice` VALUES (5, '测试测试', 2, '测试', 0, 18265100971000199, '2024-05-26 15:33:28', '2024-05-26 15:33:28');

-- ----------------------------
-- Table structure for package
-- ----------------------------
DROP TABLE IF EXISTS `package`;
CREATE TABLE `package`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '套餐的费用',
  `api_id` bigint(20) NULL DEFAULT NULL COMMENT 'APIID',
  `package_type` bigint(20) NULL DEFAULT NULL COMMENT '套餐类型',
  `quota` bigint(20) NULL DEFAULT NULL COMMENT '请求次数',
  `price` bigint(20) NULL DEFAULT NULL COMMENT '套餐的费用',
  `limit_quota` int(11) NULL DEFAULT 0 COMMENT '限购次数（0：无限制 >0：有次数限制）',
  `expired` bigint(20) NOT NULL DEFAULT 30 COMMENT '套餐过期天数',
  `is_delete` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '逻辑删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of package
-- ----------------------------
INSERT INTO `package` VALUES (1, 36894326755000135, 1, 100, 0, 1, 30, 0, '2024-06-17 16:45:38', '2024-06-18 15:14:30');
INSERT INTO `package` VALUES (2, 36894326755000135, 2, 1000, 1, 0, 30, 0, '2024-06-18 15:16:36', '2024-06-18 21:50:57');
INSERT INTO `package` VALUES (3, 36894326755000135, 2, 10000, 5, 0, 100, 0, '2024-06-18 16:05:37', '2024-06-18 21:30:40');
INSERT INTO `package` VALUES (4, 38443509231000159, 1, 1000, 10, 1, 30, 0, '2024-06-20 11:46:09', '2024-06-20 11:46:09');

-- ----------------------------
-- Table structure for package_type
-- ----------------------------
DROP TABLE IF EXISTS `package_type`;
CREATE TABLE `package_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '套餐类型名称',
  `is_delete` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '逻辑删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'SPU表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of package_type
-- ----------------------------
INSERT INTO `package_type` VALUES (1, '试用包', 0, '2024-06-17 14:13:19', '2024-06-18 20:41:02');
INSERT INTO `package_type` VALUES (2, '流量包', 0, '2024-06-17 14:13:51', '2024-06-17 14:13:51');

-- ----------------------------
-- Table structure for product_order
-- ----------------------------
DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(1) NOT NULL COMMENT '订单类型(0:商品订单,1:充值订单)',
  `user_id` bigint(20) NOT NULL COMMENT '购买用户ID',
  `package_id` bigint(20) NOT NULL COMMENT '套餐ID',
  `price` bigint(20) UNSIGNED NOT NULL COMMENT '付款金额',
  `pay_type` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '付款方式(0:积分 1:支付宝 2:微信)',
  `count` int(11) NOT NULL DEFAULT 1 COMMENT '套餐次数',
  `invoice` tinyint(1) NULL DEFAULT 0 COMMENT '是否开发票',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_order
-- ----------------------------
INSERT INTO `product_order` VALUES (1, 0, 18265100971000199, 1, 0, 0, 1, 0, '2024-06-24 17:39:29');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色编码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '角色状态(1启用 0禁用)',
  `type` tinyint(1) NOT NULL DEFAULT 2 COMMENT '角色类型(1内置 2自定义)',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `disabled` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用编辑',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '*', '上帝', 1, 1, '系统初始角色，拥有无上权力', 1, '2022-03-06 14:16:04');
INSERT INTO `role` VALUES (2, 'admin', '管理员', 1, 1, '拥有管理员权限', 0, '2022-03-06 14:16:34');
INSERT INTO `role` VALUES (3, 'developer', '开发者', 1, 2, '开发者，拥有上传接口管理自建接口的权限', 0, '2022-03-06 14:21:04');
INSERT INTO `role` VALUES (4, 'user', '普通用户', 1, 2, '普通用户', 0, '2023-11-02 22:09:17');
INSERT INTO `role` VALUES (11, 'test', '测试角色', 1, 2, '用于测试角色管理，权限没有一根', 0, '2023-12-16 15:13:26');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (139, 2, 4);
INSERT INTO `role_menu` VALUES (140, 2, 102);
INSERT INTO `role_menu` VALUES (141, 2, 224);
INSERT INTO `role_menu` VALUES (142, 2, 103);
INSERT INTO `role_menu` VALUES (143, 2, 225);
INSERT INTO `role_menu` VALUES (144, 2, 104);
INSERT INTO `role_menu` VALUES (145, 2, 202);
INSERT INTO `role_menu` VALUES (146, 2, 226);
INSERT INTO `role_menu` VALUES (147, 2, 227);
INSERT INTO `role_menu` VALUES (148, 2, 228);
INSERT INTO `role_menu` VALUES (149, 2, 229);
INSERT INTO `role_menu` VALUES (154, 3, 221);
INSERT INTO `role_menu` VALUES (155, 3, 222);
INSERT INTO `role_menu` VALUES (156, 3, 223);
INSERT INTO `role_menu` VALUES (157, 3, 230);
INSERT INTO `role_menu` VALUES (158, 3, 231);
INSERT INTO `role_menu` VALUES (192, 4, 207);
INSERT INTO `role_menu` VALUES (193, 4, 208);
INSERT INTO `role_menu` VALUES (194, 4, 2);
INSERT INTO `role_menu` VALUES (195, 4, 218);
INSERT INTO `role_menu` VALUES (196, 4, 219);
INSERT INTO `role_menu` VALUES (197, 4, 216);
INSERT INTO `role_menu` VALUES (198, 4, 217);
INSERT INTO `role_menu` VALUES (199, 4, 220);
INSERT INTO `role_menu` VALUES (200, 4, 243);
INSERT INTO `role_menu` VALUES (201, 4, 244);
INSERT INTO `role_menu` VALUES (202, 4, 245);
INSERT INTO `role_menu` VALUES (203, 4, 246);

-- ----------------------------
-- Table structure for social_user
-- ----------------------------
DROP TABLE IF EXISTS `social_user`;
CREATE TABLE `social_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方系统的唯一ID	',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GITHUB、GITEE、QQ，更多请参考',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户的授权令牌',
  `expire_in` int(11) NULL DEFAULT NULL COMMENT '第三方用户的授权令牌的有效期(部分平台可能没有)',
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '刷新令牌(部分平台可能没有)',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 open id(部分平台可能没有)',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 ID(部分平台可能没有)',
  `access_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个别平台的授权信息	(部分平台可能没有)',
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 union id(部分平台可能没有)',
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户授予的权限(部分平台可能没有)',
  `token_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个别平台的授权信息(部分平台可能没有)',
  `id_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'id token(部分平台可能没有)',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户的授权code(部分平台可能没有)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社会化用户表（social_user）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of social_user
-- ----------------------------

-- ----------------------------
-- Table structure for social_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `social_user_auth`;
CREATE TABLE `social_user_auth`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '系统用户id',
  `social_user_id` int(11) NOT NULL COMMENT '社会化用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of social_user_auth
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `type` tinyint(1) NULL DEFAULT 2 COMMENT '用户类型（1系统用户 2注册用户）',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `gender` tinyint(1) NULL DEFAULT 1 COMMENT '用户性别(1男 2女)',
  `avatar` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '用户状态(1启用 0禁用)',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户描述',
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码盐值',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'MD5密码',
  `disabled` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用编辑',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (18265100971000199, 'admin', 'hor', 1, '13217243010', 'horcn@qq.com', 1, 'https://q1.qlogo.cn/g?b=qq&nk=2786773385&s=100&t=1547904810', 1, '生活是好的，峰回路转，柳暗花明，前面总会有另一番不同的风景。', '7b8e837dc9da29525c7f9bf89b5bc179', 'd7667aade72c9e3539663d8f7c9c695a', 1, 0, '2023-10-30 22:39:20', '2024-08-28 16:12:49');
INSERT INTO `user` VALUES (18525250253000139, 'test', '测试用户', 2, NULL, '2786773385@qq.com', 2, 'https://p.qqan.com/up/2023-12/17023402112512922.jpg', 1, '测试员', '6f53435e7199103469bc397973f23673', '373e431aa091f23dd301cf6c7dd279dd', 0, 1, '2023-11-02 22:55:10', '2024-08-28 16:15:32');
INSERT INTO `user` VALUES (22333370819000179, 'cute', '焦糖布丁', 2, '13252011400', 'hor@itbk.cn', 2, 'https://img1.baidu.com/it/u=1817951587,3188870642&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 1, '今日无碍，明日无忧，秋冬冗长，你我皆好。', '98692af508686628c05991933dee0825', '8dd15c2e1e2228dba16b10eb1bc5ce8e', 0, 0, '2023-12-17 00:43:50', '2024-08-28 17:19:47');
INSERT INTO `user` VALUES (22497038442000130, 'lucyu', '卢朝钰', 2, '13117013678', '3038057092@qq.com', 1, 'https://q1.qlogo.cn/g?b=qq&nk=3038057092&s=640', 1, 'Where there is a will there is a way.', 'd6e76e5f2556526c5687ebcce1a89bff', '9443700715acd6bc3ee2d2d5548a9d55', 0, 0, '2023-12-18 22:11:38', '2024-05-24 23:17:26');
INSERT INTO `user` VALUES (36151022656000161, 'apimix', 'API测试员', 2, '13888888888', '', 1, '', 0, '', 'bf426bace90ad8e7a304282dbe1a8d67', '9a7603168a9e1120f1126f07f90bff40', 0, 0, '2024-05-24 22:58:02', '2024-05-26 15:37:41');

-- ----------------------------
-- Table structure for user_api_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_api_relation`;
CREATE TABLE `user_api_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `api_id` bigint(20) NOT NULL COMMENT '接口 id',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1-正常，0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户调用接口关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_api_relation
-- ----------------------------
INSERT INTO `user_api_relation` VALUES (1, 18265100971000199, 36894326755000135, 1, '2024-06-24 17:39:29', '2024-06-24 17:39:29');

-- ----------------------------
-- Table structure for user_package
-- ----------------------------
DROP TABLE IF EXISTS `user_package`;
CREATE TABLE `user_package`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `package_id` bigint(20) NOT NULL COMMENT '套餐ID',
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  `total_quota` bigint(20) NOT NULL COMMENT '此套餐总次数',
  `used_quota` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前套餐已使用次数',
  `count` int(11) NOT NULL DEFAULT 1 COMMENT '购买次套餐的数量',
  `expired_time` datetime NOT NULL COMMENT '过期时间戳',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '套餐状态（0：未使用 1：使用中，2：已用完，3：过期）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_package
-- ----------------------------
INSERT INTO `user_package` VALUES (1, 18265100971000199, 1, 36894326755000135, 100, 19, 1, '2024-09-01 09:39:29', 0, '2024-06-24 17:39:29', '2024-08-30 13:29:47');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 95 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和用户关联表' ROW_FORMAT = FIXED;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (90, 18265100971000199, 1);
INSERT INTO `user_role` VALUES (81, 18525250253000139, 11);
INSERT INTO `user_role` VALUES (80, 18525250253000139, 4);
INSERT INTO `user_role` VALUES (91, 36151022656000161, 4);
INSERT INTO `user_role` VALUES (89, 22497038442000130, 4);
INSERT INTO `user_role` VALUES (94, 22333370819000179, 3);
INSERT INTO `user_role` VALUES (88, 22497038442000130, 3);
INSERT INTO `user_role` VALUES (87, 22497038442000130, 2);
INSERT INTO `user_role` VALUES (93, 22333370819000179, 4);

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `token_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Token 值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '暂无备注' COMMENT '备注',
  `expired` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token_value`(`token_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_token
-- ----------------------------
INSERT INTO `user_token` VALUES (1, 18265100971000199, 'c8e5947504b846cc9293129e68acc72e', '新增测试Token 1', '2024-09-01 12:55:07', '2023-11-01 16:31:53', '2024-08-30 12:55:15');
INSERT INTO `user_token` VALUES (2, 18265100971000199, 's8e5947504b846cc9293129e68acc7ss', '新增测试Token 2', NULL, '2023-12-23 22:39:24', '2024-07-25 16:34:03');
INSERT INTO `user_token` VALUES (3, 18525250253000139, 'e62ab3f8aae4480fa585b5b0fae13682', '新增测试Token 1', NULL, '2023-11-21 21:36:09', '2024-07-25 16:34:03');
INSERT INTO `user_token` VALUES (4, 18265100971000199, '2cbd7ec6ce6248dba1a5f07e94e3771a', '新增测试Token 3', NULL, '2023-12-23 23:33:21', '2024-07-25 16:34:03');
INSERT INTO `user_token` VALUES (5, 22333370819000179, '38df8cde7c6f4754a7cd64156b09b699', '测试Token(●ˇ∀ˇ●)', NULL, '2023-12-24 00:01:03', '2024-07-25 16:34:03');
INSERT INTO `user_token` VALUES (6, 18265100971000199, '4f3219590a524264ad06f9137bffa1f1', '新增测试Token 4', NULL, '2023-12-30 13:33:50', '2024-07-25 16:34:03');

SET FOREIGN_KEY_CHECKS = 1;
