package com.OEzoa.OEasy.application.recipe.XML;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "COOKRCP01")
public class RecipeDataXML {
    private List<RecipeRowXML> rows;

    @XmlElement(name = "row")  // XML의 <row> 요소를 매핑합니다.
    public List<RecipeRowXML> getRows() {
        return rows;
    }

    public void setRows(List<RecipeRowXML> rows) {
        this.rows = rows;
    }
}
