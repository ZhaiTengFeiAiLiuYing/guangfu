package com.pb;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuangfuApplicationTests {

	@Test
	public void contextLoads() {
		Set<String> a=new HashSet<>();
		a.add("a");
		a.clear();
		System.out.println(a.size());
	}

}
