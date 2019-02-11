INSERT INTO `tb_user` (`id`, `username`, `password`, `role_id`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(1,'admin','0192023a7bbd73250516f069df18b500',1,NULL,'admin','2018-08-01 11:38:37','admin','2018-12-26 10:53:04','N'),
	(2,'admin1','0192023a7bbd73250516f069df18b500',1,NULL,'admin','2018-08-09 18:51:11','admin1','2018-12-19 15:17:28','N');


INSERT INTO `tb_role` (`id`, `role_name`, `auths`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(1,'超级管理员','11,12,21,22,23,31,41,51,61,62','类型管理编辑,模版管理编辑,投放计划管理编辑,投放素材管理编辑,投放审核管理编辑,license管理编辑,数据信息管理编辑,账号管理编辑,角色管理编辑','admin','2018-08-13 19:48:09','admin1','2018-10-22 19:03:43','N');

	INSERT INTO `tb_template_zip_file` (`id`, `file_name`, `file_version`, `ossUrl`, `status`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(27,'1.0.0-template.zip','1.0.0','http://oss-cn-beijing.aliyuncs.com/videojj-os/dev/1.0.0-template.zip',3,NULL,'admin1','2018-08-21 14:13:45','admin1','2018-08-21 14:13:45','N'),
	(28,'1.0.1-template.zip','1.0.1','http://oss-cn-beijing.aliyuncs.com/videojj-os/dev/1.0.1-template.zip',3,NULL,'admin1','2018-08-21 14:14:55','admin1','2018-08-21 14:14:55','N');

	INSERT INTO `tb_template_file` (`id`, `template_id`, `file_name`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(64,118,'os_vote_hotspot.lua',NULL,'admin','2018-11-08 10:29:41','admin','2018-11-08 10:29:41','N'),
	(65,118,'os_vote_window.lua',NULL,'admin','2018-11-08 10:29:41','admin','2018-11-08 10:29:41','N');

	INSERT INTO `tb_template` (`id`, `template_name`, `interaction_id`, `template_file_name`, `template_file_source_name`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`, `interaction_type_name`)
VALUES
	(20,'呃呃呃呃呃',41,'template-dev-video-manage-platform2249.lua',NULL,NULL,'admin','2018-08-27 12:56:04','admin','2018-10-18 16:26:58','Y','云图类型11'),
	(23,'红包模版2',40,'template-dev-video-manage-platform5701.lua',NULL,NULL,'admin','2018-08-27 13:55:49','admin','2018-10-22 14:28:29','Y','红包类型112');

	INSERT INTO `tb_operation_log` (`id`, `operation_desc`, `operation_type`, `creator`, `gmt_created`, `modifier`, `gmt_modified`, `is_deleted`)
VALUES
	(338,'新增[测试预加载]投放计划',41,'admin','2018-12-10 17:23:47','admin','2018-12-10 17:23:47','N'),
	(339,'新增[中插]应用',1,'admin','2018-12-10 18:02:32','admin','2018-12-10 18:02:32','N'),
	(340,'新增[V++云图]应用',1,'admin','2018-12-10 18:02:56','admin','2018-12-10 18:02:56','N'),
	(341,'新增[V++云图的主题]主题',21,'admin','2018-12-10 18:35:33','admin','2018-12-10 18:35:33','N');

	INSERT INTO `tb_node` (`id`, `node_name`, `icon`, `path`, `parent_id`, `parent_path`, `is_leaf`, `desc`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(1,'互动管理','icon-star','',0,NULL,0,'',NULL,'admin','2018-08-13 19:50:48','admin','2018-09-14 10:15:23','N'),
	(2,'投放管理','icon-list','',0,NULL,0,'',NULL,'admin','2018-08-13 19:52:27','admin','2018-09-14 10:15:29','N'),
	(3,'license申请','icon-map','',0,NULL,0,'',NULL,'admin','2018-08-13 19:53:27','admin','2018-09-14 10:16:34','N');



	INSERT INTO `tb_mobile_data_detail` (`id`, `user_id`, `mobile_data_id`, `creative_id`, `data_key`, `data_value`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(50,'123',123,123,'vote','A',NULL,'system','2018-11-02 18:50:30','system','2018-11-02 18:51:15','N'),
	(51,'123',123,123,'vote','A',NULL,'system','2018-11-05 11:38:49','system','2018-11-05 11:39:58','N');


	INSERT INTO `tb_mobile_data` (`id`, `user_id`, `creative_id`, `business_info`, `adv_id`, `extra_info`, `creator`, `gmt_created`, `modifier`, `gmt_modified`, `is_deleted`)
VALUES
	(53,'122',99,'{\"vote\":\"A\"}',NULL,'','system','2018-11-05 19:14:25','system','2018-11-05 19:14:25','N'),
	(54,'12',99,'{\"vote\":\"C\"}',NULL,'','system','2018-11-05 19:18:22','system','2018-11-05 19:18:22','N');


	INSERT INTO `tb_launch_task_execute` (`id`, `execute_name`, `launch_plan_id`, `execute_time`, `execute_status`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(20,'24-14:47',24,'2018-09-03 14:48:00',0,NULL,'system','2018-09-03 14:48:01','system','2018-09-03 14:48:01','N'),
	(24,'25-15:09',25,'2018-09-03 15:09:00',1,NULL,'system','2018-09-03 15:08:59','system','2018-09-03 15:08:59','N'),
	(25,'26-15:09',26,'2018-09-03 15:09:00',1,NULL,'system','2018-09-03 15:08:59','system','2018-09-03 15:08:59','N'),
	(26,'27-15:09',27,'2018-09-03 15:09:00',1,NULL,'system','2018-09-03 15:08:59','system','2018-09-03 15:08:59','N');




	INSERT INTO `tb_launch_plan_operation` (`id`, `launch_plan_name`, `interaction_id`, `creative_id`, `creative_image_id`, `launch_time_type`, `launch_date_start`, `launch_date_end`, `launch_time`, `launch_len_time`, `status`, `extra_info`, `creator`, `gmt_created`, `modifier`, `gmt_modified`, `is_deleted`, `hotspot_track_link`, `info_track_link`, `info_track_link_title`)
VALUES
	(1,'launchPlan1',16,5,NULL,2,'2018-01-27 23:27:53','2019-01-01 15:03:53','10:40','21',3,NULL,'admin1','2018-08-21 18:30:33','admin1','2018-09-13 14:52:24','N',NULL,NULL,NULL),
	(2,'wp-launchPlan001',40,18,17,1,'1970-01-19 02:20:47','1970-01-19 11:06:23','20秒','20',3,NULL,'admin1','2018-08-30 11:06:44','admin','2018-08-30 18:35:26','N',NULL,NULL,NULL);



	INSERT INTO `tb_launch_plan` (`id`, `launch_plan_name`, `interaction_id`, `creative_id`, `creative_image_id`, `launch_video_id`, `launch_time_type`, `launch_date_start`, `launch_date_end`, `launch_time`, `launch_len_time`, `status`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`, `operation_id`, `hotspot_track_link`, `info_track_link`)
VALUES
	(2,'launchPlan1',16,5,NULL,'eeccxxx2',2,'2018-01-27 23:27:53','2019-01-01 15:03:53','10:40','21',3,NULL,'admin1','2018-08-21 18:30:33','admin1','2018-12-05 17:17:59','N',1,NULL,NULL),
	(3,'wp-launchPlan001',40,18,17,'wp-test-001',1,'1970-01-19 02:20:47','1970-01-19 11:06:23','20秒','20',3,NULL,'admin1','2018-08-30 11:06:44','admin','2018-12-05 17:17:59','N',2,NULL,NULL),
	(4,'红包投放1',40,17,18,'333',20,'2018-08-30 00:00:00','2018-09-01 00:00:00','8:35',NULL,3,NULL,'admin1','2018-08-30 14:59:34','admin','2018-12-05 17:17:59','N',3,NULL,NULL),
	(8,'ddfdfdf',40,17,22,'3333',30,'2018-08-30 00:00:00','2018-09-01 00:00:00','9:10',NULL,3,NULL,'admin','2018-08-30 15:20:21','admin','2018-12-05 17:17:59','N',4,NULL,NULL);



	INSERT INTO `tb_interaction` (`id`, `interaction_type_name`, `file_name`, `content`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`, `is_system`, `img_url`)
VALUES
	(40,'红包类型1','os_wedge.json','{\n  \"title\": \"\",\n  \"type\": \"object\",\n  \"required\": [\"creativeName\"],\n  \"properties\": {\n    \"creativeName\": {\n      \"title\": \"素材名称\",  \n      \"type\": \"string\"  \n    },\n     \"interactionTypeId\": {\n      \"title\": \"素材类型\",  \n      \"type\": \"integer\",\n      \"default\": \"请选择\",\n      \"enum\": [],\n      \"enumNames\": []  \n    },\n    \"interactionTemplateId\": {\n      \"title\": \"素材模板\",\n      \"type\": \"integer\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"width\": {\n      \"type\": \"number\",\n      \"title\": \"width\",\n      \"default\": \"\"  \n    },\n    \"ratio\": {\n      \"type\": \"number\",\n      \"title\": \"ratio\",\n      \"default\": \"\"  \n    },\n    \"positionX\": {\n      \"type\": \"number\",\n      \"title\": \"positionX\",\n      \"default\": \"\"  \n    },\n    \"positionY\": {\n      \"type\": \"number\",\n      \"title\": \"positionY\",\n      \"default\": \"\"  \n    },\n    \"needShowOnPortrait\": {\n      \"type\": \"boolean\",\n      \"title\": \"needShowOnPortrait\",\n      \"default\": \"\"  \n    },\n    \"isShowClose\": {\n      \"type\": \"boolean\",\n      \"title\": \"isShowClose\",\n      \"default\": \"\"  \n    },\n    \"isShowAds\": {\n      \"type\": \"boolean\",\n      \"title\": \"isShowAds\",\n      \"default\": \"\"  \n    },\n    \n    \"isShowCountdown\": {\n      \"type\": \"boolean\",\n      \"title\": \"isShowCountdown\",\n      \"default\": \"\"  \n    },\n    \n    \"closeAfter\": {\n      \"type\": \"integer\",\n      \"title\": \"closeAfter\",\n      \"default\": 0  \n    },\n    \"linkUrl\": {\n      \"type\": \"string\",\n      \"title\": \"linkUrl\",\n      \"default\": \"\"  \n    },\n    \"videoDuration\": {\n      \"type\": \"integer\",\n      \"title\": \"videoDuration\",\n      \"default\": 0  \n    },\n    \"videoUrl\": {\n      \"type\": \"string\",\n      \"title\": \"videoUrl\",\n      \"default\": \"\"  \n    },\n    \"repeatTimes\": {\n      \"type\": \"integer\",\n      \"title\": \"repeatTimes\",\n      \"default\": 0  \n    }\n  }  \n}',NULL,'admin','2018-08-27 11:48:31','admin','2018-09-19 12:50:50','Y','N',NULL),
	(41,'云图类型1','yuntu.json','{\n  \"title\": \"\",\n  \"type\": \"object\",\n  \"required\": [\"popular_point_title\"],\n  \"properties\": {\n    \"creativeName\": {\n      \"title\": \"素材名称\",  \n      \"type\": \"string\"  \n    },\n    \"interactionTypeId\": {\n      \"title\": \"素材类型\",  \n      \"type\": \"string\",\n      \"default\": \"请选择\",\n      \"enum\": [],\n      \"enumNames\": []  \n    },\n    \"interactionTemplateId\": {\n      \"title\": \"素材模板\",\n      \"type\": \"string\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"popular_point_title\": {\n      \"type\": \"string\",\n      \"title\": \"热点标题\",\n      \"default\": \"\"  \n    },\n    \"avatar\": {\n      \"type\": \"string\",\n      \"format\": \"data-url\",\n      \"title\": \"图片\"\n    },\n    \"show_ad_sign\": {\n      \"type\": \"boolean\",\n      \"title\": \"广告标识是否可见\"  \n    },\n    \"show_close_btn\": {\n      \"type\": \"boolean\",\n      \"title\": \"关闭按钮是否可见\"  \n    },\n    \"monitorLinks\": {\n      \"type\": \"array\",\n      \"title\": \"点击监控链接\",\n      \"minItems\": 1,\n      \"items\": {\n        \"type\": \"string\"  \n      }  \n    },\n    \"exposureLinks\": {\n      \"type\": \"array\",\n      \"title\": \"曝光监控链接\",\n      \"minItems\": 1,\n      \"items\": {\n        \"type\": \"string\"  \n      }  \n    },\n    \"target_link\": {\n      \"type\": \"string\",\n      \"title\": \"跳转外链链接\",\n      \"default\": \"\"  \n    }\n  }  \n}',NULL,'admin','2018-08-28 10:39:47','admin','2018-09-19 12:50:53','Y','N',NULL),
	(42,'类型测试','yuntu.json','{\n  \"title\": \"\",\n  \"type\": \"object\",\n  \"required\": [\"creativeName\"],\n  \"properties\": {\n    \"creativeName\": {\n      \"title\": \"素材名称\",\n      \"type\": \"string\"\n    },\n    \"interactionTypeId\": {\n      \"title\": \"素材类型\",\n      \"type\": \"integer\",\n      \"default\": \"请选择\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"interactionTemplateId\": {\n      \"title\": \"素材模板\",\n      \"type\": \"integer\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"imageUrl\": {\n      \"type\": \"string\",\n      \"title\": \"图片\"\n    },\n    \"isShowAds\": {\n      \"type\": \"boolean\",\n      \"title\": \"广告标识是否可见\",\n      \"default\": true\n    },\n    \"isShowClose\": {\n      \"type\": \"boolean\",\n      \"title\": \"关闭按钮是否可见\",\n      \"default\": true\n    },\n    \"needShowOnPortrait\": {\n      \"type\": \"boolean\",\n      \"title\": \"竖屏是否可见\",\n      \"default\": false\n    },\n    \"positionX\": {\n      \"type\": \"number\",\n      \"title\": \"横屏场景，广告图片距屏幕左上角的距离占屏幕宽度比(positionX)\",\n      \"default\": 0.5\n    },\n    \"positionY\": {\n      \"type\": \"number\",\n      \"title\": \"横屏场景，广告图片距屏幕左上角的距离占屏幕高度比(positionY)\",\n      \"default\": 0.5\n    },\n    \"monitorLinks\": {\n      \"type\": \"array\",\n      \"title\": \"点击监控链接\",\n      \"items\": {\n        \"type\": \"string\",\n        \"format\": \"uri\"\n      }\n    },\n    \"exposureLinks\": {\n      \"type\": \"array\",\n      \"title\": \"曝光监控链接\",\n      \"items\": {\n        \"type\": \"string\",\n        \"format\": \"uri\"\n      }\n    },\n    \"linkUrl\": {\n      \"type\": \"string\",\n      \"format\": \"uri\",\n      \"title\": \"跳转外链链接\"\n    }\n  }\n}',NULL,'admin','2018-08-28 16:06:13','admin','2018-10-08 16:40:35','Y','N',NULL),
	(48,'yutuleix','yuntuleixing1.json','{\n  \"title\": \"\",\n  \"type\": \"object\",\n  \"required\": [\"\"],\n  \"properties\": {\n    \"creativeName\": {\n      \"title\": \"素材名称\",  \n      \"type\": \"string\"  \n    },\n     \"interactionTypeId\": {\n      \"title\": \"素材类型\",  \n      \"type\": \"Integer\",\n      \"default\": \"请选择\",\n      \"enum\": [],\n      \"enumNames\": []  \n    },\n    \"interactionTemplateId\": {\n      \"title\": \"素材模板\",\n      \"type\": \"Integer\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"height_title\": {\n      \"type\": \"string\",\n      \"title\": \"height\",\n      \"default\": \"\"  \n    },\n    \"positionX_title\": {\n      \"type\": \"string\",\n      \"title\": \"positionX\",\n      \"default\": \"\"  \n    },\n    \"positionY_title\": {\n      \"type\": \"string\",\n      \"title\": \"positionY\",\n      \"default\": \"\"  \n    },\n    \"needShowOnPortrait_title\": {\n      \"type\": \"string\",\n      \"title\": \"needShowOnPortrait\",\n      \"default\": \"\"  \n    },\n    \"count_title\": {\n      \"type\": \"string\",\n      \"title\": \"count\",\n      \"default\": \"\"  \n    },\n    \"tagImage_title\": {\n      \"type\": \"string\",\n      \"title\": \"tagImage\",\n      \"default\": \"\"  \n    },\n    \"windowAd_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowAd\",\n      \"default\": \"\"  \n    },\n    \"windowIcon_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowIcon\",\n      \"default\": \"\"  \n    },\n    \"windowDesc_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowDesc\",\n      \"default\": \"\"  \n    },\n    \"windowTitle_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowTitle\",\n      \"default\": \"\"  \n    },\n    \"windowButtonMore_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowButtonMore\",\n      \"default\": \"\"  \n    },\n    \"themeColor_title\": {\n      \"type\": \"string\",\n      \"title\": \"themeColor\",\n      \"default\": \"\"  \n    }\n  }  \n}',NULL,'admin','2018-09-04 13:09:59','admin','2018-09-13 14:19:51','Y','N',NULL),
	(49,'云图云图1','yuntuleixing1.json','{\n  \"title\": \"\",\n  \"type\": \"object\",\n  \"required\": [\"\"],\n  \"properties\": {\n    \"creativeName\": {\n      \"title\": \"素材名称\",  \n      \"type\": \"string\"  \n    },\n     \"interactionTypeId\": {\n      \"title\": \"素材类型\",  \n      \"type\": \"integer\",\n      \"default\": \"请选择\",\n      \"enum\": [],\n      \"enumNames\": []  \n    },\n    \"interactionTemplateId\": {\n      \"title\": \"素材模板\",\n      \"type\": \"integer\",\n      \"enum\": [],\n      \"enumNames\": []\n    },\n    \"height_title\": {\n      \"type\": \"string\",\n      \"title\": \"height\",\n      \"default\": \"\"  \n    },\n    \"positionX_title\": {\n      \"type\": \"string\",\n      \"title\": \"positionX\",\n      \"default\": \"\"  \n    },\n    \"positionY_title\": {\n      \"type\": \"string\",\n      \"title\": \"positionY\",\n      \"default\": \"\"  \n    },\n    \"needShowOnPortrait_title\": {\n      \"type\": \"string\",\n      \"title\": \"needShowOnPortrait\",\n      \"default\": \"\"  \n    },\n    \"count_title\": {\n      \"type\": \"string\",\n      \"title\": \"count\",\n      \"default\": \"\"  \n    },\n    \"tagImage_title\": {\n      \"type\": \"string\",\n      \"title\": \"tagImage\",\n      \"default\": \"\"  \n    },\n    \"windowAd_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowAd\",\n      \"default\": \"\"  \n    },\n    \"windowIcon_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowIcon\",\n      \"default\": \"\"  \n    },\n    \"windowDesc_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowDesc\",\n      \"default\": \"\"  \n    },\n    \"windowTitle_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowTitle\",\n      \"default\": \"\"  \n    },\n    \"windowButtonMore_title\": {\n      \"type\": \"string\",\n      \"title\": \"windowButtonMore\",\n      \"default\": \"\"  \n    },\n    \"themeColor_title\": {\n      \"type\": \"string\",\n      \"title\": \"themeColor\",\n      \"default\": \"\"  \n    }\n  }  \n}',NULL,'admin','2018-09-04 13:12:09','admin','2018-10-22 14:36:30','Y','N',NULL);




	INSERT INTO `tb_creative_image` (`id`, `source_id`, `material_name`, `interaction_id`, `interaction_name`, `template_id`, `template_name`, `hot_spot_num`, `material_content`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(17,18,'wp-sucai002',40,'类型的名称镜像',16,'模版的名称镜像',1,'sucaineirong-00001',NULL,'admin1','2018-08-30 10:51:18','admin1','2018-08-30 10:51:18','N'),
	(18,17,'wp-sucai001',40,'类型的名称镜像',16,'模版的名称镜像',1,'sucaineirong-00001',NULL,'admin1','2018-08-30 10:47:26','admin1','2018-08-30 15:03:06','N'),
	(19,17,'wp-sucai001',40,NULL,16,NULL,1,'sucaineirong-00001',NULL,'admin1','2018-08-30 10:47:26','admin1','2018-08-30 10:47:26','N'),
	(20,17,'wp-sucai001',40,NULL,16,NULL,1,'sucaineirong-00001',NULL,'admin1','2018-08-30 10:47:26','admin1','2018-08-30 10:47:26','N'),
	(21,17,'wp-sucai001',40,NULL,16,NULL,1,'sucaineirong-00001',NULL,'admin1','2018-08-30 10:47:26','admin1','2018-08-30 10:47:26','N');



	INSERT INTO `tb_creative_file` (`id`, `creative_id`, `file_name`, `file_url`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(4,143,'55ac2334-d6dc-4e4a-85ca-1ee10d5d0b49.xml','http://oss-cn-beijing.aliyuncs.com/videojj-os/dev/55ac2334-d6dc-4e4a-85ca-1ee10d5d0b49.xml',NULL,'admin1','2018-08-21 15:15:50','admin1','2019-01-16 19:07:24','Y'),
	(5,64,'43dd14ec-3f9f-4f5e-a508-b260c81dbc58.sql','http://oss-cn-beijing.aliyuncs.com/videojj-os/dev/43dd14ec-3f9f-4f5e-a508-b260c81dbc58.sql',NULL,'admin1','2018-08-21 15:23:42','admin1','2018-11-01 18:50:18','Y'),
	(6,64,'dcc65e51-4b33-4dcc-985a-c828b9e443dd.txt','http://oss-cn-beijing.aliyuncs.com/videojj-os/dev/dcc65e51-4b33-4dcc-985a-c828b9e443dd.txt',NULL,'admin1','2018-08-21 16:21:29','admin','2019-02-01 15:18:21','Y');



	INSERT INTO `tb_creative` (`id`, `material_name`, `interaction_id`, `interaction_name`, `template_id`, `template_name`, `material_content`, `status`, `hot_spot_num`, `extra_info`, `creator`, `gmt_created`, `MODIFIER`, `gmt_modified`, `is_deleted`)
VALUES
	(1,'sucai001',1,NULL,16,NULL,'sucaineirong',0,1,NULL,'admin1','2018-08-21 16:38:23','admin','2018-10-12 15:46:30','Y'),
	(3,'sucai002-1535367840969',1,NULL,16,NULL,'sucaineirong',0,1,NULL,'admin1','2018-08-21 16:41:06','admin','2018-08-27 19:04:00','Y'),
	(5,'ceshiupdate-1535368006826',1,NULL,16,NULL,'ceshiupdatecontent',0,1,NULL,'admin1','2018-08-21 16:44:04','admin','2018-08-27 19:06:46','Y');



INSERT INTO `tb_launch_plan_operation` (`id`, `launch_plan_name`, `interaction_id`, `creative_id`, `creative_image_id`, `launch_time_type`, `launch_date_start`, `launch_date_end`, `launch_time`, `launch_len_time`, `status`, `extra_info`, `creator`, `gmt_created`, `modifier`, `gmt_modified`, `is_deleted`, `hotspot_track_link`, `info_track_link`, `info_track_link_title`)
VALUES
	(1,'launchPlan1',16,5,NULL,2,'2018-01-27 23:27:53','2019-01-01 15:03:53','10:40','21',3,NULL,'admin1','2018-08-21 18:30:33','admin1','2018-09-13 14:52:24','N',NULL,NULL,NULL),
	(2,'wp-launchPlan001',40,18,17,1,'1970-01-19 02:20:47','1970-01-19 11:06:23','20秒','20',3,NULL,'admin1','2018-08-30 11:06:44','admin','2018-08-30 18:35:26','N',NULL,NULL,NULL),
	(3,'红包投放1',40,17,18,20,'2018-08-30 00:00:00','2018-09-01 00:00:00','8:35',NULL,3,NULL,'admin1','2018-08-30 14:59:34','admin','2018-08-30 18:44:06','N',NULL,NULL,NULL),
	(4,'ddfdfdf',40,17,22,30,'2018-08-30 00:00:00','2018-09-01 00:00:00','9:10',NULL,3,NULL,'admin','2018-08-30 15:20:21','admin','2018-10-22 13:08:20','N',NULL,NULL,NULL);