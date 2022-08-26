package com.example.oneToOne.controller;

import com.example.oneToOne.entity.Medicine;
import com.example.oneToOne.repository.BatchRepository;
import com.example.oneToOne.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/medicine")
public class MedicineController {
    private final MedicineRepository medicineRepository;
    private final BatchRepository batchRepository;

    @Autowired
    public MedicineController(MedicineRepository medicineRepository, BatchRepository batchRepository) {
        this.medicineRepository = medicineRepository;
        this.batchRepository = batchRepository;
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineRepository.save(medicine);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedMedicine.getId()).toUri();

        return ResponseEntity.created(location).body(savedMedicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(@PathVariable Integer id,@RequestBody Medicine medicine) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        medicine.setId(optionalMedicine.get().getId());
        medicineRepository.save(medicine);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Medicine> delete(@PathVariable Integer id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        medicineRepository.delete(optionalMedicine.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable Integer id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalMedicine.get());
    }

    @GetMapping
    public ResponseEntity<Page<Medicine>> getAll(Pageable pageable) {
        return ResponseEntity.ok(medicineRepository.findAll(pageable));
    }
}

