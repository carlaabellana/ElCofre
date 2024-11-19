package persistence;

import business.shop.Shop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Se encarga de proporcionar métodos para cargar y guardar datos de tiendas
 */
public class ShopJsonDAO {
    private static final String FILE_PATH = "shops.json";
    private Gson gson;

    /**
     * Constructor de la clase. Inicializa una instancia de Gson para manejar la conversión de objetos a JSON y viceversa.
     */
    public ShopJsonDAO(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeShopsFileIfNotExists();
    }

    /**
     * Verifica si el archivo JSON existe.
     * Si no existe, crea un archivo JSON con una lista vacía de tiendas.
     */
    public void initializeShopsFileIfNotExists() {
        if (!fileExists()) {
            saveShops(new ArrayList<>());
        }
    }

    /**
     * Comprueba si el archivo JSON existe en el sistema de archivos.
     * @return boolean para ver si existe o no
     */
    public boolean fileExists() {
        return new File(FILE_PATH).exists();
    }

    /**
     * Guarda una lista de tiendas en el archivo JSON "shops.json".
     * @param shops lista de tiendas para guardar
     */
    public void saveShops(List<Shop> shops) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(shops, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}