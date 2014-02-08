package com.hack4good.app;

/**
 * Created by Sasha on 2/8/14.
 */
public class FeatureDetailsList {

    /** This class should not be instantiated. */
    private FeatureDetailsList() {}

    public static final FeatureDetails[] FEATURES = {
            new FeatureDetails(R.string.grocery_map_label,
                    R.string.grocery_map_description,
                    GroceryMapActivity.class),
            new FeatureDetails(R.string.settings_label,
                    R.string.settings_description,
                    SettingsActivity.class)
    };
}
