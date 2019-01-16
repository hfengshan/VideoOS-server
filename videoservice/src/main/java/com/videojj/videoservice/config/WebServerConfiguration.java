package com.videojj.videoservice.config;

import org.apache.catalina.core.AprLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class WebServerConfiguration {

    @Value("${configserver.port}")
    private Integer port;
    @Bean
    @Primary
    public TomcatServletWebServerFactory createEmbeddedServletContainerFactory()
    {
        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();

        tomcatFactory.setPort(port);

        tomcatFactory.addConnectorCustomizers(new ConfigTomcatConnectorCustomizer());
//        tomcatFactory.setProtocol("org.apache.coyote.http11.Http11AprProtocol");

        tomcatFactory.addContextLifecycleListeners(new AprLifecycleListener());

        Compression compression = new Compression();

        compression.setMimeTypes( new String[]{"text/html", "text/xml", "text/plain", "text/css", "text/javascript", "application/javascript", "application/json", "application/xml"});

        compression.setEnabled(true);

        compression.setMinResponseSize(1024);

        tomcatFactory.setCompression(compression);

        return tomcatFactory;
    }
}
