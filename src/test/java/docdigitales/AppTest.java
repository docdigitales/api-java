package docdigitales;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Map;
import com.google.gson.Gson;

import org.jetbrains.kotlin.com.intellij.util.containers.HashSet;
import org.junit.Test;
import docdigitales.ApiFacturacion;
import docdigitales.JsonMap;

public class AppTest 
{
    private final String PATH_CERTIFICADO = "vendor/certificados/certificado.cer";
    private final String PATH_LLAVE       = "vendor/certificados/llave.txt";
    private ApiFacturacion api = new ApiFacturacion();
    private Gson gson = new Gson();

    @Test
    public void generaExitosamenteFactura() {
        // Json de Factura para generar
        String factura = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"factura\"},\"data\":[{\"datos_fiscales\":{\"certificado_pem\":\"\",\"llave_pem\":\"\",\"llave_password\":\"DDM090629R13\"},\"cfdi\":{\"cfdi__comprobante\":{\"folio\":\"123\",\"fecha\":\"2018-03-25T12:12:12\",\"tipo_comprobante\":\"I\",\"lugar_expedicion\":\"21100\",\"forma_pago\":\"01\",\"metodo_pago\":\"PUE\",\"moneda\":\"MXN\",\"tipo_cambio\":\"1\",\"subtotal\":\"99.00\",\"total\":\"99.00\",\"cfdi__emisor\":{\"rfc\":\"DDM090629R13\",\"nombre\":\"Emisor Test\",\"regimen_fiscal\":\"601\"},\"cfdi__receptor\":{\"rfc\":\"XEXX010101000\",\"nombre\":\"Receptor Test\",\"uso_cfdi\":\"G01\"},\"cfdi__conceptos\":{\"cfdi__concepto\":[{\"clave_producto_servicio\":\"01010101\",\"clave_unidad\":\"KGM\",\"cantidad\":\"1\",\"descripcion\":\"descripcion test\",\"valor_unitario\":\"99.00\",\"importe\":\"99.00\",\"unidad\":\"unidad\",\"no_identificacion\":\"KGM123\",\"cfdi__impuestos\":{\"cfdi__traslados\":{\"cfdi__traslado\":[{\"base\":\"99.00\",\"impuesto\":\"002\",\"tipo_factor\":\"Exento\"}]}}}]}}}}]}";
        // Certificado y Llave
        String certificadoPem = Certificados.exportarCertificadoPem(PATH_CERTIFICADO);
        String llavePem = Certificados.exportarLlavePem(PATH_LLAVE); 
        // Transformar JSON a Mapa       
        Map<String, Object> facturaDiccionario = JsonMap.toMap(factura);
        // Agregar los Datos fiscales y la fecha
        facturaDiccionario = JsonMap.setDatosFiscales(facturaDiccionario, certificadoPem, llavePem, "DDM090629R13");
        facturaDiccionario = JsonMap.setFecha(facturaDiccionario);
        // Generar la Factura
        String peticionGeneracion = gson.toJson(facturaDiccionario);
        String response = api.generacionFactura(peticionGeneracion);
        // Verificar la respuesta
        Map<String, Object> facturaGenerada = JsonMap.toMap(response);
        Map<String, Object> complemento = JsonMap.getComplemento(facturaGenerada);
        assertNotNull(complemento.get("uuid"));        
    }

    @Test
    public void cancelarExitosamenteFactura() {
        String facturaCancelacion = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"factura\"},\"data\":[{\"rfc\":\"\",\"uuid\":[\"\"],\"datos_fiscales\":{\"certificado_pem\":\"\",\"llave_pem\":\"\",\"password_llave\":\"\"},\"acuse\": false}]}";
        String[] uuidCancelar = {"C39C7784-B41E-40D6-89E7-46683205ED6C"};
        // Certificado y Llave
        String certificadoPem = Certificados.exportarCertificadoPem(PATH_CERTIFICADO);
        String llavePem = Certificados.exportarLlavePem(PATH_LLAVE);
        Map<String, Object> facturaDiccionario = JsonMap.toMap(facturaCancelacion);
        // Transformar JSON a Mapa
        facturaDiccionario = JsonMap.setDatosFiscales(facturaDiccionario, certificadoPem, llavePem, "DDM090629R13");
        facturaDiccionario = JsonMap.setUuidCancelacion(facturaDiccionario, uuidCancelar, "DDM090629R13");
        String peticionCancelacion = gson.toJson(facturaDiccionario);
        String response = api.cancelacionFactura(peticionCancelacion);
        
        Map<String, Object> facturaCancelada = JsonMap.toMap(response);
        Map<String, Object> data = JsonMap.getData(facturaCancelada);

        assertEquals("Cancelado Exitosamente", (data.get("descripcion")));
    }

    @Test
    public void envioExitosoFactura() {
        String facturaEnvio = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"factura\"},\"data\":[{\"uuid\":[\"\"],\"destinatarios\":[{\"correo\":\"sandbox@docdigitales.com\"}],\"titulo\":\"Envio de Factura: 123\",\"texto\":\"Envio de Factura con folio 123, para su revision.\",\"pdf\":\"true\"}]}";
        String[] uuidEnviar = {"ACF6B8DB-AA7C-4FBC-A0A2-D8FE04220E2B"};
        Map<String, Object> facturaDiccionario = JsonMap.toMap(facturaEnvio);

        // Establecer Parametros de envio
        facturaDiccionario = JsonMap.setUuids(facturaDiccionario, uuidEnviar);
        String peticionEnvio = gson.toJson(facturaDiccionario);
        String response = api.envioFactura(peticionEnvio);

        Map<String, Object> facturaEnviada = JsonMap.toMap(response);
        Map<String, Object> data = JsonMap.getData(facturaEnviada);
        assertEquals(true, (data.get("enviado")));
    }

    @Test
    public void descargaExitosaFactura() {
        String facturaDescarga = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"factura\"},\"data\":[{\"uuid\":[\"\"],\"destinatarios\":[{\"correo\":\"sandbox@docdigitales.com\"}],\"titulo\":\"Descargar factura\",\"texto\":\"Adjunto factura generada\",\"pdf\":\"true\"}]}";
        String[] uuidDescargar = {"ACF6B8DB-AA7C-4FBC-A0A2-D8FE04220E2B"};
        Map<String, Object> facturaDiccionario = JsonMap.toMap(facturaDescarga);

        // Establecer parametros de envio
        facturaDiccionario = JsonMap.setUuids(facturaDiccionario, uuidDescargar);
        String peticionDescarga = gson.toJson(facturaDiccionario);
        String response = api.descargaFactura(peticionDescarga);

        Map<String, Object> facturaDescargada = JsonMap.toMap(response);
        Map<String, Object> data = JsonMap.getData(facturaDescargada);
        assertNotNull(data.get("link"));
    }
}
