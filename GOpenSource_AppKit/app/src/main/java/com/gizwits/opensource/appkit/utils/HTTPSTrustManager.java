package com.gizwits.opensource.appkit.utils;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/**
 * @description 有一个信任管理器类负责决定是否信任远端的证书  X509TrustManager，更多移动开发内容请关注： http://blog.csdn.net/xiong_it
 * @charset UTF-8
 * @author xiong_it
 * @date 2015-7-16下午5:38:38
 * @version 
 */
public class HTTPSTrustManager implements X509TrustManager {  
	  
    private static TrustManager[] trustManagers;  
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};  
  
    /**
     * 该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，
     * 因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
     */
    @Override  
    public void checkClientTrusted(  
            java.security.cert.X509Certificate[] x509Certificates, String s)  
            throws java.security.cert.CertificateException {  
        // To change body of implemented methods use File | Settings | File  
        // Templates.  
    }  
    
  /**
   * 该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。在实现该方法时，
   * 也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
   */
    @Override  
    public void checkServerTrusted(  
            java.security.cert.X509Certificate[] x509Certificates, String s)  
            throws java.security.cert.CertificateException {  
        // To change body of implemented methods use File | Settings | File  
        // Templates.  
    }  
  
    public boolean isClientTrusted(X509Certificate[] chain) {  
        return true;  
    }  
  
    public boolean isServerTrusted(X509Certificate[] chain) {  
        return true;  
    }  
  
    @Override  
    public X509Certificate[] getAcceptedIssuers() {  
        return _AcceptedIssuers;  
    }  
  
    public static void allowAllSSL() {  
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {  
  
            @Override  
            public boolean verify(String arg0, SSLSession arg1) {  
                return true;  
            }  
  
        });  
  
        SSLContext context = null;  
        if (trustManagers == null) {  
            trustManagers = new TrustManager[] { new HTTPSTrustManager() };  
        }  
  
        try {  
            context = SSLContext.getInstance("TLS");  
            context.init(null, trustManagers, new SecureRandom());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        }  
  
        HttpsURLConnection.setDefaultSSLSocketFactory(context  
                .getSocketFactory());  
    }  
  
} 