package sk.seges.acris.showcase.mora.client.action.mocks.factory;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.showcase.mora.shared.domain.api.CommentData;
import sk.seges.acris.showcase.mora.shared.domain.api.MovieData;
import sk.seges.acris.showcase.mora.shared.domain.api.MovieData.MovieRating;
import sk.seges.acris.showcase.mora.shared.domain.api.common.RatingData;
import sk.seges.acris.showcase.mora.shared.domain.dto.CommentDto;
import sk.seges.acris.showcase.mora.shared.domain.dto.MovieDto;
import sk.seges.acris.showcase.mora.shared.domain.dto.common.RatingDto;


public class MoviesFactory {

	private static Set<MovieData> availableMovies = new HashSet<MovieData>();
	
	static {
		
		MovieData movie = createMovie();
		
		movie.setName("The Shawshank Redemption");
		movie.setDescription("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.Andy Dufresne is a young and successful banker whose life changes drastically when he is convicted and sentenced to life imprisonment for the murder of his wife and her lover. Set in the 1940's, the film shows how Andy, with the help of his friend Red, the prison entrepreneur, turns out to be a most unconventional prisoner.");
		movie.setPicturePath("/images/godfatherI.jpg");
		movie.setRating(createRating(MovieRating.EXCELLENT));
		movie.setComments(createComment("I believe that this film is the best story ever told on film, and I'm about to tell you why."));
		availableMovies.add(movie);
		
		movie = createMovie();
		movie.setName("The Godfather");
		movie.setDescription("The story begins as 'Don' Vito Corleone, the head of a New York Mafia 'family', oversees his daughter's wedding. His beloved son Michael has just come home from the war, but does not intend to become part of his father's business. Through Michael's life the nature of the family business becomes clear. The business of the family is just like the head of the family, kind and benevolent to those who give respect, but given to ruthless violence whenever anything stands against the good of the family. Don Vito lives his life in the way of the old country, but times are changing and some don't want to follow the old ways and look out for community and 'family'.");
		movie.setPicturePath("/images/shawshank.jpg");
		movie.setRating(createRating(MovieRating.EXCELLENT));
		movie.setComments(createComment("This movie is strong, good script, great casting, excellent acting, and over the top directing"));
		availableMovies.add(movie);
		
		movie = createMovie();
		movie.setName("The Social Network");
		movie.setDescription("On a fall night in 2003, Harvard undergrad and computer programming genius Mark Zuckerberg sits down at his computer and heatedly begins working on a new idea. In a fury of blogging and programming, what begins in his dorm room soon becomes a global social network and a revolution in communication. A mere six years and 500 million friends later, Mark Zuckerberg is the youngest billionaire in history... but for this entrepreneur, success leads to both personal and legal complications.");
		movie.setPicturePath("/images/socialnetwork.jpg");
		movie.setRating(createRating(MovieRating.GOOD));
		movie.setComments(createComment("The Social Network was an incredibly engaging film that, while mainly revolving around the invention of Facebook"));
		availableMovies.add(movie);
		
		movie = createMovie();
		movie.setName("Blonde");
		movie.setDescription("A fictional biography of Marilyn Monroe mixed with series of real events in her life: childhood years, first marriage to James Dougherty, meeting with the photographer Otto Ose, career with XX Century Fox, relationship with her mother, foster parents, life wasters Charles Chaplin Jr. (Cass) and Edward G. Robinson Jr. (Eddie G), baseball player Joe DiMaggio, playwright Arthur Miller and many other people.");
		movie.setPicturePath("/images/blonde.jpg");
		movie.setRating(createRating(MovieRating.POOR));
		movie.setComments(createComment("Not a bad biopic - though, not being a fanatic, I can't vouch for the accuracy."));
		availableMovies.add(movie);

		movie = createMovie();
		movie.setName("Dead & Nowhere");
		movie.setDescription("Homicide cop Ray Maldonado has found a third dead hooker in as many days. The realization that he's got a serial killer on his hands hits him. Wanting to stop the string of murders, Maldonado turns to the obvious suspect: Street preacher Jimmy Christmas. Searching for redemption from his past life of crime, Christmas wants the hookers he protects off the streets. But his prayers aren't enough. To hide the darkness of his past. Protect his girls. Fend off the anger of mysterious pimp Anthony and Anthony's lawyer Phillip. Jimmy knows. There will be Hell to pay if he cannot stop the disappearances of Anthony's money-making machines. It could mean his life.");
		movie.setPicturePath("/images/deadNowhere.jpg");
		movie.setRating(createRating(MovieRating.BAD));
		movie.setComments(createComment("This movie is terrible. The opening scene has a hooker getting killed."));
		availableMovies.add(movie);
	}

	private static CommentData createComment(String commentText) {
		CommentData comment = new CommentDto();
		comment.setValue(commentText);
		return comment;
	}
	
	private static RatingData createRating(MovieRating movieRating) {
		RatingData rating = new RatingDto();
		rating.setRating(movieRating);
		rating.setRatingValues(MovieRating.values());
		return rating;
	}
	
	private static MovieData createMovie() {
		return new MovieDto();
	}
	
	public static final Set<MovieData> createMockMovies() {
		return availableMovies;
	}
}