package com.krzysiek.recruiting.dto.responsDTOs;

import com.krzysiek.recruiting.dto.FileDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadAllFileResponseDTO extends BaseResponseDTO {

    private final List<FileDTO> files;

    public LoadAllFileResponseDTO(String message, List<FileDTO> files) {
        super(message);
        this.files = files;
    }
}
