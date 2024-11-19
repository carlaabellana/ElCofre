package persistence;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Define operaciones básicas de API (GET, POST, DELETE) para interactuar con un servidor remoto.
 * Incluye métodos para solicitudes HTTP y obtener datos de un grupo específico.
 */
interface ApiOperations {
    String getFromUrl(String url) throws ApiException;
    String postToUrl(String url, String body) throws ApiException;
    String deleteFromUrl(String url) throws ApiException;
    String getFromUrlGroup(String id) throws ApiException;
    String putToUrl(String url, String body) throws ApiException;
}

/**
 * La clase ApiHelper implementa la interfaz ApiOperations y proporciona una implementación concreta para interactuar con una API remota.
 */
public class ApiHelper implements ApiOperations{
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo";
    private boolean isConnected;
    private String lastError;

    /**
     * Constructor. Verifica la conexión a la API y establece el estado de conexión.
     */
    public ApiHelper(){
        this.isConnected = false;
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                isConnected = true;
            } else {
                lastError = "\nERROR: Server is unreachable. Response code: " + responseCode;
            }
        } catch (MalformedURLException e) {
            lastError = "ERROR:Malformed URL for API\n";
        } catch (IOException e) {
            lastError = "Error: The API isn’t available.\n";
        }
    }
    /**
     * Comprueba si hay conexión con la API.
     * @return true si está conectado, false en caso contrario.
     */
    public boolean isConnected() {
        return isConnected;
    }
    /**
     * Obtiene el último error registrado al intentar conectarse o interactuar con la API.
     * @return Descripción del último error.
     */
    public String getLastError() {
        return lastError;
    }
    /**
     * Envía una solicitud HTTP a la API.
     * @param apiUrl URL completa de la API a la que se envía la solicitud.
     * @param method Método HTTP de la solicitud (GET, POST, DELETE).
     * @param body Cuerpo de la solicitud para métodos POST y DELETE, si es necesario.
     * @throws ApiException Si la solicitud falla.
     */

    private String sendHttpRequest(String apiUrl, String method, String body) throws ApiException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) && body != null && !body.isEmpty()) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body.getBytes("UTF-8"));
                    os.flush();
                }
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                } catch (ApiException ignored) {
                }
            }
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
            throw new ApiException("Malformed URL: " + apiUrl, e, apiUrl);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new ApiException("Failed to make HTTP request to " + apiUrl, e, apiUrl);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }

    /**
     * Realiza una solicitud GET a una URL específica.
     * @param url URL completa a la que se realiza la solicitud GET.
     * @return Respuesta de la API como cadena.
     * @throws ApiException Si la solicitud falla.
     */
    @Override
    public String getFromUrl(String url) throws ApiException {
        return sendHttpRequest(url, "GET", null);
    }
    /**
     * Realiza una solicitud POST a una URL específica.
     * @param url URL completa a la que se realiza la solicitud POST.
     * @param body Cuerpo de la solicitud POST.
     * @return Respuesta de la API como cadena.
     * @throws ApiException Si la solicitud falla.
     */
    @Override
    public String postToUrl(String url, String body) throws ApiException {
        return sendHttpRequest(url, "POST", body);
    }
    /**
     * Realiza una solicitud DELETE a una URL específica.
     * @param url URL completa a la que se realiza la solicitud DELETE.
     * @return Respuesta de la API como cadena.
     * @throws ApiException Si la solicitud falla.
     */
    @Override
    public String deleteFromUrl(String url) throws ApiException {
        return sendHttpRequest(url, "DELETE", null);
    }
    /**
     * Realiza una solicitud GET para obtener datos de un grupo específico desde la API.
     * @param id Identificador del grupo cuyos datos se solicitan.
     * @return Respuesta de la API como cadena.
     * @throws ApiException Si la solicitud falla.
     */
    @Override
    public String getFromUrlGroup(String id) throws ApiException{
        String productsEndpoint = "/products";
        String shopEndpoint = "/shops";
        String finalUrl = BASE_URL + "/" + id + productsEndpoint;
        return sendHttpRequest(finalUrl, "GET", null);
    }

    /**
     * Realiza una solicitud PUT para obtener datos de un grupo específico desde la API.
     * @param url URL de la web para hacer la petición
     * @param body el cuerpo de texto que queremos poner en la API
     * @return Respuesta de la API como cadena.
     * @throws ApiException Si la solicitud falla.
     */
    @Override
    public String putToUrl(String url, String body) throws ApiException {
        return sendHttpRequest(url, "PUT", body);
    }
}
