package com.backendsoft.foodmenu.menu;

import com.backendsoft.foodmenu.meal.mapper.MealMapper;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.lib.MenuPdfService;
import com.backendsoft.foodmenu.menu.mapper.MenuMapper;
import com.backendsoft.foodmenu.order.OrderItemRepository;
import com.backendsoft.foodmenu.order.OrderItemDto;
import com.backendsoft.foodmenu.menu.request.MealOrderData;
import com.backendsoft.foodmenu.menu.request.MenuOrderData;
import com.backendsoft.foodmenu.menu.dao.MenuRepository;
import com.backendsoft.foodmenu.menu.lib.Menu;
import com.backendsoft.foodmenu.order.OrderItem;
import com.backendsoft.foodmenu.order.mapper.OrderMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MenuPdfServiceImpl implements MenuPdfService {
    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuMapper menuMapper;
    private final MealMapper mealMapper;
    private final OrderMapper orderMapper;

    public MenuPdfServiceImpl(MenuRepository menuRepository, OrderItemRepository orderItemRepository, MenuMapper menuMapper, MealMapper mealMapper, OrderMapper orderMapper) {
        this.menuRepository = menuRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuMapper = menuMapper;
        this.mealMapper = mealMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public byte[] generateMenuOrderPdf(String menuId) throws MenuNotFoundException {
        MenuOrderData menuOrderData = createMenuOrderData(menuId);

        try {
            return build(menuOrderData);
        } catch (DocumentException e) {
            throw new RuntimeException("Error during PDF generation: " + e.getMessage(), e);
        }
    }

    private MenuOrderData createMenuOrderData(String menuId) throws MenuNotFoundException {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(
                        String.format("The menu with id %s does not exist", menuId)));

        List<OrderItem> orderItems = orderItemRepository.findByMenuId(menuId);
        int totalCmd = (int) orderItemRepository.countOrderItemsByMenu(menuId);
        List<MealOrderData> mealOrderDataList = menu.getMenuMeals().stream().map(mm->{
            MealOrderData moData = new MealOrderData();
            moData.setMeal(mealMapper.fromEntity(mm.getMeal()));
            moData.setTotalMealOrder((int) orderItemRepository.countLinesForMealInMenu(menuId,mm.getMeal().getId()));
            return moData;
        }).toList();

        MenuOrderData menuOrderData = new MenuOrderData();
        menuOrderData.setMenu(menuMapper.fromEntity(menu));
        menuOrderData.setCustomerOrders(orderItems.stream().map(orderMapper::fromEntity).collect(Collectors.toList()));
        menuOrderData.setMealOrderDatas(mealOrderDataList);
        menuOrderData.setTotalOrders(totalCmd);

        return menuOrderData;
    }

    private byte[] build(MenuOrderData data) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);
        document.open();

        addTitle(document, data);
        addTable(document, data);
        addFooter(document, data);

        document.close();
        return baos.toByteArray();
    }

    private void addTitle(Document document, MenuOrderData data) throws DocumentException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateFormatted = data.getMenu().getDate().format(formatter);

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

        Paragraph title = new Paragraph("Orders for " + dateFormatted, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        document.add(title);
    }

    private void addTable(Document document, MenuOrderData data) throws DocumentException {

        List<MealOrderData> mealOrderDatas = data.getMealOrderDatas();
        List<OrderItemDto> customerOrders = data.getCustomerOrders();

        // Number of columns = (1 Person column) + (1 column per meal)
        PdfPTable table = new PdfPTable(mealOrderDatas.size() + 1);
        table.setWidthPercentage(100);

        // Uniform widths
        float[] widths = new float[mealOrderDatas.size() + 1];
        Arrays.fill(widths, 2f);
        table.setWidths(widths);

        // Header style
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerBg = new BaseColor(70, 130, 180);

        // First column
        PdfPCell hPersonne = new PdfPCell(new Phrase("Person", headerFont));
        hPersonne.setBackgroundColor(headerBg);
        hPersonne.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hPersonne);

        // Meal columns
        for (MealOrderData mealOrderData : mealOrderDatas) {
            String header = mealOrderData.getMeal().getName() + " (" + mealOrderData.getTotalMealOrder()+")";
            PdfPCell h = new PdfPCell(new Phrase(header, headerFont));
            h.setBackgroundColor(headerBg);
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h);
        }

        // ----- Customer rows -----

        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11);

        for (OrderItemDto orderItem : customerOrders) {

            // Person column
            PdfPCell cellName = new PdfPCell(new Phrase(orderItem.getCustomer().getFullname(), cellFont));
            cellName.setBackgroundColor(new BaseColor(240, 240, 240));
            table.addCell(cellName);

            // Choice column
            for (MealOrderData mealOrderData : mealOrderDatas) {

                boolean match = mealOrderData.getMeal().getId().equals(orderItem.getMeal().getId());

                Font choiceFont = match
                        ? new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.GREEN)
                        : new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.RED);

                PdfPCell choix = new PdfPCell(new Phrase(match ? "X" : "--", choiceFont));

                choix.setHorizontalAlignment(Element.ALIGN_CENTER);
                choix.setBackgroundColor(
                        match ? new BaseColor(220, 255, 220) : new BaseColor(255, 230, 230)
                );

                table.addCell(choix);
            }
        }

        document.add(table);
    }

    private void addFooter(Document document, MenuOrderData data) throws DocumentException {

        DateTimeFormatter french = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
        String dateFr = LocalDate.now().format(french);

        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

        Paragraph footer = new Paragraph(
                "Total: " + data.getTotalOrders() +
                        "     Generated on: " + dateFr +
                        "     No.: " + data.getMenu().getId(),
                footerFont
        );

        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(15);

        document.add(footer);
    }
}
