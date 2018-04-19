package top.wboost.common.message.queue.activemq.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import top.wboost.common.message.queue.activemq.messageEntity.BaseActiveMQReceive;
import top.wboost.common.message.queue.activemq.messageEntity.BaseActiveMQSend;
import top.wboost.common.message.queue.activemq.producer.ProducerService;
import top.wboost.common.message.queue.interfaces.MessageQueueClient;

@Component("activemqMessageQueueClient")
public class ActivemqMessageQueueClient implements MessageQueueClient<BaseActiveMQSend, BaseActiveMQReceive> {

    private static final String messageQueueName = "message:queue:";

    @Resource(name = "producerService")
    private ProducerService producerService;

    @Override
    public Object sendMessage(BaseActiveMQSend messageSend) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseActiveMQReceive getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
