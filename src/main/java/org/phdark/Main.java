package org.phdark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main
{
	
	public static final char[] CHARS_IN_WINDOWS =
	{ '?', '*', '<', '>', '★', '！',':' };

	public static final String SAVE_PATH = "/Users/phoenixdark/Downloads/1024/";

	public static final String[] RANDOM_UA =
	{ "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
			"Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
			"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
			"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
			"Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
			"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
			"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
			"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24" };

	private static List<String> PROXY_IPS = Collections.EMPTY_LIST;

	public static void main(String[] args) throws IOException
	{
		getIpList();
		System.getProperties().setProperty("http.proxyHost", "");
		System.getProperties().setProperty("http.proxyPort", "");
		Document doc = Jsoup.connect("http://1024.91lulea.click/pw/thread.php?fid=22&page=1")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip").get();
		//System.out.println(doc.html());
		Elements links = doc.select("tbody[style*='table-layout:fixed;']").select("a");
		for (Element link : links)
		{
			String href = link.attr("abs:href");
			String title = link.attr("title");
			if (href != null && href.contains("htm_data") && href.contains(".html")
					&& (title==null || title==""))
			{
				System.out.println(link.attr("abs:href") + "\t" + link.text());
				String text = link.text().trim();
				File f = createDir(getFormateFilename(text));
				processSub(href, f);
			}
		}
	}

	private static String getFormateFilename(String filename)
	{
		for (char c : CHARS_IN_WINDOWS)
		{
			filename = filename.replace(c, ' ');
		}
		filename = filename.replace("\t", "").replace("\n", "").replace("\r", "").replace(File.separatorChar, ' ');
		return filename;
	}

	private static File createDir(String filename)
	{
		File f = new File(SAVE_PATH + filename);
		if (!f.exists())
		{
			f.mkdirs();
		}
		return f;
	}

	private static void processSub(String url,File saveDir) throws IOException
	{
		Document doc = Jsoup.connect(url)
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip").get();
		Elements imgs = doc.select("div#read_tpc").select("img");
		int i = 1;
		for (Element img : imgs)
		{
			byte[] b = downloadSingleImg(img.attr("src"), false, 3);
			if (b!=null && b.length>20000) {
				File imgFile = new File(saveDir, i+".jpg");
				FileUtils.writeByteArrayToFile(imgFile, b);
				System.out.println("########下载图片[" + imgFile.getAbsolutePath() + "]成功");
			}
			i++;
		}
	}

	private static byte[] downloadSingleImg(String url, boolean proxy, int retry) throws IOException
	{
		Connection conn = Jsoup.connect(url).ignoreContentType(true).header("User-Agent", randomUA())
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Cache-Control", "no-cache").header("Upgrade-Insecure-Requests", "1");
		if (proxy)
		{
			String[] proxyIp = getRandomIp();
			System.getProperties().setProperty("http.proxyHost", proxyIp[0]);
	        System.getProperties().setProperty("http.proxyPort", proxyIp[1]);
			//conn.proxy(proxyIp[0], Integer.valueOf(proxyIp[1])).execute();
		} else {
			System.getProperties().setProperty("http.proxyHost", "");
			System.getProperties().setProperty("http.proxyPort", "");
		}
		try
		{
			Response resp = conn.execute();
			return resp.bodyAsBytes();
		}
		catch (Exception e)
		{
			if (!proxy) {
				downloadSingleImg(url, true, retry);
			} else {
				downloadSingleImg(url, true, retry--);
			}
				
		}
		System.out.println("下载图片[" + url + "]出错");
		return null;
	}

	private static String randomUA()
	{
		int i = RandomUtils.nextInt(0, RANDOM_UA.length);
		return RANDOM_UA[i];
	}

	private static String[] getRandomIp()
	{
		int i = RandomUtils.nextInt(0, PROXY_IPS.size());
		String ip = PROXY_IPS.get(i);
		return ip.split(":");
	}

	private static void getIpList() throws IOException
	{
		PROXY_IPS = new ArrayList<>();
		Document doc = Jsoup.connect("http://www.youdaili.net/Daili/http/19733.html")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip").get();

		Elements elements = doc.select("div.content").select("span");
		for (Element ele : elements)
		{
			Pattern p = Pattern.compile("\\d+.\\d+.\\d+.\\d+:\\d+");
			Matcher m = p.matcher(ele.text());
			if (m.find())
			{
				String ip = m.group();
				if (ipTest(ip)) {
					PROXY_IPS.add(ip);
				}
			}
		}
	}

	private static boolean ipTest(String ip)
	{
		Connection conn = Jsoup.connect("https://www.baidu.com").header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Cache-Control", "no-cache").header("Upgrade-Insecure-Requests", "1");
		String[] proxyIp = ip.split(":");
		System.getProperties().setProperty("http.proxyHost", proxyIp[0]);
		System.getProperties().setProperty("http.proxyPort", proxyIp[1]);
		try
		{
			conn
			//.proxy(proxyIp[0], Integer.valueOf(proxyIp[1]))
			.get();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
}
