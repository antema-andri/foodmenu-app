package com.backendsoft.foodmenu.menu;

import com.backendsoft.foodmenu.customer.lib.Customer;
import com.backendsoft.foodmenu.customer.lib.CustomerNotFoundException;
import com.backendsoft.foodmenu.customer.lib.CustomerRepository;
import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.meal.lib.Meal;
import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealRepository;
import com.backendsoft.foodmenu.meal.lib.MealUnchangedException;
import com.backendsoft.foodmenu.menu.dao.MenuMealRepository;
import com.backendsoft.foodmenu.menu.dao.MenuRepository;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.exception.UndeletableMenuException;
import com.backendsoft.foodmenu.menu.lib.Menu;
import com.backendsoft.foodmenu.menu.lib.MenuMeal;
import com.backendsoft.foodmenu.menu.lib.MenuService;
import com.backendsoft.foodmenu.menu.lib.MenuValidator;
import com.backendsoft.foodmenu.menu.mapper.CycleAvoidingMappingContext;
import com.backendsoft.foodmenu.menu.mapper.MenuMapper;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import com.backendsoft.foodmenu.menu.request.MenuPage;
import com.backendsoft.foodmenu.menu.request.MenuUpdateStatusRequest;
import com.backendsoft.foodmenu.menu.response.MealOrderTotalResponse;
import com.backendsoft.foodmenu.menu.response.NumberOderResp;
import com.backendsoft.foodmenu.order.*;
import com.backendsoft.foodmenu.order.mapper.OrderMapper;
import com.backendsoft.foodmenu.utils.EntityNotFoundException;
import com.backendsoft.foodmenu.utils.ErrorCodes;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import com.backendsoft.foodmenu.utils.RegNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MealRepository mealRepository;
    private final MenuMealRepository menuMealRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuMapper menuMapper;
    private final OrderMapper orderMapper;

    public MenuServiceImpl(MenuRepository menuRepository, MealRepository mealRepository, MenuMealRepository menuMealRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository, MenuMapper menuMapper, OrderMapper orderMapper) {
        this.menuRepository = menuRepository;
        this.mealRepository = mealRepository;
        this.menuMealRepository = menuMealRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuMapper = menuMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public MenuDto save(MenuDto menuDto) throws MenuNotFoundException, InvalidEntityException {
        List<String> errors = MenuValidator.validate(menuDto);
        String prefixId= "MENU";
        int lastMenuCount=(int) menuRepository.count();
        int maxMeals=3;

        if (!errors.isEmpty()) {
            log.error("Menu is not valid: {}", menuDto);
            System.out.println(errors.get(0));
            throw new InvalidEntityException(
                    "The menu is not valid", ErrorCodes.MENU_NOT_VALID, errors
            );
        }

        Menu menu = menuMapper.fromDto(menuDto);
        menu.setId(RegNumberUtil.generate(prefixId,lastMenuCount+1));
        menu.setActive(true);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setMaxMeals(maxMeals);
        menu.setMenuMeals(null); // clear menuMeals

        // load existing meals
        List<Meal> meals = new ArrayList<>();
        for (MealDto mDto: menuDto.getMeals()) {
            Meal meal=mealRepository.findById(mDto.getId()).orElseThrow(() -> new MenuNotFoundException(
                    String.format("The Meal with id %s does not exist", mDto.getId())
            ));
            meals.add(meal);
        }

        Menu newMenu=menuRepository.save(menu);

        for (Meal meal: meals) {
            MenuMeal mm=new MenuMeal();
            mm.setMeal(meal);
            mm.setMenu(newMenu);
            menuMealRepository.save(mm);
        }

        return menuMapper.fromEntity(newMenu);
    }

    @Override
    public MenuPage getMenus(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        MenuPage menuPageDto = new MenuPage();
        Page<Menu> menuPage = menuRepository.findByOrderByDateDesc(pageable);

        List<MenuDto> menuDtos = menuPage.getContent().stream().map(menuMapper::fromEntity).collect(Collectors.toList());
        menuPageDto.setMenus(menuDtos);
        menuPageDto.setCurrentPage(menuPage.getNumber());
        menuPageDto.setSize(menuPage.getSize());
        menuPageDto.setTotalPage(menuPage.getTotalPages());
        return menuPageDto;
    }

    @Override
    public MenuDto getMenu(String menuId) throws MenuNotFoundException {
        Menu menu=menuRepository.findById(menuId).orElseThrow(()->new MenuNotFoundException(String.format("The menu with id %s does not exist", menuId)));
        return menuMapper.fromEntity(menu);
    }

    @Override
    public OrderItemDto chooseMenu(String menuId, String customerId, String mealId)
            throws MenuNotFoundException, CustomerNotFoundException, MealNotFoundException, InvalidMealOrderException, ExcessOrderException {
        OrderItem orderItem=new OrderItem();

        if (!StringUtils.hasLength(menuId) || !StringUtils.hasLength(customerId) || !StringUtils.hasLength(mealId)) {
            throw new IllegalArgumentException("menuId, customerId or mealId must not be empty");
        }

        Menu menu=menuRepository.findById(menuId)
                .orElseThrow(()->new MenuNotFoundException(String.format("The menu with id: %s does not exist",menuId)));

        if (!menu.isActive()) {
            throw new InvalidMealOrderException(String.format("No order possible for menu ID:%s already validated",menuId));
        }

        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException(String.format("The customer with id: %s does not exist",customerId)));

        Meal meal=mealRepository.findById(mealId)
                .orElseThrow(()->new MealNotFoundException(String.format("The meal with id: %s does not exist",mealId)));

        boolean mealInMenu = menuMealRepository.existsByMenuIdAndMealId(menuId, mealId);
        if (!mealInMenu) {
            throw new InvalidMealOrderException(String.format("The meal with id %s does not belong to the menu",meal.getId()));
        }

        boolean isExistingOrderItem=orderItemRepository.existsByCustomerIdAndMenuId(customerId,menuId);
        if (isExistingOrderItem) {
            throw new ExcessOrderException(String.format("The customer with ID: %s already has an order in menuID: %s",customerId,menuId));
        }

        orderItem.setId(UUID.randomUUID().toString());
        orderItem.setMenu(menu);
        orderItem.setCustomer(customer);
        orderItem.setMeal(meal);
        orderItem.setQuantity(1);

        return orderMapper.fromEntity(orderItemRepository.save(orderItem));
    }

    @Override
    public OrderItemDto replaceOrder(String menuId, String customerId, String newMealId) throws NoOrderToUpdateException, MenuNotFoundException, MealNotFoundException {

        if (!StringUtils.hasLength(menuId) || !StringUtils.hasLength(customerId) || !StringUtils.hasLength(newMealId)) {
            throw new IllegalArgumentException("menuId, customerId or mealId must not be empty");
        }

        Menu menu=menuRepository.findById(menuId)
                .orElseThrow(()->new MenuNotFoundException(String.format("The menu with id: %s does not exist",menuId)));

        if (!menu.isActive()) {
            throw new NoOrderToUpdateException(String.format("No order change possible for menu ID:%s already validated",newMealId));
        }

        // Check for the existence of the newMeal
        Meal newMeal=mealRepository.findById(newMealId)
                .orElseThrow(()->new MealNotFoundException(String.format("The meal with id: %s does not exist",newMealId)));

        // Check that the meal belongs to the menu via MenuMeals
        boolean mealInMenu = menuMealRepository.existsByMenuIdAndMealId(menuId, newMealId);
        if (!mealInMenu) {
            throw new IllegalArgumentException("This meal does not belong to this menu");
        }

        // Get existing OrderItem
        OrderItem orderItem = orderItemRepository
                .findByCustomerIdAndMenuId(customerId, menuId)
                .orElseThrow(() -> new NoOrderToUpdateException("No order to modify"));

        // if same Meal -> nothing to change
        if (orderItem.getMeal().getId().equals(newMealId)) {
            throw new NoOrderToUpdateException("The order already contains this meal");
        }

        // update
        orderItem.setMeal(newMeal);

        return orderMapper.fromEntity(orderItemRepository.save(orderItem));
    }

    @Override
    public MenuDto changeMealList(String menuId, String currentMealId, String newMealId) throws InvalidMealOrderException, MealNotFoundException, MealUnchangedException {

        if (!StringUtils.hasLength(menuId) || !StringUtils.hasLength(currentMealId) || !StringUtils.hasLength(newMealId)) {
            throw new IllegalArgumentException("menuId, mealId or newMealId must not be empty");
        }

        if (currentMealId.equals(newMealId)) {
            throw new MealUnchangedException("No meal to modify in the menu");
        }

        MenuMeal menuMeal=menuMealRepository.findByMenuIdAndMealId(menuId,currentMealId)
                .orElseThrow(()->new InvalidMealOrderException(String.format("The meal with id %s does not belong to the menu",currentMealId)));

        Meal newMeal=mealRepository.findById(newMealId)
                .orElseThrow(()->new MealNotFoundException(String.format("The meal with id: %s does not exist",newMealId)));

        // update the meal
        menuMeal.setMeal(newMeal);
        MenuMeal updatedMenuMeal=menuMealRepository.save(menuMeal);

        return menuMapper.fromEntity(updatedMenuMeal.getMenu());
    }

    @Override
    public OrderItemDto getCustmerOrderFromMenu(String menuId, String customerId) throws OrderNotFoundException {

        if (!StringUtils.hasLength(menuId) || !StringUtils.hasLength(customerId)) {
            throw new IllegalArgumentException("menuId or customerId must not be empty");
        }

        OrderItem orderItem = orderItemRepository
                .findByCustomerIdAndMenuId(customerId, menuId)
                .orElseThrow(() ->
                        new OrderNotFoundException(String.format("The order of the customer with id %s in the menu with id %s does not exist",customerId,menuId))
                );

        return orderMapper.fromEntity(orderItem);
    }

    @Override
    public MenuDto getLatestActiveMenu() throws MenuNotFoundException {
        Menu menu=menuRepository
                .findFirstByActiveIsTrueAndMenuMealsIsNotEmptyOrderByCreatedAtDesc()
                .orElseThrow(()->new MenuNotFoundException("No latest active menu with meals found"));

        return menuMapper.fromEntity(menu, new CycleAvoidingMappingContext());
    }

    @Override
    public NumberOderResp getOrderNumber(String menuId) throws MenuNotFoundException {
        NumberOderResp numberOderResp=new NumberOderResp();

        menuRepository.findById(menuId)
                .orElseThrow(()->new MenuNotFoundException(String.format("The menu with id: %s does not exist",menuId)));

        numberOderResp.setTotalEligibleCustomers((int) customerRepository.count());
        numberOderResp.setTotalCustomersOrdered((int) orderItemRepository.countDistinctCustomersByMenu(menuId));

        return numberOderResp;
    }

    @Override
    public MealOrderTotalResponse getOrderNumberOfMeal(String menuId, String mealId) throws MealNotFoundException, MenuNotFoundException {
        if (!mealRepository.existsById(mealId)) {
            throw new MealNotFoundException("Meal not found: " + mealId);
        }
        if (!menuRepository.existsById(menuId)) {
            throw new MenuNotFoundException("Menu not found: " + menuId);
        }

        MealOrderTotalResponse numberOderMealResp=new MealOrderTotalResponse();
        numberOderMealResp.setTotalOrderOfMeal((int) orderItemRepository.countLinesForMealInMenu(menuId,mealId));

        return numberOderMealResp;
    }

    @Override
    public void deleteMenu(String menuId) throws MenuNotFoundException, UndeletableMenuException {

        Menu menu=menuRepository.findById(menuId)
                .orElseThrow(()->new MenuNotFoundException(String.format("The menu with id: %s does not exist",menuId)));

        if (!menu.getOrderItems().isEmpty()) {
            throw new UndeletableMenuException(String.format("The menu with ID:%s containing orders cannot be deleted",menuId));
        }

        menu.getMenuMeals().forEach(menuMealRepository::delete);

        menuRepository.deleteById(menu.getId());
    }

    @Override
    public MenuDto validateMenu(String menuId, MenuUpdateStatusRequest menuUpdateStatusReq) throws MenuNotFoundException {

        if (menuUpdateStatusReq==null || menuUpdateStatusReq.getStatus()==null) {
            throw new IllegalArgumentException("The status must not be empty");
        }

        Menu menu=menuRepository.findById(menuId)
                .orElseThrow(()->new MenuNotFoundException(String.format("The menu with id: %s does not exist",menuId)));

        menu.setActive(menuUpdateStatusReq.getStatus());

        return menuMapper.fromEntity(menu);
    }
}
