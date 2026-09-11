package com.backendsoft.foodmenu.menu.lib;


import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;

public interface MenuPdfService {
    byte[] generateMenuOrderPdf(String menuId) throws MenuNotFoundException;
}
