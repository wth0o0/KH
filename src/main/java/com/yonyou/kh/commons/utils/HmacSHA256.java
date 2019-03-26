package com.yonyou.kh.commons.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author : ${user}
 * @version : V1.0
 * @date : ${date} ${time}
 */
public class HmacSHA256 {

    /**
     * 定义加密方式
     * MAC算法可选以下多种算法
     * HmacSHA256
     */
    private final static String KEY_MAC = "HmacSHA256";

    /**
     * 全局数组
     */
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 构造函数
     */
    private HmacSHA256() {

    }

    /**
     * HMAC加密
     * @param data 需要加密的字节数组
     * @param key 密钥
     * @return 字节数组
     */
    public static String encryptHMAC(byte[] data, byte[] key) {
        SecretKey secretKey;
        byte[] bytes = null;
        try {
            secretKey = new SecretKeySpec(key, KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(bytes);
    }

    public static String encryptHMAC1(byte[] data, byte[] key) {
        SecretKey secretKey;
        byte[] bytes = null;
        try {
            secretKey = new SecretKeySpec(key, KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes2HexString(bytes);
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteToHexString(byte b) {
        int ret = b;
        //System.out.println("ret = " + ret);
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    private static String bytes2HexString(byte[] b) {

        String ret = "";

        for (int i = 0; i < b.length; i++) {

            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {

                hex = '0' + hex;

            }

            ret += hex.toString();

        }

        return ret;

    }

    public static void main(String[] args) throws Exception{
        String sec ="CzwUucfT1RrXhHWKxp35PGYD4BISnmFZ6tVsAQiakvd7MEegJqj8N2yb";
        byte[] sb = sec.getBytes();
        byte[] sb1 = sec.getBytes("UTF-8");
        String json = "{\"birthDate\":\"1990-01-01\",\"comId\":1,\"fuNum\":0,\"id\":-1,\"name\":\"test0001\",\"nickName\":\"test01\",\"phone\":\"15801234550\"}";
        String json1 = "{\"birthday\":\"1990-01-01\",\"define1\":-1,\"password\":\"123456\",\"phone\":\"15801234550\",\"realname\":\"test0001\",\"username\":\"15801234550\"}";
        
        String data = "{\"conditions\":[{\"name\":\"code\",\"value1\":\"storeCode\",\"type\":\"string\",\"op\":\"eq\"}],\"pager\":{\"pageIndex\":1,\"pageSize\":8},\"orders\":[{\"name\":\"province\",\"order\":\"desc\"}],\"fields\":[{\"name\":\"id\"},{\"name\":\"name\"},{\"name\":\"contact\"},{\"name\":\"address\"},{\"name\":\"province\"},{\"name\":\"city\"},{\"name\":\"area\"},{\"name\":\"code\"},{\"name\":\"erp_code\"}]}";
        
        byte[] jb = json.getBytes();
        byte[] jb1 = json.getBytes("UTF-8");
        
//        String aa = "{\"username\":\"zhangsan\",\"phone\":\"13888888888\"}";
        System.out.println(encryptHMAC(data.getBytes("UTF-8"),sb1));
//        System.out.println(HmacSHA2561.sign(jb1,sb1));
    }
}

