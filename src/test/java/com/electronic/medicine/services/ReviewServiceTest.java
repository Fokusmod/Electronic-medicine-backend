package com.electronic.medicine.services;

import com.electronic.medicine.DTO.ReviewRequest;
import com.electronic.medicine.entity.Review;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserService userService;

    @Test
    void loadReviewServiceContext() {
        assertNotNull(this.reviewRepository);
        assertNotNull(this.userService);
        assertNotNull(this.reviewService);
    }

    @Test
    @DisplayName("addSpecialistReview успешное сохранение отзыва")
    void addSpecialistReview() {
        ReviewRequest request = new ReviewRequest(1L,"User","Good work");
        Review review = new Review();
        review.setId(request.getSpecId());
        review.setAuthor(request.getAuthor());
        review.setMessage(request.getMessage());

        when(this.reviewRepository.saveAndFlush(review)).thenReturn(review);
        when(this.userService.findById(review.getId())).thenReturn(mock(User.class));

        this.reviewRepository.saveAndFlush(review);

        User user = this.userService.findById(review.getId());
        user.getReviews().add(review);

        doNothing().when(userService).saveUser(user);
        this.userService.saveUser(user);

        verify(this.reviewRepository).saveAndFlush(review);
        verify(this.userService).findById(review.getId());
        verify(this.userService).saveUser(user);
        verifyNoMoreInteractions(reviewRepository);
    }
}