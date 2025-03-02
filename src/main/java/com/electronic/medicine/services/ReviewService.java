package com.electronic.medicine.services;

import com.electronic.medicine.DTO.ReviewRequest;
import com.electronic.medicine.entity.Review;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    public void addSpecialistReview(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setAuthor(reviewRequest.getAuthor());
        review.setMessage(reviewRequest.getMessage());
        reviewRepository.saveAndFlush(review);
        User user = userService.findById(reviewRequest.getSpecId());
        Set<Review> reviewSet = user.getReviews();
        reviewSet.add(review);
        user.setReviews(reviewSet);
        userService.saveUser(user);
        log.debug("Успешное добавление отзыва для специалиста c id: {}", user.getId());
    }
}

