
DROP TABLE IF EXISTS `tb_user`;
-- 用户表
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '登录密码',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';


DROP TABLE IF EXISTS `tb_role`;
-- 角色表
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(128) NOT NULL COMMENT '角色名称',
  `auths` varchar(128) DEFAULT NULL COMMENT '节点的id列表,逗号分割',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

DROP TABLE IF EXISTS `tb_operation_log`;
-- 操作日志表
CREATE TABLE `tb_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_desc` varchar(1024) NOT NULL COMMENT '修改内容',
  `operation_type` int(11) NOT NULL COMMENT '操作类型',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';

DROP TABLE IF EXISTS `tb_creative`;
-- 素材信息表
CREATE TABLE `tb_creative` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `material_name` varchar(128) NOT NULL COMMENT '素材名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `interaction_name` varchar(128) DEFAULT NULL COMMENT '类型名称',
  `template_id` int(11) NOT NULL COMMENT '模版id',
  `template_name` varchar(128) DEFAULT NULL COMMENT '模版名称',
  `material_content` text NOT NULL COMMENT '素材内容',
  `status` tinyint(1) NOT NULL COMMENT '0:未使用 1:使用中',
  `hot_spot_num` int(11) DEFAULT '1' COMMENT '热点数',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='素材信息';



DROP TABLE IF EXISTS `tb_creative_file`;
-- 素材文件信息表
CREATE TABLE `tb_creative_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creative_id` int(11) DEFAULT NULL COMMENT '素材id',
  `file_name` varchar(128) NOT NULL COMMENT '素材文件名称',
  `file_url` varchar(128) NOT NULL COMMENT '素材文件url',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_name_UNIQUE` (`file_name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='素材文件信息';


DROP TABLE IF EXISTS `tb_creative_image`;
-- 素材信息镜像
CREATE TABLE `tb_creative_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` int(11) NOT NULL COMMENT '关联原素材id',
  `material_name` varchar(128) NOT NULL COMMENT '素材名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `interaction_name` varchar(128) DEFAULT NULL COMMENT '类型名称',
  `template_id` int(11) NOT NULL COMMENT '模版id',
  `template_name` varchar(128) DEFAULT NULL COMMENT '模版名称',
  `hot_spot_num` int(11) DEFAULT '1' COMMENT '热点数',
  `material_content` text NOT NULL COMMENT '素材内容',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='素材信息镜像';



DROP TABLE IF EXISTS `tb_interaction`;
-- 类型文件信息表
CREATE TABLE `tb_interaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `interaction_type_name` varchar(128) NOT NULL COMMENT '类型名称',
  `file_name` varchar(128) NOT NULL COMMENT '文件名称',
  `content` text NOT NULL COMMENT '文件内容',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  `is_system` char(1) NOT NULL DEFAULT 'N' COMMENT '是否系统应用',
  `img_url` varchar(128) DEFAULT NULL COMMENT '图片url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类型文件信息';



DROP TABLE IF EXISTS `tb_launch_plan`;
-- 投放计划表
CREATE TABLE `tb_launch_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `launch_plan_name` varchar(128) NOT NULL COMMENT '投放计划名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `creative_id` int(11) NOT NULL COMMENT '素材id',
  `creative_image_id` int(11) DEFAULT NULL COMMENT '镜像的id',
  `launch_video_id` varchar(512) NOT NULL COMMENT '投放视频id',
  `launch_time_type` tinyint(2) NOT NULL COMMENT '0:视频时间 1:即时 2:北京时间',
  `launch_date_start` datetime DEFAULT NULL COMMENT '投放开始日期',
  `launch_date_end` datetime DEFAULT NULL COMMENT '投放结束日期',
  `launch_time` varchar(128) DEFAULT NULL COMMENT '投放时间',
  `launch_len_time` varchar(128) DEFAULT NULL COMMENT '投放时长',
  `status` tinyint(2) DEFAULT NULL COMMENT '审核状态 0:审核未通过 1:审核通过',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  `operation_id` int(11) NOT NULL COMMENT '投放计划操作表外键',
  `hotspot_track_link` varchar(4096) DEFAULT NULL COMMENT '热点监控链接信息',
  `info_track_link` varchar(512) DEFAULT NULL COMMENT '信息层监控链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投放计划';


DROP TABLE IF EXISTS `tb_launch_plan_detail`;
-- 投放计划明细表
CREATE TABLE `tb_launch_plan_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `launch_plan_id` int(11) NOT NULL COMMENT '投放计划id',
  `launch_plan_name` int(11) NOT NULL COMMENT '投放计划名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `launch_time_type` tinyint(2) NOT NULL COMMENT '0:视频时间 1:即时 2:北京时间',
  `launch_time_start` datetime DEFAULT NULL COMMENT '投放开始时间',
  `launch_time_end` datetime DEFAULT NULL COMMENT '投放结束时间',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投放计划明细';



DROP TABLE IF EXISTS `tb_launch_plan_operation`;
-- 投放计划操作表
CREATE TABLE `tb_launch_plan_operation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `launch_plan_name` varchar(128) NOT NULL COMMENT '投放计划名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `creative_id` int(11) NOT NULL COMMENT '素材id',
  `creative_image_id` int(11) DEFAULT NULL COMMENT '镜像的id',
  `launch_time_type` tinyint(2) NOT NULL COMMENT '0:视频时间 1:即时 2:北京时间',
  `launch_date_start` datetime DEFAULT NULL COMMENT '投放开始日期',
  `launch_date_end` datetime DEFAULT NULL COMMENT '投放结束日期',
  `launch_time` varchar(128) DEFAULT NULL COMMENT '投放时间',
  `launch_len_time` varchar(128) DEFAULT NULL COMMENT '投放时长',
  `status` tinyint(2) DEFAULT NULL COMMENT '审核状态 0:审核未通过 1:审核通过',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  `hotspot_track_link` varchar(4096) DEFAULT NULL COMMENT '热点监控链接信息',
  `info_track_link` varchar(512) DEFAULT NULL COMMENT '信息层监控链接',
  `info_track_link_title` varchar(128) DEFAULT NULL COMMENT '信息层监控链接标题',
  PRIMARY KEY (`id`),
  UNIQUE KEY `launch_plan_name_UNIQUE` (`launch_plan_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投放计划操作表';



DROP TABLE IF EXISTS `tb_launch_task_execute`;
-- 直播投放计划北京时间执行task表
CREATE TABLE `tb_launch_task_execute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `execute_name` varchar(128) NOT NULL COMMENT '执行名称',
  `launch_plan_id` int(11) DEFAULT NULL COMMENT '投放计划id',
  `execute_time` datetime DEFAULT NULL COMMENT '计划执行时间',
  `execute_status` tinyint(2) DEFAULT '0' COMMENT '0:即将执行 1:执行成功 2:执行失败',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `execute_name_UNIQUE` (`execute_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='直播投放计划北京时间执行task';



DROP TABLE IF EXISTS `tb_mobile_data`;
-- 移动端数据表
CREATE TABLE `tb_mobile_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) NOT NULL COMMENT '用户名',
  `creative_id` int(11) NOT NULL COMMENT '素材id',
  `business_info` varchar(1024) NOT NULL COMMENT '业务信息',
  `adv_id` varchar(128) DEFAULT NULL COMMENT '广告id',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL DEFAULT 'system' COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) NOT NULL DEFAULT 'system' COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端数据表';



DROP TABLE IF EXISTS `tb_mobile_data_detail`;
-- 移动端详细信息
CREATE TABLE `tb_mobile_data_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) NOT NULL COMMENT '第三方平台用户id',
  `mobile_data_id` int(11) NOT NULL COMMENT '关联移动端数据id',
  `creative_id` int(11) NOT NULL COMMENT '素材id',
  `data_key` varchar(128) DEFAULT NULL COMMENT '数据的key',
  `data_value` varchar(128) DEFAULT NULL COMMENT '数据的value',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端详细信息';


DROP TABLE IF EXISTS `tb_node`;
-- 节点信息
CREATE TABLE `tb_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_name` varchar(128) NOT NULL COMMENT '节点名称',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `path` varchar(128) NOT NULL COMMENT '所在路径',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `parent_path` varchar(64) DEFAULT NULL COMMENT '向上追溯路径',
  `is_leaf` tinyint(2) DEFAULT '0' COMMENT '是否是叶子节点，0非叶子，1叶子节点',
  `desc` varchar(32) DEFAULT NULL COMMENT '节点描述',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点信息';








DROP TABLE IF EXISTS `tb_template`;

CREATE TABLE `tb_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(128) NOT NULL COMMENT '模版名称',
  `interaction_id` int(11) DEFAULT NULL COMMENT '类型id',
  `template_file_name` varchar(128) NOT NULL COMMENT '模版文件名称',
  `template_file_source_name` varchar(256) DEFAULT NULL COMMENT '模版文件原名称',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  `interaction_type_name` varchar(128) DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模版信息';





DROP TABLE IF EXISTS `tb_template_file`;

CREATE TABLE `tb_template_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL COMMENT '模版id',
  `file_name` varchar(256) NOT NULL COMMENT '文件名',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模版文件表';


DROP TABLE IF EXISTS `tb_template_zip_file`;

CREATE TABLE `tb_template_zip_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(128) NOT NULL COMMENT '压缩包名称',
  `file_version` varchar(128) NOT NULL COMMENT '版本号',
  `ossUrl` varchar(128) NOT NULL COMMENT '模版压缩文件url',
  `status` tinyint(2) NOT NULL COMMENT '0:未打包 1:已打包 2:已上传 3:打包失败 4:上传失败',
  `extra_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFIER` varchar(32) NOT NULL COMMENT '修改人',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_version_UNIQUE` (`file_version`),
  UNIQUE KEY `file_name_UNIQUE` (`file_name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模版文件信息';























