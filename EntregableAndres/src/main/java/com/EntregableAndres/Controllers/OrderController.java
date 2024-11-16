package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.Order;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    // Creamos un logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getAllOrders(Model model) {
        try {
            model.addAttribute("orders", orderService.getAllOrders());
            return "order/index";
        } catch (Exception e) {
            // Registrar el error en consola
            logger.error("Error al obtener las órdenes", e);
            model.addAttribute("error", "Error al obtener las órdenes: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/form";
    }

    @PostMapping
    public String saveOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {
        try {
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Orden guardada exitosamente");
            return "redirect:/orders";
        } catch (Exception e) {
            // Registrar error al guardar la orden
            logger.error("Error al guardar la orden", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditOrderForm(@PathVariable("id") int id, Model model) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
            model.addAttribute("order", order);
            return "order/form";
        } catch (ResourceNotFoundException e) {
            // Registrar el error específico en consola
            logger.error("Orden no encontrada con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/orders";
        } catch (Exception e) {
            // Capturar otros errores inesperados y registrarlos
            logger.error("Error al editar la orden con id: " + id, e);
            model.addAttribute("error", "Error al editar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateOrder(@PathVariable("id") int id, @ModelAttribute("order") Order order,
                              RedirectAttributes redirectAttributes) {
        try {
            order.setId(id);
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Orden actualizada exitosamente");
            return "redirect:/orders";
        } catch (Exception e) {
            // Registrar error al actualizar la orden
            logger.error("Error al actualizar la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Orden eliminada exitosamente");
        } catch (Exception e) {
            // Registrar error al eliminar la orden
            logger.error("Error al eliminar la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la orden: " + e.getMessage());
        }
        return "redirect:/orders";
    }
}
