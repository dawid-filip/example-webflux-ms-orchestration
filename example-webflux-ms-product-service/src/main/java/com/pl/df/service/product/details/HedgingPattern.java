package com.pl.df.service.product.details;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class HedgingPattern implements ExchangeFilterFunction {

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;
    private final Long maxInstances;

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        var originalRequestUrl = clientRequest.url();
        var originalServiceName = originalRequestUrl.getHost();

        return this.reactiveDiscoveryClient.getInstances(originalServiceName)
                .collectList()
                .doOnNext(instances ->
                        log.info("Available [" + originalServiceName + "] instances [" + instances.size() + "]: " + instances))
                .map(serviceInstances -> reorder(serviceInstances))
                .flatMapMany(Flux::fromIterable)
                .take(maxInstances)
                .map(serviceInstance -> buildNewUri(serviceInstance, originalRequestUrl))
                .map(uri -> invokeOriginalRequest(uri, clientRequest, exchangeFunction))
                .collectList()
                .flatMap(list -> hedging(list));
    }

    private List<ServiceInstance> reorder(List<ServiceInstance> serviceInstances) {
        var reorderedServiceInstances = new ArrayList<>(serviceInstances);
        Collections.shuffle(reorderedServiceInstances);
        return reorderedServiceInstances;
    }

    private URI buildNewUri(ServiceInstance serviceInstance, URI originalRequestUrl) {
        return URI.create(originalRequestUrl.getScheme() + "://"
                        + serviceInstance.getHost() + ':'
                        + serviceInstance.getPort()
                        + originalRequestUrl.getPath());
    }

    private Mono<ClientResponse> invokeOriginalRequest(URI uri, ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        var newRequest = ClientRequest
                .create(clientRequest.method(), uri)
                .headers(headers -> headers.addAll(clientRequest.headers()))
                .cookies(cookies -> cookies.addAll(clientRequest.cookies()))
                .attributes(attributes -> attributes.putAll(clientRequest.attributes()))
                .body(clientRequest.body())
                .build();
        return exchangeFunction.exchange(newRequest)
                .doOnNext(cr -> log.info("Requesting " + newRequest.url() + " resource..."));
    }

    // the key method
    private Mono<ClientResponse> hedging(List<Mono<ClientResponse>> list) {
        return Flux.firstWithSignal(list)           // once first retrieved than rest are going to be terminated
                .timeout(Duration.ofMillis(5000))
                .singleOrEmpty();
    }

}
