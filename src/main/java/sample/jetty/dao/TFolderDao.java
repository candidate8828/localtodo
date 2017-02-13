package sample.jetty.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TFolderDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	@SuppressWarnings({ "unchecked" })
	public <TreeBean> List<TreeBean> selectRootFolders() {
		return (List<TreeBean>)this.sqlSessionTemplate.selectList("SAMPLE_FOLDER_MAPPER.selectRootFolders");
	}
	
	@SuppressWarnings({ "unchecked" })
	public <TreeBean> List<TreeBean> selectChildrenFoldersByParentId(long id) {
		return (List<TreeBean>)this.sqlSessionTemplate.selectList("SAMPLE_FOLDER_MAPPER.selectChildrenFoldersByParentId", id);
	}
	
	public boolean checkFolderExistsOrNot(String folderName) {
		return (Integer)this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectFolderCountByName", folderName) > 0;
	}
	
	public int selectMaxOrderNumInRootFolder() {
		return this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectMaxOrderNumInRootFolder");
	}
	
	public boolean addNewRootFolder(String folderName, int maxOrderNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("folderName", folderName);
		paramMap.put("maxOrderNum", maxOrderNum);
		return this.sqlSessionTemplate.insert("SAMPLE_FOLDER_MAPPER.addNewRootFolder", paramMap) > 0;
	}
}
