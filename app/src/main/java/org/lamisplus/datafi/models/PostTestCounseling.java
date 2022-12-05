package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostTestCounseling implements Serializable{

    @SerializedName("personId")
    private int personId;

    @SerializedName("htsClientId")
    private int htsClientId;

    @SerializedName("PostTestCounselingKnowledgeAssessment")
    private PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }

    public PostTestCounselingKnowledgeAssessment getPostTestCounselingKnowledgeAssessment() {
        return postTestCounselingKnowledgeAssessment;
    }

    public void setPostTestCounselingKnowledgeAssessment(PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment) {
        this.postTestCounselingKnowledgeAssessment = postTestCounselingKnowledgeAssessment;
    }

    public class PostTestCounselingKnowledgeAssessment implements Serializable{

    }

}

