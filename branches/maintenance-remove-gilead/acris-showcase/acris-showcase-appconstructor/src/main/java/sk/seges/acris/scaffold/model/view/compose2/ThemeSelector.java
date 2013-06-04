package sk.seges.acris.scaffold.model.view.compose2;

import sk.seges.acris.scaffold.model.domain.ThemeModel;
import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.acris.scaffold.service.ReadOperation;

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
	@ServiceMapping(type = ServiceMapping.READ_TYPE, operation = ReadOperation.class)
	@Singleselect
	interface View extends ThemeModel {
		String name();

		interface FilterBy extends CommonFilteredBy {}
	}

	/**
	 * TODO: define somehow, that this detail serves for adding a theme by
	 * uploading it. After upload it should refresh the view.
	 * 
	 * NOTE: might be wondering, where is a name for the theme... it will be
	 * provided by user code as well so the one theme uploaded by user can be
	 * overriden everytime and it will have a special name.
	 */
	interface Detail extends ThemeModel {
		Upload archive();

		interface FilterBy extends CommonFilteredBy {}
		
//		@Provided(clz = XYFilter.class)
//		interface Filter2 {}
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
//	@Provided(class =)
	interface CommonFilteredBy extends ThemeModel {
		String webId();
	}
}
