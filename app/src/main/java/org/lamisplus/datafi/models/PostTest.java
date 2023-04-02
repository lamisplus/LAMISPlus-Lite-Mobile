package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostTest implements Serializable {

    @SerializedName("htsClientId")
    @Expose
    private Integer htsClientId = null;

    @SerializedName("personId")
    @Expose
    private Integer personId = null;

    @SerializedName("postTestCounselingKnowledgeAssessment")
    @Expose
    private PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment;

    public Integer getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(Integer htsClientId) {
        this.htsClientId = htsClientId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public PostTestCounselingKnowledgeAssessment getPostTestCounselingKnowledgeAssessment() {
        return postTestCounselingKnowledgeAssessment;
    }

    public void setPostTestCounselingKnowledgeAssessment(PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment) {
        this.postTestCounselingKnowledgeAssessment = postTestCounselingKnowledgeAssessment;
    }
}
