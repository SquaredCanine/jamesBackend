package nl.indigobeta.james.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jelle on 02/11/2016.
 */
public class Response
{
    @JsonProperty
    private int status;

    @JsonProperty
    private String message;

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }
}
