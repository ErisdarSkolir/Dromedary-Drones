package edu.gcc.xml;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import edu.gcc.xml.exception.XmlDaoCreationException;
import edu.gcc.xml.interceptor.DeleteInterceptor;
import edu.gcc.xml.interceptor.InsertInterceptor;
import edu.gcc.xml.interceptor.QueryInterceptor;
import edu.gcc.xml.interceptor.UpdateInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author Luke Donmoyer
 * 
 *         Factory class that creates XML DAO implementations at runtime.
 *
 */
public class XmlDaoFactory {
	private XmlDaoFactory() {
		throw new UnsupportedOperationException("Cannot create instance of static utiltiy class");
	}

	/**
	 * This method creates a DAO for the specified class. The class must be
	 * annotated with {@link XmlDao} and to be of any use must contain methods
	 * annotated with {@link XmlDelete}, {@link XmlInsert}, {@link XmlUpdate}, or
	 * {@link XPathQuery}.
	 * 
	 * @param <T>   The DAO interface type.
	 * @param clazz The class of the DAO interface.
	 * @return A new implementation instance of the DAO interface.
	 */
	public static final <T> T createDao(final Class<T> clazz) {
		if (!clazz.isInterface())
			throw new XmlDaoCreationException(String.format("%s is not an interface", clazz));

		if (!clazz.isAnnotationPresent(XmlDao.class))
			throw new XmlDaoCreationException(String.format("%s does not ahve XmlDao annotation", clazz));

		XmlDao annotation = clazz.getAnnotation(XmlDao.class);

		XmlSchema<?> schema = annotation.fileName().isEmpty() ? XmlSchema.of(annotation.value())
				: XmlSchema.of(annotation.value(), annotation.fileName());

		// Create proxy and method interceptors using byte buddy.
		Class<? extends T> proxy = new ByteBuddy().subclass(clazz)
				.method(ElementMatchers.isAnnotatedWith(XmlInsert.class))
				.intercept(MethodDelegation.to(new InsertInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XmlDelete.class))
				.intercept(MethodDelegation.to(new DeleteInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XmlUpdate.class))
				.intercept(MethodDelegation.to(new UpdateInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XPathQuery.class))
				.intercept(MethodDelegation.to(new QueryInterceptor<>(schema))).make().load(clazz.getClassLoader())
				.getLoaded();

		try {
			return proxy.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new XmlDaoCreationException(String.format("Could not create proxy for class %s", clazz));
		}
	}
}
