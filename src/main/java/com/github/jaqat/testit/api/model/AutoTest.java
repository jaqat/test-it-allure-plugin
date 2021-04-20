package com.github.jaqat.testit.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoTest {
	private String id;
	private String externalId;
	private String projectId;
	private String name;
	private String namespace;
	private String classname;
}
