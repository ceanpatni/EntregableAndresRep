package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.Product;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    // Creamos un logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAllProducts(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            return "product/index";
        } catch (Exception e) {
            // Registrar el error en consola
            logger.error("Error al obtener los productos", e);
            model.addAttribute("error", "Error al obtener los productos: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("isNew", true);
        return "product/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
            model.addAttribute("product", product);
            model.addAttribute("isNew", false);
            return "product/form";
        } catch (ResourceNotFoundException e) {
            // Registrar el error específico en consola
            logger.error("Producto no encontrado con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/products";
        } catch (Exception e) {
            // Capturar otros errores inesperados y registrarlos
            logger.error("Error al editar el producto con id: " + id, e);
            model.addAttribute("error", "Error al editar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
            return "redirect:/products";
        } catch (Exception e) {
            // Registrar error al guardar el producto
            logger.error("Error al guardar el producto", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable("id") int id, @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
            return "redirect:/products";
        } catch (Exception e) {
            // Registrar error al actualizar el producto
            logger.error("Error al actualizar el producto con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            // Registrar error al eliminar el producto
            logger.error("Error al eliminar el producto con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
