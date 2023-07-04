package org.lamisplus.datafi.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.lamisplus.datafi.models.Biometrics;

import java.util.ArrayList;
import java.util.List;

public class BiometricsClass{

    List<BiometricsClass.BiometricsClassFingers> biometricsClassFingers = new ArrayList<>();

    public BiometricsClass(List<BiometricsClass.BiometricsClassFingers> items) {
        this.biometricsClassFingers = items;
    }

    public static class BiometricsClassFingers {
        String template;
        String templateType;
        String hashed;
        Integer imageQuality;

        public BiometricsClassFingers(String template, String templateType, String hashed, Integer imageQuality) {
            this.template = template;
            this.templateType = templateType;
            this.hashed = hashed;
            this.imageQuality = imageQuality;
        }

        /**
         * This constructor is to be used to check if a particular fingerprint already exists on the database
         * @param template
         * @param templateType
         */
        public BiometricsClassFingers(String template, String templateType) {
            this.template = template;
            this.templateType = templateType;
        }

        public void setTemplate(String template){
            this.template = template;
        }

        public String getTemplate(){
            return template;
        }

        public void setTemplateType(String templateType){
            this.templateType = templateType;
        }

        public String getTemplateType(){
            return templateType;
        }

        public String getHashed() {
            return hashed;
        }

        public void setHashed(String hashed) {
            this.hashed = hashed;
        }

        public Integer getImageQuality() {
            return imageQuality;
        }

        public void setImageQuality(Integer imageQuality) {
            this.imageQuality = imageQuality;
        }
    }
}
