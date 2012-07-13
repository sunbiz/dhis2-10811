package org.hispindia.mobile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import org.netbeans.microedition.lcdui.SplashScreen;

public class SCForm1 extends MIDlet implements CommandListener {

    private boolean midletPaused = false;
    private boolean editingLastReport = false;
    private boolean firstRun = false;
    private RecordStore lastMsgStore = null;
    private boolean savedMsg = false;
    private int formID;
    private String msgVersion = "2";
    private ImageItem imgItem = new ImageItem(null, null, ImageItem.LAYOUT_TOP, null);
//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private java.util.Hashtable __previousDisplayables = new java.util.Hashtable();
    private Form InstitutionalDeliveries;
    private TextField textField16;
    private TextField textField14;
    private TextField textField15;
    private TextField textField13;
    private Form Pregnancyoutcome;
    private TextField textField20;
    private TextField textField19;
    private TextField textField18;
    private TextField textField17;
    private Form Newbornsweighted;
    private TextField textField21;
    private TextField textField22;
    private TextField textField23;
    private TextField Newborns_Weighed_female;
    private TextField Newborns_25kg_female;
    private TextField Newborns_Breastfed_female;
    private Form ANC_Services;
    private TextField textField;
    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
    private TextField textField8;
    private TextField textField5;
    private TextField textField4;
    private TextField textField7;
    private TextField textField6;
    private Form Deliveries;
    private TextField textField11;
    private TextField textField12;
    private StringItem stringItem;
    private TextField textField10;
    private TextField textField9;
    private Form FollowingImmunization;
    private TextField textField53;
    private TextField textField52;
    private TextField textField57;
    private TextField textField56;
    private TextField textField55;
    private TextField textField54;
    private TextField textField59;
    private TextField textField58;
    private Form PostNatalCare;
    private TextField textField24;
    private TextField textField25;
    private Form FamilyPlanning;
    private TextField textField27;
    private TextField textField26;
    private TextField textField29;
    private TextField textField28;
    private TextField textField31;
    private TextField textField30;
    private TextField textField33;
    private TextField textField32;
    private TextField textField35;
    private TextField textField34;
    private TextField textField37;
    private TextField textField36;
    private Form ChildImmunization;
    private TextField textField39;
    private TextField textField40;
    private TextField textField38;
    private TextField textField43;
    private TextField textField44;
    private TextField textField41;
    private TextField textField42;
    private TextField textField47;
    private TextField textField48;
    private TextField textField45;
    private TextField textField46;
    private TextField textField51;
    private TextField textField50;
    private TextField textField49;
    private TextField textMeasles2nddose;
    private TextField textFieldHepB0;
    private Form HealthFacilityServices;
    private TextField textField71;
    private TextField textField72;
    private Form LabTests;
    private TextField textField73;
    private TextField textField74;
    private Form Immunizationsessions;
    private TextField textField64;
    private TextField textField63;
    private TextField textAWWpresent;
    private Form sendPage;
    private StringItem sendMsgLabel;
    private Form AEFI;
    private TextField textField60;
    private TextField textField62;
    private TextField textField61;
    private Form ChildhoodDiseasesreported;
    private TextField textField70;
    private TextField textField69;
    private TextField textField68;
    private Form monthPage;
    private ChoiceGroup freqGroup;
    private ChoiceGroup monthChoice;
    private Form VitaminADose;
    private TextField textField67;
    private TextField textField65;
    private TextField textField66;
    private SplashScreen splashScreen;
    private Form loadPage;
    private StringItem questionLabel;
    private ChoiceGroup lastChoice;
    private ImageItem questionImage;
    private Form settingsPage;
    private TextField phone3Num;
    private TextField phone1Num;
    private TextField phone2Num;
    private Form MenstrualHygieneProgramme;
    private TextField textField75;
    private TextField textField77;
    private TextField textField76;
    private TextField textField79;
    private TextField textField78;
    private Form SubCentreCode;
    private TextField subCenterCode;
    private Form Antinatalcare;
    private TextField ANC_ThirdVisit;
    private TextField ANC_TT1;
    private TextField ANC_TT2;
    private TextField ANC_Having;
    private TextField ANC_Hypertension;
    private TextField ANC_Registration;
    private TextField ANC_RegistrationWithin;
    private TextField ANC_Given;
    private Command institutionaldeliveriesCmd;
    private Command institutionaldeliveriesBackCmd;
    private Command pregnancyoutcomeBackCmd;
    private Command pregnancyoutcomeCmd;
    private Command newbornsweightedCmd;
    private Command newbornsweightedBackCmd;
    private Command ANC_ServicesBackCmd;
    private Command ANC_ServicesCmd;
    private Command DeliveriesBackCmd;
    private Command DeliveriesCmd;
    private Command childimmunizationCmd;
    private Command followingimmunizationCmd;
    private Command followingimmunizationBackCmd;
    private Command postnatalcareBackCmd;
    private Command postnatalcareCmd;
    private Command familyplanningBackCmd;
    private Command familyplanningCmd;
    private Command childimmunizationBackCmd;
    private Command healthfacilityservicesCmd;
    private Command healthfacilityservicesBackCmd;
    private Command childhoodCmd;
    private Command childhoodBackCmd;
    private Command saveCmd;
    private Command labtestsCmd;
    private Command labtestsBackCmd;
    private Command immunizationsessionsBackCmd;
    private Command sendBackCmd;
    private Command immunizationsessionsCmd;
    private Command sendCmd;
    private Command aefiCmd;
    private Command aefiBackCmd;
    private Command vitaminadoseCmd;
    private Command monthCmd;
    private Command vitaminadoseBackCmd;
    private Command sendExitCmd;
    private Command backCommand;
    private Command menstrualhygieneprogrammeBackCmd;
    private Command menstrualhygieneprogrammeCmd;
    private Command loadExitCmd;
    private Command loadCmd;
    private Command sendSettingsCmd;
    private Command subcentercodeExitCmd;
    private Command monthBackCmd;
    private Command settingsBackCmd;
    private Command settingsCmd;
    private Command backCommand1;
    private Command subcentercodeCmd;
    private Command sendGPRSCommand;
    private Command monthExitCmd;
    private Command antinatalcareBackCmd;
    private Command antinatalcareCmd;
    private Image nrhmlogo;
    private Image question;
    private Font font;
//</editor-fold>//GEN-END:|fields|0|

    
    /**
     * The SCForm1 constructor.
     */
    public SCForm1() {
        try {
            lastMsgStore = RecordStore.openRecordStore("lastMsgStore", true);
            if (lastMsgStore.getNumRecords() == 0) {
                firstRun = true;
            } else {
                firstRun = false;
            }

            if (firstRun) {
                for (int i = 0; i < 96; i++) {
                    try {
                        lastMsgStore.addRecord("".getBytes(), 0, "".getBytes().length);
                    } catch (RecordStoreException rsex) {
                        rsex.printStackTrace();
                    }
                }
            } else {
                if (lastMsgStore.getRecord(4) != null) {
                    String checkSaved = new String(lastMsgStore.getRecord(4));
                    if (checkSaved.equals("true")) {
                        savedMsg = true;
                    }
                }
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

//<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    /**
     * Switches a display to previous displayable of the current displayable.
     * The
     * <code>display</code> instance is obtain from the
     * <code>getDisplay</code> method.
     */
    private void switchToPreviousDisplayable() {
        Displayable __currentDisplayable = getDisplay().getCurrent();
        if (__currentDisplayable != null) {
            Displayable __nextDisplayable = (Displayable) __previousDisplayables.get(__currentDisplayable);
            if (__nextDisplayable != null) {
                switchDisplayable(null, __nextDisplayable);
            }
        }
    }
//</editor-fold>//GEN-END:|methods|0|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initializes the application. It is called only once when the MIDlet is
     * started. The method is called before the
     * <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
//</editor-fold>//GEN-END:|0-initialize|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getSplashScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
//</editor-fold>//GEN-END:|3-startMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
//</editor-fold>//GEN-END:|4-resumeMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The
     * <code>display</code> instance is taken from
     * <code>getDisplay</code> method. This method is used by all actions in the
     * design for switching displayable.
     *
     * @param alert the Alert which is temporarily set to the display; if
     * <code>null</code>, then
     * <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        Displayable __currentDisplayable = display.getCurrent();
        if (__currentDisplayable != null && nextDisplayable != null) {
            __previousDisplayables.put(nextDisplayable, __currentDisplayable);
        }
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
// write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
//</editor-fold>//GEN-END:|5-switchDisplayable|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a
     * particular displayable.
     *
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == AEFI) {//GEN-BEGIN:|7-commandAction|1|644-preAction
            if (command == aefiBackCmd) {//GEN-END:|7-commandAction|1|644-preAction
                // write pre-action user code here
                switchDisplayable(null, getFollowingImmunization());//GEN-LINE:|7-commandAction|2|644-postAction
                // write post-action user code here
            } else if (command == aefiCmd) {//GEN-LINE:|7-commandAction|3|646-preAction
                // write pre-action user code here
                switchDisplayable(null, getImmunizationsessions());//GEN-LINE:|7-commandAction|4|646-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|520-preAction
        } else if (displayable == ANC_Services) {
            if (command == ANC_ServicesBackCmd) {//GEN-END:|7-commandAction|5|520-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|6|520-postAction
                // write post-action user code here
            } else if (command == ANC_ServicesCmd) {//GEN-LINE:|7-commandAction|7|522-preAction
                // write pre-action user code here
                switchDisplayable(null, getDeliveries());//GEN-LINE:|7-commandAction|8|522-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|493-preAction
        } else if (displayable == Antinatalcare) {
            if (command == antinatalcareBackCmd) {//GEN-END:|7-commandAction|9|493-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|10|493-postAction
                // write post-action user code here
            } else if (command == antinatalcareCmd) {//GEN-LINE:|7-commandAction|11|488-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|12|488-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|608-preAction
        } else if (displayable == ChildImmunization) {
            if (command == childimmunizationBackCmd) {//GEN-END:|7-commandAction|13|608-preAction
                // write pre-action user code here
                switchDisplayable(null, getFamilyPlanning());//GEN-LINE:|7-commandAction|14|608-postAction
                // write post-action user code here
            } else if (command == childimmunizationCmd) {//GEN-LINE:|7-commandAction|15|610-preAction
                // write pre-action user code here
                switchDisplayable(null, getFollowingImmunization());//GEN-LINE:|7-commandAction|16|610-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|673-preAction
        } else if (displayable == ChildhoodDiseasesreported) {
            if (command == childhoodBackCmd) {//GEN-END:|7-commandAction|17|673-preAction
                // write pre-action user code here
                switchDisplayable(null, getVitaminADose());//GEN-LINE:|7-commandAction|18|673-postAction
                // write post-action user code here
            } else if (command == childhoodCmd) {//GEN-LINE:|7-commandAction|19|675-preAction
                // write pre-action user code here
                switchDisplayable(null, getHealthFacilityServices());//GEN-LINE:|7-commandAction|20|675-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|535-preAction
        } else if (displayable == Deliveries) {
            if (command == DeliveriesBackCmd) {//GEN-END:|7-commandAction|21|535-preAction
                // write pre-action user code here
                switchDisplayable(null, getANC_Services());//GEN-LINE:|7-commandAction|22|535-postAction
                // write post-action user code here
            } else if (command == DeliveriesCmd) {//GEN-LINE:|7-commandAction|23|537-preAction
                // write pre-action user code here
                switchDisplayable(null, getInstitutionalDeliveries());//GEN-LINE:|7-commandAction|24|537-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|589-preAction
        } else if (displayable == FamilyPlanning) {
            if (command == familyplanningBackCmd) {//GEN-END:|7-commandAction|25|589-preAction
                // write pre-action user code here
                switchDisplayable(null, getPostNatalCare());//GEN-LINE:|7-commandAction|26|589-postAction
                // write post-action user code here
            } else if (command == familyplanningCmd) {//GEN-LINE:|7-commandAction|27|591-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildImmunization());//GEN-LINE:|7-commandAction|28|591-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|629-preAction
        } else if (displayable == FollowingImmunization) {
            if (command == followingimmunizationBackCmd) {//GEN-END:|7-commandAction|29|629-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildImmunization());//GEN-LINE:|7-commandAction|30|629-postAction
                // write post-action user code here
            } else if (command == followingimmunizationCmd) {//GEN-LINE:|7-commandAction|31|631-preAction
                // write pre-action user code here
                switchDisplayable(null, getAEFI());//GEN-LINE:|7-commandAction|32|631-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|683-preAction
        } else if (displayable == HealthFacilityServices) {
            if (command == healthfacilityservicesBackCmd) {//GEN-END:|7-commandAction|33|683-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhoodDiseasesreported());//GEN-LINE:|7-commandAction|34|683-postAction
                // write post-action user code here
            } else if (command == healthfacilityservicesCmd) {//GEN-LINE:|7-commandAction|35|685-preAction
                // write pre-action user code here
                switchDisplayable(null, getLabTests());//GEN-LINE:|7-commandAction|36|685-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|654-preAction
        } else if (displayable == Immunizationsessions) {
            if (command == immunizationsessionsBackCmd) {//GEN-END:|7-commandAction|37|654-preAction
                // write pre-action user code here
                switchDisplayable(null, getAEFI());//GEN-LINE:|7-commandAction|38|654-postAction
                // write post-action user code here
            } else if (command == immunizationsessionsCmd) {//GEN-LINE:|7-commandAction|39|656-preAction
                // write pre-action user code here
                switchDisplayable(null, getVitaminADose());//GEN-LINE:|7-commandAction|40|656-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|41|548-preAction
        } else if (displayable == InstitutionalDeliveries) {
            if (command == institutionaldeliveriesBackCmd) {//GEN-END:|7-commandAction|41|548-preAction
                // write pre-action user code here
                switchDisplayable(null, getDeliveries());//GEN-LINE:|7-commandAction|42|548-postAction
                // write post-action user code here
            } else if (command == institutionaldeliveriesCmd) {//GEN-LINE:|7-commandAction|43|550-preAction
                // write pre-action user code here
                switchDisplayable(null, getPregnancyoutcome());//GEN-LINE:|7-commandAction|44|550-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|45|690-preAction
        } else if (displayable == LabTests) {
            if (command == labtestsBackCmd) {//GEN-END:|7-commandAction|45|690-preAction
                // write pre-action user code here
                switchDisplayable(null, getHealthFacilityServices());//GEN-LINE:|7-commandAction|46|690-postAction
                // write post-action user code here
            } else if (command == labtestsCmd) {//GEN-LINE:|7-commandAction|47|692-preAction
                // write pre-action user code here
                switchDisplayable(null, getMenstrualHygieneProgramme());//GEN-LINE:|7-commandAction|48|692-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|49|708-preAction
        } else if (displayable == MenstrualHygieneProgramme) {
            if (command == menstrualhygieneprogrammeBackCmd) {//GEN-END:|7-commandAction|49|708-preAction
                // write pre-action user code here
                switchDisplayable(null, getLabTests());//GEN-LINE:|7-commandAction|50|708-postAction
                // write post-action user code here
            } else if (command == menstrualhygieneprogrammeCmd) {//GEN-LINE:|7-commandAction|51|710-preAction
                // write pre-action user code here
                int negativeTextField = getEmptyFields();
                if(negativeTextField > 0){
                    if(negativeTextField > 1){
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data elements are negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,MenstrualHygieneProgramme);
                    }
                    else {
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data element is negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,MenstrualHygieneProgramme);
                    }
                }
                else{
                    switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|52|710-postAction
                }
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|53|570-preAction
        } else if (displayable == Newbornsweighted) {
            if (command == newbornsweightedBackCmd) {//GEN-END:|7-commandAction|53|570-preAction
                // write pre-action user code here
                switchDisplayable(null, getPregnancyoutcome());//GEN-LINE:|7-commandAction|54|570-postAction
                // write post-action user code here
            } else if (command == newbornsweightedCmd) {//GEN-LINE:|7-commandAction|55|572-preAction
                // write pre-action user code here
                switchDisplayable(null, getPostNatalCare());//GEN-LINE:|7-commandAction|56|572-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|57|580-preAction
        } else if (displayable == PostNatalCare) {
            if (command == postnatalcareBackCmd) {//GEN-END:|7-commandAction|57|580-preAction
                // write pre-action user code here
                switchDisplayable(null, getNewbornsweighted());//GEN-LINE:|7-commandAction|58|580-postAction
                // write post-action user code here
            } else if (command == postnatalcareCmd) {//GEN-LINE:|7-commandAction|59|582-preAction
                // write pre-action user code here
                switchDisplayable(null, getFamilyPlanning());//GEN-LINE:|7-commandAction|60|582-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|61|559-preAction
        } else if (displayable == Pregnancyoutcome) {
            if (command == pregnancyoutcomeBackCmd) {//GEN-END:|7-commandAction|61|559-preAction
                // write pre-action user code here
                switchDisplayable(null, getInstitutionalDeliveries());//GEN-LINE:|7-commandAction|62|559-postAction
                // write post-action user code here
            } else if (command == pregnancyoutcomeCmd) {//GEN-LINE:|7-commandAction|63|561-preAction
                // write pre-action user code here
                switchDisplayable(null, getNewbornsweighted());//GEN-LINE:|7-commandAction|64|561-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|65|728-preAction
        } else if (displayable == SubCentreCode) {
            if (command == subcentercodeCmd) {//GEN-END:|7-commandAction|65|728-preAction
                // write pre-action user code here
                int digits = subCenterCode.getString().length();
                String digitsStr = subCenterCode.getString();
                if(digits == 0){
                        Alert myAlert = new Alert("Missing Facility Code","Please enter Facility Code!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,SubCentreCode);
                }
                else if (digits > 0 && digits < 4 ) {
                    Alert myAlert = new Alert("Wrong Facility Code","The Facility Code has 4 digits!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,SubCentreCode);
                }
                else if (digitsStr.length() > 1 && Integer.parseInt(digitsStr) == 0){
                    Alert myAlert = new Alert("Wrong Facility Code","In-correct Facility Code!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,SubCentreCode);
                }
                else if (Integer.parseInt(digitsStr) < 0){
                    Alert myAlert = new Alert("Wrong Facility Code","In-correct Facility Code!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,SubCentreCode);
                }
                else{
                    switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|66|728-postAction
                }// write post-action user code here
            } else if (command == subcentercodeExitCmd) {//GEN-LINE:|7-commandAction|67|730-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|68|730-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|69|663-preAction
        } else if (displayable == VitaminADose) {
            if (command == vitaminadoseBackCmd) {//GEN-END:|7-commandAction|69|663-preAction
                // write pre-action user code here
                switchDisplayable(null, getImmunizationsessions());//GEN-LINE:|7-commandAction|70|663-postAction
                // write post-action user code here
            } else if (command == vitaminadoseCmd) {//GEN-LINE:|7-commandAction|71|665-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhoodDiseasesreported());//GEN-LINE:|7-commandAction|72|665-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|73|226-preAction
        } else if (displayable == loadPage) {
            if (command == loadCmd) {//GEN-END:|7-commandAction|73|226-preAction
                int lastSelected = lastChoice.getSelectedIndex();
                if (lastSelected == 0) {
                    editingLastReport = true;
                } else {
                    editingLastReport = false;
                }
                switchDisplayable(null, getSubCentreCode());//GEN-LINE:|7-commandAction|74|226-postAction
                // write post-action user code here
            } else if (command == loadExitCmd) {//GEN-LINE:|7-commandAction|75|228-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|76|228-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|77|732-preAction
        } else if (displayable == monthPage) {
            if (command == monthBackCmd) {//GEN-END:|7-commandAction|77|732-preAction
                // write pre-action user code here
                switchDisplayable(null, getSubCentreCode());//GEN-LINE:|7-commandAction|78|732-postAction
                // write post-action user code here
            } else if (command == monthCmd) {//GEN-LINE:|7-commandAction|79|180-preAction
                // write pre-action user code here
                switchDisplayable(null, getANC_Services());//GEN-LINE:|7-commandAction|80|180-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|81|703-preAction
        } else if (displayable == sendPage) {
            if (command == saveCmd) {//GEN-END:|7-commandAction|81|703-preAction
                // write pre-action user code here
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());
                saveDataToRMS(monthStr, freqStr);
                Alert myAlert = new Alert("Save success","Your data has been saved!",null,AlertType.INFO);
                myAlert.setTimeout(2000);
                Display.getDisplay(this).setCurrent(myAlert,sendPage);
//GEN-LINE:|7-commandAction|82|703-postAction
                // write post-action user code here
            } else if (command == sendBackCmd) {//GEN-LINE:|7-commandAction|83|171-preAction
                // write pre-action user code here
                switchDisplayable(null, getMenstrualHygieneProgramme());//GEN-LINE:|7-commandAction|84|171-postAction
                // write post-action user code here
            } else if (command == sendCmd) {//GEN-LINE:|7-commandAction|85|169-preAction
                sendMsgLabel.setText("Sending SMS...");
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());

                final String scForm1Data = collectFormData(monthStr, freqStr);
                //<editor-fold defaultstate="collapsed" desc=" Thread to Save Records to RMS ">
                saveDataToRMS(monthStr,freqStr);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" Thread to Send SMS ">
                
                // Split to separating SMS
                String [] testSms = split(scForm1Data, "HP NRHM ");
                // Send separated SMS
                for(int i = 1; i < testSms.length; i++)
                {
                    sendDataViaSMS("HP NRHM " + testSms[i]);
                }
                //</editor-fold>

//GEN-LINE:|7-commandAction|86|169-postAction
                // write post-action user code here
            } else if (command == sendExitCmd) {//GEN-LINE:|7-commandAction|87|207-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|88|207-postAction
                // write post-action user code here
            } else if (command == sendSettingsCmd) {//GEN-LINE:|7-commandAction|89|255-preAction
                // write pre-action user code here
                switchDisplayable(null, getSettingsPage());//GEN-LINE:|7-commandAction|90|255-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|91|246-preAction
        } else if (displayable == settingsPage) {
            if (command == settingsBackCmd) {//GEN-END:|7-commandAction|91|246-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|92|246-postAction
                // write post-action user code here
            } else if (command == settingsCmd) {//GEN-LINE:|7-commandAction|93|243-preAction
                try {
                    String phone1 = phone1Num.getString();
                    String phone2 = phone2Num.getString();
                    String phone3 = phone3Num.getString();

                    lastMsgStore.setRecord(1, phone1.getBytes(), 0, phone1.length());
                    lastMsgStore.setRecord(2, phone2.getBytes(), 0, phone2.length());
                    lastMsgStore.setRecord(3, phone3.getBytes(), 0, phone3.length());
                    //the id number 4 is already in use so i choose number 5

                    if (sendMsgLabel != null) {
                        sendMsgLabel.setText("Settings Saved... Press \"Send  to send report");
                    }
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|94|243-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|95|203-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|95|203-preAction
                if (savedMsg == false) {
                    switchDisplayable(null, getSubCentreCode());
                } else {
                    switchDisplayable(null, getLoadPage());//GEN-LINE:|7-commandAction|96|203-postAction
                }
            }//GEN-BEGIN:|7-commandAction|97|7-postCommandAction
        }//GEN-END:|7-commandAction|97|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|98|
//</editor-fold>//GEN-END:|7-commandAction|98|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendPage ">//GEN-BEGIN:|167-getter|0|167-preInit
    /**
     * Returns an initialized instance of sendPage component.
     *
     * @return the initialized component instance
     */
    public Form getSendPage() {
        if (sendPage == null) {//GEN-END:|167-getter|0|167-preInit
            // write pre-init user code here
            sendPage = new Form(LocalizationSupport.getMessage("sendPageTitle"), new Item[]{getSendMsgLabel()});//GEN-BEGIN:|167-getter|1|167-postInit
            sendPage.addCommand(getSendCmd());
            sendPage.addCommand(getSendBackCmd());
            sendPage.addCommand(getSendExitCmd());
            sendPage.addCommand(getSendSettingsCmd());
            sendPage.addCommand(getSaveCmd());
            sendPage.setCommandListener(this);//GEN-END:|167-getter|1|167-postInit

            if (sendPage.size() > 1) {
                sendPage.delete(sendPage.size() - 1);
            }
            sendPage.append(imgItem);

        }//GEN-BEGIN:|167-getter|2|
        return sendPage;
    }
//</editor-fold>//GEN-END:|167-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendCmd ">//GEN-BEGIN:|168-getter|0|168-preInit
    /**
     * Returns an initialized instance of sendCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSendCmd() {
        if (sendCmd == null) {//GEN-END:|168-getter|0|168-preInit
            // write pre-init user code here
            sendCmd = new Command("Send SMS", Command.OK, 0);//GEN-LINE:|168-getter|1|168-postInit
            // write post-init user code here
        }//GEN-BEGIN:|168-getter|2|
        return sendCmd;
    }
//</editor-fold>//GEN-END:|168-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendBackCmd ">//GEN-BEGIN:|170-getter|0|170-preInit
    /**
     * Returns an initialized instance of sendBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSendBackCmd() {
        if (sendBackCmd == null) {//GEN-END:|170-getter|0|170-preInit
            // write pre-init user code here
            sendBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|170-getter|1|170-postInit
            // write post-init user code here
        }//GEN-BEGIN:|170-getter|2|
        return sendBackCmd;
    }
//</editor-fold>//GEN-END:|170-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthPage ">//GEN-BEGIN:|178-getter|0|178-preInit
    /**
     * Returns an initialized instance of monthPage component.
     *
     * @return the initialized component instance
     */
    public Form getMonthPage() {
        if (monthPage == null) {//GEN-END:|178-getter|0|178-preInit
            // write pre-init user code here
            monthPage = new Form(LocalizationSupport.getMessage("monthPageTitle"), new Item[]{getFreqGroup(), getMonthChoice()});//GEN-BEGIN:|178-getter|1|178-postInit
            monthPage.addCommand(getMonthCmd());
            monthPage.addCommand(getMonthBackCmd());
            monthPage.setCommandListener(this);//GEN-END:|178-getter|1|178-postInit
            // write pre-init user code here
        }//GEN-BEGIN:|178-getter|2|
        return monthPage;
    }
//</editor-fold>//GEN-END:|178-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthChoice ">//GEN-BEGIN:|189-getter|0|189-preInit
    /**
     * Returns an initialized instance of monthChoice component.
     *
     * @return the initialized component instance
     */
    public ChoiceGroup getMonthChoice() {
        if (monthChoice == null) {//GEN-END:|189-getter|0|189-preInit
            // write pre-init user code here
            monthChoice = new ChoiceGroup(LocalizationSupport.getMessage("monthChoiceLabel"), Choice.POPUP);//GEN-BEGIN:|189-getter|1|189-postInit
            monthChoice.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_2);
            monthChoice.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);//GEN-END:|189-getter|1|189-postInit
            // write pre-init user code here
            try {
                // create monthChoice list
                Calendar cal = Calendar.getInstance();
                String[] monthNames = {
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                };

                // append current month in to monthChoice
                monthChoice.append(monthNames[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR), null);

                // Calculate and continue add 12 previous months into monthChoice
                for (int i = 0; i < 13; i++) {               
                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
                    if (cal.get(Calendar.MONTH) == -1) {
                        cal.set(Calendar.MONTH, 11);
                        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    }
                    monthChoice.append(monthNames[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR), null);

                }


//                    for (int i = 0; i < 13; i++) {               
//                        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
//                        if (cal.get(Calendar.MONTH) == -1) {
//                            cal.set(Calendar.MONTH, 11);
//                            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
//                        }
//                        monthChoice.append(monthNames[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR), null);
//
//                    }

                // Index to save default month.
                int index = 0;// current month
                //int index = 1; // last month

                //setdefault selected item in monthChoice
                if (editingLastReport) {
                    if (lastMsgStore.getRecord(10) != null) {

                        for(index = 0; index < monthChoice.size(); index++)
                        {       
                            if(monthChoice.getString(index).equalsIgnoreCase(new String(lastMsgStore.getRecord(10)))){
                                break;
                            }
                        }
                        monthChoice.setSelectedIndex(index, true); // need the last report value
                    }
                } else {
                    monthChoice.setSelectedIndex(index, true); // if not need the last report value
                }
            } catch (RecordStoreException rsex) {
                rsex.printStackTrace();
            }
        }//GEN-BEGIN:|189-getter|2|
        return monthChoice;
    }
//</editor-fold>//GEN-END:|189-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthCmd ">//GEN-BEGIN:|179-getter|0|179-preInit
    /**
     * Returns an initialized instance of monthCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMonthCmd() {
        if (monthCmd == null) {//GEN-END:|179-getter|0|179-preInit
            // write pre-init user code here
            monthCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|179-getter|1|179-postInit
            // write post-init user code here
        }//GEN-BEGIN:|179-getter|2|
        return monthCmd;
    }
//</editor-fold>//GEN-END:|179-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreen ">//GEN-BEGIN:|202-getter|0|202-preInit
    /**
     * Returns an initialized instance of splashScreen component.
     *
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {//GEN-END:|202-getter|0|202-preInit
            // write pre-init user code here
            splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|202-getter|1|202-postInit
            splashScreen.setTitle("");
            splashScreen.setCommandListener(this);
            splashScreen.setFullScreenMode(true);
            splashScreen.setImage(getNrhmlogo());
            splashScreen.setText(LocalizationSupport.getMessage("brandingLabel"));
            splashScreen.setTimeout(3000);//GEN-END:|202-getter|1|202-postInit
            // write post-init user code here
        }//GEN-BEGIN:|202-getter|2|
        return splashScreen;
    }
//</editor-fold>//GEN-END:|202-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: nrhmlogo ">//GEN-BEGIN:|205-getter|0|205-preInit
    /**
     * Returns an initialized instance of nrhmlogo component.
     *
     * @return the initialized component instance
     */
    public Image getNrhmlogo() {
        if (nrhmlogo == null) {//GEN-END:|205-getter|0|205-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|205-getter|1|205-@java.io.IOException
                nrhmlogo = Image.createImage("/org/hispindia/mobile/images/nrhm-logo.png");
            } catch (java.io.IOException e) {//GEN-END:|205-getter|1|205-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|205-getter|2|205-postInit
// write post-init user code here
        }//GEN-BEGIN:|205-getter|3|
        return nrhmlogo;
    }
//</editor-fold>//GEN-END:|205-getter|3|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendExitCmd ">//GEN-BEGIN:|206-getter|0|206-preInit
    /**
     * Returns an initialized instance of sendExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSendExitCmd() {
        if (sendExitCmd == null) {//GEN-END:|206-getter|0|206-preInit
            // write pre-init user code here
            sendExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|206-getter|1|206-postInit
            // write post-init user code here
        }//GEN-BEGIN:|206-getter|2|
        return sendExitCmd;
    }
//</editor-fold>//GEN-END:|206-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendMsgLabel ">//GEN-BEGIN:|210-getter|0|210-preInit
    /**
     * Returns an initialized instance of sendMsgLabel component.
     *
     * @return the initialized component instance
     */
    public StringItem getSendMsgLabel() {
        if (sendMsgLabel == null) {//GEN-END:|210-getter|0|210-preInit
            // write pre-init user code here
            sendMsgLabel = new StringItem("Info:", "Data Collection Complete.\nPress \"Send SMS\" to send information to server", Item.PLAIN);//GEN-BEGIN:|210-getter|1|210-postInit
            sendMsgLabel.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | ImageItem.LAYOUT_NEWLINE_BEFORE | ImageItem.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_SHRINK | Item.LAYOUT_VSHRINK | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND | Item.LAYOUT_2);
            sendMsgLabel.setFont(getFont());//GEN-END:|210-getter|1|210-postInit
            // write post-init user code here
        }//GEN-BEGIN:|210-getter|2|
        return sendMsgLabel;
    }
//</editor-fold>//GEN-END:|210-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: font ">//GEN-BEGIN:|211-getter|0|211-preInit
    /**
     * Returns an initialized instance of font component.
     *
     * @return the initialized component instance
     */
    public Font getFont() {
        if (font == null) {//GEN-END:|211-getter|0|211-preInit
            // write pre-init user code here
            font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD | Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);//GEN-LINE:|211-getter|1|211-postInit
            // write post-init user code here
        }//GEN-BEGIN:|211-getter|2|
        return font;
    }
//</editor-fold>//GEN-END:|211-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: question ">//GEN-BEGIN:|213-getter|0|213-preInit
    /**
     * Returns an initialized instance of question component.
     *
     * @return the initialized component instance
     */
    public Image getQuestion() {
        if (question == null) {//GEN-END:|213-getter|0|213-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|213-getter|1|213-@java.io.IOException
                question = Image.createImage("/org/hispindia/mobile/images/question.png");
            } catch (java.io.IOException e) {//GEN-END:|213-getter|1|213-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|213-getter|2|213-postInit
// write post-init user code here
        }//GEN-BEGIN:|213-getter|3|
        return question;
    }
//</editor-fold>//GEN-END:|213-getter|3|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadPage ">//GEN-BEGIN:|219-getter|0|219-preInit
    /**
     * Returns an initialized instance of loadPage component.
     *
     * @return the initialized component instance
     */
    public Form getLoadPage() {
        if (loadPage == null) {//GEN-END:|219-getter|0|219-preInit
            // write pre-init user code here
            loadPage = new Form(LocalizationSupport.getMessage("loadPageTitle"), new Item[]{getQuestionImage(), getQuestionLabel(), getLastChoice()});//GEN-BEGIN:|219-getter|1|219-postInit
            loadPage.addCommand(getLoadCmd());
            loadPage.addCommand(getLoadExitCmd());
            loadPage.setCommandListener(this);//GEN-END:|219-getter|1|219-postInit
            // write post-init user code here
        }//GEN-BEGIN:|219-getter|2|
        return loadPage;
    }
//</editor-fold>//GEN-END:|219-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: questionLabel ">//GEN-BEGIN:|221-getter|0|221-preInit
    /**
     * Returns an initialized instance of questionLabel component.
     *
     * @return the initialized component instance
     */
    public StringItem getQuestionLabel() {
        if (questionLabel == null) {//GEN-END:|221-getter|0|221-preInit
            // write pre-init user code here
            questionLabel = new StringItem("", LocalizationSupport.getMessage("questionLabel"));//GEN-LINE:|221-getter|1|221-postInit
            // write post-init user code here
        }//GEN-BEGIN:|221-getter|2|
        return questionLabel;
    }
//</editor-fold>//GEN-END:|221-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: lastChoice ">//GEN-BEGIN:|222-getter|0|222-preInit
    /**
     * Returns an initialized instance of lastChoice component.
     *
     * @return the initialized component instance
     */
    public ChoiceGroup getLastChoice() {
        if (lastChoice == null) {//GEN-END:|222-getter|0|222-preInit
            // write pre-init user code here
            lastChoice = new ChoiceGroup(LocalizationSupport.getMessage("lastChoiceLabel"), Choice.EXCLUSIVE);//GEN-BEGIN:|222-getter|1|222-postInit
            lastChoice.append(LocalizationSupport.getMessage("lastChoiceYes"), null);
            lastChoice.append(LocalizationSupport.getMessage("lastChoiceNo"), null);
            lastChoice.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            lastChoice.setSelectedFlags(new boolean[]{false, false});//GEN-END:|222-getter|1|222-postInit
            // write post-init user code here
        }//GEN-BEGIN:|222-getter|2|
        return lastChoice;
    }
//</editor-fold>//GEN-END:|222-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadCmd ">//GEN-BEGIN:|225-getter|0|225-preInit
    /**
     * Returns an initialized instance of loadCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLoadCmd() {
        if (loadCmd == null) {//GEN-END:|225-getter|0|225-preInit
            // write pre-init user code here
            loadCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|225-getter|1|225-postInit
            // write post-init user code here
        }//GEN-BEGIN:|225-getter|2|
        return loadCmd;
    }
//</editor-fold>//GEN-END:|225-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadExitCmd ">//GEN-BEGIN:|227-getter|0|227-preInit
    /**
     * Returns an initialized instance of loadExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLoadExitCmd() {
        if (loadExitCmd == null) {//GEN-END:|227-getter|0|227-preInit
            // write pre-init user code here
            loadExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|227-getter|1|227-postInit
            // write post-init user code here
        }//GEN-BEGIN:|227-getter|2|
        return loadExitCmd;
    }
//</editor-fold>//GEN-END:|227-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: settingsPage ">//GEN-BEGIN:|233-getter|0|233-preInit
    /**
     * Returns an initialized instance of settingsPage component.
     *
     * @return the initialized component instance
     */
    public Form getSettingsPage() {
        if (settingsPage == null) {//GEN-END:|233-getter|0|233-preInit
            // write pre-init user code here
            settingsPage = new Form(LocalizationSupport.getMessage("settingsPageTitle"), new Item[]{getPhone1Num(), getPhone2Num(), getPhone3Num()});//GEN-BEGIN:|233-getter|1|233-postInit
            settingsPage.addCommand(getSettingsCmd());
            settingsPage.addCommand(getSettingsBackCmd());
            settingsPage.setCommandListener(this);//GEN-END:|233-getter|1|233-postInit
            // write post-init user code here
        }//GEN-BEGIN:|233-getter|2|
        return settingsPage;
    }
//</editor-fold>//GEN-END:|233-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: questionImage ">//GEN-BEGIN:|241-getter|0|241-preInit
    /**
     * Returns an initialized instance of questionImage component.
     *
     * @return the initialized component instance
     */
    public ImageItem getQuestionImage() {
        if (questionImage == null) {//GEN-END:|241-getter|0|241-preInit
            // write pre-init user code here
            questionImage = new ImageItem("", getQuestion(), ImageItem.LAYOUT_CENTER | Item.LAYOUT_2, "");//GEN-LINE:|241-getter|1|241-postInit
            // write post-init user code here
        }//GEN-BEGIN:|241-getter|2|
        return questionImage;
    }
//</editor-fold>//GEN-END:|241-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: settingsCmd ">//GEN-BEGIN:|242-getter|0|242-preInit
    /**
     * Returns an initialized instance of settingsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSettingsCmd() {
        if (settingsCmd == null) {//GEN-END:|242-getter|0|242-preInit
            // write pre-init user code here
            settingsCmd = new Command("Save", Command.OK, 0);//GEN-LINE:|242-getter|1|242-postInit
            // write post-init user code here
        }//GEN-BEGIN:|242-getter|2|
        return settingsCmd;
    }
//</editor-fold>//GEN-END:|242-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: settingsBackCmd ">//GEN-BEGIN:|245-getter|0|245-preInit
    /**
     * Returns an initialized instance of settingsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSettingsBackCmd() {
        if (settingsBackCmd == null) {//GEN-END:|245-getter|0|245-preInit
            // write pre-init user code here
            settingsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|245-getter|1|245-postInit
            // write post-init user code here
        }//GEN-BEGIN:|245-getter|2|
        return settingsBackCmd;
    }
//</editor-fold>//GEN-END:|245-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: phone1Num ">//GEN-BEGIN:|248-getter|0|248-preInit
    /**
     * Returns an initialized instance of phone1Num component.
     *
     * @return the initialized component instance
     */
    public TextField getPhone1Num() {
        if (phone1Num == null) {//GEN-END:|248-getter|0|248-preInit
            String str = "";
            try {
                if (lastMsgStore.getRecord(1) != null) {
                    str = new String(lastMsgStore.getRecord(1));
                }
            } catch (RecordStoreException rsex) {
                rsex.printStackTrace();
            }
            phone1Num = new TextField(LocalizationSupport.getMessage("phone1Num"), str, 13, TextField.PHONENUMBER);//GEN-LINE:|248-getter|1|248-postInit
            // write post-init user code here
        }//GEN-BEGIN:|248-getter|2|
        return phone1Num;
    }
//</editor-fold>//GEN-END:|248-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: phone2Num ">//GEN-BEGIN:|249-getter|0|249-preInit
    /**
     * Returns an initialized instance of phone2Num component.
     *
     * @return the initialized component instance
     */
    public TextField getPhone2Num() {
        if (phone2Num == null) {//GEN-END:|249-getter|0|249-preInit
            String str = "";
            try {
                if (lastMsgStore.getRecord(2) != null) {
                    str = new String(lastMsgStore.getRecord(2));
                }
            } catch (RecordStoreException rsex) {
                rsex.printStackTrace();
            }
            phone2Num = new TextField(LocalizationSupport.getMessage("phone2Num"), str, 13, TextField.PHONENUMBER);//GEN-LINE:|249-getter|1|249-postInit
            // write post-init user code here
        }//GEN-BEGIN:|249-getter|2|
        return phone2Num;
    }
//</editor-fold>//GEN-END:|249-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: phone3Num ">//GEN-BEGIN:|250-getter|0|250-preInit
    /**
     * Returns an initialized instance of phone3Num component.
     *
     * @return the initialized component instance
     */
    public TextField getPhone3Num() {
        if (phone3Num == null) {//GEN-END:|250-getter|0|250-preInit
            String str = "";
            try {
                if (lastMsgStore.getRecord(3) != null) {
                    str = new String(lastMsgStore.getRecord(3));
                }
            } catch (RecordStoreException rsex) {
                rsex.printStackTrace();
            }
            phone3Num = new TextField(LocalizationSupport.getMessage("phone3Num"), str, 13, TextField.PHONENUMBER);//GEN-LINE:|250-getter|1|250-postInit
            // write post-init user code here
        }//GEN-BEGIN:|250-getter|2|
        return phone3Num;
    }
//</editor-fold>//GEN-END:|250-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendSettingsCmd ">//GEN-BEGIN:|254-getter|0|254-preInit
    /**
     * Returns an initialized instance of sendSettingsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSendSettingsCmd() {
        if (sendSettingsCmd == null) {//GEN-END:|254-getter|0|254-preInit
            // write pre-init user code here
            sendSettingsCmd = new Command("Settings", Command.OK, 0);//GEN-LINE:|254-getter|1|254-postInit
            // write post-init user code here
        }//GEN-BEGIN:|254-getter|2|
        return sendSettingsCmd;
    }
//</editor-fold>//GEN-END:|254-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthExitCmd ">//GEN-BEGIN:|458-getter|0|458-preInit
    /**
     * Returns an initialized instance of monthExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMonthExitCmd() {
        if (monthExitCmd == null) {//GEN-END:|458-getter|0|458-preInit
            // write pre-init user code here
            monthExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|458-getter|1|458-postInit
            // write post-init user code here
        }//GEN-BEGIN:|458-getter|2|
        return monthExitCmd;
    }
//</editor-fold>//GEN-END:|458-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendGPRSCommand ">//GEN-BEGIN:|463-getter|0|463-preInit
    /**
     * Returns an initialized instance of sendGPRSCommand component.
     *
     * @return the initialized component instance
     */
    public Command getSendGPRSCommand() {
        if (sendGPRSCommand == null) {//GEN-END:|463-getter|0|463-preInit
            // write pre-init user code here
            sendGPRSCommand = new Command("Send", Command.OK, 0);//GEN-LINE:|463-getter|1|463-postInit
            // write post-init user code here
        }//GEN-BEGIN:|463-getter|2|
        return sendGPRSCommand;
    }
//</editor-fold>//GEN-END:|463-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: antinatalcareCmd ">//GEN-BEGIN:|487-getter|0|487-preInit
    /**
     * Returns an initialized instance of antinatalcareCmd component.
     *
     * @return the initialized component instance
     */
    public Command getAntinatalcareCmd() {
        if (antinatalcareCmd == null) {//GEN-END:|487-getter|0|487-preInit
            // write pre-init user code here
            antinatalcareCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|487-getter|1|487-postInit
            // write post-init user code here
        }//GEN-BEGIN:|487-getter|2|
        return antinatalcareCmd;
    }
//</editor-fold>//GEN-END:|487-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: antinatalcareBackCmd ">//GEN-BEGIN:|492-getter|0|492-preInit
    /**
     * Returns an initialized instance of antinatalcareBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getAntinatalcareBackCmd() {
        if (antinatalcareBackCmd == null) {//GEN-END:|492-getter|0|492-preInit
            // write pre-init user code here
            antinatalcareBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|492-getter|1|492-postInit
            // write post-init user code here
        }//GEN-BEGIN:|492-getter|2|
        return antinatalcareBackCmd;
    }
//</editor-fold>//GEN-END:|492-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_Given ">//GEN-BEGIN:|507-getter|0|507-preInit
    /**
     * Returns an initialized instance of ANC_Given component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_Given() {
        if (ANC_Given == null) {//GEN-END:|507-getter|0|507-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(11) != null) {
                        str = new String(lastMsgStore.getRecord(11));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_Given = new TextField(LocalizationSupport.getMessage("ANC_Given"), str, 4, TextField.NUMERIC);//GEN-LINE:|507-getter|1|507-postInit
            // write post-init user code here
        }//GEN-BEGIN:|507-getter|2|
        return ANC_Given;
    }
//</editor-fold>//GEN-END:|507-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_Having ">//GEN-BEGIN:|508-getter|0|508-preInit
    /**
     * Returns an initialized instance of ANC_Having component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_Having() {
        if (ANC_Having == null) {//GEN-END:|508-getter|0|508-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(12) != null) {
                        str = new String(lastMsgStore.getRecord(12));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_Having = new TextField(LocalizationSupport.getMessage("ANC_Having"), str, 4, TextField.NUMERIC);//GEN-LINE:|508-getter|1|508-postInit
            // write post-init user code here
        }//GEN-BEGIN:|508-getter|2|
        return ANC_Having;
    }
//</editor-fold>//GEN-END:|508-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_Hypertension ">//GEN-BEGIN:|509-getter|0|509-preInit
    /**
     * Returns an initialized instance of ANC_Hypertension component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_Hypertension() {
        if (ANC_Hypertension == null) {//GEN-END:|509-getter|0|509-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(13) != null) {
                        str = new String(lastMsgStore.getRecord(13));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_Hypertension = new TextField(LocalizationSupport.getMessage("ANC_Hypertension"), str, 4, TextField.NUMERIC);//GEN-LINE:|509-getter|1|509-postInit
            // write post-init user code here
        }//GEN-BEGIN:|509-getter|2|
        return ANC_Hypertension;
    }
//</editor-fold>//GEN-END:|509-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_Registration ">//GEN-BEGIN:|510-getter|0|510-preInit
    /**
     * Returns an initialized instance of ANC_Registration component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_Registration() {
        if (ANC_Registration == null) {//GEN-END:|510-getter|0|510-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(14) != null) {
                        str = new String(lastMsgStore.getRecord(14));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_Registration = new TextField(LocalizationSupport.getMessage("ANC_Registration"), str, 4, TextField.NUMERIC);//GEN-LINE:|510-getter|1|510-postInit
            // write post-init user code here
        }//GEN-BEGIN:|510-getter|2|
        return ANC_Registration;
    }
//</editor-fold>//GEN-END:|510-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_RegistrationWithin ">//GEN-BEGIN:|511-getter|0|511-preInit
    /**
     * Returns an initialized instance of ANC_RegistrationWithin component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_RegistrationWithin() {
        if (ANC_RegistrationWithin == null) {//GEN-END:|511-getter|0|511-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(15) != null) {
                        str = new String(lastMsgStore.getRecord(15));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_RegistrationWithin = new TextField(LocalizationSupport.getMessage("ANC_RegistrationWithin"), str, 4, TextField.NUMERIC);//GEN-LINE:|511-getter|1|511-postInit
            // write post-init user code here
        }//GEN-BEGIN:|511-getter|2|
        return ANC_RegistrationWithin;
    }
//</editor-fold>//GEN-END:|511-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_ThirdVisit ">//GEN-BEGIN:|512-getter|0|512-preInit
    /**
     * Returns an initialized instance of ANC_ThirdVisit component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_ThirdVisit() {
        if (ANC_ThirdVisit == null) {//GEN-END:|512-getter|0|512-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(16) != null) {
                        str = new String(lastMsgStore.getRecord(16));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_ThirdVisit = new TextField(LocalizationSupport.getMessage("ANC_ThirdVisit"), str, 4, TextField.NUMERIC);//GEN-LINE:|512-getter|1|512-postInit
            // write post-init user code here
        }//GEN-BEGIN:|512-getter|2|
        return ANC_ThirdVisit;
    }
//</editor-fold>//GEN-END:|512-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_TT1 ">//GEN-BEGIN:|513-getter|0|513-preInit
    /**
     * Returns an initialized instance of ANC_TT1 component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_TT1() {
        if (ANC_TT1 == null) {//GEN-END:|513-getter|0|513-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(17) != null) {
                        str = new String(lastMsgStore.getRecord(17));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_TT1 = new TextField(LocalizationSupport.getMessage("ANC_TT1"), str, 4, TextField.NUMERIC);//GEN-LINE:|513-getter|1|513-postInit
            // write post-init user code here
        }//GEN-BEGIN:|513-getter|2|
        return ANC_TT1;
    }
//</editor-fold>//GEN-END:|513-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_TT2 ">//GEN-BEGIN:|514-getter|0|514-preInit
    /**
     * Returns an initialized instance of ANC_TT2 component.
     *
     * @return the initialized component instance
     */
    public TextField getANC_TT2() {
        if (ANC_TT2 == null) {//GEN-END:|514-getter|0|514-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    if (lastMsgStore.getRecord(18) != null) {
                        str = new String(lastMsgStore.getRecord(18));
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            ANC_TT2 = new TextField(LocalizationSupport.getMessage("ANC_TT2"), str, 4, TextField.NUMERIC);//GEN-LINE:|514-getter|1|514-postInit
            // write post-init user code here
        }//GEN-BEGIN:|514-getter|2|
        return ANC_TT2;
    }
//</editor-fold>//GEN-END:|514-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: freqGroup ">//GEN-BEGIN:|517-getter|0|517-preInit
    /**
     * Returns an initialized instance of freqGroup component.
     *
     * @return the initialized component instance
     */
    public ChoiceGroup getFreqGroup() {
        if (freqGroup == null) {//GEN-END:|517-getter|0|517-preInit
            // write pre-init user code here
            freqGroup = new ChoiceGroup(LocalizationSupport.getMessage("freq"), Choice.POPUP);//GEN-BEGIN:|517-getter|1|517-postInit
            freqGroup.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_2);//GEN-END:|517-getter|1|517-postInit
            // write post-init user code here
            try {
                   String[] freqNames = {
                        "Monthly"
                    };

                    for (int i = 0; i < freqNames.length; i++) {
                        freqGroup.append(freqNames[i], null);
                    }

                    if (editingLastReport) {
                        if (lastMsgStore.getRecord(7) != null) {
                            int index = 0;
                            for(index = 0; index < freqGroup.size(); index++)
                            {       
                                if(freqGroup.getString(index).equalsIgnoreCase(new String(lastMsgStore.getRecord(7)))){
                                    break;
                                }
                            }
                            freqGroup.setSelectedIndex(index, true); // need the last report value
                        }
                    } else {
                        freqGroup.setSelectedIndex(0, true); // If not need the value of the last report
                    }
                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
        }//GEN-BEGIN:|517-getter|2|
        return freqGroup;
    }
//</editor-fold>//GEN-END:|517-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Antinatalcare ">//GEN-BEGIN:|473-getter|0|473-preInit
    /**
     * Returns an initialized instance of Antinatalcare component.
     *
     * @return the initialized component instance
     */
    public Form getAntinatalcare() {
        if (Antinatalcare == null) {//GEN-END:|473-getter|0|473-preInit
            // write pre-init user code here
            Antinatalcare = new Form("DHIS Form", new Item[]{getANC_Given(), getANC_Having(), getANC_Hypertension(), getANC_Registration(), getANC_RegistrationWithin(), getANC_ThirdVisit(), getANC_TT1(), getANC_TT2()});//GEN-BEGIN:|473-getter|1|473-postInit
            Antinatalcare.addCommand(getAntinatalcareCmd());
            Antinatalcare.addCommand(getAntinatalcareBackCmd());
            Antinatalcare.setCommandListener(this);//GEN-END:|473-getter|1|473-postInit
            // write post-init user code here
        }//GEN-BEGIN:|473-getter|2|
        return Antinatalcare;
    }
//</editor-fold>//GEN-END:|473-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_Services ">//GEN-BEGIN:|518-getter|0|518-preInit
    /**
     * Returns an initialized instance of ANC_Services component.
     *
     * @return the initialized component instance
     */
    public Form getANC_Services() {
        if (ANC_Services == null) {//GEN-END:|518-getter|0|518-preInit
            // write pre-init user code here
            ANC_Services = new Form("ANC Services", new Item[]{getTextField(), getTextField1(), getTextField2(), getTextField3(), getTextField4(), getTextField5(), getTextField6(), getTextField7(), getTextField8()});//GEN-BEGIN:|518-getter|1|518-postInit
            ANC_Services.addCommand(getANC_ServicesBackCmd());
            ANC_Services.addCommand(getANC_ServicesCmd());
            ANC_Services.setCommandListener(this);//GEN-END:|518-getter|1|518-postInit
            // write post-init user code here
        }//GEN-BEGIN:|518-getter|2|
        return ANC_Services;
    }
//</editor-fold>//GEN-END:|518-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_ServicesBackCmd ">//GEN-BEGIN:|519-getter|0|519-preInit
    /**
     * Returns an initialized instance of ANC_ServicesBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getANC_ServicesBackCmd() {
        if (ANC_ServicesBackCmd == null) {//GEN-END:|519-getter|0|519-preInit
            // write pre-init user code here
            ANC_ServicesBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|519-getter|1|519-postInit
            // write post-init user code here
        }//GEN-BEGIN:|519-getter|2|
        return ANC_ServicesBackCmd;
    }
//</editor-fold>//GEN-END:|519-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANC_ServicesCmd ">//GEN-BEGIN:|521-getter|0|521-preInit
    /**
     * Returns an initialized instance of ANC_ServicesCmd component.
     *
     * @return the initialized component instance
     */
    public Command getANC_ServicesCmd() {
        if (ANC_ServicesCmd == null) {//GEN-END:|521-getter|0|521-preInit
            // write pre-init user code here
            ANC_ServicesCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|521-getter|1|521-postInit
            // write post-init user code here
        }//GEN-BEGIN:|521-getter|2|
        return ANC_ServicesCmd;
    }
//</editor-fold>//GEN-END:|521-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|524-getter|0|524-preInit
    /**
     * Returns an initialized instance of textField component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|524-getter|0|524-preInit
            // write pre-init user code here
            String str = getRecordValue(11);
            textField = new TextField(LocalizationSupport.getMessage("Total_no."), str, 4, TextField.NUMERIC);//GEN-LINE:|524-getter|1|524-postInit
            // write post-init user code here
        }//GEN-BEGIN:|524-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|524-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|525-getter|0|525-preInit
    /**
     * Returns an initialized instance of textField1 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|525-getter|0|525-preInit
            // write pre-init user code here
            String str = getRecordValue(85); //  because my mistake on missing register the record for this textField
            textField1 = new TextField(LocalizationSupport.getMessage("Of_Which"), str, 4, TextField.NUMERIC);//GEN-LINE:|525-getter|1|525-postInit
            // write post-init user code here
        }//GEN-BEGIN:|525-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|525-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|526-getter|0|526-preInit
    /**
     * Returns an initialized instance of textField2 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|526-getter|0|526-preInit
            // write pre-init user code here
            String str = getRecordValue(12);
            textField2 = new TextField(LocalizationSupport.getMessage("New_woman"), str, 4, TextField.NUMERIC);//GEN-LINE:|526-getter|1|526-postInit
            // write post-init user code here
        }//GEN-BEGIN:|526-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|526-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField3 ">//GEN-BEGIN:|527-getter|0|527-preInit
    /**
     * Returns an initialized instance of textField3 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField3() {
        if (textField3 == null) {//GEN-END:|527-getter|0|527-preInit
            // write pre-init user code here
            String str = getRecordValue(13);
            textField3 = new TextField(LocalizationSupport.getMessage("Women_received"), str, 4, TextField.NUMERIC);//GEN-LINE:|527-getter|1|527-postInit
            // write post-init user code here
        }//GEN-BEGIN:|527-getter|2|
        return textField3;
    }
//</editor-fold>//GEN-END:|527-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField4 ">//GEN-BEGIN:|528-getter|0|528-preInit
    /**
     * Returns an initialized instance of textField4 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField4() {
        if (textField4 == null) {//GEN-END:|528-getter|0|528-preInit
            // write pre-init user code here
            String str = getRecordValue(14);
            textField4 = new TextField(LocalizationSupport.getMessage("TT1"), str, 4, TextField.NUMERIC);//GEN-LINE:|528-getter|1|528-postInit
            // write post-init user code here
        }//GEN-BEGIN:|528-getter|2|
        return textField4;
    }
//</editor-fold>//GEN-END:|528-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField5 ">//GEN-BEGIN:|529-getter|0|529-preInit
    /**
     * Returns an initialized instance of textField5 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField5() {
        if (textField5 == null) {//GEN-END:|529-getter|0|529-preInit
            // write pre-init user code here
            String str = getRecordValue(15);
            textField5 = new TextField(LocalizationSupport.getMessage("TT2"), str, 4, TextField.NUMERIC);//GEN-LINE:|529-getter|1|529-postInit
            // write post-init user code here
        }//GEN-BEGIN:|529-getter|2|
        return textField5;
    }
//</editor-fold>//GEN-END:|529-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField6 ">//GEN-BEGIN:|530-getter|0|530-preInit
    /**
     * Returns an initialized instance of textField6 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField6() {
        if (textField6 == null) {//GEN-END:|530-getter|0|530-preInit
            // write pre-init user code here
            String str = getRecordValue(16);
            textField6 = new TextField(LocalizationSupport.getMessage("No.of_Women"), str, 4, TextField.NUMERIC);//GEN-LINE:|530-getter|1|530-postInit
            // write post-init user code here
        }//GEN-BEGIN:|530-getter|2|
        return textField6;
    }
//</editor-fold>//GEN-END:|530-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField7 ">//GEN-BEGIN:|531-getter|0|531-preInit
    /**
     * Returns an initialized instance of textField7 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField7() {
        if (textField7 == null) {//GEN-END:|531-getter|0|531-preInit
            // write pre-init user code here
            String str = getRecordValue(17);
            textField7 = new TextField(LocalizationSupport.getMessage("New_Hypertension"), str, 4, TextField.NUMERIC);//GEN-LINE:|531-getter|1|531-postInit
            // write post-init user code here
        }//GEN-BEGIN:|531-getter|2|
        return textField7;
    }
//</editor-fold>//GEN-END:|531-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField8 ">//GEN-BEGIN:|532-getter|0|532-preInit
    /**
     * Returns an initialized instance of textField8 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField8() {
        if (textField8 == null) {//GEN-END:|532-getter|0|532-preInit
            // write pre-init user code here
            String str = getRecordValue(18);
            textField8 = new TextField(LocalizationSupport.getMessage("Hb_level"), str, 4, TextField.NUMERIC);//GEN-LINE:|532-getter|1|532-postInit
            // write post-init user code here
        }//GEN-BEGIN:|532-getter|2|
        return textField8;
    }
//</editor-fold>//GEN-END:|532-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Deliveries ">//GEN-BEGIN:|533-getter|0|533-preInit
    /**
     * Returns an initialized instance of Deliveries component.
     *
     * @return the initialized component instance
     */
    public Form getDeliveries() {
        if (Deliveries == null) {//GEN-END:|533-getter|0|533-preInit
            // write pre-init user code here
            Deliveries = new Form("Deliveries", new Item[]{getStringItem(), getTextField9(), getTextField10(), getTextField11(), getTextField12()});//GEN-BEGIN:|533-getter|1|533-postInit
            Deliveries.addCommand(getDeliveriesBackCmd());
            Deliveries.addCommand(getDeliveriesCmd());
            Deliveries.setCommandListener(this);//GEN-END:|533-getter|1|533-postInit
            // write post-init user code here
        }//GEN-BEGIN:|533-getter|2|
        return Deliveries;
    }
//</editor-fold>//GEN-END:|533-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem ">//GEN-BEGIN:|541-getter|0|541-preInit
    /**
     * Returns an initialized instance of stringItem component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {//GEN-END:|541-getter|0|541-preInit
            // write pre-init user code here
            stringItem = new StringItem(LocalizationSupport.getMessage("Home_Deliveries"), null, Item.PLAIN);//GEN-BEGIN:|541-getter|1|541-postInit
            stringItem.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_2);//GEN-END:|541-getter|1|541-postInit
            // write post-init user code here
        }//GEN-BEGIN:|541-getter|2|
        return stringItem;
    }
//</editor-fold>//GEN-END:|541-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: DeliveriesBackCmd ">//GEN-BEGIN:|534-getter|0|534-preInit
    /**
     * Returns an initialized instance of DeliveriesBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getDeliveriesBackCmd() {
        if (DeliveriesBackCmd == null) {//GEN-END:|534-getter|0|534-preInit
            // write pre-init user code here
            DeliveriesBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|534-getter|1|534-postInit
            // write post-init user code here
        }//GEN-BEGIN:|534-getter|2|
        return DeliveriesBackCmd;
    }
//</editor-fold>//GEN-END:|534-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: DeliveriesCmd ">//GEN-BEGIN:|536-getter|0|536-preInit
    /**
     * Returns an initialized instance of DeliveriesCmd component.
     *
     * @return the initialized component instance
     */
    public Command getDeliveriesCmd() {
        if (DeliveriesCmd == null) {//GEN-END:|536-getter|0|536-preInit
            // write pre-init user code here
            DeliveriesCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|536-getter|1|536-postInit
            // write post-init user code here
        }//GEN-BEGIN:|536-getter|2|
        return DeliveriesCmd;
    }
//</editor-fold>//GEN-END:|536-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField9 ">//GEN-BEGIN:|542-getter|0|542-preInit
    /**
     * Returns an initialized instance of textField9 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField9() {
        if (textField9 == null) {//GEN-END:|542-getter|0|542-preInit
            // write pre-init user code here
            String str = getRecordValue(19);
            textField9 = new TextField(LocalizationSupport.getMessage("SBA"), str, 4, TextField.NUMERIC);//GEN-LINE:|542-getter|1|542-postInit
            // write post-init user code here
        }//GEN-BEGIN:|542-getter|2|
        return textField9;
    }
//</editor-fold>//GEN-END:|542-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField10 ">//GEN-BEGIN:|543-getter|0|543-preInit
    /**
     * Returns an initialized instance of textField10 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField10() {
        if (textField10 == null) {//GEN-END:|543-getter|0|543-preInit
            // write pre-init user code here
            String str = getRecordValue(20);
            textField10 = new TextField(LocalizationSupport.getMessage("Non_SBA"), str, 4, TextField.NUMERIC);//GEN-LINE:|543-getter|1|543-postInit
            // write post-init user code here
        }//GEN-BEGIN:|543-getter|2|
        return textField10;
    }
//</editor-fold>//GEN-END:|543-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField11 ">//GEN-BEGIN:|544-getter|0|544-preInit
    /**
     * Returns an initialized instance of textField11 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField11() {
        if (textField11 == null) {//GEN-END:|544-getter|0|544-preInit
            // write pre-init user code here
            String str = getRecordValue(21);
            textField11 = new TextField(LocalizationSupport.getMessage("Newborns"), str, 4, TextField.NUMERIC);//GEN-LINE:|544-getter|1|544-postInit
            // write post-init user code here
        }//GEN-BEGIN:|544-getter|2|
        return textField11;
    }
//</editor-fold>//GEN-END:|544-getter|2|
                    
    
//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField12 ">//GEN-BEGIN:|545-getter|0|545-preInit
    /**
     * Returns an initialized instance of textField12 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField12() {
        if (textField12 == null) {//GEN-END:|545-getter|0|545-preInit
            // write pre-init user code here
            String str = getRecordValue(22);
            textField12 = new TextField(LocalizationSupport.getMessage("Mothers_Paid_JSY"), str, 4, TextField.NUMERIC);//GEN-LINE:|545-getter|1|545-postInit
            // write post-init user code here
        }//GEN-BEGIN:|545-getter|2|
        return textField12;
    }
//</editor-fold>//GEN-END:|545-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InstitutionalDeliveries ">//GEN-BEGIN:|546-getter|0|546-preInit
    /**
     * Returns an initialized instance of InstitutionalDeliveries component.
     *
     * @return the initialized component instance
     */
    public Form getInstitutionalDeliveries() {
        if (InstitutionalDeliveries == null) {//GEN-END:|546-getter|0|546-preInit
            // write pre-init user code here
            InstitutionalDeliveries = new Form("Institutional Deliveries", new Item[]{getTextField13(), getTextField14(), getTextField15(), getTextField16()});//GEN-BEGIN:|546-getter|1|546-postInit
            InstitutionalDeliveries.addCommand(getInstitutionaldeliveriesBackCmd());
            InstitutionalDeliveries.addCommand(getInstitutionaldeliveriesCmd());
            InstitutionalDeliveries.setCommandListener(this);//GEN-END:|546-getter|1|546-postInit
            // write post-init user code here
        }//GEN-BEGIN:|546-getter|2|
        return InstitutionalDeliveries;
    }
//</editor-fold>//GEN-END:|546-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField13 ">//GEN-BEGIN:|553-getter|0|553-preInit
    /**
     * Returns an initialized instance of textField13 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField13() {
        if (textField13 == null) {//GEN-END:|553-getter|0|553-preInit
            // write pre-init user code here
            String str = getRecordValue(23);
            textField13 = new TextField(LocalizationSupport.getMessage("Deliveries_at"), str, 4, TextField.NUMERIC);//GEN-LINE:|553-getter|1|553-postInit
            // write post-init user code here
        }//GEN-BEGIN:|553-getter|2|
        return textField13;
    }
//</editor-fold>//GEN-END:|553-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField14 ">//GEN-BEGIN:|554-getter|0|554-preInit
    /**
     * Returns an initialized instance of textField14 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField14() {
        if (textField14 == null) {//GEN-END:|554-getter|0|554-preInit
            // write pre-init user code here
            String str = getRecordValue(24);
            textField14 = new TextField(LocalizationSupport.getMessage("Discharged_under"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|554-getter|1|554-postInit
//GEN-END:|554-getter|1|554-postInit
            // write post-init user code here
        }//GEN-BEGIN:|554-getter|2|
        return textField14;
    }
//</editor-fold>//GEN-END:|554-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField15 ">//GEN-BEGIN:|555-getter|0|555-preInit
    /**
     * Returns an initialized instance of textField15 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField15() {
        if (textField15 == null) {//GEN-END:|555-getter|0|555-preInit
            // write pre-init user code here
            String str = getRecordValue(25);
            textField15 = new TextField(LocalizationSupport.getMessage("JSY_Mothers"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|555-getter|1|555-postInit
//GEN-END:|555-getter|1|555-postInit
            // write post-init user code here
        }//GEN-BEGIN:|555-getter|2|
        return textField15;
    }
//</editor-fold>//GEN-END:|555-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField16 ">//GEN-BEGIN:|556-getter|0|556-preInit
    /**
     * Returns an initialized instance of textField16 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField16() {
        if (textField16 == null) {//GEN-END:|556-getter|0|556-preInit
            // write pre-init user code here
            String str = getRecordValue(26);
            textField16 = new TextField(LocalizationSupport.getMessage("JSY_AWW"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|556-getter|1|556-postInit
//GEN-END:|556-getter|1|556-postInit
            // write post-init user code here
        }//GEN-BEGIN:|556-getter|2|
        return textField16;
    }
//</editor-fold>//GEN-END:|556-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Pregnancyoutcome ">//GEN-BEGIN:|557-getter|0|557-preInit
    /**
     * Returns an initialized instance of Pregnancyoutcome component.
     *
     * @return the initialized component instance
     */
    public Form getPregnancyoutcome() {
        if (Pregnancyoutcome == null) {//GEN-END:|557-getter|0|557-preInit
            // write pre-init user code here
            Pregnancyoutcome = new Form("Pregnancy Outcome", new Item[]{getTextField17(), getTextField18(), getTextField19(), getTextField20()});//GEN-BEGIN:|557-getter|1|557-postInit
            Pregnancyoutcome.addCommand(getPregnancyoutcomeBackCmd());
            Pregnancyoutcome.addCommand(getPregnancyoutcomeCmd());
            Pregnancyoutcome.setCommandListener(this);//GEN-END:|557-getter|1|557-postInit
            // write post-init user code here
        }//GEN-BEGIN:|557-getter|2|
        return Pregnancyoutcome;
    }
//</editor-fold>//GEN-END:|557-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: institutionaldeliveriesBackCmd ">//GEN-BEGIN:|547-getter|0|547-preInit
    /**
     * Returns an initialized instance of institutionaldeliveriesBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInstitutionaldeliveriesBackCmd() {
        if (institutionaldeliveriesBackCmd == null) {//GEN-END:|547-getter|0|547-preInit
            // write pre-init user code here
            institutionaldeliveriesBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|547-getter|1|547-postInit
            // write post-init user code here
        }//GEN-BEGIN:|547-getter|2|
        return institutionaldeliveriesBackCmd;
    }
//</editor-fold>//GEN-END:|547-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: institutionaldeliveriesCmd ">//GEN-BEGIN:|549-getter|0|549-preInit
    /**
     * Returns an initialized instance of institutionaldeliveriesCmd component.
     *
     * @return the initialized component instance
     */
    public Command getInstitutionaldeliveriesCmd() {
        if (institutionaldeliveriesCmd == null) {//GEN-END:|549-getter|0|549-preInit
            // write pre-init user code here
            institutionaldeliveriesCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|549-getter|1|549-postInit
            // write post-init user code here
        }//GEN-BEGIN:|549-getter|2|
        return institutionaldeliveriesCmd;
    }
//</editor-fold>//GEN-END:|549-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: pregnancyoutcomeBackCmd ">//GEN-BEGIN:|558-getter|0|558-preInit
    /**
     * Returns an initialized instance of pregnancyoutcomeBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPregnancyoutcomeBackCmd() {
        if (pregnancyoutcomeBackCmd == null) {//GEN-END:|558-getter|0|558-preInit
            // write pre-init user code here
            pregnancyoutcomeBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|558-getter|1|558-postInit
            // write post-init user code here
        }//GEN-BEGIN:|558-getter|2|
        return pregnancyoutcomeBackCmd;
    }
//</editor-fold>//GEN-END:|558-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: pregnancyoutcomeCmd ">//GEN-BEGIN:|560-getter|0|560-preInit
    /**
     * Returns an initialized instance of pregnancyoutcomeCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPregnancyoutcomeCmd() {
        if (pregnancyoutcomeCmd == null) {//GEN-END:|560-getter|0|560-preInit
            // write pre-init user code here
            pregnancyoutcomeCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|560-getter|1|560-postInit
            // write post-init user code here
        }//GEN-BEGIN:|560-getter|2|
        return pregnancyoutcomeCmd;
    }
//</editor-fold>//GEN-END:|560-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField17 ">//GEN-BEGIN:|564-getter|0|564-preInit
    /**
     * Returns an initialized instance of textField17 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField17() {
        if (textField17 == null) {//GEN-END:|564-getter|0|564-preInit
            // write pre-init user code here
            String str = getRecordValue(27);
            textField17 = new TextField(LocalizationSupport.getMessage("Live_BirthM"), str, 4, TextField.NUMERIC);//GEN-LINE:|564-getter|1|564-postInit
            // write post-init user code here
        }//GEN-BEGIN:|564-getter|2|
        return textField17;
    }
//</editor-fold>//GEN-END:|564-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField18 ">//GEN-BEGIN:|565-getter|0|565-preInit
    /**
     * Returns an initialized instance of textField18 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField18() {
        if (textField18 == null) {//GEN-END:|565-getter|0|565-preInit
            // write pre-init user code here
            String str = getRecordValue(28);
            textField18 = new TextField(LocalizationSupport.getMessage("Live_BirthF"), str, 4, TextField.NUMERIC);//GEN-LINE:|565-getter|1|565-postInit
            // write post-init user code here
        }//GEN-BEGIN:|565-getter|2|
        return textField18;
    }
//</editor-fold>//GEN-END:|565-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField19 ">//GEN-BEGIN:|566-getter|0|566-preInit
    /**
     * Returns an initialized instance of textField19 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField19() {
        if (textField19 == null) {//GEN-END:|566-getter|0|566-preInit
            // write pre-init user code here
            String str = getRecordValue(29);
            textField19 = new TextField(LocalizationSupport.getMessage("Still_Birth"), str, 4, TextField.NUMERIC);//GEN-LINE:|566-getter|1|566-postInit
            // write post-init user code here
        }//GEN-BEGIN:|566-getter|2|
        return textField19;
    }
//</editor-fold>//GEN-END:|566-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField20 ">//GEN-BEGIN:|567-getter|0|567-preInit
    /**
     * Returns an initialized instance of textField20 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField20() {
        if (textField20 == null) {//GEN-END:|567-getter|0|567-preInit
            // write pre-init user code here
            String str = getRecordValue(30);
            textField20 = new TextField(LocalizationSupport.getMessage("Abortion"), str, 4, TextField.NUMERIC);//GEN-LINE:|567-getter|1|567-postInit
            // write post-init user code here
        }//GEN-BEGIN:|567-getter|2|
        return textField20;
    }
//</editor-fold>//GEN-END:|567-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Newbornsweighted ">//GEN-BEGIN:|568-getter|0|568-preInit
    /**
     * Returns an initialized instance of Newbornsweighted component.
     *
     * @return the initialized component instance
     */
    public Form getNewbornsweighted() {
        if (Newbornsweighted == null) {//GEN-END:|568-getter|0|568-preInit
            // write pre-init user code here
            Newbornsweighted = new Form("Newborns Weighted", new Item[]{getTextField21(), getNewborns_Weighed_female(), getTextField22(), getNewborns_25kg_female(), getTextField23(), getNewborns_Breastfed_female()});//GEN-BEGIN:|568-getter|1|568-postInit
            Newbornsweighted.addCommand(getNewbornsweightedBackCmd());
            Newbornsweighted.addCommand(getNewbornsweightedCmd());
            Newbornsweighted.setCommandListener(this);//GEN-END:|568-getter|1|568-postInit
            // write post-init user code here
        }//GEN-BEGIN:|568-getter|2|
        return Newbornsweighted;
    }
//</editor-fold>//GEN-END:|568-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: newbornsweightedBackCmd ">//GEN-BEGIN:|569-getter|0|569-preInit
    /**
     * Returns an initialized instance of newbornsweightedBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getNewbornsweightedBackCmd() {
        if (newbornsweightedBackCmd == null) {//GEN-END:|569-getter|0|569-preInit
            // write pre-init user code here
            newbornsweightedBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|569-getter|1|569-postInit
            // write post-init user code here
        }//GEN-BEGIN:|569-getter|2|
        return newbornsweightedBackCmd;
    }
//</editor-fold>//GEN-END:|569-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: newbornsweightedCmd ">//GEN-BEGIN:|571-getter|0|571-preInit
    /**
     * Returns an initialized instance of newbornsweightedCmd component.
     *
     * @return the initialized component instance
     */
    public Command getNewbornsweightedCmd() {
        if (newbornsweightedCmd == null) {//GEN-END:|571-getter|0|571-preInit
            // write pre-init user code here
            newbornsweightedCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|571-getter|1|571-postInit
            // write post-init user code here
        }//GEN-BEGIN:|571-getter|2|
        return newbornsweightedCmd;
    }
//</editor-fold>//GEN-END:|571-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField21 ">//GEN-BEGIN:|575-getter|0|575-preInit
    /**
     * Returns an initialized instance of textField21 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField21() {
        if (textField21 == null) {//GEN-END:|575-getter|0|575-preInit
            // write pre-init user code here
            String str = getRecordValue(31);
            textField21 = new TextField(LocalizationSupport.getMessage("Newborns_Weighed_male"), str, 4, TextField.NUMERIC);//GEN-LINE:|575-getter|1|575-postInit
            // write post-init user code here
        }//GEN-BEGIN:|575-getter|2|
        return textField21;
    }
//</editor-fold>//GEN-END:|575-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField22 ">//GEN-BEGIN:|576-getter|0|576-preInit
    /**
     * Returns an initialized instance of textField22 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField22() {
        if (textField22 == null) {//GEN-END:|576-getter|0|576-preInit
            // write pre-init user code here
            String str = getRecordValue(32);
            textField22 = new TextField(LocalizationSupport.getMessage("Newborns_2.5kg_male"), str, 4, TextField.NUMERIC);//GEN-LINE:|576-getter|1|576-postInit
            // write post-init user code here
        }//GEN-BEGIN:|576-getter|2|
        return textField22;
    }
//</editor-fold>//GEN-END:|576-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField23 ">//GEN-BEGIN:|577-getter|0|577-preInit
    /**
     * Returns an initialized instance of textField23 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField23() {
        if (textField23 == null) {//GEN-END:|577-getter|0|577-preInit
            // write pre-init user code here
            String str = getRecordValue(33);
            textField23 = new TextField(LocalizationSupport.getMessage("Newborns_Breastfed_male"), str, 4, TextField.NUMERIC);//GEN-LINE:|577-getter|1|577-postInit
            // write post-init user code here
        }//GEN-BEGIN:|577-getter|2|
        return textField23;
    }
//</editor-fold>//GEN-END:|577-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PostNatalCare ">//GEN-BEGIN:|578-getter|0|578-preInit
    /**
     * Returns an initialized instance of PostNatalCare component.
     *
     * @return the initialized component instance
     */
    public Form getPostNatalCare() {
        if (PostNatalCare == null) {//GEN-END:|578-getter|0|578-preInit
            // write pre-init user code here
            PostNatalCare = new Form("Post -Natal Care", new Item[]{getTextField24(), getTextField25()});//GEN-BEGIN:|578-getter|1|578-postInit
            PostNatalCare.addCommand(getPostnatalcareBackCmd());
            PostNatalCare.addCommand(getPostnatalcareCmd());
            PostNatalCare.setCommandListener(this);//GEN-END:|578-getter|1|578-postInit
            // write post-init user code here
        }//GEN-BEGIN:|578-getter|2|
        return PostNatalCare;
    }
//</editor-fold>//GEN-END:|578-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: postnatalcareBackCmd ">//GEN-BEGIN:|579-getter|0|579-preInit
    /**
     * Returns an initialized instance of postnatalcareBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPostnatalcareBackCmd() {
        if (postnatalcareBackCmd == null) {//GEN-END:|579-getter|0|579-preInit
            // write pre-init user code here
            postnatalcareBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|579-getter|1|579-postInit
            // write post-init user code here
        }//GEN-BEGIN:|579-getter|2|
        return postnatalcareBackCmd;
    }
//</editor-fold>//GEN-END:|579-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: postnatalcareCmd ">//GEN-BEGIN:|581-getter|0|581-preInit
    /**
     * Returns an initialized instance of postnatalcareCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPostnatalcareCmd() {
        if (postnatalcareCmd == null) {//GEN-END:|581-getter|0|581-preInit
            // write pre-init user code here
            postnatalcareCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|581-getter|1|581-postInit
            // write post-init user code here
        }//GEN-BEGIN:|581-getter|2|
        return postnatalcareCmd;
    }
//</editor-fold>//GEN-END:|581-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField24 ">//GEN-BEGIN:|585-getter|0|585-preInit
    /**
     * Returns an initialized instance of textField24 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField24() {
        if (textField24 == null) {//GEN-END:|585-getter|0|585-preInit
            // write pre-init user code here
            String str = getRecordValue(34);
            textField24 = new TextField(LocalizationSupport.getMessage("Post_partum"), str, 4, TextField.NUMERIC);//GEN-LINE:|585-getter|1|585-postInit
            // write post-init user code here
        }//GEN-BEGIN:|585-getter|2|
        return textField24;
    }
//</editor-fold>//GEN-END:|585-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField25 ">//GEN-BEGIN:|586-getter|0|586-preInit
    /**
     * Returns an initialized instance of textField25 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField25() {
        if (textField25 == null) {//GEN-END:|586-getter|0|586-preInit
            // write pre-init user code here
            String str = getRecordValue(35);
            textField25 = new TextField(LocalizationSupport.getMessage("Post_partum_48"), str, 4, TextField.NUMERIC);//GEN-LINE:|586-getter|1|586-postInit
            // write post-init user code here
        }//GEN-BEGIN:|586-getter|2|
        return textField25;
    }
//</editor-fold>//GEN-END:|586-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FamilyPlanning ">//GEN-BEGIN:|587-getter|0|587-preInit
    /**
     * Returns an initialized instance of FamilyPlanning component.
     *
     * @return the initialized component instance
     */
    public Form getFamilyPlanning() {
        if (FamilyPlanning == null) {//GEN-END:|587-getter|0|587-preInit
            // write pre-init user code here
            FamilyPlanning = new Form("Family Planning", new Item[]{getTextField26(), getTextField27(), getTextField28(), getTextField29(), getTextField30(), getTextField31(), getTextField32(), getTextField33(), getTextField34(), getTextField35(), getTextField36(), getTextField37()});//GEN-BEGIN:|587-getter|1|587-postInit
            FamilyPlanning.addCommand(getFamilyplanningBackCmd());
            FamilyPlanning.addCommand(getFamilyplanningCmd());
            FamilyPlanning.setCommandListener(this);//GEN-END:|587-getter|1|587-postInit
            // write post-init user code here
        }//GEN-BEGIN:|587-getter|2|
        return FamilyPlanning;
    }
//</editor-fold>//GEN-END:|587-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField26 ">//GEN-BEGIN:|594-getter|0|594-preInit
    /**
     * Returns an initialized instance of textField26 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField26() {
        if (textField26 == null) {//GEN-END:|594-getter|0|594-preInit
            // write pre-init user code here
            String str = getRecordValue(36);
            textField26 = new TextField(LocalizationSupport.getMessage("IUD_Inserted"), str, 4, TextField.NUMERIC);//GEN-LINE:|594-getter|1|594-postInit
            // write post-init user code here
        }//GEN-BEGIN:|594-getter|2|
        return textField26;
    }
//</editor-fold>//GEN-END:|594-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField27 ">//GEN-BEGIN:|595-getter|0|595-preInit
    /**
     * Returns an initialized instance of textField27 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField27() {
        if (textField27 == null) {//GEN-END:|595-getter|0|595-preInit
            // write pre-init user code here
            String str = getRecordValue(37);
            textField27 = new TextField(LocalizationSupport.getMessage("IUD_Removed"), str, 4, TextField.NUMERIC);//GEN-LINE:|595-getter|1|595-postInit
            // write post-init user code here
        }//GEN-BEGIN:|595-getter|2|
        return textField27;
    }
//</editor-fold>//GEN-END:|595-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField28 ">//GEN-BEGIN:|596-getter|0|596-preInit
    /**
     * Returns an initialized instance of textField28 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField28() {
        if (textField28 == null) {//GEN-END:|596-getter|0|596-preInit
            // write pre-init user code here
            String str = getRecordValue(38);
            textField28 = new TextField(LocalizationSupport.getMessage("Oral_Pills"), str, 4, TextField.NUMERIC);//GEN-LINE:|596-getter|1|596-postInit
            // write post-init user code here
        }//GEN-BEGIN:|596-getter|2|
        return textField28;
    }
//</editor-fold>//GEN-END:|596-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField29 ">//GEN-BEGIN:|597-getter|0|597-preInit
    /**
     * Returns an initialized instance of textField29 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField29() {
        if (textField29 == null) {//GEN-END:|597-getter|0|597-preInit
            // write pre-init user code here
            String str = getRecordValue(39);
            textField29 = new TextField(LocalizationSupport.getMessage("Condom_pieces"), str, 4, TextField.NUMERIC);//GEN-LINE:|597-getter|1|597-postInit
            // write post-init user code here
        }//GEN-BEGIN:|597-getter|2|
        return textField29;
    }
//</editor-fold>//GEN-END:|597-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField30 ">//GEN-BEGIN:|598-getter|0|598-preInit
    /**
     * Returns an initialized instance of textField30 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField30() {
        if (textField30 == null) {//GEN-END:|598-getter|0|598-preInit
            // write pre-init user code here
            String str = getRecordValue(40);
            textField30 = new TextField(LocalizationSupport.getMessage("Centchroman"), str, 4, TextField.NUMERIC);//GEN-LINE:|598-getter|1|598-postInit
            // write post-init user code here
        }//GEN-BEGIN:|598-getter|2|
        return textField30;
    }
//</editor-fold>//GEN-END:|598-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField31 ">//GEN-BEGIN:|599-getter|0|599-preInit
    /**
     * Returns an initialized instance of textField31 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField31() {
        if (textField31 == null) {//GEN-END:|599-getter|0|599-preInit
            // write pre-init user code here
            String str = getRecordValue(41);
            textField31 = new TextField(LocalizationSupport.getMessage("Emergency_contraceptive"), str, 4, TextField.NUMERIC);//GEN-LINE:|599-getter|1|599-postInit
            // write post-init user code here
        }//GEN-BEGIN:|599-getter|2|
        return textField31;
    }
//</editor-fold>//GEN-END:|599-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField32 ">//GEN-BEGIN:|600-getter|0|600-preInit
    /**
     * Returns an initialized instance of textField32 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField32() {
        if (textField32 == null) {//GEN-END:|600-getter|0|600-preInit
            // write pre-init user code here
            String str = getRecordValue(42);
            textField32 = new TextField(LocalizationSupport.getMessage("ComplicationsM"), str, 4, TextField.NUMERIC);//GEN-LINE:|600-getter|1|600-postInit
            // write post-init user code here
        }//GEN-BEGIN:|600-getter|2|
        return textField32;
    }
//</editor-fold>//GEN-END:|600-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField33 ">//GEN-BEGIN:|601-getter|0|601-preInit
    /**
     * Returns an initialized instance of textField33 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField33() {
        if (textField33 == null) {//GEN-END:|601-getter|0|601-preInit
            // write pre-init user code here
            String str = getRecordValue(43);
            textField33 = new TextField(LocalizationSupport.getMessage("ComplicationsF"), str, 4, TextField.NUMERIC);//GEN-LINE:|601-getter|1|601-postInit
            // write post-init user code here
        }//GEN-BEGIN:|601-getter|2|
        return textField33;
    }
//</editor-fold>//GEN-END:|601-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField34 ">//GEN-BEGIN:|602-getter|0|602-preInit
    /**
     * Returns an initialized instance of textField34 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField34() {
        if (textField34 == null) {//GEN-END:|602-getter|0|602-preInit
            // write pre-init user code here
            String str = getRecordValue(44);
            textField34 = new TextField(LocalizationSupport.getMessage("FailureM"), str, 4, TextField.NUMERIC);//GEN-LINE:|602-getter|1|602-postInit
            // write post-init user code here
        }//GEN-BEGIN:|602-getter|2|
        return textField34;
    }
//</editor-fold>//GEN-END:|602-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField35 ">//GEN-BEGIN:|603-getter|0|603-preInit
    /**
     * Returns an initialized instance of textField35 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField35() {
        if (textField35 == null) {//GEN-END:|603-getter|0|603-preInit
            // write pre-init user code here
            String str = getRecordValue(45);
            textField35 = new TextField(LocalizationSupport.getMessage("FailuresF"), str, 4, TextField.NUMERIC);//GEN-LINE:|603-getter|1|603-postInit
            // write post-init user code here
        }//GEN-BEGIN:|603-getter|2|
        return textField35;
    }
//</editor-fold>//GEN-END:|603-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField36 ">//GEN-BEGIN:|604-getter|0|604-preInit
    /**
     * Returns an initialized instance of textField36 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField36() {
        if (textField36 == null) {//GEN-END:|604-getter|0|604-preInit
            // write pre-init user code here
            String str = getRecordValue(46);
            textField36 = new TextField(LocalizationSupport.getMessage("DeathM"), str, 4, TextField.NUMERIC);//GEN-LINE:|604-getter|1|604-postInit
            // write post-init user code here
        }//GEN-BEGIN:|604-getter|2|
        return textField36;
    }
//</editor-fold>//GEN-END:|604-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField37 ">//GEN-BEGIN:|605-getter|0|605-preInit
    /**
     * Returns an initialized instance of textField37 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField37() {
        if (textField37 == null) {//GEN-END:|605-getter|0|605-preInit
            // write pre-init user code here
            String str = getRecordValue(47);
            textField37 = new TextField(LocalizationSupport.getMessage("DeathF"), str, 4, TextField.NUMERIC);//GEN-LINE:|605-getter|1|605-postInit
            // write post-init user code here
        }//GEN-BEGIN:|605-getter|2|
        return textField37;
    }
//</editor-fold>//GEN-END:|605-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: familyplanningBackCmd ">//GEN-BEGIN:|588-getter|0|588-preInit
    /**
     * Returns an initialized instance of familyplanningBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFamilyplanningBackCmd() {
        if (familyplanningBackCmd == null) {//GEN-END:|588-getter|0|588-preInit
            // write pre-init user code here
            familyplanningBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|588-getter|1|588-postInit
            // write post-init user code here
        }//GEN-BEGIN:|588-getter|2|
        return familyplanningBackCmd;
    }
//</editor-fold>//GEN-END:|588-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: familyplanningCmd ">//GEN-BEGIN:|590-getter|0|590-preInit
    /**
     * Returns an initialized instance of familyplanningCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFamilyplanningCmd() {
        if (familyplanningCmd == null) {//GEN-END:|590-getter|0|590-preInit
            // write pre-init user code here
            familyplanningCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|590-getter|1|590-postInit
            // write post-init user code here
        }//GEN-BEGIN:|590-getter|2|
        return familyplanningCmd;
    }
//</editor-fold>//GEN-END:|590-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChildImmunization ">//GEN-BEGIN:|606-getter|0|606-preInit
    /**
     * Returns an initialized instance of ChildImmunization component.
     *
     * @return the initialized component instance
     */
    public Form getChildImmunization() {
        if (ChildImmunization == null) {//GEN-END:|606-getter|0|606-preInit
            // write pre-init user code here
            ChildImmunization = new Form("Child Immu(0-11 months) ", new Item[]{getTextField38(), getTextField39(), getTextField40(), getTextField41(), getTextField42(), getTextField43(), getTextField44(), getTextField45(), getTextFieldHepB0(), getTextField46(), getTextField47(), getTextField48(), getTextField49(), getTextMeasles2nddose(), getTextField50(), getTextField51()});//GEN-BEGIN:|606-getter|1|606-postInit
            ChildImmunization.addCommand(getChildimmunizationBackCmd());
            ChildImmunization.addCommand(getChildimmunizationCmd());
            ChildImmunization.setCommandListener(this);//GEN-END:|606-getter|1|606-postInit
            // write post-init user code here
        }//GEN-BEGIN:|606-getter|2|
        return ChildImmunization;
    }
//</editor-fold>//GEN-END:|606-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: childimmunizationBackCmd ">//GEN-BEGIN:|607-getter|0|607-preInit
    /**
     * Returns an initialized instance of childimmunizationBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getChildimmunizationBackCmd() {
        if (childimmunizationBackCmd == null) {//GEN-END:|607-getter|0|607-preInit
            // write pre-init user code here
            childimmunizationBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|607-getter|1|607-postInit
            // write post-init user code here
        }//GEN-BEGIN:|607-getter|2|
        return childimmunizationBackCmd;
    }
//</editor-fold>//GEN-END:|607-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: childimmunizationCmd ">//GEN-BEGIN:|609-getter|0|609-preInit
    /**
     * Returns an initialized instance of childimmunizationCmd component.
     *
     * @return the initialized component instance
     */
    public Command getChildimmunizationCmd() {
        if (childimmunizationCmd == null) {//GEN-END:|609-getter|0|609-preInit
            // write pre-init user code here
            childimmunizationCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|609-getter|1|609-postInit
            // write post-init user code here
        }//GEN-BEGIN:|609-getter|2|
        return childimmunizationCmd;
    }
//</editor-fold>//GEN-END:|609-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField38 ">//GEN-BEGIN:|613-getter|0|613-preInit
    /**
     * Returns an initialized instance of textField38 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField38() {
        if (textField38 == null) {//GEN-END:|613-getter|0|613-preInit
            // write pre-init user code here
            String str = getRecordValue(48);
            textField38 = new TextField(LocalizationSupport.getMessage("BCG"), str, 4, TextField.NUMERIC);//GEN-LINE:|613-getter|1|613-postInit
            // write post-init user code here
        }//GEN-BEGIN:|613-getter|2|
        return textField38;
    }
//</editor-fold>//GEN-END:|613-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField39 ">//GEN-BEGIN:|614-getter|0|614-preInit
    /**
     * Returns an initialized instance of textField39 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField39() {
        if (textField39 == null) {//GEN-END:|614-getter|0|614-preInit
            // write pre-init user code here
            String str = getRecordValue(49);
            textField39 = new TextField(LocalizationSupport.getMessage("DPT1"), str, 4, TextField.NUMERIC);//GEN-LINE:|614-getter|1|614-postInit
            // write post-init user code here
        }//GEN-BEGIN:|614-getter|2|
        return textField39;
    }
//</editor-fold>//GEN-END:|614-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField40 ">//GEN-BEGIN:|615-getter|0|615-preInit
    /**
     * Returns an initialized instance of textField40 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField40() {
        if (textField40 == null) {//GEN-END:|615-getter|0|615-preInit
            // write pre-init user code here
            String str = getRecordValue(50);
            textField40 = new TextField(LocalizationSupport.getMessage("DPT2"), str, 4, TextField.NUMERIC);//GEN-LINE:|615-getter|1|615-postInit
            // write post-init user code here
        }//GEN-BEGIN:|615-getter|2|
        return textField40;
    }
//</editor-fold>//GEN-END:|615-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField41 ">//GEN-BEGIN:|616-getter|0|616-preInit
    /**
     * Returns an initialized instance of textField41 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField41() {
        if (textField41 == null) {//GEN-END:|616-getter|0|616-preInit
            // write pre-init user code here
            String str = getRecordValue(51);
            textField41 = new TextField(LocalizationSupport.getMessage("DPT3"), str, 4, TextField.NUMERIC);//GEN-LINE:|616-getter|1|616-postInit
            // write post-init user code here
        }//GEN-BEGIN:|616-getter|2|
        return textField41;
    }
//</editor-fold>//GEN-END:|616-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField42 ">//GEN-BEGIN:|617-getter|0|617-preInit
    /**
     * Returns an initialized instance of textField42 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField42() {
        if (textField42 == null) {//GEN-END:|617-getter|0|617-preInit
            // write pre-init user code here
            String str = getRecordValue(52);
            textField42 = new TextField(LocalizationSupport.getMessage("OPV0"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|617-getter|1|617-postInit
//GEN-END:|617-getter|1|617-postInit
        }//GEN-BEGIN:|617-getter|2|
        return textField42;
    }
//</editor-fold>//GEN-END:|617-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField43 ">//GEN-BEGIN:|618-getter|0|618-preInit
    /**
     * Returns an initialized instance of textField43 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField43() {
        if (textField43 == null) {//GEN-END:|618-getter|0|618-preInit
            // write pre-init user code here
            String str = getRecordValue(53);
            textField43 = new TextField(LocalizationSupport.getMessage("OPV1"), str, 4, TextField.NUMERIC);//GEN-LINE:|618-getter|1|618-postInit
            // write post-init user code here
        }//GEN-BEGIN:|618-getter|2|
        return textField43;
    }
//</editor-fold>//GEN-END:|618-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField44 ">//GEN-BEGIN:|619-getter|0|619-preInit
    /**
     * Returns an initialized instance of textField44 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField44() {
        if (textField44 == null) {//GEN-END:|619-getter|0|619-preInit
            // write pre-init user code here
            String str = getRecordValue(54);
            textField44 = new TextField(LocalizationSupport.getMessage("OPV2"), str, 4, TextField.NUMERIC);//GEN-LINE:|619-getter|1|619-postInit
            // write post-init user code here
        }//GEN-BEGIN:|619-getter|2|
        return textField44;
    }
//</editor-fold>//GEN-END:|619-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField45 ">//GEN-BEGIN:|620-getter|0|620-preInit
    /**
     * Returns an initialized instance of textField45 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField45() {
        if (textField45 == null) {//GEN-END:|620-getter|0|620-preInit
            // write pre-init user code here
            String str = getRecordValue(55);
            textField45 = new TextField(LocalizationSupport.getMessage("OPV3"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|620-getter|1|620-postInit
//GEN-END:|620-getter|1|620-postInit
        }//GEN-BEGIN:|620-getter|2|
        return textField45;
    }
//</editor-fold>//GEN-END:|620-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField46 ">//GEN-BEGIN:|621-getter|0|621-preInit
    /**
     * Returns an initialized instance of textField46 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField46() {
        if (textField46 == null) {//GEN-END:|621-getter|0|621-preInit
            // write pre-init user code here
            String str = getRecordValue(56);
            textField46 = new TextField(LocalizationSupport.getMessage("B1"), str, 4, TextField.NUMERIC);//GEN-LINE:|621-getter|1|621-postInit
            // write post-init user code here
        }//GEN-BEGIN:|621-getter|2|
        return textField46;
    }
//</editor-fold>//GEN-END:|621-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField47 ">//GEN-BEGIN:|622-getter|0|622-preInit
    /**
     * Returns an initialized instance of textField47 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField47() {
        if (textField47 == null) {//GEN-END:|622-getter|0|622-preInit
            // write pre-init user code here
            String str = getRecordValue(57);
            textField47 = new TextField(LocalizationSupport.getMessage("B2"), str, 4, TextField.NUMERIC);//GEN-LINE:|622-getter|1|622-postInit
            // write post-init user code here
        }//GEN-BEGIN:|622-getter|2|
        return textField47;
    }
//</editor-fold>//GEN-END:|622-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField48 ">//GEN-BEGIN:|623-getter|0|623-preInit
    /**
     * Returns an initialized instance of textField48 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField48() {
        if (textField48 == null) {//GEN-END:|623-getter|0|623-preInit
            // write pre-init user code here
            String str = getRecordValue(58);
            textField48 = new TextField(LocalizationSupport.getMessage("B3"), str, 4, TextField.NUMERIC);//GEN-LINE:|623-getter|1|623-postInit
            // write post-init user code here
        }//GEN-BEGIN:|623-getter|2|
        return textField48;
    }
//</editor-fold>//GEN-END:|623-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField49 ">//GEN-BEGIN:|624-getter|0|624-preInit
    /**
     * Returns an initialized instance of textField49 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField49() {
        if (textField49 == null) {//GEN-END:|624-getter|0|624-preInit
            // write pre-init user code here
            String str = getRecordValue(59);
            textField49 = new TextField(LocalizationSupport.getMessage("Measles"), str, 4, TextField.NUMERIC);//GEN-LINE:|624-getter|1|624-postInit
            // write post-init user code here
        }//GEN-BEGIN:|624-getter|2|
        return textField49;
    }
//</editor-fold>//GEN-END:|624-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField50 ">//GEN-BEGIN:|625-getter|0|625-preInit
    /**
     * Returns an initialized instance of textField50 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField50() {
        if (textField50 == null) {//GEN-END:|625-getter|0|625-preInit
            // write pre-init user code here
            String str = getRecordValue(60);
            textField50 = new TextField(LocalizationSupport.getMessage("ImmunizationM"), str, 4, TextField.NUMERIC);//GEN-LINE:|625-getter|1|625-postInit
            // write post-init user code here
        }//GEN-BEGIN:|625-getter|2|
        return textField50;
    }
//</editor-fold>//GEN-END:|625-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField51 ">//GEN-BEGIN:|626-getter|0|626-preInit
    /**
     * Returns an initialized instance of textField51 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField51() {
        if (textField51 == null) {//GEN-END:|626-getter|0|626-preInit
            // write pre-init user code here
            String str = getRecordValue(61);
            textField51 = new TextField(LocalizationSupport.getMessage("ImmunizationF"), str, 4, TextField.NUMERIC);//GEN-LINE:|626-getter|1|626-postInit
            // write post-init user code here
        }//GEN-BEGIN:|626-getter|2|
        return textField51;
    }
//</editor-fold>//GEN-END:|626-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField52 ">//GEN-BEGIN:|634-getter|0|634-preInit
    /**
     * Returns an initialized instance of textField52 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField52() {
        if (textField52 == null) {//GEN-END:|634-getter|0|634-preInit
            // write pre-init user code here
            String str = getRecordValue(62);
            textField52 = new TextField(LocalizationSupport.getMessage("DPT_Booster"), str, 4, TextField.NUMERIC);//GEN-LINE:|634-getter|1|634-postInit
            // write post-init user code here
        }//GEN-BEGIN:|634-getter|2|
        return textField52;
    }
//</editor-fold>//GEN-END:|634-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField53 ">//GEN-BEGIN:|635-getter|0|635-preInit
    /**
     * Returns an initialized instance of textField53 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField53() {
        if (textField53 == null) {//GEN-END:|635-getter|0|635-preInit
            // write pre-init user code here
            String str = getRecordValue(63);
            textField53 = new TextField(LocalizationSupport.getMessage("OPV_Booster"), str, 4, TextField.NUMERIC);//GEN-LINE:|635-getter|1|635-postInit
            // write post-init user code here
        }//GEN-BEGIN:|635-getter|2|
        return textField53;
    }
//</editor-fold>//GEN-END:|635-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField54 ">//GEN-BEGIN:|636-getter|0|636-preInit
    /**
     * Returns an initialized instance of textField54 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField54() {
        if (textField54 == null) {//GEN-END:|636-getter|0|636-preInit
            // write pre-init user code here
            String str = getRecordValue(64);
            textField54 = new TextField(LocalizationSupport.getMessage("MMR"), str, 4, TextField.NUMERIC);//GEN-LINE:|636-getter|1|636-postInit
            // write post-init user code here
        }//GEN-BEGIN:|636-getter|2|
        return textField54;
    }
//</editor-fold>//GEN-END:|636-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField55 ">//GEN-BEGIN:|637-getter|0|637-preInit
    /**
     * Returns an initialized instance of textField55 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField55() {
        if (textField55 == null) {//GEN-END:|637-getter|0|637-preInit
            // write pre-init user code here
            String str = getRecordValue(65);
            textField55 = new TextField(LocalizationSupport.getMessage("Immunization12M"), str, 4, TextField.NUMERIC);//GEN-LINE:|637-getter|1|637-postInit
            // write post-init user code here
        }//GEN-BEGIN:|637-getter|2|
        return textField55;
    }
//</editor-fold>//GEN-END:|637-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField56 ">//GEN-BEGIN:|638-getter|0|638-preInit
    /**
     * Returns an initialized instance of textField56 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField56() {
        if (textField56 == null) {//GEN-END:|638-getter|0|638-preInit
            // write pre-init user code here
            String str = getRecordValue(66);
            textField56 = new TextField(LocalizationSupport.getMessage("Immunization12F"), str, 4, TextField.NUMERIC);//GEN-LINE:|638-getter|1|638-postInit
            // write post-init user code here
        }//GEN-BEGIN:|638-getter|2|
        return textField56;
    }
//</editor-fold>//GEN-END:|638-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField57 ">//GEN-BEGIN:|639-getter|0|639-preInit
    /**
     * Returns an initialized instance of textField57 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField57() {
        if (textField57 == null) {//GEN-END:|639-getter|0|639-preInit
            // write pre-init user code here
            String str = getRecordValue(67);
            textField57 = new TextField(LocalizationSupport.getMessage("DT5"), str, 4, TextField.NUMERIC);//GEN-LINE:|639-getter|1|639-postInit
            // write post-init user code here
        }//GEN-BEGIN:|639-getter|2|
        return textField57;
    }
//</editor-fold>//GEN-END:|639-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField58 ">//GEN-BEGIN:|640-getter|0|640-preInit
    /**
     * Returns an initialized instance of textField58 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField58() {
        if (textField58 == null) {//GEN-END:|640-getter|0|640-preInit
            // write pre-init user code here
            String str = getRecordValue(68);
            textField58 = new TextField(LocalizationSupport.getMessage("TT10"), str, 4, TextField.NUMERIC);//GEN-LINE:|640-getter|1|640-postInit
            // write post-init user code here
        }//GEN-BEGIN:|640-getter|2|
        return textField58;
    }
//</editor-fold>//GEN-END:|640-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField59 ">//GEN-BEGIN:|641-getter|0|641-preInit
    /**
     * Returns an initialized instance of textField59 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField59() {
        if (textField59 == null) {//GEN-END:|641-getter|0|641-preInit
            // write pre-init user code here
            String str = getRecordValue(69);
            textField59 = new TextField(LocalizationSupport.getMessage("TT16"), str, 4, TextField.NUMERIC);//GEN-LINE:|641-getter|1|641-postInit
            // write post-init user code here
        }//GEN-BEGIN:|641-getter|2|
        return textField59;
    }
//</editor-fold>//GEN-END:|641-getter|2|

    
    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Measles 2nd dose ">
    /**
     * Returns an initialized instance of Measles 2nd dose component.
     *
     * @return the initialized component instance
     */
    public TextField getTextFieldMeasles2nddose() {
        if (textMeasles2nddose == null) {
            // write pre-init user code here
            String str = getRecordValue(87);
            textMeasles2nddose = new TextField(LocalizationSupport.getMessage("Measles2nddose"), str, 4, TextField.NUMERIC);
            // write post-init user code here
        }
        return textMeasles2nddose;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AEFI ">//GEN-BEGIN:|642-getter|0|642-preInit
    /**
     * Returns an initialized instance of AEFI component.
     *
     * @return the initialized component instance
     */
    public Form getAEFI() {
        if (AEFI == null) {//GEN-END:|642-getter|0|642-preInit
            // write pre-init user code here
            AEFI = new Form("AEFI", new Item[]{getTextField60(), getTextField61(), getTextField62()});//GEN-BEGIN:|642-getter|1|642-postInit
            AEFI.addCommand(getAefiBackCmd());
            AEFI.addCommand(getAefiCmd());
            AEFI.setCommandListener(this);//GEN-END:|642-getter|1|642-postInit
            // write post-init user code here
        }//GEN-BEGIN:|642-getter|2|
        return AEFI;
    }
//</editor-fold>//GEN-END:|642-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: followingimmunizationBackCmd ">//GEN-BEGIN:|628-getter|0|628-preInit
    /**
     * Returns an initialized instance of followingimmunizationBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFollowingimmunizationBackCmd() {
        if (followingimmunizationBackCmd == null) {//GEN-END:|628-getter|0|628-preInit
            // write pre-init user code here
            followingimmunizationBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|628-getter|1|628-postInit
            // write post-init user code here
        }//GEN-BEGIN:|628-getter|2|
        return followingimmunizationBackCmd;
    }
//</editor-fold>//GEN-END:|628-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: followingimmunizationCmd ">//GEN-BEGIN:|630-getter|0|630-preInit
    /**
     * Returns an initialized instance of followingimmunizationCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFollowingimmunizationCmd() {
        if (followingimmunizationCmd == null) {//GEN-END:|630-getter|0|630-preInit
            // write pre-init user code here
            followingimmunizationCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|630-getter|1|630-postInit
            // write post-init user code here
        }//GEN-BEGIN:|630-getter|2|
        return followingimmunizationCmd;
    }
//</editor-fold>//GEN-END:|630-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField60 ">//GEN-BEGIN:|649-getter|0|649-preInit
    /**
     * Returns an initialized instance of textField60 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField60() {
        if (textField60 == null) {//GEN-END:|649-getter|0|649-preInit
            // write pre-init user code here
            String str = getRecordValue(70);
            textField60 = new TextField(LocalizationSupport.getMessage("Abscess"), str, 4, TextField.NUMERIC);//GEN-LINE:|649-getter|1|649-postInit
            // write post-init user code here
        }//GEN-BEGIN:|649-getter|2|
        return textField60;
    }
//</editor-fold>//GEN-END:|649-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField61 ">//GEN-BEGIN:|650-getter|0|650-preInit
    /**
     * Returns an initialized instance of textField61 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField61() {
        if (textField61 == null) {//GEN-END:|650-getter|0|650-preInit
            // write pre-init user code here
            String str = getRecordValue(71);
            textField61 = new TextField(LocalizationSupport.getMessage("Death"), str, 4, TextField.NUMERIC);//GEN-LINE:|650-getter|1|650-postInit
            // write post-init user code here
        }//GEN-BEGIN:|650-getter|2|
        return textField61;
    }
//</editor-fold>//GEN-END:|650-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField62 ">//GEN-BEGIN:|651-getter|0|651-preInit
    /**
     * Returns an initialized instance of textField62 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField62() {
        if (textField62 == null) {//GEN-END:|651-getter|0|651-preInit
            // write pre-init user code here
            String str = getRecordValue(72);
            textField62 = new TextField(LocalizationSupport.getMessage("Others"), str, 4, TextField.NUMERIC);//GEN-LINE:|651-getter|1|651-postInit
            // write post-init user code here
        }//GEN-BEGIN:|651-getter|2|
        return textField62;
    }
//</editor-fold>//GEN-END:|651-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Immunizationsessions ">//GEN-BEGIN:|652-getter|0|652-preInit
    /**
     * Returns an initialized instance of Immunizationsessions component.
     *
     * @return the initialized component instance
     */
    public Form getImmunizationsessions() {
        if (Immunizationsessions == null) {//GEN-END:|652-getter|0|652-preInit
            // write pre-init user code here
            Immunizationsessions = new Form("Immu Sessions", new Item[]{getTextField63(), getTextField64(), getTextAWWpresent()});//GEN-BEGIN:|652-getter|1|652-postInit
            Immunizationsessions.addCommand(getImmunizationsessionsBackCmd());
            Immunizationsessions.addCommand(getImmunizationsessionsCmd());
            Immunizationsessions.setCommandListener(this);//GEN-END:|652-getter|1|652-postInit
            // write post-init user code here
        }//GEN-BEGIN:|652-getter|2|
        return Immunizationsessions;
    }
//</editor-fold>//GEN-END:|652-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField63 ">//GEN-BEGIN:|659-getter|0|659-preInit
    /**
     * Returns an initialized instance of textField63 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField63() {
        if (textField63 == null) {//GEN-END:|659-getter|0|659-preInit
            // write pre-init user code here
            String str = getRecordValue(73);
            textField63 = new TextField(LocalizationSupport.getMessage("Sessions_Planned"), str, 4, TextField.NUMERIC);//GEN-LINE:|659-getter|1|659-postInit
            // write post-init user code here
        }//GEN-BEGIN:|659-getter|2|
        return textField63;
    }
//</editor-fold>//GEN-END:|659-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField64 ">//GEN-BEGIN:|660-getter|0|660-preInit
    /**
     * Returns an initialized instance of textField64 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField64() {
        if (textField64 == null) {//GEN-END:|660-getter|0|660-preInit
            // write pre-init user code here
            String str = getRecordValue(74);
            textField64 = new TextField(LocalizationSupport.getMessage("Sessions_held"), str, 4, TextField.NUMERIC);//GEN-LINE:|660-getter|1|660-postInit
            // write post-init user code here
        }//GEN-BEGIN:|660-getter|2|
        return textField64;
    }
//</editor-fold>//GEN-END:|660-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: VitaminADose ">//GEN-BEGIN:|661-getter|0|661-preInit
    /**
     * Returns an initialized instance of VitaminADose component.
     *
     * @return the initialized component instance
     */
    public Form getVitaminADose() {
        if (VitaminADose == null) {//GEN-END:|661-getter|0|661-preInit
            // write pre-init user code here
            VitaminADose = new Form("Vit.A Dose(9 mnt & 5 yrs) ", new Item[]{getTextField65(), getTextField66(), getTextField67()});//GEN-BEGIN:|661-getter|1|661-postInit
            VitaminADose.addCommand(getVitaminadoseBackCmd());
            VitaminADose.addCommand(getVitaminadoseCmd());
            VitaminADose.setCommandListener(this);//GEN-END:|661-getter|1|661-postInit
            // write post-init user code here
        }//GEN-BEGIN:|661-getter|2|
        return VitaminADose;
    }
//</editor-fold>//GEN-END:|661-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: aefiBackCmd ">//GEN-BEGIN:|643-getter|0|643-preInit
    /**
     * Returns an initialized instance of aefiBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getAefiBackCmd() {
        if (aefiBackCmd == null) {//GEN-END:|643-getter|0|643-preInit
            // write pre-init user code here
            aefiBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|643-getter|1|643-postInit
            // write post-init user code here
        }//GEN-BEGIN:|643-getter|2|
        return aefiBackCmd;
    }
//</editor-fold>//GEN-END:|643-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: aefiCmd ">//GEN-BEGIN:|645-getter|0|645-preInit
    /**
     * Returns an initialized instance of aefiCmd component.
     *
     * @return the initialized component instance
     */
    public Command getAefiCmd() {
        if (aefiCmd == null) {//GEN-END:|645-getter|0|645-preInit
            // write pre-init user code here
            aefiCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|645-getter|1|645-postInit
            // write post-init user code here
        }//GEN-BEGIN:|645-getter|2|
        return aefiCmd;
    }
//</editor-fold>//GEN-END:|645-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: immunizationsessionsBackCmd ">//GEN-BEGIN:|653-getter|0|653-preInit
    /**
     * Returns an initialized instance of immunizationsessionsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getImmunizationsessionsBackCmd() {
        if (immunizationsessionsBackCmd == null) {//GEN-END:|653-getter|0|653-preInit
            // write pre-init user code here
            immunizationsessionsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|653-getter|1|653-postInit
            // write post-init user code here
        }//GEN-BEGIN:|653-getter|2|
        return immunizationsessionsBackCmd;
    }
//</editor-fold>//GEN-END:|653-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: immunizationsessionsCmd ">//GEN-BEGIN:|655-getter|0|655-preInit
    /**
     * Returns an initialized instance of immunizationsessionsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getImmunizationsessionsCmd() {
        if (immunizationsessionsCmd == null) {//GEN-END:|655-getter|0|655-preInit
            // write pre-init user code here
            immunizationsessionsCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|655-getter|1|655-postInit
            // write post-init user code here
        }//GEN-BEGIN:|655-getter|2|
        return immunizationsessionsCmd;
    }
//</editor-fold>//GEN-END:|655-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: vitaminadoseBackCmd ">//GEN-BEGIN:|662-getter|0|662-preInit
    /**
     * Returns an initialized instance of vitaminadoseBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getVitaminadoseBackCmd() {
        if (vitaminadoseBackCmd == null) {//GEN-END:|662-getter|0|662-preInit
            // write pre-init user code here
            vitaminadoseBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|662-getter|1|662-postInit
            // write post-init user code here
        }//GEN-BEGIN:|662-getter|2|
        return vitaminadoseBackCmd;
    }
//</editor-fold>//GEN-END:|662-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: vitaminadoseCmd ">//GEN-BEGIN:|664-getter|0|664-preInit
    /**
     * Returns an initialized instance of vitaminadoseCmd component.
     *
     * @return the initialized component instance
     */
    public Command getVitaminadoseCmd() {
        if (vitaminadoseCmd == null) {//GEN-END:|664-getter|0|664-preInit
            // write pre-init user code here
            vitaminadoseCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|664-getter|1|664-postInit
            // write post-init user code here
        }//GEN-BEGIN:|664-getter|2|
        return vitaminadoseCmd;
    }
//</editor-fold>//GEN-END:|664-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField65 ">//GEN-BEGIN:|666-getter|0|666-preInit
    /**
     * Returns an initialized instance of textField65 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField65() {
        if (textField65 == null) {//GEN-END:|666-getter|0|666-preInit
            // write pre-init user code here
            String str = getRecordValue(75);
            textField65 = new TextField(LocalizationSupport.getMessage("dose1"), str, 4, TextField.NUMERIC);//GEN-LINE:|666-getter|1|666-postInit
            // write post-init user code here
        }//GEN-BEGIN:|666-getter|2|
        return textField65;
    }
//</editor-fold>//GEN-END:|666-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField66 ">//GEN-BEGIN:|667-getter|0|667-preInit
    /**
     * Returns an initialized instance of textField66 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField66() {
        if (textField66 == null) {//GEN-END:|667-getter|0|667-preInit
            // write pre-init user code here
            String str = getRecordValue(76);
            textField66 = new TextField(LocalizationSupport.getMessage("dose5"), str, 4, TextField.NUMERIC);//GEN-LINE:|667-getter|1|667-postInit
            // write post-init user code here
        }//GEN-BEGIN:|667-getter|2|
        return textField66;
    }
//</editor-fold>//GEN-END:|667-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField67 ">//GEN-BEGIN:|668-getter|0|668-preInit
    /**
     * Returns an initialized instance of textField67 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField67() {
        if (textField67 == null) {//GEN-END:|668-getter|0|668-preInit
            // write pre-init user code here
            String str = getRecordValue(77);
            textField67 = new TextField(LocalizationSupport.getMessage("dose9"), str, 4, TextField.NUMERIC);//GEN-LINE:|668-getter|1|668-postInit
            // write post-init user code here
        }//GEN-BEGIN:|668-getter|2|
        return textField67;
    }
//</editor-fold>//GEN-END:|668-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChildhoodDiseasesreported ">//GEN-BEGIN:|671-getter|0|671-preInit
    /**
     * Returns an initialized instance of ChildhoodDiseasesreported component.
     *
     * @return the initialized component instance
     */
    public Form getChildhoodDiseasesreported() {
        if (ChildhoodDiseasesreported == null) {//GEN-END:|671-getter|0|671-preInit
            // write pre-init user code here
            ChildhoodDiseasesreported = new Form("Childhood Diseases reported", new Item[]{getTextField68(), getTextField69(), getTextField70()});//GEN-BEGIN:|671-getter|1|671-postInit
            ChildhoodDiseasesreported.addCommand(getChildhoodBackCmd());
            ChildhoodDiseasesreported.addCommand(getChildhoodCmd());
            ChildhoodDiseasesreported.setCommandListener(this);//GEN-END:|671-getter|1|671-postInit
            // write post-init user code here
        }//GEN-BEGIN:|671-getter|2|
        return ChildhoodDiseasesreported;
    }
//</editor-fold>//GEN-END:|671-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: childhoodBackCmd ">//GEN-BEGIN:|672-getter|0|672-preInit
    /**
     * Returns an initialized instance of childhoodBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getChildhoodBackCmd() {
        if (childhoodBackCmd == null) {//GEN-END:|672-getter|0|672-preInit
            // write pre-init user code here
            childhoodBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|672-getter|1|672-postInit
            // write post-init user code here
        }//GEN-BEGIN:|672-getter|2|
        return childhoodBackCmd;
    }
//</editor-fold>//GEN-END:|672-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: childhoodCmd ">//GEN-BEGIN:|674-getter|0|674-preInit
    /**
     * Returns an initialized instance of childhoodCmd component.
     *
     * @return the initialized component instance
     */
    public Command getChildhoodCmd() {
        if (childhoodCmd == null) {//GEN-END:|674-getter|0|674-preInit
            // write pre-init user code here
            childhoodCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|674-getter|1|674-postInit
            // write post-init user code here
        }//GEN-BEGIN:|674-getter|2|
        return childhoodCmd;
    }
//</editor-fold>//GEN-END:|674-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField68 ">//GEN-BEGIN:|678-getter|0|678-preInit
    /**
     * Returns an initialized instance of textField68 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField68() {
        if (textField68 == null) {//GEN-END:|678-getter|0|678-preInit
            // write pre-init user code here
            String str = getRecordValue(78);
            textField68 = new TextField(LocalizationSupport.getMessage("ChildhoodMeasles"), str, 4, TextField.NUMERIC);//GEN-LINE:|678-getter|1|678-postInit
            // write post-init user code here
        }//GEN-BEGIN:|678-getter|2|
        return textField68;
    }
//</editor-fold>//GEN-END:|678-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField69 ">//GEN-BEGIN:|679-getter|0|679-preInit
    /**
     * Returns an initialized instance of textField69 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField69() {
        if (textField69 == null) {//GEN-END:|679-getter|0|679-preInit
            // write pre-init user code here
            String str = getRecordValue(79);
            textField69 = new TextField(LocalizationSupport.getMessage("Diarrhoea"), str, 4, TextField.NUMERIC);//GEN-LINE:|679-getter|1|679-postInit
            // write post-init user code here
        }//GEN-BEGIN:|679-getter|2|
        return textField69;
    }
//</editor-fold>//GEN-END:|679-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField70 ">//GEN-BEGIN:|680-getter|0|680-preInit
    /**
     * Returns an initialized instance of textField70 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField70() {
        if (textField70 == null) {//GEN-END:|680-getter|0|680-preInit
            // write pre-init user code here
            String str = getRecordValue(80);
            textField70 = new TextField(LocalizationSupport.getMessage("Malaria"), str, 4, TextField.NUMERIC);//GEN-LINE:|680-getter|1|680-postInit
            // write post-init user code here
        }//GEN-BEGIN:|680-getter|2|
        return textField70;
    }
//</editor-fold>//GEN-END:|680-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: HealthFacilityServices ">//GEN-BEGIN:|681-getter|0|681-preInit
    /**
     * Returns an initialized instance of HealthFacilityServices component.
     *
     * @return the initialized component instance
     */
    public Form getHealthFacilityServices() {
        if (HealthFacilityServices == null) {//GEN-END:|681-getter|0|681-preInit
            // write pre-init user code here
            HealthFacilityServices = new Form("Health Facility Services", new Item[]{getTextField71(), getTextField72()});//GEN-BEGIN:|681-getter|1|681-postInit
            HealthFacilityServices.addCommand(getHealthfacilityservicesBackCmd());
            HealthFacilityServices.addCommand(getHealthfacilityservicesCmd());
            HealthFacilityServices.setCommandListener(this);//GEN-END:|681-getter|1|681-postInit
            // write post-init user code here
        }//GEN-BEGIN:|681-getter|2|
        return HealthFacilityServices;
    }
//</editor-fold>//GEN-END:|681-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: LabTests ">//GEN-BEGIN:|688-getter|0|688-preInit
    /**
     * Returns an initialized instance of LabTests component.
     *
     * @return the initialized component instance
     */
    public Form getLabTests() {
        if (LabTests == null) {//GEN-END:|688-getter|0|688-preInit
            // write pre-init user code here
            LabTests = new Form("Lab Tests", new Item[]{getTextField73(), getTextField74()});//GEN-BEGIN:|688-getter|1|688-postInit
            LabTests.addCommand(getLabtestsBackCmd());
            LabTests.addCommand(getLabtestsCmd());
            LabTests.setCommandListener(this);//GEN-END:|688-getter|1|688-postInit
            // write post-init user code here
        }//GEN-BEGIN:|688-getter|2|
        return LabTests;
    }
//</editor-fold>//GEN-END:|688-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: healthfacilityservicesBackCmd ">//GEN-BEGIN:|682-getter|0|682-preInit
    /**
     * Returns an initialized instance of healthfacilityservicesBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getHealthfacilityservicesBackCmd() {
        if (healthfacilityservicesBackCmd == null) {//GEN-END:|682-getter|0|682-preInit
            // write pre-init user code here
            healthfacilityservicesBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|682-getter|1|682-postInit
            // write post-init user code here
        }//GEN-BEGIN:|682-getter|2|
        return healthfacilityservicesBackCmd;
    }
//</editor-fold>//GEN-END:|682-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: healthfacilityservicesCmd ">//GEN-BEGIN:|684-getter|0|684-preInit
    /**
     * Returns an initialized instance of healthfacilityservicesCmd component.
     *
     * @return the initialized component instance
     */
    public Command getHealthfacilityservicesCmd() {
        if (healthfacilityservicesCmd == null) {//GEN-END:|684-getter|0|684-preInit
            // write pre-init user code here
            healthfacilityservicesCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|684-getter|1|684-postInit
            // write post-init user code here
        }//GEN-BEGIN:|684-getter|2|
        return healthfacilityservicesCmd;
    }
//</editor-fold>//GEN-END:|684-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: labtestsBackCmd ">//GEN-BEGIN:|689-getter|0|689-preInit
    /**
     * Returns an initialized instance of labtestsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLabtestsBackCmd() {
        if (labtestsBackCmd == null) {//GEN-END:|689-getter|0|689-preInit
            // write pre-init user code here
            labtestsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|689-getter|1|689-postInit
            // write post-init user code here
        }//GEN-BEGIN:|689-getter|2|
        return labtestsBackCmd;
    }
//</editor-fold>//GEN-END:|689-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: labtestsCmd ">//GEN-BEGIN:|691-getter|0|691-preInit
    /**
     * Returns an initialized instance of labtestsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLabtestsCmd() {
        if (labtestsCmd == null) {//GEN-END:|691-getter|0|691-preInit
            // write pre-init user code here
            labtestsCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|691-getter|1|691-postInit
            // write post-init user code here
        }//GEN-BEGIN:|691-getter|2|
        return labtestsCmd;
    }
//</editor-fold>//GEN-END:|691-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField71 ">//GEN-BEGIN:|696-getter|0|696-preInit
    /**
     * Returns an initialized instance of textField71 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField71() {
        if (textField71 == null) {//GEN-END:|696-getter|0|696-preInit
            // write pre-init user code here
            String str = getRecordValue(81);
            textField71 = new TextField(LocalizationSupport.getMessage("VHNDs"), str, 4, TextField.NUMERIC);//GEN-LINE:|696-getter|1|696-postInit
            // write post-init user code here
        }//GEN-BEGIN:|696-getter|2|
        return textField71;
    }
//</editor-fold>//GEN-END:|696-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField72 ">//GEN-BEGIN:|697-getter|0|697-preInit
    /**
     * Returns an initialized instance of textField72 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField72() {
        if (textField72 == null) {//GEN-END:|697-getter|0|697-preInit
            // write pre-init user code here
            String str = getRecordValue(82);
            textField72 = new TextField(LocalizationSupport.getMessage("OPD_ALL"), str, 4, TextField.NUMERIC);//GEN-LINE:|697-getter|1|697-postInit
            // write post-init user code here
        }//GEN-BEGIN:|697-getter|2|
        return textField72;
    }
//</editor-fold>//GEN-END:|697-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField73 ">//GEN-BEGIN:|698-getter|0|698-preInit
    /**
     * Returns an initialized instance of textField73 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField73() {
        if (textField73 == null) {//GEN-END:|698-getter|0|698-preInit
            // write pre-init user code here
            String str = getRecordValue(83);
            textField73 = new TextField(LocalizationSupport.getMessage("Hb_tests_conducted"), str, 4, TextField.NUMERIC);//GEN-LINE:|698-getter|1|698-postInit
            // write post-init user code here
        }//GEN-BEGIN:|698-getter|2|
        return textField73;
    }
//</editor-fold>//GEN-END:|698-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField74 ">//GEN-BEGIN:|699-getter|0|699-preInit
    /**
     * Returns an initialized instance of textField74 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField74() {
        if (textField74 == null) {//GEN-END:|699-getter|0|699-preInit
            // write pre-init user code here
            String str = getRecordValue(84);
            textField74 = new TextField(LocalizationSupport.getMessage("Hb_7mg"), str, 4, TextField.NUMERIC);//GEN-LINE:|699-getter|1|699-postInit
            // write post-init user code here
        }//GEN-BEGIN:|699-getter|2|
        return textField74;
    }
//</editor-fold>//GEN-END:|699-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FollowingImmunization ">//GEN-BEGIN:|627-getter|0|627-preInit
    /**
     * Returns an initialized instance of FollowingImmunization component.
     *
     * @return the initialized component instance
     */
    public Form getFollowingImmunization() {
        if (FollowingImmunization == null) {//GEN-END:|627-getter|0|627-preInit
            // write pre-init user code here
            FollowingImmunization = new Form("Immu (>16 months) ", new Item[]{getTextField52(), getTextField53(), getTextField54(), getTextField55(), getTextField56(), getTextField57(), getTextField58(), getTextField59()});//GEN-BEGIN:|627-getter|1|627-postInit
            FollowingImmunization.addCommand(getFollowingimmunizationBackCmd());
            FollowingImmunization.addCommand(getFollowingimmunizationCmd());
            FollowingImmunization.setCommandListener(this);//GEN-END:|627-getter|1|627-postInit
            // write post-init user code here
        }//GEN-BEGIN:|627-getter|2|
        return FollowingImmunization;
    }
//</editor-fold>//GEN-END:|627-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: saveCmd ">//GEN-BEGIN:|702-getter|0|702-preInit
    /**
     * Returns an initialized instance of saveCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSaveCmd() {
        if (saveCmd == null) {//GEN-END:|702-getter|0|702-preInit
            // write pre-init user code here
            saveCmd = new Command("Save", Command.OK, 0);//GEN-LINE:|702-getter|1|702-postInit
            // write post-init user code here
        }//GEN-BEGIN:|702-getter|2|
        return saveCmd;
    }
//</editor-fold>//GEN-END:|702-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|705-getter|0|705-preInit
    /**
     * Returns an initialized instance of backCommand component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|705-getter|0|705-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|705-getter|1|705-postInit
            // write post-init user code here
        }//GEN-BEGIN:|705-getter|2|
        return backCommand;
    }
//</editor-fold>//GEN-END:|705-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MenstrualHygieneProgramme ">//GEN-BEGIN:|706-getter|0|706-preInit
    /**
     * Returns an initialized instance of MenstrualHygieneProgramme component.
     *
     * @return the initialized component instance
     */
    public Form getMenstrualHygieneProgramme() {
        if (MenstrualHygieneProgramme == null) {//GEN-END:|706-getter|0|706-preInit
            // write pre-init user code here
            MenstrualHygieneProgramme = new Form("Menstrual Hygiene Programme", new Item[]{getTextField76(), getTextField77(), getTextField78(), getTextField79(), getTextField75()});//GEN-BEGIN:|706-getter|1|706-postInit
            MenstrualHygieneProgramme.addCommand(getMenstrualhygieneprogrammeBackCmd());
            MenstrualHygieneProgramme.addCommand(getMenstrualhygieneprogrammeCmd());
            MenstrualHygieneProgramme.setCommandListener(this);//GEN-END:|706-getter|1|706-postInit
            // write post-init user code here
        }//GEN-BEGIN:|706-getter|2|
        return MenstrualHygieneProgramme;
    }
//</editor-fold>//GEN-END:|706-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField75 ">//GEN-BEGIN:|713-getter|0|713-preInit
    /**
     * Returns an initialized instance of textField75 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField75() {
        if (textField75 == null) {//GEN-END:|713-getter|0|713-preInit
            // write pre-init user code here
            String str = getRecordValue(92);
            textField75 = new TextField(LocalizationSupport.getMessage("Amount_received"), str, 4, TextField.NUMERIC);//GEN-LINE:|713-getter|1|713-postInit
            // write post-init user code here
        }//GEN-BEGIN:|713-getter|2|
        return textField75;
    }
//</editor-fold>//GEN-END:|713-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField76 ">//GEN-BEGIN:|714-getter|0|714-preInit
    /**
     * Returns an initialized instance of textField76 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField76() {
        if (textField76 == null) {//GEN-END:|714-getter|0|714-preInit
            // write pre-init user code here
            String str = getRecordValue(93);
            textField76 = new TextField(LocalizationSupport.getMessage("Napkin_pkts_received"), str, 4, TextField.NUMERIC);//GEN-LINE:|714-getter|1|714-postInit
            // write post-init user code here
        }//GEN-BEGIN:|714-getter|2|
        return textField76;
    }
//</editor-fold>//GEN-END:|714-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField77 ">//GEN-BEGIN:|715-getter|0|715-preInit
    /**
     * Returns an initialized instance of textField77 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField77() {
        if (textField77 == null) {//GEN-END:|715-getter|0|715-preInit
            // write pre-init user code here
            String str = getRecordValue(94);
            textField77 = new TextField(LocalizationSupport.getMessage("Napkin_pkts_distb_sold"), str, 4, TextField.NUMERIC);//GEN-LINE:|715-getter|1|715-postInit
            // write post-init user code here
        }//GEN-BEGIN:|715-getter|2|
        return textField77;
    }
//</editor-fold>//GEN-END:|715-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField78 ">//GEN-BEGIN:|716-getter|0|716-preInit
    /**
     * Returns an initialized instance of textField78 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField78() {
        if (textField78 == null) {//GEN-END:|716-getter|0|716-preInit
            // write pre-init user code here
            String str = getRecordValue(95);
            textField78 = new TextField(LocalizationSupport.getMessage("Adol_girls_receving_napkin"), str, 4, TextField.NUMERIC);//GEN-LINE:|716-getter|1|716-postInit
            // write post-init user code here
        }//GEN-BEGIN:|716-getter|2|
        return textField78;
    }
//</editor-fold>//GEN-END:|716-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField79 ">//GEN-BEGIN:|717-getter|0|717-preInit
    /**
     * Returns an initialized instance of textField79 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField79() {
        if (textField79 == null) {//GEN-END:|717-getter|0|717-preInit
            // write pre-init user code here
            String str = getRecordValue(96);
            textField79 = new TextField(LocalizationSupport.getMessage("IEC_booklets_distributed"), str, 4, TextField.NUMERIC);//GEN-LINE:|717-getter|1|717-postInit
            // write post-init user code here
        }//GEN-BEGIN:|717-getter|2|
        return textField79;
    }
//</editor-fold>//GEN-END:|717-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: menstrualhygieneprogrammeBackCmd ">//GEN-BEGIN:|707-getter|0|707-preInit
    /**
     * Returns an initialized instance of menstrualhygieneprogrammeBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMenstrualhygieneprogrammeBackCmd() {
        if (menstrualhygieneprogrammeBackCmd == null) {//GEN-END:|707-getter|0|707-preInit
            // write pre-init user code here
            menstrualhygieneprogrammeBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|707-getter|1|707-postInit
            // write post-init user code here
        }//GEN-BEGIN:|707-getter|2|
        return menstrualhygieneprogrammeBackCmd;
    }
//</editor-fold>//GEN-END:|707-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: menstrualhygieneprogrammeCmd ">//GEN-BEGIN:|709-getter|0|709-preInit
    /**
     * Returns an initialized instance of menstrualhygieneprogrammeCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMenstrualhygieneprogrammeCmd() {
        if (menstrualhygieneprogrammeCmd == null) {//GEN-END:|709-getter|0|709-preInit
            // write pre-init user code here
            menstrualhygieneprogrammeCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|709-getter|1|709-postInit
            // write post-init user code here
        }//GEN-BEGIN:|709-getter|2|
        return menstrualhygieneprogrammeCmd;
    }
//</editor-fold>//GEN-END:|709-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textFieldHepB0 ">//GEN-BEGIN:|718-getter|0|718-preInit
    /**
     * Returns an initialized instance of textFieldHepB0 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextFieldHepB0() {
        if (textFieldHepB0 == null) {//GEN-END:|718-getter|0|718-preInit
            // write pre-init user code here
            String str = getRecordValue(86);
            textFieldHepB0 = new TextField(LocalizationSupport.getMessage("HepB0"), str, 4, TextField.NUMERIC);//GEN-LINE:|718-getter|1|718-postInit
            // write post-init user code here
        }//GEN-BEGIN:|718-getter|2|
        return textFieldHepB0;
    }
//</editor-fold>//GEN-END:|718-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textMeasles2nddose ">//GEN-BEGIN:|719-getter|0|719-preInit
    /**
     * Returns an initialized instance of textMeasles2nddose component.
     *
     * @return the initialized component instance
     */
    public TextField getTextMeasles2nddose() {
        if (textMeasles2nddose == null) {//GEN-END:|719-getter|0|719-preInit
            // write pre-init user code here
            String str = getRecordValue(87);
            textMeasles2nddose = new TextField(LocalizationSupport.getMessage("Measles2nddose"), str, 4, TextField.NUMERIC);//GEN-LINE:|719-getter|1|719-postInit
            // write post-init user code here
        }//GEN-BEGIN:|719-getter|2|
        return textMeasles2nddose;
    }
//</editor-fold>//GEN-END:|719-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Newborns_Weighed_female ">//GEN-BEGIN:|721-getter|0|721-preInit
    /**
     * Returns an initialized instance of Newborns_Weighed_female component.
     *
     * @return the initialized component instance
     */
    public TextField getNewborns_Weighed_female() {
        if (Newborns_Weighed_female == null) {//GEN-END:|721-getter|0|721-preInit
            // write pre-init user code here
            String str = getRecordValue(89);
            Newborns_Weighed_female = new TextField(LocalizationSupport.getMessage("Newborns_Weighed_female"), str, 4, TextField.NUMERIC);//GEN-LINE:|721-getter|1|721-postInit
            // write post-init user code here
        }//GEN-BEGIN:|721-getter|2|
        return Newborns_Weighed_female;
    }
//</editor-fold>//GEN-END:|721-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Newborns_25kg_female ">//GEN-BEGIN:|722-getter|0|722-preInit
    /**
     * Returns an initialized instance of Newborns_25kg_female component.
     *
     * @return the initialized component instance
     */
    public TextField getNewborns_25kg_female() {
        if (Newborns_25kg_female == null) {//GEN-END:|722-getter|0|722-preInit
            // write pre-init user code here
            String str = getRecordValue(90);
            Newborns_25kg_female = new TextField(LocalizationSupport.getMessage("Newborns_2.5kg_female"), str, 4, TextField.NUMERIC);//GEN-LINE:|722-getter|1|722-postInit
            // write post-init user code here
        }//GEN-BEGIN:|722-getter|2|
        return Newborns_25kg_female;
    }
//</editor-fold>//GEN-END:|722-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Newborns_Breastfed_female ">//GEN-BEGIN:|723-getter|0|723-preInit
    /**
     * Returns an initialized instance of Newborns_Breastfed_female component.
     *
     * @return the initialized component instance
     */
    public TextField getNewborns_Breastfed_female() {
        if (Newborns_Breastfed_female == null) {//GEN-END:|723-getter|0|723-preInit
            // write pre-init user code here
            String str = getRecordValue(91);
            Newborns_Breastfed_female = new TextField(LocalizationSupport.getMessage("Newborns_Breastfed_female"), str, 4, TextField.NUMERIC);//GEN-LINE:|723-getter|1|723-postInit
            // write post-init user code here
        }//GEN-BEGIN:|723-getter|2|
        return Newborns_Breastfed_female;
    }
//</editor-fold>//GEN-END:|723-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textAWWpresent ">//GEN-BEGIN:|720-getter|0|720-preInit
    /**
     * Returns an initialized instance of textAWWpresent component.
     *
     * @return the initialized component instance
     */
    public TextField getTextAWWpresent() {
        if (textAWWpresent == null) {//GEN-END:|720-getter|0|720-preInit
            // write pre-init user code here
            String str = getRecordValue(88);
            textAWWpresent = new TextField(LocalizationSupport.getMessage("AWWpresent"), str, 4, TextField.NUMERIC);//GEN-LINE:|720-getter|1|720-postInit
            // write post-init user code here
        }//GEN-BEGIN:|720-getter|2|
        return textAWWpresent;
    }
//</editor-fold>//GEN-END:|720-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: SubCentreCode ">//GEN-BEGIN:|724-getter|0|724-preInit
    /**
     * Returns an initialized instance of SubCentreCode component.
     *
     * @return the initialized component instance
     */
    public Form getSubCentreCode() {
        if (SubCentreCode == null) {//GEN-END:|724-getter|0|724-preInit
            // write pre-init user code here
            SubCentreCode = new Form("Facility Code", new Item[]{getSubCenterCode()});//GEN-BEGIN:|724-getter|1|724-postInit
            SubCentreCode.addCommand(getSubcentercodeCmd());
            SubCentreCode.addCommand(getSubcentercodeExitCmd());
            SubCentreCode.setCommandListener(this);//GEN-END:|724-getter|1|724-postInit
            // write post-init user code here
        }//GEN-BEGIN:|724-getter|2|
        return SubCentreCode;
    }
//</editor-fold>//GEN-END:|724-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand1 ">//GEN-BEGIN:|725-getter|0|725-preInit
    /**
     * Returns an initialized instance of backCommand1 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {//GEN-END:|725-getter|0|725-preInit
            // write pre-init user code here
            backCommand1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|725-getter|1|725-postInit
            // write post-init user code here
        }//GEN-BEGIN:|725-getter|2|
        return backCommand1;
    }
//</editor-fold>//GEN-END:|725-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentercodeCmd ">//GEN-BEGIN:|727-getter|0|727-preInit
    /**
     * Returns an initialized instance of subcentercodeCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSubcentercodeCmd() {
        if (subcentercodeCmd == null) {//GEN-END:|727-getter|0|727-preInit
            // write pre-init user code here
            subcentercodeCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|727-getter|1|727-postInit
            // write post-init user code here
        }//GEN-BEGIN:|727-getter|2|
        return subcentercodeCmd;
    }
//</editor-fold>//GEN-END:|727-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentercodeExitCmd ">//GEN-BEGIN:|729-getter|0|729-preInit
    /**
     * Returns an initialized instance of subcentercodeExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSubcentercodeExitCmd() {
        if (subcentercodeExitCmd == null) {//GEN-END:|729-getter|0|729-preInit
            // write pre-init user code here
            subcentercodeExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|729-getter|1|729-postInit
            // write post-init user code here
        }//GEN-BEGIN:|729-getter|2|
        return subcentercodeExitCmd;
    }
//</editor-fold>//GEN-END:|729-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthBackCmd ">//GEN-BEGIN:|731-getter|0|731-preInit
    /**
     * Returns an initialized instance of monthBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMonthBackCmd() {
        if (monthBackCmd == null) {//GEN-END:|731-getter|0|731-preInit
            // write pre-init user code here
            monthBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|731-getter|1|731-postInit
            // write post-init user code here
        }//GEN-BEGIN:|731-getter|2|
        return monthBackCmd;
    }
//</editor-fold>//GEN-END:|731-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subCenterCode ">//GEN-BEGIN:|735-getter|0|735-preInit
    /**
     * Returns an initialized instance of subCenterCode component.
     *
     * @return the initialized component instance
     */
    public TextField getSubCenterCode() {
        if (subCenterCode == null) {//GEN-END:|735-getter|0|735-preInit
            // write pre-init user code here
            String str = getRecordValue(6);
            subCenterCode = new TextField(LocalizationSupport.getMessage("subCentreCode"), str, 4, TextField.NUMERIC);//GEN-LINE:|735-getter|1|735-postInit
            // write post-init user code here
        }//GEN-BEGIN:|735-getter|2|
        return subCenterCode;
    }
//</editor-fold>//GEN-END:|735-getter|2|
  //<editor-fold defaultstate="collapsed" desc=" getEmptyFields() ">
    /*
     * Return number of textfields that have negative Value.
     */
    private int getEmptyFields() {
        
        String fullStr = "";
        int j = 0;
        int negativeData = 0;
        
        fullStr = getMyBaseData();
        String[] allContent = split(fullStr, ":");

        for (int i = 0; i < allContent.length; i++) {
            if (allContent[i].equals("")) {
                j++;
            }
            else if (!isIntNumber(allContent[i])){
                negativeData ++;
            }
            // Becase "-" in computer is different with minus (also "-" character in mobile
            // So we have to check like this
            // To treat with "-0" value
            else if (allContent[i] != "00" && allContent[i] != "000" && allContent[i]!= "0000" && allContent[i].length() > 1 && Integer.parseInt(allContent[i]) == 0){
                negativeData ++;
            }
            else if(Integer.parseInt(allContent[i]) < 0){
                negativeData ++;
            }
            else{}
        }

        try {
            if (j != 0) {
                Image missingImg = Image.createImage("/org/hispindia/mobile/images/exclamation.png");
                imgItem.setImage(missingImg);
                if (j == 1) {
                    imgItem.setLabel(j + " field is not filled. Please verify...");
                } else {
                    imgItem.setLabel(j + " fields are not filled. Please verify...");
                }
                imgItem.setLayout(ImageItem.LAYOUT_DEFAULT);
            } else {
                Image completeImg = Image.createImage("/org/hispindia/mobile/images/success.png");
                imgItem.setImage(completeImg);
                imgItem.setLabel("All fields are filled");
                imgItem.setLayout(ImageItem.LAYOUT_DEFAULT);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return negativeData;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" intToByteArray ">
    public static final byte[] intToByteArray(int value) {
        return new byte[]{
                    (byte) (value >>> 24),
                    (byte) (value >>> 16),
                    (byte) (value >>> 8),
                    (byte) value
                };
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" split ">
    //TODO: Replace Vector with Array to improve performance
    private String[] split(String original, String separator) {
        Vector nodes = new Vector();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        return result;
    }
    //</editor-fold>

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    public RecordStore getLastMsgStore() {
        return lastMsgStore;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public boolean isSavedMsg() {
        return savedMsg;
    }

    public void setSavedMsg(boolean savedMsg) {
        this.savedMsg = savedMsg;
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }
    
    private String getMyBaseData(){
    String myData = textField.getString() + ":" +
                textField1.getString() + ":" +
                textField2.getString() + ":" +
                textField3.getString() + ":" +
                textField4.getString() + ":" +
                textField5.getString() + ":" +
                textField6.getString() + ":" +
                textField7.getString() + ":" +
                textField8.getString() + ":" +
                textField9.getString() + ":" +
                textField10.getString() + ":" +
                textField11.getString() + ":" +
                textField12.getString() + ":" +
                textField13.getString() + ":" +
                textField14.getString() + ":" +
                textField15.getString() + ":" +
                textField16.getString() + ":" +
                textField17.getString() + ":" +
                textField18.getString() + ":" +
                textField19.getString() + ":" +
                textField20.getString() + ":" +
                textField21.getString() + ":" +
				Newborns_Weighed_female.getString() + ":" +
                textField22.getString() + ":" +
				Newborns_25kg_female.getString() + ":" +
                textField23.getString() + ":" +
				Newborns_Breastfed_female.getString() + ":" +
                textField24.getString() + ":" +
                textField25.getString() + ":" +
                textField26.getString() + ":" +
                textField27.getString() + ":" +
                textField28.getString() + ":" +
                textField29.getString() + ":" +
                textField30.getString() + ":" +
                textField31.getString() + ":" +
                textField32.getString() + ":" +
                textField33.getString() + ":" +
                textField34.getString() + ":" +
                textField35.getString() + ":" +
                textField36.getString() + ":" +
                textField37.getString() + ":" +
                textField38.getString() + ":" +
                textField39.getString() + ":" +
                textField40.getString() + ":" +
                textField41.getString() + ":" +
                textField42.getString() + ":" +
                textField43.getString() + ":" +
                textField44.getString() + ":" +
                textField45.getString() + ":" +
                textFieldHepB0.getString() + ":" + // add more
                textField46.getString() + ":" +
                textField47.getString() + ":" +
                textField48.getString() + ":" +
                textField49.getString() + ":" +
                textMeasles2nddose.getString() + ":" + // add more
                textField50.getString() + ":" +
                textField51.getString() + ":" +
                textField52.getString() + ":" +
                textField53.getString() + ":" +
                textField54.getString() + ":" +
                textField55.getString() + ":" +
                textField56.getString() + ":" +
                textField57.getString() + ":" +
                textField58.getString() + ":" +
                textField59.getString() + ":" +
                textField60.getString() + ":" +
                textField61.getString() + ":" +
                textField62.getString() + ":" +
                textField63.getString() + ":" +
                textField64.getString() + ":" +
                textAWWpresent.getString() + ":" + // add more
                textField65.getString() + ":" +
                textField66.getString() + ":" +
                textField67.getString() + ":" +
                textField68.getString() + ":" +
                textField69.getString() + ":" +
                textField70.getString() + ":" +
                textField71.getString() + ":" +
                textField72.getString() + ":" +
                textField73.getString() + ":" +
                textField74.getString() + ":" +
                // changed the order on 18/04/2012
                textField76.getString() + ":" +
                textField77.getString() + ":" +
                textField78.getString() + ":" +
                textField79.getString() + ":" +
                textField75.getString();
    return myData;
    }
    private String collectFormData(String monthStr, String freqStr) {
        String[] monthNames = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        int i = 0;
        for (i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equalsIgnoreCase(monthStr.substring(0, 3))) {
                break;
            }
        }
        String monthData = "";
        if (i < 9) {
            // monthData = monthStr.substring(4).substring(2,2) + "-0" + (i + 1) + "-01";
            monthData = monthStr.substring(4).substring(2,4)+ "0" + (i + 1);
        } else {
            // monthData = monthStr.substring(4).substring(2, 2) + "-" + (i + 1) + "-01";
            monthData = monthStr.substring(4).substring(2, 4) +(i + 1);
        }
        
        //for frequency setting of the program
        String freqData = "";
        freqData = freqStr;
        
        String myData = "";
       
        myData = getMyBaseData();
        
        // Start remove 0 data
        String[] myDataTemp = split(myData, ":");
        myData = "";
        int seperateIndex = 0;
        while(seperateIndex < myDataTemp.length - 1){
            // create again myData
            // Check and remove 0 value
            if(myDataTemp[seperateIndex].length() != 0 && Integer.parseInt(myDataTemp[seperateIndex]) != 0){
                myData = myData + myDataTemp[seperateIndex] + ":";
            }
            else{
                if(seperateIndex != 0)
                    myData = myData + ":";
            }
            seperateIndex++;
        }
        if(myDataTemp[seperateIndex].length() != 0 && Integer.parseInt(myDataTemp[myDataTemp.length - 1]) != 0){
                myData = myData + myDataTemp[myDataTemp.length - 1];
        }
        // End remove 0 data
        
        // create myReturnData to content the last data to return
        String myReturnData = "";
        
        int characters = 0;  // THE CHARACTERS OF DATA on one SMS
        seperateIndex = 0; // THE INDEX OF DATA IN STRING [] myDataTemp blow
        
        formID = 1;
        String periodType = "3";
        String subCenter = subCenterCode.getString();
        //return "HP NRHM "+ msgVersion + "!" + formID + "*" + periodType + "?" + monthData + "$" + myData;
        
        // If the message too long, seperate to 2 message
        // lengh of "HP NRHM "+ + subCenter + formID + "*" + monthData + "$" is 19 characters
        if(myData.length() > 141 - 40)
        {
            // assign data into String [] myDataTemp
            myDataTemp = split(myData, ":");
            // reset myData
            myData = "";

            while(seperateIndex < myDataTemp.length){
                // addIndex to content the Index of data. Have to have 2 characters
                String addIndex = Integer.toString(seperateIndex);
                if (addIndex.length()<2)
                    addIndex = "0"+ addIndex;
                
                // reset characters;
                characters = 1;
                // reset myData
                myData = "";
                // if characters <= 138 - 40. BECAUSE seperateIndex + "!" CONTENT 3 CHARACTERS
                while(characters < 139 - 40){
                if(myData.length() + myDataTemp[seperateIndex].length() < 139 - 40){
                        myData += myDataTemp[seperateIndex] + ":";
                        characters = myData.length();
                        seperateIndex ++;
                        if (seperateIndex == myDataTemp.length)
                            break; // break if seperateIndex = myDataTemp.lengh
                }else{
                    break; // break if the cannot append more into myData
                }

                }// end while
                
                // remove ":" at the end of each message
                myData = myData.substring(0, myData.length() -1);

                
                // append data to myReturnData
                myReturnData += "HP NRHM "+ subCenter + formID + "*" + monthData + "$" + addIndex + "?" + myData;
                
            }// end while
        } // end if myData.leght > 145
        else{
            myReturnData = "HP NRHM "+ subCenter + formID + "*" + monthData + "$" + myData;
        }
        
        // return "HP NRHM "+ formID + "*" + monthData + "$" + myData;
        return myReturnData;
    }

    private void sendDataViaSMS(final String scForm1Data) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    
                    int j = 0;
                    for (int i = 0; i < 3; i++) {
                        if (lastMsgStore.getRecord(i + 1) != null) {
                            j++;
                            MessageConnection smsConn = (MessageConnection) Connector.open("sms://" + new String(lastMsgStore.getRecord(i + 1)));
                            TextMessage sms = (TextMessage) smsConn.newMessage(MessageConnection.TEXT_MESSAGE);
                            sms.setPayloadText(scForm1Data);
                            smsConn.send(sms);
                            smsConn.close();
                        }
                    }
                    if (j == 0) {
                        sendMsgLabel.setText("No number to send SMS... Please go to settings and enter a phone number");
                    } else {
                        sendMsgLabel.setText("Message Sent Successfully. You can go back and edit or exit the application now.");
                    }
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void saveDataToRMS(final String monthStr, final String freqString) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    lastMsgStore.setRecord(8, msgVersion.getBytes(), 0, msgVersion.length());
                    lastMsgStore.setRecord(9, intToByteArray(formID), 0, intToByteArray(formID).length);
                    lastMsgStore.setRecord(10, monthStr.getBytes(), 0, monthStr.length()); // record for month
                    lastMsgStore.setRecord(7, freqString.getBytes(), 0, freqString.length()); // record for frequency
                    lastMsgStore.setRecord(6, subCenterCode.getString().getBytes(), 0, subCenterCode.getString().length()); // record for frequency
                    
                    //<editor-fold defaultstate="collapsed" desc="update data into RMS ">
                    lastMsgStore.setRecord(11, textField.getString().getBytes(), 0, textField.getString().length());
                    lastMsgStore.setRecord(85, textField1.getString().getBytes(), 0, textField1.getString().length()); // missing textField1 so I add one more record in RMS
                    lastMsgStore.setRecord(86, textFieldHepB0.getString().getBytes(), 0, textFieldHepB0.getString().length()); // missing textFieldHepB0 so I add one more record in RMS
                    lastMsgStore.setRecord(87, textMeasles2nddose.getString().getBytes(), 0, textMeasles2nddose.getString().length()); // missing textMeasles2nddose so I add one more record in RMS
                    lastMsgStore.setRecord(88, textAWWpresent.getString().getBytes(), 0, textAWWpresent.getString().length()); // missing textAWWpresent so I add one more record in RMS
                    lastMsgStore.setRecord(89, Newborns_Weighed_female.getString().getBytes(), 0, Newborns_Weighed_female.getString().length()); // missing Newborns_Weighed_female so I add one more record in RMS
                    lastMsgStore.setRecord(90, Newborns_25kg_female.getString().getBytes(), 0, Newborns_25kg_female.getString().length()); // missing Newborns_25kg_female so I add one more record in RMS
                    lastMsgStore.setRecord(91, Newborns_Breastfed_female.getString().getBytes(), 0, Newborns_Breastfed_female.getString().length()); // missing Newborns_Breastfed_female so I add one more record in RMS
                    lastMsgStore.setRecord(92, textField75.getString().getBytes(), 0, textField75.getString().length()); // missing textField75 so I add one more record in RMS
                    lastMsgStore.setRecord(93, textField76.getString().getBytes(), 0, textField76.getString().length()); // missing textField76 so I add one more record in RMS
                    lastMsgStore.setRecord(94, textField77.getString().getBytes(), 0, textField77.getString().length()); // missing textField77 so I add one more record in RMS
                    lastMsgStore.setRecord(95, textField78.getString().getBytes(), 0, textField78.getString().length()); // missing textField78 so I add one more record in RMS
                    lastMsgStore.setRecord(96, textField79.getString().getBytes(), 0, textField79.getString().length()); // missing textField79 so I add one more record in RMS
                    lastMsgStore.setRecord(12, textField2.getString().getBytes(), 0, textField2.getString().length());
                    lastMsgStore.setRecord(13, textField3.getString().getBytes(), 0, textField3.getString().length());
                    lastMsgStore.setRecord(14, textField4.getString().getBytes(), 0, textField4.getString().length());
                    lastMsgStore.setRecord(15, textField5.getString().getBytes(), 0, textField5.getString().length());
                    lastMsgStore.setRecord(16, textField6.getString().getBytes(), 0, textField6.getString().length());
                    lastMsgStore.setRecord(17, textField7.getString().getBytes(), 0, textField7.getString().length());
                    lastMsgStore.setRecord(18, textField8.getString().getBytes(), 0, textField8.getString().length());
                    lastMsgStore.setRecord(19, textField9.getString().getBytes(), 0, textField9.getString().length());
                    lastMsgStore.setRecord(20, textField10.getString().getBytes(), 0, textField10.getString().length());
                    lastMsgStore.setRecord(21, textField11.getString().getBytes(), 0, textField11.getString().length());
                    lastMsgStore.setRecord(22, textField12.getString().getBytes(), 0, textField12.getString().length());
                    lastMsgStore.setRecord(23, textField13.getString().getBytes(), 0, textField13.getString().length());
                    lastMsgStore.setRecord(24, textField14.getString().getBytes(), 0, textField14.getString().length());
                    lastMsgStore.setRecord(25, textField15.getString().getBytes(), 0, textField15.getString().length());
                    lastMsgStore.setRecord(26, textField16.getString().getBytes(), 0, textField16.getString().length());
                    lastMsgStore.setRecord(27, textField17.getString().getBytes(), 0, textField17.getString().length());
                    lastMsgStore.setRecord(28, textField18.getString().getBytes(), 0, textField18.getString().length());
                    lastMsgStore.setRecord(29, textField19.getString().getBytes(), 0, textField19.getString().length());
                    lastMsgStore.setRecord(30, textField20.getString().getBytes(), 0, textField20.getString().length());
                    lastMsgStore.setRecord(31, textField21.getString().getBytes(), 0, textField21.getString().length());
                    lastMsgStore.setRecord(32, textField22.getString().getBytes(), 0, textField22.getString().length());
                    lastMsgStore.setRecord(33, textField23.getString().getBytes(), 0, textField23.getString().length());
                    lastMsgStore.setRecord(34, textField24.getString().getBytes(), 0, textField24.getString().length());
                    lastMsgStore.setRecord(35, textField25.getString().getBytes(), 0, textField25.getString().length());
                    lastMsgStore.setRecord(36, textField26.getString().getBytes(), 0, textField26.getString().length());
                    lastMsgStore.setRecord(37, textField27.getString().getBytes(), 0, textField27.getString().length());
                    lastMsgStore.setRecord(38, textField28.getString().getBytes(), 0, textField28.getString().length());
                    lastMsgStore.setRecord(39, textField29.getString().getBytes(), 0, textField29.getString().length());
                    lastMsgStore.setRecord(40, textField30.getString().getBytes(), 0, textField30.getString().length());
                    lastMsgStore.setRecord(41, textField31.getString().getBytes(), 0, textField31.getString().length());
                    lastMsgStore.setRecord(42, textField32.getString().getBytes(), 0, textField32.getString().length());
                    lastMsgStore.setRecord(43, textField33.getString().getBytes(), 0, textField33.getString().length());
                    lastMsgStore.setRecord(44, textField34.getString().getBytes(), 0, textField34.getString().length());
                    lastMsgStore.setRecord(45, textField35.getString().getBytes(), 0, textField35.getString().length());
                    lastMsgStore.setRecord(46, textField36.getString().getBytes(), 0, textField36.getString().length());
                    lastMsgStore.setRecord(47, textField37.getString().getBytes(), 0, textField37.getString().length());
                    lastMsgStore.setRecord(48, textField38.getString().getBytes(), 0, textField38.getString().length());
                    lastMsgStore.setRecord(49, textField39.getString().getBytes(), 0, textField39.getString().length());
                    lastMsgStore.setRecord(50, textField40.getString().getBytes(), 0, textField40.getString().length());
                    lastMsgStore.setRecord(51, textField41.getString().getBytes(), 0, textField41.getString().length());
                    lastMsgStore.setRecord(52, textField42.getString().getBytes(), 0, textField42.getString().length());
                    lastMsgStore.setRecord(53, textField43.getString().getBytes(), 0, textField43.getString().length());
                    lastMsgStore.setRecord(54, textField44.getString().getBytes(), 0, textField44.getString().length());
                    lastMsgStore.setRecord(55, textField45.getString().getBytes(), 0, textField45.getString().length());
                    lastMsgStore.setRecord(56, textField46.getString().getBytes(), 0, textField46.getString().length());
                    lastMsgStore.setRecord(57, textField47.getString().getBytes(), 0, textField47.getString().length());
                    lastMsgStore.setRecord(58, textField48.getString().getBytes(), 0, textField48.getString().length());
                    lastMsgStore.setRecord(59, textField49.getString().getBytes(), 0, textField49.getString().length());
                    lastMsgStore.setRecord(60, textField50.getString().getBytes(), 0, textField50.getString().length());
                    lastMsgStore.setRecord(61, textField51.getString().getBytes(), 0, textField51.getString().length());
                    lastMsgStore.setRecord(62, textField52.getString().getBytes(), 0, textField52.getString().length());
                    lastMsgStore.setRecord(63, textField53.getString().getBytes(), 0, textField53.getString().length());
                    lastMsgStore.setRecord(64, textField54.getString().getBytes(), 0, textField54.getString().length());
                    lastMsgStore.setRecord(65, textField55.getString().getBytes(), 0, textField55.getString().length());
                    lastMsgStore.setRecord(66, textField56.getString().getBytes(), 0, textField56.getString().length());
                    lastMsgStore.setRecord(67, textField57.getString().getBytes(), 0, textField57.getString().length());
                    lastMsgStore.setRecord(68, textField58.getString().getBytes(), 0, textField58.getString().length());
                    lastMsgStore.setRecord(69, textField59.getString().getBytes(), 0, textField59.getString().length());
                    lastMsgStore.setRecord(70, textField60.getString().getBytes(), 0, textField60.getString().length());
                    lastMsgStore.setRecord(71, textField61.getString().getBytes(), 0, textField61.getString().length());
                    lastMsgStore.setRecord(72, textField62.getString().getBytes(), 0, textField62.getString().length());
                    lastMsgStore.setRecord(73, textField63.getString().getBytes(), 0, textField63.getString().length());
                    lastMsgStore.setRecord(74, textField64.getString().getBytes(), 0, textField64.getString().length());
                    lastMsgStore.setRecord(75, textField65.getString().getBytes(), 0, textField65.getString().length());
                    lastMsgStore.setRecord(76, textField66.getString().getBytes(), 0, textField66.getString().length());
                    lastMsgStore.setRecord(77, textField67.getString().getBytes(), 0, textField67.getString().length());
                    lastMsgStore.setRecord(78, textField68.getString().getBytes(), 0, textField68.getString().length());
                    lastMsgStore.setRecord(79, textField69.getString().getBytes(), 0, textField69.getString().length());
                    lastMsgStore.setRecord(80, textField70.getString().getBytes(), 0, textField70.getString().length());
                    lastMsgStore.setRecord(81, textField71.getString().getBytes(), 0, textField71.getString().length());
                    lastMsgStore.setRecord(82, textField72.getString().getBytes(), 0, textField72.getString().length());
                    lastMsgStore.setRecord(83, textField73.getString().getBytes(), 0, textField73.getString().length());
                    lastMsgStore.setRecord(84, textField74.getString().getBytes(), 0, textField74.getString().length());
                    
                    //</editor-fold>


                    lastMsgStore.setRecord(4, "true".getBytes(), 0, "true".getBytes().length); //record for edit or not edit option (on load page)
                    savedMsg = true;
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        }).start();
    }
    
    
    /*
     * Author : Thai Chuong
     * This function use to get value from RecordStore
     * return String : the value of the record
     * int recordNumber: the numberic of the record in RecordStore
     */
    public String getRecordValue (int recordNumber){
        String str = "";
            if (editingLastReport) {
                try {
                    byte[] myValue = lastMsgStore.getRecord(recordNumber);
                    //fixed the problem that the textField was defined "numeric" still accept "-" character
                    if (myValue != null) {
                        str = new String(myValue);
                        if(!isIntNumber(str)){str = "";} // Check if the value is not a integer
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            return str;
    }
    
    /*
     * Author : Thai Chuong
     * This function use check a String is Integer or not
     * return boolean : true/false
     * String num: The String value need to check
     */
    private boolean isIntNumber(String num){
        try{
        Integer.parseInt(num);
        } catch(NumberFormatException nfe) {
            return false;
        }
            return true;
    }
}