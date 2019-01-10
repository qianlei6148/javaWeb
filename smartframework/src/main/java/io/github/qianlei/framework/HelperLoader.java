package io.github.qianlei.framework;

import io.github.qianlei.framework.helper.BeanHelper;
import io.github.qianlei.framework.helper.ClassHelper;
import io.github.qianlei.framework.helper.ControllerHelper;
import io.github.qianlei.framework.helper.IocHelper;
import io.github.qianlei.framework.util.ClassUtil;

/**
 * 加载相对应得Helper类
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                ControllerHelper.class,
                IocHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(),false);
        }
    }
}
