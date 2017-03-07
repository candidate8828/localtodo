package sample.jetty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.TFileDao;
import sample.jetty.domain.FileBean;

@Service
@Transactional
public class TFileService {
private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TFileDao tFileDao;
	
	@Transactional
	public long addNewFile(FileBean fileBean) throws Exception {
		return tFileDao.addNewFile(fileBean);
	}
}
