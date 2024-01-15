package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.MilkStockDTO;
import com.dairyProducts.details.entity.MilkStock;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.MilkStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class MilkStockService {
    @Autowired
    MilkStockDTO milkStockDTO;
    @Autowired
    private MilkStockRepository milkStockRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public MilkStockService(MilkStockRepository milkStockRepo) {
        this.milkStockRepo = milkStockRepo;
    }

    public ResponseEntity<?> getMilkStockDetailsService(int id) {
        Optional<MilkStock> milkOptional = milkStockRepo.findById(id);

        if (milkOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No details found for the provided id: " + id);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(milkStockDTO);
        }
    }
    public ResponseEntity<String> addMilkStockDetailsService(MilkStockDTO milkStockDTO) {
        MilkStock milkStock = new MilkStock();
        milkStock.setLoadedQuantity(milkStockDTO.getLoadedQuantity());
        milkStock.setEmployeeId(milkStockDTO.getEmployeeId());
        milkStock.setBalanceQuantity(milkStockDTO.getLoadedQuantity());
        String quantity = Integer.toString((int) milkStockDTO.getLoadedQuantity());

        if(milkStockDTO.getEmployeeId().isEmpty() || !milkStockDTO.getEmployeeId().matches("[0-9]{6}")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee id is mandatory & please check 6 digit employee id to add details");
        }else if(milkStockDTO.getLoadedQuantity() == 0 || !quantity.matches("[0-9]+([.][0-9]+)?")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is mandatory and it should be in numeric to add details");
        }else {
            milkStockRepo.save(milkStock);
            return ResponseEntity.status(HttpStatus.OK).body("Stock Details successfully added in the database");
        }
    }
}
