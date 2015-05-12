package org.wzj.memcached.node;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author Wen
 */
public class ArrayModNodeLocator implements NodeLocator {

    private MemcachedNode[] allNodes;

    public ArrayModNodeLocator(MemcachedNode[] allNodes) {
        this.allNodes = allNodes;
    }

    @Override
    public MemcachedNode getPrimary(String key) {
        return allNodes[getServerForKey(key)];
    }


    @Override
    public Collection<MemcachedNode> getAll() {
        return Arrays.asList(allNodes);
    }

    private int getServerForKey(String key) {
        int hashCode = key.hashCode();
        return (hashCode < 0 ? -hashCode : hashCode) % allNodes.length;
    }


}
