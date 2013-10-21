package org.wzj.memcached.test;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.wzj.memcached.MemcachedClient;
import org.wzj.memcached.future.OperationFutrue;

public class Example {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		
		MemcachedClient memcachedClient = new MemcachedClient(new String[]{"127.0.0.1:11211" }) ;
		
		
		//sync 
		boolean set1 = memcachedClient.set("key1", "Hi") ;
		boolean set2 = memcachedClient.set("key2", "Hello") ;
		
		Map<String, Object> gets = memcachedClient.gets("key1" ,"key2") ;
		
		System.out.println(String.format("set key1 : %s ", set1));
		System.out.println(String.format("set key2 : %s ", set2));
		
		System.out.println(String.format("key1: %s", gets.get("key1")));
		System.out.println(String.format("key2: %s", gets.get("key2")));
		
		
		//async
		
		OperationFutrue asynGet = memcachedClient.asynSet("key3" ,"xuojingxiong") ;
		
		Boolean set3 = (Boolean)asynGet.get(2, TimeUnit.SECONDS);
		
		System.out.println(String.format("set key3 : %s ", set3));
		
		memcachedClient.shutdown(); 
		
		
		
	}

}
