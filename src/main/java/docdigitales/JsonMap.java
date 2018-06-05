package docdigitales;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import com.google.gson.*;

public class JsonMap{
  private static Gson gson = new Gson();

  public static Map<String, Object> toMap(String jsonString) {
    Map<String, Object> map = new HashMap<String, Object>();
    return gson.fromJson(jsonString, map.getClass());
  }

  public static Map<String, Object> getData(Map<String, Object> facturaCancelada) {
    ArrayList data = (ArrayList) facturaCancelada.get("data");
    return (Map<String, Object>) ((Map<String, Object>) data.get(0));
  }

  public static Map<String, Object> getComplemento(Map<String, Object> facturaGenerada) {
    ArrayList data = (ArrayList) facturaGenerada.get("data");
    return (Map<String, Object>) ((Map<String, Object>) data.get(0)).get("cfdi_complemento");
  }

  public static Map<String, Object> setDatosFiscales(Map<String, Object> facturaDiccionario, String certificadoPem, String llavePem, String passwordLlave) {
    ArrayList data = (ArrayList) facturaDiccionario.get("data");
    Map<String, Object> datosFiscalesMap = (Map<String, Object>)((Map<String, Object>)data.get(0)).get("datos_fiscales");
    datosFiscalesMap.put("certificado_pem", certificadoPem);
    datosFiscalesMap.put("llave_pem", llavePem);
    datosFiscalesMap.put("llave_password", passwordLlave);

    ((Map<String, Object>)(((ArrayList)facturaDiccionario.get("data")).get(0))).put("datos_fiscales", datosFiscalesMap);    
    return facturaDiccionario;
  }

  public static Map<String, Object> setFecha(Map<String, Object> facturaDiccionario) {
    try {
      ArrayList data = (ArrayList) facturaDiccionario.get("data");
      SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      String fecha = dt.format(new Date());
      Map<String, Object> cfdiComprobanteMap = ((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) data.get(0)).get("cfdi")).get("cfdi__comprobante"));
      cfdiComprobanteMap.put("fecha", fecha);
      ((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>)((ArrayList)facturaDiccionario.get("data")).get(0)).get("cfdi"))).put("cfdi__comprobante", cfdiComprobanteMap);
      return facturaDiccionario;
    } catch(Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public static Map<String, Object> setUuidCancelacion(Map<String, Object> facturaDiccionario, String[] uuidsCancelar, String rfc) {
    ArrayList data = (ArrayList) facturaDiccionario.get("data");
    ((Map<String, Object>)((ArrayList)facturaDiccionario.get("data")).get(0)).put("uuid", uuidsCancelar);
    ((Map<String, Object>) ((ArrayList) facturaDiccionario.get("data")).get(0)).put("rfc", rfc);
    return facturaDiccionario;
  }

  public static Map<String, Object> setUuids(Map<String, Object> facturaDiccionario, String[] uuids) {
    ArrayList data = (ArrayList) facturaDiccionario.get("data");
    ((Map<String, Object>) ((ArrayList) facturaDiccionario.get("data")).get(0)).put("uuid", uuids);
    return facturaDiccionario;
  }
}