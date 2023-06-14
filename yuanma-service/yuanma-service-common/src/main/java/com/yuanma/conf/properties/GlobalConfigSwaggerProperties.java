
package com.yuanma.conf.properties;

import lombok.Data;


@Data
public class GlobalConfigSwaggerProperties {

	private String title = " 接口 RESTful API";

	private String version = "1.0.0";

	private String description = "API 描述";

	private boolean enable = true;

	private Object[] scanner = new Object[]{};
}
