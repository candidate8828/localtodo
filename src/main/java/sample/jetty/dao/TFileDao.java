package sample.jetty.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.FileBean;

@Repository
public class TFileDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	public long addNewFile(FileBean fileBean) throws Exception {
		long result = this.sqlSessionTemplate.insert("SAMPLE_FILE_MAPPER.addNewFile", fileBean);
		return result;
	}
	
	public FileBean getFileByLogIdAndCreateTime(long logId, long createTime) throws Exception {
		HashMap<String, Long> param = new HashMap<String, Long>();
		param.put("logId", logId);
		param.put("createTime", createTime);
		return (FileBean)this.sqlSessionTemplate.selectOne("SAMPLE_FILE_MAPPER.getFileByLogIdAndCreateTime", param);
	}
}
