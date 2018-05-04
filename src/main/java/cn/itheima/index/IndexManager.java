/**   
* @Title: IndexManager.java 
* @Package cn.itheima.index 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-2-6 上午10:18:34 
* @version V1.0   
*/
package cn.itheima.index;

import cn.itheima.dao.BookDao;
import cn.itheima.dao.impl.BookDaoImpl;
import cn.itheima.po.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: IndexManager 
 * @Description: 索引管理类 
 * @author 传智 小杨老师  
 * @date 2018-2-6 上午10:18:34 
 *  
 */
public class IndexManager {
	
	/**
	 * 定义索引库位置常量
	 */
	private static final String INDEX_PATH = "E:\\teach\\0337\\04lucene&solr\\index\\";
	
	
	/**
	 * 索引流程实现（创建索引）
	 * @throws IOException 
	 */
	@Test
	public void createIndex() throws IOException{
//		1.采集数据
		BookDao bookDao = new BookDaoImpl();
		List<Book> bookList = bookDao.queryBookList();
		
//		2.建立文档对象（Document）
		List<Document> docList = new ArrayList<Document>();
		for(Book book:bookList){
			// 创建文档对象
			Document doc = new Document();
			
//			图书id
			// add方法：把域添加到文档对象
			// 参数field：域
			// TextField：文本域。name：域的名称;value：域的值;store：指定是否把域的值保存到文档中
			doc.add(new TextField("bookId", book.getId()+"", Store.YES));
			
//			图书名称
			doc.add(new TextField("bookName", book.getBookname(), Store.YES));
//			图书价格
			doc.add(new TextField("bookPrice", book.getPrice()+"", Store.YES));
//			图书图片
			doc.add(new TextField("bookPic", book.getPic(), Store.YES));
//			图书描述
			doc.add(new TextField("bookDesc", book.getBookdesc(), Store.YES));
			
			docList.add(doc);
		}
		
//		3.建立分析器对象（Analyzer），用于分词
		//Analyzer analyzer = new StandardAnalyzer();
		
		// 使用ik分词器
		Analyzer analyzer = new IKAnalyzer();
		
//		4.建立索引库配置对象（IndexWriterConfig），配置索引库
		// matchVersion参数：指定当前lucene的版本
		// analyzer参数：当前使用的分析器对象
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		
//		5.建立索引库目录对象（Directory），指定索引库的位置
		File path = new File("E:\\teach\\0337\\04lucene&solr\\index\\");
		Directory directory = FSDirectory.open(path);
		
//		6.建立索引库操作对象（IndexWriter），用于操作索引库
		IndexWriter writer = new IndexWriter(directory, iwc);
		
//		7.使用IndexWriter对象，把文档对象写入索引库
		for(Document doc:docList){
			// addDocument方法：把文档对象添加到索引库
			writer.addDocument(doc);
			
		}
		
//		8.释放资源
		writer.close();
		
	}
	
	/**
	 * 检索流程实现（读取索引数据）
	 * @throws Exception 
	 */
	@Test
	public void readIndex() throws Exception{
//		1.建立分析器对象（Analyzer），用于分词
		//Analyzer analyzer = new StandardAnalyzer();
		
		// 使用ik分词器
		Analyzer analyzer = new IKAnalyzer();
		
//		2.建立查询对象（Query）：bookName:lucene
		// 2.1.建立查询解析器对象
			// 参数f：默认搜索域；参数a：使用分析器对象
		QueryParser parser = new QueryParser("bookName", analyzer);
		// 2.2.使用查询解析器对象，解析表达式，实例化Query对象
		Query query = parser.parse("bookName:lucene");
		
//		3.建立索引库目录对象（Directory），指定索引库的位置
		Directory directory = FSDirectory.open(new File(INDEX_PATH));
		
//		4.建立索引读取对象（IndexReader），用于把索引数据读取到内存中
		IndexReader reader = DirectoryReader.open(directory);
		
//		5.建立索引搜索对象（IndexSearcher），用于执行搜索
		IndexSearcher searcher = new IndexSearcher(reader);
		
//		6.使用IndexSearcher对象，执行搜索，返回搜索结果集TopDocs
		// search方法：执行搜索
		// 参数query：查询对象
		// 参数n：指定搜索结果排序以后的前n个
		TopDocs topDoc = searcher.search(query, 10);
		
//		7.处理结果集
		// 7.1打印实际搜索到的结果数量
		System.out.println("实际搜索到的结果数量："+topDoc.totalHits);
		
		// 7.2获取搜索结果的文档分值对象
		// 这里包含两个信息：一个是文档id；另一个文档的分值
		ScoreDoc[] scoreDocs = topDoc.scoreDocs;
		for(ScoreDoc sd:scoreDocs){
			System.out.println("-------------------------------");
			// 获取文档id和文档分值
			int docId = sd.doc;
			float score = sd.score;
			System.out.println("当前文档id："+docId+",当前文档分值:"+score);
			
			// 根据文档id取数据（相当于关系数据库中根据主键查询）
			Document doc = searcher.doc(docId);
			System.out.println("图书id："+doc.get("bookId"));
			System.out.println("图书名称："+doc.get("bookName"));
			System.out.println("图书价格："+doc.get("bookPrice"));
			System.out.println("图书图片："+doc.get("bookPic"));
			System.out.println("图书描述："+doc.get("bookDesc"));
		}
		
//		8.释放资源
		reader.close();
	}
	
	
	/**
	 * 检索流程实现（读取索引数据,实现分页搜索）
	 * @throws Exception 
	 */
	@Test
	public void readIndexPage() throws Exception{
//		1.建立分析器对象（Analyzer），用于分词
		//Analyzer analyzer = new StandardAnalyzer();
		
		// 使用ik分词器
		Analyzer analyzer = new IKAnalyzer();
		
//		2.建立查询对象（Query）：bookName:lucene
		// 2.1.建立查询解析器对象
			// 参数f：默认搜索域；参数a：使用分析器对象
		QueryParser parser = new QueryParser("bookName", analyzer);
		// 2.2.使用查询解析器对象，解析表达式，实例化Query对象
		Query query = parser.parse("bookName:lucene");
		
//		3.建立索引库目录对象（Directory），指定索引库的位置
		Directory directory = FSDirectory.open(new File(INDEX_PATH));
		
//		4.建立索引读取对象（IndexReader），用于把索引数据读取到内存中
		IndexReader reader = DirectoryReader.open(directory);
		
//		5.建立索引搜索对象（IndexSearcher），用于执行搜索
		IndexSearcher searcher = new IndexSearcher(reader);
		
//		6.使用IndexSearcher对象，执行搜索，返回搜索结果集TopDocs
		// search方法：执行搜索
		// 参数query：查询对象
		// 参数n：指定搜索结果排序以后的前n个
		TopDocs topDoc = searcher.search(query, 10);
		
//		7.处理结果集
		// 7.1打印实际搜索到的结果数量
		System.out.println("实际搜索到的结果数量："+topDoc.totalHits);
		
		// 7.2获取搜索结果的文档分值对象
		// 这里包含两个信息：一个是文档id；另一个文档的分值
		ScoreDoc[] scoreDocs = topDoc.scoreDocs;
		
		// 增加分页处理============================start
		// 1.当前页
		//int page=1;
		int page=2;
		
		// 2.每一页显示大小
		int pageSize = 2;
		
		// 3.当前页的记录开始数
		int start = (page-1)*pageSize;
		
		// 4.当前页的记录结束数
		// 常规的情况：start+pageSize;
		// 最后一页的情况：scoreDocs.length;
		// 取两者的最小值，是为了防止最后一页数据的，下标越界异常
		int end = Math.min(start+pageSize, scoreDocs.length);
		
		// 增加分页处理============================end
		//for(ScoreDoc sd:scoreDocs){
		for(int i=start;i<end;i++){
			
			System.out.println("-------------------------------");
			// 获取文档id和文档分值
			int docId = scoreDocs[i].doc;
			float score = scoreDocs[i].score;
			System.out.println("当前文档id："+docId+",当前文档分值:"+score);
			
			// 根据文档id取数据（相当于关系数据库中根据主键查询）
			Document doc = searcher.doc(docId);
			System.out.println("图书id："+doc.get("bookId"));
			System.out.println("图书名称："+doc.get("bookName"));
			System.out.println("图书价格："+doc.get("bookPrice"));
			System.out.println("图书图片："+doc.get("bookPic"));
			System.out.println("图书描述："+doc.get("bookDesc"));
		}
		
//		8.释放资源
		reader.close();
	}
	
	
	@Test
	public void test(){
		System.out.println(111);
		System.out.println(111);
		System.out.println(222);
		System.out.println(222);
	}
	
	
	
	
	
	
	
	

}
