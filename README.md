MemcachedClient4J
===========

A written using netty3 framework mecached client, realize mecached ASCII protocol , performance more than Spymecached. There are many features and extension to be perfect. Provide both synchronous and asynchronous interfaces, use method is very simple, examples are as follows:

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



