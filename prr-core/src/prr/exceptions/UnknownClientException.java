package prr.exceptions;

public class UnknownClientException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private String _clientKey;

    public UnknownClientException(String clientKey){
        _clientKey = clientKey;
    }

    public String getClientKey(){
        return _clientKey;
    }
}
