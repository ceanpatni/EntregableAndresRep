package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.Product;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Api(tags = "Productos", description = "Operaciones relacionadas con los productos")  // Describe la clase
@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Obtener todos los productos", notes = "Devuelve una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa"),
            @ApiResponse(code = 500, message = "Error al obtener los productos")
    })
    @GetMapping
    public String getAllProducts(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            return "product/index";
        } catch (Exception e) {
            logger.error("Error al obtener los productos", e);
            model.addAttribute("error", "Error al obtener los productos: " + e.getMessage());
            return "error";
        }
    }

    @ApiOperation(value = "Crear un nuevo producto", notes = "Muestra el formulario para crear un nuevo producto")
    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("isNew", true);
        return "product/form";
    }

    @ApiOperation(value = "Editar un producto existente", notes = "Muestra el formulario para editar un producto existente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Formulario de edición cargado exitosamente"),
            @ApiResponse(code = 404, message = "Producto no encontrado"),
            @ApiResponse(code = 500, message = "Error al obtener el producto")
    })
    @GetMapping("/{id}/edit")
    public String showEditProductForm(@ApiParam(value = "ID del producto a editar", required = true) @PathVariable("id") int id, Model model) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
            model.addAttribute("product", product);
            model.addAttribute("isNew", false);
            return "product/form";
        } catch (ResourceNotFoundException e) {
            logger.error("Producto no encontrado con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/products";
        } catch (Exception e) {
            logger.error("Error al editar el producto con id: " + id, e);
            model.addAttribute("error", "Error al editar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @ApiOperation(value = "Guardar un producto", notes = "Guarda un nuevo producto en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Producto guardado exitosamente"),
            @ApiResponse(code = 500, message = "Error al guardar el producto")
    })
    @PostMapping
    public String saveProduct(@ApiParam(value = "Producto a guardar", required = true) @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
            return "redirect:/products";
        } catch (Exception e) {
            logger.error("Error al guardar el producto", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @ApiOperation(value = "Actualizar un producto", notes = "Actualiza un producto existente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Producto actualizado exitosamente"),
            @ApiResponse(code = 404, message = "Producto no encontrado"),
            @ApiResponse(code = 500, message = "Error al actualizar el producto")
    })
    @PostMapping("/{id}/edit")
    public String updateProduct(@ApiParam(value = "ID del producto a actualizar", required = true) @PathVariable("id") int id,
                                @ApiParam(value = "Producto con los nuevos datos", required = true) @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
            return "redirect:/products";
        } catch (Exception e) {
            logger.error("Error al actualizar el producto con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @ApiOperation(value = "Eliminar un producto", notes = "Elimina un producto de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Producto eliminado exitosamente"),
            @ApiResponse(code = 500, message = "Error al eliminar el producto")
    })
    @GetMapping("/{id}/delete")
    public String deleteProduct(@ApiParam(value = "ID del producto a eliminar", required = true) @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar el producto con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
