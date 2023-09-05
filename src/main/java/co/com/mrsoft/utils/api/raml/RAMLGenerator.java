package co.com.mrsoft.utils.api.raml;

import co.com.mrsoft.utils.api.csv.model.Record;
import co.com.mrsoft.utils.api.raml.model.*;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static co.com.mrsoft.utils.api.csv.model.DataTypeEnum.DATE;

public class RAMLGenerator {
    private final static String JSON_PARENT_SEPARATOR = ".";
    private final String filename;
    private final HashMap<String, Node> nodeCache;
    private final List<Node> nodeRoots;

    public RAMLGenerator(String filename) {
        this.filename = filename;
        this.nodeCache = new HashMap<>();
        this.nodeRoots = new ArrayList<>();
    }

    public void generate(String displayName, String description, Collection<Record> csvRecords) throws RAMLGeneratorException {
        Document document = buildDocument(displayName, description, csvRecords);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename))) {
            Yaml yaml = new Yaml();
            Map<String, Object> documentMap = document.toMap();
            yaml.dump(documentMap, writer);
        } catch (IOException e) {
            throw new RAMLGeneratorException("Error generating RAML", e);
        }
    }

    private Document buildDocument(String displayName, String description, Collection<Record> csvRecords) throws RAMLGeneratorException {
        for(Record csvRecord : csvRecords) {
            String jsonName = csvRecord.getJsonName();

            //If the cache has already the object it means is duplicated
            if(this.nodeCache.get(jsonName) != null) {
                throw new RAMLGeneratorException(csvRecord.getRowNumber(), "Object " + jsonName + " is duplicated");
            }

            // Get the parent name
            String[] jsonParentArray = this.splitJsonName(jsonName);
            String nodeName;
            ObjectNode parentNode = null;

            //It has a parent
            if(jsonParentArray.length == 2)
            {
                nodeName = jsonParentArray[1];
                parentNode = this.getOrBuildObjectNode(jsonParentArray[0]);
            }
            else {
                nodeName = jsonParentArray[0];
            }

            //Creates the scalar field
            this.nodeCache.put(jsonName, this.buildScalarNode(csvRecord, parentNode, nodeName));
        }

        Document document = new Document();
        document.setDisplayName(displayName);
        document.setDescription(description);
        document.setProperties(this.nodeRoots);
        return document;
    }

    private Node buildScalarNode(Record csvRecord, ObjectNode parentNode, String nodeName) {
        Node node;

        switch(csvRecord.getDataType()) {
            case BOOLEAN:
                BooleanScalarNode bNode = new BooleanScalarNode();
                bNode.setName(nodeName);
                bNode.setDescription(csvRecord.getDescription());
                bNode.setRequired(csvRecord.isRequired());
                node = bNode;
                break;
            case DECIMAL:
                NumberScalarNode nNode = new NumberScalarNode();
                nNode.setName(nodeName);
                nNode.setDescription(csvRecord.getDescription());
                nNode.setRequired(csvRecord.isRequired());
                nNode.setExample(csvRecord.getExample());
                node = nNode;
                break;
            case DATE:
            case DATETIME:
                DateScalarNode dNode = new DateScalarNode();
                dNode.setName(nodeName);
                dNode.setDescription(csvRecord.getDescription());
                dNode.setRequired(csvRecord.isRequired());
                dNode.setExample(csvRecord.getExample());

                if(csvRecord.getDataType() == DATE) {
                    dNode.setDateType(DateScalarNode.DateTypeEnum.DATE_ONLY);
                }
                else {
                    dNode.setDateType(DateScalarNode.DateTypeEnum.DATETIME);
                }

                node = dNode;
                break;
            default: //String
                StringScalarNode sNode = new StringScalarNode();
                sNode.setName(nodeName);
                sNode.setDescription(csvRecord.getDescription());
                sNode.setRequired(csvRecord.isRequired());
                sNode.setExample(csvRecord.getExample());
                sNode.setMinLength(csvRecord.getMinLength());
                sNode.setMaxLength(csvRecord.getMaxLength());
                sNode.setEnumValues(csvRecord.getEnumValues());
                sNode.setPattern(csvRecord.getRegexPattern());
                node = sNode;
        }

        if(parentNode != null) {
            parentNode.getProperties().add(node);
        }

        return node;
    }

    private ObjectNode getOrBuildObjectNode(String name) {
        //Gets the node
        ObjectNode node = (ObjectNode)nodeCache.get(name);

        //If node doesn't exist needs to be created
        if(node == null) {
            //Gets the parent names
            String[] jsonParentArray = this.splitJsonName(name);

            //It has a parent
            if(jsonParentArray.length == 2) {
                //Gets the parent node (recursion)
                ObjectNode parentNode = this.getOrBuildObjectNode(jsonParentArray[0]);

                //Creates the node and makes the relationship with the parent
                node = new ObjectNode(jsonParentArray[1], new ArrayList<>());
                parentNode.getProperties().add(node);
            }
            //It is a parent
            else {
                //Creates the object
                node = new ObjectNode(jsonParentArray[0], new ArrayList<>());
                this.nodeRoots.add(node);
            }

            //Stores the object in the cache
            this.nodeCache.put(name, node);
            return node;
        }
        else {
            return node;
        }
    }

    private String[] splitJsonName(String jsonName) {
        int index = jsonName.lastIndexOf(JSON_PARENT_SEPARATOR);

        //It has a parent
        if(index > 0) {
            String parent = jsonName.substring(0, index);
            String name = jsonName.substring(index + 1);
            return new String[]{parent, name};
        }
        else {
            return new String[]{jsonName};
        }
    }
}
