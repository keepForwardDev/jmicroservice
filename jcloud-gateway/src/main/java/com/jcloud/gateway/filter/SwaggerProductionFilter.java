package com.jcloud.gateway.filter;

import com.github.xiaoymin.knife4j.spring.filter.BasicFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * to protect swagger docs
 *
 * @author jiaxm
 * @date 2021/11/2
 */
@Profile("prod")
@Component
public class SwaggerProductionFilter extends BasicFilter implements WebFilter {

    @Value("${springfox.documentation.swagger.username:admin}")
    private String username;

    @Value("${springfox.documentation.swagger.password:jcloud}")
    private String password;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (this.match(exchange.getRequest().getURI().toString())) {
            return exchange.getSession().flatMap(session -> {
                Object authSession = session.getAttribute("SwaggerBootstrapUiBasicAuthSession");
                if (authSession != null) {
                    return chain.filter(exchange);
                } else {
                    return attemptLogin(exchange, chain);
                }
            });
        }
        return chain.filter(exchange);
    }

    private Mono<Void> attemptLogin(ServerWebExchange exchange, WebFilterChain chain) {
        String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (auth != null && !"".equals(auth)) {
            String userAndPass = this.decodeBase64(auth.substring(6));
            String[] upArr = userAndPass.split(":");
            if (upArr.length != 2) {
                return this.writeForbiddenCode(exchange);
            }
            String iptUser = upArr[0];
            String iptPass = upArr[1];
            if (iptUser.equals(username) && iptPass.equals(this.password)) {
                return exchange.getSession().doOnNext(r -> r.getAttributes().put("SwaggerBootstrapUiBasicAuthSession", this.username))
                        .then(chain.filter(exchange));
            }

            return this.writeForbiddenCode(exchange);
        }
        return writeForbiddenCode(exchange);
    }



    private Mono<Void> writeForbiddenCode(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        return exchange.getResponse().writeWith(Mono.fromSupplier(() -> {
            String msg = "You do not have permission to access this resource";
            return bufferFactory.wrap(msg.getBytes());
        }));
    }
}
