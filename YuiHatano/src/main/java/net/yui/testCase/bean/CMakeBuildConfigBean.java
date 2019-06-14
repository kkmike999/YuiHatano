package net.yui.testCase.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kkmike999 on 2019/06/13.
 * <p>
 * .externalNativeBuild/cmake/debug/armeabi-v7a/android_gradle_build.json 解析成的对象
 */
public class CMakeBuildConfigBean {

    Map<String, Library> libraries = new HashMap<>();

    public List<Library> getLibraries() {
        return new ArrayList<>(libraries.values());
    }

    public static class Library {
        String     abi;
        String     artifactName = "";
        String     buildCommand;
        String     buildType;
        List<Bean> files        = new ArrayList<>();

        public String getArtifactName() {
            return artifactName;
        }

        public List<String> getPaths() {
            List<String> paths = new ArrayList<>();

            for (Bean bean : files) {
                paths.add(bean.src);
            }
            return paths;
        }
    }

    private static class Bean {
        String flags;
        String src = "";
        String workingDirectory;
    }
}
