package cn.edu.xmu.gxj.matchp.control;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PrintBean {
	    @Autowired
	    ApplicationContext applicationContext;
	    public static int a = 1234567;
	    public void printBeans() {
	        System.out.println(Arrays.asList(applicationContext.getBeanDefinitionNames()));
	    }
}
