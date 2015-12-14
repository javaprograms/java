import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;


public class Test {
//	//test()
	public static void main(String[] args) {
		List list=new ArrayList();
		 try{  
			 String url="http://1.163.com/list/0-0-1-2.html";
			 Parser parser = new Parser( (HttpURLConnection) (new URL(url)).openConnection() );  
			 parser.setEncoding("utf-8");
	            // 这里是控制测试的部分，后面的例子修改的就是这个地方。  
			  NodeFilter filter2 = new HasAttributeFilter( "class", "w-goods-title f-txtabb" );
			 //NodeList nodes = parser.extractAllNodesThatMatch(filter);
	           // NodeFilter filter = new TagNameFilter ("p");  
			 NodeFilter filterID = new TagNameFilter( "p" );
			 //NodeFilter filterChild = new HasChildFilter(filterA);
			 NodeFilter filter = new AndFilter(filterID, filter2);
			 
	            NodeList nodes = parser.extractAllNodesThatMatch(filter);   
	              
	            if(nodes!=null) {  
	            	long tmp=0;
	                for (int i = 0; i < nodes.size(); i++) {  
	                    Node textnode = (Node) nodes.elementAt(i);  
	                     LuckTime luckTime=new LuckTime();
	                     
	                    String time=textnode.getChildren().elementAt(0).toHtml();
	                    String num=textnode.getChildren().elementAt(4)==null?null:textnode.getChildren().elementAt(4).toHtml();  
	                    if(num==null)
	                    {
	                    	break;
	                    }
	                    luckTime.setNum(num);
	                    luckTime.setTime(time);
	                    list.add(luckTime);
	                    tmp=tmp+Long.parseLong(num);
	                }  
	                LuckTime luckTime=new LuckTime();
	                luckTime.setNum(String.valueOf(tmp));
	                luckTime.setTime("汇总");
	                list.add(luckTime);
	            }               
	            
	            
	        }  
	        catch( Exception e ) {      
	        	e.printStackTrace();
	        }  
	
	}
	public static void main3(String[] args) {
		// TODO Auto-generated method stub
		try{ 
			String catalog=args[0];
			String num=args[1];
			String pagesize=args[2];
			String topstr=args[3];
			String filename=args[4];
			
			XlsWrite xlsWrite =new XlsWrite(filename);
			//getJoinNum("116","1238","5000");
			List numlist=getJoinNum(catalog,num,pagesize);
			//getTop50("01-77-00-03-51");
		    
			xlsWrite.createSheet("当前以参与抽奖号", 0);
			xlsWrite.setSheetindex(0);
			xlsWrite.setParalist("用户id,用户昵称,参与时间,参与次数,记录集id,ip地址,ip,抽奖号","userid,nickname,time,num,rid,ipaddress,ip,code");
			xlsWrite.writeData(numlist);
			
			
			List timelist=getTop50(topstr);
			
			xlsWrite.createSheet("TOP参与信息", 1);
			xlsWrite.setSheetindex(1);
			xlsWrite.setParalist("时间,值","time,num");
			
			xlsWrite.writeData(timelist);
			xlsWrite.closeXls();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		      
	}
	public static List getTop50(String mark){
		List list=new ArrayList();
		 try{  
			 String url="http://1.163.com/detail/"+mark+".html";
			 Parser parser = new Parser( (HttpURLConnection) (new URL(url)).openConnection() );  
			 parser.setEncoding("utf-8");
	            // 这里是控制测试的部分，后面的例子修改的就是这个地方。  
			  NodeFilter filter2 = new HasAttributeFilter( "class", "time" );
			 //NodeList nodes = parser.extractAllNodesThatMatch(filter);
	           // NodeFilter filter = new TagNameFilter ("DIV");  
			 NodeFilter filterID = new TagNameFilter( "td" );
			 //NodeFilter filterChild = new HasChildFilter(filterA);
			 NodeFilter filter = new AndFilter(filterID, filter2);
			 
	            NodeList nodes = parser.extractAllNodesThatMatch(filter);   
	              
	            if(nodes!=null) {  
	            	long tmp=0;
	                for (int i = 0; i < nodes.size(); i++) {  
	                    Node textnode = (Node) nodes.elementAt(i);  
	                     LuckTime luckTime=new LuckTime();
	                     
	                    String time=textnode.getChildren().elementAt(0).toHtml();
	                    String num=textnode.getChildren().elementAt(4)==null?null:textnode.getChildren().elementAt(4).toHtml();  
	                    if(num==null)
	                    {
	                    	break;
	                    }
	                    luckTime.setNum(num);
	                    luckTime.setTime(time);
	                    list.add(luckTime);
	                    tmp=tmp+Long.parseLong(num);
	                }  
	                LuckTime luckTime=new LuckTime();
	                luckTime.setNum(String.valueOf(tmp));
	                luckTime.setTime("汇总");
	                list.add(luckTime);
	            }               
	            
	            
	        }  
	        catch( Exception e ) {      
	        	e.printStackTrace();
	        }  
		return list;
	}
	 
	//获取当期参与所有幸运号
	/**
	 * 
	 * @param gid     商品类别
	 * @param period  当前期数
	 * @param maxnum  最大参与次数
	 * @return
	 */
	public static List getJoinNum(String gid,String period,String maxnum){
		List list=new ArrayList();
		String   url="http://1.163.com/record/getDuobaoRecord.do?gid="+gid+"&period="+period+"&pageSize="+maxnum+"&pageNum=1&t=1419608901666";
		String encoding="utf-8";
		String jscontent = OpenUrl.getURLContent(url, encoding);
		JSONObject jb = JSONObject.fromObject(jscontent);
		  //{"result":{"list":[{"time":"2014-12-26 10:31:14.674","multi":0,"code":["10005020","10006029"]}]},"code":0}
		  JSONObject j=JSONObject.fromObject(jb.get("result"));
		  JSONArray ja = JSONArray.fromObject(j.get("list"));
		  for( Object o :ja){
			  JSONObject jsonNode = JSONObject.fromObject(o);
			  String time=jsonNode.getString("time"); //参与时间
			  String num=jsonNode.getString("num");//参与次数
			  String rid=jsonNode.getString("rid");//参与记录id
			  //用户信息
			  JSONObject jsonNodeUser= jsonNode.getJSONObject("user");
			  String userid=jsonNodeUser.getString("uid");
			  String nickname=jsonNodeUser.getString("nickname");
			  String ipAdress=jsonNodeUser.getString("IPAddress");
			  String ip=jsonNodeUser.getString("IP");
			 
			 List codelist= getLunckNum(  gid,  period,rid);
			 for(int i=0;i<codelist.size();i++){
				 LuckInfo luckInfo=new LuckInfo();
				 luckInfo.setUserid(userid);
				 luckInfo.setIp(ip);
				 luckInfo.setIpaddress(ipAdress);
				 luckInfo.setNum(num);
				 luckInfo.setTime(time);
				 luckInfo.setNickname(nickname);
				 luckInfo.setCode(codelist.get(i).toString());
				 luckInfo.setRid(rid);
				 list.add(luckInfo);
			 }
		  }
		  return list;
//		  JSONObject jsonNode = JSONObject.fromObject(ja.get(0));
//		  String ctime=jsonNode.getString("time");  
//		  JSONArray codeArray=jsonNode.getJSONArray("code");
//		  int [ ]  a= new int[codeArray.size()];;
//		  for (Object o : codeArray)
//	        {
//			  getLunckNum("12313");
//	            System.out.println(o.toString());
//	          
//	        }
		 
	}
	//获取指定的幸运号
	public static   List<String> getLunckNum(String gid,String period,String rid ){
		
		  String url="http://1.163.com/code/get.do?gid="+gid+"&period="+period+"&rid="+rid;
			String encoding="utf-8";
			String jscontent = OpenUrl.getURLContent(url, encoding);
		  JSONObject jb = JSONObject.fromObject(jscontent);
		  //{"result":{"list":[{"time":"2014-12-26 10:31:14.674","multi":0,"code":["10005020","10006029"]}]},"code":0}
		  JSONObject j=JSONObject.fromObject(jb.get("result"));
		  JSONArray ja = JSONArray.fromObject(j.get("list"));
		  JSONObject jsonNode = JSONObject.fromObject(ja.get(0));
		  String ctime=jsonNode.getString("time");
		  
		  JSONArray codeArray=jsonNode.getJSONArray("code");
		  List<String> list=new ArrayList<String>();
		  for (Object o : codeArray)
	        {
	            
			  list.add(o.toString());
	          
	        }
		return list;
	}

}
