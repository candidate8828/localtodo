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
	
	/*@SuppressWarnings({ "unchecked" })
	public <TreeBean> List<TreeBean> selectRootFolders() {
		return (List<TreeBean>)this.sqlSessionTemplate.selectList("SAMPLE_FOLDER_MAPPER.selectRootFolders");
	}*/
	
	@SuppressWarnings({ "unchecked" })
	public <TreeBean> List<TreeBean> selectChildrenFoldersByParentId(long id) {
		return (List<TreeBean>)this.sqlSessionTemplate.selectList("SAMPLE_FOLDER_MAPPER.selectChildrenFoldersByParentId", id);
	}
	
	public boolean checkFolderExistsOrNot(String folderName, long id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("folderName", folderName);
		paramMap.put("id", id);
		return (Integer)this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectFolderCountByName", paramMap) > 0;
	}

	public int selectMaxOrderNumByParentId(long id) {
		Integer resultInteger = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectMaxOrderNumByParentId", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}

	public boolean addNewFolder(String folderName, long id, int maxOrderNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("folderName", folderName);
		paramMap.put("id", id);
		paramMap.put("maxOrderNum", maxOrderNum);
		return this.sqlSessionTemplate.insert("SAMPLE_FOLDER_MAPPER.addNewFolder", paramMap) > 0;
	}
	
	/**
	 * 查询指定folder node下存在的有效folder的数量
	 * @param id
	 * @return
	 */
	public int selectValidFolderCountByFolderId(long id) {
		Integer resultInteger = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectValidFolderCountByFolderId", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}
	
	/**
	 * 查询指定folder node下存在的有效log的数量
	 * @param id
	 * @return
	 */
	public int selectValidLogCountByFolderId(long id) {
		Integer resultInteger = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectValidLogCountByFolderId", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}
	
	/**
	 * 查询指定folder node下存在的有效log的数量
	 * @param id
	 * @return
	 */
	public int deleteFolderById(long id) {
		Integer resultInteger = this.sqlSessionTemplate.delete("SAMPLE_FOLDER_MAPPER.deleteFolderById", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}
}
