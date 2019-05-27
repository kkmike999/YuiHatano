package android.content.res;

import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.yui.R;
import net.yui.utils.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String R_COLOR  = "color";
    private static final String R_ARRAY  = "array";

    private String mPackageName = "";

    ResourceCache mResourceCache;

    private Document mValuesDocument;

    public ShadowResources() {
        this.mPackageName = getPackageName();
        this.mResourceCache = new ResourceCache();
    }

    public int getColor(@ColorRes int id) throws NotFoundException {
        return getColor(id, null);
    }

    public int getColor(@ColorRes int id, @Nullable Resources.Theme theme) throws NotFoundException {
        Map<Integer, String> colorValues = getResIdValueMap(R_COLOR);
        String               colorHex    = colorValues.get(id);

        if (colorHex != null) {
            return Color.parseColor(colorHex);
        }
        //        throw new Resources.NotFoundException("Color resource ID #0x" + Integer.toHexString(id));
        return 0;
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
        Map<Integer, String>      idNameMap      = getIdTable(R_ARRAY);
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
        Map<Integer, String>       idNameMap   = getIdTable(R_ARRAY);
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
        Map<Integer, String> idValueMap = getResIdValueMap(R_STRING);
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

        String manifestPath = null;

        // AndroidManifest.xml 可能目录（不同gradle版本，不同目录，蛋疼）
        List<String> manifestPaths = Arrays.asList(
                "build/intermediates/manifests/aapt/debug/AndroidManifest.xml",
                "build/intermediates/manifests/aapt/release/AndroidManifest.xml",
                "build/intermediates/manifests/aapt/full/release/AndroidManifest.xml",
                "build/intermediates/manifests/full/debug/AndroidManifest.xml",
                "build/intermediates/aapt_friendly_merged_manifests/debug/processDebugManifest/aapt/AndroidManifest.xml",
                "build/intermediates/aapt_friendly_merged_manifests/release/processDebugManifest/aapt/AndroidManifest.xml",
                "build/intermediates/merged_manifests/debug/processDebugManifest/merged/AndroidManifest.xml",
                "build/intermediates/merged_manifests/release/processDebugManifest/merged/AndroidManifest.xml",
                "build/intermediates/merged_manifests/debug/AndroidManifest.xml",
                "build/intermediates/library_manifest/debug/AndroidManifest.xml",
                "build/intermediates/aapt_friendly_merged_manifests/debug/processDebugManifest/aapt/AndroidManifest.xml"
        );

        manifestPath = findExistPath(manifestPaths);

        if (TextUtils.isEmpty(manifestPath)) {
            manifestPath = findPath("AndroidManifest.xml", "build/intermediates");
        }

        if (TextUtils.isEmpty(manifestPath)) {
            throw new RuntimeException("没有找到AndroidManifest.xml");
        }

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

    //////////////////////////////////////////////////////////////////

    /**
     * 获取 资源id-值 映射表
     *
     * @param resName 颜色{@linkplain #R_COLOR}，字符串{@linkplain #R_STRING}，字符串数组{@linkplain #R_ARRAY}
     *
     * @return
     */
    protected Map<Integer, String> getResIdValueMap(String resName) {
        Map<Integer, String> idTable      = getIdTable(resName);
        Map<String, String>  nameValueMap = getResNameAndValueMap(resName);

        Map<Integer, String> idValueMap = new HashMap<>();
        Set<Integer>         ids        = idTable.keySet();

        for (Integer id : ids) {
            String _resName = idTable.get(id);

            if (nameValueMap.containsKey(_resName)) {
                String value = nameValueMap.get(_resName);

                idValueMap.put(id, value);
            }
        }

        return idValueMap;
    }

    /**
     * 获取{@linkplain R.string}、{@linkplain R.color}、{@linkplain R.array}等 "资源id-资源名"映射表
     *
     * @param resName 颜色{@linkplain #R_COLOR}，字符串{@linkplain #R_STRING}，字符串数组{@linkplain #R_ARRAY}
     *
     * @return id - string name 映射表
     */
    protected Map<Integer, String> getIdTable(@RTypes String resName) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 获取colors.xml、strings.xml等 资源名-值 映射表
     *
     * @param resName 资源名：颜色{@linkplain #R_COLOR}，字符串{@linkplain #R_STRING}，字符串数组{@linkplain #R_ARRAY}
     *
     * @return
     */
    protected Map<String, String> getResNameAndValueMap(String resName) {
        Map<String, String> map = new HashMap<>();

        Document document = getValuesXmlDocument();
        Elements colors   = document.getElementsByTag(resName);

        for (int i = 0; i < colors.size(); i++) {
            Element element = colors.get(i);
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
        String path = findExistPath(Arrays.asList(
                "build/intermediates/res/merged/debug/values/values.xml",
                "build/intermediates/packaged_res/debug/values/values.xml",
                "build/intermediates/incremental/mergeDebugResources/merged.dir/values/values.xml",
                "build/intermediates/incremental/packageDebugResources/merged.dir/values/values.xml"));

        if (TextUtils.isEmpty(path)) {
            path = findPath("values.xml", "build/intermediates");
        }

        String absolutePath = new File(path).getAbsolutePath();

        if (mResourceCache.hasValuesDocumentCache(absolutePath)) {
            return mResourceCache.getValuesDocumentCache(absolutePath);
        }
        String   content  = new FileReader().read(new File(path));
        Document document = Jsoup.parse(content);

        mResourceCache.cacheValuesDocument(absolutePath, document);

        return document;
    }

    @StringDef({R_STRING, R_ARRAY, R_COLOR})
    @interface RTypes {}

    /**
     * 找到文件存在的路径
     *
     * @param paths
     *
     * @return
     */
    private String findExistPath(List<String> paths) {
        for (String path : paths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return "";
    }

    private String findPath(String name, String dir) {
        File file = findFile(name, dir);

        if (file.exists()) {
            return file.getPath();
        }
        return "";
    }

    private File findFile(String name, String dir) {
        File[] files = new File(dir).listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                File f = findFile(name, file.getPath());
                if (f != null) {
                    return f;
                }
                continue;
            }
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }
}
