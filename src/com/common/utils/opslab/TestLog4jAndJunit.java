package com.common.utils.opslab;

import org.junit.Test;

public class TestLog4jAndJunit {

	private  static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestLog4jAndJunit.class);
	
	@Test
	public void test() {
		logger.info("21333");
	}

}
