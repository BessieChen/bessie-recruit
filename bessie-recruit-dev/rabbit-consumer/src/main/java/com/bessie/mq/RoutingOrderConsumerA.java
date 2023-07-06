package com.bessie.mq;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-07 03:04
 **/

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 构建生产者，发送消息
 */

/**
 * 构建消费者，监听消息
 */
public class RoutingOrderConsumerA {

    public static void main(String[] args) throws Exception {

        // 1. 创建连接工厂以及参数配置
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.219");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 2. 创建连接Connection
        Connection connection = factory.newConnection();

        // 3. 创建管道Channel
        Channel channel = connection.createChannel();

        // 4. 创建队列Queue（简单模式不需要交换机Exchange）
        String routing_queue_order = "routing_queue_order";
        channel.queueDeclare(routing_queue_order, true, false, false, null);


        Consumer consumer = new DefaultConsumer(channel){
            /**
             * 重写消息配送方法
             * @param consumerTag 消息的标签（标识）
             * @param envelope  信封（一些信息，比如交换机路由等等信息）
             * @param properties 配置信息
             * @param body 收到的消息数据
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("body = " + new String(body));
            }
        };
        /**
         * queue: 监听的队列名
         * autoAck: 是否自动确认，true：告知mq消费者已经消费的确认通知
         * callback: 回调函数，处理监听到的消息
         */
        channel.basicConsume(routing_queue_order, true, consumer);

        // 不需要关闭连接，则持续监听

    }
}