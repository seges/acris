package sk.seges.acris.showcase.mora.shared.action;

import java.util.Set;

import sk.seges.acris.showcase.mora.shared.domain.api.MovieData;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class FetchMovies {

	@Out(1)
	Set<MovieData> movies;

}