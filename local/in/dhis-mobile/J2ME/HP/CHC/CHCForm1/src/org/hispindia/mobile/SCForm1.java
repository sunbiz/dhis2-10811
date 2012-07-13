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
    private Form sendPage;
    private StringItem sendMsgLabel;
    private Form monthPage;
    private ChoiceGroup freqGroup;
    private ChoiceGroup monthChoice;
    private SplashScreen splashScreen;
    private Form loadPage;
    private StringItem questionLabel;
    private ChoiceGroup lastChoice;
    private ImageItem questionImage;
    private Form settingsPage;
    private TextField phone3Num;
    private TextField phone1Num;
    private TextField phone2Num;
    private Form SubCentreCode;
    private TextField subCenterCode;
    private Form InPatientHeadCountatmidnight;
    private TextField textField46;
    private StringItem stringItem10;
    private Form PSDeaths;
    private TextField textField44;
    private TextField textField45;
    private StringItem stringItem9;
    private Form OthersDenlOphAYUSHetc;
    private TextField textField49;
    private TextField textField50;
    private TextField textField51;
    private StringItem stringItem12;
    private Form OperationTheatre;
    private TextField textField48;
    private TextField textField47;
    private StringItem stringItem11;
    private Form Widaltestsconducted;
    private TextField textField11;
    private StringItem stringItem5;
    private TextField textField58;
    private Form InPatientAdmissions;
    private TextField textField41;
    private TextField textField40;
    private TextField textField43;
    private TextField textField42;
    private StringItem stringItem8;
    private Form PatientServices;
    private TextField textField37;
    private TextField textField35;
    private Form LaboratoryTesting;
    private TextField textField52;
    private TextField textField53;
    private StringItem stringItem2;
    private Form Childhooddiseases05yrs;
    private TextField textField24;
    private TextField textField25;
    private TextField textField22;
    private TextField textField23;
    private TextField textField20;
    private TextField textField21;
    private TextField textField8;
    private TextField textField9;
    private TextField textField7;
    private Form BlindnessControlProgramme;
    private TextField textField29;
    private TextField textField28;
    private TextField textField31;
    private TextField textField30;
    private TextField textField26;
    private TextField textField27;
    private Form Malariatestsconducted;
    private TextField textField64;
    private TextField textField63;
    private TextField textField62;
    private StringItem stringItem4;
    private Form VDRLtestsconducted;
    private TextField textField34;
    private TextField textField32;
    private TextField textField33;
    private StringItem stringItem3;
    private TextField textField61;
    private TextField textField60;
    private TextField textField59;
    private Form HIVtestsconducted;
    private StringItem stringItem1;
    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
    private TextField textField4;
    private TextField textField5;
    private TextField textField6;
    private Form Outputpatient;
    private TextField textField;
    private StringItem stringItem;
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
    private Command Csectiondeliveriesokcmd;
    private Command sendExitCmd;
    private Command Complicatedpregnanciesokcmd;
    private Command Complicatedpregnanciesackcmd;
    private Command ANCServicesbackcmd;
    private Command Csectiondeliveriesbackcmd;
    private Command ANCServicesokcmd;
    private Command RTISTICasesTreatedbackcmd;
    private Command MTPokCommand;
    private Command RTISTICasesTreatedokcmd;
    private Command itemCommand;
    private Command PostNatalCareokcmd;
    private Command PostNatalCarebackcmd;
    private Command MTPbackcmd;
    private Command backCommand;
    private Command menstrualhygieneprogrammeBackCmd;
    private Command loadExitCmd;
    private Command menstrualhygieneprogrammeCmd;
    private Command loadCmd;
    private Command sendSettingsCmd;
    private Command subcentercodeExitCmd;
    private Command monthBackCmd;
    private Command settingsBackCmd;
    private Command settingsCmd;
    private Command backCommand1;
    private Command subcentercodeCmd;
    private Command Qualityinsterilizationservicesokcmd;
    private Command Qualityinsterilizationservicesbackcmd;
    private Command psDeathsokcmd;
    private Command InPatientHeadCountatmidnightbackcmd;
    private Command PSDeathsbackcmd;
    private Command ANCservicesokcmd;
    private Command ANCservicesbackcmd;
    private Command backCommand2;
    private Command okCommand;
    private Command InPatientAdmissionsbackcmd;
    private Command PatientServicesokcmd;
    private Command PatientServicesbackcmd;
    private Command LaboratoryTestingokcmd;
    private Command LaboratoryTestingbackcmd;
    private Command InPatientAdmissionsokcmd;
    private Command FamilyPlanningokcmd;
    private Command FamilyPlanningbackcmd;
    private Command BlindnessControlProgrammeokcmd;
    private Command BlindnessControlProgrammebackcmd;
    private Command Childhooddiseases05yrsokcmd;
    private Command Childhooddiseases05yrsbackcmd;
    private Command VDRLtestsconductedokcmd;
    private Command VDRLtestsconductedbackcmd;
    private Command Malariatestsconductedbackcmd;
    private Command Malariatestsconductedokcmd;
    private Command OperationTheatrebackcmd;
    private Command InPatientHeadCountatmidnightokcmd;
    private Command Othersetcbackcmd;
    private Command OperationTheatreokcmd;
    private Command Widaltestsconductedbackcmd;
    private Command Othersetcokcmd;
    private Command Widaltestsconductedokcmd;
    private Command sendGPRSCommand;
    private Command HIVtestsconductedbackCmd;
    private Command monthExitCmd;
    private Command HIVtestsconductedCmd;
    private Command OutPatientbackCmd;
    private Command OutputPatientCmd;
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
                for (int i = 0; i < 59; i++) {
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
        if (displayable == BlindnessControlProgramme) {//GEN-BEGIN:|7-commandAction|1|781-preAction
            if (command == BlindnessControlProgrammebackcmd) {//GEN-END:|7-commandAction|1|781-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhooddiseases05yrs());//GEN-LINE:|7-commandAction|2|781-postAction
                // write post-action user code here
            } else if (command == BlindnessControlProgrammeokcmd) {//GEN-LINE:|7-commandAction|3|783-preAction
                // write pre-action user code here
                switchDisplayable(null, getPatientServices());//GEN-LINE:|7-commandAction|4|783-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|777-preAction
        } else if (displayable == Childhooddiseases05yrs) {
            if (command == Childhooddiseases05yrsbackcmd) {//GEN-END:|7-commandAction|5|777-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|6|777-postAction
                // write post-action user code here
            } else if (command == Childhooddiseases05yrsokcmd) {//GEN-LINE:|7-commandAction|7|779-preAction
                // write pre-action user code here
                switchDisplayable(null, getBlindnessControlProgramme());//GEN-LINE:|7-commandAction|8|779-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|1005-preAction
        } else if (displayable == HIVtestsconducted) {
            if (command == HIVtestsconductedCmd) {//GEN-END:|7-commandAction|9|1005-preAction
                // write pre-action user code here
                switchDisplayable(null, getWidaltestsconducted());//GEN-LINE:|7-commandAction|10|1005-postAction
                // write post-action user code here
            } else if (command == HIVtestsconductedbackCmd) {//GEN-LINE:|7-commandAction|11|1003-preAction
                // write pre-action user code here
                switchDisplayable(null, getLaboratoryTesting());//GEN-LINE:|7-commandAction|12|1003-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|791-preAction
        } else if (displayable == InPatientAdmissions) {
            if (command == InPatientAdmissionsbackcmd) {//GEN-END:|7-commandAction|13|791-preAction
                // write pre-action user code here
                switchDisplayable(null, getPatientServices());//GEN-LINE:|7-commandAction|14|791-postAction
                // write post-action user code here
            } else if (command == InPatientAdmissionsokcmd) {//GEN-LINE:|7-commandAction|15|793-preAction
                // write pre-action user code here
                switchDisplayable(null, getPSDeaths());//GEN-LINE:|7-commandAction|16|793-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|832-preAction
        } else if (displayable == InPatientHeadCountatmidnight) {
            if (command == InPatientHeadCountatmidnightbackcmd) {//GEN-END:|7-commandAction|17|832-preAction
                // write pre-action user code here
                switchDisplayable(null, getPSDeaths());//GEN-LINE:|7-commandAction|18|832-postAction
                // write post-action user code here
            } else if (command == InPatientHeadCountatmidnightokcmd) {//GEN-LINE:|7-commandAction|19|834-preAction
                // write pre-action user code here
                switchDisplayable(null, getOutputpatient());//GEN-LINE:|7-commandAction|20|834-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|796-preAction
        } else if (displayable == LaboratoryTesting) {
            if (command == LaboratoryTestingbackcmd) {//GEN-END:|7-commandAction|21|796-preAction
                // write pre-action user code here
                switchDisplayable(null, getOthersDenlOphAYUSHetc());//GEN-LINE:|7-commandAction|22|796-postAction
                // write post-action user code here
            } else if (command == LaboratoryTestingokcmd) {//GEN-LINE:|7-commandAction|23|798-preAction
                // write pre-action user code here
                switchDisplayable(null, getHIVtestsconducted());//GEN-LINE:|7-commandAction|24|798-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|855-preAction
        } else if (displayable == Malariatestsconducted) {
            if (command == Malariatestsconductedbackcmd) {//GEN-END:|7-commandAction|25|855-preAction
                // write pre-action user code here
                switchDisplayable(null, getVDRLtestsconducted());//GEN-LINE:|7-commandAction|26|855-postAction
                // write post-action user code here
            } else if (command == Malariatestsconductedokcmd) {//GEN-LINE:|7-commandAction|27|857-preAction
                // write pre-action user code here
                int negativeTextField = getEmptyFields();
                if(negativeTextField > 0){
                    if(negativeTextField > 1){
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data elements are negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,Malariatestsconducted);
                    }
                    else {
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data element is negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,Malariatestsconducted);
                    }
                }
                else{
                    switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|28|857-postAction
                }// write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|836-preAction
        } else if (displayable == OperationTheatre) {
            if (command == OperationTheatrebackcmd) {//GEN-END:|7-commandAction|29|836-preAction
                // write pre-action user code here
                switchDisplayable(null, getOutputpatient());//GEN-LINE:|7-commandAction|30|836-postAction
                // write post-action user code here
            } else if (command == OperationTheatreokcmd) {//GEN-LINE:|7-commandAction|31|838-preAction
                // write pre-action user code here
                switchDisplayable(null, getOthersDenlOphAYUSHetc());//GEN-LINE:|7-commandAction|32|838-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|840-preAction
        } else if (displayable == OthersDenlOphAYUSHetc) {
            if (command == Othersetcbackcmd) {//GEN-END:|7-commandAction|33|840-preAction
                // write pre-action user code here
                switchDisplayable(null, getOperationTheatre());//GEN-LINE:|7-commandAction|34|840-postAction
                // write post-action user code here
            } else if (command == Othersetcokcmd) {//GEN-LINE:|7-commandAction|35|842-preAction
                // write pre-action user code here
                switchDisplayable(null, getLaboratoryTesting());//GEN-LINE:|7-commandAction|36|842-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|994-preAction
        } else if (displayable == Outputpatient) {
            if (command == OutPatientbackCmd) {//GEN-END:|7-commandAction|37|994-preAction
                // write pre-action user code here
                switchDisplayable(null, getInPatientHeadCountatmidnight());//GEN-LINE:|7-commandAction|38|994-postAction
                // write post-action user code here
            } else if (command == OutputPatientCmd) {//GEN-LINE:|7-commandAction|39|996-preAction
                // write pre-action user code here
                switchDisplayable(null, getOperationTheatre());//GEN-LINE:|7-commandAction|40|996-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|41|828-preAction
        } else if (displayable == PSDeaths) {
            if (command == PSDeathsbackcmd) {//GEN-END:|7-commandAction|41|828-preAction
                // write pre-action user code here
                switchDisplayable(null, getInPatientAdmissions());//GEN-LINE:|7-commandAction|42|828-postAction
                // write post-action user code here
            } else if (command == psDeathsokcmd) {//GEN-LINE:|7-commandAction|43|830-preAction
                // write pre-action user code here
                switchDisplayable(null, getInPatientHeadCountatmidnight());//GEN-LINE:|7-commandAction|44|830-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|45|786-preAction
        } else if (displayable == PatientServices) {
            if (command == PatientServicesbackcmd) {//GEN-END:|7-commandAction|45|786-preAction
                // write pre-action user code here
                switchDisplayable(null, getBlindnessControlProgramme());//GEN-LINE:|7-commandAction|46|786-postAction
                // write post-action user code here
            } else if (command == PatientServicesokcmd) {//GEN-LINE:|7-commandAction|47|788-preAction
                // write pre-action user code here
                switchDisplayable(null, getInPatientAdmissions());//GEN-LINE:|7-commandAction|48|788-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|49|728-preAction
        } else if (displayable == SubCentreCode) {
            if (command == subcentercodeCmd) {//GEN-END:|7-commandAction|49|728-preAction
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
                    switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|50|728-postAction
                }// write post-action user code here
            } else if (command == subcentercodeExitCmd) {//GEN-LINE:|7-commandAction|51|730-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|52|730-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|53|850-preAction
        } else if (displayable == VDRLtestsconducted) {
            if (command == VDRLtestsconductedbackcmd) {//GEN-END:|7-commandAction|53|850-preAction
                // write pre-action user code here
                switchDisplayable(null, getWidaltestsconducted());//GEN-LINE:|7-commandAction|54|850-postAction
                // write post-action user code here
            } else if (command == VDRLtestsconductedokcmd) {//GEN-LINE:|7-commandAction|55|852-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalariatestsconducted());//GEN-LINE:|7-commandAction|56|852-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|57|844-preAction
        } else if (displayable == Widaltestsconducted) {
            if (command == Widaltestsconductedbackcmd) {//GEN-END:|7-commandAction|57|844-preAction
                // write pre-action user code here
                switchDisplayable(null, getHIVtestsconducted());//GEN-LINE:|7-commandAction|58|844-postAction
                // write post-action user code here
            } else if (command == Widaltestsconductedokcmd) {//GEN-LINE:|7-commandAction|59|846-preAction
                // write pre-action user code here
                switchDisplayable(null, getVDRLtestsconducted());//GEN-LINE:|7-commandAction|60|846-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|61|226-preAction
        } else if (displayable == loadPage) {
            if (command == loadCmd) {//GEN-END:|7-commandAction|61|226-preAction
                int lastSelected = lastChoice.getSelectedIndex();
                if (lastSelected == 0) {
                    editingLastReport = true;
                } else {
                    editingLastReport = false;
                }
                switchDisplayable(null, getSubCentreCode());//GEN-LINE:|7-commandAction|62|226-postAction
                // write post-action user code here
            } else if (command == loadExitCmd) {//GEN-LINE:|7-commandAction|63|228-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|64|228-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|65|732-preAction
        } else if (displayable == monthPage) {
            if (command == monthBackCmd) {//GEN-END:|7-commandAction|65|732-preAction
                // write pre-action user code here
                switchDisplayable(null, getSubCentreCode());//GEN-LINE:|7-commandAction|66|732-postAction
                // write post-action user code here
            } else if (command == monthCmd) {//GEN-LINE:|7-commandAction|67|180-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhooddiseases05yrs());//GEN-LINE:|7-commandAction|68|180-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|69|703-preAction
        } else if (displayable == sendPage) {
            if (command == saveCmd) {//GEN-END:|7-commandAction|69|703-preAction
                // write pre-action user code here
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());
                saveDataToRMS(monthStr, freqStr);
                Alert myAlert = new Alert("Save success","Your data has been saved!",null,AlertType.INFO);
                myAlert.setTimeout(2000);
                Display.getDisplay(this).setCurrent(myAlert,sendPage);
//GEN-LINE:|7-commandAction|70|703-postAction
                // write post-action user code here
            } else if (command == sendBackCmd) {//GEN-LINE:|7-commandAction|71|171-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalariatestsconducted());//GEN-LINE:|7-commandAction|72|171-postAction
                // write post-action user code here
            } else if (command == sendCmd) {//GEN-LINE:|7-commandAction|73|169-preAction
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

//GEN-LINE:|7-commandAction|74|169-postAction
                // write post-action user code here
            } else if (command == sendExitCmd) {//GEN-LINE:|7-commandAction|75|207-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|76|207-postAction
                // write post-action user code here
            } else if (command == sendSettingsCmd) {//GEN-LINE:|7-commandAction|77|255-preAction
                // write pre-action user code here
                switchDisplayable(null, getSettingsPage());//GEN-LINE:|7-commandAction|78|255-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|79|246-preAction
        } else if (displayable == settingsPage) {
            if (command == settingsBackCmd) {//GEN-END:|7-commandAction|79|246-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|80|246-postAction
                // write post-action user code here
            } else if (command == settingsCmd) {//GEN-LINE:|7-commandAction|81|243-preAction
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
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|82|243-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|83|203-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|83|203-preAction
                if (savedMsg == false) {
                    switchDisplayable(null, getSubCentreCode());
                } else {
                    switchDisplayable(null, getLoadPage());//GEN-LINE:|7-commandAction|84|203-postAction
                }
            }//GEN-BEGIN:|7-commandAction|85|7-postCommandAction
        }//GEN-END:|7-commandAction|85|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|86|
//</editor-fold>//GEN-END:|7-commandAction|86|







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













































    
    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Measles 2nd dose ">
    /**
     * Returns an initialized instance of Measles 2nd dose component.
     *
     * @return the initialized component instance
     */
    
//</editor-fold>



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

















//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Childhooddiseases05yrs ">//GEN-BEGIN:|774-getter|0|774-preInit
    /**
     * Returns an initialized instance of Childhooddiseases05yrs component.
     *
     * @return the initialized component instance
     */
    public Form getChildhooddiseases05yrs() {
        if (Childhooddiseases05yrs == null) {//GEN-END:|774-getter|0|774-preInit
            // write pre-init user code here
            Childhooddiseases05yrs = new Form("Childhood Diseases (0-5yrs)", new Item[]{getTextField20(), getTextField21(), getTextField22(), getTextField23(), getTextField24(), getTextField7(), getTextField8(), getTextField9(), getTextField25()});//GEN-BEGIN:|774-getter|1|774-postInit
            Childhooddiseases05yrs.addCommand(getChildhooddiseases05yrsbackcmd());
            Childhooddiseases05yrs.addCommand(getChildhooddiseases05yrsokcmd());
            Childhooddiseases05yrs.setCommandListener(this);//GEN-END:|774-getter|1|774-postInit
            // write post-init user code here
        }//GEN-BEGIN:|774-getter|2|
        return Childhooddiseases05yrs;
    }
//</editor-fold>//GEN-END:|774-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: BlindnessControlProgramme ">//GEN-BEGIN:|775-getter|0|775-preInit
    /**
     * Returns an initialized instance of BlindnessControlProgramme component.
     *
     * @return the initialized component instance
     */
    public Form getBlindnessControlProgramme() {
        if (BlindnessControlProgramme == null) {//GEN-END:|775-getter|0|775-preInit
            // write pre-init user code here
            BlindnessControlProgramme = new Form("Blindness Control Programme", new Item[]{getTextField26(), getTextField27(), getTextField28(), getTextField29(), getTextField30(), getTextField31()});//GEN-BEGIN:|775-getter|1|775-postInit
            BlindnessControlProgramme.addCommand(getBlindnessControlProgrammebackcmd());
            BlindnessControlProgramme.addCommand(getBlindnessControlProgrammeokcmd());
            BlindnessControlProgramme.setCommandListener(this);//GEN-END:|775-getter|1|775-postInit
            // write post-init user code here
        }//GEN-BEGIN:|775-getter|2|
        return BlindnessControlProgramme;
    }
//</editor-fold>//GEN-END:|775-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PatientServices ">//GEN-BEGIN:|784-getter|0|784-preInit
    /**
     * Returns an initialized instance of PatientServices component.
     *
     * @return the initialized component instance
     */
    public Form getPatientServices() {
        if (PatientServices == null) {//GEN-END:|784-getter|0|784-preInit
            // write pre-init user code here
            PatientServices = new Form("Patient Services", new Item[]{getTextField35(), getTextField37()});//GEN-BEGIN:|784-getter|1|784-postInit
            PatientServices.addCommand(getPatientServicesbackcmd());
            PatientServices.addCommand(getPatientServicesokcmd());
            PatientServices.setCommandListener(this);//GEN-END:|784-getter|1|784-postInit
            // write post-init user code here
        }//GEN-BEGIN:|784-getter|2|
        return PatientServices;
    }
//</editor-fold>//GEN-END:|784-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientAdmissions ">//GEN-BEGIN:|789-getter|0|789-preInit
    /**
     * Returns an initialized instance of InPatientAdmissions component.
     *
     * @return the initialized component instance
     */
    public Form getInPatientAdmissions() {
        if (InPatientAdmissions == null) {//GEN-END:|789-getter|0|789-preInit
            // write pre-init user code here
            InPatientAdmissions = new Form("Patient Services ", new Item[]{getStringItem8(), getTextField40(), getTextField41(), getTextField42(), getTextField43()});//GEN-BEGIN:|789-getter|1|789-postInit
            InPatientAdmissions.addCommand(getInPatientAdmissionsbackcmd());
            InPatientAdmissions.addCommand(getInPatientAdmissionsokcmd());
            InPatientAdmissions.setCommandListener(this);//GEN-END:|789-getter|1|789-postInit
            // write post-init user code here
        }//GEN-BEGIN:|789-getter|2|
        return InPatientAdmissions;
    }
//</editor-fold>//GEN-END:|789-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: LaboratoryTesting ">//GEN-BEGIN:|794-getter|0|794-preInit
    /**
     * Returns an initialized instance of LaboratoryTesting component.
     *
     * @return the initialized component instance
     */
    public Form getLaboratoryTesting() {
        if (LaboratoryTesting == null) {//GEN-END:|794-getter|0|794-preInit
            // write pre-init user code here
            LaboratoryTesting = new Form("Laboratory Testing", new Item[]{getStringItem2(), getTextField52(), getTextField53()});//GEN-BEGIN:|794-getter|1|794-postInit
            LaboratoryTesting.addCommand(getLaboratoryTestingbackcmd());
            LaboratoryTesting.addCommand(getLaboratoryTestingokcmd());
            LaboratoryTesting.setCommandListener(this);//GEN-END:|794-getter|1|794-postInit
            // write post-init user code here
        }//GEN-BEGIN:|794-getter|2|
        return LaboratoryTesting;
    }
//</editor-fold>//GEN-END:|794-getter|2|







//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANCServicesbackcmd ">//GEN-BEGIN:|738-getter|0|738-preInit
    /**
     * Returns an initialized instance of ANCServicesbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getANCServicesbackcmd() {
        if (ANCServicesbackcmd == null) {//GEN-END:|738-getter|0|738-preInit
            // write pre-init user code here
            ANCServicesbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|738-getter|1|738-postInit
            // write post-init user code here
        }//GEN-BEGIN:|738-getter|2|
        return ANCServicesbackcmd;
    }
//</editor-fold>//GEN-END:|738-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANCServicesokcmd ">//GEN-BEGIN:|740-getter|0|740-preInit
    /**
     * Returns an initialized instance of ANCServicesokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getANCServicesokcmd() {
        if (ANCServicesokcmd == null) {//GEN-END:|740-getter|0|740-preInit
            // write pre-init user code here
            ANCServicesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|740-getter|1|740-postInit
            // write post-init user code here
        }//GEN-BEGIN:|740-getter|2|
        return ANCServicesokcmd;
    }
//</editor-fold>//GEN-END:|740-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Csectiondeliveriesbackcmd ">//GEN-BEGIN:|743-getter|0|743-preInit
    /**
     * Returns an initialized instance of Csectiondeliveriesbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getCsectiondeliveriesbackcmd() {
        if (Csectiondeliveriesbackcmd == null) {//GEN-END:|743-getter|0|743-preInit
            // write pre-init user code here
            Csectiondeliveriesbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|743-getter|1|743-postInit
            // write post-init user code here
        }//GEN-BEGIN:|743-getter|2|
        return Csectiondeliveriesbackcmd;
    }
//</editor-fold>//GEN-END:|743-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Csectiondeliveriesokcmd ">//GEN-BEGIN:|745-getter|0|745-preInit
    /**
     * Returns an initialized instance of Csectiondeliveriesokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getCsectiondeliveriesokcmd() {
        if (Csectiondeliveriesokcmd == null) {//GEN-END:|745-getter|0|745-preInit
            // write pre-init user code here
            Csectiondeliveriesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|745-getter|1|745-postInit
            // write post-init user code here
        }//GEN-BEGIN:|745-getter|2|
        return Csectiondeliveriesokcmd;
    }
//</editor-fold>//GEN-END:|745-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Complicatedpregnanciesackcmd ">//GEN-BEGIN:|748-getter|0|748-preInit
    /**
     * Returns an initialized instance of Complicatedpregnanciesackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getComplicatedpregnanciesackcmd() {
        if (Complicatedpregnanciesackcmd == null) {//GEN-END:|748-getter|0|748-preInit
            // write pre-init user code here
            Complicatedpregnanciesackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|748-getter|1|748-postInit
            // write post-init user code here
        }//GEN-BEGIN:|748-getter|2|
        return Complicatedpregnanciesackcmd;
    }
//</editor-fold>//GEN-END:|748-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Complicatedpregnanciesokcmd ">//GEN-BEGIN:|750-getter|0|750-preInit
    /**
     * Returns an initialized instance of Complicatedpregnanciesokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getComplicatedpregnanciesokcmd() {
        if (Complicatedpregnanciesokcmd == null) {//GEN-END:|750-getter|0|750-preInit
            // write pre-init user code here
            Complicatedpregnanciesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|750-getter|1|750-postInit
            // write post-init user code here
        }//GEN-BEGIN:|750-getter|2|
        return Complicatedpregnanciesokcmd;
    }
//</editor-fold>//GEN-END:|750-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PostNatalCarebackcmd ">//GEN-BEGIN:|753-getter|0|753-preInit
    /**
     * Returns an initialized instance of PostNatalCarebackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPostNatalCarebackcmd() {
        if (PostNatalCarebackcmd == null) {//GEN-END:|753-getter|0|753-preInit
            // write pre-init user code here
            PostNatalCarebackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|753-getter|1|753-postInit
            // write post-init user code here
        }//GEN-BEGIN:|753-getter|2|
        return PostNatalCarebackcmd;
    }
//</editor-fold>//GEN-END:|753-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PostNatalCareokcmd ">//GEN-BEGIN:|755-getter|0|755-preInit
    /**
     * Returns an initialized instance of PostNatalCareokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPostNatalCareokcmd() {
        if (PostNatalCareokcmd == null) {//GEN-END:|755-getter|0|755-preInit
            // write pre-init user code here
            PostNatalCareokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|755-getter|1|755-postInit
            // write post-init user code here
        }//GEN-BEGIN:|755-getter|2|
        return PostNatalCareokcmd;
    }
//</editor-fold>//GEN-END:|755-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MTPbackcmd ">//GEN-BEGIN:|758-getter|0|758-preInit
    /**
     * Returns an initialized instance of MTPbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getMTPbackcmd() {
        if (MTPbackcmd == null) {//GEN-END:|758-getter|0|758-preInit
            // write pre-init user code here
            MTPbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|758-getter|1|758-postInit
            // write post-init user code here
        }//GEN-BEGIN:|758-getter|2|
        return MTPbackcmd;
    }
//</editor-fold>//GEN-END:|758-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MTPokCommand ">//GEN-BEGIN:|760-getter|0|760-preInit
    /**
     * Returns an initialized instance of MTPokCommand component.
     *
     * @return the initialized component instance
     */
    public Command getMTPokCommand() {
        if (MTPokCommand == null) {//GEN-END:|760-getter|0|760-preInit
            // write pre-init user code here
            MTPokCommand = new Command("Next", Command.OK, 0);//GEN-LINE:|760-getter|1|760-postInit
            // write post-init user code here
        }//GEN-BEGIN:|760-getter|2|
        return MTPokCommand;
    }
//</editor-fold>//GEN-END:|760-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: RTISTICasesTreatedbackcmd ">//GEN-BEGIN:|763-getter|0|763-preInit
    /**
     * Returns an initialized instance of RTISTICasesTreatedbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getRTISTICasesTreatedbackcmd() {
        if (RTISTICasesTreatedbackcmd == null) {//GEN-END:|763-getter|0|763-preInit
            // write pre-init user code here
            RTISTICasesTreatedbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|763-getter|1|763-postInit
            // write post-init user code here
        }//GEN-BEGIN:|763-getter|2|
        return RTISTICasesTreatedbackcmd;
    }
//</editor-fold>//GEN-END:|763-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand ">//GEN-BEGIN:|765-getter|0|765-preInit
    /**
     * Returns an initialized instance of itemCommand component.
     *
     * @return the initialized component instance
     */
    public Command getItemCommand() {
        if (itemCommand == null) {//GEN-END:|765-getter|0|765-preInit
            // write pre-init user code here
            itemCommand = new Command("Item", Command.ITEM, 0);//GEN-LINE:|765-getter|1|765-postInit
            // write post-init user code here
        }//GEN-BEGIN:|765-getter|2|
        return itemCommand;
    }
//</editor-fold>//GEN-END:|765-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: RTISTICasesTreatedokcmd ">//GEN-BEGIN:|767-getter|0|767-preInit
    /**
     * Returns an initialized instance of RTISTICasesTreatedokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getRTISTICasesTreatedokcmd() {
        if (RTISTICasesTreatedokcmd == null) {//GEN-END:|767-getter|0|767-preInit
            // write pre-init user code here
            RTISTICasesTreatedokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|767-getter|1|767-postInit
            // write post-init user code here
        }//GEN-BEGIN:|767-getter|2|
        return RTISTICasesTreatedokcmd;
    }
//</editor-fold>//GEN-END:|767-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FamilyPlanningbackcmd ">//GEN-BEGIN:|770-getter|0|770-preInit
    /**
     * Returns an initialized instance of FamilyPlanningbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getFamilyPlanningbackcmd() {
        if (FamilyPlanningbackcmd == null) {//GEN-END:|770-getter|0|770-preInit
            // write pre-init user code here
            FamilyPlanningbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|770-getter|1|770-postInit
            // write post-init user code here
        }//GEN-BEGIN:|770-getter|2|
        return FamilyPlanningbackcmd;
    }
//</editor-fold>//GEN-END:|770-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FamilyPlanningokcmd ">//GEN-BEGIN:|772-getter|0|772-preInit
    /**
     * Returns an initialized instance of FamilyPlanningokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getFamilyPlanningokcmd() {
        if (FamilyPlanningokcmd == null) {//GEN-END:|772-getter|0|772-preInit
            // write pre-init user code here
            FamilyPlanningokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|772-getter|1|772-postInit
            // write post-init user code here
        }//GEN-BEGIN:|772-getter|2|
        return FamilyPlanningokcmd;
    }
//</editor-fold>//GEN-END:|772-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Childhooddiseases05yrsbackcmd ">//GEN-BEGIN:|776-getter|0|776-preInit
    /**
     * Returns an initialized instance of Childhooddiseases05yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getChildhooddiseases05yrsbackcmd() {
        if (Childhooddiseases05yrsbackcmd == null) {//GEN-END:|776-getter|0|776-preInit
            // write pre-init user code here
            Childhooddiseases05yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|776-getter|1|776-postInit
            // write post-init user code here
        }//GEN-BEGIN:|776-getter|2|
        return Childhooddiseases05yrsbackcmd;
    }
//</editor-fold>//GEN-END:|776-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Childhooddiseases05yrsokcmd ">//GEN-BEGIN:|778-getter|0|778-preInit
    /**
     * Returns an initialized instance of Childhooddiseases05yrsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getChildhooddiseases05yrsokcmd() {
        if (Childhooddiseases05yrsokcmd == null) {//GEN-END:|778-getter|0|778-preInit
            // write pre-init user code here
            Childhooddiseases05yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|778-getter|1|778-postInit
            // write post-init user code here
        }//GEN-BEGIN:|778-getter|2|
        return Childhooddiseases05yrsokcmd;
    }
//</editor-fold>//GEN-END:|778-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: BlindnessControlProgrammebackcmd ">//GEN-BEGIN:|780-getter|0|780-preInit
    /**
     * Returns an initialized instance of BlindnessControlProgrammebackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getBlindnessControlProgrammebackcmd() {
        if (BlindnessControlProgrammebackcmd == null) {//GEN-END:|780-getter|0|780-preInit
            // write pre-init user code here
            BlindnessControlProgrammebackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|780-getter|1|780-postInit
            // write post-init user code here
        }//GEN-BEGIN:|780-getter|2|
        return BlindnessControlProgrammebackcmd;
    }
//</editor-fold>//GEN-END:|780-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: BlindnessControlProgrammeokcmd ">//GEN-BEGIN:|782-getter|0|782-preInit
    /**
     * Returns an initialized instance of BlindnessControlProgrammeokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getBlindnessControlProgrammeokcmd() {
        if (BlindnessControlProgrammeokcmd == null) {//GEN-END:|782-getter|0|782-preInit
            // write pre-init user code here
            BlindnessControlProgrammeokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|782-getter|1|782-postInit
            // write post-init user code here
        }//GEN-BEGIN:|782-getter|2|
        return BlindnessControlProgrammeokcmd;
    }
//</editor-fold>//GEN-END:|782-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PatientServicesbackcmd ">//GEN-BEGIN:|785-getter|0|785-preInit
    /**
     * Returns an initialized instance of PatientServicesbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPatientServicesbackcmd() {
        if (PatientServicesbackcmd == null) {//GEN-END:|785-getter|0|785-preInit
            // write pre-init user code here
            PatientServicesbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|785-getter|1|785-postInit
            // write post-init user code here
        }//GEN-BEGIN:|785-getter|2|
        return PatientServicesbackcmd;
    }
//</editor-fold>//GEN-END:|785-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PatientServicesokcmd ">//GEN-BEGIN:|787-getter|0|787-preInit
    /**
     * Returns an initialized instance of PatientServicesokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPatientServicesokcmd() {
        if (PatientServicesokcmd == null) {//GEN-END:|787-getter|0|787-preInit
            // write pre-init user code here
            PatientServicesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|787-getter|1|787-postInit
            // write post-init user code here
        }//GEN-BEGIN:|787-getter|2|
        return PatientServicesokcmd;
    }
//</editor-fold>//GEN-END:|787-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientAdmissionsbackcmd ">//GEN-BEGIN:|790-getter|0|790-preInit
    /**
     * Returns an initialized instance of InPatientAdmissionsbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getInPatientAdmissionsbackcmd() {
        if (InPatientAdmissionsbackcmd == null) {//GEN-END:|790-getter|0|790-preInit
            // write pre-init user code here
            InPatientAdmissionsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|790-getter|1|790-postInit
            // write post-init user code here
        }//GEN-BEGIN:|790-getter|2|
        return InPatientAdmissionsbackcmd;
    }
//</editor-fold>//GEN-END:|790-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientAdmissionsokcmd ">//GEN-BEGIN:|792-getter|0|792-preInit
    /**
     * Returns an initialized instance of InPatientAdmissionsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getInPatientAdmissionsokcmd() {
        if (InPatientAdmissionsokcmd == null) {//GEN-END:|792-getter|0|792-preInit
            // write pre-init user code here
            InPatientAdmissionsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|792-getter|1|792-postInit
            // write post-init user code here
        }//GEN-BEGIN:|792-getter|2|
        return InPatientAdmissionsokcmd;
    }
//</editor-fold>//GEN-END:|792-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: LaboratoryTestingbackcmd ">//GEN-BEGIN:|795-getter|0|795-preInit
    /**
     * Returns an initialized instance of LaboratoryTestingbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getLaboratoryTestingbackcmd() {
        if (LaboratoryTestingbackcmd == null) {//GEN-END:|795-getter|0|795-preInit
            // write pre-init user code here
            LaboratoryTestingbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|795-getter|1|795-postInit
            // write post-init user code here
        }//GEN-BEGIN:|795-getter|2|
        return LaboratoryTestingbackcmd;
    }
//</editor-fold>//GEN-END:|795-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: LaboratoryTestingokcmd ">//GEN-BEGIN:|797-getter|0|797-preInit
    /**
     * Returns an initialized instance of LaboratoryTestingokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getLaboratoryTestingokcmd() {
        if (LaboratoryTestingokcmd == null) {//GEN-END:|797-getter|0|797-preInit
            // write pre-init user code here
            LaboratoryTestingokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|797-getter|1|797-postInit
            // write post-init user code here
        }//GEN-BEGIN:|797-getter|2|
        return LaboratoryTestingokcmd;
    }
//</editor-fold>//GEN-END:|797-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANCservicesbackcmd ">//GEN-BEGIN:|803-getter|0|803-preInit
    /**
     * Returns an initialized instance of ANCservicesbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getANCservicesbackcmd() {
        if (ANCservicesbackcmd == null) {//GEN-END:|803-getter|0|803-preInit
            // write pre-init user code here
            ANCservicesbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|803-getter|1|803-postInit
            // write post-init user code here
        }//GEN-BEGIN:|803-getter|2|
        return ANCservicesbackcmd;
    }
//</editor-fold>//GEN-END:|803-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ANCservicesokcmd ">//GEN-BEGIN:|805-getter|0|805-preInit
    /**
     * Returns an initialized instance of ANCservicesokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getANCservicesokcmd() {
        if (ANCservicesokcmd == null) {//GEN-END:|805-getter|0|805-preInit
            // write pre-init user code here
            ANCservicesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|805-getter|1|805-postInit
            // write post-init user code here
        }//GEN-BEGIN:|805-getter|2|
        return ANCservicesokcmd;
    }
//</editor-fold>//GEN-END:|805-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand2 ">//GEN-BEGIN:|813-getter|0|813-preInit
    /**
     * Returns an initialized instance of backCommand2 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand2() {
        if (backCommand2 == null) {//GEN-END:|813-getter|0|813-preInit
            // write pre-init user code here
            backCommand2 = new Command("Back", Command.BACK, 0);//GEN-LINE:|813-getter|1|813-postInit
            // write post-init user code here
        }//GEN-BEGIN:|813-getter|2|
        return backCommand2;
    }
//</editor-fold>//GEN-END:|813-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|815-getter|0|815-preInit
    /**
     * Returns an initialized instance of okCommand component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|815-getter|0|815-preInit
            // write pre-init user code here
            okCommand = new Command("Next", Command.OK, 0);//GEN-LINE:|815-getter|1|815-postInit
            // write post-init user code here
        }//GEN-BEGIN:|815-getter|2|
        return okCommand;
    }
//</editor-fold>//GEN-END:|815-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PSDeaths ">//GEN-BEGIN:|822-getter|0|822-preInit
    /**
     * Returns an initialized instance of PSDeaths component.
     *
     * @return the initialized component instance
     */
    public Form getPSDeaths() {
        if (PSDeaths == null) {//GEN-END:|822-getter|0|822-preInit
            // write pre-init user code here
            PSDeaths = new Form("Patient Services ", new Item[]{getStringItem9(), getTextField44(), getTextField45()});//GEN-BEGIN:|822-getter|1|822-postInit
            PSDeaths.addCommand(getPSDeathsbackcmd());
            PSDeaths.addCommand(getPsDeathsokcmd());
            PSDeaths.setCommandListener(this);//GEN-END:|822-getter|1|822-postInit
            // write post-init user code here
        }//GEN-BEGIN:|822-getter|2|
        return PSDeaths;
    }
//</editor-fold>//GEN-END:|822-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientHeadCountatmidnight ">//GEN-BEGIN:|823-getter|0|823-preInit
    /**
     * Returns an initialized instance of InPatientHeadCountatmidnight
     * component.
     *
     * @return the initialized component instance
     */
    public Form getInPatientHeadCountatmidnight() {
        if (InPatientHeadCountatmidnight == null) {//GEN-END:|823-getter|0|823-preInit
            // write pre-init user code here
            InPatientHeadCountatmidnight = new Form("Patient Services ", new Item[]{getStringItem10(), getTextField46()});//GEN-BEGIN:|823-getter|1|823-postInit
            InPatientHeadCountatmidnight.addCommand(getInPatientHeadCountatmidnightbackcmd());
            InPatientHeadCountatmidnight.addCommand(getInPatientHeadCountatmidnightokcmd());
            InPatientHeadCountatmidnight.setCommandListener(this);//GEN-END:|823-getter|1|823-postInit
            // write post-init user code here
        }//GEN-BEGIN:|823-getter|2|
        return InPatientHeadCountatmidnight;
    }
//</editor-fold>//GEN-END:|823-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OperationTheatre ">//GEN-BEGIN:|824-getter|0|824-preInit
    /**
     * Returns an initialized instance of OperationTheatre component.
     *
     * @return the initialized component instance
     */
    public Form getOperationTheatre() {
        if (OperationTheatre == null) {//GEN-END:|824-getter|0|824-preInit
            // write pre-init user code here
            OperationTheatre = new Form("Patient Services ", new Item[]{getStringItem11(), getTextField47(), getTextField48()});//GEN-BEGIN:|824-getter|1|824-postInit
            OperationTheatre.addCommand(getOperationTheatrebackcmd());
            OperationTheatre.addCommand(getOperationTheatreokcmd());
            OperationTheatre.setCommandListener(this);//GEN-END:|824-getter|1|824-postInit
            // write post-init user code here
        }//GEN-BEGIN:|824-getter|2|
        return OperationTheatre;
    }
//</editor-fold>//GEN-END:|824-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OthersDenlOphAYUSHetc ">//GEN-BEGIN:|825-getter|0|825-preInit
    /**
     * Returns an initialized instance of OthersDenlOphAYUSHetc component.
     *
     * @return the initialized component instance
     */
    public Form getOthersDenlOphAYUSHetc() {
        if (OthersDenlOphAYUSHetc == null) {//GEN-END:|825-getter|0|825-preInit
            // write pre-init user code here
            OthersDenlOphAYUSHetc = new Form("Patient Services ", new Item[]{getStringItem12(), getTextField49(), getTextField50(), getTextField51()});//GEN-BEGIN:|825-getter|1|825-postInit
            OthersDenlOphAYUSHetc.addCommand(getOthersetcbackcmd());
            OthersDenlOphAYUSHetc.addCommand(getOthersetcokcmd());
            OthersDenlOphAYUSHetc.setCommandListener(this);//GEN-END:|825-getter|1|825-postInit
            // write post-init user code here
        }//GEN-BEGIN:|825-getter|2|
        return OthersDenlOphAYUSHetc;
    }
//</editor-fold>//GEN-END:|825-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Widaltestsconducted ">//GEN-BEGIN:|826-getter|0|826-preInit
    /**
     * Returns an initialized instance of Widaltestsconducted component.
     *
     * @return the initialized component instance
     */
    public Form getWidaltestsconducted() {
        if (Widaltestsconducted == null) {//GEN-END:|826-getter|0|826-preInit
            // write pre-init user code here
            Widaltestsconducted = new Form("Laboratory Testing ", new Item[]{getStringItem5(), getTextField58(), getTextField11()});//GEN-BEGIN:|826-getter|1|826-postInit
            Widaltestsconducted.addCommand(getWidaltestsconductedbackcmd());
            Widaltestsconducted.addCommand(getWidaltestsconductedokcmd());
            Widaltestsconducted.setCommandListener(this);//GEN-END:|826-getter|1|826-postInit
            // write post-init user code here
        }//GEN-BEGIN:|826-getter|2|
        return Widaltestsconducted;
    }
//</editor-fold>//GEN-END:|826-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: VDRLtestsconducted ">//GEN-BEGIN:|847-getter|0|847-preInit
    /**
     * Returns an initialized instance of VDRLtestsconducted component.
     *
     * @return the initialized component instance
     */
    public Form getVDRLtestsconducted() {
        if (VDRLtestsconducted == null) {//GEN-END:|847-getter|0|847-preInit
            // write pre-init user code here
            VDRLtestsconducted = new Form("Laboratory Testing", new Item[]{getStringItem3(), getTextField59(), getTextField60(), getTextField61(), getTextField32(), getTextField33(), getTextField34()});//GEN-BEGIN:|847-getter|1|847-postInit
            VDRLtestsconducted.addCommand(getVDRLtestsconductedbackcmd());
            VDRLtestsconducted.addCommand(getVDRLtestsconductedokcmd());
            VDRLtestsconducted.setCommandListener(this);//GEN-END:|847-getter|1|847-postInit
            // write post-init user code here
        }//GEN-BEGIN:|847-getter|2|
        return VDRLtestsconducted;
    }
//</editor-fold>//GEN-END:|847-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malariatestsconducted ">//GEN-BEGIN:|848-getter|0|848-preInit
    /**
     * Returns an initialized instance of Malariatestsconducted component.
     *
     * @return the initialized component instance
     */
    public Form getMalariatestsconducted() {
        if (Malariatestsconducted == null) {//GEN-END:|848-getter|0|848-preInit
            // write pre-init user code here
            Malariatestsconducted = new Form("Laboratory Testing", new Item[]{getStringItem4(), getTextField62(), getTextField63(), getTextField64()});//GEN-BEGIN:|848-getter|1|848-postInit
            Malariatestsconducted.addCommand(getMalariatestsconductedbackcmd());
            Malariatestsconducted.addCommand(getMalariatestsconductedokcmd());
            Malariatestsconducted.setCommandListener(this);//GEN-END:|848-getter|1|848-postInit
            // write post-init user code here
        }//GEN-BEGIN:|848-getter|2|
        return Malariatestsconducted;
    }
//</editor-fold>//GEN-END:|848-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Qualityinsterilizationservicesbackcmd ">//GEN-BEGIN:|818-getter|0|818-preInit
    /**
     * Returns an initialized instance of Qualityinsterilizationservicesbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getQualityinsterilizationservicesbackcmd() {
        if (Qualityinsterilizationservicesbackcmd == null) {//GEN-END:|818-getter|0|818-preInit
            // write pre-init user code here
            Qualityinsterilizationservicesbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|818-getter|1|818-postInit
            // write post-init user code here
        }//GEN-BEGIN:|818-getter|2|
        return Qualityinsterilizationservicesbackcmd;
    }
//</editor-fold>//GEN-END:|818-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Qualityinsterilizationservicesokcmd ">//GEN-BEGIN:|820-getter|0|820-preInit
    /**
     * Returns an initialized instance of Qualityinsterilizationservicesokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getQualityinsterilizationservicesokcmd() {
        if (Qualityinsterilizationservicesokcmd == null) {//GEN-END:|820-getter|0|820-preInit
            // write pre-init user code here
            Qualityinsterilizationservicesokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|820-getter|1|820-postInit
            // write post-init user code here
        }//GEN-BEGIN:|820-getter|2|
        return Qualityinsterilizationservicesokcmd;
    }
//</editor-fold>//GEN-END:|820-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PSDeathsbackcmd ">//GEN-BEGIN:|827-getter|0|827-preInit
    /**
     * Returns an initialized instance of PSDeathsbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPSDeathsbackcmd() {
        if (PSDeathsbackcmd == null) {//GEN-END:|827-getter|0|827-preInit
            // write pre-init user code here
            PSDeathsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|827-getter|1|827-postInit
            // write post-init user code here
        }//GEN-BEGIN:|827-getter|2|
        return PSDeathsbackcmd;
    }
//</editor-fold>//GEN-END:|827-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: psDeathsokcmd ">//GEN-BEGIN:|829-getter|0|829-preInit
    /**
     * Returns an initialized instance of psDeathsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getPsDeathsokcmd() {
        if (psDeathsokcmd == null) {//GEN-END:|829-getter|0|829-preInit
            // write pre-init user code here
            psDeathsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|829-getter|1|829-postInit
            // write post-init user code here
        }//GEN-BEGIN:|829-getter|2|
        return psDeathsokcmd;
    }
//</editor-fold>//GEN-END:|829-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientHeadCountatmidnightbackcmd ">//GEN-BEGIN:|831-getter|0|831-preInit
    /**
     * Returns an initialized instance of InPatientHeadCountatmidnightbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInPatientHeadCountatmidnightbackcmd() {
        if (InPatientHeadCountatmidnightbackcmd == null) {//GEN-END:|831-getter|0|831-preInit
            // write pre-init user code here
            InPatientHeadCountatmidnightbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|831-getter|1|831-postInit
            // write post-init user code here
        }//GEN-BEGIN:|831-getter|2|
        return InPatientHeadCountatmidnightbackcmd;
    }
//</editor-fold>//GEN-END:|831-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: InPatientHeadCountatmidnightokcmd ">//GEN-BEGIN:|833-getter|0|833-preInit
    /**
     * Returns an initialized instance of InPatientHeadCountatmidnightokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInPatientHeadCountatmidnightokcmd() {
        if (InPatientHeadCountatmidnightokcmd == null) {//GEN-END:|833-getter|0|833-preInit
            // write pre-init user code here
            InPatientHeadCountatmidnightokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|833-getter|1|833-postInit
            // write post-init user code here
        }//GEN-BEGIN:|833-getter|2|
        return InPatientHeadCountatmidnightokcmd;
    }
//</editor-fold>//GEN-END:|833-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OperationTheatrebackcmd ">//GEN-BEGIN:|835-getter|0|835-preInit
    /**
     * Returns an initialized instance of OperationTheatrebackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getOperationTheatrebackcmd() {
        if (OperationTheatrebackcmd == null) {//GEN-END:|835-getter|0|835-preInit
            // write pre-init user code here
            OperationTheatrebackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|835-getter|1|835-postInit
            // write post-init user code here
        }//GEN-BEGIN:|835-getter|2|
        return OperationTheatrebackcmd;
    }
//</editor-fold>//GEN-END:|835-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OperationTheatreokcmd ">//GEN-BEGIN:|837-getter|0|837-preInit
    /**
     * Returns an initialized instance of OperationTheatreokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getOperationTheatreokcmd() {
        if (OperationTheatreokcmd == null) {//GEN-END:|837-getter|0|837-preInit
            // write pre-init user code here
            OperationTheatreokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|837-getter|1|837-postInit
            // write post-init user code here
        }//GEN-BEGIN:|837-getter|2|
        return OperationTheatreokcmd;
    }
//</editor-fold>//GEN-END:|837-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Othersetcbackcmd ">//GEN-BEGIN:|839-getter|0|839-preInit
    /**
     * Returns an initialized instance of Othersetcbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getOthersetcbackcmd() {
        if (Othersetcbackcmd == null) {//GEN-END:|839-getter|0|839-preInit
            // write pre-init user code here
            Othersetcbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|839-getter|1|839-postInit
            // write post-init user code here
        }//GEN-BEGIN:|839-getter|2|
        return Othersetcbackcmd;
    }
//</editor-fold>//GEN-END:|839-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Othersetcokcmd ">//GEN-BEGIN:|841-getter|0|841-preInit
    /**
     * Returns an initialized instance of Othersetcokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getOthersetcokcmd() {
        if (Othersetcokcmd == null) {//GEN-END:|841-getter|0|841-preInit
            // write pre-init user code here
            Othersetcokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|841-getter|1|841-postInit
            // write post-init user code here
        }//GEN-BEGIN:|841-getter|2|
        return Othersetcokcmd;
    }
//</editor-fold>//GEN-END:|841-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Widaltestsconductedbackcmd ">//GEN-BEGIN:|843-getter|0|843-preInit
    /**
     * Returns an initialized instance of Widaltestsconductedbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getWidaltestsconductedbackcmd() {
        if (Widaltestsconductedbackcmd == null) {//GEN-END:|843-getter|0|843-preInit
            // write pre-init user code here
            Widaltestsconductedbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|843-getter|1|843-postInit
            // write post-init user code here
        }//GEN-BEGIN:|843-getter|2|
        return Widaltestsconductedbackcmd;
    }
//</editor-fold>//GEN-END:|843-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Widaltestsconductedokcmd ">//GEN-BEGIN:|845-getter|0|845-preInit
    /**
     * Returns an initialized instance of Widaltestsconductedokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getWidaltestsconductedokcmd() {
        if (Widaltestsconductedokcmd == null) {//GEN-END:|845-getter|0|845-preInit
            // write pre-init user code here
            Widaltestsconductedokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|845-getter|1|845-postInit
            // write post-init user code here
        }//GEN-BEGIN:|845-getter|2|
        return Widaltestsconductedokcmd;
    }
//</editor-fold>//GEN-END:|845-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: VDRLtestsconductedbackcmd ">//GEN-BEGIN:|849-getter|0|849-preInit
    /**
     * Returns an initialized instance of VDRLtestsconductedbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getVDRLtestsconductedbackcmd() {
        if (VDRLtestsconductedbackcmd == null) {//GEN-END:|849-getter|0|849-preInit
            // write pre-init user code here
            VDRLtestsconductedbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|849-getter|1|849-postInit
            // write post-init user code here
        }//GEN-BEGIN:|849-getter|2|
        return VDRLtestsconductedbackcmd;
    }
//</editor-fold>//GEN-END:|849-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: VDRLtestsconductedokcmd ">//GEN-BEGIN:|851-getter|0|851-preInit
    /**
     * Returns an initialized instance of VDRLtestsconductedokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getVDRLtestsconductedokcmd() {
        if (VDRLtestsconductedokcmd == null) {//GEN-END:|851-getter|0|851-preInit
            // write pre-init user code here
            VDRLtestsconductedokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|851-getter|1|851-postInit
            // write post-init user code here
        }//GEN-BEGIN:|851-getter|2|
        return VDRLtestsconductedokcmd;
    }
//</editor-fold>//GEN-END:|851-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malariatestsconductedbackcmd ">//GEN-BEGIN:|854-getter|0|854-preInit
    /**
     * Returns an initialized instance of Malariatestsconductedbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMalariatestsconductedbackcmd() {
        if (Malariatestsconductedbackcmd == null) {//GEN-END:|854-getter|0|854-preInit
            // write pre-init user code here
            Malariatestsconductedbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|854-getter|1|854-postInit
            // write post-init user code here
        }//GEN-BEGIN:|854-getter|2|
        return Malariatestsconductedbackcmd;
    }
//</editor-fold>//GEN-END:|854-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malariatestsconductedokcmd ">//GEN-BEGIN:|856-getter|0|856-preInit
    /**
     * Returns an initialized instance of Malariatestsconductedokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getMalariatestsconductedokcmd() {
        if (Malariatestsconductedokcmd == null) {//GEN-END:|856-getter|0|856-preInit
            // write pre-init user code here
            Malariatestsconductedokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|856-getter|1|856-postInit
            // write post-init user code here
        }//GEN-BEGIN:|856-getter|2|
        return Malariatestsconductedokcmd;
    }
//</editor-fold>//GEN-END:|856-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField40 ">//GEN-BEGIN:|940-getter|0|940-preInit
    /**
     * Returns an initialized instance of textField40 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField40() {
        if (textField40 == null) {//GEN-END:|940-getter|0|940-preInit
            // write pre-init user code here
            String str = getRecordValue(28);
            textField40 = new TextField(LocalizationSupport.getMessage("Malechildren19yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|940-getter|1|940-postInit
            // write post-init user code here
        }//GEN-BEGIN:|940-getter|2|
        return textField40;
    }
//</editor-fold>//GEN-END:|940-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField41 ">//GEN-BEGIN:|941-getter|0|941-preInit
    /**
     * Returns an initialized instance of textField41 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField41() {
        if (textField41 == null) {//GEN-END:|941-getter|0|941-preInit
            // write pre-init user code here
            String str = getRecordValue(29);
            textField41 = new TextField(LocalizationSupport.getMessage("Femalechildren19yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|941-getter|1|941-postInit
            // write post-init user code here
        }//GEN-BEGIN:|941-getter|2|
        return textField41;
    }
//</editor-fold>//GEN-END:|941-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField42 ">//GEN-BEGIN:|942-getter|0|942-preInit
    /**
     * Returns an initialized instance of textField42 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField42() {
        if (textField42 == null) {//GEN-END:|942-getter|0|942-preInit
            // write pre-init user code here
            String str = getRecordValue(30);
            textField42 = new TextField(LocalizationSupport.getMessage("MaleAdult"), str, 4, TextField.NUMERIC);//GEN-LINE:|942-getter|1|942-postInit
            // write post-init user code here
        }//GEN-BEGIN:|942-getter|2|
        return textField42;
    }
//</editor-fold>//GEN-END:|942-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField43 ">//GEN-BEGIN:|943-getter|0|943-preInit
    /**
     * Returns an initialized instance of textField43 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField43() {
        if (textField43 == null) {//GEN-END:|943-getter|0|943-preInit
            // write pre-init user code here
            String str = getRecordValue(31);
            textField43 = new TextField(LocalizationSupport.getMessage("FemaleAdult"), str, 4, TextField.NUMERIC);//GEN-LINE:|943-getter|1|943-postInit
            // write post-init user code here
        }//GEN-BEGIN:|943-getter|2|
        return textField43;
    }
//</editor-fold>//GEN-END:|943-getter|2|







//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField35 ">//GEN-BEGIN:|935-getter|0|935-preInit
    /**
     * Returns an initialized instance of textField35 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField35() {
        if (textField35 == null) {//GEN-END:|935-getter|0|935-preInit
            // write pre-init user code here
            String str = getRecordValue(26);
            textField35 = new TextField(LocalizationSupport.getMessage("RKSmeetings"), str, 4, TextField.NUMERIC);//GEN-LINE:|935-getter|1|935-postInit
            // write post-init user code here
        }//GEN-BEGIN:|935-getter|2|
        return textField35;
    }
//</editor-fold>//GEN-END:|935-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField37 ">//GEN-BEGIN:|937-getter|0|937-preInit
    /**
     * Returns an initialized instance of textField37 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField37() {
        if (textField37 == null) {//GEN-END:|937-getter|0|937-preInit
            // write pre-init user code here
            String str = getRecordValue(37);
            textField37 = new TextField(LocalizationSupport.getMessage("Timesambulanceservicesused"), str, 4, TextField.NUMERIC);//GEN-LINE:|937-getter|1|937-postInit
            // write post-init user code here
        }//GEN-BEGIN:|937-getter|2|
        return textField37;
    }
//</editor-fold>//GEN-END:|937-getter|2|





//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem2 ">//GEN-BEGIN:|952-getter|0|952-preInit
    /**
     * Returns an initialized instance of stringItem2 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem2() {
        if (stringItem2 == null) {//GEN-END:|952-getter|0|952-preInit
            // write pre-init user code here
            stringItem2 = new StringItem(LocalizationSupport.getMessage("LaboratoryTestDetails"), null);//GEN-LINE:|952-getter|1|952-postInit
            // write post-init user code here
        }//GEN-BEGIN:|952-getter|2|
        return stringItem2;
    }
//</editor-fold>//GEN-END:|952-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField52 ">//GEN-BEGIN:|956-getter|0|956-preInit
    /**
     * Returns an initialized instance of textField52 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField52() {
        if (textField52 == null) {//GEN-END:|956-getter|0|956-preInit
            // write pre-init user code here
            String str = getRecordValue(41);
            textField52 = new TextField(LocalizationSupport.getMessage("Hbtestsconducted"), str, 4, TextField.NUMERIC);//GEN-LINE:|956-getter|1|956-postInit
            // write post-init user code here
        }//GEN-BEGIN:|956-getter|2|
        return textField52;
    }
//</editor-fold>//GEN-END:|956-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField53 ">//GEN-BEGIN:|957-getter|0|957-preInit
    /**
     * Returns an initialized instance of textField53 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField53() {
        if (textField53 == null) {//GEN-END:|957-getter|0|957-preInit
            // write pre-init user code here
            String str = getRecordValue(42);
            textField53 = new TextField(LocalizationSupport.getMessage("Hb<7mg"), str, 4, TextField.NUMERIC);//GEN-LINE:|957-getter|1|957-postInit
            // write post-init user code here
        }//GEN-BEGIN:|957-getter|2|
        return textField53;
    }
//</editor-fold>//GEN-END:|957-getter|2|









//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField20 ">//GEN-BEGIN:|920-getter|0|920-preInit
    /**
     * Returns an initialized instance of textField20 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField20() {
        if (textField20 == null) {//GEN-END:|920-getter|0|920-preInit
            // write pre-init user code here
            String str = getRecordValue(11);
            textField20 = new TextField(LocalizationSupport.getMessage("Diphtheria"), str, 4, TextField.NUMERIC);//GEN-LINE:|920-getter|1|920-postInit
            // write post-init user code here
        }//GEN-BEGIN:|920-getter|2|
        return textField20;
    }
//</editor-fold>//GEN-END:|920-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField21 ">//GEN-BEGIN:|921-getter|0|921-preInit
    /**
     * Returns an initialized instance of textField21 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField21() {
        if (textField21 == null) {//GEN-END:|921-getter|0|921-preInit
            // write pre-init user code here
            String str = getRecordValue(12);
            textField21 = new TextField(LocalizationSupport.getMessage("Pertussis"), str, 4, TextField.NUMERIC);//GEN-LINE:|921-getter|1|921-postInit
            // write post-init user code here
        }//GEN-BEGIN:|921-getter|2|
        return textField21;
    }
//</editor-fold>//GEN-END:|921-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField22 ">//GEN-BEGIN:|922-getter|0|922-preInit
    /**
     * Returns an initialized instance of textField22 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField22() {
        if (textField22 == null) {//GEN-END:|922-getter|0|922-preInit
            // write pre-init user code here
            String str = getRecordValue(13);
            textField22 = new TextField(LocalizationSupport.getMessage("TetanusNeonatorum"), str, 4, TextField.NUMERIC);//GEN-LINE:|922-getter|1|922-postInit
            // write post-init user code here
        }//GEN-BEGIN:|922-getter|2|
        return textField22;
    }
//</editor-fold>//GEN-END:|922-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField23 ">//GEN-BEGIN:|923-getter|0|923-preInit
    /**
     * Returns an initialized instance of textField23 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField23() {
        if (textField23 == null) {//GEN-END:|923-getter|0|923-preInit
            // write pre-init user code here
            String str = getRecordValue(14);
            textField23 = new TextField(LocalizationSupport.getMessage("Tetanusothers"), str, 4, TextField.NUMERIC);//GEN-LINE:|923-getter|1|923-postInit
            // write post-init user code here
        }//GEN-BEGIN:|923-getter|2|
        return textField23;
    }
//</editor-fold>//GEN-END:|923-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField24 ">//GEN-BEGIN:|924-getter|0|924-preInit
    /**
     * Returns an initialized instance of textField24 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField24() {
        if (textField24 == null) {//GEN-END:|924-getter|0|924-preInit
            // write pre-init user code here
            String str = getRecordValue(15);
            textField24 = new TextField(LocalizationSupport.getMessage("Polio"), str, 4, TextField.NUMERIC);//GEN-LINE:|924-getter|1|924-postInit
            // write post-init user code here
        }//GEN-BEGIN:|924-getter|2|
        return textField24;
    }
//</editor-fold>//GEN-END:|924-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField25 ">//GEN-BEGIN:|925-getter|0|925-preInit
    /**
     * Returns an initialized instance of textField25 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField25() {
        if (textField25 == null) {//GEN-END:|925-getter|0|925-preInit
            // write pre-init user code here
            String str = getRecordValue(19);
            textField25 = new TextField(LocalizationSupport.getMessage("NoadmittedwithRespiratoryInfections"), str, 4, TextField.NUMERIC);//GEN-LINE:|925-getter|1|925-postInit
            // write post-init user code here
        }//GEN-BEGIN:|925-getter|2|
        return textField25;
    }
//</editor-fold>//GEN-END:|925-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField26 ">//GEN-BEGIN:|926-getter|0|926-preInit
    /**
     * Returns an initialized instance of textField26 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField26() {
        if (textField26 == null) {//GEN-END:|926-getter|0|926-preInit
            // write pre-init user code here
            String str = getRecordValue(20);
            textField26 = new TextField(LocalizationSupport.getMessage("Cataractoperations"), str, 4, TextField.NUMERIC);//GEN-LINE:|926-getter|1|926-postInit
            // write post-init user code here
        }//GEN-BEGIN:|926-getter|2|
        return textField26;
    }
//</editor-fold>//GEN-END:|926-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField27 ">//GEN-BEGIN:|927-getter|0|927-preInit
    /**
     * Returns an initialized instance of textField27 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField27() {
        if (textField27 == null) {//GEN-END:|927-getter|0|927-preInit
            // write pre-init user code here
            String str = getRecordValue(21);
            textField27 = new TextField(LocalizationSupport.getMessage("IOLimplantations"), str, 4, TextField.NUMERIC);//GEN-LINE:|927-getter|1|927-postInit
            // write post-init user code here
        }//GEN-BEGIN:|927-getter|2|
        return textField27;
    }
//</editor-fold>//GEN-END:|927-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField28 ">//GEN-BEGIN:|928-getter|0|928-preInit
    /**
     * Returns an initialized instance of textField28 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField28() {
        if (textField28 == null) {//GEN-END:|928-getter|0|928-preInit
            // write pre-init user code here
            String str = getRecordValue(22);
            textField28 = new TextField(LocalizationSupport.getMessage("Schoolchildrendetectedwithrefractiveerrors"), str, 4, TextField.NUMERIC);//GEN-LINE:|928-getter|1|928-postInit
            // write post-init user code here
        }//GEN-BEGIN:|928-getter|2|
        return textField28;
    }
//</editor-fold>//GEN-END:|928-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField29 ">//GEN-BEGIN:|929-getter|0|929-preInit
    /**
     * Returns an initialized instance of textField29 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField29() {
        if (textField29 == null) {//GEN-END:|929-getter|0|929-preInit
            // write pre-init user code here
            String str = getRecordValue(23);
            textField29 = new TextField(LocalizationSupport.getMessage("Childrengivenfreeglasses"), str, 4, TextField.NUMERIC);//GEN-LINE:|929-getter|1|929-postInit
            // write post-init user code here
        }//GEN-BEGIN:|929-getter|2|
        return textField29;
    }
//</editor-fold>//GEN-END:|929-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField30 ">//GEN-BEGIN:|930-getter|0|930-preInit
    /**
     * Returns an initialized instance of textField30 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField30() {
        if (textField30 == null) {//GEN-END:|930-getter|0|930-preInit
            // write pre-init user code here
            String str = getRecordValue(24);
            textField30 = new TextField(LocalizationSupport.getMessage("Eyescollected"), str, 4, TextField.NUMERIC);//GEN-LINE:|930-getter|1|930-postInit
            // write post-init user code here
        }//GEN-BEGIN:|930-getter|2|
        return textField30;
    }
//</editor-fold>//GEN-END:|930-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField31 ">//GEN-BEGIN:|931-getter|0|931-preInit
    /**
     * Returns an initialized instance of textField31 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField31() {
        if (textField31 == null) {//GEN-END:|931-getter|0|931-preInit
            // write pre-init user code here
            String str = getRecordValue(25);
            textField31 = new TextField(LocalizationSupport.getMessage("Eyesutilised"), str, 4, TextField.NUMERIC);//GEN-LINE:|931-getter|1|931-postInit
            // write post-init user code here
        }//GEN-BEGIN:|931-getter|2|
        return textField31;
    }
//</editor-fold>//GEN-END:|931-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField44 ">//GEN-BEGIN:|944-getter|0|944-preInit
    /**
     * Returns an initialized instance of textField44 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField44() {
        if (textField44 == null) {//GEN-END:|944-getter|0|944-preInit
            // write pre-init user code here
            String str = getRecordValue(32);
            textField44 = new TextField(LocalizationSupport.getMessage("Male_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|944-getter|1|944-postInit
            // write post-init user code here
        }//GEN-BEGIN:|944-getter|2|
        return textField44;
    }
//</editor-fold>//GEN-END:|944-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField45 ">//GEN-BEGIN:|945-getter|0|945-preInit
    /**
     * Returns an initialized instance of textField45 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField45() {
        if (textField45 == null) {//GEN-END:|945-getter|0|945-preInit
            // write pre-init user code here
            String str = getRecordValue(33);
            textField45 = new TextField(LocalizationSupport.getMessage("Female_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|945-getter|1|945-postInit
            // write post-init user code here
        }//GEN-BEGIN:|945-getter|2|
        return textField45;
    }
//</editor-fold>//GEN-END:|945-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField46 ">//GEN-BEGIN:|946-getter|0|946-preInit
    /**
     * Returns an initialized instance of textField46 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField46() {
        if (textField46 == null) {//GEN-END:|946-getter|0|946-preInit
            // write pre-init user code here
            String str = getRecordValue(34);
            textField46 = new TextField(LocalizationSupport.getMessage("Headcountat_midnight"), str, 4, TextField.NUMERIC);//GEN-LINE:|946-getter|1|946-postInit
            // write post-init user code here
        }//GEN-BEGIN:|946-getter|2|
        return textField46;
    }
//</editor-fold>//GEN-END:|946-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField47 ">//GEN-BEGIN:|947-getter|0|947-preInit
    /**
     * Returns an initialized instance of textField47 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField47() {
        if (textField47 == null) {//GEN-END:|947-getter|0|947-preInit
            // write pre-init user code here
            String str = getRecordValue(36);
            textField47 = new TextField(LocalizationSupport.getMessage("Operationmajor"), str, 4, TextField.NUMERIC);//GEN-LINE:|947-getter|1|947-postInit
            // write post-init user code here
        }//GEN-BEGIN:|947-getter|2|
        return textField47;
    }
//</editor-fold>//GEN-END:|947-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField48 ">//GEN-BEGIN:|948-getter|0|948-preInit
    /**
     * Returns an initialized instance of textField48 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField48() {
        if (textField48 == null) {//GEN-END:|948-getter|0|948-preInit
            // write pre-init user code here
            String str = getRecordValue(37);
            textField48 = new TextField(LocalizationSupport.getMessage("Operationminor"), str, 4, TextField.NUMERIC);//GEN-LINE:|948-getter|1|948-postInit
            // write post-init user code here
        }//GEN-BEGIN:|948-getter|2|
        return textField48;
    }
//</editor-fold>//GEN-END:|948-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField49 ">//GEN-BEGIN:|949-getter|0|949-preInit
    /**
     * Returns an initialized instance of textField49 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField49() {
        if (textField49 == null) {//GEN-END:|949-getter|0|949-preInit
            // write pre-init user code here
            String str = getRecordValue(38);
            textField49 = new TextField(LocalizationSupport.getMessage("AYUSH"), str, 4, TextField.NUMERIC);//GEN-LINE:|949-getter|1|949-postInit
            // write post-init user code here
        }//GEN-BEGIN:|949-getter|2|
        return textField49;
    }
//</editor-fold>//GEN-END:|949-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField50 ">//GEN-BEGIN:|950-getter|0|950-preInit
    /**
     * Returns an initialized instance of textField50 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField50() {
        if (textField50 == null) {//GEN-END:|950-getter|0|950-preInit
            // write pre-init user code here
            String str = getRecordValue(39);
            textField50 = new TextField(LocalizationSupport.getMessage("DentalProcedures"), str, 4, TextField.NUMERIC);//GEN-LINE:|950-getter|1|950-postInit
            // write post-init user code here
        }//GEN-BEGIN:|950-getter|2|
        return textField50;
    }
//</editor-fold>//GEN-END:|950-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField51 ">//GEN-BEGIN:|951-getter|0|951-preInit
    /**
     * Returns an initialized instance of textField51 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField51() {
        if (textField51 == null) {//GEN-END:|951-getter|0|951-preInit
            // write pre-init user code here
            String str = getRecordValue(40);
            textField51 = new TextField(LocalizationSupport.getMessage("Adolcounselling"), str, 4, TextField.NUMERIC);//GEN-LINE:|951-getter|1|951-postInit
            // write post-init user code here
        }//GEN-BEGIN:|951-getter|2|
        return textField51;
    }
//</editor-fold>//GEN-END:|951-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField58 ">//GEN-BEGIN:|962-getter|0|962-preInit
    /**
     * Returns an initialized instance of textField58 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField58() {
        if (textField58 == null) {//GEN-END:|962-getter|0|962-preInit
            // write pre-init user code here
            String str = getRecordValue(49);
            textField58 = new TextField(LocalizationSupport.getMessage("Widaltestsconducted"), str, 4, TextField.NUMERIC);//GEN-LINE:|962-getter|1|962-postInit
            // write post-init user code here
        }//GEN-BEGIN:|962-getter|2|
        return textField58;
    }
//</editor-fold>//GEN-END:|962-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem8 ">//GEN-BEGIN:|975-getter|0|975-preInit
    /**
     * Returns an initialized instance of stringItem8 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem8() {
        if (stringItem8 == null) {//GEN-END:|975-getter|0|975-preInit
            // write pre-init user code here
            stringItem8 = new StringItem(LocalizationSupport.getMessage("InPatientAdmissions"), null);//GEN-LINE:|975-getter|1|975-postInit
            // write post-init user code here
        }//GEN-BEGIN:|975-getter|2|
        return stringItem8;
    }
//</editor-fold>//GEN-END:|975-getter|2|





//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem9 ">//GEN-BEGIN:|976-getter|0|976-preInit
    /**
     * Returns an initialized instance of stringItem9 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem9() {
        if (stringItem9 == null) {//GEN-END:|976-getter|0|976-preInit
            // write pre-init user code here
            stringItem9 = new StringItem(LocalizationSupport.getMessage("Deaths"), null);//GEN-LINE:|976-getter|1|976-postInit
            // write post-init user code here
        }//GEN-BEGIN:|976-getter|2|
        return stringItem9;
    }
//</editor-fold>//GEN-END:|976-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem10 ">//GEN-BEGIN:|977-getter|0|977-preInit
    /**
     * Returns an initialized instance of stringItem10 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem10() {
        if (stringItem10 == null) {//GEN-END:|977-getter|0|977-preInit
            // write pre-init user code here
            stringItem10 = new StringItem(LocalizationSupport.getMessage("Headcountatmidnight"), null);//GEN-LINE:|977-getter|1|977-postInit
            // write post-init user code here
        }//GEN-BEGIN:|977-getter|2|
        return stringItem10;
    }
//</editor-fold>//GEN-END:|977-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem11 ">//GEN-BEGIN:|978-getter|0|978-preInit
    /**
     * Returns an initialized instance of stringItem11 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem11() {
        if (stringItem11 == null) {//GEN-END:|978-getter|0|978-preInit
            // write pre-init user code here
            stringItem11 = new StringItem(LocalizationSupport.getMessage("OperationTheatre"), null);//GEN-LINE:|978-getter|1|978-postInit
            // write post-init user code here
        }//GEN-BEGIN:|978-getter|2|
        return stringItem11;
    }
//</editor-fold>//GEN-END:|978-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem12 ">//GEN-BEGIN:|979-getter|0|979-preInit
    /**
     * Returns an initialized instance of stringItem12 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem12() {
        if (stringItem12 == null) {//GEN-END:|979-getter|0|979-preInit
            // write pre-init user code here
            stringItem12 = new StringItem(LocalizationSupport.getMessage("Others"), null);//GEN-LINE:|979-getter|1|979-postInit
            // write post-init user code here
        }//GEN-BEGIN:|979-getter|2|
        return stringItem12;
    }
//</editor-fold>//GEN-END:|979-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem5 ">//GEN-BEGIN:|972-getter|0|972-preInit
    /**
     * Returns an initialized instance of stringItem5 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem5() {
        if (stringItem5 == null) {//GEN-END:|972-getter|0|972-preInit
            // write pre-init user code here
            stringItem5 = new StringItem(LocalizationSupport.getMessage("Widal_tests_conducted"), null);//GEN-LINE:|972-getter|1|972-postInit
            // write post-init user code here
        }//GEN-BEGIN:|972-getter|2|
        return stringItem5;
    }
//</editor-fold>//GEN-END:|972-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem3 ">//GEN-BEGIN:|963-getter|0|963-preInit
    /**
     * Returns an initialized instance of stringItem3 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem3() {
        if (stringItem3 == null) {//GEN-END:|963-getter|0|963-preInit
            // write pre-init user code here
            stringItem3 = new StringItem(LocalizationSupport.getMessage("VDRL_tests_conducted"), null);//GEN-LINE:|963-getter|1|963-postInit
            // write post-init user code here
        }//GEN-BEGIN:|963-getter|2|
        return stringItem3;
    }
//</editor-fold>//GEN-END:|963-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField59 ">//GEN-BEGIN:|964-getter|0|964-preInit
    /**
     * Returns an initialized instance of textField59 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField59() {
        if (textField59 == null) {//GEN-END:|964-getter|0|964-preInit
            // write pre-init user code here
            String str = getRecordValue(51);
            textField59 = new TextField(LocalizationSupport.getMessage("Male_3"), str, 4, TextField.NUMERIC);//GEN-LINE:|964-getter|1|964-postInit
            // write post-init user code here
        }//GEN-BEGIN:|964-getter|2|
        return textField59;
    }
//</editor-fold>//GEN-END:|964-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField60 ">//GEN-BEGIN:|965-getter|0|965-preInit
    /**
     * Returns an initialized instance of textField60 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField60() {
        if (textField60 == null) {//GEN-END:|965-getter|0|965-preInit
            // write pre-init user code here
            String str = getRecordValue(52);
            textField60 = new TextField(LocalizationSupport.getMessage("FemaleNonANC"), str, 4, TextField.NUMERIC);//GEN-LINE:|965-getter|1|965-postInit
            // write post-init user code here
        }//GEN-BEGIN:|965-getter|2|
        return textField60;
    }
//</editor-fold>//GEN-END:|965-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField61 ">//GEN-BEGIN:|966-getter|0|966-preInit
    /**
     * Returns an initialized instance of textField61 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField61() {
        if (textField61 == null) {//GEN-END:|966-getter|0|966-preInit
            // write pre-init user code here
            String str = getRecordValue(53);
            textField61 = new TextField(LocalizationSupport.getMessage("FemalewithANC"), str, 4, TextField.NUMERIC);//GEN-LINE:|966-getter|1|966-postInit
            // write post-init user code here
        }//GEN-BEGIN:|966-getter|2|
        return textField61;
    }
//</editor-fold>//GEN-END:|966-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem4 ">//GEN-BEGIN:|968-getter|0|968-preInit
    /**
     * Returns an initialized instance of stringItem4 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem4() {
        if (stringItem4 == null) {//GEN-END:|968-getter|0|968-preInit
            // write pre-init user code here
            stringItem4 = new StringItem(LocalizationSupport.getMessage("Malaria_tests_conducted"), null);//GEN-LINE:|968-getter|1|968-postInit
            // write post-init user code here
        }//GEN-BEGIN:|968-getter|2|
        return stringItem4;
    }
//</editor-fold>//GEN-END:|968-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField62 ">//GEN-BEGIN:|969-getter|0|969-preInit
    /**
     * Returns an initialized instance of textField62 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField62() {
        if (textField62 == null) {//GEN-END:|969-getter|0|969-preInit
            // write pre-init user code here
            String str = getRecordValue(57);
            textField62 = new TextField(LocalizationSupport.getMessage("Bloodsmearsexamined"), str, 4, TextField.NUMERIC);//GEN-LINE:|969-getter|1|969-postInit
            // write post-init user code here
        }//GEN-BEGIN:|969-getter|2|
        return textField62;
    }
//</editor-fold>//GEN-END:|969-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField63 ">//GEN-BEGIN:|970-getter|0|970-preInit
    /**
     * Returns an initialized instance of textField63 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField63() {
        if (textField63 == null) {//GEN-END:|970-getter|0|970-preInit
            // write pre-init user code here
            String str = getRecordValue(58);
            textField63 = new TextField(LocalizationSupport.getMessage("PlasmodiumVivaxtestpositive"), str, 4, TextField.NUMERIC);//GEN-LINE:|970-getter|1|970-postInit
            // write post-init user code here
        }//GEN-BEGIN:|970-getter|2|
        return textField63;
    }
//</editor-fold>//GEN-END:|970-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField64 ">//GEN-BEGIN:|971-getter|0|971-preInit
    /**
     * Returns an initialized instance of textField64 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField64() {
        if (textField64 == null) {//GEN-END:|971-getter|0|971-preInit
            // write pre-init user code here
            String str = getRecordValue(59);
            textField64 = new TextField(LocalizationSupport.getMessage("PlasmodiumFalciparumtestpositive"), str, 4, TextField.NUMERIC);//GEN-LINE:|971-getter|1|971-postInit
            // write post-init user code here
        }//GEN-BEGIN:|971-getter|2|
        return textField64;
    }
//</editor-fold>//GEN-END:|971-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField11 ">//GEN-BEGIN:|983-getter|0|983-preInit
    /**
     * Returns an initialized instance of textField11 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField11() {
        if (textField11 == null) {//GEN-END:|983-getter|0|983-preInit
            // write pre-init user code here
            String str = getRecordValue(50);
            textField11 = new TextField(LocalizationSupport.getMessage("Widaltests(positive)"), str, 4, TextField.NUMERIC);//GEN-LINE:|983-getter|1|983-postInit
            // write post-init user code here
        }//GEN-BEGIN:|983-getter|2|
        return textField11;
    }
//</editor-fold>//GEN-END:|983-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField32 ">//GEN-BEGIN:|984-getter|0|984-preInit
    /**
     * Returns an initialized instance of textField32 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField32() {
        if (textField32 == null) {//GEN-END:|984-getter|0|984-preInit
            // write pre-init user code here
            String str = getRecordValue(54);
            textField32 = new TextField(LocalizationSupport.getMessage("F_ANC(positive)_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|984-getter|1|984-postInit
            // write post-init user code here
        }//GEN-BEGIN:|984-getter|2|
        return textField32;
    }
//</editor-fold>//GEN-END:|984-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField33 ">//GEN-BEGIN:|985-getter|0|985-preInit
    /**
     * Returns an initialized instance of textField33 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField33() {
        if (textField33 == null) {//GEN-END:|985-getter|0|985-preInit
            // write pre-init user code here
            String str = getRecordValue(55);
            textField33 = new TextField(LocalizationSupport.getMessage("F_NonANC(tested)_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|985-getter|1|985-postInit
            // write post-init user code here
        }//GEN-BEGIN:|985-getter|2|
        return textField33;
    }
//</editor-fold>//GEN-END:|985-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField34 ">//GEN-BEGIN:|986-getter|0|986-preInit
    /**
     * Returns an initialized instance of textField34 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField34() {
        if (textField34 == null) {//GEN-END:|986-getter|0|986-preInit
            // write pre-init user code here
            String str = getRecordValue(56);
            textField34 = new TextField(LocalizationSupport.getMessage("F_NonANC(positive)_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|986-getter|1|986-postInit
            // write post-init user code here
        }//GEN-BEGIN:|986-getter|2|
        return textField34;
    }
//</editor-fold>//GEN-END:|986-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField7 ">//GEN-BEGIN:|1015-getter|0|1015-preInit
    /**
     * Returns an initialized instance of textField7 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField7() {
        if (textField7 == null) {//GEN-END:|1015-getter|0|1015-preInit
            // write pre-init user code here
            String str = getRecordValue(16);
            textField7 = new TextField(LocalizationSupport.getMessage("CD-Measles"), str, 4, TextField.NUMERIC);//GEN-LINE:|1015-getter|1|1015-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1015-getter|2|
        return textField7;
    }
//</editor-fold>//GEN-END:|1015-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField8 ">//GEN-BEGIN:|1016-getter|0|1016-preInit
    /**
     * Returns an initialized instance of textField8 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField8() {
        if (textField8 == null) {//GEN-END:|1016-getter|0|1016-preInit
            // write pre-init user code here
            String str = getRecordValue(17);
            textField8 = new TextField(LocalizationSupport.getMessage("CD-Diarr&Dehyd"), str, 4, TextField.NUMERIC);//GEN-LINE:|1016-getter|1|1016-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1016-getter|2|
        return textField8;
    }
//</editor-fold>//GEN-END:|1016-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField9 ">//GEN-BEGIN:|1017-getter|0|1017-preInit
    /**
     * Returns an initialized instance of textField9 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField9() {
        if (textField9 == null) {//GEN-END:|1017-getter|0|1017-preInit
            // write pre-init user code here
            String str = getRecordValue(18);
            textField9 = new TextField(LocalizationSupport.getMessage("CD-Malaria"), str, 4, TextField.NUMERIC);//GEN-LINE:|1017-getter|1|1017-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1017-getter|2|
        return textField9;
    }
//</editor-fold>//GEN-END:|1017-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Outputpatient ">//GEN-BEGIN:|992-getter|0|992-preInit
    /**
     * Returns an initialized instance of Outputpatient component.
     *
     * @return the initialized component instance
     */
    public Form getOutputpatient() {
        if (Outputpatient == null) {//GEN-END:|992-getter|0|992-preInit
            // write pre-init user code here
            Outputpatient = new Form("Patient Services ", new Item[]{getStringItem(), getTextField()});//GEN-BEGIN:|992-getter|1|992-postInit
            Outputpatient.addCommand(getOutPatientbackCmd());
            Outputpatient.addCommand(getOutputPatientCmd());
            Outputpatient.setCommandListener(this);//GEN-END:|992-getter|1|992-postInit
            // write post-init user code here
        }//GEN-BEGIN:|992-getter|2|
        return Outputpatient;
    }
//</editor-fold>//GEN-END:|992-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem ">//GEN-BEGIN:|999-getter|0|999-preInit
    /**
     * Returns an initialized instance of stringItem component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {//GEN-END:|999-getter|0|999-preInit
            // write pre-init user code here
            stringItem = new StringItem(LocalizationSupport.getMessage("Outpatient"), null);//GEN-LINE:|999-getter|1|999-postInit
            // write post-init user code here
        }//GEN-BEGIN:|999-getter|2|
        return stringItem;
    }
//</editor-fold>//GEN-END:|999-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|1000-getter|0|1000-preInit
    /**
     * Returns an initialized instance of textField component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|1000-getter|0|1000-preInit
            // write pre-init user code here
            String str = getRecordValue(35);
            textField = new TextField(LocalizationSupport.getMessage("OPDALL"), str, 4, TextField.NUMERIC);//GEN-LINE:|1000-getter|1|1000-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1000-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|1000-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: HIVtestsconducted ">//GEN-BEGIN:|1001-getter|0|1001-preInit
    /**
     * Returns an initialized instance of HIVtestsconducted component.
     *
     * @return the initialized component instance
     */
    public Form getHIVtestsconducted() {
        if (HIVtestsconducted == null) {//GEN-END:|1001-getter|0|1001-preInit
            // write pre-init user code here
            HIVtestsconducted = new Form("Laboratory Testing ", new Item[]{getStringItem1(), getTextField1(), getTextField2(), getTextField3(), getTextField4(), getTextField5(), getTextField6()});//GEN-BEGIN:|1001-getter|1|1001-postInit
            HIVtestsconducted.addCommand(getHIVtestsconductedbackCmd());
            HIVtestsconducted.addCommand(getHIVtestsconductedCmd());
            HIVtestsconducted.setCommandListener(this);//GEN-END:|1001-getter|1|1001-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1001-getter|2|
        return HIVtestsconducted;
    }
//</editor-fold>//GEN-END:|1001-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem1 ">//GEN-BEGIN:|1008-getter|0|1008-preInit
    /**
     * Returns an initialized instance of stringItem1 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem1() {
        if (stringItem1 == null) {//GEN-END:|1008-getter|0|1008-preInit
            // write pre-init user code here
            stringItem1 = new StringItem(LocalizationSupport.getMessage("HIVtestsconducted"), null);//GEN-LINE:|1008-getter|1|1008-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1008-getter|2|
        return stringItem1;
    }
//</editor-fold>//GEN-END:|1008-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|1009-getter|0|1009-preInit
    /**
     * Returns an initialized instance of textField1 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|1009-getter|0|1009-preInit
            // write pre-init user code here
            String str = getRecordValue(43);
            textField1 = new TextField(LocalizationSupport.getMessage("M(numbertested)"), str, 4, TextField.NUMERIC);//GEN-LINE:|1009-getter|1|1009-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1009-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|1009-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|1010-getter|0|1010-preInit
    /**
     * Returns an initialized instance of textField2 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|1010-getter|0|1010-preInit
            // write pre-init user code here
            String str = getRecordValue(44);
            textField2 = new TextField(LocalizationSupport.getMessage("FNonANCtested"), str, 4, TextField.NUMERIC);//GEN-LINE:|1010-getter|1|1010-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1010-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|1010-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField3 ">//GEN-BEGIN:|1011-getter|0|1011-preInit
    /**
     * Returns an initialized instance of textField3 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField3() {
        if (textField3 == null) {//GEN-END:|1011-getter|0|1011-preInit
            // write pre-init user code here
            String str = getRecordValue(45);
            textField3 = new TextField(LocalizationSupport.getMessage("FwithANCtested"), str, 4, TextField.NUMERIC);//GEN-LINE:|1011-getter|1|1011-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1011-getter|2|
        return textField3;
    }
//</editor-fold>//GEN-END:|1011-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField4 ">//GEN-BEGIN:|1012-getter|0|1012-preInit
    /**
     * Returns an initialized instance of textField4 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField4() {
        if (textField4 == null) {//GEN-END:|1012-getter|0|1012-preInit
            // write pre-init user code here
            String str = getRecordValue(46);
            textField4 = new TextField(LocalizationSupport.getMessage("Mnumberpositive"), str, 4, TextField.NUMERIC);//GEN-LINE:|1012-getter|1|1012-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1012-getter|2|
        return textField4;
    }
//</editor-fold>//GEN-END:|1012-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField5 ">//GEN-BEGIN:|1013-getter|0|1013-preInit
    /**
     * Returns an initialized instance of textField5 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField5() {
        if (textField5 == null) {//GEN-END:|1013-getter|0|1013-preInit
            // write pre-init user code here
            String str = getRecordValue(47);
            textField5 = new TextField(LocalizationSupport.getMessage("FnonANCpositive"), str, 4, TextField.NUMERIC);//GEN-LINE:|1013-getter|1|1013-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1013-getter|2|
        return textField5;
    }
//</editor-fold>//GEN-END:|1013-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField6 ">//GEN-BEGIN:|1014-getter|0|1014-preInit
    /**
     * Returns an initialized instance of textField6 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField6() {
        if (textField6 == null) {//GEN-END:|1014-getter|0|1014-preInit
            // write pre-init user code here
            String str = getRecordValue(48);
            textField6 = new TextField(LocalizationSupport.getMessage("FwithANCpositive"), str, 4, TextField.NUMERIC);//GEN-LINE:|1014-getter|1|1014-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1014-getter|2|
        return textField6;
    }
//</editor-fold>//GEN-END:|1014-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OutPatientbackCmd ">//GEN-BEGIN:|993-getter|0|993-preInit
    /**
     * Returns an initialized instance of OutPatientbackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getOutPatientbackCmd() {
        if (OutPatientbackCmd == null) {//GEN-END:|993-getter|0|993-preInit
            // write pre-init user code here
            OutPatientbackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|993-getter|1|993-postInit
            // write post-init user code here
        }//GEN-BEGIN:|993-getter|2|
        return OutPatientbackCmd;
    }
//</editor-fold>//GEN-END:|993-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: OutputPatientCmd ">//GEN-BEGIN:|995-getter|0|995-preInit
    /**
     * Returns an initialized instance of OutputPatientCmd component.
     *
     * @return the initialized component instance
     */
    public Command getOutputPatientCmd() {
        if (OutputPatientCmd == null) {//GEN-END:|995-getter|0|995-preInit
            // write pre-init user code here
            OutputPatientCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|995-getter|1|995-postInit
            // write post-init user code here
        }//GEN-BEGIN:|995-getter|2|
        return OutputPatientCmd;
    }
//</editor-fold>//GEN-END:|995-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: HIVtestsconductedbackCmd ">//GEN-BEGIN:|1002-getter|0|1002-preInit
    /**
     * Returns an initialized instance of HIVtestsconductedbackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getHIVtestsconductedbackCmd() {
        if (HIVtestsconductedbackCmd == null) {//GEN-END:|1002-getter|0|1002-preInit
            // write pre-init user code here
            HIVtestsconductedbackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|1002-getter|1|1002-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1002-getter|2|
        return HIVtestsconductedbackCmd;
    }
//</editor-fold>//GEN-END:|1002-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: HIVtestsconductedCmd ">//GEN-BEGIN:|1004-getter|0|1004-preInit
    /**
     * Returns an initialized instance of HIVtestsconductedCmd component.
     *
     * @return the initialized component instance
     */
    public Command getHIVtestsconductedCmd() {
        if (HIVtestsconductedCmd == null) {//GEN-END:|1004-getter|0|1004-preInit
            // write pre-init user code here
            HIVtestsconductedCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|1004-getter|1|1004-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1004-getter|2|
        return HIVtestsconductedCmd;
    }
//</editor-fold>//GEN-END:|1004-getter|2|










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
            else if (allContent[i].length() > 1 && Integer.parseInt(allContent[i]) == 0){
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
    String myData =    textField20.getString() + ":" +
                    textField21.getString() + ":" +
                    textField22.getString() + ":" +
                    textField23.getString() + ":" +
                    textField24.getString() + ":" +
                    textField7.getString() + ":" +
                    textField8.getString() + ":" +
                    textField9.getString() + ":" +
                    textField25.getString() + ":" +

                    textField26.getString() + ":" +
                    textField27.getString() + ":" +
                    textField28.getString() + ":" +
                    textField29.getString() + ":" +
                    textField30.getString() + ":" +
                    textField31.getString() + ":" +

                    textField35.getString() + ":" +
                    textField37.getString() + ":" +

                    textField40.getString() + ":" +
                    textField41.getString() + ":" +
                    textField42.getString() + ":" +
                    textField43.getString() + ":" +

                    textField44.getString() + ":" +
                    textField45.getString() + ":" +

                    textField46.getString() + ":" +

                    textField.getString() + ":" +

                    textField47.getString() + ":" +
                    textField48.getString() + ":" +

                    textField49.getString() + ":" +
                    textField50.getString() + ":" +
                    textField51.getString() + ":" +

                    textField52.getString() + ":" +
                    textField53.getString() + ":" +

                    textField1.getString() + ":" +
                    textField2.getString() + ":" +
                    textField3.getString() + ":" +
                    textField4.getString() + ":" +
                    textField5.getString() + ":" +
                    textField6.getString() + ":" +

                    textField58.getString() + ":" +
                    textField11.getString() + ":" +

                    textField59.getString() + ":" +
                    textField60.getString() + ":" +
                    textField61.getString() + ":" +
                    textField32.getString() + ":" +
                    textField33.getString() + ":" +
                    textField34.getString() + ":" +

                    textField62.getString() + ":" +
                    textField63.getString() + ":" +
                    textField64.getString() ;                
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
        
        // create myReturnData to content the last data to return
        String myReturnData = "";
        
        int characters = 0;  // THE CHARACTERS OF DATA on one SMS
        int seperateIndex = 0; // THE INDEX OF DATA IN STRING [] myDataTemp blow
        
        formID = 3;
        String periodType = "3";
        String subCenter = subCenterCode.getString();
        //return "HP NRHM "+ msgVersion + "!" + formID + "*" + periodType + "?" + monthData + "$" + myData;
        
        // If the message too long, seperate to 2 message
        // lengh of "HP NRHM "+ + subCenter + formID + "*" + monthData + "$" + "88" + "?" is 22 characters
        if(myData.length() > 138 - 40)
        {
            // assign data into String [] myDataTemp
            String[] myDataTemp = split(myData, ":");
            // reset myData
            myData = "";

            while(seperateIndex < myDataTemp.length){
                // To get the Index of beginning dataelement in the csv file
                int csvIndex = seperateIndex + 88; // PHC form 1 ended at index 87 in CSV file
                String addIndex = Integer.toString(csvIndex);
                
                if (addIndex.length()<2)
                    addIndex = "0"+ addIndex;
                
                // To correct the sms message.
                int offset = 0;
                if(addIndex.length() == 3)
                    offset = 1;
                
                // reset characters;
                characters = 1;
                // reset myData
                myData = "";
                // if characters <= 138 - offset - 40.
                while(characters < 139 - offset - 40){
                if(myData.length() + myDataTemp[seperateIndex].length() < 139 - offset - 40){
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
        } // end if myData.leght > 
        else{
            myReturnData = "HP NRHM "+ subCenter + formID + "*" + monthData + "$" + "88" + "?" + myData;
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
                    lastMsgStore.setRecord(11, textField20.getString().getBytes(), 0,  textField20.getString().length());
                    lastMsgStore.setRecord(12, textField21.getString().getBytes(), 0,  textField21.getString().length());
                    lastMsgStore.setRecord(13, textField22.getString().getBytes(), 0,  textField22.getString().length());
                    lastMsgStore.setRecord(14, textField23.getString().getBytes(), 0,  textField23.getString().length());
                    lastMsgStore.setRecord(15, textField24.getString().getBytes(), 0,  textField24.getString().length());
                    lastMsgStore.setRecord(16, textField7.getString().getBytes(),  0,  textField7.getString().length());
                    lastMsgStore.setRecord(17, textField8.getString().getBytes(),  0,  textField8.getString().length());
                    lastMsgStore.setRecord(18, textField9.getString().getBytes(),  0,  textField9.getString().length());
                    lastMsgStore.setRecord(19, textField25.getString().getBytes(), 0,  textField25.getString().length());

                    lastMsgStore.setRecord(20, textField26.getString().getBytes(), 0,  textField26.getString().length());
                    lastMsgStore.setRecord(21, textField27.getString().getBytes(), 0,  textField27.getString().length());
                    lastMsgStore.setRecord(22, textField28.getString().getBytes(), 0,  textField28.getString().length());
                    lastMsgStore.setRecord(23, textField29.getString().getBytes(), 0,  textField29.getString().length());
                    lastMsgStore.setRecord(24, textField30.getString().getBytes(), 0,  textField30.getString().length());
                    lastMsgStore.setRecord(25, textField31.getString().getBytes(), 0,  textField31.getString().length());

                    lastMsgStore.setRecord(26, textField35.getString().getBytes(), 0, textField35.getString().length());
                    lastMsgStore.setRecord(27, textField37.getString().getBytes(), 0, textField37.getString().length());

                    lastMsgStore.setRecord(28, textField40.getString().getBytes(), 0, textField40.getString().length());
                    lastMsgStore.setRecord(29, textField41.getString().getBytes(), 0, textField41.getString().length());
                    lastMsgStore.setRecord(30, textField42.getString().getBytes(), 0, textField42.getString().length());
                    lastMsgStore.setRecord(31, textField43.getString().getBytes(), 0, textField43.getString().length());

                    lastMsgStore.setRecord(32, textField44.getString().getBytes(), 0, textField44.getString().length());
                    lastMsgStore.setRecord(33, textField45.getString().getBytes(), 0, textField45.getString().length());

                    lastMsgStore.setRecord(34, textField46.getString().getBytes(), 0, textField46.getString().length());

                    lastMsgStore.setRecord(35, textField.getString().getBytes(), 0, textField.getString().length());

                    lastMsgStore.setRecord(36, textField47.getString().getBytes(), 0, textField47.getString().length());
                    lastMsgStore.setRecord(37, textField48.getString().getBytes(), 0, textField48.getString().length());

                    lastMsgStore.setRecord(38, textField49.getString().getBytes(), 0, textField49.getString().length());
                    lastMsgStore.setRecord(39, textField50.getString().getBytes(), 0, textField50.getString().length());
                    lastMsgStore.setRecord(40, textField51.getString().getBytes(), 0, textField51.getString().length());

                    lastMsgStore.setRecord(41, textField52.getString().getBytes(), 0, textField52.getString().length());
                    lastMsgStore.setRecord(42, textField53.getString().getBytes(), 0, textField53.getString().length());

                    lastMsgStore.setRecord(43, textField1.getString().getBytes(), 0, textField1.getString().length());
                    lastMsgStore.setRecord(44, textField2.getString().getBytes(), 0, textField2.getString().length());
                    lastMsgStore.setRecord(45, textField3.getString().getBytes(), 0, textField3.getString().length());
                    lastMsgStore.setRecord(46, textField4.getString().getBytes(), 0, textField4.getString().length());
                    lastMsgStore.setRecord(47, textField5.getString().getBytes(), 0, textField5.getString().length());
                    lastMsgStore.setRecord(48, textField6.getString().getBytes(), 0, textField6.getString().length());

                    lastMsgStore.setRecord(49, textField58.getString().getBytes(), 0, textField58.getString().length());
                    lastMsgStore.setRecord(50, textField11.getString().getBytes(), 0, textField11.getString().length());

                    lastMsgStore.setRecord(51, textField59.getString().getBytes(), 0, textField59.getString().length());
                    lastMsgStore.setRecord(52, textField60.getString().getBytes(), 0, textField60.getString().length());
                    lastMsgStore.setRecord(53, textField61.getString().getBytes(), 0, textField61.getString().length());
                    lastMsgStore.setRecord(54, textField32.getString().getBytes(), 0, textField32.getString().length());
                    lastMsgStore.setRecord(55, textField33.getString().getBytes(), 0,textField33.getString().length());
                    lastMsgStore.setRecord(56, textField34.getString().getBytes(), 0, textField34.getString().length());

                    lastMsgStore.setRecord(57, textField62.getString().getBytes(), 0, textField62.getString().length());
                    lastMsgStore.setRecord(58, textField63.getString().getBytes(), 0, textField63.getString().length());
                    lastMsgStore.setRecord(59, textField64.getString().getBytes(), 0, textField64.getString().length());
                    
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