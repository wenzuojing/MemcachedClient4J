package org.wzj.memcached.node;

import java.util.Collection;

/**
 * @author Wen
 */
public class MemcachedNodeFactory {

    private NodeLocator locator;

    public MemcachedNodeFactory(String[] servers, int connSize) {

        if (servers == null || servers.length == 0) {
            throw new IllegalArgumentException("Must include at least one node");
        }

        if (connSize <= 0) connSize = 1;

        MemcachedNode[] nodes = new MemcachedNode[servers.length];

        for (int i = 0, len = nodes.length; i < len; i++) {
            String server = servers[i];

            String[] split = server.split("[:|(\\s)+]");

            if (split.length != 2) {
                throw new IllegalArgumentException(server + " is not illegal ");
            }

            nodes[i] = new MemcachedNode(split[0], Integer.parseInt(split[1]), connSize);
        }

        locator = new ArrayModNodeLocator(nodes);
    }

    public MemcachedNode locate(String key) {
        return locator.getPrimary(key);
    }

    public Collection<MemcachedNode> getAllMemcachedNodes() {
        return locator.getAll();
    }


}
