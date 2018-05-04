/**   
* @Title: BookDao.java 
* @Package cn.itheima.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-2-6 上午9:53:50 
* @version V1.0   
*/
package cn.itheima.dao;

import java.util.List;

import cn.itheima.po.Book;

/** 
 * @ClassName: BookDao 
 * @Description: 图书dao接口
 * @author 传智 小杨老师  
 * @date 2018-2-6 上午9:53:50 
 *  
 */
public interface BookDao {
	
	/**
	 * 查询全部图书列表
	 */
	List<Book> queryBookList();

}
