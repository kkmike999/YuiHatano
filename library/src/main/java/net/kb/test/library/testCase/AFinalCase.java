package net.kb.test.library.testCase;

import net.kb.test.library.KBSharedPrefCase;
import net.kb.test.library.afinal.FinalDbUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class AFinalCase extends KBSharedPrefCase {

    @Rule
    public ExternalResource afinalRule = new ExternalResource() {
        @Override
        protected void after() {
            FinalDbUtils.clear();
        }
    };
}
