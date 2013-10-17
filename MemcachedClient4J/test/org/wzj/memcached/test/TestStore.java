package org.wzj.memcached.test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.wzj.memcached.MemcachedClient;
import org.wzj.memcached.OperationFutrue;

public class TestStore {
	
	private MemcachedClient client ;
	
	
	@Before
	public void init(){
		client =  new MemcachedClient("127.0.0.1:11211");
	}
	
	
	@Test
	public void add() throws InterruptedException, ExecutionException{
		
		
		OperationFutrue operationFutrue = client.ayncAdd("name", "wenzuojing", new Date(0)) ;
		
		boolean  result = (Boolean)operationFutrue.get();
		
		assert(result);
	}
	
	
	@Test
	public void set() throws InterruptedException, ExecutionException{
		
		
		OperationFutrue operationFutrue = client.ayncSet("name", "wenzuojing", new Date(0)) ;
		
		boolean  result = (Boolean)operationFutrue.get();
		
		assert(result);
	}
	
	@Test
	public void replace() throws InterruptedException, ExecutionException{
		
		
		OperationFutrue operationFutrue = client.ayncReplace("name", "wen", new Date(0)) ;
		
		boolean  result = (Boolean)operationFutrue.get();
		
		assert(result);
	}
	

}
