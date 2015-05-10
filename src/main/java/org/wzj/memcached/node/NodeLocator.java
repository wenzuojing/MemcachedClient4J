package org.wzj.memcached.node;

import java.util.Collection;
import java.util.Iterator;


/**
 * @author Wen
 */
public interface NodeLocator {

    MemcachedNode getPrimary(String key);

    Collection<MemcachedNode> getAll();

}
