package co.com.mrsoft.utils.api.raml.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class ArrayNode implements Node {
    private String name;
    private String description;
    private List<Node> items;

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}
