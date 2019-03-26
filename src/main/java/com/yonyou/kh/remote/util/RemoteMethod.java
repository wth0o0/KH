package com.yonyou.kh.remote.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.kh.commons.utils.HmacSHA256;
import com.yonyou.kh.commons.utils.ResultJson;

/**
 * 请求接口工具
 * 
 * @author wangjian
 *
 */
public class RemoteMethod {
	
	/**
	 * 请求门店信息
	 * @param url
	 * @param param
	 * @param secretKey
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static ResultJson connectionUVipFindStoreInfo(String url, JSONObject param, String secretKey) throws Exception{
		
		HttpClient client = wrapClient(url);
        HttpPost httpPost = getHttpPost(url, param.toString(), secretKey);
        String body = null;
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        }
        HashMap<String, Object> ret = fromJson2Map(body);
        String flag = ret.get("flag").toString();
        List<Map<String, Object>> datas = null;
        if("1".equals(flag)){
            if(ret.get("data")!=null){
                datas = (List<Map<String, Object>>)ret.get("data");
                return ResultJson.buildSuccess("success", datas);
            }
        }
        
        return ResultJson.buildError(ret.get("errormsg").toString());
		
	}
	
	
	
	
	
	
	/**
	 * 请求门店信息
	 * @param url
	 * @param param
	 * @param secretKey
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static ResultJson connectionUVipSendMessage(String url, List<JSONObject> param, String secretKey) throws Exception{
		
		HttpClient client = wrapClient(url);
        HttpPost httpPost = getHttpPost(url, param.toString(), secretKey);
        String body = null;
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        }
        HashMap<String, Object> ret = fromJson2Map(body);
        String flag = ret.get("flag").toString();
        if("1".equals(flag)){
            if(ret.get("success_phone")!=null){
                return ResultJson.buildSuccess("success", ret.get("success_phone"));
            }
        }
        	return ResultJson.buildError(ret.get("msg").toString());
        
	}
	
	
	
	
	
	
	
	
	//===========================华丽的分割线=================================================================================

	/**
	 * 根据U会员接口请求方式封装HttpPost
	 * @param url 请求地址
	 * @param param 请求参数
	 * @param secret secretKey
	 * @return
	 */
	private static HttpPost getHttpPost(String url,String param ,String secret){
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(param, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        String sign = null;
        try {
        	// 对请求参数进行加密，生成签名
            sign = HmacSHA256.encryptHMAC(param.getBytes("UTF-8"), secret.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("调用u上传接口生成加密字符失败：param="+param+";url="+url);
        }
        // 请求header添加验签
        httpPost.setHeader("X-Authorization", sign);
        return httpPost;
    }
	
	/**
	 * 处理请求协议
	 * @param host
	 * @return
	 */
	private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }
	
	/**
	 * https请求
	 * @param httpClient
	 */
	private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
	
	private static HashMap<String, Object> fromJson2Map(String jsonString) {
        HashMap jsonMap = JSON.parseObject(jsonString, HashMap.class);

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        for(Iterator iter = jsonMap.keySet().iterator(); iter.hasNext();){
            String key = (String)iter.next();
            if(jsonMap.get(key) instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)jsonMap.get(key);
                List list = handleJSONArray(jsonArray);
                resultMap.put(key, list);
            }else{
                resultMap.put(key, jsonMap.get(key));
            }
        }
        return resultMap;
    }
	
	private static List<HashMap<String, Object>> handleJSONArray(JSONArray jsonArray){
        List list = new ArrayList();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            HashMap map = new HashMap<String, Object>();
            for (Map.Entry entry : jsonObject.entrySet()) {
                if(entry.getValue() instanceof  JSONArray){
                    map.put((String)entry.getKey(), handleJSONArray((JSONArray)entry.getValue()));
                }else{
                    map.put((String)entry.getKey(), entry.getValue());
                }
            }
            list.add(map);
        }
        return list;
    }
	
}
