package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.OrderItem;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.OrderItemService;
import com.EntregableAndres.Services.OrderService;
import com.EntregableAndres.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orderItems")
public class OrderItemController {

    // Creamos un logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getAllOrderItems(Model model) {
        try {
            model.addAttribute("orderItems", orderItemService.getAllOrderItems());
            return "orderItem/index";
        } catch (Exception e) {
            // Registrar el error en consola
            logger.error("Error al obtener los elementos de la orden", e);
            model.addAttribute("error", "Error al obtener los elementos de la orden: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    public String showNewOrderItemForm(Model model) {
        try {
            model.addAttribute("orderItem", new OrderItem());
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("products", productService.getAllProducts());
            return "orderItem/form";
        } catch (Exception e) {
            // Registrar el error en consola
            logger.error("Error al cargar el formulario para nuevo elemento de la orden", e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    public String saveOrderItem(@ModelAttribute("orderItem") OrderItem orderItem, RedirectAttributes redirectAttributes) {
        try {
            orderItemService.saveOrderItem(orderItem);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden guardado exitosamente");
            return "redirect:/orderItems";
        } catch (Exception e) {
            // Registrar el error en consola
            logger.error("Error al guardar el elemento de la orden", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditOrderItemForm(@PathVariable("id") int id, Model model) {
        try {
            OrderItem orderItem = orderItemService.getOrderItemById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Elemento de la orden no encontrado con id: " + id));
            model.addAttribute("orderItem", orderItem);
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("products", productService.getAllProducts());
            return "orderItem/form";
        } catch (ResourceNotFoundException e) {
            // Registrar el error específico en consola
            logger.error("Elemento de la orden no encontrado con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/orderItems";
        } catch (Exception e) {
            // Capturar otros errores inesperados y registrarlos
            logger.error("Error al editar el elemento de la orden con id: " + id, e);
            model.addAttribute("error", "Error al editar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateOrderItem(@PathVariable("id") int id, @ModelAttribute("orderItem") OrderItem orderItem,
                                  RedirectAttributes redirectAttributes) {
        try {
            orderItem.setId(id);
            orderItemService.saveOrderItem(orderItem);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden actualizado exitosamente");
            return "redirect:/orderItems";
        } catch (Exception e) {
            // Registrar error al actualizar el elemento de la orden
            logger.error("Error al actualizar el elemento de la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteOrderItem(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            orderItemService.deleteOrderItem(id);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden eliminado exitosamente");
        } catch (Exception e) {
            // Registrar error al eliminar el elemento de la orden
            logger.error("Error al eliminar el elemento de la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el elemento de la orden: " + e.getMessage());
        }
        return "redirect:/orderItems";
    }
}
