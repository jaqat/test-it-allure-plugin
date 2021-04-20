package com.github.jaqat.testit.api;

import com.github.jaqat.testit.api.client.TestItApiClientBuilder;
import com.github.jaqat.testit.api.model.AutoTest;
import com.github.jaqat.testit.api.model.WorkItem;
import com.github.jaqat.testit.plugin.settings.PluginSettings;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;

public class TestItApiWrapper {

    private final ITestItApiClient testItApiClient;
    private final PluginSettings settings;


    public static TestItApiWrapper
    getTestItApiWrapper(Project project) {
        PluginSettings pluginSettings = PluginSettings.getInstance(project);
        return new TestItApiWrapper(pluginSettings);
    }

    public TestItApiWrapper(PluginSettings settings) {
        this.settings = settings;
        this.testItApiClient = TestItApiClientBuilder.buildTestItApiClient(settings.getServerUrl(), settings.getApiKey());
    }

    public AutoTest createAutoTest(AutoTest autoTest) {
        return testItApiClient.createAutoTest(autoTest);
    }

    public String getProjectUuid(int projectId) {
        return testItApiClient.getProjects()
                .stream()
                .filter(project -> project.getGlobalId().equals(String.valueOf(projectId)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Test-It's project with 'globalId' = " + projectId + " isn't found"))
                .getId();
    }

    public WorkItem getWorkItem(String id) {
        try {
            return testItApiClient.getWorkItem(id);
        } catch (Exception e) {
            Notifications.Bus.notify(
                    new Notification(
                            "TestIt.Plugin",
                            "Get Test-It manual test info",
                            "There is no manual test with id: " + id,
                            NotificationType.WARNING)
            );
            return null;
        }

    }

    public static void main(String[] args) {
        PluginSettings settings = new PluginSettings();
        settings.setServerUrl("https://testit.corp.dev.vtb/");
        settings.setProjectId(5778);
        settings.setApiKey("MWpkVnZ0UUhQM2ZtSmVoa0x6");

        TestItApiWrapper wrapper = new TestItApiWrapper(settings);

        AutoTest autoTest = wrapper.createAutoTest(
                AutoTest.builder()
                        .projectId(String.valueOf(settings.getProjectId()))
                        .name("Check")
                        .build()
        );
        System.out.println(autoTest.getId());
    }

}
