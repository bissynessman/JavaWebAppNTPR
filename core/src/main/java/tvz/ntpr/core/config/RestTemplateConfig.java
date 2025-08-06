package tvz.ntpr.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import tvz.ntpr.core.helper.JwtHolder;
import tvz.ntpr.core.security.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    private static final String CRON_API_KEY = "supersecretapikeythatihaveconcoctedformycronscheduler";

    @Autowired
    private JwtHolder jwtHolder;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RequestInterceptor(() -> jwtHolder));
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public RestTemplate tokensRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate cronRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().set("X-API-KEY", CRON_API_KEY);
            return execution.execute(request, body);
        });
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
