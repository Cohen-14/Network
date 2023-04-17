package prr.exceptions;

public class DuplicateClientException extends Exception{
    
    private static final long serialVersionUID = 202208091753L;

    private String _clientkey;
    private String _name;

    public DuplicateClientException(String clientKey, String name){
        _clientkey = clientKey;
        _name = name;
    }

    public String getClientKey(){
        return _clientkey;
    }

    public String getName(){
        return _name;
    }
}
