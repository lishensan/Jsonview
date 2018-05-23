package com.mountain.JsView;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;


public class JavaScriptInterface<T extends View> {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private T t;
    private SparseArray<SoftReference<Object>> mObjectSparseArray = new SparseArray<>();


    public JavaScriptInterface(T t) {
        this.t = t;
        mObjectSparseArray.put(t.getContext().hashCode(), new SoftReference<Object>(t.getContext()));
        mObjectSparseArray.put(t.hashCode(), new SoftReference<Object>(t));
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int newJobj(final String classType, final String argsJson) {
        if (classType != null) {
            try {
                Object o;
                Class<?> aClass = getClassFromType(classType);
                if (argsJson != null) {
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
                int hashCode = o.hashCode();
                mObjectSparseArray.put(hashCode, new SoftReference<>(o));
                return hashCode;
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
            return t.getContext().hashCode();
        }
        return 0;
    }

    /**
     * 获取一些通用上下文对象
     */
    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int getActivity() {
        return t.getContext().hashCode();
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
    public int exec(int objId, final String methodName, final String argsJson, int thread) {
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
                    MainTheadExecRunnable mainTheadExecRunnable = new MainTheadExecRunnable(object, methodName, argsJson);
                    mHandler.post(mainTheadExecRunnable);
                    object.wait();
                    return mainTheadExecRunnable.getResult();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } else {
            return exec(object, object.getClass(), methodName, argsJson);
        }
        return 0;
    }

    @JavascriptInterface //android4.2之后，如果不加上该注解，js无法调用android方法（安全）
    public int staticExec(String classId, final String methodName, final String argsJson, int thread) {
        try {
            Class classFromType = getClassFromType(classId);
            if (thread == 1) {
                //UIThread
                synchronized (classFromType) {
                    try {
                        MainTheadExecStaticRunnable mainTheadExecRunnable = new MainTheadExecStaticRunnable(classFromType, methodName,
                                argsJson);
                        mHandler.post(mainTheadExecRunnable);
                        classFromType.wait();
                        return mainTheadExecRunnable.getResult();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                return exec(null, classFromType, methodName, argsJson);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int exec(Object object, Class execObjClazz, final String methodName, final String argsJson) {
        try {
            if (argsJson != null) {
                JSONArray jsonArray = new JSONArray(argsJson);
                int length = jsonArray.length();
                Class[] classes = new Class[length];
                Object[] argValues = new Object[length];
                extraExecParams(jsonArray, classes, argValues);
                Method method = execObjClazz.getMethod(methodName, classes);
                method.setAccessible(true);
                Object invoke = method.invoke(object, argValues);
                method.setAccessible(false);
                if (invoke != null) {
                    int returnObjId = invoke.hashCode();
                    mObjectSparseArray.put(returnObjId, new SoftReference<Object>(invoke));
                    return returnObjId;
                }
            } else {
                Method method = execObjClazz.getMethod(methodName);
                Object invoke = method.invoke(object);
                method.setAccessible(false);
                if (invoke != null) {
                    int returnObjId = invoke.hashCode();
                    mObjectSparseArray.put(returnObjId, new SoftReference<Object>(invoke));
                    return returnObjId;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
                        argValues[i] = mObjectSparseArray.get(objHash).get();
                    }
                }
            }
        }
    }

    private class MainTheadExecRunnable implements Runnable {
        Object mObject;
        String mMethod;
        String mArgsJson;
        int result;

        public MainTheadExecRunnable(Object object, final String methodName, final String argsJson) {
            setObject(object, methodName, argsJson);
        }

        public void setObject(Object object, final String methodName, final String argsJson) {
            this.mObject = object;
            this.mMethod = methodName;
            this.mArgsJson = argsJson;
        }

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (mObject) {
                result = exec(mObject, mObject.getClass(), mMethod, mArgsJson);
                mObject.notify();
            }
        }
    }

    private class MainTheadExecStaticRunnable implements Runnable {
        Class mClassType;
        String mMethod;
        String mArgsJson;
        int result;

        public MainTheadExecStaticRunnable(Class classType, final String methodName, final String argsJson) {
            setObject(classType, methodName, argsJson);
        }

        public void setObject(Class classType, final String methodName, final String argsJson) {
            this.mClassType = classType;
            this.mMethod = methodName;
            this.mArgsJson = argsJson;
        }

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (mClassType) {
                result = exec(null, mClassType, mMethod, mArgsJson);
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
