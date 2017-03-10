package sample.jetty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.InitDao;

@Service
@Transactional
public class InitService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InitDao initDao;
	
	@Transactional(readOnly=true)
	public boolean checkTablesExistsOrNot() throws Exception {
		return initDao.checkTablesExistsOrNot();
	}
	
}
