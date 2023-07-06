package com.bessie.mq;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-07 03:04
 **/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 构建生产者，发送消息
 */
public class WorkQueueProducer {

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
        /**
         * queue: 队列名
         * durable: 是否持久化，true：重启后，队列依然存在，否则不存在
         * exclusive: 是否独占，true：只能有一个消费者监听这个队列，一般设置为false
         * autoDelete: 是否自动删除，true：当没有消费者的时候，自动删除队列
         * arguments: map类型其他参数
         */
        channel.queueDeclare("work_queue", true, false, false, null);

        // 5. 向队列发送消息
        /**
         * exchange: 交换机名称，简单模式没有，直接设置为 ""
         * routingKey: 路由key，映射路径，如果交换机是默认""，则路由key和队列名一致
         * props: 配置信息
         * body: 消息数据
         */
        for (int i = 0 ; i < 10 ; i ++) {
            String task = "bessie 开始上班, 搬砖喽~ 开启任务[" + i + "]";
            channel.basicPublish("", "work_queue", null, task.getBytes());
        }

        // 7. 释放
        channel.close();
        connection.close();
    }
}