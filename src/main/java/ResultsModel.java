public class ResultsModel {

    private String id, kankorID, name, fatherName, grandFatherName, sex, schoolEndDate, province, school, points, result;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKankorID() {
        return kankorID;
    }

    public void setKankorID(String kankorID) {
        this.kankorID = kankorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGrandFatherName() {
        return grandFatherName;
    }

    public void setGrandFatherName(String grandFatherName) {
        this.grandFatherName = grandFatherName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchoolEndDate() {
        return schoolEndDate;
    }

    public void setSchoolEndDate(String schoolEndDate) {
        this.schoolEndDate = schoolEndDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public ResultsModel() {
    }

    public ResultsModel(String id, String kankorID, String name, String fatherName, String grandFatherName, String sex, String schoolEndDate, String province, String school, String points, String result) {
        this.id = id;
        this.kankorID = kankorID;
        this.name = name;
        this.fatherName = fatherName;
        this.grandFatherName = grandFatherName;
        this.sex = sex;
        this.schoolEndDate = schoolEndDate;
        this.province = province;
        this.school = school;
        this.points = points;
        this.result = result;
    }
}
