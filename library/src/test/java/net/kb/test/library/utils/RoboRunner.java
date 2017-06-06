package net.kb.test.library.utils;

import android.os.Build;

import org.junit.runners.model.InitializationError;
import org.robolectric.RoboSettings;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kkmike999 on 2016/05/26.
 */
public class RoboRunner extends RobolectricTestRunner {

    private static final int TARGET_SDK_VERSION = Build.VERSION_CODES.LOLLIPOP;
    private static final int MIN_SDK_VERSION    = Build.VERSION_CODES.JELLY_BEAN;

    public RoboRunner(Class<?> klass) throws InitializationError {
        super(klass);

        RoboSettings.setMavenRepositoryUrl("http://maven.aliyun.com/nexus/content/groups/public/");
        RoboSettings.setMavenRepositoryId("alimaven");
    }

//    @Override
//    protected AndroidManifest getAppManifest(Config config) {
//        // 处理Manifest目录. 参考 http://stackoverflow.com/questions/35709151/robolectric-cannot-find-androidmanifest-xml
//
//        final String projectDirectory = System.getProperty("user.dir");
//
//        final String manifestPath       = String.format("%s/build/intermediates/manifest/androidTest/debug/AndroidManifest.xml", projectDirectory);
//        final String manifestPathBackup = String.format("%s/build/intermediates/manifests/aapt/release/AndroidManifest.xml", projectDirectory);
//        final String resourcesPath      = String.format("%s/src/main/res", projectDirectory);
//        final String assetsPath         = String.format("%s/src/main/assets", projectDirectory);
//
//        FsFile androidManifestFile = new File(manifestPath).exists() ? Fs.fileFromPath(manifestPath) : Fs.fileFromPath(manifestPathBackup);
//
//        AndroidManifest manifest = new AndroidManifest(
//                androidManifestFile,
//                Fs.fileFromPath(resourcesPath),
//                Fs.fileFromPath(assetsPath)) {
//            @Override
//            public int getTargetSdkVersion() {
//                return TARGET_SDK_VERSION;
//            }
//
//            @Override
//            public int getMinSdkVersion() {
//                return MIN_SDK_VERSION;
//            }
//        };
//
//        return manifest;
//    }

}
