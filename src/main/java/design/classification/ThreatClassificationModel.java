package design.classification;

import org.xml.sax.SAXException;
import settings.threatmodel_configs.STRIDEAttackerConfig;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class ThreatClassificationModel {

    HashMap<String, ThreatCategory> threatCategoryHashMap;

    public ThreatClassificationModel(){
        threatCategoryHashMap = new HashMap<String, ThreatCategory>();
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public HashMap<String, ThreatCategory> getThreatCategories() throws IOException, SAXException, ParserConfigurationException {

        this.createThreatCategories();

        return threatCategoryHashMap;
    }

    /**
     *
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void createThreatCategories() throws ParserConfigurationException, SAXException, IOException {

        STRIDEAttackerConfig strideAttackerConfig = new STRIDEAttackerConfig();

        HashMap<String,String> threatIdsAndNames = strideAttackerConfig.loadThreatCategoryIdsAndNames();

        for (String threatID : threatIdsAndNames.keySet()){

            ThreatCategory threatCategory = this.createThreatCategory(threatID, threatIdsAndNames.get(threatID));
            threatCategoryHashMap.put(threatID, threatCategory);
        }
    }

    /**
     *
     * @param id
     * @param name
     * @return
     */
    private ThreatCategory createThreatCategory(String id, String name){

        ThreatCategory threatCategory = new ThreatCategory();

        threatCategory.setId(id);
        threatCategory.setName(name);

        return threatCategory;
    }
}
