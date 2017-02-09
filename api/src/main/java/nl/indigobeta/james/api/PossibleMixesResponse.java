package nl.indigobeta.james.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jelle on 19/12/2016.
 */
public class PossibleMixesResponse
{
    @JsonProperty
    private int status;

    @JsonProperty
    private String message;

    @JsonProperty
    private int[] possibleMixes;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int[] getPossibleMixes() {
        return possibleMixes;
    }

    public void setPossibleMixes(int[] possibleMixes) {
        this.possibleMixes = possibleMixes;
    }
}
