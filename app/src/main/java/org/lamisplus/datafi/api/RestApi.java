package org.lamisplus.datafi.api;

import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    @POST("authenticate")
    Call<Object> getToken(@Body Object json);

    @POST("patient")
    Call<Object> createPatient(@Body Object personPayload);

    @POST("risk-stratification")
    Call<RiskStratification> createRiskStratification(@Body Object riskStratification);

    @POST("hts")
    Call<Object> createClientIntakeHTS(@Body Object clientIntakePayload);

    @PUT("hts/{id}/pre-test-counseling")
    Call<Object> updatePreTestCounseling(@Path ("id") int patientId, @Body Object preTestCounselingPayload);

    @PUT("hts/{id}/post-test-counseling")
    Call<Object> updatePostTestCounselingKnowledgeAssessment(@Path ("id") int patientId, @Body Object postTestCounselingPayload);

    @PUT("hts/{id}/recency")
    Call<Object> updateRecency(@Path ("id") int patientId, @Body Object recencyPayload);

    @PUT("hts/{id}/request-result")
    Call<Object> updateRequestResult(@Path ("id") int patientId, @Body Object recencyPayload);

    @POST("index-elicitation")
    Call<Object> createIndexElicitation(@Body Object elicitationPayload);

    @GET("account")
    Call<Object> getAccount();

    @POST("biometrics/templates")
    Call<Object> createBiometrics(@Body Object biometricsPayload);

    @GET("patient")
    Call<Object> getPatients(@Query("searchParam") String searchParam,
                                      @Query("pageNo") Integer pageNo, @Query("pageSize") Integer pageSize);

    @GET("hiv/non-biometric-patient/enrollment/{facilityId}")
    Call<Object> getAllPatientWithoutBiomentic(@Path ("facilityId") int facilityId);

}
