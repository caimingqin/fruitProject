package com.cmd;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.asm.ClassReader;
import org.springframework.util.ClassUtils;

import com.mce.core.inject.Bean;
import com.mce.core.inject.annotation.asm.ClassMetadataReadingVisitor;
import com.mce.util.jackson.JacksonUtils;

public class ClassVistorTest {

	@Test
	public void test() throws Exception {

		InputStream is=new FileInputStream("E:/web/workspace/test/target/test/WEB-INF/classes/cmq/com/TestCommand.class");
		DataInputStream dstream = new DataInputStream(new BufferedInputStream(
				is));
		ClassMetadataReadingVisitor cmrv = new ClassMetadataReadingVisitor();// ��Ҫ����
		ClassReader cf = new ClassReader(dstream);
		cf.accept(cmrv, true);
		
		System.out.println(JacksonUtils.writeValueString(cmrv));
		String[] annotations = cmrv.getAnnotations();
		for(String an:annotations){
			System.out.println(an);
		}
		System.out.println(Bean.class.getName());
		Class<?> forName = ClassUtils.forName(cmrv.getClassName(),null);
		Bean bean = forName.getAnnotation(Bean.class);
		
		System.out.println(bean.name());
	}
}
