package com.bessie.mq;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-07 03:04
 **/

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 构建发布订阅模式的生产者，发送消息
 */
public class PubSubProducer {

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

        // 4. 创建交换机 Exchange
        /**
         * exchange: 交换机的名称
         * type: 交换机的类型
         *      FANOUT("fanout"): 广播模式，发布订阅，把消息发送给所有的绑定的队列
         *      DIRECT("direct"): 定向投递模式，把消息发送给指定的“routing key”的队列
         *      TOPIC("topic"): 通配符模式，把消息发送给符合的“routing pattern”的队列
         *      HEADERS("headers"): 使用率不多，参数匹配
         * durable: 是否持久化
         * autoDelete: 是否自动删除
         * internal: 内部意思，true：表示当前exchange是rabbitmq内部使用的，用户创建的队列不会消费该类型交换机下的消息，所以我们一般使用false即可
         * arguments: map类型的参数
         */
        String fanout_exchange = "fanout_exchange";
        channel.exchangeDeclare(fanout_exchange,
                BuiltinExchangeType.FANOUT,
                true, false, false, null);

        // 定义两个队列
        String fanout_queue_a = "fanout_queue_a";
        String fanout_queue_b = "fanout_queue_b";
        channel.queueDeclare(fanout_queue_a, true, false, false, null);
        channel.queueDeclare(fanout_queue_b, true, false, false, null);

        // 绑定交换机和队列
        channel.queueBind(fanout_queue_a, fanout_exchange, "");
        channel.queueBind(fanout_queue_b, fanout_exchange, "");

        for (int i = 0 ; i < 10 ; i ++) {
            String task = "bessie 开始上班, 搬砖喽~ 开启任务[" + i + "]";
            channel.basicPublish(fanout_exchange, "", null, task.getBytes());
        }

        channel.close();
        connection.close();
    }
}