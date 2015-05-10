package org.wzj.memcached.operation;

/**
 * memcached 文本协议
 * <p/>
 * Created by wens on 15-5-7.
 */
public interface TextProtocol {

    byte[] buildCmd();

    void setReply(String reply);

    int getDataBlockLength();

    void setDataBlock(byte[] data);

}
