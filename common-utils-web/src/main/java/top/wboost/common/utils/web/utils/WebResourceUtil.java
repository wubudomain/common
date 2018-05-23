package top.wboost.common.utils.web.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

/**
 * 网络资源工具
 * 推荐使用{@link HttpClientUtil}}
 * @author jwSun
 * @date 2017年2月5日 下午4:49:04
 */
@Deprecated
public class WebResourceUtil {

    public enum RequestMethod {
        GET, POST
    }

    private static SSLSocketFactory sslSocketFactory;

    public static SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    /**
     * 获取指定URL页面或资源
     * @author sjw
     * @date 2016年9月29日 下午5:11:14
     * @param urlString url地址
     */
    public static String getURLResponse(String urlString) {
        return getURLResponse(urlString, null, RequestMethod.GET, "utf-8", null);
    }

    /**
     * 获取指定URL页面或资源
     * @author sjw
     * @date 2016年9月29日 下午5:11:58
     * @param urlString
     *            url地址
     * @param method
     *            访问方法:GET/POST
     * @return
     */
    public static String getURLResponse(String urlString, Map<String, String> params) {
        return getURLResponse(urlString, params, RequestMethod.GET, "utf-8", null);
    }

    /**
     * 获取指定URL页面或资源
     * @author sjw
     * @date 2016年9月29日 下午5:11:58
     * @param urlString
     *            url地址
     * @param method
     *            访问方法:GET/POST
     * @return
     */
    public static String getURLResponse(String urlString, Map<String, String> params, RequestMethod method) {
        return getURLResponse(urlString, params, method, "utf-8", null);
    }

    /**
     * 获取指定URL页面或资源
     * @author sjw
     * @date 2016年9月29日 下午5:11:58
     * @param urlString
     *            url地址
     * @param method
     *            访问方法:GET/POST
     * @return
     */
    public static String getURLResponse(String urlString, Map<String, String> params, RequestMethod method,
            String charset) {
        return getURLResponse(urlString, params, method, charset, null);
    }

    /**
     * 获取指定URL页面或资源
     * @author sjw
     * @date 2016年9月29日 下午5:13:06
     * @param urlString
     *            url地址
     * @param method
     *            访问方法:GET/POST
     * @param charset
     *            访问到的资源编码
     * @return
     */
    public static String getURLResponse(String urlString, RequestMethod method, String charset) {
        return getURLResponse(urlString, null, method, charset, null);
    }

    public static String getURLResponse(String urlString, Map<String, String> params, RequestMethod method,
            String charset, Map<String, String> headers) {
        return getURLResponse(urlString, params, method, charset, headers, "form");
    }

    public static String getURLResponseJson(String urlString, Map<String, Object> params, RequestMethod method) {
        return getURLResponse(urlString, params, method, "utf-8", null, "json");
    }

    /**
     * 获取指定URL页面或资源
     * @param urlString
     *            URL
     * @param method
     *            "GET"/"POST"
     * @param headers
     *            Map<String,String> header参数
     * @param params
     *            Map<String,String> 传递参数
     * @author sjw
     */
    public static String getURLResponse(String urlString, Map<String, ?> params, RequestMethod method, String charset,
            Map<String, String> headers, String paramType) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            if (charset == null)
                charset = "utf-8";

            String paramStr = getParams(params, paramType);

            if ("GET".equalsIgnoreCase(method.toString())) {
                if (!"".equals(paramStr)) {
                    urlString = urlString + "?" + paramStr;
                }
            }

            connection = getConnection(urlString);
            setHeader(connection, headers);

            if (RequestMethod.POST.toString().equalsIgnoreCase(method.toString())) {
                beforePost(connection, paramStr, paramType);
            } else if (RequestMethod.GET.toString().equalsIgnoreCase(method.toString())) {
                beforeGet(connection, paramStr);
            } else {
                return "";
            }

            return readStream(connection.getInputStream(), charset);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            closeQuietly(bufferedInputStream);
            closeQuietly(inputStream);
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void beforeGet(HttpURLConnection connection, String paramStr) throws Exception {
        connection.setRequestMethod(RequestMethod.GET.toString());
    }

    private static void beforePost(HttpURLConnection connection, String paramStr, String paramType) {
        DataOutputStream dataOutputStream = null;
        try {
            connection.setRequestMethod(RequestMethod.POST.toString());
            if (paramType.equals("json")) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            }
            if (paramStr != null) {
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(paramStr);
                dataOutputStream.flush();
                dataOutputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(dataOutputStream);
        }

    }

    private static void closeQuietly(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeQuietly(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (null != headers) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = headers.get(key);
                connection.setRequestProperty(key, value);
            }
        }
    }

    private static String getParams(Map<String, ?> params, String paramsType) throws Exception {
        if (paramsType.equals("json")) {
            return JSONObject.toJSONString(params);
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (null != params) {
            Iterator<?> mapIterator = params.entrySet().iterator();
            while (mapIterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Entry<String, ?> mapEntry = (Entry<String, ?>) mapIterator.next();
                if ("".equals(stringBuffer.toString())) {
                    stringBuffer.append(
                            mapEntry.getKey() + "=" + URLEncoder.encode(mapEntry.getValue().toString(), "utf-8"));
                } else {
                    stringBuffer.append(
                            "&" + mapEntry.getKey() + "=" + URLEncoder.encode(mapEntry.getValue().toString(), "utf-8"));
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 流转化为文本
     * @author jwSun
     * @date 2017年4月20日 下午6:43:17
     * @param inputStream
     * @param charset
     * @return
     */
    private static String readStream(InputStream inputStream, String charset) {
        StringBuffer sb = new StringBuffer();
        try {
            int len = 0;
            byte[] b = new byte[1024];
            sb.setLength(0);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            while ((len = bufferedInputStream.read(b)) != -1) {
                sb.append(new String(b, 0, len, charset));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static HttpURLConnection getConnection(String urlString) {
        if (urlString == null || urlString == "") {
            throw new RuntimeException("urlString is null");
        }
        if (urlString.indexOf("http") == -1) {
            urlString = "http://" + urlString;
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            if (urlString.indexOf("https://") != -1) {
                connection = getHttpsConnection(urlString, url);
            }
            if (connection == null) {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setDoInput(true);// 可以使用conn.getOutputStream().write()
            connection.setDoOutput(true);// 可以使用conn.getInputStream().read()
            connection.setUseCaches(false);// 请求不能使用缓存
            connection.setInstanceFollowRedirects(true);// 设置能自动执行重定向
        } catch (Exception e) {
        }

        return connection;
    }

    public static HttpURLConnection getHttpsConnection(String urlString, URL url) {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslSocketFactory);
        } catch (Exception e) {

        }
        return connection;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        String resp = getURLResponseJson("www.baidu.com", map, RequestMethod.POST);
        System.out.println(resp);
    }
}
