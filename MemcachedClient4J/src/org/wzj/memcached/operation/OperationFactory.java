package org.wzj.memcached.operation;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.future.OperationFutrue;
import org.wzj.memcached.node.MemcachedNode;
import org.wzj.memcached.node.MemcachedNodeFactory;
import org.wzj.memcached.operation.BaseOperation.Callback;

/**
 * 
 * @author Wen
 * 
 */
public class OperationFactory {

	private int maxLoopNum;

	private MemcachedNodeFactory memcachedNodeFactory;

	public OperationFactory(MemcachedNodeFactory memcachedNodeFactory) {
		this.memcachedNodeFactory = memcachedNodeFactory;
		maxLoopNum = this.memcachedNodeFactory.getAllMemcachedNodes().size() * 100;
	}

	private MemcachedNode getAvailableMemcachedNode(String key) {

		int i = 0;

		Iterator<MemcachedNode> iterator = this.memcachedNodeFactory
				.locate(key);

		while (iterator.hasNext()) {
			MemcachedNode node = iterator.next();

			if (node.isConnected()) {
				return node;
			} else {
				i++;

				try {
					Thread.sleep(100 * i);

					if (node.isConnected())
						return node;

				} catch (InterruptedException e) {
				}

				if (i >= maxLoopNum) {
					throw new IllegalStateException("not has available server ");
				}

			}
		}

		return null;
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

	public Operaction createStore(String cmd, String key, Object value,
			Date expiry, final OperationFutrue futrue) {

		if (cmd == null) {
			throw new NullPointerException("cmd not must be  null !");
		}

		if (expiry == null)
			expiry = new Date(0);

		int expiryTime = (int) (expiry.getTime() / 1000);

		if (MemcachedConstants.CMD_ADD == cmd) {

			Operaction operation = new AddOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key));

			return operation;
		} else if (MemcachedConstants.CMD_SET == cmd) {

			Operaction operation = new SetOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});

			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key));
			return operation;

		} else if (MemcachedConstants.CMD_REPLACE == cmd) {
			Operaction operation = new ReplaceOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key));

			return operation;
		} else if (MemcachedConstants.CMD_APPEND == cmd) {
			Operaction operation = new AppendOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key));

			return operation;
		} else {
			throw new IllegalArgumentException(
					"Cmd may be 'add' , 'append' 'set' or 'replace' ");
		}
	}

	public Operaction createGet(String[] keys, final OperationFutrue futrue) {

		if (keys == null || keys.length == 0) {
			throw new IllegalArgumentException();
		}
		Operaction operation = new GetOperation(keys, new Callback() {
			@Override
			public void complete(Object msg) {
				futrue.setMsg(msg);
			}
		});
		futrue.setOperation(operation);

		operation.setMemcachedNode(getAvailableMemcachedNode(keys[0]));

		return operation;
	}

	public Operaction createIncrOrDecr(String cmd, String key, long inc,
			final OperationFutrue futrue) {

		Operaction operation = new IncrOrDecrOperation(cmd, key, inc,
				new Callback() {
					@Override
					public void complete(Object msg) {
						futrue.setMsg(msg);
					}
				});
		futrue.setOperation(operation);

		operation.setMemcachedNode(getAvailableMemcachedNode(key));

		return operation;
	}

	public Operaction createDelete(String key, Date expiry,
			final OperationFutrue futrue) {

		Operaction operation = new DeleteOperation(key, expiry, new Callback() {
			@Override
			public void complete(Object msg) {
				futrue.setMsg(msg);
			}
		});
		futrue.setOperation(operation);

		operation.setMemcachedNode(getAvailableMemcachedNode(key));

		return operation;
	}

	public Operaction createStats(String server, final OperationFutrue futrue) {

		String[] split = server.split("[:|(\\s)+]");

		if (split.length != 2) {
			throw new IllegalArgumentException(server + " is not illegal ");
		}

		MemcachedNode memcachedNode = getMemcachedNode(server);

		if (memcachedNode == null) {
			throw new RuntimeException("not found " + server);

		}

		if (!memcachedNode.isConnected()) {
			throw new IllegalStateException(server + " is not available ");
		}

		Operaction operation = new StatsOperation(new Callback() {
			@Override
			public void complete(Object msg) {
				futrue.setMsg(msg);
			}
		});
		futrue.setOperation(operation);

		operation.setMemcachedNode(memcachedNode);

		return operation;
	}

	public Operaction createFlush(String server, final OperationFutrue futrue) {
		
		String[] split = server.split("[:|(\\s)+]");

		if (split.length != 2) {
			throw new IllegalArgumentException(server + " is not illegal ");
		}

		MemcachedNode memcachedNode = getMemcachedNode(server);

		if (memcachedNode == null) {
			throw new RuntimeException("not found " + server);

		}

		if (!memcachedNode.isConnected()) {
			throw new IllegalStateException(server + " is not available ");
		}

		Operaction operation = new FlushOperation(new Callback() {
			@Override
			public void complete(Object msg) {
				futrue.setMsg(msg);
			}
		});
		futrue.setOperation(operation);

		operation.setMemcachedNode(memcachedNode);

		return operation;
	}

}
