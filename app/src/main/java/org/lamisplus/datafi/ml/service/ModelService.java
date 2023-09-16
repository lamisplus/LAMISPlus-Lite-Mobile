package org.lamisplus.datafi.ml.service;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Computable;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.OutputField;
import org.lamisplus.datafi.ml.domain.ModelInputFields;
import org.lamisplus.datafi.ml.domain.ScoringResult;
import org.lamisplus.datafi.ml.exception.ScoringException;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class used to prepare and score models
 */
public class ModelService {

    //private Log log = LogFactory.getLog(this.getClass());

    public ScoringResult score(String facilityName, String encounterDate, ModelInputFields inputFields,
                               boolean debug) {

        try {
            //String fullModelFileName = modelId.concat(".pmml");
            //InputStream stream = ModelService.class.getClassLoader().getResourceAsStream(new File(""));

            // Building a model evaluator from a PMML file
            File file = new File(Environment.getExternalStorageDirectory() + "/" + "sample_rf_pmml_7_19.pmml");
            Evaluator evaluator = new LoadingModelEvaluatorBuilder().load(file).build();
            evaluator.verify();
            ScoringResult scoringResult = new ScoringResult(score(evaluator, inputFields, debug));
            Log.v("Baron", "The score is " + scoringResult);
            return scoringResult;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Baron View", Objects.requireNonNull(e.getMessage()));
//			log.error("Exception during preparation of input parameters or scoring of values for HTS model. "
//			        + e.getMessage());
            throw new ScoringException("Exception during preparation of input parameters or scoring of values", e);
        }
    }

    /**
     * A method that scores a model
     *
     * @param evaluator
     * @param inputFields
     * @return
     */
    private Map<String, Object> score(Evaluator evaluator, ModelInputFields inputFields, boolean debug) {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<FieldName, ?> evaluationResultFromEvaluator = evaluator.evaluate(prepareEvaluationArgs(evaluator, inputFields));

        List<OutputField> outputFields = evaluator.getOutputFields();
        //List<TargetField> targetFields = evaluator.getTargetFields();

        for (OutputField targetField : outputFields) {
            FieldName targetFieldName = targetField.getName();
            Object targetFieldValue = evaluationResultFromEvaluator.get(targetField.getName());

            if (targetFieldValue instanceof Computable) {
                targetFieldValue = ((Computable) targetFieldValue).getResult();
            }

            result.put(targetFieldName.getValue(), targetFieldValue);
        }
        //TODO: this is purely for debugging
        if (debug) {
            Map<String, Object> modelInputs = new HashMap<String, Object>();
            Map<String, Object> combinedResult = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : inputFields.getFields().entrySet()) {
                modelInputs.put(entry.getKey(), entry.getValue());
            }
            combinedResult.put("predictions", result);
            combinedResult.put("ModelInputs", modelInputs);

            return combinedResult;
        } else {
            return result;
        }
    }

    /**
     * Performs variable mapping
     *
     * @param evaluator
     * @param inputFields
     * @return variable-value pair
     */
    private Map<FieldName, FieldValue> prepareEvaluationArgs(Evaluator evaluator, ModelInputFields inputFields) {
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();

        List<InputField> evaluatorFields = evaluator.getActiveFields();

        for (InputField evaluatorField : evaluatorFields) {
            FieldName evaluatorFieldName = evaluatorField.getName();
            String evaluatorFieldNameValue = evaluatorFieldName.getValue();

            Object inputValue = inputFields.getFields().get(evaluatorFieldNameValue);

            if (inputValue == null) {
                //log.warn("Model value not found for the following field: " + evaluatorFieldNameValue);
            }

            arguments.put(evaluatorFieldName, evaluatorField.prepare(inputValue));
        }
        return arguments;
    }
}
