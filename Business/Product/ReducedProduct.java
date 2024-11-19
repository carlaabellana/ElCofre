package business.product;
/**
 * Clase para productos con impuesto reducido, ajustando su tasa según la valoración promedio.
 * Incentiva productos de alta calidad con menores impuestos.
 */
public class ReducedProduct extends Product{
    /**
     * Tasa de impuesto reducido estándar para productos, expresada como porcentaje.
     */
    private static final double REDUCED_TAX_RATE = 10.0;
    /**
     * Tasa de impuesto aún más reducida aplicable a productos con una valoración promedio
     * alta, expresada como porcentaje.
     */
    private static final double REDUCED_TAX_RATE_REDUCED_PRICE = 5.0;
    /**
     * Valoración promedio mínima requerida para que un producto sea elegible para la tasa
     * de impuesto reducido adicional.
     */
    private static final double MINIMUM_AVERAGE_RATING = 3.5;
    /**
     * Valoración promedio actual del producto.
     */
    private final double averageRating;
    /**
     * Constructor para crear una instancia de un producto con impuesto reducido.
     *
     * @param name El nombre del producto.
     * @param brand La marca del producto.
     * @param mrp El precio de venta sugerido por el fabricante (MRP).
     * @param averageRating La valoración promedio del producto.
     */
    public ReducedProduct(String name, String brand, double mrp, double averageRating) {
        super(name, brand, mrp, "REDUCED");
        this.averageRating = averageRating;
    }
    /**
     * Calcula el precio final del producto. Los productos con valoraciones por encima de un cierto
     * umbral aplican una tasa impositiva aún más baja.
     *
     * @param price El precio base del producto sin impuestos.
     * @return El precio del producto después de aplicar la tasa de impuesto reducido correspondiente.
     */
    public double calculatePrice(double price) {
        if (averageRating > MINIMUM_AVERAGE_RATING) {
            return (price / (1 + (REDUCED_TAX_RATE_REDUCED_PRICE / 100)));
        } else {
            return (price / (1 + REDUCED_TAX_RATE / 100));
        }
    }
}
