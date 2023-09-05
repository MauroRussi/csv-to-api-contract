package co.com.mrsoft.utils.api.raml.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static co.com.mrsoft.utils.api.raml.model.RAMLConstants.*;

@Data
@ToString
public class Document {
    private String displayName;
    private String description;
    private List<Node> properties = new ArrayList<>();

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(DISPLAY_NAME, this.displayName);
        map.put(DESCRIPTION, this.description);
        map.put(TYPE, OBJECT_TYPE);

        Map<String, Object> propertiesMap = new LinkedHashMap<>();

        for(Node node : this.properties) {
            propertiesMap.put(node.getName(), node.toMap());
        }

        map.put(PROPERTIES, propertiesMap);

        return map;
    }
}
