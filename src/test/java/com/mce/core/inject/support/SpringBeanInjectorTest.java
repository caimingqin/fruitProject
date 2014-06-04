package com.mce.core.inject.support;

import org.junit.Test;

import com.cmd.JdbcQuery;
import com.cmd.SaveCityCommand;

public class SpringBeanInjectorTest {

	@Test
	public void test3(){
		SpringBeanInjector springBeanInjector = new SpringBeanInjector();
		springBeanInjector.registClass(SaveCityCommand.class.getName(), SaveCityCommand.class);
		springBeanInjector.registClass(JdbcQuery.class.getName(),JdbcQuery.class);
		SaveCityCommand bean = springBeanInjector.getBean(SaveCityCommand.class);
		System.out.println("bb===ccc======ggggg===="+bean.getJdbcQuery());
		
	}
	
	@Test
	public void tes(){
		
		System.out.println("bb===ccc======ggggg===="+Thread.currentThread().getContextClassLoader().getResource("/"));
		
	}
}
