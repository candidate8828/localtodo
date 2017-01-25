package sample.jetty.embedmysql;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.mysql.management.MysqldResource;

/**
 * 
 * @author 李岩飞
 * @email eliyanfei@126.com	
 * 2016年11月2日 下午1:44:55
 *
 */
public final class EmbedMySqlServer {
	private MysqldResource mysqlInstance;
	//配置信息
	public final Properties props;
	//端口信息
	private String port;
	/**
	 * 考虑到数据库的性能问题,允许将数据库放在其它磁盘
	 */
	private String embedMySqlHome;

	public EmbedMySqlServer(final Properties props) {
		this.props = props;
	}

	public EmbedMySqlServer(final Properties props, String embedMySqlHome) {
		this.embedMySqlHome = embedMySqlHome;
		this.props = props;
	}

	public final String getEmbedMySqlHome() {
		return null == embedMySqlHome ? getPlatformBaseDir() : embedMySqlHome;
	}

	/**
	* 获得当前应用主目录
	* @return 当前应用启动程序所在目录.
	*/
	public static String getPlatformBaseDir() {
		return System.getProperty("user.dir");
	}

	public static boolean isBlank(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public void startup() {
		final File baseDir = new File(getEmbedMySqlHome(), "mysql-em");
		mysqlInstance = new MysqldResource(baseDir);
		port = props.getProperty("port");
		if (isBlank(port))
			props.put("port", port = String.valueOf((int) (Math.random() * 40000)));
		final Set<Object> keys = props.keySet();
		final Map<String, String> options = new HashMap<String, String>(keys.size());
		for (final Object key : keys) {
			final String val = props.getProperty(key.toString());
			if ("".equals(val))
				options.put(key.toString(), null);
			else
				options.put(key.toString(), val.replace("{$contextPath}", getPlatformBaseDir()));
		}
		if (!mysqlInstance.isRunning())
			mysqlInstance.start("Em_MySQL", options, false, keys.contains("defaults-file"));
	}

	public String getPort() {
		return port;
	}

	/**
	 * 判断mysql是否正在运行
	 */
	public boolean isRunning() {
		return null == mysqlInstance ? false : mysqlInstance.isRunning();
	}

	public void shutdown() {
		if (mysqlInstance != null)
			mysqlInstance.shutdown();
	}

	public void cleanup() {
		if (mysqlInstance != null)
			mysqlInstance.cleanup();
	}
}
