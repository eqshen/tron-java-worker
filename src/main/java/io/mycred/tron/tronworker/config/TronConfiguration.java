package io.mycred.tron.tronworker.config;

import io.mycred.tron.tronworker.enums.NetTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tron.config")
@Data
public class TronConfiguration {
    private NetTypeEnum netType;
    private String rpcVersion;
    private String fullNode;
    private String solidityNode;
    private String eventServer;
}
