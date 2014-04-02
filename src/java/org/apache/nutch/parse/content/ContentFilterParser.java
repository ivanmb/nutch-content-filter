package org.apache.nutch.parse.content;

//JDK imports
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

//Nutch imports
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;

//Commons imports
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//W3C imports
import org.w3c.dom.DocumentFragment;

public class ContentFilterParser implements HtmlParseFilter {

	private static final Log LOG = LogFactory.getLog(ContentFilterParser.class
			.getName());

	private Configuration conf;

	/** The Content meta data filter attribute name */
	private static final String FILTER_CONTENT_PROP = "content.filter.words";
	private static final String SEPARATOR = ","; 

	public ContentFilterParser() {
		
	}
	
	/**
	 * Scan the HTML document looking for the desired content
	 */
	public ParseResult filter(Content content, ParseResult parseResult,
			HTMLMetaTags metaTags, DocumentFragment doc) {

		String url = content.getUrl();
		Parse parse = parseResult.get(url);
		String pageText = parse.getData().toString();
		
		String filters = this.conf.get(FILTER_CONTENT_PROP);
		boolean containsAny = false;
		
		for(String filter: filters.split(SEPARATOR)) {
			if (StringUtils.containsIgnoreCase(pageText, filter)) {
				LOG.debug(String.format("Desired content found on url %s", url));
				containsAny = true;
			}
		}
		
		return containsAny ? parseResult : null;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public Configuration getConf() {
		return this.conf;
	}
}
