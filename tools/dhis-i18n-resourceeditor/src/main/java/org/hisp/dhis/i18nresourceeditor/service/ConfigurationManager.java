package org.hisp.dhis.i18nresourceeditor.service;

import org.hisp.dhis.i18nresourceeditor.persistence.FileUtils;
import org.hisp.dhis.i18nresourceeditor.exception.I18nResourceException;
import org.hisp.dhis.i18nresourceeditor.exception.I18nInvalidLocaleException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Hashtable;

public class ConfigurationManager {

    private I18nLogging log;
    private Hashtable<String, String> config;
    private boolean usingDefaults = true;

    public ConfigurationManager(I18nLogging log) {
        this.log = log;

        try {
            config = FileUtils.loadResource("config.properties ");

            usingDefaults = false;
        } catch (I18nResourceException e) {
            config = getDefaults();
        }
    }

    public Collection<String> getResourceFilenames() {
        Collection<String> validFilenames = new ArrayList<String>();

        String filenames = config.get("DHIS2ResourceFiles");

        String[] tmp = filenames.split(",");

        for (int i = 0; i < tmp.length; i++) {
            validFilenames.add(tmp[i]);
        }

        return validFilenames;
    }

    public Locale getDefaultLocale() {
        String localeName = config.get("DefaultLocale");

        Locale locale = null;

        try {
            locale = FileUtils.getLocaleFromFilename(localeName);
        } catch (I18nInvalidLocaleException e) {
            try {
                localeName = getDefaults().get("DefaultLocale");
                locale = FileUtils.getLocaleFromFilename(localeName);
            } catch (I18nInvalidLocaleException ex) {
                locale = Locale.UK; // Should be unreachable
            }

            log.outWarning("Using default configuration, unable to determine default locale from string: " +
                    localeName + " now using: " + locale);
        }

        return locale;
    }

    /**
     * Method called if configuration file cant be read
     * @return Hashtable object containing 
     */
    public Hashtable<String, String> getDefaults() {
        Hashtable<String, String> defaultConfig = new Hashtable<String, String>();

        defaultConfig.put("DefaultLocale", "en_GB");
        defaultConfig.put("DHIS2ResourceFiles", "i18n_module,i18n_global");

        return defaultConfig;
    }

    public boolean isUsingDefaults() {
        return usingDefaults;
    }
}
