package business.shop;
import business.Catalogue;
import business.product.Product;

import java.util.ArrayList;
/**
 * La clase representa una tienda y almacena información sobre ella, como su nombre, descripción, año de fundación, ganancias, modelo de negocio y un catálogo de productos que ofrece.
 */
public abstract class Shop {
    private String name;
    private String description;
    private int since;
    private double earnings;
    public String businessModel;
    private final ArrayList<Catalogue> catalogue;
    /**
     * Constructor de la clase Shop.
     *
     * @param name El nombre de la tienda.
     * @param description La descripción de la tienda.
     * @param since El año de fundación de la tienda.
     * @param businessModel El modelo de negocio de la tienda.
     * @param catalogue El catálogo de productos de la tienda.
     */
    public Shop(String name, String description, int since, String businessModel,ArrayList<Catalogue> catalogue) {
        this.name = name;
        this.description = description;
        this.since = since;
        this.businessModel = businessModel;
        this.earnings = 0.0;
        this.catalogue = catalogue;
    }
    /**
     * Obtiene el nombre de la tienda.
     * @return String con el nombre de la tienda
     */
    public String getName() {
        return name;
    }
    /**
     * Obtiene la descripción de la tienda.
     * @return String con la descripción de la tienda
     */
    public String getDescription() {
        return description;
    }
    /**
     * Obtiene el año de fundación de la tienda.
     * @return int del año
     */
    public int getSince() {
        return since;
    }
    /**
     * Obtiene las ganancias de la tienda.
     * @return double con el número de ganancias
     */
    public double getEarnings() {
        return earnings;
    }
    /**
     * Obtiene el catálogo de productos de la tienda.
     * @return array del catálogo con sus productos
     */
    public ArrayList<Catalogue> getCatalogue() {
        return catalogue;
    }
    /**
     * Agrega un producto al catálogo de la tienda con nombre y precio.
     * @param productName Nombre del producto
     * @param priceAtShop Precio de ese producto en la tienda
     */
    public void addToCatalogue(String productName, double priceAtShop){
        Catalogue product = new Catalogue(productName, priceAtShop);
        catalogue.add(product);
    }
    /**
     * Establece las ganancias de la tienda.
     * @param earnings Los beneficios que ha obtenido la tienda
     */
    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }
    /**
     * Método abstracto para calcular el descuento aplicable a un producto. Debe ser implementado por las subclases.
     *
     * @param price El precio original del producto.
     * @param product El producto al cual aplicar el descuento.
     * @return El precio del producto después de aplicar el descuento.
     */
    public abstract double calculateDiscount(double price, Product product);

}
