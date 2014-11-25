package com.A1w0n.androidcommonutils;

import java.lang.reflect.Field;

public class ReflectionUtil<T> {

	private Object obj;
	private String fieldName;

	private boolean inited;
	private Field field;

	public ReflectionUtil(Object obj, String fieldName) {
		if (obj == null) {
			throw new IllegalArgumentException("obj cannot be null");
		}

		this.obj = obj;
		this.fieldName = fieldName;
	}

    /**
     * 用来初始化field成员变量，也就是通过反射机制获取obj类对应的名叫
     * fieldName的成员变量
     */
	private void prepare() {
		if (inited) {
            return;
        }

		inited = true;

		Class<?> c = obj.getClass();

		while (c != null) {
			try {
				Field f = c.getDeclaredField(fieldName);
				f.setAccessible(true);
				field = f;
				return;
			} catch (Exception e) {
			} finally {
				c = c.getSuperclass();
			}
		}
	}

    /**
     * 返回Obj类实例的名叫fieldName的成员变量
     *
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
	public T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
		prepare();

        // 没在obj里头找到名叫fieldName的成员变量
		if (field == null) {
            throw new NoSuchFieldException();
        }

		try {
			@SuppressWarnings("unchecked")
			T r = (T) field.get(obj);
			return r;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("unable to cast object");
		}
	}

	public void set(T val) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
		prepare();

		if (field == null)
			throw new NoSuchFieldException();

		field.set(obj, val);
	}
}
