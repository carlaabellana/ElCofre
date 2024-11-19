package business.product;
import business.Review;

import java.util.ArrayList;
/**
 * La clase Product representa un producto en el sistema de gestión de compras.
 * Contiene información como nombre, marca, categoría, precio y reviews asociadas.
 */
public abstract class Product {
    private String name;
    private String brand;
    private double mrp;
    private String category;
    private ArrayList<Review> reviews;
    /**
     * Constructor que inicializa un nuevo objeto Product con la información básica del producto.
     *
     * @param name    El nombre del producto.
     * @param brand   La marca del producto.
     * @param mrp     El precio de venta recomendado del fabricante.
     * @param category La categoría del producto.
     */
    public Product(String name, String brand, double mrp, String category) {
        this.name = name;
        this.brand = brand;
        this.mrp = mrp;
        this.category = category;
        this.reviews = new ArrayList<>();
    }
    /**
     * Obtiene el nombre del producto.
     *
     * @return String del nombre del producto.
     */
    public String getName() {
        return name;
    }
    /**
     * Obtiene la marca del producto.
     *
     * @return String de la marca del producto.
     */
    public String getBrand() {
        return brand;
    }
    /**
     * Obtiene el precio de venta
     *
     * @return Double del precio de venta.
     */
    public double getMrp() {
        return mrp;
    }
    /**
     * Obtiene la lista de valoraciones asociadas al producto.
     *
     * @return La lista de valoraciones del producto.
     */
    public ArrayList<Review> getReviews(){
        return reviews;
    }
    /**
     * Agrega una nueva reseña al producto.
     *
     * @param review La reseña a agregar.
     */
    public void addReview(Review review) {
        reviews.add(review);
    }
    /**
     * Establece la lista de reseñas del producto.
     *
     * @param reviews La lista de reseñas.
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
    /**
     * Método abstracto para calcular el precio final del producto. Debe ser implementado por las subclases.
     *
     * @param price El precio base para el cálculo.
     * @return El precio final del producto después de aplicar cualquier lógica específica de cálculo.
     */
    public abstract double calculatePrice(double price);

}
