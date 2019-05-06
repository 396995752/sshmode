package dao;

import java.sql.Blob;

public interface SystemDao {
	public String test();
	
	public String uploadPic(String filename,String url,Blob content);
	
	public String loadPics();
}
