package com.example.utsav.bhojpuri;

class Statuslist {

    public String getGrievance() {
        return grievance;
    }

    public void setGrievance(String grievance) {
        this.grievance = grievance;
    }

    private String grievance;
     private String grievance_type;
     private String school;
     private String status;


    public String getGrievance_type() {
        return grievance_type;
    }

    public void setGrievance_type(String grievance_type) {
        this.grievance_type = grievance_type;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public int getGrievance_id() {
        return grievance_id;
    }

    public void setGrievance_id(int grievance_id) {
        this.grievance_id = grievance_id;
    }

    private int grievance_id;
     private String created_on;
     private String block;
     private String cluster;

}

class Statuslistt {
    private String reason;
    private String leave_type;
    private String from_date;
    private String status;
    private String to_date;
    private String days;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getFromDate() {
        return from_date;
    }

    public void setFromDate(String from_date) {
        this.from_date = from_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getToDate() {
        return to_date;
    }

    public void setToDate(String to_date) {
        this.to_date = to_date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getLeaveRequest_id() {
        return leave_request_id;
    }

    public void setLeaveRequest_id(int leave_request_id) {
        this.leave_request_id = leave_request_id;
    }

    private int leave_request_id;
    private String created_on;





}