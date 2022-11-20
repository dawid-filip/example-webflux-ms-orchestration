# example-webflux-ms-orchestration
Simple [WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) microservice orchestration with 
[load balancer](https://spring.io/guides/gs/service-registration-and-discovery/) and 
[gateway](https://spring.io/projects/spring-cloud-gateway) example.

## How to run
Follow steps in order:
1. Package `example-webflux-ms-lib` module and then,
2. Run `example-webflux-ms-eureka-server` module,
3. Run `example-webflux-ms-gateway` module,
4. Run `example-webflux-ms-product-details-service` module and,
5. Run `example-webflux-ms-product-service` module.

## How to test
Through gateway API:
- http://localhost:8080/public
- http://localhost:8080/product
- http://localhost:8080/product/{id}
- http://localhost:8080/product-details
- http://localhost:8080/product-details/{id}

Direct API:
- http://localhost:8091/public
- http://localhost:8091/product
- http://localhost:8091/product/{id}
- http://localhost:8092/product-details
- http://localhost:8092/product-details/{id}


## Requirements
- `JDK 17` or higher