package com.example.attendance;

public class Leave {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplying_for() {
        return applying_for;
    }

    public void setApplying_for(String applying_for) {
        this.applying_for = applying_for;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String applying_for;
    private String leave_type;
    private String from_date;
    private String to_date;
    private String status;
    private String reason;

    public Leave() {

    }

    public  Leave(String id, String applying_for, String leave_type, String from_date, String to_date, String status, String reason) {
        this.id = id;
        this.applying_for = applying_for;
        this.leave_type = leave_type;
        this.from_date = from_date;
        this.to_date = to_date;
        this.status = status;
        this.reason = reason;
    }

}
