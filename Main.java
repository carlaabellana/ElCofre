import business.product.ProductManager;
import business.shop.ShopManager;
import persistence.ApiHelper;
import persistence.ProductApiJsonDAO;
import presentation.ConsoleUIManager;
import presentation.Controller;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * La clase Main sirve como punto de entrada para la aplicación elCofre Digital Shopping Experiences.
 * Inicializa los componentes necesarios y lanza la aplicación.
 */
public class Main {
    /**
     * El método principal de la aplicación, responsable de iniciar elCofre Digital Shopping Experiences.
     *
     * @param args Los argumentos de línea de comandos (sin usar en esta aplicación).
     */
    public static void main(String[] args) {

        ConsoleUIManager uiManager = new ConsoleUIManager();
        uiManager.showPrincipalMessage();
        ApiHelper apiHelper = new ApiHelper();

        if(!apiHelper.isConnected()){
            uiManager.showMessage(apiHelper.getLastError());
        }else{
            uiManager.showMessage("Starting program...\n");
        }
        try {
            ProductManager productManager = new ProductManager();
            ShopManager shopManager = new ShopManager();
            ProductApiJsonDAO ProductApiJsonDAO = new ProductApiJsonDAO();

            Controller controller = new Controller(uiManager, productManager, shopManager, apiHelper, ProductApiJsonDAO);
            controller.run();
        } catch (FileNotFoundException fileNotFoundException) {
            uiManager.showMessage("\n\nVerifying local files...\nError: The products.json file can’t be accessed.\n\nShutting down...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}