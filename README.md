
> 本项目是xfunction所有应用的后端API，不同的应用对应module。

## 技术要点
* API基本springboot框架，开源并没有提供数据库脚本，请参考model中的数据表对象来创建。
* 开源中配置文件仅提供主文件application.properties，其中的“XXX"的配置，请新建多环境配置文件，配置自己的参数。
* 目录说明
  * net.xfunction.java.api.* 为启动类
  * net.xfunction.java.api.config.* 配置类
  * net.xfunction.java.api.config.modules.* 各应用功能类
* 主要依赖说明如下
  
```
      <dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		
		<!-- 数据库相关依赖包 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.2</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>1.1.10</version>
		</dependency>
		<!-- pagehelper 非官方支持 starter https://github.com/pagehelper/pagehelper-spring-boot -->
		<dependency>
			<groupId>com.github.pagehelper</groupId>
			<artifactId>pagehelper-spring-boot-starter</artifactId>
			<version>1.2.10</version>
		</dependency>
		<!-- 通用mapper 非官方支持 starter https://github.com/abel533/Mapper/wiki/1.3-spring-boot -->
		<dependency>
			<groupId>tk.mybatis</groupId>
			<artifactId>mapper-spring-boot-starter</artifactId>
			<version>2.1.4</version>
		</dependency>


		<!-- shiro https://shiro.apache.org/spring-boot.html 借用其密码机制 -->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-spring</artifactId>
			<version>${shiro.version}</version>
		</dependency>


		<!-- 缓存相关依赖类 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-cache</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-annotations</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-pool2</artifactId>
		</dependency>
		<!-- cache -->

		<!-- 接口调用 https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
		</dependency>

		<!-- 阿里云核心包 -->
		<dependency>
			<groupId>com.aliyun</groupId>
			<artifactId>aliyun-java-sdk-core</artifactId>
			<version>4.1.0</version>
		</dependency>

		<!-- JSON Web Token -->
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.4.1</version>
		</dependency>


		<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>fastjson</artifactId>
			<version>1.2.56</version>
		</dependency>

		<!-- excel导入导出包 -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>easyexcel</artifactId>
			<version>2.0.3</version>
		</dependency>

		<!--配置跨域 -->
		<dependency>
			<groupId>com.thetransactioncompany</groupId>
			<artifactId>cors-filter</artifactId>
			<version>2.6</version>
		</dependency>

		<!-- 邮件发送相关依赖 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
		
		<!-- websocket -->
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        
        <!--RocketMq  -->
		<dependency>
			<groupId>com.aliyun.openservices</groupId>
			<artifactId>ons-client</artifactId>
			<version>1.8.0.Final</version>
		</dependency>

		<!-- 阿里语音转录 -->
        <dependency>
		    <groupId>com.alibaba.nls</groupId>
		    <artifactId>nls-sdk-transcriber</artifactId>
		    <version>2.1.6</version>
		</dependency>
  ```

## 应用module

* modules/meeting/* 视频会议相关，前端对应[（xfunction-meeting）](https://github.com/KelvinDong/xfunction-meeting)项目
* modules/shortlink/* 短链接相关，前端对应[ （xfunction-www）](https://github.com/KelvinDong/xfunction-www)项目
* modules/user/*
* modules/activity/*
* modules/questionnire/*  以上三涉及用户信息、活动、问卷，前端对应[ （xfunction-activity）](https://github.com/KelvinDong/xfunction-activity)项目




