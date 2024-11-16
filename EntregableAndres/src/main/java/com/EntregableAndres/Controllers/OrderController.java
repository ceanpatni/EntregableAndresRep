package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.Order;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@Api(tags = "Orders", description = "Operaciones relacionadas con las órdenes") // Descripción general de los endpoints de este controlador
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Obtener todas las órdenes", notes = "Este endpoint obtiene una lista de todas las órdenes")
    @GetMapping
    public String getAllOrders(Model model) {
        try {
            model.addAttribute("orders", orderService.getAllOrders());
            return "order/index";
        } catch (Exception e) {
            logger.error("Error al obtener las órdenes", e);
            model.addAttribute("error", "Error al obtener las órdenes: " + e.getMessage());
            return "error";
        }
    }

    @ApiOperation(value = "Crear una nueva orden", notes = "Este endpoint muestra el formulario para crear una nueva orden")
    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/form";
    }

    @ApiOperation(value = "Guardar una nueva orden", notes = "Este endpoint guarda la información de una nueva orden")
    @PostMapping
    public String saveOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {
        try {
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Orden guardada exitosamente");
            return "redirect:/orders";
        } catch (Exception e) {
            logger.error("Error al guardar la orden", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @ApiOperation(value = "Editar una orden", notes = "Este endpoint muestra el formulario para editar una orden existente")
    @GetMapping("/{id}/edit")
    public String showEditOrderForm(@ApiParam(value = "ID de la orden", required = true) @PathVariable("id") int id, Model model) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
            model.addAttribute("order", order);
            return "order/form";
        } catch (ResourceNotFoundException e) {
            logger.error("Orden no encontrada con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/orders";
        } catch (Exception e) {
            logger.error("Error al editar la orden con id: " + id, e);
            model.addAttribute("error", "Error al editar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @ApiOperation(value = "Actualizar una orden", notes = "Este endpoint actualiza los detalles de una orden existente")
    @PostMapping("/{id}/edit")
    public String updateOrder(@ApiParam(value = "ID de la orden", required = true) @PathVariable("id") int id,
                              @ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {
        try {
            order.setId(id);
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Orden actualizada exitosamente");
            return "redirect:/orders";
        } catch (Exception e) {
            logger.error("Error al actualizar la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la orden: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @ApiOperation(value = "Eliminar una orden", notes = "Este endpoint elimina una orden según el ID proporcionado")
    @GetMapping("/{id}/delete")
    public String deleteOrder(@ApiParam(value = "ID de la orden", required = true) @PathVariable("id") int id,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Orden eliminada exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la orden: " + e.getMessage());
        }
        return "redirect:/orders";
    }
}
