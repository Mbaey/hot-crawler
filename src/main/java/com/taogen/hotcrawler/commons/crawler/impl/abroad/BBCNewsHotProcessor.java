package com.taogen.hotcrawler.commons.crawler.impl.abroad;

import com.taogen.hotcrawler.commons.config.SiteProperties;
import com.taogen.hotcrawler.commons.constant.RequestMethod;
import com.taogen.hotcrawler.commons.crawler.DocumentHotProcessor;
import com.taogen.hotcrawler.commons.entity.Info;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component("BBCNewsHotProcessor")
public class BBCNewsHotProcessor extends DocumentHotProcessor
{
    public static final String ITEM_KEY = "gs-c-promo-heading";

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
    }

    @Override
    protected Elements getElements(Document document) {
        return document.getElementsByClass("nw-c-top-stories--standard").get(0).getElementsByClass(ITEM_KEY);
    }

    @Override
    protected List<Info> getInfoDataByElements(Elements elements) {
        List<Info> list = new ArrayList<>();
        if (elements != null) {
            int i = 0;
            for (Element element : elements) {
                try {
                    String infoTitle = element.getElementsByClass("gs-c-promo-heading__title").html();
                    StringBuilder infoUrl = new StringBuilder();
                    infoUrl.append(this.prefix);
                    infoUrl.append(element.attr("href"));
                    String id = String.valueOf(++i);
                    list.add(new Info(id, infoTitle, infoUrl.toString()));
                } catch (IndexOutOfBoundsException e) {
                    log.error("Can't find attribute!", e);
                }
            }
        }
        return list;
    }
}
