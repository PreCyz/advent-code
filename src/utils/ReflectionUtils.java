package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class ReflectionUtils {
    private static Constructor<?> constructor;
    private static Method toLong;
    private static Method mul;
    private static Method divide;
    private static Method multiply;
    private static Method addInit;
    static Object worryLevel;
    private static Method copyValue;
    private ReflectionUtils() {}

    public static Method getToLongMethod(Object object) throws NoSuchMethodException {
        if (toLong == null) {
            toLong = object.getClass().getDeclaredMethod("toLong", new Class[]{});
            toLong.setAccessible(true);
        }
        return toLong;
    }
    public static Method getMulMethod(Object object) throws NoSuchMethodException {
        if (mul == null) {
            mul = object.getClass().getDeclaredMethod("mul", new Class[]{int.class, object.getClass()});
            mul.setAccessible(true);
        }
        return mul;
    }

    public static Method getDivideMethod(Object object) throws NoSuchMethodException {
        if (divide == null) {
            divide = object.getClass().getDeclaredMethod("divideAndRemainderBurnikelZiegler", new Class[]{object.getClass(), object.getClass()});
            divide.setAccessible(true);
        }
        return divide;
    }

    public static Method getMultiplyMethod(Object object, Object argument) throws NoSuchMethodException {
        if (multiply == null) {
            multiply = argument.getClass().getDeclaredMethod("multiply", new Class[]{argument.getClass(), object.getClass()});
            multiply.setAccessible(true);
        }
        return multiply;
    }

    public static Method getAddMethod(Object object) throws NoSuchMethodException {
        if (addInit == null) {
            addInit = object.getClass().getDeclaredMethod("add", new Class[]{object.getClass()});
            addInit.setAccessible(true);
        }
        return addInit;
    }

    public static Constructor<?> getConstructorInt() throws NoSuchMethodException, ClassNotFoundException {
        if (constructor == null) {
            constructor = Class.forName("java.math.MutableBigInteger").getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
        }
        return constructor;
    }


    public Object getWorryLevel() throws Exception {
        if (worryLevel == null) {
            worryLevel = getConstructorInt().newInstance(0);
        }
        return worryLevel;
    }
    public static Method getCopyValue(Object value) throws NoSuchMethodException {
        if (copyValue == null) {
            copyValue = value.getClass().getDeclaredMethod("copyValue", new Class[]{value.getClass()});
            copyValue.setAccessible(true);
        }
        return copyValue;
    }
}
