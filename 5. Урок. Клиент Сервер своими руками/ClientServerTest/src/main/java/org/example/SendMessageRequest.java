package org.example;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SendMessageRequest - Отправить сообщение
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageRequest extends AbstractRequest {

    public static final String TYPE = "sendMessage";
    private String recipient;
    private String message;

    public SendMessageRequest() {
        setType(TYPE);
    }
}
