package com.backendsoft.foodmenu.meal;

import com.backendsoft.foodmenu.meal.lib.*;
import com.backendsoft.foodmenu.meal.mapper.MealMapper;
import com.backendsoft.foodmenu.menu.request.MealPage;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    public MealDto save(MealDto mealDto) throws InvalidEntityException {
        List<String> errors = MealValidator.validate(mealDto);
        String prefixId= "MEAL";
        int lastMealCount=(int) mealRepository.count();

        if (!errors.isEmpty()) {
            log.error("Meal is not valid: {}", mealDto);
            System.out.println(errors.get(0));
            throw new InvalidEntityException("The meal is not valid", ErrorCodes.MEAL_NOT_VALID, errors);
        }

        Meal meal=mealMapper.fromDto(mealDto);
        meal.setId(RegNumberUtil.generate(prefixId,lastMealCount+1));

        return mealMapper.fromEntity(mealRepository.save(meal));
    }

    @Override
    public MealDto getMeal(String mealId) throws MealNotFoundException {
        Meal meal=mealRepository.findById(mealId)
                .orElseThrow(()->new MealNotFoundException(String.format("The meal with id %s does not exist", mealId)));
        return mealMapper.fromEntity(meal);
    }

    @Override
    public MealPage getMeals(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        MealPage mealPageDto = new MealPage();
        Page<Meal> mealPage = mealRepository.findMealsByKeyword(keyword, pageable);

        List<MealDto> mealDtos = mealPage.getContent().stream().map(mealMapper::fromEntity).collect(Collectors.toList());
        mealPageDto.setMeals(mealDtos);
        mealPageDto.setCurrentPage(mealPage.getNumber());
        mealPageDto.setSize(mealPage.getSize());
        mealPageDto.setTotalPage(mealPage.getTotalPages());
        return mealPageDto;
    }

    @Override
    public MealDto updateMeal(
            String mealId,
            String name,
            String description
    )throws MealNotFoundException, MealUnchangedException {

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealNotFoundException(
                        String.format("The meal with id %s does not exist", mealId))
                );

        boolean changed = false;

        if (StringUtils.hasText(name) && !meal.getName().equals(name)) {
            meal.setName(name);
            changed = true;
        }

        if (StringUtils.hasText(description) && !meal.getDescription().equals(description)) {
            meal.setDescription(description);
            changed = true;
        }

        if (!changed) {
            throw new MealUnchangedException("No modification detected");
        }

        return mealMapper.fromEntity(mealRepository.save(meal));
    }

    @Override
    public void deleteMeal(String mealId) throws MealNotFoundException, UndeletableMealException {

        if (!StringUtils.hasLength(mealId)) {
            throw new IllegalArgumentException("The meal id must not be empty");
        }

        Meal meal=mealRepository.findById(mealId)
                .orElseThrow(()->new MealNotFoundException(String.format("The meal with id %s does not exist", mealId)));

        if (!meal.getMenuMeals().isEmpty()) {
            throw new UndeletableMealException(String.format("The meal with ID:%s is contained in at least one menu and cannot be deleted", mealId));
        }

        mealRepository.deleteById(mealId);
    }
}
