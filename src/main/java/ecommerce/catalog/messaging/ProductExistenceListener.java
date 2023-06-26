package ecommerce.catalog.messaging;

import ecommerce.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductExistenceListener {

    private ProductService productService;

    private JmsTemplate jmsTemplate;

    @Autowired
    public ProductExistenceListener(ProductService productService, JmsTemplate jmsTemplate) {
        this.productService = productService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "ProductValidationQueue")
    public void checkProductExistence(Long productId) {
        boolean productExists = productService.getProductById(productId).isPresent();

        // Send response back to the cart service
        jmsTemplate.convertAndSend("ProductValidationResponseQueue", productExists);
    }
}
