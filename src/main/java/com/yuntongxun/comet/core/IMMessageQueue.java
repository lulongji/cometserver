package com.yuntongxun.comet.core;

import com.yuntongxun.comet.model.Message;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 下发消息处理
 */
public class IMMessageQueue {

    private DeferredResult<List<Message>> result;

    private final LinkedList<Message> messageQueue = new LinkedList<>();

    /**
     * 存储消息
     *
     * @param message
     */
    public synchronized void send(Message message) {
        messageQueue.add(message);
    }

    public DeferredResult<List<Message>> poll() {
        result = new DeferredResult<>(10000L);


        flush();


        result.onTimeout(() -> result.setResult(null));
        return result;
    }

    /**
     * flush()方法会在DeferredResult可用（非空且未被使用）时把消息发送出去，在send和poll时都会执行flush(),
     * 这样无论什么情况下消息最终都会被发送出去
     */
    private synchronized void flush() {
        if (result != null && !result.hasResult() && messageQueue.size() > 0) {
            //这里需要拷贝一份消息，因为此处为异步调用，而在当前线程中，messageQueue的引用随后将被clear()
            result.setResult(new ArrayList<>(messageQueue));
            messageQueue.clear();
        }
    }


}
