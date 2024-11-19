package business.product;
/**
 * Clase SuperReducedProduct que representa productos con un impuesto super reducido. Esta clase
 * es utilizada para productos que, bajo ciertas condiciones, pueden beneficiarse de una tasa de
 * impuesto mucho menor en comparación con otros productos.
 *
 */
public class SuperReducedProduct extends Product{
    /**
     * Tasa de impuesto super reducido aplicable a ciertos productos, expresado como un porcentaje.
     */
    private static final double SUPER_REDUCED_TAX_RATE = 4.0;
    /**
     * Umbral de precio a partir del cual el producto es elegible para aplicar la tasa de impuesto
     * super reducido. Si el precio del producto es menor a este umbral, se aplica el impuesto.
     */
    private static final double SUPER_REDUCED_TAX_RATE_THRESHOLD = 100.0;
    /**
     * Constructor para crear una instancia de un producto con impuesto super reducido.
     *
     * @param name El nombre del producto.
     * @param brand La marca del producto.
     * @param mrp El precio de venta sugerido por el fabricante (MRP).
     */
    public SuperReducedProduct(String name, String brand, double mrp) {
        super(name, brand, mrp, "SUPER_REDUCED");
    }
    /**
     * Calcula el precio final del producto aplicando la tasa de impuesto super reducido si el
     * precio del producto está por debajo de un cierto umbral.
     *
     * @param priceAtShop El precio base del producto sin impuestos.
     * @return El precio del producto después de aplicar el impuesto super reducido, si es aplicable. De lo contrario, devuelve el precio sin cambios.
     */
    public double calculatePrice(double priceAtShop) {
        if (priceAtShop >= SUPER_REDUCED_TAX_RATE_THRESHOLD) {
            return priceAtShop;
        } else {
            double originalPrice = priceAtShop / (1 + SUPER_REDUCED_TAX_RATE /100);
            return originalPrice;
        }
    }
}
