package com.mountain.jsview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;


public class JsInterface<T extends ViewGroup> {
    private static final String TAG = "JsInterface";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private T t;
    private HashMap<Integer, SoftReference<Object>> mObjectSparseArray = new HashMap<>();
    private JsEngine mJsEngine;

    private int contextId;
    private int contentId;
    private int jsInterfaceId;

    public JsInterface(T t) {
        this.t = t;
        contextId = registObj(t.getContext());
        contentId = registObj(t);
        jsInterfaceId = registObj(this);

        mJsEngine = new JsEngine(t.getContext());
//      mWebView.loadData("jsfile", "text/html", null);
//      mWebview.loadUrl("http://");//在线模板

    }


    public JsEngine getJsEngine() {
        return mJsEngine;
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int newJobj(final String classType, final String argsJson) {
//        KLog.d(TAG, classType + "  " + argsJson);
        if (classType != null) {
            try {
                Object o;
                Class<?> aClass = getClassFromType(classType);
                if (argsJson != null && argsJson.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(argsJson);
                    int length = jsonArray.length();
                    Class[] classes = new Class[length];
                    Object[] argValues = new Object[length];
                    extraExecParams(jsonArray, classes, argValues);
                    Constructor<?> constructor = aClass.getConstructor(classes);
                    o = constructor.newInstance(argValues);
                } else {
                    Constructor<?> constructor;
                    constructor = aClass.getConstructor();
                    o = constructor.newInstance();
                }
                return registObj(o);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private Class forClassType(String classId) throws ClassNotFoundException {
        return Class.forName(classId.substring(1, classId.length()).replace("/", "."));
    }


    /**
     * 获取一些通用上下文对象
     */
    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int getObjId(String classId) {
        if ("Landroid/content/Context".equals(classId)) {
            return contextId;
        } else if ("this".equals(classId)) {
            return jsInterfaceId;
        }
        return 0;
    }


    /**
     * 注册对象
     */
    public int registObj(Object object) {
        SoftReference<Object> objectSoftReference = new SoftReference<>(object);
        int hashCode = objectSoftReference.hashCode();
        mObjectSparseArray.put(hashCode, objectSoftReference);
        return hashCode;
    }

    /**
     * 获取一些通用上下文对象
     */
    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int getActivity() {
        return contextId;
    }

    /**
     * 获取一些通用上下文对象
     */
    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int getContentView() {
        return contentId;
    }

    /**
     * 获取一些通用上下文对象
     */
    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public void addView(int viewObjId) {
        SoftReference<Object> objectSoftReference = mObjectSparseArray.get(viewObjId);
        Object o = objectSoftReference.get();
        if (o instanceof View) {
            View view = (View) o;
            t.addView(view);
        }
    }

    private Class getClassFromType(String classType) throws ClassNotFoundException {
        if ("I".equals(classType)) {
            return int.class;
        } else if ("V".equals(classType)) {
            return void.class;
        } else if ("C".equals(classType)) {
            return char.class;
        } else if ("Z".equals(classType)) {
            return boolean.class;
        } else if ("B".equals(classType)) {
            return byte.class;
        } else if ("S".equals(classType)) {
            return short.class;
        } else if ("F".equals(classType)) {
            return float.class;
        } else if ("J".equals(classType)) {
            return long.class;
        } else if ("D".equals(classType)) {
            return double.class;
        }
        return forClassType(classType);
    }

    private Object getPrimitiveValue(String classType, String value) {
        if ("I".equals(classType)) {
            return (int) Long.parseLong(value);
        } else if ("V".equals(classType)) {
            return null;//TODO:
        } else if ("C".equals(classType)) {
            return (char) Long.parseLong(value);
        } else if ("Z".equals(classType)) {
            return Boolean.parseBoolean(value);
        } else if ("B".equals(classType)) {
            return (byte) Long.parseLong(value);
        } else if ("S".equals(classType)) {
            return (short) Long.parseLong(value);
        } else if ("F".equals(classType)) {
            return Float.parseFloat(value);
        } else if ("J".equals(classType)) {
            return Long.parseLong(value);
        } else if ("D".equals(classType)) {
            return Double.parseDouble(value);
        }
        return null;
    }


    private boolean isStringType(Class stringClass) {
        if (stringClass.isPrimitive()) {
            return false;
        }
        try {
            return stringClass == String.class || stringClass == CharSequence.class;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public double exec(int objId, final String methodName, final String argsJson, int thread) {
        SoftReference<Object> objectSoftReference = mObjectSparseArray.get(objId);
        Object object = null;
        if (objectSoftReference != null) {
            object = objectSoftReference.get();
        }
        if (object == null) {
            return 0;
        }
        if (thread == 1) {
            //UIThread
            synchronized (object) {
                try {
                    MainTheadExecRunnable mainTheadExecRunnable = new MainTheadExecRunnable(true, object, methodName, argsJson);
                    mHandler.post(mainTheadExecRunnable);
                    object.wait();
                    return mainTheadExecRunnable.getResult();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } else {
            return exec(true, object, object.getClass(), methodName, argsJson);
        }
        return 0;
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public double set(int objId, final String fieldName, final String argsJson, int thread) {
        SoftReference<Object> objectSoftReference = mObjectSparseArray.get(objId);
        Object object = null;
        if (objectSoftReference != null) {
            object = objectSoftReference.get();
        }
        if (object == null) {
            return 0;
        }
        if (thread == 1) {
            //UIThread
            synchronized (object) {
                try {
                    MainTheadExecRunnable mainTheadExecRunnable = new MainTheadExecRunnable(false, object, fieldName, argsJson);
                    mHandler.post(mainTheadExecRunnable);
                    object.wait();
                    return mainTheadExecRunnable.getResult();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } else {
            return exec(false, object, object.getClass(), fieldName, argsJson);
        }
        return 0;
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public double staticExec(String classId, final String methodName, final String argsJson, int thread) {
        try {
            Class classFromType = getClassFromType(classId);
            if (thread == 1) {
                //UIThread
                synchronized (classFromType) {
                    try {
                        MainTheadExecStaticRunnable mainTheadExecRunnable = new MainTheadExecStaticRunnable(true, classFromType, methodName,
                                argsJson);
                        mHandler.post(mainTheadExecRunnable);
                        classFromType.wait();
                        return mainTheadExecRunnable.getResult();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                return exec(true, null, classFromType, methodName, argsJson);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param isMethod 是否是方法调用
     * @param object 调用的对象
     * @param execObjClazz 调用的class
     * @param name 对应的名字（方法名，或者字段名字）
     * @param argsJson 参数
     *
     * return 结果的软引用
     */
    private double exec(boolean isMethod, Object object, Class execObjClazz, final String name, final String argsJson) {
//        KLog.d(TAG, isMethod, object, execObjClazz, name, argsJson);
        try {
            if (argsJson != null) {
                JSONArray jsonArray = new JSONArray(argsJson);
                int length = jsonArray.length();
                Class[] classes = new Class[length];
                Object[] argValues = new Object[length];
                extraExecParams(jsonArray, classes, argValues);
                if (isMethod) {
                    Method method = execObjClazz.getMethod(name, classes);
                    method.setAccessible(true);
                    Object invoke = method.invoke(object, argValues);
                    method.setAccessible(false);
                    if (invoke != null) {
                        if (!invoke.getClass().isPrimitive()) {
                            return registObj(invoke);
                        } else {
                            try {
                                return (double) invoke;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    Field field = execObjClazz.getField(name);
                    if (field != null) {
                        field.set(object, argValues[0]);
                    }
                }
            } else {
                Method method = execObjClazz.getMethod(name);
                Object invoke = method.invoke(object);
                method.setAccessible(false);
                if (invoke != null) {
                    return registObj(invoke);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private void extraExecParams(JSONArray jsonArray, Class[] classes, Object[] argValues) throws ClassNotFoundException {
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String argType = keys.next();
                Class clazz = getClassFromType(argType);
                classes[i] = clazz;
                String argValue = jsonObject.optString(argType);
                if (isStringType(clazz)) {
                    argValues[i] = argValue;
                } else {
                    if (clazz.isPrimitive()) {
                        argValues[i] = getPrimitiveValue(argType, argValue);
                    } else {
                        int objHash = Integer.parseInt(argValue);
                        SoftReference<Object> objectSoftReference = mObjectSparseArray.get(objHash);
                        if (objectSoftReference != null) {
                            argValues[i] = objectSoftReference.get();
                        } else {
                            if (clazz == Object.class) {
                                argValues[i] = argValue;
                            }
                        }
                    }
                }
            }
        }
    }

    private class MainTheadExecRunnable implements Runnable {
        Object mObject;
        String mMethod;
        String mArgsJson;
        double result;
        boolean isMethod;

        public MainTheadExecRunnable(boolean isMethod, Object object, final String methodName, final String argsJson) {
            setObject(isMethod, object, methodName, argsJson);
        }

        public void setObject(boolean isMethod, Object object, final String methodName, final String argsJson) {
            this.isMethod = isMethod;
            this.mObject = object;
            this.mMethod = methodName;
            this.mArgsJson = argsJson;
        }

        public double getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (mObject) {
                result = exec(isMethod, mObject, mObject.getClass(), mMethod, mArgsJson);
                mObject.notify();
            }
        }
    }

    private class MainTheadExecStaticRunnable implements Runnable {
        Class mClassType;
        String mMethod;
        String mArgsJson;
        double result;
        boolean isMethod;

        public MainTheadExecStaticRunnable(boolean isMethod, Class classType, final String methodName, final String argsJson) {
            setObject(isMethod, classType, methodName, argsJson);
        }

        public void setObject(boolean isMethod, Class classType, final String methodName, final String argsJson) {
            this.mClassType = classType;
            this.mMethod = methodName;
            this.mArgsJson = argsJson;
            this.isMethod = isMethod;
        }

        public double getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (mClassType) {
                result = exec(isMethod, null, mClassType, mMethod, mArgsJson);
                mClassType.notify();
            }
        }
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public void showToast(final String json) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(t.getContext(), json, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
