package com.example.xionico_app.data.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class House implements Serializable {

    private String city;
    private String state;
    private String streetAddress;
    private String highResolutionLink;
}
