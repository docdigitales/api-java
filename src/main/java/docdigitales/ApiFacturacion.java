package docdigitales;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiFacturacion {

  private final String EMPRESA_API_KEY = "123123123";
  public  final static okhttp3.MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");
  
  public String generacionFactura(String peticionGeneracion) {
    String uriGeneracion = "http://api.docdigitales.com/v1/facturas/generar";
    return getPostResponse(uriGeneracion, peticionGeneracion);
  }

  public String cancelacionFactura(String peticionCancelacion) {
    String uriCancelacion = "http://api.docdigitales.com/v1/facturas/cancelar";
    return getPostResponse(uriCancelacion, peticionCancelacion);
  }

  public String envioFactura(String peticionEnvio) {
    String uriEnvio = "http://api.docdigitales.com/v1/facturas/enviar";
    return getPostResponse(uriEnvio, peticionEnvio);
  }

  public String descargaFactura(String peticionDescarga) {
    String uriDescarga = "http://api.docdigitales.com/v1/facturas/descargar";
    return getPostResponse(uriDescarga, peticionDescarga);
  }

  private String getPostResponse(String uri, String data) {
    try {
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(JSON, data);
      Request request = new Request.Builder().url(uri).post(body)
        .header("Authorization", "Token token=".concat(EMPRESA_API_KEY))
        .addHeader("ACCEPT", "application/json")
        .addHeader("CONTENT_TYPE", "application/json")
        .addHeader("Access-Control-Allow-Origin", "*").build();
      Response response = client.newCall(request).execute();
      return response.body().string();
    } catch(Exception e) {
      System.out.println(e);
      return null;
    }
  }
}