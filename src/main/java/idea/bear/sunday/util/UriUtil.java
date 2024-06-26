package idea.bear.sunday.util;

import com.damnhandy.uri.template.MalformedUriTemplateException;
import com.damnhandy.uri.template.UriTemplateComponent;
import com.damnhandy.uri.template.impl.UriTemplateParser;
import com.intellij.openapi.diagnostic.Logger;

import java.util.LinkedList;

public class UriUtil {

    private static final Logger logger = Logger.getInstance(UriUtil.class);

    public static String getUriValue(String uri) {
        UriTemplateParser uriTemplateParser = new UriTemplateParser();
        String value = "";

        try {
            LinkedList<UriTemplateComponent> list = uriTemplateParser.scan(uri);
            if (!list.isEmpty() && list.get(0) != null) {
                value = list.get(0).getValue();
            }
        } catch (MalformedUriTemplateException me) {
            logger.error("MalformedUriTemplateException encountered: ", me);
        }

        return value;
    }
}
