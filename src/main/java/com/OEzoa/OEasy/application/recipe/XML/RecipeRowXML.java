package com.OEzoa.OEasy.application.recipe.XML;

import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Setter;

@Setter
public class RecipeRowXML {
    private String recipeName;
    private String details;
    private String tip;
    private String imageUrl;
    private String manual01;
    private String manual02;
    private String manual03;
    private String manual04;
    private String manual05;
    private String manual06;

    public static OeRecipe of(RecipeRowXML recipeRowXML, String imageUrl){
        return OeRecipe.builder()
                .ingredients(recipeRowXML.details)
                .title(recipeRowXML.recipeName)
                .tip(recipeRowXML.tip)
                .img(imageUrl)
                .build();
    }


    @XmlElement(name = "RCP_NM")  // 요리 이름
    public String getRecipeName() {
        return recipeName;
    }

    @XmlElement(name = "RCP_NA_TIP")  // 재료
    public String getTip() {
        return tip;
    }

    @XmlElement(name = "RCP_PARTS_DTLS")  // 재료
    public String getDetails() {
        return details;
    }

    @XmlElement(name = "MANUAL01")
    public String getManual01() {
        return manual01;
    }
    @XmlElement(name = "MANUAL02")
    public String getManual02() {
        return manual02;
    }
    @XmlElement(name = "MANUAL03")
    public String getManual03() {
        return manual03;
    }
    @XmlElement(name = "MANUAL04")
    public String getManual04() {
        return manual04;
    }
    @XmlElement(name = "MANUAL05")
    public String getManual05() {
        return manual05;
    }
    @XmlElement(name = "MANUAL06")
    public String getManual06() {
        return manual06;
    }


    @XmlElement(name = "ATT_FILE_NO_MK")
    public String getImageUrl() {
        return imageUrl;
    }

}
