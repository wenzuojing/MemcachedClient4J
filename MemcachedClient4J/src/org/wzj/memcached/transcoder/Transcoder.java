package org.wzj.memcached.transcoder;

import org.wzj.memcached.CachedData;


/**
 * 
 * @author Wen
 *
 */
public interface Transcoder {

	Object decode(CachedData data);

	CachedData encode(Object value);

}
