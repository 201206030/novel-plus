package com.java2nb.novel.core.utils;

import com.java2nb.novel.core.config.HttpProxyProperties;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;

@Component
public class RestTemplates {

    private static HttpProxyProperties httpProxyProperties;

    RestTemplates(HttpProxyProperties properties) {
        httpProxyProperties = properties;
    }

    @SneakyThrows
    public static RestTemplate newInstance(String charset) {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        //忽略证书
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", csf)
            .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

        //连接池的最大连接数，0代表不限；如果取0，需要考虑连接泄露导致系统崩溃的后果
        connectionManager.setMaxTotal(1000);
        //每个路由的最大连接数,如果只调用一个地址,可以将其设置为最大连接数
        connectionManager.setDefaultMaxPerRoute(300);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        if (Objects.nonNull(httpProxyProperties) && Boolean.TRUE.equals(httpProxyProperties.getEnabled())) {
            HttpHost proxy = new HttpHost(httpProxyProperties.getIp(), httpProxyProperties.getPort());
            clientBuilder.setProxy(proxy);
            if (StringUtils.isNotBlank(httpProxyProperties.getUsername()) && StringUtils.isNotBlank(
                httpProxyProperties.getPassword())) {
                // 创建CredentialsProvider实例并添加代理认证信息
                CredentialsProvider provider = new BasicCredentialsProvider();
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    httpProxyProperties.getUsername(), httpProxyProperties.getPassword());
                provider.setCredentials(AuthScope.ANY, credentials);
                clientBuilder.setDefaultCredentialsProvider(provider);
            }
        }
        CloseableHttpClient httpClient = clientBuilder.setConnectionManager(connectionManager)
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(3000);
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(30000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName(charset));
                break;
            }
        }
        return restTemplate;
    }

}
