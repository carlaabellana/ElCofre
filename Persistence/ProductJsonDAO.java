package persistence;
import business.product.GeneralProduct;
import business.product.Product;
import business.product.ReducedProduct;
import business.product.SuperReducedProduct;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
/**
 * Esta clase proporciona métodos para cargar y guardar datos de productos (objetos Product) en formato JSON
 */
public class ProductJsonDAO {
    private static final String FILE_PATH = "products.json";
    private Gson gson;

    /**
     * Constructor de la clase. Inicializa una instancia de Gson para manejar la conversión de objetos a JSON y viceversa.
     * @throws FileNotFoundException
     */
    public ProductJsonDAO() throws FileNotFoundException {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        if (!Files.exists(Path.of(FILE_PATH))) {
            throw new FileNotFoundException("El archivo 'products.json' no se encuentra.");
        }
    }

    /**
     * Guarda una lista de productos en el archivo JSON "products.json"
     * @param products Lista de productos para guardar
     */
    public void saveProducts(List<Product> products) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(products, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}