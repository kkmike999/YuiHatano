package net.kb.test.library;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.Resources;
import android.content.res.ShadowResources;

import net.kb.test.library.utils.ReflectUtils;
import net.kkmike.sptest.ShadowSharedPreference;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kkmike999 on 2017/05/25.
 */
public class KBSharedPrefCase {

    private Context mContext;

    @Rule
    public ExternalResource contextRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            Context context = mock(Context.class);

            ShadowResources resources     = new ShadowResources();
            ShadowContext   shadowContext = new ShadowContext(resources);

            // resources
            Answer resourceAnswer = new JRAnswer(resources);

            Resources mockResource = mock(Resources.class);
            doAnswer(resourceAnswer).when(mockResource).getString(anyInt());

            // context
            Answer contextAnswer = new JRAnswer(shadowContext);
            doAnswer(contextAnswer).when(context).getString(anyInt());
            when(context.getApplicationContext()).thenReturn(context);
            when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(new ShadowSharedPreference());

            mContext = context;
        }
    };

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

    public Context getContext() {
        return mContext;
    }
}
