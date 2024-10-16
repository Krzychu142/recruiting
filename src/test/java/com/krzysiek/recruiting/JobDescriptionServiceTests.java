package com.krzysiek.recruiting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.krzysiek.recruiting.model.JobDescription;
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
        JobDescription jobDescription = new JobDescription();
        jobDescription.setId(1L);
        jobDescription.setJobName("test");

        //When
        when(jobDescriptionRepository.save(jobDescription)).thenReturn(jobDescription);
        Long createdJobId = jobDescriptionService.createJobDescription(jobDescription);

        // Then
        assertNotNull(createdJobId);
        assertEquals(1L, createdJobId);
        verify(jobDescriptionRepository, times(1)).save(jobDescription);
    }

    @Test
    void givenInvalidJobDescription_whenCreateJobDescription_thenThrowValidationException(){
        //Given
        //When
        //Then
    }

}
