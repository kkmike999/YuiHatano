package com.joyrun.testmodule;

import net.yui.YuiCase;
import net.yui.testCase.rule.DbFlowRule;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by kkmike999 on 2018/07/13.
 */
public class DbFlowTest extends YuiCase {

    @Rule
    public DbFlowRule dbFlowRule = new DbFlowRule(this);

    @Test
    public void test() {
        // 当本module不引用dbflow
        System.out.println();
    }
}
