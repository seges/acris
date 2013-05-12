package sk.seges.svn.json.reader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.seges.svn.hooks.FieldsDefinition;
import sk.seges.svn.json.data.OverallInfo;
import sk.seges.svn.json.data.RevisionInfo;

public class PayloadJSONReader {
	public PayloadJSONReader() {
		
	}
	
	public OverallInfo readFromJSON(String payload) {
        JSONObject jsonObject;
        
        try {
        	jsonObject = new JSONObject(payload);
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}
        
		OverallInfo overallInfo = new OverallInfo();
		
		try {
			overallInfo.setRepositoryPath(jsonObject.getString(FieldsDefinition.REPOSITORY_PATH));
			overallInfo.setProjectName(jsonObject.getString(FieldsDefinition.PROJECT_NAME));
			
			JSONArray revisions = jsonObject.getJSONArray(FieldsDefinition.REVISIONS);
			
			for (int i = 0; i < revisions.length(); i++) {
				
				RevisionInfo revisionInfo = new RevisionInfo();
				
				JSONObject revision = revisions.getJSONObject(i);
				
				JSONArray added = revision.getJSONArray(FieldsDefinition.ADDED);
				
				List<String> addPath = new ArrayList<String>();
				for (int j = 0; j < added.length(); j++) {
					addPath.add(added.getString(j));
				}
				revisionInfo.setAddedPaths(addPath);
				
				revisionInfo.setAuthor(revision.getString(FieldsDefinition.AUTHOR));
				
				revisionInfo.setUrl(revision.getString(FieldsDefinition.URL));
				revisionInfo.setTimestamp(revision.getLong(FieldsDefinition.TIMESTAMP));
				
				JSONArray modified = revision.getJSONArray(FieldsDefinition.MODIFIED);
				
				List<String> modifiedPath = new ArrayList<String>();
				for (int j = 0; j < modified.length(); j++) {
					modifiedPath.add(modified.getString(j));
				}
				revisionInfo.setModifiedPaths(modifiedPath);
				
				revisionInfo.setMessage(revision.getString(FieldsDefinition.MESSAGE));
				
				JSONArray removed = revision.getJSONArray(FieldsDefinition.REMOVED);
				
				List<String> removedPath = new ArrayList<String>();
				for (int j = 0; j < removed.length(); j++) {
					removedPath.add(removed.getString(j));
				}
				revisionInfo.setRemovedPaths(removedPath);
				
				revisionInfo.setRevisionNr(revision.getLong(FieldsDefinition.REVISION));
				
				overallInfo.getRevisions().add(revisionInfo);
			}
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}

		return overallInfo;
	}
}
