package org.wzj.memcached.transcoder;

import org.wzj.memcached.CachedData;
import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Wen
 */
public class WhalinTranscoder implements Transcoder {


    static final int SPECIAL_BYTE = 1;
    static final int SPECIAL_BOOLEAN = 8192;
    static final int SPECIAL_INT = 4;
    static final int SPECIAL_LONG = 16384;
    static final int SPECIAL_CHARACTER = 16;
    static final int SPECIAL_STRING = 32;
    static final int SPECIAL_STRINGBUFFER = 64;
    static final int SPECIAL_FLOAT = 128;
    static final int SPECIAL_SHORT = 256;
    static final int SPECIAL_DOUBLE = 512;
    static final int SPECIAL_DATE = 1024;
    static final int SPECIAL_STRINGBUILDER = 2048;
    static final int SPECIAL_BYTEARRAY = 4096;
    static final int COMPRESSED = 2;
    static final int SERIALIZED = 8;

    private final TranscoderUtils tu = new TranscoderUtils(false);

    private int compressionThreshold; // MemcachedConstants.DEFAULT_COMPRESSION_THRESHOLD;

    private int maxSize;

    public WhalinTranscoder() {
        this(MemcachedConstants.DEFAULT_COMPRESSION_THRESHOLD, 20 * 1024 * 1024 /*2M*/);
    }

    public WhalinTranscoder(int compressionThreshold, int maxSize) {
        super();
        this.compressionThreshold = compressionThreshold;
        this.maxSize = maxSize;
    }


    @Override
    public Object decode(CachedData d) {
        byte[] data = d.getData();
        Object rv = null;
        if ((d.getFlags() & COMPRESSED) != 0) {
            data = decompress(d.getData());
        }
        if ((d.getFlags() & SERIALIZED) != 0) {
            rv = deserialize(data);
        } else {
            int f = d.getFlags() & ~COMPRESSED;
            switch (f) {
                case SPECIAL_BOOLEAN:
                    rv = Boolean.valueOf(this.decodeBoolean(data));
                    break;
                case SPECIAL_INT:
                    rv = Integer.valueOf(tu.decodeInt(data));
                    break;
                case SPECIAL_SHORT:
                    rv = Short.valueOf((short) tu.decodeInt(data));
                    break;
                case SPECIAL_LONG:
                    rv = Long.valueOf(tu.decodeLong(data));
                    break;
                case SPECIAL_DATE:
                    rv = new Date(tu.decodeLong(data));
                    break;
                case SPECIAL_BYTE:
                    rv = Byte.valueOf(tu.decodeByte(data));
                    break;
                case SPECIAL_FLOAT:
                    rv = new Float(Float.intBitsToFloat(tu.decodeInt(data)));
                    break;
                case SPECIAL_DOUBLE:
                    rv = new Double(Double.longBitsToDouble(tu.decodeLong(data)));
                    break;
                case SPECIAL_BYTEARRAY:
                    rv = data;
                    break;
                case SPECIAL_STRING:
                    rv = decodeString(data);
                    break;
                case SPECIAL_STRINGBUFFER:
                    rv = new StringBuffer(decodeString(data));
                    break;
                case SPECIAL_STRINGBUILDER:
                    rv = new StringBuilder(decodeString(data));
                    break;
                case SPECIAL_CHARACTER:
                    rv = decodeCharacter(data);
                    break;
                default:

            }
        }
        return rv;
    }

    @Override
    public CachedData encode(Object o) {
        byte[] b = null;
        int flags = 0;
        if (o instanceof String) {
            b = encodeString((String) o);
            flags |= SPECIAL_STRING;
            if (StringUtils.isJsonObject((String) o)) {
                return new CachedData(flags, b);
            }
        } else if (o instanceof StringBuffer) {
            flags |= SPECIAL_STRINGBUFFER;
            b = encodeString(String.valueOf(o));
        } else if (o instanceof StringBuilder) {
            flags |= SPECIAL_STRINGBUILDER;
            b = encodeString(String.valueOf(o));
        } else if (o instanceof Long) {
            b = tu.encodeLong((Long) o);
            flags |= SPECIAL_LONG;
        } else if (o instanceof Integer) {
            b = tu.encodeInt((Integer) o);
            flags |= SPECIAL_INT;
        } else if (o instanceof Short) {
            b = tu.encodeInt((Short) o);
            flags |= SPECIAL_SHORT;
        } else if (o instanceof Boolean) {
            b = this.encodeBoolean((Boolean) o);
            flags |= SPECIAL_BOOLEAN;
        } else if (o instanceof Date) {
            b = tu.encodeLong(((Date) o).getTime());
            flags |= SPECIAL_DATE;
        } else if (o instanceof Byte) {
            b = tu.encodeByte((Byte) o);
            flags |= SPECIAL_BYTE;
        } else if (o instanceof Float) {
            b = tu.encodeInt(Float.floatToIntBits((Float) o));
            flags |= SPECIAL_FLOAT;
        } else if (o instanceof Double) {
            b = tu.encodeLong(Double.doubleToLongBits((Double) o));
            flags |= SPECIAL_DOUBLE;
        } else if (o instanceof byte[]) {
            b = (byte[]) o;
            flags |= SPECIAL_BYTEARRAY;
        } else if (o instanceof Character) {
            b = tu.encodeInt((Character) o);
            flags |= SPECIAL_CHARACTER;
        } else {
            b = serialize(o);
            flags |= SERIALIZED;
        }
        assert b != null;
        if (b.length > compressionThreshold) {
            byte[] compressed = compress(b);
            if (compressed.length < b.length) {

                b = compressed;
                flags |= COMPRESSED;
            }
        }
        return new CachedData(flags, b);
    }

    protected Character decodeCharacter(byte[] b) {
        return Character.valueOf((char) tu.decodeInt(b));
    }

    public byte[] encodeBoolean(boolean b) {
        byte[] rv = new byte[1];
        rv[0] = (byte) (b ? 1 : 0);
        return rv;
    }

    public boolean decodeBoolean(byte[] in) {
        assert in.length == 1 : "Wrong length for a boolean";
        return in[0] == 1;
    }

    protected byte[] serialize(Object o) {
        if (o == null) {
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(o);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rv;
    }

    /**
     * Get the object represented by the given serialized bytes.
     */
    protected Object deserialize(byte[] in) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not deserialize bytes ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not deserialize bytes ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rv;
    }

    /**
     * Compress the given array of bytes.
     */
    protected byte[] compress(byte[] in) {
        if (in == null) {
            throw new NullPointerException("Can't compress null");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gz = null;
        try {
            gz = new GZIPOutputStream(bos);
            gz.write(in);
        } catch (IOException e) {
            throw new RuntimeException("IO exception compressing data", e);
        } finally {
            if (gz != null) {
                try {
                    gz.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] rv = bos.toByteArray();
        return rv;
    }

    /**
     * Decompress the given array of bytes.
     *
     * @return null if the bytes cannot be decompressed
     */
    protected byte[] decompress(byte[] in) {
        ByteArrayOutputStream bos = null;
        if (in != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(in);
            bos = new ByteArrayOutputStream();
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(bis);

                byte[] buf = new byte[8192];
                int r = -1;
                while ((r = gis.read(buf)) > 0) {
                    bos.write(buf, 0, r);
                }
            } catch (IOException e) {
                // bos = null;
                throw new RuntimeException("Failed to decompress data", e);

            } finally {

                if (gis != null) {
                    try {
                        gis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return bos == null ? null : bos.toByteArray();
    }

    /**
     * Decode the string with the current character set.
     */
    protected String decodeString(byte[] data) {
        String rv = null;
        if (data != null) {
            rv = new String(data, MemcachedConstants.DEFAULT_CHARSET);
        }
        return rv;
    }

    /**
     * Encode a string into the current character set.
     */
    protected byte[] encodeString(String in) {
        byte[] rv = null;
        rv = in.getBytes(MemcachedConstants.DEFAULT_CHARSET);
        return rv;
    }


}
