package edu.gcc.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class ReflectionUtils {
	private ReflectionUtils() {
		throw new UnsupportedOperationException("Cannot instantiate static utility class");
	}

	public static final MethodHandle getFieldGetter(final Class<?> clazz, final String name,
			final MethodHandles.Lookup lookup) throws IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);

		MethodHandle getter = lookup.unreflectGetter(field);

		field.setAccessible(false);

		return getter;
	}

	public static final MethodHandle getFieldSetter(final Class<?> clazz, final String name,
			final MethodHandles.Lookup lookup) throws NoSuchFieldException, SecurityException, IllegalAccessException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);

		MethodHandle setter = lookup.unreflectSetter(field);

		field.setAccessible(false);

		return setter;
	}
}
