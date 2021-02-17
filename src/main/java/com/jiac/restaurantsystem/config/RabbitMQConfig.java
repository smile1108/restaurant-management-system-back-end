package com.jiac.restaurantsystem.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FileName: RabbitMQConfig
 * Author: Jiac
 * Date: 2020/10/14 22:33
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_INFORM_EMAIL = "user_register_email";
//    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
//    public static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";
//    public static final String ROUTINGKEY_EMAIL = "inform_email";
//    public static final String ROUTINGKEY_SMS = "inform_sms";

    //声明交换机
//    @Bean(EXCHANGE_ROUTING_INFORM)
//    public Exchange EXCHANGE_ROUTING_INFORM(){
//        return ExchangeBuilder.directExchange(EXCHANGE_ROUTING_INFORM)
//                .durable(true)   //mq重启之后这个交换机还在
//                .build();
//    }

    //声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

//    @Bean(QUEUE_INFORM_SMS)
//    public Queue QUEUE_INFORM_SMS(){
//        return new Queue(QUEUE_INFORM_SMS);
//    }
//
//    //绑定交换机和队列
//    @Bean
//    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL)Queue queue,
//                                              @Qualifier(EXCHANGE_ROUTING_INFORM)Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
//    }
//
//    @Bean
//    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS)Queue queue,
//                                              @Qualifier(EXCHANGE_ROUTING_INFORM)Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
//    }
}
