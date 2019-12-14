package id.ac.itn.moca.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TvShowList implements Serializable {

	@SerializedName("page")
	private int page;

	@SerializedName("total_results")
	private int totalResults;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<TvShow> results;

	Throwable error;

	public TvShowList(List<TvShow> results) {
		this.results = results;
		this.error = null;
	}

	public TvShowList(Throwable error) {
		this.error = error;
		this.results = null;
	}

	public Throwable getError(){
		return this.error;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<TvShow> results){
		this.results = results;
	}

	public List<TvShow> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TvShowList{" +
			"page = '" + page + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}