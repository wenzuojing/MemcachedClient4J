package org.wzj.memcached.transcoder;


/**
 * 
 * @author Wen
 *
 */
public class TranscoderFactory {
	
	private static final  Transcoder  transcoder = new WhalinTranscoder() ;
	
	public static  Transcoder  getTranscoder(){
		return transcoder ;
	}

}
