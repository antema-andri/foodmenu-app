package com.backendsoft.foodmenu.menu;

import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.lib.MenuPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping({"/api/"})
public class MenuPdfController {
    private final MenuPdfService menuPdfService;

    public MenuPdfController(MenuPdfService menuPdfService) {
        this.menuPdfService = menuPdfService;
    }

    @GetMapping("menus/{menuId}/menu-order-pdf")
    public ResponseEntity<byte[]> generateOrderPDF(@PathVariable String menuId) throws MenuNotFoundException {
        byte[] pdfBytes=menuPdfService.generateMenuOrderPdf(menuId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=menu-" + menuId + "-orders.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping(value = "menus/{menuId}/menu-order-str-pdf", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getStrPdf(@PathVariable String menuId) throws MenuNotFoundException {
        byte[] pdfBytes = menuPdfService.generateMenuOrderPdf(menuId); // ton code
        String base64 = Base64.getEncoder().encodeToString(pdfBytes);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"file.pdf\"")
                .body(base64);
    }
}
