package dao.impl;

/*
 * Created on 2007-7-11
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.io.Serializable;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.fileupload.FileItem;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import sun.awt.image.ImageWatched.Link;


/**
 * @author longxiaoyao
 * 基于连接为Hibernate
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseDao extends HibernateDaoSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8619417561937254203L;

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#saveObject(java.lang.Object)
	 */
	public Object saveObject(Object object) {
		return getHibernateTemplate().save(object);
	}

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#updateObject(java.lang.Object)
	 */
	public Object updateObject(Object object) {
		getHibernateTemplate().update(object);
		return object;
	}

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#delObject(java.lang.Object)
	 */
	public void delObject(Object object) {
		getHibernateTemplate().delete(object);
	}

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#loadObject(java.lang.Class, java.io.Serializable)
	 */
	public Object loadObject(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#selectByHql(java.lang.String)
	 */
	public List selectByHql(String hql) {
		return getHibernateTemplate().find(hql);
	}
	
	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#selectByHql(java.lang.String, java.lang.Object[])
	 */
	public List selectByHql(String hql, Object[] obj) {
		if(null == obj || obj.length == 0) {
			return getHibernateTemplate().find(hql);
		} else {
			return getHibernateTemplate().find(hql, obj);
		}
	}

	public void modifyBySql(String sql, Object[] obj) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getSession().connection();
			ps = con.prepareStatement(sql);
			if(null != obj) {
				for(int i = 0; i < obj.length; i++) {
					/**
					 * 如何判断数据类型是Long
					 * 做法待改善
					 * 现在只是超出varchar2范围就当Long类型（CLOB和BLOB除外）来存
					 */
					if(obj[i].toString().length() >= 2000){
						//Long 类型存数据库
						ps.setCharacterStream(i+1, new StringReader(obj[i].toString()), obj[i].toString().length());
					}else{
						ps.setObject(i+1, obj[i]);
					}
				}
			}
			ps.execute();
			ps.close();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("根据sql修改失败\r\n" + e.getMessage());
		}
		flush();
	}
	
	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#delObjectBySql(java.lang.String)
	 */
	public void delObjectBySql(String sql) {
		modifyBySql(sql, null);
	}

	/* (non-Javadoc)
	 * @see cn.com.gsoft.account.dao.IBaseDao#delObjectBySql(java.lang.String, java.lang.Object[])
	 */
	public void delObjectBySql(String sql, Object[] obj) {
		modifyBySql(sql, obj);
	}

	public void updateObjectBySql(String sql) {
		modifyBySql(sql, null);
	}

	public void updateObjectBySql(String sql, Object[] obj) {
		modifyBySql(sql, obj);
	}

	public List selectBySql(String sql) {
		return selectBySql(sql, null);
	}

	public List selectBySql(String sql, Object[] obj) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getSession().connection();
			ps = con.prepareStatement(sql);
			if(null != obj) {
				for(int i = 0; i < obj.length; i++) {
					ps.setObject(i+1, obj[i]);
				}
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Map base = new LinkedHashMap(100);
				ResultSetMetaData data = rs.getMetaData();
				for(int i = 1; i < (data.getColumnCount()+1); i++) {
					String name = data.getColumnName(i);
					Object o = rs.getObject(name);
					base.put(name.toLowerCase(), o);
				}
				list.add(base);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("根据sql查询失败\r\n" + e.getMessage());
		} finally {
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void modifyBySqlAndList(String sql, List datas) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getSession().connection();
			ps = con.prepareStatement(sql);
			Iterator it = datas.iterator();
			while(it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				for(int i = 0; i < obj.length; i++) {
					ps.setObject(i+1, obj[i]);
				}
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("根据sql修改失败\r\n" + e.getMessage());
		}
		flush();
	}

	public void flush() {
		getHibernateTemplate().flush();
		getHibernateTemplate().clear();
	}
	
	public String[] callProcedure(String sql, Object[] obj) {
		String[] result = new String[2];
		Connection con = null;
		CallableStatement ps = null;
		try {
			con = getSession().connection();
			ps = con.prepareCall(sql);
			ps.registerOutParameter(1, Types.VARCHAR);
			ps.registerOutParameter(2, Types.VARCHAR);
			if(null != obj) {
				for(int i = 0; i < obj.length; i++) {
					ps.setObject(i+3, obj[i]);
				}
			}
			ps.execute();
			result[0] = (String) ps.getObject(1);
			result[1] = (String) ps.getObject(2);
			ps.close();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行存储过程失败\r\n" + e.getMessage());
		}
		return result;
	}

	public String[] callProcedure(String sql) {
		return callProcedure(sql, null);
	}

	



	/* (non-Javadoc)
	 * @see cn.com.gsoft.core.dao.IBaseDao#saveOrUpdateObject(java.lang.Object)
	 */
	public void saveOrUpdateObject(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

    public Object saveOrUpdate(Object object) {
        this.getHibernateTemplate().saveOrUpdate(object);
        return object;
    }
   
   
   
   
    public Object load(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}
    
    public boolean excuteSql(String sql){
        boolean ifSuccess = false;
		Statement sta = null;
		Connection con = null;
		try {
			con = getSession().connection();
			sta = con.createStatement();
			ifSuccess  = sta.execute(sql);		
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行sql失败\r\n" + e.getMessage());
		} finally {
			try {
				sta.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return ifSuccess;
	}	
}

