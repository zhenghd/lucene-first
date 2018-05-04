/**   
* @Title: BookDaoImpl.java 
* @Package cn.itheima.dao.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-2-6 上午9:55:06 
* @version V1.0   
*/
package cn.itheima.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cn.itheima.dao.BookDao;
import cn.itheima.po.Book;

/** 
 * @ClassName: BookDaoImpl 
 * @Description: 图书dao接口实现类
 * @author 传智 小杨老师  
 * @date 2018-2-6 上午9:55:06 
 *  
 */
public class BookDaoImpl implements BookDao {

	/* (non-Javadoc)
	 * @see cn.itheima.dao.BookDao#queryBookList()
	 */
	public List<Book> queryBookList() {
		// TODO Auto-generated method stub
		// 定义图书结果集list
		List<Book> bookList = new ArrayList<Book>();
		
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try{
//			加载驱动
			Class.forName("com.mysql.jdbc.Driver");
//			创建数据库连接对象
			con = DriverManager.
					getConnection("jdbc:mysql://127.0.0.1:3306/72_lucene", "root", "admin");
			
//			定义一个sql语句
			String sql = "select * from book";
			
//			创建statement语句对象
			psmt = con.prepareStatement(sql);
			
//			设置参数
			
//			执行
			rs = psmt.executeQuery();
			
//			处理结果集
			while(rs.next()){
				// 定义图书对象
				Book book = new Book();
				
//				图书id
				book.setId(rs.getInt("id"));
//				图书名称
				book.setBookname(rs.getString("bookname"));
//				图书价格
				book.setPrice(rs.getFloat("price"));
//				图书图片
				book.setPic(rs.getString("pic"));
				
//				图书描述
				book.setBookdesc(rs.getString("bookdesc"));
				
				bookList.add(book);
				
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			释放资源
			try{
				if(rs != null) rs.close();
				if(psmt != null) psmt.close();
				if(con != null) con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}

		return bookList;
	}
	
	/**
	 * 测试接口方法是否能够正常执行
	 */
	public static void main(String[] args) {
		
		BookDao bookDao = new BookDaoImpl();
		List<Book> list = bookDao.queryBookList();
		for(Book book:list){
			System.out.println(book);
		}
		
	}

}
