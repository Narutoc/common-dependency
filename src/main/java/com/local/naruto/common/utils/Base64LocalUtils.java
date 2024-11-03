package com.local.naruto.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;

/**
 * base64工具类
 *
 * @author naruto chen
 * @since 2023-09-27
 */
public class Base64LocalUtils {

    private static final char[] BASE64CHAR = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', '+', '/'
    };
    private static final char BASE64PAD = '=';
    private static final byte[] DECODE = new byte[128];

    static {
        Arrays.fill(DECODE, Byte.MAX_VALUE);
        for (int i = 0; i < BASE64CHAR.length; i++) {
            DECODE[BASE64CHAR[i]] = (byte) i;
        }
    }

    private static int decode0(char[] inputBuffer, byte[] outputBuffer, int wp) {
        int outlen = 3;
        if (inputBuffer[3] == BASE64PAD) {
            outlen = 2;
        }
        if (inputBuffer[2] == BASE64PAD) {
            outlen = 1;
        }
        int b0 = DECODE[inputBuffer[0]];
        int b1 = DECODE[inputBuffer[1]];
        int b2 = DECODE[inputBuffer[2]];
        int b3 = DECODE[inputBuffer[3]];
        int i = b0 << 2 & 0xfc | b1 >> 4 & 0x3;
        int i1 = b1 << 4 & 0xf0 | b2 >> 2 & 0xf;
        switch (outlen) {
            case 1:
                outputBuffer[wp] = (byte) i;
                return 1;
            case 2:
                outputBuffer[wp++] = (byte) i;
                outputBuffer[wp] = (byte) i1;
                return 2;
            case 3:
                outputBuffer[wp++] = (byte) i;
                outputBuffer[wp++] = (byte) i1;
                outputBuffer[wp] = (byte) (b2 << 6 & 0xc0 | b3 & 0x3f);
                return 3;
            default:
                throw new RuntimeException("Couldn't decode.");
        }
    }

    public static byte[] decode(char[] data, int offset, int length) {
        char[] inputBuffer = new char[4];
        int inputBufferCount = 0;
        byte[] outputBuffer = new byte[length / 4 * 3 + 3];
        int outputBufferCount = 0;
        for (int i = offset; i < offset + length; i++) {
            char ch = data[i];
            boolean b = ch == BASE64PAD
                || ch < DECODE.length && DECODE[ch] != Byte.MAX_VALUE;
            if (b) {
                inputBuffer[inputBufferCount++] = ch;
                if (inputBufferCount == inputBuffer.length) {
                    inputBufferCount = 0;
                    outputBufferCount += decode0(inputBuffer, outputBuffer, outputBufferCount);
                }
            }
        }
        if (outputBufferCount == outputBuffer.length) {
            return outputBuffer;
        }
        byte[] ret = new byte[outputBufferCount];
        System.arraycopy(outputBuffer, 0, ret, 0, outputBufferCount);
        return ret;
    }

    public static byte[] decode(String data) {
        char[] inputBuffer = new char[4];
        int inputBufferCount = 0;
        byte[] outputBuffer = new byte[data.length() / 4 * 3 + 3];
        int outputBufferCount = 0;
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            boolean b = ch == BASE64PAD
                || ch < DECODE.length && DECODE[ch] != Byte.MAX_VALUE;
            if (b) {
                inputBuffer[inputBufferCount++] = ch;
                if (inputBufferCount == inputBuffer.length) {
                    inputBufferCount = 0;
                    outputBufferCount += decode0(inputBuffer, outputBuffer, outputBufferCount);
                }
            }
        }
        if (outputBufferCount == outputBuffer.length) {
            return outputBuffer;
        }
        byte[] ret = new byte[outputBufferCount];
        System.arraycopy(outputBuffer, 0, ret, 0, outputBufferCount);
        return ret;
    }

    public static void decode(char[] data, int off, int len, OutputStream ostream)
        throws IOException {
        char[] inputBuffer = new char[4];
        int inputBufferCount = 0;
        byte[] outputBuffer = new byte[3];
        for (int i = off; i < off + len; i++) {
            char ch = data[i];
            boolean b = ch == BASE64PAD
                || ch < DECODE.length && DECODE[ch] != Byte.MAX_VALUE;
            if (b) {
                inputBuffer[inputBufferCount++] = ch;
                if (inputBufferCount == inputBuffer.length) {
                    inputBufferCount = 0;
                    int obufcount = decode0(inputBuffer, outputBuffer, 0);
                    ostream.write(outputBuffer, 0, obufcount);
                }
            }
        }
    }

    public static void decode(String data, OutputStream ostream) throws IOException {
        char[] inputBuffer = new char[4];
        int inputBufferCount = 0;
        byte[] outputBuffer = new byte[3];
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            boolean b = (ch == BASE64PAD)
                || (ch < DECODE.length && DECODE[ch] != Byte.MAX_VALUE);
            if (b) {
                inputBuffer[inputBufferCount++] = ch;
                if (inputBufferCount == inputBuffer.length) {
                    inputBufferCount = 0;
                    int obufcount = decode0(inputBuffer, outputBuffer, 0);
                    ostream.write(outputBuffer, 0, obufcount);
                }
            }
        }
    }

    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    public static String encode(byte[] data, int off, int len) {
        if (len <= 0) {
            return "";
        }
        char[] out = new char[len / 3 * 4 + 4];
        int rightIndex = off;
        int windex = 0;
        int rest = len - off;
        while (rest >= 3) {
            int i = ((data[rightIndex] & 0xff) << 16)
                + ((data[rightIndex + 1] & 0xff) << 8)
                + (data[rightIndex + 2] & 0xff);
            out[windex++] = BASE64CHAR[i >> 18];
            out[windex++] = BASE64CHAR[(i >> 12) & 0x3f];
            out[windex++] = BASE64CHAR[(i >> 6) & 0x3f];
            out[windex++] = BASE64CHAR[i & 0x3f];
            rightIndex += 3;
            rest -= 3;
        }
        if (rest == 1) {
            int i = data[rightIndex] & 0xff;
            out[windex++] = BASE64CHAR[i >> 2];
            out[windex++] = BASE64CHAR[(i << 4) & 0x3f];
            out[windex++] = BASE64PAD;
            out[windex++] = BASE64PAD;
        } else if (rest == 2) {
            int i = ((data[rightIndex] & 0xff) << 8) + (data[rightIndex + 1] & 0xff);
            out[windex++] = BASE64CHAR[i >> 10];
            out[windex++] = BASE64CHAR[(i >> 4) & 0x3f];
            out[windex++] = BASE64CHAR[(i << 2) & 0x3f];
            out[windex++] = BASE64PAD;
        }
        return new String(out, 0, windex);
    }

    public static void encode(byte[] data, int offset, int len, OutputStream outputStream)
        throws IOException {
        if (len <= 0) {
            return;
        }
        byte[] out = new byte[4];
        int rightIndex = offset;
        int rest = len - offset;
        while (rest >= 3) {
            int i = ((data[rightIndex] & 0xff) << 16)
                + ((data[rightIndex + 1] & 0xff) << 8)
                + (data[rightIndex + 2] & 0xff);
            out[0] = (byte) BASE64CHAR[i >> 18];
            out[1] = (byte) BASE64CHAR[(i >> 12) & 0x3f];
            out[2] = (byte) BASE64CHAR[(i >> 6) & 0x3f];
            out[3] = (byte) BASE64CHAR[i & 0x3f];
            outputStream.write(out, 0, 4);
            rightIndex += 3;
            rest -= 3;
        }
        if (rest == 1) {
            int i = data[rightIndex] & 0xff;
            out[0] = (byte) BASE64CHAR[i >> 2];
            out[1] = (byte) BASE64CHAR[(i << 4) & 0x3f];
            out[2] = (byte) BASE64PAD;
            out[3] = (byte) BASE64PAD;
            outputStream.write(out, 0, 4);
            return;
        }
        if (rest == 2) {
            int i = ((data[rightIndex] & 0xff) << 8) + (data[rightIndex + 1] & 0xff);
            out[0] = (byte) BASE64CHAR[i >> 10];
            out[1] = (byte) BASE64CHAR[(i >> 4) & 0x3f];
            out[2] = (byte) BASE64CHAR[(i << 2) & 0x3f];
            out[3] = (byte) BASE64PAD;
            outputStream.write(out, 0, 4);
        }
    }

    public static void encode(byte[] data, int off, int len, Writer writer) throws IOException {
        if (len <= 0) {
            return;
        }
        char[] out = new char[4];
        int rightIndex = off;
        int rest = len - off;
        int output = 0;
        while (rest >= 3) {
            int i = ((data[rightIndex] & 0xff) << 16)
                + ((data[rightIndex + 1] & 0xff) << 8)
                + (data[rightIndex + 2] & 0xff);
            out[0] = BASE64CHAR[i >> 18];
            out[1] = BASE64CHAR[(i >> 12) & 0x3f];
            out[2] = BASE64CHAR[(i >> 6) & 0x3f];
            out[3] = BASE64CHAR[i & 0x3f];
            writer.write(out, 0, 4);
            rightIndex += 3;
            rest -= 3;
            output += 4;
            if (output % 76 == 0) {
                writer.write("\n");
            }
        }
        if (rest == 1) {
            int i = data[rightIndex] & 0xff;
            out[0] = BASE64CHAR[i >> 2];
            out[1] = BASE64CHAR[(i << 4) & 0x3f];
            out[2] = BASE64PAD;
            out[3] = BASE64PAD;
            writer.write(out, 0, 4);
            return;
        }
        if (rest == 2) {
            int i = ((data[rightIndex] & 0xff) << 8) + (data[rightIndex + 1] & 0xff);
            out[0] = BASE64CHAR[i >> 10];
            out[1] = BASE64CHAR[(i >> 4) & 0x3f];
            out[2] = BASE64CHAR[(i << 2) & 0x3f];
            out[3] = BASE64PAD;
            writer.write(out, 0, 4);
        }
    }
}
