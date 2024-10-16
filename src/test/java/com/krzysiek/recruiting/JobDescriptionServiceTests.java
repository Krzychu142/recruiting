package com.krzysiek.recruiting;

import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import com.krzysiek.recruiting.service.JobDescriptionServiceServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JobDescriptionServiceTests {

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;

    @InjectMocks
    private JobDescriptionServiceServiceImplementation jobDescriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidJobDescription_whenCreateJobDescription_thenSaveInRepository(){
        //Given
        //When
        //Then
    }

    @Test
    void givenInvalidJobDescription_whenCreateJobDescription_thenThrowValidationException(){
        //Given
        //When
        //Then
    }

}
