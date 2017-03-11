package sample.jetty.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.TFolderDao;
import sample.jetty.domain.FolderBean;
import sample.jetty.domain.TreeBean;

@Service
@Transactional
public class TFolderService {
	@Autowired
	private TFolderDao tFolderDao;

	@Transactional(readOnly=true)
	public List<TreeBean> selectChildrenFoldersByParentId(long id) throws Exception {
		List<FolderBean> folderList = tFolderDao.selectChildrenFoldersByParentId(id);
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		TreeBean treeBean = null;
		if (null != folderList && !folderList.isEmpty()) {
			for (FolderBean folder : folderList) {
				treeBean = new TreeBean();
				treeBean.setId(folder.getId());
				treeBean.setText(folder.getFolderName());
				treeList.add(treeBean);
			}
		}
		return treeList;
	}
	
	@Transactional(readOnly=true)
	public boolean checkFolderExistsOrNot(String folderName, long id) throws Exception {
		return tFolderDao.checkFolderExistsOrNot(folderName, id);
	}
	
	@Transactional
	public boolean deleteFolderById(long id) throws Exception {
		int existsValidFolderCount = tFolderDao.selectValidFolderCountByFolderId(id);
		int existsRelateLogCount = tFolderDao.selectValidLogCountByFolderId(id);
		if (0 == (existsValidFolderCount + existsRelateLogCount)) {
			// 进行删除操作，然后返回删除成功
			FolderBean selectedFolder = tFolderDao.selectFolderById(id);
			// 先查出来，获取parentId的值，再删掉此node，然后将parentId相同的folders重新排序
			int effectCount = tFolderDao.deleteFolderById(id);
			// 重新排列同一级下的node的order_by顺序
			List<FolderBean> folderList = tFolderDao.selectChildrenFoldersByParentId(selectedFolder.getParentId());
			FolderBean tempFolder = null;
			for (int i=1; i<= folderList.size(); i++) {
				tempFolder = folderList.get(i-1);
				tempFolder.setOrderBy(i);
				tFolderDao.updateFolderById(tempFolder);
			}
			return effectCount > 0;
		} else { // 否则不能删除，返回删除失败的标识
			return false;
		}
	}
	
	@Transactional
	public void moveUp(long folderId) throws Exception {
		// 先查出有没有比当前同级folder的orderby还小的记录
		List<FolderBean> searchedList = tFolderDao.selectExchangeFolderListByOrderBy4Up(folderId);
		// 有则对调两个记录的orderby的值
		if(null != searchedList && searchedList.size() == 2){
			Date now = new Date();
			int tempOrderBy = 0;
			FolderBean folderOne = searchedList.get(0);
			FolderBean folderTwo = searchedList.get(1);
			tempOrderBy = folderOne.getOrderBy();
			folderOne.setOrderBy(folderTwo.getOrderBy());
			folderTwo.setOrderBy(tempOrderBy);
			folderOne.setLastUpdDt(now);
			folderTwo.setLastUpdDt(now);
			tFolderDao.updateFolderById(folderOne);
			tFolderDao.updateFolderById(folderTwo);
		} else {// 没有则throw出exception("already moved to the top in this folder level")
			throw new Exception("already moved to the top in this folder level");
		}
	}
	
	@Transactional
	public void moveDown(long folderId) throws Exception {
		// 先查出有没有比当前同级folder的orderby还大的记录
		List<FolderBean> searchedList = tFolderDao.selectExchangeFolderListByOrderBy4Down(folderId);
		// 有则对调两个记录的orderby的值
		if(null != searchedList && searchedList.size() == 2){
			Date now = new Date();
			int tempOrderBy = 0;
			FolderBean folderOne = searchedList.get(0);
			FolderBean folderTwo = searchedList.get(1);
			tempOrderBy = folderOne.getOrderBy();
			folderOne.setOrderBy(folderTwo.getOrderBy());
			folderTwo.setOrderBy(tempOrderBy);
			folderOne.setLastUpdDt(now);
			folderTwo.setLastUpdDt(now);
			tFolderDao.updateFolderById(folderOne);
			tFolderDao.updateFolderById(folderTwo);
		} else {// 没有则throw出exception("already moved to the bottom in this folder level")
			throw new Exception("already moved to the bottom in this folder level");
		}
	}
	
	@Transactional
	public boolean addNewFolder(String folderName, long id) throws Exception {
		int maxOrderNum = tFolderDao.selectMaxOrderNumByParentId(id);
		return tFolderDao.addNewFolder(folderName, id, maxOrderNum+1) > 0L;
	}
	
	@Transactional
	public boolean updateFolderName(String folderName, long id) throws Exception {
		FolderBean tempFolder = tFolderDao.selectFolderById(id);
		tempFolder.setFolderName(folderName);
		tempFolder.setLastUpdDt(new Date());
		return tFolderDao.updateFolderById(tempFolder) > 0;
	}
}
