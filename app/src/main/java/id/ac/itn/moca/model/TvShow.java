package id.ac.itn.moca.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TvShow implements Serializable {

	@SerializedName("original_name")
	private String originalName;

	@SerializedName("genre_ids")
	private List<Integer> genreIds;

	@SerializedName("name")
	private String name;

	@SerializedName("popularity")
	private Object popularity;

	@SerializedName("origin_country")
	private List<String> originCountry;

	@SerializedName("vote_count")
	private int voteCount;

	@SerializedName("first_air_date")
	private String firstAirDate;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("id")
	private int id;

	@SerializedName("vote_average")
	private Object voteAverage;

	@SerializedName("overview")
	private String overview;

	@SerializedName("poster_path")
	private String posterPath;

	public void setOriginalName(String originalName){
		this.originalName = originalName;
	}

	public String getOriginalName(){
		return originalName;
	}

	public void setGenreIds(List<Integer> genreIds){
		this.genreIds = genreIds;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPopularity(Object popularity){
		this.popularity = popularity;
	}

	public Object getPopularity(){
		return popularity;
	}

	public void setOriginCountry(List<String> originCountry){
		this.originCountry = originCountry;
	}

	public List<String> getOriginCountry(){
		return originCountry;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}

	public void setFirstAirDate(String firstAirDate){
		this.firstAirDate = firstAirDate;
	}

	public String getFirstAirDate(){
		return firstAirDate;
	}

	public void setBackdropPath(String backdropPath){
		this.backdropPath = backdropPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public void setOriginalLanguage(String originalLanguage){
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setVoteAverage(Object voteAverage){
		this.voteAverage = voteAverage;
	}

	public Object getVoteAverage(){
		return voteAverage;
	}

	public void setOverview(String overview){
		this.overview = overview;
	}

	public String getOverview(){
		return overview;
	}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getPosterPath(){
		return posterPath;
	}

	@Override
 	public String toString(){
		return 
			"TvShow{" +
			"original_name = '" + originalName + '\'' + 
			",genre_ids = '" + genreIds + '\'' + 
			",name = '" + name + '\'' + 
			",popularity = '" + popularity + '\'' + 
			",origin_country = '" + originCountry + '\'' + 
			",vote_count = '" + voteCount + '\'' + 
			",first_air_date = '" + firstAirDate + '\'' + 
			",backdrop_path = '" + backdropPath + '\'' + 
			",original_language = '" + originalLanguage + '\'' + 
			",id = '" + id + '\'' + 
			",vote_average = '" + voteAverage + '\'' + 
			",overview = '" + overview + '\'' + 
			",poster_path = '" + posterPath + '\'' + 
			"}";
		}
}