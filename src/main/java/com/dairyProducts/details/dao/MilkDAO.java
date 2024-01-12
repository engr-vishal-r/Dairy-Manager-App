package com.dairyProducts.details.dao;

import com.dairyProducts.details.entity.Milk;
import com.dairyProducts.details.repository.MilkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MilkDAO {

    @Autowired
    MilkRepository milkRepo;

    public Optional<Milk> getMilkDetailsDao(long cardNumber) {
        return milkRepo.findByCardNumber(cardNumber);
    }

    public String addMilkDetailsDao(Milk milk) {
        milkRepo.save(milk);
        return "Details Added Successfully";
    }

    public String updateMilkDetailsDao(Milk milk) {
        milkRepo.save(milk);
        return "Details Updated Successfully";
    }

    public String deleteMilkDetailsDao(int id) {
        milkRepo.deleteById(id);
        return "Details Deleted Successfully";

    }
}