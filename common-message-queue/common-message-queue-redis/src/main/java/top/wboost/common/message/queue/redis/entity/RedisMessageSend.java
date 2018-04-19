package top.wboost.common.message.queue.redis.entity;

import top.wboost.common.message.queue.entity.BaseMessageSend;

public class RedisMessageSend implements BaseMessageSend {

    @Override
    public Object getBody() {
        return body;
    }

    private Object body;

    public void setBody(Object body) {
        this.body = body;
    }

}
