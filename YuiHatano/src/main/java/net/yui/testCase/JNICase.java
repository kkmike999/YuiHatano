package net.yui.testCase;

import net.yui.testCase.rule.CompileCppRule;

import org.junit.ClassRule;
import org.junit.rules.TestRule;

/**
 * Created by kkmike999 on 2019/06/13.
 * <p>
 * JNI测试用例基类
 */
public class JNICase {

    @ClassRule
    public static TestRule compileRule = new CompileCppRule();

}
