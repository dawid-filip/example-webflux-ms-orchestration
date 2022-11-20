package com.pl.df.lib;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Log4j2
public class HostPortLogWebFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final String UNKNOWN = "<unknown>";

    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> nextHandlerFunction) {
        final String hostName = getHostName(serverRequest);

        if (UNKNOWN.equalsIgnoreCase(hostName)) {
            log.warn("Client address is " + UNKNOWN);
            return ServerResponse.status(FORBIDDEN).build();
        }

        log.info("Client address is " + hostName);
        return nextHandlerFunction.handle(serverRequest);
    }

    private String getHostName(ServerRequest serverRequest) {
        InetSocketAddress inetSocketAddressLocal = serverRequest
                .localAddress()
                .get();

        if (Objects.nonNull(inetSocketAddressLocal)) {
            return extractHostNameAndPort(inetSocketAddressLocal);
        }

        InetSocketAddress inetSocketAddressRemote = serverRequest
                .remoteAddress()
                .get();

        if (Objects.nonNull(inetSocketAddressRemote)) {
            return extractHostNameAndPort(inetSocketAddressRemote);
        }

        return UNKNOWN;
    }

    private String extractHostNameAndPort(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort();
    }

}
