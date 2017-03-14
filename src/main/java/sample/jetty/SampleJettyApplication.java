/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.jetty;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import sample.jetty.embedmysql.EmbedMySqlServer;

@SpringBootApplication
public class SampleJettyApplication {
	public static final ConcurrentHashMap<String,EmbedMySqlServer> MAIN_THREAD_LOCAL = new ConcurrentHashMap<String,EmbedMySqlServer>();
	
	public static void main(String[] args) throws Exception {
		InputStream inputstream = null;
		try {
			Properties pro = new Properties();
			//根据机器配置，设置不同的参数
			URL url = org.springframework.util.ResourceUtils.getURL("classpath:MySql_general.properties");
			inputstream = url.openStream();
			pro.load(inputstream);
			//new EmbedMySqlServer(pro).startup();
			
			String dbPath = (new File("")).getAbsolutePath() + "/db/";
			dbPath = dbPath.replaceAll("\\\\", "/");
			//可以把数据库放到其他磁盘
			EmbedMySqlServer mysqldbServer = new EmbedMySqlServer(pro, dbPath /*"C:\\gfworklog\\db\\"*/);
			
			SampleJettyApplication.MAIN_THREAD_LOCAL.put("embedMysqlServer",mysqldbServer);

			mysqldbServer.startup();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{inputstream.close();}catch(Exception ex){}
		}
		System.out.println(Thread.currentThread().getName()+" ========================= "+1+" =========================");
		ConfigurableApplicationContext cac = SpringApplication.run(SampleJettyApplication.class, args);
		
		
		
	}

}
