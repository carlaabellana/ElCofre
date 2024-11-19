package business.shop;

import business.Catalogue;
import business.product.Product;
import java.util.ArrayList;
/**
 * Herencia de Shop para implementar funcionalidad para una tienda patrocinada por una marca específica,
 * ofreciendo descuentos en productos de esa marca.
 */
public class SponsoredShop extends Shop{
    private String sponsorBrand;
    /**
     * Construye una SponsoredShop con detalles específicos y la marca patrocinadora.
     * @param name El nombre de la tienda.
     * @param description La descripción de la tienda.
     * @param since El año de fundación de la tienda.
     * @param catalogue El catálogo de productos ofrecidos por la tienda.
     * @param sponsorBrand La marca patrocinadora de la tienda, ofreciendo descuentos en sus productos.
     */
    public SponsoredShop(String name, String description, int since, ArrayList<Catalogue> catalogue, String sponsorBrand) {
        super(name, description, since, "SPONSORED",catalogue);
        this.sponsorBrand = sponsorBrand;
    }
    /**
     * Establece la marca patrocinadora de la tienda.
     * @param sponsorBrand La nueva marca patrocinadora.
     */
    public void setSponsorBrand(String sponsorBrand) {
        this.sponsorBrand = sponsorBrand;
    }

    /**
     * Calcula el descuento para un producto si es de la marca patrocinadora.
     * @param price El precio original del producto.
     * @param product El producto para el cual calcular el descuento.
     * @return El precio con descuento si el producto es de la marca patrocinadora, de lo contrario, el precio original.
     */
    @Override
    public double calculateDiscount(double price, Product product) {
        if (product.getBrand().equals(sponsorBrand)) {
            price = product.calculatePrice(price) * 0.9;
        } else {
            price = product.calculatePrice(price);
        }
        return price;
    }
}
