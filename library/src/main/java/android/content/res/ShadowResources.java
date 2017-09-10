package android.content.res;

import android.content.res.Resources.NotFoundException;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.yui.utils.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kkmike999 on 2017/05/26.
 * <p>
 * {@link Resources} Shadow类
 */
public class ShadowResources {

    private static final String R_STRING = "string";
    private static final String R_ARRAY  = "array";

    private String mPackageName = "";

    ResourceCache mResourceCache;

    private Document mValuesDocument;

    public ShadowResources() {
        this.mPackageName = getPackageName();
        this.mResourceCache = new ResourceCache();
    }

    public String getString(@StringRes int id) throws NotFoundException {
        return getText(id).toString();
    }

    @NonNull
    protected CharSequence getText(@StringRes int id) throws NotFoundException {
        CharSequence res = getResourceText(id);//  mResourcesImpl.getAssets().getResourceText(id);
        if (res != null) {
            return res;
        }
        throw new Resources.NotFoundException("String resource ID #0x" + Integer.toHexString(id));
    }

    @NonNull
    public String[] getStringArray(@ArrayRes int id) throws NotFoundException {
        Map<Integer, String>      idNameMap      = getArrayIdTable();
        Map<String, List<String>> stringArrayMap = getResourceStringArrayMap();

        if (idNameMap.containsKey(id)) {
            String name = idNameMap.get(id);

            if (stringArrayMap.containsKey(name)) {
                List<String> stringList = stringArrayMap.get(name);

                return stringList.toArray(new String[0]);
            }
        }
        throw new Resources.NotFoundException("String array resource ID #0x" + Integer.toHexString(id));
    }

    @NonNull
    public int[] getIntArray(@ArrayRes int id) throws NotFoundException {
        Map<Integer, String>       idNameMap   = getArrayIdTable();
        Map<String, List<Integer>> intArrayMap = getResourceIntArrayMap();

        if (idNameMap.containsKey(id)) {
            String name = idNameMap.get(id);

            if (intArrayMap.containsKey(name)) {
                List<Integer> intList  = intArrayMap.get(name);
                int[]         intArray = new int[intList.size()];

                for (int i = 0; i < intList.size(); i++) {
                    intArray[i] = intList.get(i);
                }

                return intArray;
            }
        }
        return new int[0];
    }

    private CharSequence getResourceText(@StringRes int id) {
        Map<Integer, String> idValueMap = getStringResIdValueMap();
        return idValueMap.get(id);
    }

    protected Map<String, List<String>> getResourceStringArrayMap() {
        Map<String, List<String>> arrayMap = getResourceArrayMap("string-array");
        return arrayMap;
    }

    /**
     * 获取integer-array "数组名-items"表
     *
     * @return
     */
    protected Map<String, List<Integer>> getResourceIntArrayMap() {
        Map<String, List<String>>  arrayMap    = getResourceArrayMap("integer-array");
        Map<String, List<Integer>> intArrayMap = new HashMap<>();

        Set<String> keys = arrayMap.keySet();

        for (String key : keys) {
            List<Integer> integerList = new ArrayList<>();
            List<String>  stringList  = arrayMap.get(key);

            for (String item : stringList) {
                integerList.add(Integer.valueOf(item));
            }

            intArrayMap.put(key, integerList);
        }
        return intArrayMap;
    }

    protected Map<String, List<String>> getResourceArrayMap(String tag) {
        Map<String, List<String>> map = new HashMap<>();

        Document document = getValuesXmlDocument();
        Elements elements = document.getElementsByTag(tag);

        for (Element element : elements) {
            Elements items = element.getElementsByTag("item");

            List<String> itemsText = new ArrayList<>();

            for (Element item : items) {
                String text = ((TextNode) item.childNode(0)).text();

                itemsText.add(text);
            }

            String name = element.attr("name");
            map.put(name, itemsText);
        }
        return map;
    }

    public String getPackageName() {
        if (!TextUtils.isEmpty(mPackageName)) {
            return mPackageName;
        }

        String manifestPath = "build/intermediates/manifests/aapt/debug/AndroidManifest.xml";

        FileReader reader = new FileReader();

        if (new File(manifestPath).exists()) {
            String  manifest    = reader.readString(manifestPath);
            Element body        = Jsoup.parse(manifest).body();
            String  packageName = body.childNode(0).attr("package");

            return mPackageName = packageName;
        } else {
            // 使用androidTest的manifest.xml
            manifestPath = "build/intermediates/manifest/androidTest/debug/AndroidManifest.xml";

            String  manifest    = reader.readString(manifestPath);
            Element body        = Jsoup.parse(manifest).body();
            String  packageName = body.childNode(0).attr("package");

            // androidTest包的package，后面会多了个".test"
            packageName = packageName.substring(0, packageName.length() - ".test".length());

            return mPackageName = packageName;
        }
    }

    /**
     * 获取 资源id-值 映射表
     *
     * @return
     */
    protected Map<Integer, String> getStringResIdValueMap() {
        Map<Integer, String> idTable      = getStringIdTable();
        Map<String, String>  nameValueMap = getStringResNameAndValueMap();

        Map<Integer, String> idValueMap = new HashMap<>();
        Set<Integer>         ids        = idTable.keySet();

        for (Integer id : ids) {
            String resName = idTable.get(id);

            if (nameValueMap.containsKey(resName)) {
                String value = nameValueMap.get(resName);

                idValueMap.put(id, value);
            }
        }

        return idValueMap;
    }

    /**
     * 获取strings.xml 资源名-值 映射表
     *
     * @return
     */
    protected Map<String, String> getStringResNameAndValueMap() {
        Map<String, String> map = new HashMap<>();

        Document document = getValuesXmlDocument();
        Elements strings  = document.getElementsByTag("string");

        for (int i = 0; i < strings.size(); i++) {
            Element element = strings.get(i);
            String  name    = element.attr("name");

            if (element.childNodeSize() > 0 && element.childNode(0) instanceof TextNode) {
                String text = ((TextNode) element.childNode(0)).text();

                map.put(name, text);
            }
        }

        return map;
    }

    /**
     * 解析values.xml
     *
     * @return
     */
    private Document getValuesXmlDocument() {
        String path         = "build/intermediates/res/merged/debug/values/values.xml";
        String absolutePath = new File(path).getAbsolutePath();

        if (mResourceCache.hasValuesDocumentCache(absolutePath)) {
            return mResourceCache.getValuesDocumentCache(absolutePath);
        }
        String   content  = new FileReader().read(new File(path));
        Document document = Jsoup.parse(content);

        mResourceCache.cacheValuesDocument(absolutePath, document);

        return document;
    }

    /**
     * 获取{@linkplain R.string}"资源id-资源名"映射表
     *
     * @return id - string name 映射表
     */
    protected Map<Integer, String> getStringIdTable() {
        try {
            return getIdTable(R_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 获取{@linkplain R.array}"资源id-资源名"映射表
     *
     * @return id - string name 映射表
     */
    protected Map<Integer, String> getArrayIdTable() {
        try {
            return getIdTable(R_ARRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    protected Map<Integer, String> getIdTable(@RTypes String resName) throws IllegalAccessException, ClassNotFoundException {
        // R资源类全称
        String rClassName = mPackageName + ".R$" + resName;

        if (mResourceCache.hasIdTableCache(rClassName)) {
            return mResourceCache.getIdTableCache(rClassName);
        }

        Class                stringRClass = Class.forName(rClassName);
        Field[]              fields       = stringRClass.getDeclaredFields();
        Map<Integer, String> idTable      = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);

            int    id   = (int) field.get(null);
            String name = field.getName();

            idTable.put(id, name);
        }

        mResourceCache.cacheIdTable(rClassName, idTable);

        return idTable;
    }

    @StringDef({R_STRING, R_ARRAY})
    @interface RTypes {}
}
