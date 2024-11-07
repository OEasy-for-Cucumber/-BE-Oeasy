package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.application.recipe.XML.RecipeDataXML;
import com.OEzoa.OEasy.application.recipe.XML.RecipeRowXML;
import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import com.OEzoa.OEasy.domain.recipe.OeRecipeManual;
import com.OEzoa.OEasy.domain.recipe.OeRecipeManualRepository;
import com.OEzoa.OEasy.domain.recipe.OeRecipeRepository;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
@TimeTrace
public class RecipeUploadService {

    private final FileUploader fileUploader;

    private final OeRecipeRepository oeRecipeRepository;;
    private final OeRecipeManualRepository oeRecipeManualRepository;

    // 외부 API URL을 정의합니다.
    @Value("${recipe.api.key}")
    private String key;

    /**
     * API에서 데이터를 가져와 파싱하고, S3에 이미지를 업로드한 후 DB에 저장합니다.
     */
    @Transactional
    public RecipeDataXML fetchAndSaveRecipes() {
        String apiUrl = "https://openapi.foodsafetykorea.go.kr/api/"+key+"/COOKRCP01/xml/1/150/RCP_PARTS_DTLS=%EC%98%A4%EC%9D%B4/";
        try {
            // 외부 API로부터 XML 데이터를 요청합니다.
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject(apiUrl, String.class);

            // XML 데이터를 Java 객체로 파싱합니다.
            RecipeDataXML recipeData = parseXmlData(xmlData);

            // 파싱된 레시피 데이터를 하나씩 처리합니다.
            for (RecipeRowXML row : recipeData.getRows()) {

                // 이미지가 있는 경우, S3에 업로드하고 S3 URL을 설정합니다.
                if (row.getImageUrl() != null && !row.getImageUrl().isEmpty()) {
                    URL url = new URL(row.getImageUrl());
                    long contentLength = url.openConnection().getContentLengthLong();

                    try (InputStream imageStream = url.openStream()) {

                        String imageKey = row.getRecipeName() + ".png";
                        String s3ImageUrl = fileUploader.uploadImage(imageKey, row.getImageUrl());
                        OeRecipe oeRecipe = oeRecipeRepository.save(RecipeRowXML.of(row,s3ImageUrl));
                        saveRecipeManual(row, oeRecipe);

                    } catch (IOException e) {
                        System.err.println("Failed to access image URL: " + row.getImageUrl());
                        e.printStackTrace();
                    }
                }
                // 완성된 Recipe 객체를 DB에 저장합니다.
                //recipeRepository.save(recipe);
            }
            return recipeData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("레시피 저장 중 오류 발생", e);
        }

    }

    /**
     * XML 데이터를 RecipeData 객체로 파싱합니다.
     * @param xmlData - XML 문자열
     * @return - RecipeData 객체
     */
    private RecipeDataXML parseXmlData(String xmlData) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RecipeDataXML.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RecipeDataXML) unmarshaller.unmarshal(new StringReader(xmlData));
    }

    private void saveRecipeManual(RecipeRowXML row, OeRecipe oeRecipe) {
        if(!row.getManual01().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(1)
                    .recipe(oeRecipe)
                    .content(row.getManual01())
                    .build());
        }if(!row.getManual02().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(2)
                    .recipe(oeRecipe)
                    .content(row.getManual02())
                    .build());
        }if(!row.getManual03().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(3)
                    .recipe(oeRecipe)
                    .content(row.getManual03())
                    .build());
        }if(!row.getManual04().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(4)
                    .recipe(oeRecipe)
                    .content(row.getManual04())
                    .build());
        }if(!row.getManual05().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(5)
                    .recipe(oeRecipe)
                    .content(row.getManual05())
                    .build());
        }if(!row.getManual06().isEmpty()){
            oeRecipeManualRepository.save(OeRecipeManual.builder()
                    .order(6)
                    .recipe(oeRecipe)
                    .content(row.getManual06())
                    .build());
        }
    }
}