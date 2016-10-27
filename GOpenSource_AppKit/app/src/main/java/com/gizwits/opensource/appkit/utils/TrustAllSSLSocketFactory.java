package com.gizwits.opensource.appkit.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import android.os.Build;

/**
 * 
 * @description ssl安全链接工厂 更多移动开发内容请关注： http://blog.csdn.net/xiong_it
 * @charset UTF-8
 * @author xiong_it
 * @date 2015-7-20下午4:43:25
 * @version
 */
public class TrustAllSSLSocketFactory  extends SSLSocketFactory   {
	
	private javax.net.ssl.SSLSocketFactory factory;
	  private static TrustAllSSLSocketFactory instance;

	  private TrustAllSSLSocketFactory() throws KeyManagementException, UnrecoverableKeyException,
	      NoSuchAlgorithmException, KeyStoreException {
	    super(null);

	    SSLContext context = SSLContext.getInstance("TLS");
	    context.init(null, new TrustManager[] { new TrustAllManager() }, null);
	    factory = context.getSocketFactory();
	    setHostnameVerifier(new X509HostnameVerifier() {

	      @Override
	      public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

	      }

	      @Override
	      public void verify(String host, X509Certificate cert) throws SSLException {

	      }

	      @Override
	      public void verify(String host, SSLSocket ssl) throws IOException {

	      }

	      @Override
	      public boolean verify(String host, SSLSession session) {
	        return true;
	      }
	    });
	  }

	  public static SocketFactory getDefault() {
	    if (instance == null) {
	      try {
	        instance = new TrustAllSSLSocketFactory();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    return instance;
	  }

	  @Override
	  public Socket createSocket() throws IOException {
	    return factory.createSocket();
	  }

	  @Override
	  public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
	      throws IOException, UnknownHostException {
	    if (Build.VERSION.SDK_INT < 11) { // 3.0
	      injectHostname(socket, host);
	    }

	    return factory.createSocket(socket, host, port, autoClose);
	  }

	  private void injectHostname(Socket socket, String host) {
	    try {
	      Field field = InetAddress.class.getDeclaredField("hostName");
	      field.setAccessible(true);
	      field.set(socket.getInetAddress(), host);
	    } catch (Exception ignored) {
	    }
	  }

	  public class TrustAllManager implements X509TrustManager {

	    @Override
	    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
	        throws CertificateException {

	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
	        throws CertificateException {

	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	      return null;
	    }
	  }

	}