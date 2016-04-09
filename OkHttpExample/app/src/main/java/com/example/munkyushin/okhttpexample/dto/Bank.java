package com.example.munkyushin.okhttpexample.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by MunkyuShin on 4/9/16.
 */
public class Bank {
    @JsonProperty("id")
    private String mId;

    @JsonProperty("countryname")
    private String mCountryName;

    @JsonProperty("countryshortname")
    private String mCountryShortName;

    @JsonProperty("mjsector_namecode")
    private List<NameCode> mSectorNamecode;

    @JsonProperty("projectdocs")
    private List<Doc> mProjectDocs;

    @Override
    public String toString() {
        return "[" + "id: " + mId + "countryname: " + mCountryName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public String getCountryShortName() {
        return mCountryShortName;
    }

    public void setCountryShortName(String countryShortName) {
        mCountryShortName = countryShortName;
    }

    public List<NameCode> getSectorNamecode() {
        return mSectorNamecode;
    }

    public void setSectorNamecode(List<NameCode> sectorNamecode) {
        mSectorNamecode = sectorNamecode;
    }

    public List<Doc> getProjectDocs() {
        return mProjectDocs;
    }

    public void setProjectDocs(List<Doc> projectDocs) {
        mProjectDocs = projectDocs;
    }
}
