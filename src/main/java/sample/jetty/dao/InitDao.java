package sample.jetty.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InitDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	public boolean checkTablesExistsOrNot() throws Exception {
		return ((Long)this.sqlSessionTemplate.selectOne("SAMPLE_INIT_MAPPER.checkTablesExistsOrNot")) >= 0L;
	}
}
