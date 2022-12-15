package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostTest implements Serializable {

    @SerializedName("htsClientId")
    @Expose
    private int htsClientId;

    @SerializedName("personId")
    @Expose
    private int personId;

    @SerializedName("postTestCounselingKnowledgeAssessment")
    @Expose
    private PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment;

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public PostTestCounselingKnowledgeAssessment getPostTestCounselingKnowledgeAssessment() {
        return postTestCounselingKnowledgeAssessment;
    }

    public void setPostTestCounselingKnowledgeAssessment(PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment) {
        this.postTestCounselingKnowledgeAssessment = postTestCounselingKnowledgeAssessment;
    }
}