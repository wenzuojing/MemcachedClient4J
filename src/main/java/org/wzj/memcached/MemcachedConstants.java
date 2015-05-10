package org.wzj.memcached;

import java.nio.charset.Charset;


/**
 * @author Wen
 */
public class MemcachedConstants {

    // return codes
    public static final String VALUE = "VALUE";            // start of value line from server
    public static final String STATS = "STAT";            // start of stats line from server
    public static final String ITEM = "ITEM";            // start of item line from server
    public static final String DELETED = "DELETED";        // successful deletion
    public static final String NOTFOUND = "NOT_FOUND";        // record not found for delete or incr/decr
    public static final String STORED = "STORED";        // successful store of data
    public static final String NOTSTORED = "NOT_STORED";    // data not stored
    public static final String OK = "OK";            // success
    public static final String END = "END";            // end of data from server

    public static final String ERROR = "ERROR";            // invalid command name from client
    public static final String CLIENT_ERROR = "CLIENT_ERROR";    // client error in input line - invalid protocol
    public static final String SERVER_ERROR = "SERVER_ERROR";    // server error

    public static final byte[] B_END = "END\r\n".getBytes();
    public static final byte[] B_NOTFOUND = "NOT_FOUND\r\n".getBytes();
    public static final byte[] B_DELETED = "DELETED\r\r".getBytes();
    public static final byte[] B_STORED = "STORED\r\r".getBytes();


    public static final String CMD_ADD = "add";
    public static final String CMD_SET = "set";
    public static final String CMD_REPLACE = "replace";
    public static final String CMD_APPEND = "append";
    public static final String CMD_DELETE = "delete";
    public static final String CMD_GET = "get";
    public static final String CMD_INCR = "incr";
    public static final String CMD_DECR = "decr";
    public static final String CMD_STATS = "stats";
    public static final String CMD_FLUSH = "flush_all";


    public static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

    // default compression threshold
    public static final int DEFAULT_COMPRESSION_THRESHOLD = 30720;

    public static final int DEFAULT_OPERATION_TIME_SEC = 2;


}
