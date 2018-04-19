package top.wboost.common.utils.web.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HTML相关工具类
 */
public class HtmlUtil {

    /**
     * 输出json数据
     * @author jwSun
     * @date 2016年7月27日 上午9:46:16
     */
    public static void writerJson(HttpServletResponse response, String data) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        writer(response, data);
    }

    /**
     * 输出数据
     * @author jwSun
     * @date 2016年7月27日 上午9:45:49
     */
    private static void writer(HttpServletResponse response, String data) {
        PrintWriter out = null;
        try {
            // 设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 获取用户的Ip地址
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip;
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 是否为ajax请求
     * @param request
     * @return
     */
    public static Boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if (header == null) {
            return false;
        } else if (header.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得绝对路径
     * @date 2016年9月23日 下午4:18:48
     * @param request
     * @param fileName
     * @return
     * @author jwSun
     */
    public static String getFilePath(HttpServletRequest request, String fileName) {
        String realPath = request.getSession().getServletContext().getRealPath("");
        String savePath = null;
        if (java.io.File.separator.equals("\\")) {//window环境下
            savePath = realPath.substring(0, realPath.lastIndexOf("\\"));
        } else {//linux环境下
            savePath = realPath.substring(0, realPath.lastIndexOf("/"));
        }
        return savePath;
    }

    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getBasePath() {
        try {
            HttpServletRequest request = getRequest();
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getBasePath(ServletRequest request) {
        try {
            HttpServletRequest httpRequest = toHttp(request);
            return httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort()
                    + httpRequest.getContextPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getBaseIpPath(ServletRequest request) {
        try {
            HttpServletRequest httpRequest = toHttp(request);
            return httpRequest.getScheme() + "://" + getIP(httpRequest) + ":" + httpRequest.getServerPort()
                    + httpRequest.getContextPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        try {
            return (HttpServletRequest) request;
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpServletResponse getResponse() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据url路径获得参数
     * @author jwSun
     * @date 2017年6月1日 上午11:12:01
     * @param originalUrl
     * @return
     */
    public static Map<String, String> getRequestUrlParam(String originalUrl) {
        Map<String, String> map = new HashMap<>();
        if (originalUrl.indexOf("?") != -1) {
            String params = originalUrl.substring(originalUrl.indexOf("?") + 1);
            String[] paramArray = params.split("&");
            List<String> s = new ArrayList<>();
            Collections.addAll(s, paramArray);
            s.forEach((String param) -> {
                String[] entry = param.split("=");
                if (entry.length == 1) {
                    map.put(entry[0], "");
                } else {
                    map.put(entry[0], entry[1]);
                }
            });
        }
        return map;
    }

}
