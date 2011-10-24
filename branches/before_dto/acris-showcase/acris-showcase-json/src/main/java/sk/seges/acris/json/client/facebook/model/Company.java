package sk.seges.acris.json.client.facebook.model;

import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;

@JsonObject
public class Company {

	@Field
	private String id;

	@Field
	private String name;

	@Field
	private String picture;

	@Field
	private String link;

	@Field
	private String category;

	@Field
	private String username;

	@Field
//	@DateTimePattern("yyyy")
	private String founded;

	@Field(value="company_overview")
	private String overview;

	@Field
	private String mission;

	@Field
	private String products;

	@Field
	private Integer fan_count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFounded() {
		return founded;
	}

	public void setFounded(String founded) {
		this.founded = founded;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getMission() {
		return mission;
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public Integer getFan_count() {
		return fan_count;
	}

	public void setFan_count(Integer fan_count) {
		this.fan_count = fan_count;
	}
}