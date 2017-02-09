package sample.jetty.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.UserBean;

@Repository
public class TUserDao {

	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;

	public UserBean selectUserById(long id) {
		return this.sqlSessionTemplate.selectOne("SAMPLE_USER_MAPPER.selectUserById", id);
	}
	
	public int updateUserById(long id) {
		int effectivenum = this.sqlSessionTemplate.update("SAMPLE_USER_MAPPER.updateUserById", id);
		return effectivenum;
	}

}
