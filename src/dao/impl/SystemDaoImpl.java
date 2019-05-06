package dao.impl;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

import dao.SystemDao;
import entity.Attachment;

public class SystemDaoImpl extends BaseDao implements SystemDao {

	@Override
	public String test() {
		// TODO Auto-generated method stub
		System.out.println(12334);
		String hql="select u from User u";
		List list=this.selectByHql(hql);
		System.out.println("List.size()="+list.size());
		return "success";
	}
	
	public String uploadPic(String filename,String url,Blob content){
		String msg="";
		Attachment a=new Attachment();
		String sql="select max(id)+1 as id from Attachment";
		List list =this.selectBySql(sql);
		int id=1;
		if(list.size()>0){
			Map m=(Map)list.get(0);
			id=new Integer(m.get("id")==null?"1":m.get("id").toString());
		}
		a.setId(id);
		a.setContent(content);
		a.setName(filename);
		a.setUrl(url);
		this.saveObject(a);
		return msg;
	}
	
	public String loadPics(){
		String str="";
		String hql="select a from Attachment a ";
		List list =this.selectByHql(hql);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Attachment a=(Attachment)list.get(i);
				String url=a.getUrl();
				str+="<div class=\"product-grid\"><div class=\"content_box\"><a href=\"single.html\"><div class=\"left-grid-view grid-view-left\">"+
						"<img src="+"\""+url+"\" class=\"img-responsive watch-right\" alt=/><div class=\"mask\"><div class=\"info\">Quick View</div></div></a></div><h4><a href=\"#\"> Duis autem</a></h4>"+
						"<p>It is a long established fact that a reader</p>Rs. 499</div></div> ";
			}
		}
		str+="<div class=\"clearfix\"> </div>";
		return str;
	}
}
