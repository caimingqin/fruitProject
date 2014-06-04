package com.mce.domain.event.bootstrap;

import com.mce.core.inject.ConfigurableBeanInjector;
import com.mce.core.inject.support.SpringBeanInjector;
import com.mce.domain.event.DomainEventHandler;
import com.mce.domain.event.DomainEventInterceptor;
import com.mce.domain.event.DomainEventProcessorConfiger;
import com.mce.domain.event.DomainEventProcessorDSL;
import com.mce.domain.event.EventBuffer;
import com.mce.domain.event.EventBufferFactory;
import com.mce.domain.event.buffer.DefaultEventBufferFacatory;
import com.mce.domain.event.config.AnnotationDomainEventHandlerConfigurable;
import com.mce.domain.event.handler.AnnotationActionEventHandlerUtils;
import com.mce.domain.event.handler.DomainEventHandlerConfigurable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DomainEventProcessorBootstrap {
	private DomainEventProcessorDSL depdsl;
	private int coreSize = 1;
	private ConfigurableBeanInjector cBeanInjector = null;
	private Log logger = LogFactory.getLog(getClass().getName());

	public DomainEventProcessorBootstrap() {
		this(2);
	}

	public DomainEventProcessorBootstrap(int coreSize2) {
		this(new SpringBeanInjector(), coreSize2);
	}

	public DomainEventProcessorBootstrap(ConfigurableBeanInjector cBeanInjector) {
		this(cBeanInjector, 2);
	}

	public DomainEventProcessorBootstrap(
			ConfigurableBeanInjector cBeanInjector, int coreSize2) {
		Validate.notNull(cBeanInjector, "BeanInjector required");
		this.cBeanInjector = cBeanInjector;
		init(coreSize2);
	}

	private void init(int coreSize2) {
		if (coreSize2 > 1)
			this.coreSize = coreSize2;
	}

	public EventBuffer start() {
		EventBufferFactory ebf = getEventBufferFactory();
		DomainEventHandler[] dehs = buildDomainEventHandlers();
		this.depdsl = new DomainEventProcessorDSL(this.coreSize, ebf);
		DomainEventProcessorConfiger depc = this.depdsl
				.createDomainEventProcessor(dehs);
		return this.depdsl.start(depc);
	}

	private EventBufferFactory getEventBufferFactory() {
		EventBufferFactory ebf = (EventBufferFactory) this.cBeanInjector
				.getBeanOnSafe(EventBufferFactory.class);
		if (ebf == null) {
			ebf = new DefaultEventBufferFacatory();
		}
		return ebf;
	}

	private DomainEventHandler[] buildDomainEventHandlers() {
		DomainEventHandlerConfigurable cecf = getDomainEventConfigurable();
		Object[] handlers = cecf.build();
		if ((handlers != null) && (handlers.length > 0)) {
			this.logger.info("Handler[" + handlers.length + "] EventHandlers");
		}
		for (Object obs : handlers) {
			this.logger.info("[" + this.cBeanInjector + "] inject to[" + obs
					+ ']');
			this.cBeanInjector.inject(obs);
		}
		List<DomainEventInterceptor> ints = new ArrayList<DomainEventInterceptor>();
		String[] iNames = this.cBeanInjector
				.getBeanNamesForType(DomainEventInterceptor.class);
		for (String name : iNames) {
			DomainEventInterceptor dei = (DomainEventInterceptor) this.cBeanInjector
					.getBean(name);
			ints.add(dei);
		}
		List<Object> obsl = Arrays.asList(handlers);
		return AnnotationActionEventHandlerUtils.convert(obsl, ints);
	}

	private DomainEventHandlerConfigurable getDomainEventConfigurable() {
		DomainEventHandlerConfigurable cecf = (DomainEventHandlerConfigurable) this.cBeanInjector
				.getBeanOnSafe(DomainEventHandlerConfigurable.class);
		cecf = cecf == null ? new AnnotationDomainEventHandlerConfigurable()
				: cecf;
		return cecf;
	}

	public void stop() {
		this.depdsl.stop();
	}

	public void addComponent(Object aComponent) {
		this.logger.info("Add DomainEventComponent To Injector[" + aComponent
				+ "]");
		this.cBeanInjector.registBean(aComponent.getClass().getSimpleName(),
				aComponent);
	}
}
