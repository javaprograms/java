

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class OpenUrl {
	private static ServerSocket server;
	private final static int port = 80;

	public static void main(String[] args) {
//		try {
//			server = new ServerSocket(port);
//			System.out.println("server port: " + port + " start...");
//		} catch (Throwable t) {
//			t.printStackTrace();
//			return;
//		}
//		nextServerThreadStart();
		//http://1.163.com/history/00-80-00-03-10.html
		//String url="http://1.163.com/history/00-80-00-03-10.html";
		
		int i=1;
		while(i<4000){
		String url="http://1.163.com/code/get.do?gid=177&period=351&rid="+i;
		String encoding="utf-8";
		String content = getURLContent(url, encoding);
		System.out.println(i+" : "+content);
		JSONObject.fromObject(content);
		i++;
		}
	}

	private static Thread nextServerThreadStart() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				Socket socket = null;
				try {
					socket = server.accept();
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					nextServerThreadStart();
				}

				if (socket != null) {
					// System.out.println(socket.getPort());
					Thread t = sendThreadStart(socket);
					while (t.isAlive()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		return t;
	}

	private static Thread sendThreadStart(final Socket socket) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				BufferedReader is = null;
				OutputStream os = null;
				try {
					socket.setSoTimeout(20000);
					Map<String, String> paramters = getParamters(socket);
					String encoding = paramters.get("encoding");
					String url = paramters.get("url");
					String content = getURLContent(url, encoding);
					if (content == null)
						return;

					if (content.indexOf("<base ") == -1) {
						content = content.replaceFirst("<head>",
								"<head><base href=\"" + url + "\" />");
					}

					Writer out = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream(), encoding));
					out.write(content);
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					/*
					 * if (out != null){ try { out.close(); } catch (Exception
					 * e) {} }
					 */
					/*
					 * if (is != null) { try { is.close(); } catch (Exception e)
					 * { } }
					 */

					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
				}

			}
		});
		t.start();
		return t;
	}

	private static String gethead(final Socket socket) throws Exception {
		InputStream reader = socket.getInputStream();
		int available;
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		while ((available = reader.available()) > 0) {
			byte[] b = new byte[available];
			reader.read(b);
			data.write(b);
		}
		// System.out.println(data.toString());
		return data.toString();
	}

	private static Map<String, String> getParamters(final Socket socket)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String head = gethead(socket);
		if (head.split(" ").length < 2)
			return map;
		String GETStr = head.split(" ")[1];
		String[] split = GETStr.replaceFirst("%\\?", "").split("[&?]+");
		for (String str : split) {
			int i = str.indexOf("=");
			if (i >= 1 && i <= str.length() - 2) {
				map.put(str.substring(0, i), str.substring(i + 1));
			}
		}
		return map;
	}

	public static String getURLContent(String url, String encoding) {
		if (url == null || "".equals(url.trim()))
			return null;

		StringBuffer content = new StringBuffer();
		try {
			// 新建URL对象
			URL u = new URL(url);
			InputStream in = new BufferedInputStream(u.openStream());
			InputStreamReader theHTML = new InputStreamReader(in,
					encoding != null ? encoding : "gb2312");
			int c;
			while ((c = theHTML.read()) != -1) {
				content.append((char) c);
			}
		}
		// 处理异常
		catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		return content.toString();
	}
}
