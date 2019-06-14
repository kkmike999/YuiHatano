package net.yui.testCase.rule;

import com.google.gson.Gson;

import net.yui.testCase.bean.CMakeBuildConfigBean;
import net.yui.testCase.bean.CMakeBuildConfigBean.Library;
import net.yui.utils.FileReader;
import net.yui.utils.FileWritter;
import net.yui.utils.ShellUtils;
import net.yui.utils.SignatureUtil;
import net.yui.utils.os.OSUtils;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kkmike999 on 2019/06/13.
 * <p>
 * 编译cpp Rule
 */
public class CompileCppRule extends ExternalResource {
    private File mDylibsDir = new File("build/dylibs/");

    /**
     * 编译时间戳
     */
    static Map<String, String> sCompileMD5 = new HashMap<>();

    @Override
    protected void before() throws Throwable {
        if (OSUtils.isMacOS()) {
            startCompile();
        } else {
            throw new RuntimeException("目前仅支持MaxOS");
        }
    }

    /**
     * 开始编译cpp
     */
    public void startCompile() throws NoSuchFieldException, IllegalAccessException {
        File   cmake_debug = new File("./.externalNativeBuild/cmake/debug");
        File[] platforms   = cmake_debug.listFiles();

        if (platforms.length == 0) {
            throw new RuntimeException(cmake_debug.getAbsolutePath() + " no sub files.\n" +
                    "try to File -> Sync Project with Gradle Files."
            );
        }

        File CMakeFiles           = platforms[0];
        File android_gradle_build = new File(CMakeFiles, "android_gradle_build.json");

        if (!android_gradle_build.exists()) {
            throw new RuntimeException(android_gradle_build.getAbsolutePath() + " not exist.");
        }

        CMakeBuildConfigBean config    = new Gson().fromJson(FileReader.read(android_gradle_build), CMakeBuildConfigBean.class);
        List<Library>        libraries = config.getLibraries();

        // 设置环境变量
        addJavaLibraryPath(mDylibsDir.getAbsolutePath());

        for (Library library : libraries) {
            try {
                compile(library.getArtifactName(), library.getPaths());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println();
    }

    /**
     * 设置环境变量
     *
     * @param path 目录
     */
    public void addJavaLibraryPath(String path) throws NoSuchFieldException, IllegalAccessException {
        String libraryPath = new File(path).getAbsolutePath() + ":" + System.getProperty("java.library.path");
        System.setProperty("java.library.path", libraryPath);

        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);
    }

    /**
     * 编译cpp为dylib
     *
     * @param dylibName 动态库名称
     * @param cppPaths  cpp文件路径列表
     */
    public void compile(String dylibName, List<String> cppPaths) throws IOException, InterruptedException {
        String JAVA_HOME = System.getenv("JAVA_HOME");
        if (!new File(JAVA_HOME).exists()) {
            throw new RuntimeException("未配置 JAVA_HOME环境变量");
        }

        File dylibFile = new File(mDylibsDir, "lib" + dylibName + ".dylib");

        String cacheMd5 = sCompileMD5.get(dylibFile.getAbsolutePath());
        String md5      = dylibFile.exists() ? SignatureUtil.fileMD5(dylibFile) : "";

        if (cacheMd5 != null && dylibFile.exists() && cacheMd5.equals(md5)) {
            return;
        }

        List<File> oFiles = new ArrayList<>();

        for (String cppPath : cppPaths) {
            File cppFile = new File(cppPath);
            File oFile   = new File(mDylibsDir, "lib" + cppFile.getName().split("\\.")[0] + ".o");
            oFiles.add(oFile);

            // 删除旧静态库
            oFile.delete();
        }

        // 删除动态库
        dylibFile.delete();

        File shellFile = new File("./build/make_macOS_dylib.sh");
        shellFile.delete();

        StringBuffer cppPathsShell = new StringBuffer();
        StringBuffer oNamesShell   = new StringBuffer();
        for (int i = 0; i < cppPaths.size(); i++) {
            File oFile = new File(cppPaths.get(i));
            cppPathsShell.append("cppPaths[" + i + "]=\"" + oFile.getPath() + "\"\n");
            oNamesShell.append("oNames[" + i + "]=" + oFile.getName().split("\\.")[0] + "\n");
        }

        // shell编译脚本
        String shell = "#!/usr/bin/env bash\n\n" +
                "name=" + dylibName + "\n" +
                "OUTPUT=" + mDylibsDir.getPath() + "\n" +
                "mkdir -p ${OUTPUT}\n\n" +
                cppPathsShell.toString() + "\n" +
                oNamesShell.toString() + "\n\n" +
                "for i in \"${!cppPaths[@]}\";\n" +
                "do\n" +
                "    path=${cppPaths[i]};\n" +
                "    o_name=${oNames[i]};\n" +
                "    echo cpp源文件:$path , 静态库文件 $o_name.o\n" +
                "\n" +
                "    cc -c \\\n" +
                "    -I$JAVA_HOME/include/darwin \\\n" +
                "    -I$JAVA_HOME/include/ \\\n" +
                "    \"${path}\" \\\n" +
                "    -o ${OUTPUT}/lib${o_name}.o\n" +
                "done" +
                "\n\n" +
                "# .dylib file\n" +
                "g++ -dynamiclib -undefined suppress -flat_namespace ${OUTPUT}/*.o -o " + dylibFile.getPath() + "\n" +
                "\n" +
                "echo 生成dylib：" + dylibFile.getPath();

        new FileWritter().writeString(shellFile.getPath(), shell);

        ShellUtils.exec("/bin/sh " + shellFile.getPath());

        shellFile.delete();
        System.out.println("\n-----------\n");

        if (!dylibFile.exists()) {
            throw new RuntimeException("未生成dylib文件");
        }

        // cache
        md5 = SignatureUtil.fileMD5(dylibFile);
        sCompileMD5.put(dylibFile.getAbsolutePath(), md5);
    }
}
