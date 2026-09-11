package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.utils.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class MenuPage extends Page {
    private List<MenuDto> menus;
}
