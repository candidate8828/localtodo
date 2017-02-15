package sample.jetty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.TFolderDao;
import sample.jetty.domain.TreeBean;

@Service
@Transactional
public class TFolderService {
	@Autowired
	private TFolderDao tFolderDao;
	
	/*@Transactional(readOnly=true)
	public List<TreeBean> selectRootFolders() {
		return tFolderDao.selectRootFolders();
	}*/
	
	@Transactional(readOnly=true)
	public List<TreeBean> selectChildrenFoldersByParentId(long id) {
		return tFolderDao.selectChildrenFoldersByParentId(id);
	}
	
	@Transactional(readOnly=true)
	public boolean checkFolderExistsOrNot(String folderName, long id) {
		return tFolderDao.checkFolderExistsOrNot(folderName, id);
	}
	
	@Transactional
	public boolean deleteFolderById(long id) {
		int existsValidFolderCount = tFolderDao.selectValidFolderCountByFolderId(id);
		int existsRelateLogCount = tFolderDao.selectValidLogCountByFolderId(id);
		if (0 == (existsValidFolderCount + existsRelateLogCount)) {
			// TODO 进行删除操作，然后返回删除成功
			// 先查出来，获取parentId的值，再删掉此node，然后将parentId相同的folders重新排序
			int effectCount = tFolderDao.deleteFolderById(id);
			// TODO 重新排列同一级下的node的order_by顺序
			return effectCount > 0;
		} else { // 否则不能删除，返回删除失败的标识
			return false;
		}
	}
	
	@Transactional
	public boolean addNewFolder(String folderName, long id) {
		int maxOrderNum = tFolderDao.selectMaxOrderNumByParentId(id);
		return tFolderDao.addNewFolder(folderName, id, maxOrderNum+1);
	}
}
