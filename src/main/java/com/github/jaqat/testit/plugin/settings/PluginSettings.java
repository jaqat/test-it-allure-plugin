package com.github.jaqat.testit.plugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
		name = "PluginSettings",
		storages = {@Storage("settings.xml")}
)
public class PluginSettings implements PersistentStateComponent<PluginSettings> {

	private String apiKey;
	private String serverUrl;
	private int projectId;

	public String getApiKey() {
		return apiKey;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setApiKey(String apiKey){
	    this.apiKey = apiKey;
    }

	public void setServerUrl(String serverUrl){
	    this.serverUrl = serverUrl;
    }

	public void setProjectId(int projectId){
	    this.projectId = projectId;
    }

	@Nullable
	@Override
	public PluginSettings getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull PluginSettings settings) {
		XmlSerializerUtil.copyBean(settings, this);
	}

	@Nullable
	public static PluginSettings getInstance(Project project) {
		return ServiceManager.getService(project, PluginSettings.class);
	}
}
