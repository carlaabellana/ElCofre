package business.product;
import business.Review;
import com.google.gson.JsonSyntaxException;
import persistence.ApiException;
import persistence.ApiHelper;
import persistence.ProductJsonDAO;
import persistence.ProductApiJsonDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
/**
 * La clase ProductManager gestiona las operaciones relacionadas con los productos en el sistema.
 * Esto incluye la creación, eliminación, búsqueda y valoración de productos.
 */
public class ProductManager {
    private ProductApiJsonDAO productApiJsonDAO;
    private final ProductJsonDAO productJsonDAO;
    private ArrayList<Product> productList;
    private ApiHelper apiHelper;

    /**
     * Constructor que inicializa un nuevo ProductManager.
     * @throws FileNotFoundException exception para la utilización de ficheros
     */
    public ProductManager() throws IOException {
        this.apiHelper = new ApiHelper();
        this.productApiJsonDAO = new ProductApiJsonDAO();
        this.productJsonDAO =  new ProductJsonDAO();
        this.productList = new ArrayList<>();
    }
    /**
     * Crea un producto y lo agrega a la lista de productos. Si la API está conectada, también intenta guardar el producto mediante la API y en el almacenamiento local.
     *
     * @param name Nombre del producto.
     * @param brand Marca del producto.
     * @param mrp Precio máximo de venta al público.
     * @param category Categoría del producto (GENERAL, REDUCED, SUPER_REDUCED).
     * @param averageRating Valoración media del producto, necesaria para productos con categoría REDUCED.
     * @return Verdadero si el producto se crea y guarda correctamente, falso en caso de una excepción.
     */
    public boolean createProduct(String name,String brand, double mrp, String category, double averageRating){
        if(apiHelper.isConnected()) {
            if ("GENERAL".equalsIgnoreCase(category)) {
                GeneralProduct generalProduct = new GeneralProduct(name, brand, mrp);
                try {
                    productList.add(generalProduct);
                    productApiJsonDAO.saveProduct(generalProduct);
                }catch(ApiException e){
                    return false;
                }
            }
            if ("REDUCED".equalsIgnoreCase(category)) {
                ReducedProduct reducedProduct = new ReducedProduct(name, brand, mrp, averageRating);
                try {
                    productList.add(reducedProduct);
                    productApiJsonDAO.saveProduct(reducedProduct);
                }catch (ApiException e){
                    return false;
                }
            }
            if ("SUPER_REDUCED".equalsIgnoreCase(category)) {
                SuperReducedProduct superReducedProduct = new SuperReducedProduct(name, brand, mrp);
                try {
                    productList.add(superReducedProduct);
                    productApiJsonDAO.saveProduct(superReducedProduct);
                }catch(ApiException e){
                    return false;
                }
            }
        }else{
            if ("GENERAL".equalsIgnoreCase(category)) {
                GeneralProduct generalProduct = new GeneralProduct(name, brand, mrp);
                    productList.add(generalProduct);
                    productJsonDAO.saveProducts(productList);
            }
            if ("REDUCED".equalsIgnoreCase(category)) {
                ReducedProduct reducedProduct = new ReducedProduct(name, brand, mrp, averageRating);
                productList.add(reducedProduct);
                productJsonDAO.saveProducts(productList);
            }
            if ("SUPER_REDUCED".equalsIgnoreCase(category)) {
                SuperReducedProduct superReducedProduct = new SuperReducedProduct(name, brand, mrp);
                productList.add(superReducedProduct);
                productJsonDAO.saveProducts(productList);
            }
        }
        return true;
    }

    /**
    * Carga y devuelve una lista de productos utilizando la API del DAO de productos.
    * @return Una lista de objetos Product cargados. Si ocurre una excepción, devuelve una lista vacía.
     */
    public ArrayList<Product> productArrayList(){
        ArrayList<Product> productArrayList = new ArrayList<>();
            try {
                productArrayList = productApiJsonDAO.loadProducts();
            } catch (ApiException | JsonSyntaxException e) {
                System.out.println(e.getMessage());
            }
        return productArrayList;
    }

    /**
     * Obtiene todos los productos que hay actualmente
     *
     * @return la lista con los productos
     */
    public ArrayList<Product> getProductList(){
        return productList;
    }

    /**
     * Comprueba que el nombre del producto ya exista
     *
     * @param name con el nombre del producto.
     */
    public boolean nameProductExists (String name){
        boolean exists = false;
        ArrayList<Product> products = new ArrayList<>();
        if(apiHelper.isConnected()){
            products = productArrayList();
        }else{
            products = getProductList();
        }
        for(Product product: products){
            if(product.getName().equalsIgnoreCase(name)){
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Obtiene un producto en concreto según su nombre
     *
     * @param name el nombre del producto.
     * @return un producto tipo Product con el nombre que queremos buscar
     */
    public Product getProductByName(String name){
        ArrayList<Product> products = new ArrayList<>();
        if(apiHelper.isConnected()){
            products = productArrayList();
        }else{
            products = getProductList();
        }
        for(Product product: products){
            if(product.getName().equalsIgnoreCase(name)){
                return product;
            }
        }
        return null;
    }

    /**
     * Elimina un producto de la lista de productos gestionados por su índice.
     *
     * @param index Índice del producto a eliminar.
     */
    public void removeProduct(int index){
        if(index >= 0 && index < productList.size()){
            Product productToRemove = productList.remove(index);
            productList.remove(productToRemove);
            productJsonDAO.saveProducts(productList);
        }
    }
    /**
     * Elimina un producto de la lista y de la API utilizando su índice en la lista de productos.
     *
     * @param index Índice del producto a eliminar en la lista de productos.
     * @param productList Lista de productos de donde se eliminará el producto.
     * @throws ApiException Si ocurre un error al eliminar el producto de la API.
     */
    public void removeProductFromApi(int index, ArrayList<Product> productList) throws ApiException {
        if(index <= productList.size()){
            productApiJsonDAO.removeProduct("P1-G70", index);
        }
    }

    /**
     * Comprueba si el precio de un producto excede su precio máximo de venta al público.
     * @param price Precio a comprobar.
     * @return Verdadero si el precio es menor o igual al precio máximo de venta al público de algún producto, falso en caso contrario.
     */
    public boolean priceProductExceed(double price){
        boolean ok = false;
        ArrayList<Product> products;
        if(apiHelper.isConnected()){
            products = productArrayList();
            for (Product p : products) {
                if (price <= p.getMrp()) {
                    ok = true;
                    break;
                }
            }

        }else{
            products = getProductList();
            for (Product p : products) {
                if (price <= p.getMrp()) {
                    ok = true;
                    break;
                }
            }
        }

        return ok;
    }
    /**
     * Busca productos que coincidan con el texto de búsqueda en su nombre o marca y devuelve una lista de los productos encontrados.
     *
     * @param searchInput Texto de búsqueda para comparar con el nombre o marca de los productos.
     * @return Lista de productos que coinciden con el criterio de búsqueda.
     */
    public ArrayList<Product> searchProducts (String searchInput){
        ArrayList<Product> results = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();
        if(apiHelper.isConnected()){
            products = productArrayList();
        }else{
            products = getProductList();
        }
        for(Product product: products){
            if(matchProduct(product, searchInput)){
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Comprueba si un producto coincide con el texto de búsqueda en su nombre o marca.
     *
     * @param product Producto a comparar.
     * @param text Texto de búsqueda.
     * @return Verdadero si el nombre o la marca del producto contienen el texto de búsqueda, falso en caso contrario.
     */
    private boolean matchProduct(Product product, String text){
        String productName = product.getName().toLowerCase();
        String brand = product.getBrand().toLowerCase();
        text = text.toLowerCase();
        boolean nameMatch = productName.contains(text);
        boolean brandMatch = brand.contains(text);
        return nameMatch || brandMatch;
    }

    /**
     * Agrega una reseña de producto con calificación y comentario a un producto.
     *
     * @param rating la puntuación que se le va a poner al producto
     * @param comment el comentario que se le va a poner al producto
     * @param product el producto que le tenemos que añadir la reseña
     */
    public void addProductReview(int rating, String comment, Product product){
        boolean api;
        if(apiHelper.isConnected()){
            api = true;
        }else{
            api = false;
        }

        if(product.getReviews() == null){
            product.setReviews(new ArrayList<>());
        }
        Review review = new Review(rating, comment);
        product.addReview(review);

        if (api) {
            productApiJsonDAO.updateProduct(product);
        } else {
            productJsonDAO.saveProducts(productList);
        }

    }

    /**
     * Obtiene las reseñas de un producto.
     *
     * @param product con producto que queremos obtener sus reseñas.
     * @return Lista con todas las reseñas de ese producto.
     */
    public ArrayList<Review> getReviews(Product product){
        return product.getReviews();
    }
}
