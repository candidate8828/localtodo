package sample.jetty.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.FolderBean;

@Repository
public class TFolderDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;

	@SuppressWarnings({ "unchecked", "hiding" })
	public <FolderBean> List<FolderBean> selectChildrenFoldersByParentId(long id) throws Exception {
		return (List<FolderBean>)this.sqlSessionTemplate.selectList("SAMPLE_FOLDER_MAPPER.selectChildrenFoldersByParentId", id);
	}
	
	public boolean checkFolderExistsOrNot(String folderName, long id) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("folderName", folderName);
		paramMap.put("id", id);
		return (Integer)this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectFolderCountByName", paramMap) > 0;
	}

	public int selectMaxOrderNumByParentId(long id) throws Exception {
		Integer resultInteger = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectMaxOrderNumByParentId", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}

	public long addNewFolder(String folderName, long id, int maxOrderNum) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("folderName", folderName);
		paramMap.put("id", id);
		paramMap.put("maxOrderNum", maxOrderNum);
		return this.sqlSessionTemplate.insert("SAMPLE_FOLDER_MAPPER.addNewFolder", paramMap);
	}
	
	public FolderBean selectFolderById(long id) throws Exception {
		FolderBean folder = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectFolderById", id);
		return folder;
	}
	
	/**
	 * 查询指定folder node下存在的有效folder的数量
	 * @param id
	 * @return
	 */
	public int selectValidFolderCountByFolderId(long id) throws Exception {
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
	public int selectValidLogCountByFolderId(long id) throws Exception {
		Integer resultInteger = this.sqlSessionTemplate.selectOne("SAMPLE_FOLDER_MAPPER.selectValidLogCountByFolderId", id);
		if (null == resultInteger) {
			return 0;
		} else {
			return resultInteger.intValue();
		}
	}
	
	/**
	 * 根据指定id刪除指定的folder
	 * @param id
	 * @return
	 */
	public int deleteFolderById(long id) throws Exception {
		int resultInt = this.sqlSessionTemplate.delete("SAMPLE_FOLDER_MAPPER.deleteFolderById", id);
		if (0 == resultInt) {
			return 0;
		} else {
			return resultInt;
		}
	}
	
	public int updateFolderById(FolderBean folder) throws Exception {
		if (0 == folder.getId()) {
			return 0;
		}
		int resultInt = this.sqlSessionTemplate.update("SAMPLE_FOLDER_MAPPER.updateFolderById", folder);
		return resultInt;
	}
	
	public long addFolderAndLogRelation(long parentFolderId, long logId) throws Exception {
		if (parentFolderId < -2 || logId <= 0) {
			throw new Exception("parentFolderId <= 0 or logId <= 0");
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentFolderId", parentFolderId);
		paramMap.put("logId", logId);
		return this.sqlSessionTemplate.insert("SAMPLE_FOLDER_MAPPER.addFolderAndLogRelation", paramMap);
	}
}
