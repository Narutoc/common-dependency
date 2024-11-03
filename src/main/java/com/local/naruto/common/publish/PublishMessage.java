package com.local.naruto.common.publish;

import javax.annotation.Resource;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * 将消息发布至topic
 *
 * @author naruto chen
 * @since 2023-12-02
 */
@Service
public class PublishMessage {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 异步向topic发布message
     *
     * @param topic   topic
     * @param message message
     */
    public void publish(String topic, String message) {
        publishMessageToTopic(topic, message);
    }

    /**
     * 向topic发布message
     *
     * @param topic   topic
     * @param message message
     */
    private void publishMessageToTopic(String topic, String message) {
        try {
            RTopic publisherTopic = redissonClient.getTopic(topic);
            // 异步发送消息
            publisherTopic.publishAsync(message);
        } catch (Exception exception) {
            throw new RuntimeException("publish message error is: " + exception.getMessage());
        }
    }
}
