package co.com.mrsoft.utils.api.raml.model;

import java.util.Map;

public interface Node {
    String getName();
    Map<String, Object> toMap();
}
