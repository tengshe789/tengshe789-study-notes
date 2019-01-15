package com.pansoft.xbrl.cloud.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;

/**
 * @program: xbrl-cloud-config
 * @description: 阿波罗客户端的配置类sample
 * @author: <a href="mailto:xuran@pansoft.com">tEngSHe789</a>
 * @create: 2019-01-15 14:40
 **/
@AllArgsConstructor
@Slf4j
public class ApolloRefreshConfig {
	/**
	 * 需要一个context域
	 */
	private final RefreshScope refreshScope;
	/**
	 * 这是随便写的一个样品配置类
	 */
	private final SampleConfig sampleConfig;

	/**
	 * apollo 自动刷新的核心配置
	 * @param changeEvent
	 */
	@ApolloConfigChangeListener
	public void onChange(ConfigChangeEvent changeEvent) {
		// apollo 管理界面的key 是否改变了
		boolean apolloKeyIsChange = false;
		// 拿到 已经改变的key的集合 查找
		for (String keys : changeEvent.changedKeys()){
			if (keys.startsWith("redis.cache")) {
				apolloKeyIsChange = true;
				break;
			}
		}
		//没改变则返回
		if (apolloKeyIsChange == false) {
			return;
		}
		//配置更新
		refreshScope.refresh("SampleConfig");
		log.info("正在刷新配置");
	}
}
