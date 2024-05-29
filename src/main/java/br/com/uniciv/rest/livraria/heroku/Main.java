package br.com.uniciv.rest.livraria.heroku;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.internal.HttpConnection;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import jakarta.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import jakarta.servlet.annotation.ServletSecurity.TransportGuarantee;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;

import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Main {

    public static void main(String[] args) throws Exception{
    	
    	File keyStoreFile = new File("server.keystore");
    	final Server server = new Server();
    	
    	HttpConfiguration httpConfig = new HttpConfiguration();
    	httpConfig.setSecureScheme("https");
    	httpConfig.setSecurePort(8443);
		
    	
    	/**
    	 * Foi necessário adicionar para remover a segurança SNI que não funciona localmente.
		*/
    	SecureRequestCustomizer src = new SecureRequestCustomizer();
    	src.setSniHostCheck(false);
    	httpConfig.addCustomizer(src);
    	
    	ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
    	http.setPort(8080);
    	
    	SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
    	sslContextFactory.setKeyStorePath(keyStoreFile.getAbsolutePath());
    	sslContextFactory.setKeyStorePassword("livraria");
    	sslContextFactory.setKeyManagerPassword("livraria");
    	
    	HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
    	ServerConnector https = new ServerConnector(server, 
    			new SslConnectionFactory(sslContextFactory, 
    				HttpVersion.HTTP_1_1.asString()), 
    			new HttpConnectionFactory(httpsConfig));
    	
    	https.setPort(8443);
    	
    	server.setConnectors(new ServerConnector[] {http, https});
    	
    	/* Código antes de adicionar o ssl - verificar no commit do eclipse.
        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        */
        
        final WebAppContext root = new WebAppContext();

        root.setContextPath("/livraria-virtual");
        // Parent loader priority is a class loader setting that Jetty accepts.
        // By default Jetty will behave like most web containers in that it will
        // allow your application to replace non-server libraries that are part of the
        // container. Setting parent loader priority to true changes this behavior.
        // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);

        final String webappDirLocation = "src/main/webapp/";
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setBaseResourceAsString(webappDirLocation);
        
        /**
         * Implementando autenticação e autorização de usuário
         * */
        HashLoginService loginService = new HashLoginService("MyRealm");
       
        
        ResourceHandler rh = new ResourceHandler();
        Resource base = ResourceFactory.of(rh).newResource("./myrealm.properties");

        loginService.setConfig(base);
		
        
        server.addBean(loginService);
        
        
        String[] roles = new String[] {"admin","user"};
        
        
        
       
        Constraint constraint = new ConstraintSecurityHandler().createConstraint("auth",roles,EmptyRoleSemantic.PERMIT,TransportGuarantee.CONFIDENTIAL);
    
        
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);
        
        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        server.setHandler(security);
        
        security.setConstraintMappings(Arrays.asList(mapping));
        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);      
        
        
        security.setHandler(root);

        server.start();
        server.join();
    }
    
}
