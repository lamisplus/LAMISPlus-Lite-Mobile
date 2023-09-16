package org.lamisplus.datafi.ml.controller.requestDto;


import androidx.annotation.NonNull;

import java.io.Serializable;

//@Data
//@AllArgsConstructor
//@Builder
public class ModelConfigs  implements Serializable {
    @NonNull
    private final String facilityId;

    private final String debug;

    @NonNull
    private final String modelId;
    @NonNull
    private final String encounterDate;

    public ModelConfigs() {
        facilityId = null;
        debug = null;
        modelId = null;
        encounterDate = null;
    }
}
