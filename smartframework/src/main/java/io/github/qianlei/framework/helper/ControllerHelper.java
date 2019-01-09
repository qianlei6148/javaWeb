package io.github.qianlei.framework.helper;

import io.github.qianlei.framework.annotation.Action;
import io.github.qianlei.framework.bean.Handler;
import io.github.qianlei.framework.bean.Request;
import io.github.qianlei.framework.util.ArrayUtil;
import io.github.qianlei.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 */
public final class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系(Action Map)
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        //获取应用包中所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            //遍历这些Controler类
            for (Class<?> controllerClass : controllerClassSet) {
                //获取Controller类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //判断当前方法是否带有Action注解
                        if (method.isAnnotationPresent(Action.class)) {
                            //从Action注解中获取url映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //验证url映射规则
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    //获取请求的方法和请求路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    //初始化
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
