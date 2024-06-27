package org.example;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BroadcastMessageRequest - послать сообщение ВСЕМ пользователям (кроме себя)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BroadcastMessageRequest extends AbstractRequest {

    public static final String TYPE = "sendBroadcastMessage";

    private String message;

    private String sender;

    public BroadcastMessageRequest() {
        setType(TYPE);
    }
}
