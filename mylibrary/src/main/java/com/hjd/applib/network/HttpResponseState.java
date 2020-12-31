package com.hjd.applib.network;


import com.google.gson.annotations.SerializedName;

/**
 * Created by HJD on 2018/7/23 0023 and 13:39.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 */
public class HttpResponseState {
    /**
     * errorCode : null
     * errorMsg : null
     * data : null
     * draw : 0
     * recordsFiltered : 0
     * recordsTotal : 0
     * success : true
     */

    @SerializedName("errorCode")
    public Object errorCode;
    @SerializedName("errorMsg")
    public Object errorMsg;
    @SerializedName("data")
    public Object data;
    @SerializedName("draw")
    public int draw;
    @SerializedName("recordsFiltered")
    public int recordsFiltered;
    @SerializedName("recordsTotal")
    public int recordsTotal;
    @SerializedName("success")
    public boolean success;
    @SerializedName("statusCode")
    public int statusCode;
    @SerializedName("statusMsg")
    public String  statusMsg;

    public String  getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String  statusMsg) {
        this.statusMsg = statusMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

//    @SerializedName("statusCode")
//    public int statusCode;
//    @SerializedName("statusMsg")
//    public String statusMsg;


}
