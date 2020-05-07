package com.taogen.hotcrawler.commons.crawler.impl.stream;

import com.taogen.hotcrawler.commons.config.SiteProperties;
import com.taogen.hotcrawler.commons.constant.RequestMethod;
import com.taogen.hotcrawler.commons.crawler.SimpleDocumentHotProcessor;
import com.taogen.hotcrawler.commons.entity.Info;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("BilibiliHotProcessor")
public class BilibiliHotProcessor extends SimpleDocumentHotProcessor
{
    private static final String ITEM_KEY = "info";

    @Autowired
    private SiteProperties siteProperties;

    @Autowired
    private ApplicationContext context;

    @Override
    @PostConstruct
    protected void initialize(){
        RequestMethod requestMethod = RequestMethod.GET;
        setFieldsByProperties(siteProperties, requestMethod, generateHeader(),generateRequestBody());
        injectBeansByContext(context);
        setLog(LoggerFactory.getLogger(getClass()));
        this.elementClass = ITEM_KEY;
    }

    @Override
    protected Info getInfoByElement(Element element) {
        Elements elements1 = element.getElementsByTag("a");
        String infoTitle = elements1.html();
        StringBuilder infoUrl = new StringBuilder();
//        infoUrl.append(this.prefix);
        infoUrl.append(elements1.attr("href"));
        String url = infoUrl.toString(); //.substring(0, infoUrl.indexOf("#"));
        Info info = new Info();
        info.setTitle(infoTitle);
        info.setUrl(url);
        return info;
    }
}
