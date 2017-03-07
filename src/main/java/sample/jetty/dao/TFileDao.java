package sample.jetty.dao;

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
}
