package org.example;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * получить список всех логинов, которые в данный момент есть в чате
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListRequest extends AbstractRequest {

    public static final String TYPE = "ListRequest";

    public ListRequest() {
        setType(TYPE);
    }
}