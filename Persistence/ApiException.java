package persistence;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.InterruptedIOException;
/**
 * ApiException es una clase de excepción personalizada para manejar errores durante la comunicación con una API.
 * Incluye información sobre el mensaje de error, la causa y la URL involucrada en la operación que falló.
 */
public class ApiException extends IOException {
    protected String url;
    /**
     * Constructor que inicializa la excepción con un mensaje, una causa y la URL específica de la operación fallida.
     * @param message Mensaje de error.
     * @param cause Causa raíz de la excepción.
     * @param url URL de la operación que generó el error.
     */
    public ApiException(String message, Throwable cause, String url) {
        super(message, cause);
        this.url = url;
    }
    /**
     * Obtiene la URL asociada con la excepción.
     * @return URL que generó el error.
     */
    public String getUrl() {
        return url;
    }
}
/**
 * Excepción específica para manejar interrupciones en la conexión durante las operaciones de API.
 */
class ConnectionInterruptedException extends ApiException {
    public ConnectionInterruptedException(String url, InterruptedIOException cause) {
        super(cause.getMessage(), cause, url);
    }
}
/**
 * Excepción específica para manejar URLs mal formadas en las solicitudes de API.
 */
class MalformedURLException extends ApiException {
    public MalformedURLException(String url, URISyntaxException cause) {
        super(cause.getMessage(), cause, url);
    }
}
/**
 * Excepción específica para manejar casos donde el servidor de la API es inalcanzable.
 */
class UnreachableServerException extends ApiException {
    public UnreachableServerException(String url, IOException cause) {
        super(cause.getMessage(), cause, url);
    }
}
/**
 * Excepción que representa errores de estado de respuesta HTTP específicos en operaciones de API.
 * Incluye el código de estado HTTP específico que causó el error.
 */
class StatusCodeException extends ApiException {
    private int statusCode;

    public StatusCodeException(String message, int statusCode, String url) {
        super(message, null, url);
        this.statusCode = statusCode;
    }
    /**
     * Obtiene el código de estado HTTP de la excepción.
     * @return Código de estado HTTP.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
/**
 * Excepción para errores de API no disponible, subclase de StatusCodeException con un mensaje predeterminado.
 */
class UnavailableApiException extends StatusCodeException {
    public UnavailableApiException(String url, int statusCode) {
        super("API is unavailable", statusCode, url);
    }
}
/**
 * Excepción para solicitudes incorrectas, subclase de StatusCodeException con un mensaje predeterminado.
 */
class IncorrectRequestException extends StatusCodeException {
    public IncorrectRequestException(String url, int statusCode) {
        super("Request is incorrect", statusCode, url);
    }
}
