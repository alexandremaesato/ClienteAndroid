package clientefeedback.aplicacaocliente;



/**
 * Created by viniciusthiengo on 2/1/15.
 */
public interface Transaction {
    public void doBefore();
    public void doAfter(String answer);
    public RequestData getRequestData();
}
