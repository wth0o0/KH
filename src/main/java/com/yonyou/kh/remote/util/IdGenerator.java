package com.yonyou.kh.remote.util;

import java.util.UUID;

public class IdGenerator {
	
	private IdGenerator() {
	}

	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}