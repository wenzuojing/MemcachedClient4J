package org.wzj.memcached.test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.wzj.memcached.MemcachedClient;
import org.wzj.memcached.future.OperationFutrue;

public class TestMemcachedClient4J {

	private MemcachedClient client ;
	
	
	@Before
	public void init(){
		client =  new MemcachedClient(new String[]{"127.0.0.1:11211" } );
	}
	
	
	@Test
	public void add() throws InterruptedException, ExecutionException{
		
		
		OperationFutrue operationFutrue = client.asynAdd("name", "wenzuojing", new Date(0)) ;
		
		boolean  result = (Boolean)operationFutrue.get();
		
		assert(result);
	}
	
	
	@Test
	public void set() throws InterruptedException, ExecutionException{

		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		for(int i =0  ; i <  1    ;  i++){
			OperationFutrue operationFutrue = client.asynSet("name"+i, "wenzuojing", new Date(0)) ;
			Object object = operationFutrue.get();
		}
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}
	
	@Test
	public void replace() throws InterruptedException, ExecutionException{
		
		OperationFutrue operationFutrue = client.asynReplace("name", "wen", new Date(0)) ;
		
		boolean  result = (Boolean)operationFutrue.get();
		
		assert(result);
	}
	
	@Test
	public void get() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynGet("name") ;
		
		assert(operationFutrue.get() != null ) ;
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
		//System.out.println(operationFutrue.get());
	}
	
	
	@Test
	public void incr() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynIncr("wen" , 2L) ;
		
		System.out.println(operationFutrue.get());
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}
	
	@Test
	public void decr() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynDecr("wen" , 2L) ;
		
		System.out.println(operationFutrue.get());
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}
	
	@Test
	public void delete() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynDelete("name7", new Date(2000)) ;
		
		System.out.println(operationFutrue.get());
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}
	
	@Test
	public void stats() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynStats("127.0.0.1:11211");
		
		System.out.println(operationFutrue.get());
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}
	
	@Test
	public void flush() throws InterruptedException, ExecutionException{
		long start = System.currentTimeMillis() ;
		System.out.println("start :"+ start);
		
		OperationFutrue operationFutrue = client.asynFlush("127.0.0.1:11211");
		
		System.out.println(operationFutrue.get());
		
		long end = System.currentTimeMillis() ;
		System.out.println("end :"+ end);
		
		System.out.println(end -start);
		
	}

}
