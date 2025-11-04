package com.github.zhuchao941.canal2sql.util;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;

/**
 * @author 赵威 2025/7/9 15:54
 */
public class RabbitMQSender {
    private static final String QUEUE_NAME = "sql_log_queue";
    private static Channel channel;

    static {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.188.1");
            factory.setUsername("zhaowei");
            factory.setPassword("3219582981zw..");

            Connection connection = factory.newConnection();
            channel = connection.createChannel();

            // 声明队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(SQL message) {
        try {
            channel.basicPublish("", QUEUE_NAME, null, JSONObject.toJSONBytes(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Data
class SQL {
    String operateDate;
    String schemaName;
    String tableName;
    String type;
    String operateSql;
    String rollbackSql;

}