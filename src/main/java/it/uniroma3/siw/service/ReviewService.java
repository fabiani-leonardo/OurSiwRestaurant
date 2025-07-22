package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.MenuLine;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review findById(Long id) {
        return this.reviewRepository.findById(id).orElse(null);
    }

	public void save(Review review) {
		this.reviewRepository.save(review);
	}
	
    public Iterable<Review> getAllReviews(){
    	return this.reviewRepository.findAll();
    }
    
	public void remove(Long id) {
		this.reviewRepository.deleteById(id);
	}
	
	   public Review findByUser(User user) {
	        return this.reviewRepository.findByUser(user).orElse(null);
	    }
}
