package net.yui.testCase;

import net.kb.test.library.KBCase;
import net.yui.xutils.XUtilsDbUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;


/**
 * Created by kkmike999 on 2017/06/13.
 */
public class XUtilsCase extends KBCase {

    @Rule
    public ExternalResource xutilsRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            System.out.println("XUtilsCase before");
            XUtilsDbUtils.setUp();
            XUtilsDbUtils.init(getApplication());
        }

        @Override
        protected void after() {
            System.out.println("XUtilsCase after");
            XUtilsDbUtils.clearAndCloseAndDrop();
        }
    };
}
