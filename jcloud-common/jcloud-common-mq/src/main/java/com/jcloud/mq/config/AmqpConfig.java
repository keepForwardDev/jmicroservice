package com.jcloud.mq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 队列交换机配置,配置后自动生成
 * @see org.springframework.amqp.rabbit.core.RabbitAdmin
 * @author jiaxm
 * @date 2021/1/14
 */
@Configuration
public class AmqpConfig {

    /**
     * 日志队列
     */
    public static final String LOG_QUEUE_NAME = "syslog-queue";

    /**
     * 开放系统api交换机
     */
    public static final String API_LOG_EXCHANGE_NAME = "syslog.api";


    /**
     * api队列
     * @return
     */
    @Bean
    public Queue apiQueue() {
        /**
         * 参数明细
         * 1、queue 队列名称
         * 2、durable 是否持久化，如果持久化，mq重启后队列还在
         * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
         * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
         * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
         *
         */
        return QueueBuilder.durable(LOG_QUEUE_NAME).build();
    }

    /**
     * api 广播交换机，可能以后会集成api 短信服务， api邮件服务，那么只需要将队列绑定到交换机即可
     * @return
     */
    @Bean
    public FanoutExchange apiFanoutExchange() {
        return new FanoutExchange(API_LOG_EXCHANGE_NAME, true, false);
    }

    /**
     * 将交换机绑定到队列上
     * @return
     */
    @Bean
    public Binding apiQueueBindingToApiFanoutExchange(Queue apiQueue, FanoutExchange apiFanoutExchange) {
        return BindingBuilder.bind(apiQueue).to(apiFanoutExchange);
    }

    /**
     * 采用object mapper 转化数据、
     * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration objectProvider<MessageConverter> messageConverter
     * @param objectMapper
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }


}
