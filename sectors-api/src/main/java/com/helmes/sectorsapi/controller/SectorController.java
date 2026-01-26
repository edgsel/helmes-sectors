package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.SectorDTO;
import com.helmes.sectorsapi.service.SectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sectors")
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/tree")
    public ResponseEntity<List<SectorDTO>> getSectorTree() {
        return ResponseEntity.ok(sectorService.getSectorTree());
    }
}
