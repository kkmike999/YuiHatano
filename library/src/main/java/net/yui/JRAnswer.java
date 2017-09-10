package net.yui;

import net.yui.utils.ReflectUtils;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

class JRAnswer implements Answer {

    Object realObject;

    public JRAnswer(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments  = invocation.getArguments();
        Method   method     = invocation.getMethod();
        Class[]  paramTypes = method.getParameterTypes();

        // 找到resource一模一样的方法
        Method method2 = ReflectUtils.findMethod(realObject.getClass(), method.getName(), paramTypes);

        return method2.invoke(realObject, arguments);
    }
}