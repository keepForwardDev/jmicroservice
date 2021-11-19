package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jcloud.admin.entity.SysLog;
import com.jcloud.admin.mapper.SysLogMapper;
import com.jcloud.admin.service.LogPersistStrategy;
import com.jcloud.common.consts.Const;
import com.jcloud.mq.config.AmqpConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 保存到mq 进行削峰，在利用订阅线程持久化
 * @author jiaxm
 * @date 2021/9/18
 */
@Profile(value = Const.PROFILE_PROD)
@Service
@Import(RabbitAutoConfiguration.class)
public class MqLogPersist implements LogPersistStrategy {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PrivilegesManager privilegesManager;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Long persist(SysLog sysLog) {
        Long id = IdWorker.getId();
        sysLog.setId(id);
        rabbitTemplate.convertAndSend(AmqpConfig.API_LOG_EXCHANGE_NAME, StringUtils.EMPTY, sysLog);
        return id;
    }


    @RabbitHandler
    @RabbitListener(queues = AmqpConfig.LOG_QUEUE_NAME)
    public void apiRabbitListener(SysLog log, Channel channel, Message message) throws IOException {
        String content = log.getContent();
        // apiLimit:2:data-center:jcloud-admin:/user/checkPhone/{phone}
        String[] apiArray = content.split(StringPool.COLON);
        Integer privilegeType = Integer.valueOf(apiArray[1]);
        privilegesManager.decrApi(privilegeType, apiArray[2], apiArray[4], apiArray[3]);
        log.setCreateTime(new Date());
        sysLogMapper.insert(log);
        // 手动提交ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
