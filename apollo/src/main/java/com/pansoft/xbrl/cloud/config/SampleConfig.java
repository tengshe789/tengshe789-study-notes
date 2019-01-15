package com.pansoft.xbrl.cloud.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @program: xbrl-root
 * @description: 需要配置config样品
 * @author: <a href="mailto:xuran@pansoft.com">tEngSHe789</a>
 * @create: 2019-01-15 15:47
 **/
@Data
@ConfigurationProperties(prefix = "redis.cache")
@ConditionalOnProperty("redis.cache.enabled")
@Component("SampleConfig")
@RefreshScope
public class SampleConfig {

	//假设需要一个redis配置

	private int expireSeconds;
	private String clusterNodes;
	private int commandTimeout;



}
