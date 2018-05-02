package mx.edu.utng.lajosefa.entity;

/**
 * Created by Diana Manzano on 29/03/2018.
 */

public class Result {
    public String messageId;

    public Result(String messageId) {
        this.messageId = messageId;
    }

    public Result() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
