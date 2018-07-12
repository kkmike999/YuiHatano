package net.yui.testCase;

import net.yui.YuiCase;
import net.yui.testCase.rule.DbFlowRule;

import org.junit.Rule;
import org.junit.rules.TestRule;

/**
 * Created by kkmike999 on 2017/06/13.
 * <p>
 * DbFlow Case
 */
public class DbFlowCase extends YuiCase {

    @Rule
    public TestRule innerRule = new DbFlowRule(this);
}
