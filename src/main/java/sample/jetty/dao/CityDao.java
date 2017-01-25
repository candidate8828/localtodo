package sample.jetty.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.City;

@Repository
public class CityDao {

	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;

	public City selectCityById(long id) {
		/*try {
			System.out.println(this.sqlSessionTemplate.getConnection().getMetaData());
			System.out.println(this.sqlSessionTemplate.getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource());
			System.out.println(this.sqlSessionTemplate.getSqlSessionFactory().getConfiguration().getEnvironment().getClass());
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		return this.sqlSessionTemplate.selectOne("SAMPLE_CITYMAPPER.selectCityById", id);
	}
	
	public int updateCityById(long id) {
		int effectivenum = this.sqlSessionTemplate.update("SAMPLE_CITYMAPPER.updateCityById", id);
		System.out.println("*********************    "+effectivenum+"    *********************");
		return effectivenum;
	}

}
