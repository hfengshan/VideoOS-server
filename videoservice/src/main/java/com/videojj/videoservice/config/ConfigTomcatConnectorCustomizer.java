package com.videojj.videoservice.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;

class ConfigTomcatConnectorCustomizer implements TomcatConnectorCustomizer
{
    public void customize(Connector connector)
    {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//        Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
//        Http11AprProtocol protocol = (Http11AprProtocol) connector.getProtocolHandler();
        //设置最大连接数
        protocol.setMaxConnections(5000);
        //设置最大线程数
        protocol.setMaxThreads(2000);

        protocol.setConnectionTimeout(30000);
    }
}