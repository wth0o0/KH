package com.yonyou.kh.commons.utils;

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

import org.apache.commons.collections.map.HashedMap;
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
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author : wuzhe
 * @version : V1.0
 */
@Component
public class YlaModel {

    private final static String c_appid = "";
    private final static String c_secret = "";
    private final static String c_token = "";

    private final static String url_pre = "";
    private final static String p_secret = "";
    private final static String p_token = "";


    public HttpPost getHttpPost(String url,String param ,String secret){
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(param, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        String sign = null;
        try {
            sign = HmacSHA256.encryptHMAC(param.getBytes("UTF-8"), secret.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("调用u上传接口生成加密字符失败：param="+param+";url="+url);
        }
        httpPost.setHeader("X-Authorization", sign);
        return httpPost;
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }

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


    private HashMap<String, Object> fromJson2Map(String jsonString) {
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
    private  List<HashMap<String, Object>> handleJSONArray(JSONArray jsonArray){
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

    /**
     * 通过会员id 查询会员详情
     *  @param
     * @return    :
     *  @createDate   : 2017/6/13 17:39
     *  @author          : yangfw@xinfushe.com
     *  @version         : v1.0
     *  @updateDate     : 2017/6/13 17:39
     *  @updateAuthor  :
     */
    public Object queryEmpDetail(String mid) throws Exception {
        Map<String,Object> param = new HashMap();
        Map<String,String> user = new HashedMap();
        user.put("name","mid");
        user.put("value1",mid);
        user.put("type","string");
        user.put("op","eq");
        List<Map> list = new ArrayList<>();
        list.add(user);
        param.put("conditions",list);

        String url = url_pre + "member/query/v1?access_token="+p_token;
        HttpClient client = wrapClient(url);
        HttpPost httpPost = getHttpPost(url, JSONObject.toJSON(param).toString(), p_secret);
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
            }
        }
        return datas;
    }


    public static void main(String[] args) throws Exception {

        new YlaModel().queryEmpDetail("yl_PCiDBJsVg4bk");

    }
}
