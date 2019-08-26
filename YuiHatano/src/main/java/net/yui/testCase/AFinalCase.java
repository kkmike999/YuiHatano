package net.yui.testCase;

import net.yui.YuiCase;
import net.yui.afinal.FinalDbUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class AFinalCase extends YuiCase {

    @Rule
    public ExternalResource afinalRule = new ExternalResource() {
        @Override
        protected void after() {
            FinalDbUtils.clear();
        }
    };
}
