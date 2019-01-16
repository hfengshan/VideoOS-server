package com.videojj.videoservice;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * VideoOS - A Mini-App platform base on video player
 http://videojj.com/videoos/
 Copyright (C) 2019  Shanghai Ji Lian Network Technology Co., Ltd

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 * @Author @videopls.com
 * Created by  on 2018/10/8 下午3:27.
 * @Description:
 */
@EnableAutoConfiguration
@MapperScan({"com.videojj.videoservice.dao"})
@Configuration
@ComponentScan(basePackageClasses = VideoServiceApplication.class)//默认加载该类所在目录以及子目录的所有的类
public class VideoServiceApplication {
}
