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
    public void generarExitosamenteRecepion() {
        // Json de Recepcion para generar (cambiar por los api keys de tu empresa)
        String recepcion = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"recepcion\"},\"data\":[{\"datos_fiscales\":{\"certificado_pem\":\"\",\"llave_pem\":\"\",\"llave_password\":\"DDM090629R13\"},\"cfdi\":{\"cfdi__comprobante\":{\"folio\":\"793\",\"serie\":\"C\",\"fecha\":\"\",\"tipo_comprobante\":\"P\",\"lugar_expedicion\":\"83448\",\"moneda\":\"XXX\",\"subtotal\":\"0\",\"total\":\"0\",\"cfdi__emisor\":{\"rfc\":\"DDM090629R13\",\"nombre\":\"CARLOS CESAR OCHOA GAXIOLA\",\"regimen_fiscal\":\"601\"},\"cfdi__receptor\":{\"rfc\":\"GNP9211244P0\",\"nombre\":\"GRUPO NACIONAL PROVINCIAL S.A.B.\",\"uso_cfdi\":\"P01\"},\"cfdi__conceptos\":{\"cfdi__concepto\":[{\"clave_producto_servicio\":\"84111506\",\"clave_unidad\":\"ACT\",\"cantidad\":\"1\",\"descripcion\":\"Pago\",\"valor_unitario\":\"0\",\"importe\":\"0\"}]},\"cfdi__complemento\":{\"pago10__pagos\":{\"pago10__pago\":{\"fecha-pago\":\"2018-08-10T11:21:09\",\"forma-pago\":\"01\",\"tipo-cambio\":\"\",\"moneda\":\"MXN\",\"monto\":\"5000.00\",\"rfc-emisor-ordenante\":\"\",\"rfc-emisor-beneficiario\":\"\",\"nombre-banco-ordenante\":\"\",\"cuenta-ordenante\":\"\",\"cuenta-beneficiario\":\"\",\"tipo-cadena-pago\":\"\",\"num-operacion\":\"1123121241\",\"certificado-pago\":\"\",\"cadena-pago\":\"\",\"sello-pago\":\"\",\"pago10__docto_relacionado\":[{\"id-documento\":\"682C60DC-2A2F-47CD-A34D-39180EFAB2B1\",\"serie\":\"ASD\",\"folio\":\"53452\",\"moneda-dr\":\"MXN\",\"tipo-cambio-dr\":\"\",\"metodo-pago-dr\":\"PPD\",\"numero-parcialidad\":\"1\",\"importe-pagado\":\"5000.00\",\"importe-saldo-anterior\":\"7000.00\",\"importe-saldo-insoluto\":\"2000.00\"}]}}}}}}]}";
        
        // Certificado y Llave
        String certificadoPem = Certificados.exportarCertificadoPem(PATH_CERTIFICADO);
        String llavePem = Certificados.exportarLlavePem(PATH_LLAVE);

        // Transformar JSON a Mapa
        Map<String, Object> recepcionDiccionario = JsonMap.toMap(recepcion);

        // Agregar los Datos fiscales y la fecha
        recepcionDiccionario = JsonMap.setDatosFiscales(recepcionDiccionario, certificadoPem, llavePem, "DDM090629R13");
        recepcionDiccionario = JsonMap.setFecha(recepcionDiccionario);

        // Generar la Factura
        String peticionGeneracion = gson.toJson(recepcionDiccionario);
        String response = api.generacionRecepcionPago(peticionGeneracion);

        // Verificar la respuesta
        Map<String, Object> recepcionGenerada = JsonMap.toMap(response);
        Map<String, Object> complemento = JsonMap.getComplemento(recepcionGenerada);
        assertNotNull(complemento.get("uuid"));
    }

    @Test
    public void cancelarExitosamenteFactura() {
        String facturaCancelacion = "{\"meta\":{\"empresa_uid\":\"asd123asd\",\"empresa_api_key\":\"123123123\",\"ambiente\":\"S\",\"objeto\":\"factura\"},\"data\":[{\"rfc\":\"\",\"uuid\":[\"\"],\"datos_fiscales\":{\"certificado_pem\":\"\",\"llave_pem\":\"\",\"password_llave\":\"\"},\"acuse\": false}]}";
        String[] uuidCancelar = {"80B88D9E-9671-4BAB-9956-73E0BA3FB1D6"};
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
        String[] uuidEnviar = {"80B88D9E-9671-4BAB-9956-73E0BA3FB1D6"};
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
        String[] uuidDescargar = {"80B88D9E-9671-4BAB-9956-73E0BA3FB1D6"};
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
