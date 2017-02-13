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
	
	@Transactional(readOnly=true)
	public List<TreeBean> selectRootFolders() {
		return tFolderDao.selectRootFolders();
	}
	
	@Transactional(readOnly=true)
	public List<TreeBean> selectChildrenFoldersByParentId(long id) {
		return tFolderDao.selectChildrenFoldersByParentId(id);
	}
	
	@Transactional(readOnly=true)
	public boolean checkFolderExistsOrNot(String folderName) {
		return tFolderDao.checkFolderExistsOrNot(folderName);
	}
	
	@Transactional
	public boolean addNewRootFolder(String folderName) {
		int maxOrderNum = tFolderDao.selectMaxOrderNumInRootFolder();
		return tFolderDao.addNewRootFolder(folderName, maxOrderNum+1);
	}
}
