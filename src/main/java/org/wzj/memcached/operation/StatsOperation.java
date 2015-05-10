package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

import java.util.HashMap;
import java.util.Map;

public class StatsOperation extends BaseOperation {

    private Map<String, String> stats = new HashMap<String, String>();

    public StatsOperation(Callback callback) {
        super(MemcachedConstants.CMD_STATS, callback);
    }

    @Override
    public byte[] buildCmd() {
        return "stats\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        parseReply(reply);
    }

    private void parseReply(String line) {
        if (line.startsWith(MemcachedConstants.STATS)) {
            String[] split = line.split(" ");
            stats.put(split[1], split[2]);

        } else if (line.equals(MemcachedConstants.END)) {
            this.setStatus(Status.COMPLETE);
            callback.complete(stats);
        } else {
            callback.complete(null);
        }
    }


}
