package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.node.MemcachedNode;
import org.wzj.memcached.node.MemcachedNodeFactory;

import java.util.Collection;
import java.util.Date;

/**
 * @author Wen
 */
public class OperationFactory {


    private MemcachedNodeFactory memcachedNodeFactory;

    public OperationFactory(MemcachedNodeFactory memcachedNodeFactory) {
        this.memcachedNodeFactory = memcachedNodeFactory;
    }

    private MemcachedNode getAvailableMemcachedNode(String key) {
        return memcachedNodeFactory.locate(key);
    }

    private MemcachedNode getMemcachedNode(String server) {
        Collection<MemcachedNode> allMemcachedNodes = this.memcachedNodeFactory
                .getAllMemcachedNodes();

        for (MemcachedNode node : allMemcachedNodes) {
            if (node.getServerInfo().equals(server)) {
                return node;
            }
        }

        return null;
    }

    public Operation createStore(String cmd, String key, Object value,
                                          Date expiry) {

        if (cmd == null) {
            throw new NullPointerException("cmd not must be  null !");
        }

        if (expiry == null)
            expiry = new Date(0);

        int expiryTime = (int) (expiry.getTime() / 1000);

        if (MemcachedConstants.CMD_ADD.equals(cmd)) {

            Operation operation = new AddOperation(key, value, expiryTime);
            operation.setMemcachedNode(getAvailableMemcachedNode(key));

            return operation;
        } else if (MemcachedConstants.CMD_SET.equals(cmd)) {

            Operation operation = new SetOperation(key, value, expiryTime);
            operation.setMemcachedNode(getAvailableMemcachedNode(key));
            return operation;

        } else if (MemcachedConstants.CMD_REPLACE.equals(cmd)) {
            Operation operation = new ReplaceOperation(key, value, expiryTime);
            operation.setMemcachedNode(getAvailableMemcachedNode(key));

            return operation;
        } else if (MemcachedConstants.CMD_APPEND.equals(cmd)) {
            Operation operation = new AppendOperation(key, value, expiryTime);
            operation.setMemcachedNode(getAvailableMemcachedNode(key));

            return operation;
        } else {
            throw new IllegalArgumentException(
                    "Cmd may be 'add' , 'append' 'set' or 'replace' ");
        }
    }

    public Operation createGet(String[] keys) {

        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException();
        }
        Operation operation = new GetOperation(keys);
        operation.setMemcachedNode(getAvailableMemcachedNode(keys[0]));

        return operation;
    }

    public Operation createIncrOrDecr(String cmd, String key, long inc) {

        Operation operation = new IncrOrDecrOperation(cmd, key, inc);
        operation.setMemcachedNode(getAvailableMemcachedNode(key));
        return operation;
    }

    public Operation createDelete(String key, Date expiry) {

        Operation operation = new DeleteOperation(key, expiry);
        operation.setMemcachedNode(getAvailableMemcachedNode(key));

        return operation;
    }

    public Operation createStats(String server) {

        String[] split = server.split("[:|(\\s)+]");

        if (split.length != 2) {
            throw new IllegalArgumentException(server + " is not illegal ");
        }

        MemcachedNode memcachedNode = getMemcachedNode(server);

        if (memcachedNode == null) {
            throw new RuntimeException("not found " + server);

        }

        Operation operation = new StatsOperation();

        operation.setMemcachedNode(memcachedNode);

        return operation;
    }

    public Operation createFlush(String server) {

        String[] split = server.split("[:|(\\s)+]");

        if (split.length != 2) {
            throw new IllegalArgumentException(server + " is not illegal ");
        }

        MemcachedNode memcachedNode = getMemcachedNode(server);

        if (memcachedNode == null) {
            throw new RuntimeException("not found " + server);

        }

        Operation operation = new FlushOperation();
        operation.setMemcachedNode(memcachedNode);

        return operation;
    }

}
