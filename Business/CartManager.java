package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Esta clase se utiliza para gestionar un conjunto de productos en un carrito de compras.
 */
public class CartManager {
    ArrayList<Cart> cartProducts;

    /**
     * Construye un nuevo CartManager con la lista de productos en el carrito especificada.
     * @param cartProducts la lista de productos en el carrito
     */
    public CartManager(ArrayList<Cart> cartProducts) {
        this.cartProducts = cartProducts;
    }

    /**
     * Este método proporciona acceso a la lista de productos en el carrito.
     * @return cartProducts la lista de productos en el carrito.
     */
    public ArrayList<Cart> getCartProducts() {
        return cartProducts;
    }
}