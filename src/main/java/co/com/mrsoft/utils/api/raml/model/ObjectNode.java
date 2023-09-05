package co.com.mrsoft.utils.api.raml.model;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static co.com.mrsoft.utils.api.raml.model.RAMLConstants.*;

@Data
@AllArgsConstructor
public class ObjectNode implements Node {
    private String name;
    private List<Node> properties;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(TYPE, OBJECT_TYPE);

        Map<String, Object> propertiesMap = new LinkedHashMap<>();

        for(Node node : this.properties) {
            propertiesMap.put(node.getName(), node.toMap());
        }

        map.put(PROPERTIES, propertiesMap);

        return map;
    }
}
