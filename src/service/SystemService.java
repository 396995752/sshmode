package service;

import java.sql.Blob;

public interface SystemService {
	public String test();

	public String uploadPic(String fileFileName, String url, Blob content);
	
	public String loadPics();
}
