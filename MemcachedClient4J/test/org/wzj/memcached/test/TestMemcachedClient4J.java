package org.wzj.memcached.test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wzj.memcached.MemcachedClient;

public class TestMemcachedClient4J {

	private MemcachedClient client ;
	
	
	@Before
	public void init(){
		client =  new MemcachedClient(new String[]{"127.0.0.1:11211" } );
	}
	
	@Test
	public void flush(){
		long start = System.currentTimeMillis() ;
		boolean flush = client.flush("127.0.0.1:11211") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(flush == true ) ;
		System.out.println("flush taken time : "+(end -start));
		
	}
	
	
	@Test
	public void add() throws InterruptedException, ExecutionException{
		
		//this.flush(); 
		long start = System.currentTimeMillis() ;
		boolean result = client.add("name", "wen zuojing") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(result == true ) ;
		System.out.println("add taken time : "+(end -start));
	}
	
	
	
	
	@Test
	public void set() throws InterruptedException, ExecutionException{

		client.flush("127.0.0.1:11211") ;
		long start = System.currentTimeMillis() ;
		boolean result = client.set("name", "wen zuojing") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(result == true ) ;
		System.out.println("set taken time : "+(end -start));
		
	}
	
	@Test
	public void replace() throws InterruptedException, ExecutionException{
		
		client.set("name", "wen zuojing") ;
		
		long start = System.currentTimeMillis() ;
		boolean result = client.replace("name", "zuojingxiong") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(result == true ) ;
		System.out.println("replace taken time : "+(end -start));
	}
	
	@Test
	public void append() throws InterruptedException, ExecutionException{
		
		client.set("name", "zuojing") ;
		
		long start = System.currentTimeMillis() ;
		boolean result = client.append("name", "xiong") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(result == true ) ;
		System.out.println("append taken time : "+(end -start));
	}
	
	@Test
	public void get() throws InterruptedException, ExecutionException{
		
		client.set("name", "wen zuojing") ;
		
		long start = System.currentTimeMillis() ;
		String result = (String)client.get("name") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue("wen zuojing".equals(result) ) ;
		System.out.println("get taken time : "+(end -start));
	}
	
	@Test
	public void gets() throws InterruptedException, ExecutionException{
		
		client.set("name1", "wen zuojing") ;
		client.set("name2", "zuojingxiong") ;
		
		long start = System.currentTimeMillis() ;
		Map<String,Object> result = (Map<String,Object>)client.gets("name1" ,"name2") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue("wen zuojing".equals(result.get("name1")) ) ;
		Assert.assertTrue("zuojingxiong".equals(result.get("name2")) ) ;
		System.out.println("gets taken time : "+(end -start));
	}
	
	
	@Test
	public void incr() throws InterruptedException, ExecutionException{
		
		client.set("age", "99") ;
		
		long start = System.currentTimeMillis() ;
		long   resutl = client.incr("age") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue (resutl == 100 ) ;
		System.out.println("incr taken time : "+(end -start));
		
	}
	
	@Test
	public void decr() throws InterruptedException, ExecutionException{
        client.set("age", "99") ;
		
		long start = System.currentTimeMillis() ;
		long   resutl = client.decr("age") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(resutl == 98 ) ;
		System.out.println("decr taken time : "+(end -start));
		
	}
	
	@Test
	public void delete() throws InterruptedException, ExecutionException{
		
        client.set("age", "99") ;
		
		long start = System.currentTimeMillis() ;
		boolean   resutl = client.delete("age") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(resutl ) ;
		System.out.println("delete taken time : "+(end -start));
		
	}
	
	@Test
	public void stats() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		Map<String, String> stats = client.stats("127.0.0.1:11211") ;
		long end = System.currentTimeMillis() ;
		Assert.assertTrue(stats.keySet().size() > 0 );
		System.out.println("stats taken time : "+(end -start));
		
	}
	


}
