package edu.gcc.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * @author Luke Donmoyer
 * 
 *         Static utility class with a few reflection helper methods.
 */
public class ReflectionUtils {
	private ReflectionUtils() {
		throw new UnsupportedOperationException("Cannot instantiate static utility class");
	}

	/**
	 * Creates a method handle getter for the field in the given class. This ignores field
	 * protection so it should only be used at initialization time and by trusted
	 * code.
	 * 
	 * @param clazz  The class to get the field of.
	 * @param name   The name of the field to get.
	 * @param lookup The {@link MethodHandle.Lookup} for the given class.
	 * @return A method handle getter for the specified field.
	 * 
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static final MethodHandle getFieldGetter(final Class<?> clazz, final String name,
			final MethodHandles.Lookup lookup) throws IllegalAccessException, NoSuchFieldException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);

		MethodHandle getter = lookup.unreflectGetter(field);

		field.setAccessible(false);

		return getter;
	}

	/**
	 * Creates a method handle setter for the field in the given class. This ignores field
	 * protection so it should only be used at initialization time and by trusted
	 * code.
	 * 
	 * @param clazz  The class to set the field of.
	 * @param name   The name of the field to set.
	 * @param lookup The {@link MethodHandle.Lookup} for the given class.
	 * @return A method handle setter for the specified field.
	 * 
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static final MethodHandle getFieldSetter(final Class<?> clazz, final String name,
			final MethodHandles.Lookup lookup) throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);

		MethodHandle setter = lookup.unreflectSetter(field);

		field.setAccessible(false);

		return setter;
	}
}
