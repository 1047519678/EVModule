package cn.mvc.pojo;

public class Email {
    private int createId;
    private int taskId;
    private String email;
    private String empyName;
    private int EmailPushState;
    private String remark;
    private int rank;
    private String createTime;

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpyName() {
        return empyName;
    }

    public void setEmpyName(String empyName) {
        this.empyName = empyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getEmailPushState() {
        return EmailPushState;
    }

    public void setEmailPushState(int EmailPushState) {
        this.EmailPushState = EmailPushState;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
