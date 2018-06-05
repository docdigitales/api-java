package docdigitales;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.xml.bind.DatatypeConverter;

public class Certificados {

  public static String exportarCertificadoPem(String pathCertificado) {
    try {
      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      byte[] buffer = Files.readAllBytes(new File(pathCertificado).toPath());
      X509Certificate cert = (X509Certificate)certFactory.generateCertificate(new ByteArrayInputStream(buffer));
      return formatoPemCertificado(cert, new StringWriter());
    } catch (Exception ioe) {
      System.out.println(ioe.toString());
      return null;
    } 
  }

  public static String exportarLlavePem(String pathLlave) {    
    try {
      // contenido de la llave en PEM: openssl pkcs8 -inform DER -in llave.key -passin
      // pass:passwordLlave -out llave.pem
      byte[] buffer = Files.readAllBytes(new File(pathLlave).toPath());
      String contenidoLlave = new String(buffer);
      return formatoPemLlave(contenidoLlave, new StringWriter());
    } catch(Exception e) {
      System.out.println(e.toString());
      return null;
    }
 }

  private static String formatoPemCertificado(X509Certificate certificado, StringWriter writer) {
    try {
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(DatatypeConverter.printBase64Binary(certificado.getEncoded()).replaceAll("(.{64})", "$1\n"));
      writer.write("\n-----END CERTIFICATE-----\n");
    } catch (CertificateEncodingException e) {
      return null;
    }
    return writer.toString();
  }

  private static String formatoPemLlave(String contenidoLlave, StringWriter writer) {
    try {
      writer.write("-----BEGIN RSA PRIVATE KEY-----\n");
      writer.write(contenidoLlave);
      writer.write("-----END RSA PRIVATE KEY-----\n");
    } catch(Exception e) {
      System.out.println(e);
      return null;
    }
    return writer.toString();
  }
}