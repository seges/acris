package sk.seges.acris.scaffold.model.view.compose2;

import sk.seges.acris.scaffold.model.domain.ThemeModel;
import sk.seges.acris.scaffold.model.view.compose.ViewComposer;

/**
 * Idea: we have a dialog where we can pick a theme for a web site (e.g. from a
 * combo filled by available themes for a customer). There are 3 buttons (add,
 * cancel, switch). Switch triggers main action - switching the theme.
 * 
 * "Add" is responsible for opening hidden panel with possibility to upload new
 * theme. Within the panel there is upload button that will upload a file and
 * after that refreshes the view / combo.
 * 
 * @author ladislav.gazo
 */
@ViewComposer
public class ThemeSelector {
	@Singleselect
	interface View extends ThemeModel {
		String name();

		interface FilterBy extends CommonFilteredBy {}
	}

	/**
	 * TODO: define somehow, that this detail serves for adding a theme by
	 * uploading it. After upload it should refresh the view.
	 */
	interface Detail extends ThemeModel {
		String name();

		Upload archive();

		interface FilterBy extends CommonFilteredBy {}
	}

	/**
	 * Name of the interface is not important but keeps the language fluency. I
	 * put extend of ThemeModel there because maybe we can interconnect the
	 * information about the webId into the query - selecting all themes where
	 * webId is something provided by user code (e.g. value taken from
	 * properties.js)
	 * 
	 * This is an example of shared filter...
	 */
	@Provided
	interface CommonFilteredBy extends ThemeModel {
		String webId();
	}
}
