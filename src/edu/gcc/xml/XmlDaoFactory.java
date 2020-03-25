package edu.gcc.xml;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import edu.gcc.xml.exception.DaoCreationException;
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
 */
public class XmlDaoFactory {
	private XmlDaoFactory() {
		throw new UnsupportedOperationException("Cannot create instance of static utiltiy class");
	}

	public static final <T> T createDao(final Class<T> clazz) {
		if (!clazz.isInterface())
			throw new DaoCreationException(String.format("%s is not an interface", clazz));

		if (!clazz.isAnnotationPresent(XmlDao.class))
			throw new DaoCreationException(String.format("%s does not ahve XmlDao annotation", clazz));

		Schema<?> schema = Schema.of(clazz.getAnnotation(XmlDao.class).value());

		Class<? extends T> proxy = new ByteBuddy()
				.subclass(clazz)
				.method(ElementMatchers.isAnnotatedWith(XmlInsert.class))
				.intercept(MethodDelegation.to(new InsertInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XmlDelete.class))
				.intercept(MethodDelegation.to(new DeleteInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XmlUpdate.class))
				.intercept(MethodDelegation.to(new UpdateInterceptor<>(schema)))
				.method(ElementMatchers.isAnnotatedWith(XPathQuery.class))
				.intercept(MethodDelegation.to(new QueryInterceptor<>(schema)))
				.make()
				.load(clazz.getClassLoader())
				.getLoaded();

		try {
			return proxy.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new DaoCreationException(String.format("Could not create proxy for class %s", clazz));
		}
	}
}


