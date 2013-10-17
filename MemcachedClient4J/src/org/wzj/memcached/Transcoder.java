package org.wzj.memcached;


/**
 * 
 * @author Wen
 *
 */
public interface Transcoder {

	Object decode(CachedData data);

	CachedData encode(Object value);

}
