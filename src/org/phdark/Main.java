package org.phdark;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main
{

	public static final String SAVE_PATH = "/Users/phoenixdark/Downloads/";
	
	public static void main(String[] args) throws IOException
	{
		Document doc = Jsoup.connect("http://1024.91lulea.click/pw/thread.php?fid=22&page=4").header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip").get();
		//System.out.println(doc.html());
		Elements links = doc.select("tbody[style*='table-layout:fixed;']").select("a");
		for (Element link : links) {
			System.out.println(link.attr("abs:href") + "\t" + link.text());
		}
	}

}
