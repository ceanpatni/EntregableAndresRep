package com.EntregableAndres.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Declaración del Logger
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Registrar el error con nivel ERROR
        logger.error("Error de recurso no encontrado: {}", ex.getMessage(), ex);

        // Agregar el mensaje de error al atributo flash
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(
            Exception ex,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Registrar el error general con nivel ERROR
        logger.error("Ha ocurrido un error inesperado: {}", ex.getMessage(), ex);

        // Agregar el mensaje de error al atributo flash
        redirectAttributes.addFlashAttribute("error", "Ha ocurrido un error: " + ex.getMessage());
        return "redirect:/error";
    }
}
