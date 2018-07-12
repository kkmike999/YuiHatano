package android.shadow;

/**
 * Created by kkmike999 on 2017/06/12.
 * <p>
 * Shadow类继承此接口，例子：
 * <p>
 * public class ShadowContext implements Shadow {...}
 */
public interface Shadow {

    /**
     * 把proxy对象传递给Shadow
     *
     * @param proxyObject
     */
    void setProxyObject(Object proxyObject);
}
