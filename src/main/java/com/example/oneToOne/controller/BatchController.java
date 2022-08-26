package com.example.oneToOne.controller;
import com.example.oneToOne.entity.Batch;
import com.example.oneToOne.entity.Book;
import com.example.oneToOne.entity.Library;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {
    private final BatchRepository batchRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public BatchController(BatchRepository batchRepository, MedicineRepository medicineRepository) {
        this.batchRepository = batchRepository;
        this.medicineRepository = medicineRepository;
    }

    @PostMapping
    public ResponseEntity<Batch> create(@RequestBody Batch batch) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(batch.getMedicine().getId());
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        batch.setMedicine(optionalMedicine.get());

        Batch savedBatch = batchRepository.save(batch);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBatch.getId()).toUri();

        return ResponseEntity.created(location).body(savedBatch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> update(@RequestBody Batch batch, @PathVariable Integer id) {
        Optional<Batch> oldBatch = batchRepository.findById(id);
        Medicine defaultMedicine = medicineRepository.findById(1).get();
        if(oldBatch == null) {
            batch.setMedicine(defaultMedicine);
        } else {
            batch.setId(oldBatch.get().getId());
            batch.setMedicine(oldBatch.get().getMedicine());
        }
        batchRepository.save(batch);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Batch> delete(@PathVariable Integer id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);
        if (!optionalBatch.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        batchRepository.delete(optionalBatch.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {
        return ResponseEntity.ok(batchRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getById(@PathVariable Integer id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);
        if (!optionalBatch.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalBatch.get());
    }
}

