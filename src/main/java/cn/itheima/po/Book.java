/**   
* @Title: Book.java 
* @Package cn.itheima.po 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-2-6 上午9:52:08 
* @version V1.0   
*/
package cn.itheima.po;

/** 
 * @ClassName: Book 
 * @Description: 图书pojo 
 * @author 传智 小杨老师  
 * @date 2018-2-6 上午9:52:08 
 *  
 */
public class Book {
	
	  private Integer id; // int(11) DEFAULT NULL,
	  private String bookname; // varchar(500) DEFAULT NULL,
	  private Float price; // float DEFAULT NULL,
	  private String pic; // varchar(200) DEFAULT NULL,
	  private String bookdesc; // varchar(2000) DEFAULT NULL
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the bookname
	 */
	public String getBookname() {
		return bookname;
	}
	/**
	 * @param bookname the bookname to set
	 */
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	/**
	 * @return the price
	 */
	public Float getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Float price) {
		this.price = price;
	}
	/**
	 * @return the pic
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * @param pic the pic to set
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * @return the bookdesc
	 */
	public String getBookdesc() {
		return bookdesc;
	}
	/**
	 * @param bookdesc the bookdesc to set
	 */
	public void setBookdesc(String bookdesc) {
		this.bookdesc = bookdesc;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Book [id=" + id + ", bookname=" + bookname + ", price=" + price
				+ ", pic=" + pic + ", bookdesc=" + bookdesc + "]";
	}


}
