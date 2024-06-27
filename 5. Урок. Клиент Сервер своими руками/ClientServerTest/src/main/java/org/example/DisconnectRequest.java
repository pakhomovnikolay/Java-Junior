package org.example;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DisconnectRequest - клиент оповещает сервер о том, что он отключился
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DisconnectRequest extends AbstractRequest {

    public static final String TYPE = "DisconnectRequest";

    private String recipient;

    public DisconnectRequest() {
        setType(TYPE);
    }
}
