package com.github.jaqat.testit.api;

import com.github.jaqat.testit.api.model.AutoTest;
import com.github.jaqat.testit.api.model.Project;
import com.github.jaqat.testit.api.model.WorkItem;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ITestItApiClient {

	@POST("/api/v2/autoTests")
	AutoTest createAutoTest(@Body AutoTest autoTest);

	@GET("/api/v2/projects")
	List<Project> getProjects();

	@GET("/api/v2/workItems/{workItemId}")
	WorkItem getWorkItem(@Path("workItemId") String id);

}
