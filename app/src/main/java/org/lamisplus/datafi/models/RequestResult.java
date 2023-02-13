package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestResult implements Serializable {


    @SerializedName("personId")
    private int personId;

    @SerializedName("htsClientId")
    private int htsClientId;

    @SerializedName("hivTestResult")
    private String hivTestResult;

    @SerializedName("hivTestResult2")
    private String hivTestResult2;

    @SerializedName("prepAccepted")
    private String prepAccepted;

    @SerializedName("prepOffered")
    private String prepOffered;

    @SerializedName("cd4")
    private CD4 cd4;

    @SerializedName("confirmatoryTest")
    private ConfirmatoryTest confirmatoryTest;

    @SerializedName("confirmatoryTest2")
    private ConfirmatoryTest2 confirmatoryTest2;

    @SerializedName("test1")
    private Test1 test1;

    @SerializedName("test2")
    private Test2 test2;

    @SerializedName("tieBreakerTest")
    private TieBreakerTest tieBreakerTest;

    @SerializedName("tieBreakerTest2")
    private TieBreakerTest2 tieBreakerTest2;

    @SerializedName("syphilisTesting")
    private SyphilisTesting syphilisTesting;

    @SerializedName("hepatitisTesting")
    private HepatitisTesting hepatitisTesting;

    @SerializedName("others")
    private Others others;

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

    public String getHivTestResult() {
        return hivTestResult;
    }

    public void setHivTestResult(String hivTestResult) {
        this.hivTestResult = hivTestResult;
    }

    public String getHivTestResult2() {
        return hivTestResult2;
    }

    public void setHivTestResult2(String hivTestResult2) {
        this.hivTestResult2 = hivTestResult2;
    }

    public String getPrepAccepted() {
        return prepAccepted;
    }

    public void setPrepAccepted(String prepAccepted) {
        this.prepAccepted = prepAccepted;
    }

    public String getPrepOffered() {
        return prepOffered;
    }

    public void setPrepOffered(String prepOffered) {
        this.prepOffered = prepOffered;
    }

    public CD4 getCd4() {
        return cd4;
    }

    public void setCd4(CD4 cd4) {
        this.cd4 = cd4;
    }

    public ConfirmatoryTest getConfirmatoryTest() {
        return confirmatoryTest;
    }

    public void setConfirmatoryTest(ConfirmatoryTest confirmatoryTest) {
        this.confirmatoryTest = confirmatoryTest;
    }

    public ConfirmatoryTest2 getConfirmatoryTest2() {
        return confirmatoryTest2;
    }

    public void setConfirmatoryTest2(ConfirmatoryTest2 confirmatoryTest2) {
        this.confirmatoryTest2 = confirmatoryTest2;
    }

    public Test1 getTest1() {
        return test1;
    }

    public void setTest1(Test1 test1) {
        this.test1 = test1;
    }

    public Test2 getTest2() {
        return test2;
    }

    public void setTest2(Test2 test2) {
        this.test2 = test2;
    }

    public TieBreakerTest getTieBreakerTest() {
        return tieBreakerTest;
    }

    public void setTieBreakerTest(TieBreakerTest tieBreakerTest) {
        this.tieBreakerTest = tieBreakerTest;
    }

    public TieBreakerTest2 getTieBreakerTest2() {
        return tieBreakerTest2;
    }

    public void setTieBreakerTest2(TieBreakerTest2 tieBreakerTest2) {
        this.tieBreakerTest2 = tieBreakerTest2;
    }

    public SyphilisTesting getSyphilisTesting() {
        return syphilisTesting;
    }

    public void setSyphilisTesting(SyphilisTesting syphilisTesting) {
        this.syphilisTesting = syphilisTesting;
    }

    public HepatitisTesting getHepatitisTesting() {
        return hepatitisTesting;
    }

    public void setHepatitisTesting(HepatitisTesting hepatitisTesting) {
        this.hepatitisTesting = hepatitisTesting;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }

    public static class CD4 implements Serializable{

        @SerializedName("cd4Count")
        private String cd4Count;

        @SerializedName("cd4SemiQuantitative")
        private String cd4SemiQuantitative;

        @SerializedName("cd4FlowCyteometry")
        private String cd4FlowCyteometry;

        public String getCd4Count() {
            return cd4Count;
        }

        public void setCd4Count(String cd4Count) {
            this.cd4Count = cd4Count;
        }

        public String getCd4SemiQuantitative() {
            return cd4SemiQuantitative;
        }

        public void setCd4SemiQuantitative(String cd4SemiQuantitative) {
            this.cd4SemiQuantitative = cd4SemiQuantitative;
        }

        public String getCd4FlowCyteometry() {
            return cd4FlowCyteometry;
        }

        public void setCd4FlowCyteometry(String cd4FlowCyteometry) {
            this.cd4FlowCyteometry = cd4FlowCyteometry;
        }
    }

    public static class ConfirmatoryTest implements Serializable{
        @SerializedName("date")
        private String date;

        @SerializedName("result")
        private String result;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static class ConfirmatoryTest2 implements Serializable{

        @SerializedName("date2")
        private String date2;

        @SerializedName("result2")
        private String result2;

        public String getDate2() {
            return date2;
        }

        public void setDate2(String date2) {
            this.date2 = date2;
        }

        public String getResult2() {
            return result2;
        }

        public void setResult2(String result2) {
            this.result2 = result2;
        }
    }

    public static class Test1 implements Serializable{

        @SerializedName("date")
        private String date;

        @SerializedName("result")
        private String result;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static class Test2 implements Serializable{
        @SerializedName("date2")
        private String date2;

        @SerializedName("result2")
        private String result2;

        public String getDate2() {
            return date2;
        }

        public void setDate2(String date2) {
            this.date2 = date2;
        }

        public String getResult2() {
            return result2;
        }

        public void setResult2(String result2) {
            this.result2 = result2;
        }
    }

    public static class TieBreakerTest implements Serializable{

        @SerializedName("date")
        private String date;

        @SerializedName("result")
        private String result;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static class TieBreakerTest2 implements Serializable{

        @SerializedName("date2")
        private String date2;

        @SerializedName("result2")
        private String result2;

        public String getDate2() {
            return date2;
        }

        public void setDate2(String date2) {
            this.date2 = date2;
        }

        public String getResult2() {
            return result2;
        }

        public void setResult2(String result2) {
            this.result2 = result2;
        }
    }

    public static class SyphilisTesting implements Serializable{
        @SerializedName("syphilisTestResult")
        private String syphilisTestResult;

        public String getSyphilisTestResult() {
            return syphilisTestResult;
        }

        public void setSyphilisTestResult(String syphilisTestResult) {
            this.syphilisTestResult = syphilisTestResult;
        }
    }

    public static class HepatitisTesting implements Serializable{

        @SerializedName("hepatitisBTestResult")
        private String hepatitisBTestResult;

        @SerializedName("hepatitisCTestResult")
        private String hepatitisCTestResult;

        public String getHepatitisBTestResult() {
            return hepatitisBTestResult;
        }

        public void setHepatitisBTestResult(String hepatitisBTestResult) {
            this.hepatitisBTestResult = hepatitisBTestResult;
        }

        public String getHepatitisCTestResult() {
            return hepatitisCTestResult;
        }

        public void setHepatitisCTestResult(String hepatitisCTestResult) {
            this.hepatitisCTestResult = hepatitisCTestResult;
        }
    }

    public static class Others implements Serializable{

        @SerializedName("adhocCode")
        private String adhocCode;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        public String getAdhocCode() {
            return adhocCode;
        }

        public void setAdhocCode(String adhocCode) {
            this.adhocCode = adhocCode;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

}
