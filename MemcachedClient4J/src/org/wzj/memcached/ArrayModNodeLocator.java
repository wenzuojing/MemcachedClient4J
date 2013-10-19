package org.wzj.memcached;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 * 
 * @author Wen
 *
 */
public class ArrayModNodeLocator  implements NodeLocator {
	
	private MemcachedNode[] allNodes  ;
	
	

	public ArrayModNodeLocator(MemcachedNode[] allNodes) {
		this.allNodes = allNodes;
	}

	@Override
	public MemcachedNode getPrimary(String key) {
		
		return allNodes[getServerForKey(key)];
	}

	@Override
	public Iterator<MemcachedNode> getSequence(String key) {
		return new NodeIterator(getServerForKey(key) - 1);
	}

	@Override
	public Collection<MemcachedNode> getAll() {
		return Arrays.asList(allNodes);
	}
	
	private int getServerForKey(String key ){
		int hashCode = key.hashCode();
		return ( hashCode < 0  ? -hashCode : hashCode) % allNodes.length  ;
	}
	
	
	private class NodeIterator implements Iterator<MemcachedNode>{
		
		 int index ;
		
		 NodeIterator(int index ){
			 this.index  = index   ;
		 }

		@Override
		public boolean hasNext() {
			return true ;
		}

		@Override
		public MemcachedNode next() {
			this.index  = this.index+1 ;
			if(this.index >= allNodes.length ){
				this.index = 0 ;
			}
			
			
			return allNodes[this.index];
		}

		@Override
		public void remove() {
			assert false : "not support ! " ;
		}
		
	}

}
