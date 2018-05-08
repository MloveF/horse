package com.kaishengit.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.swing.text.TabExpander;

@Component
public class MessageConsumer {

    @JmsListener(destination = "spring-queue")
    public void queueMessageConsumer(Message message) {

        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("获取的内容是：" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    @JmsListener(destination = "spring-topic",containerFactory = "jmsListenerContainerFactory")
    public void topicMessageConsumer(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {
            System.out.println("获得的主题内容是：" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
