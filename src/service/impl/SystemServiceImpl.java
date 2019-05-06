package service.impl;

import java.sql.Blob;

import dao.SystemDao;
import service.SystemService;

public class SystemServiceImpl implements SystemService {
	public SystemDao systemDao;
	
	public SystemDao getSystemDao() {
		return systemDao;
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}

	@Override
	public String test() {
		// TODO Auto-generated method stub
		return systemDao.test();
	}
	
	public String uploadPic(String filename,String url,Blob content){
		return systemDao.uploadPic(filename,url,content);
	}
	
	public String loadPics(){
		return systemDao.loadPics();
	}

}
