package android.content.res;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/05/27.
 */
public class ResourceCache {

    static Map<String, Map<Integer, String>> sResMap              = new HashMap<>();
    static Map<String, Document>             sValuesDocumentCache = new HashMap<>();

    protected Map<Integer, String> getIdTableCache(String rClassName) throws IllegalAccessException, ClassNotFoundException {
        return sResMap.get(rClassName);
    }

    protected void cacheIdTable(String rClassName, Map<Integer, String> map) {
        sResMap.put(rClassName, map);
    }

    protected boolean hasIdTableCache(String rClassName) {
        return sResMap.containsKey(rClassName);
    }

    /**
     * @param absolutePath 绝对路径
     * @return
     */
    protected Document getValuesDocumentCache(String absolutePath) {
        return sValuesDocumentCache.get(absolutePath);
    }

    /**
     * @param absolutePath 绝对路径
     * @param document
     */
    protected void cacheValuesDocument(String absolutePath, Document document) {
        sValuesDocumentCache.put(absolutePath, document);
    }

    /**
     * @param absolutePath 绝对路径
     * @return
     */
    protected boolean hasValuesDocumentCache(String absolutePath) {
        return sValuesDocumentCache.containsKey(absolutePath);
    }

    protected void clearCache(){
        sResMap.clear();
        sValuesDocumentCache.clear();
    }
}
