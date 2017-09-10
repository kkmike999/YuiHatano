package net.yui.testCase;

import net.kb.test.library.KBCase;
import net.yui.dbflow.DbFlowUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * Created by kkmike999 on 2017/06/13.
 * <p>
 * DbFlow Case
 */
public class DbFlowCase extends KBCase {

    @Rule
    public ExternalResource innerRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            DbFlowUtils.destroy();
        }

        @Override
        protected void after() {
            DbFlowUtils.destroy();
        }
    };
}
