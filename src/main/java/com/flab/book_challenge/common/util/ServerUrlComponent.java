package com.flab.book_challenge.common.util;

import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Getter
public class ServerUrlComponent {
    @Value("${server.url}")
    private String url;

    private ServerUrlComponent() {
    }

    public String buildURL(String path, Map<String, Object> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + path);
        params.forEach(builder::queryParam);

        return builder.build().toUriString();
    }

}
