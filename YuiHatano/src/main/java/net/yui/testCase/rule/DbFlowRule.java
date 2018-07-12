package net.yui.testCase.rule;

import net.yui.YuiCase;
import net.yui.dbflow.DbFlowUtils;

import org.junit.rules.ExternalResource;

/**
 * Created by kkmike999 on 2018/07/12.
 */
public class DbFlowRule extends ExternalResource {

    YuiCase mYuiCase;

    public DbFlowRule(YuiCase yuiCase) {
        this.mYuiCase = yuiCase;
    }

    @Override
    protected void before() throws Throwable {
        DbFlowUtils.destroy();
        DbFlowUtils.init(mYuiCase.getContext());
    }

    @Override
    protected void after() {
        DbFlowUtils.destroy();
    }
}
