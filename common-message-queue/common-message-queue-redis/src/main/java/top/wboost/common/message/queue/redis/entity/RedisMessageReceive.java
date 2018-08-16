package top.wboost.common.message.queue.redis.entity;

import top.wboost.common.message.queue.entity.BaseMessageReceive;

public class RedisMessageReceive implements BaseMessageReceive {

    private Object body;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}
