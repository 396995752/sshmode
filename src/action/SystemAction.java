package action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;

import service.SystemService;

public class SystemAction {
	public SystemService systemService;
	private InputStream inputStream; 
	private File file;
	private String fileContentType;
    private String fileFileName;
    
	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String execute(){
		String a=systemService.test();
		System.out.println(a+"@@");
		return "success";
	}
	
	public String uploadPic(){
//		String savePath =ServletActionContext.getServletContext().getRealPath("/images/");
		String savePath="C:/Users/HP/Desktop/sshMode_/WebRoot/images/";
		JSONObject jsonObject=new JSONObject();
		String returnmsg= "success";
		jsonObject.accumulate("msg", 1);
		jsonObject.accumulate("success", true);
		try {
			FileInputStream fis=new FileInputStream(file);//文件输入流
//			long aa=fis.getChannel().size();
			Blob content=Hibernate.createBlob(fis);
			String url="images/"+fileFileName;
			String a=systemService.uploadPic(fileFileName,url,content);
			String newFilePath=savePath+"\\"+fileFileName;
			FileUtils.copyFile(file, new File(newFilePath));
//			byte[] byt = new byte[fis.available()];
//			int length = 0;
//			int n = fis.read();
//			FileOutputStream fos = new FileOutputStream(savePath+fileFileName);
//			fos.write(byt);
//			byte buffer[] = new byte[1024];
//			while((length = fis.read(buffer))>0){
//			while((length = fis.read(buffer))>0){
//			if(n!=-1){
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
//                fos.write(buffer, 0, length);
//            }
//			fis.close();
            //关闭输出流
//            fos.close();
			inputStream = new ByteArrayInputStream(jsonObject.toString().getBytes("UTF-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
//		System.out.println(a+"@@");
		return "success";
	}
	
	public String loadPics(){
		JSONObject jsonObject=new JSONObject();
		String returnmsg= "success";
		System.out.println(123432);
		jsonObject.accumulate("success", true);
		String str=systemService.loadPics();
		jsonObject.accumulate("msg",str);
		try {
			inputStream = new ByteArrayInputStream(jsonObject.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
}
