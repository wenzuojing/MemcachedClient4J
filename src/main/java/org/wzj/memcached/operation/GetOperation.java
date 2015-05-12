package org.wzj.memcached.operation;

import org.wzj.memcached.CachedData;
import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.transcoder.TranscoderFactory;

import java.util.HashMap;
import java.util.Map;

public class GetOperation extends BaseOperation {

    private String[] keys;

    private Map<String, Object> dataMap = new HashMap<String, Object>();

    private volatile String[] dataInfo = null;

    private volatile int dataBlockLength = -1;

    public GetOperation(String[] keys) {
        super(MemcachedConstants.CMD_GET);
        this.keys = keys;
    }


    @Override
    public byte[] buildCmd() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("get");
        for (String key : keys) {
            sb.append(" ").append(key);
        }

        sb.append("\r\n");
        return sb.toString().getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        parseReply(reply);
    }

    private void parseReply(String line) {
        if (line.startsWith(MemcachedConstants.VALUE)) {
            dataInfo = line.split(" "); // example : VALUE key 32 10
            dataBlockLength = Integer.parseInt(dataInfo[3]);
        } else if (line.startsWith(MemcachedConstants.END)) {
            this.setResult(dataMap);
        } else {
            throw new RuntimeException("server error");
        }
    }

    @Override
    public int getDataBlockLength() {
        return dataBlockLength;
    }

    @Override
    public void setDataBlock(byte[] data) {
        CachedData cachedData = new CachedData(
                Integer.parseInt(dataInfo[2]), data);

        Object obj = TranscoderFactory.getTranscoder().decode(
                cachedData);
        dataMap.put(dataInfo[1], obj);
        dataInfo = null;
        dataBlockLength = -1;

    }


}
