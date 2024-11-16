package com.EntregableAndres.Controllers;

import com.EntregableAndres.Entity.OrderItem;
import com.EntregableAndres.Exception.ResourceNotFoundException;
import com.EntregableAndres.Services.OrderItemService;
import com.EntregableAndres.Services.OrderService;
import com.EntregableAndres.Services.ProductService;
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
@RequestMapping("/orderItems")
@Api(tags = "Order Items", description = "Operaciones relacionadas con los elementos de la orden")  // Descripción general de este controlador
public class OrderItemController {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Obtener todos los elementos de la orden", notes = "Este endpoint obtiene una lista de todos los elementos de la orden")
    @GetMapping
    public String getAllOrderItems(Model model) {
        try {
            model.addAttribute("orderItems", orderItemService.getAllOrderItems());
            return "orderItem/index";
        } catch (Exception e) {
            logger.error("Error al obtener los elementos de la orden", e);
            model.addAttribute("error", "Error al obtener los elementos de la orden: " + e.getMessage());
            return "error";
        }
    }

    @ApiOperation(value = "Crear un nuevo elemento de la orden", notes = "Este endpoint muestra el formulario para crear un nuevo elemento de la orden")
    @GetMapping("/new")
    public String showNewOrderItemForm(Model model) {
        try {
            model.addAttribute("orderItem", new OrderItem());
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("products", productService.getAllProducts());
            return "orderItem/form";
        } catch (Exception e) {
            logger.error("Error al cargar el formulario para nuevo elemento de la orden", e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "error";
        }
    }

    @ApiOperation(value = "Guardar un nuevo elemento de la orden", notes = "Este endpoint guarda los datos de un nuevo elemento de la orden")
    @PostMapping
    public String saveOrderItem(@ModelAttribute("orderItem") OrderItem orderItem, RedirectAttributes redirectAttributes) {
        try {
            orderItemService.saveOrderItem(orderItem);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden guardado exitosamente");
            return "redirect:/orderItems";
        } catch (Exception e) {
            logger.error("Error al guardar el elemento de la orden", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @ApiOperation(value = "Editar un elemento de la orden", notes = "Este endpoint muestra el formulario para editar un elemento de la orden existente")
    @GetMapping("/{id}/edit")
    public String showEditOrderItemForm(@ApiParam(value = "ID del elemento de la orden", required = true) @PathVariable("id") int id, Model model) {
        try {
            OrderItem orderItem = orderItemService.getOrderItemById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Elemento de la orden no encontrado con id: " + id));
            model.addAttribute("orderItem", orderItem);
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("products", productService.getAllProducts());
            return "orderItem/form";
        } catch (ResourceNotFoundException e) {
            logger.error("Elemento de la orden no encontrado con id: " + id, e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/orderItems";
        } catch (Exception e) {
            logger.error("Error al editar el elemento de la orden con id: " + id, e);
            model.addAttribute("error", "Error al editar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @ApiOperation(value = "Actualizar un elemento de la orden", notes = "Este endpoint actualiza los detalles de un elemento de la orden existente")
    @PostMapping("/{id}/edit")
    public String updateOrderItem(@ApiParam(value = "ID del elemento de la orden", required = true) @PathVariable("id") int id,
                                  @ModelAttribute("orderItem") OrderItem orderItem, RedirectAttributes redirectAttributes) {
        try {
            orderItem.setId(id);
            orderItemService.saveOrderItem(orderItem);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden actualizado exitosamente");
            return "redirect:/orderItems";
        } catch (Exception e) {
            logger.error("Error al actualizar el elemento de la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el elemento de la orden: " + e.getMessage());
            return "redirect:/orderItems";
        }
    }

    @ApiOperation(value = "Eliminar un elemento de la orden", notes = "Este endpoint elimina un elemento de la orden según el ID proporcionado")
    @GetMapping("/{id}/delete")
    public String deleteOrderItem(@ApiParam(value = "ID del elemento de la orden", required = true) @PathVariable("id") int id,
                                  RedirectAttributes redirectAttributes) {
        try {
            orderItemService.deleteOrderItem(id);
            redirectAttributes.addFlashAttribute("success", "Elemento de la orden eliminado exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar el elemento de la orden con id: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el elemento de la orden: " + e.getMessage());
        }
        return "redirect:/orderItems";
    }
}
