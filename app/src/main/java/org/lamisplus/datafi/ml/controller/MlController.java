package org.lamisplus.datafi.ml.controller;


import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.ml.controller.requestDto.ModelConfigs;
import org.lamisplus.datafi.ml.domain.ModelInputFields;
import org.lamisplus.datafi.ml.domain.ScoringResult;
import org.lamisplus.datafi.ml.service.ModelService;
import org.lamisplus.datafi.ml.utils.MLUtils;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;


public class MlController {


//    public String welcomeApi(){
//        return "Welcome to LAMISPlus machine learning module";
//    }

    /**
     * API end point for evaluating an ML model
     *
     * @param
     * @return
     */

    public static Object processModel(String payload) {
        Log.v("ML_LAMISPLUS", payload);
        ModelService modelService = new ModelService();
        try {
//            ModelConfigs modelConfigs1 = mlRequestDTO.getModelConfigs();
//            System.out.println("incoming" + request.getReader());
//            requestBody = MLUtils.fetchRequestBody(request.getReader());
//            System.out.println("body " + request.getReader());
            //ObjectNode modelConfigs = MLUtils.getModelConfig(requestBody);
            ObjectMapper objectMapper = new ObjectMapper();
            //String dtoAsString = new Gson().toJson(riskStratification);

            String facilityMflCode = "LBgwDTw2C8u"; // modelConfigs1.getFacilityId();
            String debug = "false"; // modelConfigs1.getDebug();
            boolean isDebugMode = debug.equals("true");

            if (facilityMflCode.equals("")) { // TODO: this should reflect how facilities are identified in LAMISPlus
                facilityMflCode = MLUtils.getDefaultMflCode();
            }
            String modelId = "hts_v1"; //  modelConfigs1.getModelId();
            String encounterDate = "2021-06-05"; // modelConfigs1.getEncounterDate();

            if (StringUtils.isBlank(facilityMflCode) || StringUtils.isBlank(modelId) || StringUtils.isBlank(encounterDate)) {
//                return new ResponseEntity<Object>("The service requires model, date, and facility information",
//                        new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            JSONObject profile = MLUtils.getHTSFacilityProfile("Facility.Datim.ID", "LBgwDTw2C8u", MLUtils.getFacilityCutOffs());

            if (profile == null) {
//                return new ResponseEntity<Object>(
//                        "The facility provided currently doesn't have an HTS cut-off profile. Provide an appropriate facility",
//                        new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            ModelInputFields inputFields = MLUtils.extractHTSCaseFindingVariablesFromRequestBody(payload, "LBgwDTw2C8u",
                    "2023-07-28");

            ScoringResult scoringResult = modelService.score("LBgwDTw2C8u", "2023-07-28", inputFields, false);
            Log.v("Baron", "Score is " + scoringResult);
            LamisCustomHandler.showJson(scoringResult);
            return scoringResult;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Baron", "Error message "+ Arrays.toString(e.getStackTrace()) + e.getMessage());
            return null;
        }
    }
}
