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
    private ChoiceGroup monthChoice;
    private ChoiceGroup freqGroup;
    private ChoiceGroup yearChoiceGroup;
    private SplashScreen splashScreen;
    private Form loadPage;
    private StringItem questionLabel;
    private ChoiceGroup lastChoice;
    private ImageItem questionImage;
    private Form settingsPage;
    private TextField phone3Num;
    private TextField phone1Num;
    private TextField phone2Num;
    private Form Fever;
    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
    private TextField textField;
    private StringItem stringItem;
    private Form MaleDeathsGT5yrs;
    private StringItem stringItem10;
    private TextField textField21;
    private TextField textField20;
    private TextField textField23;
    private TextField textField22;
    private Form Fevermorethan7days;
    private TextField textField37;
    private TextField textField38;
    private TextField textField35;
    private TextField textField36;
    private TextField textField33;
    private TextField textField34;
    private TextField textField32;
    private TextField textField39;
    private Form FemaledeathsLT5yrs;
    private StringItem stringItem9;
    private TextField textField30;
    private TextField textField31;
    private TextField textField28;
    private TextField textField29;
    private Form MalecasesGT5yrs;
    private StringItem stringItem2;
    private TextField textField9;
    private TextField textField8;
    private TextField textField11;
    private TextField textField10;
    private Form Maledeaths5yrs;
    private StringItem stringItem8;
    private TextField textField18;
    private TextField textField17;
    private TextField textField16;
    private TextField textField19;
    private Form FemalecasesGT5yrs;
    private StringItem stringItem7;
    private TextField textField14;
    private TextField textField15;
    private TextField textField13;
    private TextField textField12;
    private Form Weekspage;
    private Form Morethan3weeks;
    private StringItem stringItem12;
    private TextField textField54;
    private TextField textField55;
    private TextField textField50;
    private TextField textField51;
    private TextField textField52;
    private TextField textField53;
    private TextField textField49;
    private TextField textField48;
    private Form Coughwithorwithoutfever;
    private TextField textField46;
    private TextField textField45;
    private TextField textField44;
    private TextField textField43;
    private TextField textField42;
    private TextField textField41;
    private TextField textField40;
    private TextField textField47;
    private StringItem stringItem4;
    private Form Femalecasesmorethan5yrs;
    private StringItem stringItem19;
    private TextField textField65;
    private TextField textField66;
    private TextField textField67;
    private Form Maledeathslessthan5yrs;
    private StringItem stringItem16;
    private TextField textField68;
    private TextField textField70;
    private TextField textField69;
    private Form Femaledeathslessthan5yrs;
    private StringItem stringItem17;
    private TextField textField72;
    private TextField textField71;
    private TextField textField73;
    private Form Loosewaterystoolsoflessthan2weeksduration;
    private TextField textField57;
    private TextField textField58;
    private StringItem stringItem1;
    private TextField textField56;
    private Form Femalecaseslessthan5yrs;
    private StringItem stringItem13;
    private TextField textField61;
    private TextField textField59;
    private TextField textField60;
    private Form Malecasesmorethan5yrs;
    private StringItem stringItem14;
    private TextField textField62;
    private TextField textField63;
    private TextField textField64;
    private Form FemalecasesLT5yrs;
    private TextField textField4;
    private TextField textField7;
    private TextField textField5;
    private TextField textField6;
    private StringItem stringItem6;
    private Form FemaledeathsGT5yrs;
    private StringItem stringItem11;
    private TextField textField27;
    private TextField textField26;
    private TextField textField25;
    private TextField textField24;
    private Form Unusualsymptomsleadingtodeath;
    private TextField textField100;
    private TextField textField99;
    private TextField textField102;
    private TextField textField101;
    private TextField textField103;
    private TextField textField97;
    private TextField textField98;
    private TextField textField96;
    private Form AFPcaseslessthan15yrsofage;
    private TextField textField93;
    private TextField textField94;
    private TextField textField91;
    private TextField textField92;
    private TextField textField95;
    private TextField textField89;
    private TextField textField90;
    private StringItem stringItem5;
    private TextField textField88;
    private Form Jaundicecasesoflessthan4weeksduration;
    private TextField textField86;
    private TextField textField87;
    private TextField textField84;
    private TextField textField85;
    private StringItem stringItem3;
    private TextField textField83;
    private TextField textField82;
    private TextField textField81;
    private TextField textField80;
    private Form Femaledeathsmorethan5yrs;
    private StringItem stringItem15;
    private TextField textField79;
    private TextField textField78;
    private TextField textField77;
    private Form Maledeathsmorethan5yrs;
    private StringItem stringItem18;
    private TextField textField76;
    private TextField textField75;
    private TextField textField74;
    private Form subcentrecode;
    private TextField subCentreCode;
    private Command sendBackCmd;
    private Command sendCmd;
    private Command monthCmd;
    private Command sendExitCmd;
    private Command loadExitCmd;
    private Command loadCmd;
    private Command sendSettingsCmd;
    private Command settingsBackCmd;
    private Command settingsCmd;
    private Command pregnancyoutcomeBackCmd;
    private Command sendGPRSCommand;
    private Command monthExitCmd;
    private Command antinatalcareBackCmd;
    private Command deliveryCmd;
    private Command deliveryBackCmd;
    private Command antinatalcareCmd;
    private Command pregnancyoutcomeCmd;
    private Command MaternalDeathsCmd;
    private Command MaternalDeathsBackCmd;
    private Command Mortalitydatails_infantsBackcmd;
    private Command Infantdeathsupto4weeksbycauseCmd;
    private Command Mortalitydatails_infantsCmd;
    private Command Infantdeathsupto4weeksbycauseBackCmd;
    private Command AdolescentAdultdeathsbycauseBackCmd;
    private Command AdolescentAdultdeathsbycauseCmd;
    private Command Infantchilddeathsupto5yearsbycauseBackCmd;
    private Command Infantchilddeathsupto5yearsbycauseCmd;
    private Command AdolescentAdultdeathsbycauseCmd02;
    private Command AdolescentAdultdeathsbycauseBackCmd02;
    private Command AdolescentAdultdeathsbycauseBackCmd01;
    private Command AdolescentAdultdeathsbycauseCmd01;
    private Command backCommand3;
    private Command okCommand2;
    private Command backCommand2;
    private Command okCommand1;
    private Command backCommand1;
    private Command okCommand;
    private Command backCommand;
    private Command backCommand7;
    private Command okCommand6;
    private Command backCommand6;
    private Command okCommand5;
    private Command backCommand5;
    private Command okCommand4;
    private Command backCommand4;
    private Command okCommand3;
    private Command saveCmd;
    private Command FeverOk;
    private Command FeverBackCmd;
    private Command Maledeaths5yrsOkCmd1;
    private Command Maledeaths5yrsBackCmd1;
    private Command Fevermorethan7daysOkCmd;
    private Command Fevermorethan7daysBackCmd;
    private Command FemaledeathsLT5yrsOkCmd;
    private Command FemaledeathsLT5yrsBackCmd;
    private Command Malecases5yrsBackCmd;
    private Command Malecases5yrsOkCmd;
    private Command okCommand7;
    private Command Maledeaths5yrsBackCmd;
    private Command Maledeaths5yrsOkCmd;
    private Command Femalecases5yrsBackCmd;
    private Command Femalecases5yrsOkCmd;
    private Command cancelCommand;
    private Command WeekspageExitCmd;
    private Command WeekspageBackCmd;
    private Command WeekspageOkCmd;
    private Command Morethan3weeksOkCmd;
    private Command Morethan3weeksBackCmd;
    private Command CoughwithorwithoutfeverOkCmd;
    private Command CoughwithorwithoutfeverBackCmd;
    private Command Malecasesmorethan5yrsokcmd;
    private Command Femalecasesmorethan5yrsbackcmd;
    private Command Femalecasesmorethan5yrsokcmd;
    private Command Maledeathslessthan5yrsbackcmd;
    private Command Maledeathslessthan5yrsokcmd;
    private Command Femaledeathslessthan5yrsbackcmd;
    private Command Femaledeathslessthan5yrsokcmd;
    private Command loosewaterystoolsbackCmd;
    private Command loosewaterystoolsokCmd;
    private Command FemalecasesLT5yrsbackcmd;
    private Command FemalecasesLT5yrsokcmd;
    private Command Malecasesmorethan5yrsbackcmd;
    private Command FemaledeathsGT5yrsOkCmd;
    private Command FemaledeathsGT5yrsBackcmd;
    private Command FemalecasesLT5yrsBackCmd;
    private Command FemalecasesLT5yrsOkCmd;
    private Command Unusualsymptomsleadingtodeathokcmd;
    private Command Unusualsymptomsleadingtodeathbackcmd;
    private Command AFPcaseslessthan15yrsofageokcmd;
    private Command AFPcaseslessthan15yrsofagebackcmd;
    private Command Jaundicecasesoflessthan4weeksdurationokcmd;
    private Command Jaundicecasesoflessthan4weeksdurationbackcmd;
    private Command Femaledeathsmorethan5yrsokcmd;
    private Command Femaledeathsmorethan5yrsbackcmd;
    private Command cancelCommand1;
    private Command Maledeathsmorethan5yrsokcmd;
    private Command Maledeathsmorethan5yrsbackcmd;
    private Command monthBackCmd;
    private Command subcentreExistCmd;
    private Command subcentrecodeCmd;
    private Command subcentrecodeBackCmd;
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
                for (int i = 0; i < 114; i++) {
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
        if (displayable == AFPcaseslessthan15yrsofage) {//GEN-BEGIN:|7-commandAction|1|916-preAction
            if (command == AFPcaseslessthan15yrsofagebackcmd) {//GEN-END:|7-commandAction|1|916-preAction
                // write pre-action user code here
                switchDisplayable(null, getJaundicecasesoflessthan4weeksduration());//GEN-LINE:|7-commandAction|2|916-postAction
                // write post-action user code here
            } else if (command == AFPcaseslessthan15yrsofageokcmd) {//GEN-LINE:|7-commandAction|3|918-preAction
                // write pre-action user code here
                switchDisplayable(null, getUnusualsymptomsleadingtodeath());//GEN-LINE:|7-commandAction|4|918-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|779-preAction
        } else if (displayable == Coughwithorwithoutfever) {
            if (command == CoughwithorwithoutfeverBackCmd) {//GEN-END:|7-commandAction|5|779-preAction
                // write pre-action user code here
                switchDisplayable(null, getFevermorethan7days());//GEN-LINE:|7-commandAction|6|779-postAction
                // write post-action user code here
            } else if (command == CoughwithorwithoutfeverOkCmd) {//GEN-LINE:|7-commandAction|7|781-preAction
                // write pre-action user code here
                switchDisplayable(null, getMorethan3weeks());//GEN-LINE:|7-commandAction|8|781-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|724-preAction
        } else if (displayable == FemalecasesGT5yrs) {
            if (command == Femalecases5yrsBackCmd) {//GEN-END:|7-commandAction|9|724-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalecasesGT5yrs());//GEN-LINE:|7-commandAction|10|724-postAction
                // write post-action user code here
            } else if (command == Femalecases5yrsOkCmd) {//GEN-LINE:|7-commandAction|11|726-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeaths5yrs());//GEN-LINE:|7-commandAction|12|726-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|840-preAction
        } else if (displayable == FemalecasesLT5yrs) {
            if (command == FemalecasesLT5yrsBackCmd) {//GEN-END:|7-commandAction|13|840-preAction
                // write pre-action user code here
                switchDisplayable(null, getFever());//GEN-LINE:|7-commandAction|14|840-postAction
                // write post-action user code here
            } else if (command == FemalecasesLT5yrsOkCmd) {//GEN-LINE:|7-commandAction|15|842-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalecasesGT5yrs());//GEN-LINE:|7-commandAction|16|842-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|874-preAction
        } else if (displayable == Femalecaseslessthan5yrs) {
            if (command == FemalecasesLT5yrsbackcmd) {//GEN-END:|7-commandAction|17|874-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoosewaterystoolsoflessthan2weeksduration());//GEN-LINE:|7-commandAction|18|874-postAction
                // write post-action user code here
            } else if (command == FemalecasesLT5yrsokcmd) {//GEN-LINE:|7-commandAction|19|876-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalecasesmorethan5yrs());//GEN-LINE:|7-commandAction|20|876-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|884-preAction
        } else if (displayable == Femalecasesmorethan5yrs) {
            if (command == Femalecasesmorethan5yrsbackcmd) {//GEN-END:|7-commandAction|21|884-preAction
                // write pre-action user code here
                switchDisplayable(null, getMalecasesmorethan5yrs());//GEN-LINE:|7-commandAction|22|884-postAction
                // write post-action user code here
            } else if (command == Femalecasesmorethan5yrsokcmd) {//GEN-LINE:|7-commandAction|23|886-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeathslessthan5yrs());//GEN-LINE:|7-commandAction|24|886-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|849-preAction
        } else if (displayable == FemaledeathsGT5yrs) {
            if (command == FemaledeathsGT5yrsBackcmd) {//GEN-END:|7-commandAction|25|849-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaleDeathsGT5yrs());//GEN-LINE:|7-commandAction|26|849-postAction
                // write post-action user code here
            } else if (command == FemaledeathsGT5yrsOkCmd) {//GEN-LINE:|7-commandAction|27|851-preAction
                // write pre-action user code here
                switchDisplayable(null, getFevermorethan7days());//GEN-LINE:|7-commandAction|28|851-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|757-preAction
        } else if (displayable == FemaledeathsLT5yrs) {
            if (command == FemaledeathsLT5yrsBackCmd) {//GEN-END:|7-commandAction|29|757-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeaths5yrs());//GEN-LINE:|7-commandAction|30|757-postAction
                // write post-action user code here
            } else if (command == FemaledeathsLT5yrsOkCmd) {//GEN-LINE:|7-commandAction|31|759-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaleDeathsGT5yrs());//GEN-LINE:|7-commandAction|32|759-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|894-preAction
        } else if (displayable == Femaledeathslessthan5yrs) {
            if (command == Femaledeathslessthan5yrsbackcmd) {//GEN-END:|7-commandAction|33|894-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeathslessthan5yrs());//GEN-LINE:|7-commandAction|34|894-postAction
                // write post-action user code here
            } else if (command == Femaledeathslessthan5yrsokcmd) {//GEN-LINE:|7-commandAction|35|896-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeathsmorethan5yrs());//GEN-LINE:|7-commandAction|36|896-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|906-preAction
        } else if (displayable == Femaledeathsmorethan5yrs) {
            if (command == Femaledeathsmorethan5yrsbackcmd) {//GEN-END:|7-commandAction|37|906-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaledeathsmorethan5yrs());//GEN-LINE:|7-commandAction|38|906-postAction
                // write post-action user code here
            } else if (command == Femaledeathsmorethan5yrsokcmd) {//GEN-LINE:|7-commandAction|39|908-preAction
                // write pre-action user code here
                switchDisplayable(null, getJaundicecasesoflessthan4weeksduration());//GEN-LINE:|7-commandAction|40|908-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|41|664-preAction
        } else if (displayable == Fever) {
            if (command == FeverBackCmd) {//GEN-END:|7-commandAction|41|664-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|42|664-postAction
                // write post-action user code here
            } else if (command == FeverOk) {//GEN-LINE:|7-commandAction|43|666-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesLT5yrs());//GEN-LINE:|7-commandAction|44|666-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|45|766-preAction
        } else if (displayable == Fevermorethan7days) {
            if (command == Fevermorethan7daysBackCmd) {//GEN-END:|7-commandAction|45|766-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsGT5yrs());//GEN-LINE:|7-commandAction|46|766-postAction
                // write post-action user code here
            } else if (command == Fevermorethan7daysOkCmd) {//GEN-LINE:|7-commandAction|47|768-preAction
                // write pre-action user code here
                switchDisplayable(null, getCoughwithorwithoutfever());//GEN-LINE:|7-commandAction|48|768-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|49|911-preAction
        } else if (displayable == Jaundicecasesoflessthan4weeksduration) {
            if (command == Jaundicecasesoflessthan4weeksdurationbackcmd) {//GEN-END:|7-commandAction|49|911-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsmorethan5yrs());//GEN-LINE:|7-commandAction|50|911-postAction
                // write post-action user code here
            } else if (command == Jaundicecasesoflessthan4weeksdurationokcmd) {//GEN-LINE:|7-commandAction|51|913-preAction
                // write pre-action user code here
                switchDisplayable(null, getAFPcaseslessthan15yrsofage());//GEN-LINE:|7-commandAction|52|913-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|53|869-preAction
        } else if (displayable == Loosewaterystoolsoflessthan2weeksduration) {
            if (command == loosewaterystoolsbackCmd) {//GEN-END:|7-commandAction|53|869-preAction
                // write pre-action user code here
                switchDisplayable(null, getMorethan3weeks());//GEN-LINE:|7-commandAction|54|869-postAction
                // write post-action user code here
            } else if (command == loosewaterystoolsokCmd) {//GEN-LINE:|7-commandAction|55|871-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecaseslessthan5yrs());//GEN-LINE:|7-commandAction|56|871-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|57|743-preAction
        } else if (displayable == MaleDeathsGT5yrs) {
            if (command == Maledeaths5yrsBackCmd1) {//GEN-END:|7-commandAction|57|743-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsLT5yrs());//GEN-LINE:|7-commandAction|58|743-postAction
                // write post-action user code here
            } else if (command == Maledeaths5yrsOkCmd1) {//GEN-LINE:|7-commandAction|59|745-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsGT5yrs());//GEN-LINE:|7-commandAction|60|745-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|61|713-preAction
        } else if (displayable == MalecasesGT5yrs) {
            if (command == Malecases5yrsBackCmd) {//GEN-END:|7-commandAction|61|713-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesLT5yrs());//GEN-LINE:|7-commandAction|62|713-postAction
                // write post-action user code here
            } else if (command == Malecases5yrsOkCmd) {//GEN-LINE:|7-commandAction|63|715-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesGT5yrs());//GEN-LINE:|7-commandAction|64|715-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|65|879-preAction
        } else if (displayable == Malecasesmorethan5yrs) {
            if (command == Malecasesmorethan5yrsbackcmd) {//GEN-END:|7-commandAction|65|879-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecaseslessthan5yrs());//GEN-LINE:|7-commandAction|66|879-postAction
                // write post-action user code here
            } else if (command == Malecasesmorethan5yrsokcmd) {//GEN-LINE:|7-commandAction|67|881-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesmorethan5yrs());//GEN-LINE:|7-commandAction|68|881-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|69|734-preAction
        } else if (displayable == Maledeaths5yrs) {
            if (command == Maledeaths5yrsBackCmd) {//GEN-END:|7-commandAction|69|734-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesGT5yrs());//GEN-LINE:|7-commandAction|70|734-postAction
                // write post-action user code here
            } else if (command == Maledeaths5yrsOkCmd) {//GEN-LINE:|7-commandAction|71|736-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsLT5yrs());//GEN-LINE:|7-commandAction|72|736-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|73|889-preAction
        } else if (displayable == Maledeathslessthan5yrs) {
            if (command == Maledeathslessthan5yrsbackcmd) {//GEN-END:|7-commandAction|73|889-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemalecasesmorethan5yrs());//GEN-LINE:|7-commandAction|74|889-postAction
                // write post-action user code here
            } else if (command == Maledeathslessthan5yrsokcmd) {//GEN-LINE:|7-commandAction|75|891-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathslessthan5yrs());//GEN-LINE:|7-commandAction|76|891-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|77|899-preAction
        } else if (displayable == Maledeathsmorethan5yrs) {
            if (command == Maledeathsmorethan5yrsbackcmd) {//GEN-END:|7-commandAction|77|899-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathslessthan5yrs());//GEN-LINE:|7-commandAction|78|899-postAction
                // write post-action user code here
            } else if (command == Maledeathsmorethan5yrsokcmd) {//GEN-LINE:|7-commandAction|79|901-preAction
                // write pre-action user code here
                switchDisplayable(null, getFemaledeathsmorethan5yrs());//GEN-LINE:|7-commandAction|80|901-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|81|795-preAction
        } else if (displayable == Morethan3weeks) {
            if (command == Morethan3weeksBackCmd) {//GEN-END:|7-commandAction|81|795-preAction
                // write pre-action user code here
                switchDisplayable(null, getCoughwithorwithoutfever());//GEN-LINE:|7-commandAction|82|795-postAction
                // write post-action user code here
            } else if (command == Morethan3weeksOkCmd) {//GEN-LINE:|7-commandAction|83|797-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoosewaterystoolsoflessthan2weeksduration());//GEN-LINE:|7-commandAction|84|797-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|85|921-preAction
        } else if (displayable == Unusualsymptomsleadingtodeath) {
            if (command == Unusualsymptomsleadingtodeathbackcmd) {//GEN-END:|7-commandAction|85|921-preAction
                // write pre-action user code here
                switchDisplayable(null, getAFPcaseslessthan15yrsofage());//GEN-LINE:|7-commandAction|86|921-postAction
                // write post-action user code here
            } else if (command == Unusualsymptomsleadingtodeathokcmd) {//GEN-LINE:|7-commandAction|87|923-preAction
                // write pre-action user code here
                int negativeTextField = getEmptyFields();
                if(negativeTextField > 0){
                    if(negativeTextField > 1){
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data elements are negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,Unusualsymptomsleadingtodeath);
                    }
                    else {
                        Alert myAlert = new Alert("Negative Value",negativeTextField + " Data element is negative value!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,Unusualsymptomsleadingtodeath);
                    }
                }
                else{
                    switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|88|923-postAction
                }// write post-action user code here
            }//GEN-BEGIN:|7-commandAction|89|814-preAction
        } else if (displayable == Weekspage) {
            if (command == WeekspageExitCmd) {//GEN-END:|7-commandAction|89|814-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|90|814-postAction
                // write post-action user code here
            } else if (command == WeekspageOkCmd) {//GEN-LINE:|7-commandAction|91|812-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|92|812-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|93|226-preAction
        } else if (displayable == loadPage) {
            if (command == loadCmd) {//GEN-END:|7-commandAction|93|226-preAction
                int lastSelected = lastChoice.getSelectedIndex();
                if (lastSelected == 0) {
                    editingLastReport = true;
                } else {
                    editingLastReport = false;
                }
                switchDisplayable(null, getSubcentrecode());//GEN-LINE:|7-commandAction|94|226-postAction
                // write post-action user code here
            } else if (command == loadExitCmd) {//GEN-LINE:|7-commandAction|95|228-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|96|228-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|97|1017-preAction
        } else if (displayable == monthPage) {
            if (command == monthBackCmd) {//GEN-END:|7-commandAction|97|1017-preAction
                // write pre-action user code here
                switchDisplayable(null, getSubcentrecode());//GEN-LINE:|7-commandAction|98|1017-postAction
                // write post-action user code here
            } else if (command == monthCmd) {//GEN-LINE:|7-commandAction|99|180-preAction
                // write pre-action user code here
                switchDisplayable(null, getFever());//GEN-LINE:|7-commandAction|100|180-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|101|653-preAction
        } else if (displayable == sendPage) {
            if (command == saveCmd) {//GEN-END:|7-commandAction|101|653-preAction
                // write pre-action user code here
                
                // for week data
                final String monthStr = "" + (monthChoice.getSelectedIndex() + 1);
                // for year 
                final String freqStr = yearChoiceGroup.getString(yearChoiceGroup.getSelectedIndex());
                saveDataToRMS(monthStr, freqStr);
                Alert myAlert = new Alert("Save success","Your data has been saved!",null,AlertType.INFO);
                myAlert.setTimeout(2000);
                Display.getDisplay(this).setCurrent(myAlert,sendPage);
//GEN-LINE:|7-commandAction|102|653-postAction
                // write post-action user code here
            } else if (command == sendBackCmd) {//GEN-LINE:|7-commandAction|103|171-preAction
                // write pre-action user code here
                switchDisplayable(null, getUnusualsymptomsleadingtodeath());//GEN-LINE:|7-commandAction|104|171-postAction
                // write post-action user code here
            } else if (command == sendCmd) {//GEN-LINE:|7-commandAction|105|169-preAction
                sendMsgLabel.setText("Sending SMS...");
                final String monthStr = ""+ (monthChoice.getSelectedIndex() + 1);
                final String freqStr = yearChoiceGroup.getString(yearChoiceGroup.getSelectedIndex());

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

//GEN-LINE:|7-commandAction|106|169-postAction
                // write post-action user code here
            } else if (command == sendExitCmd) {//GEN-LINE:|7-commandAction|107|207-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|108|207-postAction
                // write post-action user code here
            } else if (command == sendSettingsCmd) {//GEN-LINE:|7-commandAction|109|255-preAction
                // write pre-action user code here
                switchDisplayable(null, getSettingsPage());//GEN-LINE:|7-commandAction|110|255-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|111|246-preAction
        } else if (displayable == settingsPage) {
            if (command == settingsBackCmd) {//GEN-END:|7-commandAction|111|246-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|112|246-postAction
                // write post-action user code here
            } else if (command == settingsCmd) {//GEN-LINE:|7-commandAction|113|243-preAction
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
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|114|243-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|115|203-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|115|203-preAction
                if (savedMsg == false) {
                    switchDisplayable(null, getSubcentrecode());
                } else {
                    switchDisplayable(null, getLoadPage());//GEN-LINE:|7-commandAction|116|203-postAction
                }
            }//GEN-BEGIN:|7-commandAction|117|1020-preAction
        } else if (displayable == subcentrecode) {
            if (command == subcentreExistCmd) {//GEN-END:|7-commandAction|117|1020-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|118|1020-postAction
                // write post-action user code here
            } else if (command == subcentrecodeCmd) {//GEN-LINE:|7-commandAction|119|1012-preAction
                // write pre-action user code here
                int digits = subCentreCode.getString().length();
                String digitsStr = subCentreCode.getString();
                if(digits == 0){
                        Alert myAlert = new Alert("Missing Facility Code","Please enter Facility Code!",null,AlertType.INFO);
                        myAlert.setTimeout(3000);
                        Display.getDisplay(this).setCurrent(myAlert,subcentrecode);
                }
                else if (digits > 0 && digits < 4  ) {
                    Alert myAlert = new Alert("Wrong Facility Code","The Facility Code has 4 digits!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,subcentrecode);
                }
                else if (digitsStr.length() > 1 && Integer.parseInt(digitsStr) == 0){
                    Alert myAlert = new Alert("Wrong Facility Code","In-correct Facility Code!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,subcentrecode);
                }
                else if (Integer.parseInt(digitsStr) < 0){
                    Alert myAlert = new Alert("Wrong Facility Code","In-correct Facility Code!",null,AlertType.INFO);
                    myAlert.setTimeout(3000);
                    Display.getDisplay(this).setCurrent(myAlert,subcentrecode);
                }
                else{
                    switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|120|1012-postAction
                }// write post-action user code here
            }//GEN-BEGIN:|7-commandAction|121|7-postCommandAction
        }//GEN-END:|7-commandAction|121|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|122|
//</editor-fold>//GEN-END:|7-commandAction|122|

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
            monthPage = new Form(LocalizationSupport.getMessage("monthPageTitle"), new Item[]{getYearChoiceGroup(), getFreqGroup(), getMonthChoice()});//GEN-BEGIN:|178-getter|1|178-postInit
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
            monthChoice = new ChoiceGroup(LocalizationSupport.getMessage("weekChoiceLabel"), Choice.POPUP);//GEN-BEGIN:|189-getter|1|189-postInit
            monthChoice.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_2);
            monthChoice.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);//GEN-END:|189-getter|1|189-postInit
            // write pre-init user code here
            try {
                // Create week choice list
                String [] weekNames = {
                "week 1","week 2","week 3","week 4","week 5","week 6","week 7","week 8","week 9","week 10",
                "week 11","week 12","week 13","week 14","week 15","week 16","week 17","week 18","week 19","week 20",
                "week 21","week 22","week 23","week 24","week 25","week 26","week 27","week 28","week 29","week 30",
                "week 31","week 32","week 33","week 34","week 35","week 36","week 37","week 38","week 39","week 40",
                "week 41","week 42","week 43","week 44","week 45","week 46","week 47","week 48","week 49","week 50",
                "week 51","week 52"
                };
                
                
                for (int i = 0; i < 52; i++){
                    monthChoice.append(weekNames[i], null);
                }
                
                //setdefault selected item in monthChoice
                int index = 0;
                if (editingLastReport) {
                        
                        if (lastMsgStore.getRecord(10) != null) {
                        
                            index = Integer.parseInt(new String(lastMsgStore.getRecord(10)));
                            if (index < 1)
                                index = 1;
                            monthChoice.setSelectedIndex(index - 1, true); // need the last report value
                        }
                    } else {
                        // Set to the last week
                        Calendar cal = Calendar.getInstance();
                        index = orderOfCurrentWeek(cal.get(Calendar.YEAR)) - 1;
                        if(index < 0)
                            index = 0;
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
            phone1Num = new TextField(LocalizationSupport.getMessage("phone1Num"), str, 10, TextField.PHONENUMBER);//GEN-LINE:|248-getter|1|248-postInit
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
            phone2Num = new TextField(LocalizationSupport.getMessage("phone2Num"), str, 10, TextField.PHONENUMBER);//GEN-LINE:|249-getter|1|249-postInit
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
            phone3Num = new TextField(LocalizationSupport.getMessage("phone3Num"), str, 10, TextField.PHONENUMBER);//GEN-LINE:|250-getter|1|250-postInit
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


//<editor-fold defaultstate="collapsed" desc=" Generated Getter: pregnancyoutcomeBackCmd ">//GEN-BEGIN:|479-getter|0|479-preInit
    /**
     * Returns an initialized instance of pregnancyoutcomeBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPregnancyoutcomeBackCmd() {
        if (pregnancyoutcomeBackCmd == null) {//GEN-END:|479-getter|0|479-preInit
            // write pre-init user code here
            pregnancyoutcomeBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|479-getter|1|479-postInit
            // write post-init user code here
        }//GEN-BEGIN:|479-getter|2|
        return pregnancyoutcomeBackCmd;
    }
//</editor-fold>//GEN-END:|479-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: pregnancyoutcomeCmd ">//GEN-BEGIN:|482-getter|0|482-preInit
    /**
     * Returns an initialized instance of pregnancyoutcomeCmd component.
     *
     * @return the initialized component instance
     */
    public Command getPregnancyoutcomeCmd() {
        if (pregnancyoutcomeCmd == null) {//GEN-END:|482-getter|0|482-preInit
            // write pre-init user code here
            pregnancyoutcomeCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|482-getter|1|482-postInit
            // write post-init user code here
        }//GEN-BEGIN:|482-getter|2|
        return pregnancyoutcomeCmd;
    }
//</editor-fold>//GEN-END:|482-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: deliveryBackCmd ">//GEN-BEGIN:|485-getter|0|485-preInit
    /**
     * Returns an initialized instance of deliveryBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getDeliveryBackCmd() {
        if (deliveryBackCmd == null) {//GEN-END:|485-getter|0|485-preInit
            // write pre-init user code here
            deliveryBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|485-getter|1|485-postInit
            // write post-init user code here
        }//GEN-BEGIN:|485-getter|2|
        return deliveryBackCmd;
    }
//</editor-fold>//GEN-END:|485-getter|2|

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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: deliveryCmd ">//GEN-BEGIN:|495-getter|0|495-preInit
    /**
     * Returns an initialized instance of deliveryCmd component.
     *
     * @return the initialized component instance
     */
    public Command getDeliveryCmd() {
        if (deliveryCmd == null) {//GEN-END:|495-getter|0|495-preInit
            // write pre-init user code here
            deliveryCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|495-getter|1|495-postInit
            // write post-init user code here
        }//GEN-BEGIN:|495-getter|2|
        return deliveryCmd;
    }
//</editor-fold>//GEN-END:|495-getter|2|

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
                        "weekly"
                    };
                    
                    for (int i = 0; i < freqNames.length; i++) {
                        freqGroup.append(freqNames[i], null);
                    }
                       
                    freqGroup.setSelectedIndex(0, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }//GEN-BEGIN:|517-getter|2|
        return freqGroup;
    }
//</editor-fold>//GEN-END:|517-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Mortalitydatails_infantsBackcmd ">//GEN-BEGIN:|526-getter|0|526-preInit
    /**
     * Returns an initialized instance of Mortalitydatails_infantsBackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMortalitydatails_infantsBackcmd() {
        if (Mortalitydatails_infantsBackcmd == null) {//GEN-END:|526-getter|0|526-preInit
            // write pre-init user code here
            Mortalitydatails_infantsBackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|526-getter|1|526-postInit
            // write post-init user code here
        }//GEN-BEGIN:|526-getter|2|
        return Mortalitydatails_infantsBackcmd;
    }
//</editor-fold>//GEN-END:|526-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Mortalitydatails_infantsCmd ">//GEN-BEGIN:|528-getter|0|528-preInit
    /**
     * Returns an initialized instance of Mortalitydatails_infantsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMortalitydatails_infantsCmd() {
        if (Mortalitydatails_infantsCmd == null) {//GEN-END:|528-getter|0|528-preInit
            // write pre-init user code here
            Mortalitydatails_infantsCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|528-getter|1|528-postInit
            // write post-init user code here
        }//GEN-BEGIN:|528-getter|2|
        return Mortalitydatails_infantsCmd;
    }
//</editor-fold>//GEN-END:|528-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantdeathsupto4weeksbycauseBackCmd ">//GEN-BEGIN:|531-getter|0|531-preInit
    /**
     * Returns an initialized instance of Infantdeathsupto4weeksbycauseBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInfantdeathsupto4weeksbycauseBackCmd() {
        if (Infantdeathsupto4weeksbycauseBackCmd == null) {//GEN-END:|531-getter|0|531-preInit
            // write pre-init user code here
            Infantdeathsupto4weeksbycauseBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|531-getter|1|531-postInit
            // write post-init user code here
        }//GEN-BEGIN:|531-getter|2|
        return Infantdeathsupto4weeksbycauseBackCmd;
    }
//</editor-fold>//GEN-END:|531-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantdeathsupto4weeksbycauseCmd ">//GEN-BEGIN:|533-getter|0|533-preInit
    /**
     * Returns an initialized instance of Infantdeathsupto4weeksbycauseCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInfantdeathsupto4weeksbycauseCmd() {
        if (Infantdeathsupto4weeksbycauseCmd == null) {//GEN-END:|533-getter|0|533-preInit
            // write pre-init user code here
            Infantdeathsupto4weeksbycauseCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|533-getter|1|533-postInit
            // write post-init user code here
        }//GEN-BEGIN:|533-getter|2|
        return Infantdeathsupto4weeksbycauseCmd;
    }
//</editor-fold>//GEN-END:|533-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantchilddeathsupto5yearsbycauseBackCmd ">//GEN-BEGIN:|536-getter|0|536-preInit
    /**
     * Returns an initialized instance of
     * Infantchilddeathsupto5yearsbycauseBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getInfantchilddeathsupto5yearsbycauseBackCmd() {
        if (Infantchilddeathsupto5yearsbycauseBackCmd == null) {//GEN-END:|536-getter|0|536-preInit
            // write pre-init user code here
            Infantchilddeathsupto5yearsbycauseBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|536-getter|1|536-postInit
            // write post-init user code here
        }//GEN-BEGIN:|536-getter|2|
        return Infantchilddeathsupto5yearsbycauseBackCmd;
    }
//</editor-fold>//GEN-END:|536-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantchilddeathsupto5yearsbycauseCmd ">//GEN-BEGIN:|538-getter|0|538-preInit
    /**
     * Returns an initialized instance of Infantchilddeathsupto5yearsbycauseCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getInfantchilddeathsupto5yearsbycauseCmd() {
        if (Infantchilddeathsupto5yearsbycauseCmd == null) {//GEN-END:|538-getter|0|538-preInit
            // write pre-init user code here
            Infantchilddeathsupto5yearsbycauseCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|538-getter|1|538-postInit
            // write post-init user code here
        }//GEN-BEGIN:|538-getter|2|
        return Infantchilddeathsupto5yearsbycauseCmd;
    }
//</editor-fold>//GEN-END:|538-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseBackCmd ">//GEN-BEGIN:|541-getter|0|541-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseBackCmd() {
        if (AdolescentAdultdeathsbycauseBackCmd == null) {//GEN-END:|541-getter|0|541-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|541-getter|1|541-postInit
            // write post-init user code here
        }//GEN-BEGIN:|541-getter|2|
        return AdolescentAdultdeathsbycauseBackCmd;
    }
//</editor-fold>//GEN-END:|541-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseCmd ">//GEN-BEGIN:|543-getter|0|543-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseCmd() {
        if (AdolescentAdultdeathsbycauseCmd == null) {//GEN-END:|543-getter|0|543-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|543-getter|1|543-postInit
            // write post-init user code here
        }//GEN-BEGIN:|543-getter|2|
        return AdolescentAdultdeathsbycauseCmd;
    }
//</editor-fold>//GEN-END:|543-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MaternalDeathsBackCmd ">//GEN-BEGIN:|546-getter|0|546-preInit
    /**
     * Returns an initialized instance of MaternalDeathsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaternalDeathsBackCmd() {
        if (MaternalDeathsBackCmd == null) {//GEN-END:|546-getter|0|546-preInit
            // write pre-init user code here
            MaternalDeathsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|546-getter|1|546-postInit
            // write post-init user code here
        }//GEN-BEGIN:|546-getter|2|
        return MaternalDeathsBackCmd;
    }
//</editor-fold>//GEN-END:|546-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MaternalDeathsCmd ">//GEN-BEGIN:|548-getter|0|548-preInit
    /**
     * Returns an initialized instance of MaternalDeathsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaternalDeathsCmd() {
        if (MaternalDeathsCmd == null) {//GEN-END:|548-getter|0|548-preInit
            // write pre-init user code here
            MaternalDeathsCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|548-getter|1|548-postInit
            // write post-init user code here
        }//GEN-BEGIN:|548-getter|2|
        return MaternalDeathsCmd;
    }
//</editor-fold>//GEN-END:|548-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseBackCmd01 ">//GEN-BEGIN:|605-getter|0|605-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseBackCmd01
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseBackCmd01() {
        if (AdolescentAdultdeathsbycauseBackCmd01 == null) {//GEN-END:|605-getter|0|605-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseBackCmd01 = new Command("Back", Command.BACK, 0);//GEN-LINE:|605-getter|1|605-postInit
            // write post-init user code here
        }//GEN-BEGIN:|605-getter|2|
        return AdolescentAdultdeathsbycauseBackCmd01;
    }
//</editor-fold>//GEN-END:|605-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseCmd01 ">//GEN-BEGIN:|607-getter|0|607-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseCmd01
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseCmd01() {
        if (AdolescentAdultdeathsbycauseCmd01 == null) {//GEN-END:|607-getter|0|607-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseCmd01 = new Command("Next", Command.OK, 0);//GEN-LINE:|607-getter|1|607-postInit
            // write post-init user code here
        }//GEN-BEGIN:|607-getter|2|
        return AdolescentAdultdeathsbycauseCmd01;
    }
//</editor-fold>//GEN-END:|607-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseBackCmd02 ">//GEN-BEGIN:|609-getter|0|609-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseBackCmd02
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseBackCmd02() {
        if (AdolescentAdultdeathsbycauseBackCmd02 == null) {//GEN-END:|609-getter|0|609-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseBackCmd02 = new Command("Back", Command.BACK, 0);//GEN-LINE:|609-getter|1|609-postInit
            // write post-init user code here
        }//GEN-BEGIN:|609-getter|2|
        return AdolescentAdultdeathsbycauseBackCmd02;
    }
//</editor-fold>//GEN-END:|609-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycauseCmd02 ">//GEN-BEGIN:|611-getter|0|611-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycauseCmd02
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAdolescentAdultdeathsbycauseCmd02() {
        if (AdolescentAdultdeathsbycauseCmd02 == null) {//GEN-END:|611-getter|0|611-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycauseCmd02 = new Command("Next", Command.OK, 0);//GEN-LINE:|611-getter|1|611-postInit
            // write post-init user code here
        }//GEN-BEGIN:|611-getter|2|
        return AdolescentAdultdeathsbycauseCmd02;
    }
//</editor-fold>//GEN-END:|611-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: saveCmd ">//GEN-BEGIN:|652-getter|0|652-preInit
    /**
     * Returns an initialized instance of saveCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSaveCmd() {
        if (saveCmd == null) {//GEN-END:|652-getter|0|652-preInit
            // write pre-init user code here
            saveCmd = new Command("Save", Command.OK, 0);//GEN-LINE:|652-getter|1|652-postInit
            // write post-init user code here
        }//GEN-BEGIN:|652-getter|2|
        return saveCmd;
    }
//</editor-fold>//GEN-END:|652-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Fever ">//GEN-BEGIN:|654-getter|0|654-preInit
    /**
     * Returns an initialized instance of Fever component.
     *
     * @return the initialized component instance
     */
    public Form getFever() {
        if (Fever == null) {//GEN-END:|654-getter|0|654-preInit
            // write pre-init user code here
            Fever = new Form("Fever < 7 days", new Item[]{getStringItem(), getTextField(), getTextField1(), getTextField2(), getTextField3()});//GEN-BEGIN:|654-getter|1|654-postInit
            Fever.addCommand(getFeverBackCmd());
            Fever.addCommand(getFeverOk());
            Fever.setCommandListener(this);//GEN-END:|654-getter|1|654-postInit
            // write post-init user code here
        }//GEN-BEGIN:|654-getter|2|
        return Fever;
    }
//</editor-fold>//GEN-END:|654-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|667-getter|0|667-preInit
    /**
     * Returns an initialized instance of textField component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|667-getter|0|667-preInit
            // write pre-init user code here
            String str = getRecordValue(11);
            textField = new TextField(LocalizationSupport.getMessage("Only_Fever_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|667-getter|1|667-postInit
            // write post-init user code here
        }//GEN-BEGIN:|667-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|667-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FeverBackCmd ">//GEN-BEGIN:|663-getter|0|663-preInit
    /**
     * Returns an initialized instance of FeverBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFeverBackCmd() {
        if (FeverBackCmd == null) {//GEN-END:|663-getter|0|663-preInit
            // write pre-init user code here
            FeverBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|663-getter|1|663-postInit
            // write post-init user code here
        }//GEN-BEGIN:|663-getter|2|
        return FeverBackCmd;
    }
//</editor-fold>//GEN-END:|663-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FeverOk ">//GEN-BEGIN:|665-getter|0|665-preInit
    /**
     * Returns an initialized instance of FeverOk component.
     *
     * @return the initialized component instance
     */
    public Command getFeverOk() {
        if (FeverOk == null) {//GEN-END:|665-getter|0|665-preInit
            // write pre-init user code here
            FeverOk = new Command("Next", Command.OK, 0);//GEN-LINE:|665-getter|1|665-postInit
            // write post-init user code here
        }//GEN-BEGIN:|665-getter|2|
        return FeverOk;
    }
//</editor-fold>//GEN-END:|665-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|668-getter|0|668-preInit
    /**
     * Returns an initialized instance of textField1 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|668-getter|0|668-preInit
            // write pre-init user code here
            String str = getRecordValue(114);
            textField1 = new TextField(LocalizationSupport.getMessage("With_Rash_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|668-getter|1|668-postInit
            // write post-init user code here
        }//GEN-BEGIN:|668-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|668-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|669-getter|0|669-preInit
    /**
     * Returns an initialized instance of textField2 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|669-getter|0|669-preInit
            // write pre-init user code here
            String str = getRecordValue(12);
            textField2 = new TextField(LocalizationSupport.getMessage("With_Bleeding_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|669-getter|1|669-postInit
            // write post-init user code here
        }//GEN-BEGIN:|669-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|669-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField3 ">//GEN-BEGIN:|670-getter|0|670-preInit
    /**
     * Returns an initialized instance of textField3 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField3() {
        if (textField3 == null) {//GEN-END:|670-getter|0|670-preInit
            // write pre-init user code here
            String str = getRecordValue(13);
            textField3 = new TextField(LocalizationSupport.getMessage("With_Daze_1"), str, 4, TextField.NUMERIC);//GEN-LINE:|670-getter|1|670-postInit
            // write post-init user code here
        }//GEN-BEGIN:|670-getter|2|
        return textField3;
    }
//</editor-fold>//GEN-END:|670-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|675-getter|0|675-preInit
    /**
     * Returns an initialized instance of backCommand component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|675-getter|0|675-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|675-getter|1|675-postInit
            // write post-init user code here
        }//GEN-BEGIN:|675-getter|2|
        return backCommand;
    }
//</editor-fold>//GEN-END:|675-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|677-getter|0|677-preInit
    /**
     * Returns an initialized instance of okCommand component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|677-getter|0|677-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|677-getter|1|677-postInit
            // write post-init user code here
        }//GEN-BEGIN:|677-getter|2|
        return okCommand;
    }
//</editor-fold>//GEN-END:|677-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand1 ">//GEN-BEGIN:|679-getter|0|679-preInit
    /**
     * Returns an initialized instance of backCommand1 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {//GEN-END:|679-getter|0|679-preInit
            // write pre-init user code here
            backCommand1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|679-getter|1|679-postInit
            // write post-init user code here
        }//GEN-BEGIN:|679-getter|2|
        return backCommand1;
    }
//</editor-fold>//GEN-END:|679-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand1 ">//GEN-BEGIN:|681-getter|0|681-preInit
    /**
     * Returns an initialized instance of okCommand1 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand1() {
        if (okCommand1 == null) {//GEN-END:|681-getter|0|681-preInit
            // write pre-init user code here
            okCommand1 = new Command("Ok", Command.OK, 0);//GEN-LINE:|681-getter|1|681-postInit
            // write post-init user code here
        }//GEN-BEGIN:|681-getter|2|
        return okCommand1;
    }
//</editor-fold>//GEN-END:|681-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand2 ">//GEN-BEGIN:|683-getter|0|683-preInit
    /**
     * Returns an initialized instance of backCommand2 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand2() {
        if (backCommand2 == null) {//GEN-END:|683-getter|0|683-preInit
            // write pre-init user code here
            backCommand2 = new Command("Back", Command.BACK, 0);//GEN-LINE:|683-getter|1|683-postInit
            // write post-init user code here
        }//GEN-BEGIN:|683-getter|2|
        return backCommand2;
    }
//</editor-fold>//GEN-END:|683-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand2 ">//GEN-BEGIN:|685-getter|0|685-preInit
    /**
     * Returns an initialized instance of okCommand2 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand2() {
        if (okCommand2 == null) {//GEN-END:|685-getter|0|685-preInit
            // write pre-init user code here
            okCommand2 = new Command("Ok", Command.OK, 0);//GEN-LINE:|685-getter|1|685-postInit
            // write post-init user code here
        }//GEN-BEGIN:|685-getter|2|
        return okCommand2;
    }
//</editor-fold>//GEN-END:|685-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand3 ">//GEN-BEGIN:|687-getter|0|687-preInit
    /**
     * Returns an initialized instance of backCommand3 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand3() {
        if (backCommand3 == null) {//GEN-END:|687-getter|0|687-preInit
            // write pre-init user code here
            backCommand3 = new Command("Back", Command.BACK, 0);//GEN-LINE:|687-getter|1|687-postInit
            // write post-init user code here
        }//GEN-BEGIN:|687-getter|2|
        return backCommand3;
    }
//</editor-fold>//GEN-END:|687-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand3 ">//GEN-BEGIN:|689-getter|0|689-preInit
    /**
     * Returns an initialized instance of okCommand3 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand3() {
        if (okCommand3 == null) {//GEN-END:|689-getter|0|689-preInit
            // write pre-init user code here
            okCommand3 = new Command("Ok", Command.OK, 0);//GEN-LINE:|689-getter|1|689-postInit
            // write post-init user code here
        }//GEN-BEGIN:|689-getter|2|
        return okCommand3;
    }
//</editor-fold>//GEN-END:|689-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand4 ">//GEN-BEGIN:|691-getter|0|691-preInit
    /**
     * Returns an initialized instance of backCommand4 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand4() {
        if (backCommand4 == null) {//GEN-END:|691-getter|0|691-preInit
            // write pre-init user code here
            backCommand4 = new Command("Back", Command.BACK, 0);//GEN-LINE:|691-getter|1|691-postInit
            // write post-init user code here
        }//GEN-BEGIN:|691-getter|2|
        return backCommand4;
    }
//</editor-fold>//GEN-END:|691-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand4 ">//GEN-BEGIN:|693-getter|0|693-preInit
    /**
     * Returns an initialized instance of okCommand4 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand4() {
        if (okCommand4 == null) {//GEN-END:|693-getter|0|693-preInit
            // write pre-init user code here
            okCommand4 = new Command("Ok", Command.OK, 0);//GEN-LINE:|693-getter|1|693-postInit
            // write post-init user code here
        }//GEN-BEGIN:|693-getter|2|
        return okCommand4;
    }
//</editor-fold>//GEN-END:|693-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand5 ">//GEN-BEGIN:|695-getter|0|695-preInit
    /**
     * Returns an initialized instance of backCommand5 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand5() {
        if (backCommand5 == null) {//GEN-END:|695-getter|0|695-preInit
            // write pre-init user code here
            backCommand5 = new Command("Back", Command.BACK, 0);//GEN-LINE:|695-getter|1|695-postInit
            // write post-init user code here
        }//GEN-BEGIN:|695-getter|2|
        return backCommand5;
    }
//</editor-fold>//GEN-END:|695-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand5 ">//GEN-BEGIN:|697-getter|0|697-preInit
    /**
     * Returns an initialized instance of okCommand5 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand5() {
        if (okCommand5 == null) {//GEN-END:|697-getter|0|697-preInit
            // write pre-init user code here
            okCommand5 = new Command("Ok", Command.OK, 0);//GEN-LINE:|697-getter|1|697-postInit
            // write post-init user code here
        }//GEN-BEGIN:|697-getter|2|
        return okCommand5;
    }
//</editor-fold>//GEN-END:|697-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand6 ">//GEN-BEGIN:|699-getter|0|699-preInit
    /**
     * Returns an initialized instance of backCommand6 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand6() {
        if (backCommand6 == null) {//GEN-END:|699-getter|0|699-preInit
            // write pre-init user code here
            backCommand6 = new Command("Back", Command.BACK, 0);//GEN-LINE:|699-getter|1|699-postInit
            // write post-init user code here
        }//GEN-BEGIN:|699-getter|2|
        return backCommand6;
    }
//</editor-fold>//GEN-END:|699-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand6 ">//GEN-BEGIN:|701-getter|0|701-preInit
    /**
     * Returns an initialized instance of okCommand6 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand6() {
        if (okCommand6 == null) {//GEN-END:|701-getter|0|701-preInit
            // write pre-init user code here
            okCommand6 = new Command("Ok", Command.OK, 0);//GEN-LINE:|701-getter|1|701-postInit
            // write post-init user code here
        }//GEN-BEGIN:|701-getter|2|
        return okCommand6;
    }
//</editor-fold>//GEN-END:|701-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand7 ">//GEN-BEGIN:|703-getter|0|703-preInit
    /**
     * Returns an initialized instance of backCommand7 component.
     *
     * @return the initialized component instance
     */
    public Command getBackCommand7() {
        if (backCommand7 == null) {//GEN-END:|703-getter|0|703-preInit
            // write pre-init user code here
            backCommand7 = new Command("Back", Command.BACK, 0);//GEN-LINE:|703-getter|1|703-postInit
            // write post-init user code here
        }//GEN-BEGIN:|703-getter|2|
        return backCommand7;
    }
//</editor-fold>//GEN-END:|703-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand7 ">//GEN-BEGIN:|705-getter|0|705-preInit
    /**
     * Returns an initialized instance of okCommand7 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand7() {
        if (okCommand7 == null) {//GEN-END:|705-getter|0|705-preInit
            // write pre-init user code here
            okCommand7 = new Command("Ok", Command.OK, 0);//GEN-LINE:|705-getter|1|705-postInit
            // write post-init user code here
        }//GEN-BEGIN:|705-getter|2|
        return okCommand7;
    }
//</editor-fold>//GEN-END:|705-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem ">//GEN-BEGIN:|707-getter|0|707-preInit
    /**
     * Returns an initialized instance of stringItem component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {//GEN-END:|707-getter|0|707-preInit
            // write pre-init user code here
            stringItem = new StringItem("Male cases<5 yrs", null, Item.PLAIN);//GEN-BEGIN:|707-getter|1|707-postInit
            stringItem.setLayout(ImageItem.LAYOUT_DEFAULT);//GEN-END:|707-getter|1|707-postInit
            // write post-init user code here
        }//GEN-BEGIN:|707-getter|2|
        return stringItem;
    }
//</editor-fold>//GEN-END:|707-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MalecasesGT5yrs ">//GEN-BEGIN:|711-getter|0|711-preInit
    /**
     * Returns an initialized instance of MalecasesGT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMalecasesGT5yrs() {
        if (MalecasesGT5yrs == null) {//GEN-END:|711-getter|0|711-preInit
            // write pre-init user code here
            MalecasesGT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem2(), getTextField8(), getTextField9(), getTextField10(), getTextField11()});//GEN-BEGIN:|711-getter|1|711-postInit
            MalecasesGT5yrs.addCommand(getMalecases5yrsBackCmd());
            MalecasesGT5yrs.addCommand(getMalecases5yrsOkCmd());
            MalecasesGT5yrs.setCommandListener(this);//GEN-END:|711-getter|1|711-postInit
            // write post-init user code here
        }//GEN-BEGIN:|711-getter|2|
        return MalecasesGT5yrs;
    }
//</editor-fold>//GEN-END:|711-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malecases5yrsBackCmd ">//GEN-BEGIN:|712-getter|0|712-preInit
    /**
     * Returns an initialized instance of Malecases5yrsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMalecases5yrsBackCmd() {
        if (Malecases5yrsBackCmd == null) {//GEN-END:|712-getter|0|712-preInit
            // write pre-init user code here
            Malecases5yrsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|712-getter|1|712-postInit
            // write post-init user code here
        }//GEN-BEGIN:|712-getter|2|
        return Malecases5yrsBackCmd;
    }
//</editor-fold>//GEN-END:|712-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malecases5yrsOkCmd ">//GEN-BEGIN:|714-getter|0|714-preInit
    /**
     * Returns an initialized instance of Malecases5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMalecases5yrsOkCmd() {
        if (Malecases5yrsOkCmd == null) {//GEN-END:|714-getter|0|714-preInit
            // write pre-init user code here
            Malecases5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|714-getter|1|714-postInit
            // write post-init user code here
        }//GEN-BEGIN:|714-getter|2|
        return Malecases5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|714-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField8 ">//GEN-BEGIN:|716-getter|0|716-preInit
    /**
     * Returns an initialized instance of textField8 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField8() {
        if (textField8 == null) {//GEN-END:|716-getter|0|716-preInit
            // write pre-init user code here
            String str = getRecordValue(18);
            textField8 = new TextField(LocalizationSupport.getMessage("Only_Fever_3"), str, 4, TextField.NUMERIC);//GEN-LINE:|716-getter|1|716-postInit
            // write post-init user code here
        }//GEN-BEGIN:|716-getter|2|
        return textField8;
    }
//</editor-fold>//GEN-END:|716-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField9 ">//GEN-BEGIN:|717-getter|0|717-preInit
    /**
     * Returns an initialized instance of textField9 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField9() {
        if (textField9 == null) {//GEN-END:|717-getter|0|717-preInit
            // write pre-init user code here
            String str = getRecordValue(19);
            textField9 = new TextField(LocalizationSupport.getMessage("With_Rash_3"), str, 4, TextField.NUMERIC);//GEN-LINE:|717-getter|1|717-postInit
            // write post-init user code here
        }//GEN-BEGIN:|717-getter|2|
        return textField9;
    }
//</editor-fold>//GEN-END:|717-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField10 ">//GEN-BEGIN:|718-getter|0|718-preInit
    /**
     * Returns an initialized instance of textField10 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField10() {
        if (textField10 == null) {//GEN-END:|718-getter|0|718-preInit
            // write pre-init user code here
            String str = getRecordValue(20);
            textField10 = new TextField(LocalizationSupport.getMessage("With_Bleeding_3"), str, 4, TextField.NUMERIC);//GEN-LINE:|718-getter|1|718-postInit
            // write post-init user code here
        }//GEN-BEGIN:|718-getter|2|
        return textField10;
    }
//</editor-fold>//GEN-END:|718-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField11 ">//GEN-BEGIN:|719-getter|0|719-preInit
    /**
     * Returns an initialized instance of textField11 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField11() {
        if (textField11 == null) {//GEN-END:|719-getter|0|719-preInit
            // write pre-init user code here
            String str = getRecordValue(21);
            textField11 = new TextField(LocalizationSupport.getMessage("With_Daze_3"), str, 4, TextField.NUMERIC);//GEN-LINE:|719-getter|1|719-postInit
            // write post-init user code here
        }//GEN-BEGIN:|719-getter|2|
        return textField11;
    }
//</editor-fold>//GEN-END:|719-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesGT5yrs ">//GEN-BEGIN:|722-getter|0|722-preInit
    /**
     * Returns an initialized instance of FemalecasesGT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemalecasesGT5yrs() {
        if (FemalecasesGT5yrs == null) {//GEN-END:|722-getter|0|722-preInit
            // write pre-init user code here
            FemalecasesGT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem7(), getTextField12(), getTextField14(), getTextField15(), getTextField13()});//GEN-BEGIN:|722-getter|1|722-postInit
            FemalecasesGT5yrs.addCommand(getFemalecases5yrsBackCmd());
            FemalecasesGT5yrs.addCommand(getFemalecases5yrsOkCmd());
            FemalecasesGT5yrs.setCommandListener(this);//GEN-END:|722-getter|1|722-postInit
            // write post-init user code here
        }//GEN-BEGIN:|722-getter|2|
        return FemalecasesGT5yrs;
    }
//</editor-fold>//GEN-END:|722-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField12 ">//GEN-BEGIN:|727-getter|0|727-preInit
    /**
     * Returns an initialized instance of textField12 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField12() {
        if (textField12 == null) {//GEN-END:|727-getter|0|727-preInit
            // write pre-init user code here
            String str = getRecordValue(22);
            textField12 = new TextField(LocalizationSupport.getMessage("Only_Fever_4"), str, 4, TextField.NUMERIC);//GEN-LINE:|727-getter|1|727-postInit
            // write post-init user code here
        }//GEN-BEGIN:|727-getter|2|
        return textField12;
    }
//</editor-fold>//GEN-END:|727-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField14 ">//GEN-BEGIN:|729-getter|0|729-preInit
    /**
     * Returns an initialized instance of textField14 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField14() {
        if (textField14 == null) {//GEN-END:|729-getter|0|729-preInit
            // write pre-init user code here
            String str = getRecordValue(24);
            textField14 = new TextField(LocalizationSupport.getMessage("With_Rash_4"), str, 4, TextField.NUMERIC);//GEN-LINE:|729-getter|1|729-postInit
            // write post-init user code here
        }//GEN-BEGIN:|729-getter|2|
        return textField14;
    }
//</editor-fold>//GEN-END:|729-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField15 ">//GEN-BEGIN:|730-getter|0|730-preInit
    /**
     * Returns an initialized instance of textField15 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField15() {
        if (textField15 == null) {//GEN-END:|730-getter|0|730-preInit
            // write pre-init user code here
            String str = getRecordValue(25);
            textField15 = new TextField(LocalizationSupport.getMessage("With_Bleeding_4"), str, 4, TextField.NUMERIC);//GEN-LINE:|730-getter|1|730-postInit
            // write post-init user code here
        }//GEN-BEGIN:|730-getter|2|
        return textField15;
    }
//</editor-fold>//GEN-END:|730-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField13 ">//GEN-BEGIN:|731-getter|0|731-preInit
    /**
     * Returns an initialized instance of textField13 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField13() {
        if (textField13 == null) {//GEN-END:|731-getter|0|731-preInit
            // write pre-init user code here
            String str = getRecordValue(23);
            textField13 = new TextField(LocalizationSupport.getMessage("With_Daze_4"), str, 4, TextField.NUMERIC);//GEN-LINE:|731-getter|1|731-postInit
            // write post-init user code here
        }//GEN-BEGIN:|731-getter|2|
        return textField13;
    }
//</editor-fold>//GEN-END:|731-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeaths5yrs ">//GEN-BEGIN:|732-getter|0|732-preInit
    /**
     * Returns an initialized instance of Maledeaths5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMaledeaths5yrs() {
        if (Maledeaths5yrs == null) {//GEN-END:|732-getter|0|732-preInit
            // write pre-init user code here
            Maledeaths5yrs = new Form("Fever < 7 days", new Item[]{getStringItem8(), getTextField16(), getTextField17(), getTextField18(), getTextField19()});//GEN-BEGIN:|732-getter|1|732-postInit
            Maledeaths5yrs.addCommand(getMaledeaths5yrsBackCmd());
            Maledeaths5yrs.addCommand(getMaledeaths5yrsOkCmd());
            Maledeaths5yrs.setCommandListener(this);//GEN-END:|732-getter|1|732-postInit
            // write post-init user code here
        }//GEN-BEGIN:|732-getter|2|
        return Maledeaths5yrs;
    }
//</editor-fold>//GEN-END:|732-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField16 ">//GEN-BEGIN:|737-getter|0|737-preInit
    /**
     * Returns an initialized instance of textField16 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField16() {
        if (textField16 == null) {//GEN-END:|737-getter|0|737-preInit
            // write pre-init user code here
            String str = getRecordValue(26);
            textField16 = new TextField(LocalizationSupport.getMessage("Only_Fever_5"), str, 4, TextField.NUMERIC);//GEN-LINE:|737-getter|1|737-postInit
            // write post-init user code here
        }//GEN-BEGIN:|737-getter|2|
        return textField16;
    }
//</editor-fold>//GEN-END:|737-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField17 ">//GEN-BEGIN:|738-getter|0|738-preInit
    /**
     * Returns an initialized instance of textField17 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField17() {
        if (textField17 == null) {//GEN-END:|738-getter|0|738-preInit
            // write pre-init user code here
            String str = getRecordValue(27);
            textField17 = new TextField(LocalizationSupport.getMessage("With_Rash_5"), str, 4, TextField.NUMERIC);//GEN-LINE:|738-getter|1|738-postInit
            // write post-init user code here
        }//GEN-BEGIN:|738-getter|2|
        return textField17;
    }
//</editor-fold>//GEN-END:|738-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField18 ">//GEN-BEGIN:|739-getter|0|739-preInit
    /**
     * Returns an initialized instance of textField18 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField18() {
        if (textField18 == null) {//GEN-END:|739-getter|0|739-preInit
            // write pre-init user code here
            String str = getRecordValue(28);
            textField18 = new TextField(LocalizationSupport.getMessage("With_Bleeding_5"), str, 4, TextField.NUMERIC);//GEN-LINE:|739-getter|1|739-postInit
            // write post-init user code here
        }//GEN-BEGIN:|739-getter|2|
        return textField18;
    }
//</editor-fold>//GEN-END:|739-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField19 ">//GEN-BEGIN:|740-getter|0|740-preInit
    /**
     * Returns an initialized instance of textField19 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField19() {
        if (textField19 == null) {//GEN-END:|740-getter|0|740-preInit
            // write pre-init user code here
            String str = getRecordValue(29);
            textField19 = new TextField(LocalizationSupport.getMessage("With_Daze_5"), str, 4, TextField.NUMERIC);//GEN-LINE:|740-getter|1|740-postInit
            // write post-init user code here
        }//GEN-BEGIN:|740-getter|2|
        return textField19;
    }
//</editor-fold>//GEN-END:|740-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MaleDeathsGT5yrs ">//GEN-BEGIN:|741-getter|0|741-preInit
    /**
     * Returns an initialized instance of MaleDeathsGT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMaleDeathsGT5yrs() {
        if (MaleDeathsGT5yrs == null) {//GEN-END:|741-getter|0|741-preInit
            // write pre-init user code here
            MaleDeathsGT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem10(), getTextField20(), getTextField21(), getTextField22(), getTextField23()});//GEN-BEGIN:|741-getter|1|741-postInit
            MaleDeathsGT5yrs.addCommand(getMaledeaths5yrsBackCmd1());
            MaleDeathsGT5yrs.addCommand(getMaledeaths5yrsOkCmd1());
            MaleDeathsGT5yrs.setCommandListener(this);//GEN-END:|741-getter|1|741-postInit
            // write post-init user code here
        }//GEN-BEGIN:|741-getter|2|
        return MaleDeathsGT5yrs;
    }
//</editor-fold>//GEN-END:|741-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField20 ">//GEN-BEGIN:|746-getter|0|746-preInit
    /**
     * Returns an initialized instance of textField20 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField20() {
        if (textField20 == null) {//GEN-END:|746-getter|0|746-preInit
            // write pre-init user code here
            String str = getRecordValue(30);
            textField20 = new TextField(LocalizationSupport.getMessage("Only_Fever_7"), str, 4, TextField.NUMERIC);//GEN-LINE:|746-getter|1|746-postInit
            // write post-init user code here
        }//GEN-BEGIN:|746-getter|2|
        return textField20;
    }
//</editor-fold>//GEN-END:|746-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField21 ">//GEN-BEGIN:|747-getter|0|747-preInit
    /**
     * Returns an initialized instance of textField21 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField21() {
        if (textField21 == null) {//GEN-END:|747-getter|0|747-preInit
            // write pre-init user code here
            String str = getRecordValue(31);
            textField21 = new TextField(LocalizationSupport.getMessage("With_Rash_7"), str, 4, TextField.NUMERIC);//GEN-LINE:|747-getter|1|747-postInit
            // write post-init user code here
        }//GEN-BEGIN:|747-getter|2|
        return textField21;
    }
//</editor-fold>//GEN-END:|747-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField22 ">//GEN-BEGIN:|748-getter|0|748-preInit
    /**
     * Returns an initialized instance of textField22 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField22() {
        if (textField22 == null) {//GEN-END:|748-getter|0|748-preInit
            // write pre-init user code here
            String str = getRecordValue(32);
            textField22 = new TextField(LocalizationSupport.getMessage("With_Bleeding_7"), str, 4, TextField.NUMERIC);//GEN-LINE:|748-getter|1|748-postInit
            // write post-init user code here
        }//GEN-BEGIN:|748-getter|2|
        return textField22;
    }
//</editor-fold>//GEN-END:|748-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField23 ">//GEN-BEGIN:|749-getter|0|749-preInit
    /**
     * Returns an initialized instance of textField23 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField23() {
        if (textField23 == null) {//GEN-END:|749-getter|0|749-preInit
            // write pre-init user code here
            String str = getRecordValue(33);
            textField23 = new TextField(LocalizationSupport.getMessage("With_Daze_7"), str, 4, TextField.NUMERIC);//GEN-LINE:|749-getter|1|749-postInit
            // write post-init user code here
        }//GEN-BEGIN:|749-getter|2|
        return textField23;
    }
//</editor-fold>//GEN-END:|749-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsLT5yrs ">//GEN-BEGIN:|755-getter|0|755-preInit
    /**
     * Returns an initialized instance of FemaledeathsLT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemaledeathsLT5yrs() {
        if (FemaledeathsLT5yrs == null) {//GEN-END:|755-getter|0|755-preInit
            // write pre-init user code here
            FemaledeathsLT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem9(), getTextField28(), getTextField29(), getTextField30(), getTextField31()});//GEN-BEGIN:|755-getter|1|755-postInit
            FemaledeathsLT5yrs.addCommand(getFemaledeathsLT5yrsBackCmd());
            FemaledeathsLT5yrs.addCommand(getFemaledeathsLT5yrsOkCmd());
            FemaledeathsLT5yrs.setCommandListener(this);//GEN-END:|755-getter|1|755-postInit
            // write post-init user code here
        }//GEN-BEGIN:|755-getter|2|
        return FemaledeathsLT5yrs;
    }
//</editor-fold>//GEN-END:|755-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField28 ">//GEN-BEGIN:|760-getter|0|760-preInit
    /**
     * Returns an initialized instance of textField28 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField28() {
        if (textField28 == null) {//GEN-END:|760-getter|0|760-preInit
            // write pre-init user code here
            String str = getRecordValue(38);
            textField28 = new TextField(LocalizationSupport.getMessage("Only_Fever_6"), str, 4, TextField.NUMERIC);//GEN-LINE:|760-getter|1|760-postInit
            // write post-init user code here
        }//GEN-BEGIN:|760-getter|2|
        return textField28;
    }
//</editor-fold>//GEN-END:|760-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField29 ">//GEN-BEGIN:|761-getter|0|761-preInit
    /**
     * Returns an initialized instance of textField29 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField29() {
        if (textField29 == null) {//GEN-END:|761-getter|0|761-preInit
            // write pre-init user code here
            String str = getRecordValue(39);
            textField29 = new TextField(LocalizationSupport.getMessage("With_Rash_6"), str, 4, TextField.NUMERIC);//GEN-LINE:|761-getter|1|761-postInit
            // write post-init user code here
        }//GEN-BEGIN:|761-getter|2|
        return textField29;
    }
//</editor-fold>//GEN-END:|761-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField30 ">//GEN-BEGIN:|762-getter|0|762-preInit
    /**
     * Returns an initialized instance of textField30 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField30() {
        if (textField30 == null) {//GEN-END:|762-getter|0|762-preInit
            // write pre-init user code here
            String str = getRecordValue(40);
            textField30 = new TextField(LocalizationSupport.getMessage("With_Bleeding_6"), str, 4, TextField.NUMERIC);//GEN-LINE:|762-getter|1|762-postInit
            // write post-init user code here
        }//GEN-BEGIN:|762-getter|2|
        return textField30;
    }
//</editor-fold>//GEN-END:|762-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField31 ">//GEN-BEGIN:|763-getter|0|763-preInit
    /**
     * Returns an initialized instance of textField31 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField31() {
        if (textField31 == null) {//GEN-END:|763-getter|0|763-preInit
            // write pre-init user code here
            String str = getRecordValue(41);
            textField31 = new TextField(LocalizationSupport.getMessage("With_Daze_6"), str, 4, TextField.NUMERIC);//GEN-LINE:|763-getter|1|763-postInit
            // write post-init user code here
        }//GEN-BEGIN:|763-getter|2|
        return textField31;
    }
//</editor-fold>//GEN-END:|763-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Fevermorethan7days ">//GEN-BEGIN:|764-getter|0|764-preInit
    /**
     * Returns an initialized instance of Fevermorethan7days component.
     *
     * @return the initialized component instance
     */
    public Form getFevermorethan7days() {
        if (Fevermorethan7days == null) {//GEN-END:|764-getter|0|764-preInit
            // write pre-init user code here
            Fevermorethan7days = new Form("Fever > 7 days", new Item[]{getTextField32(), getTextField33(), getTextField34(), getTextField35(), getTextField36(), getTextField37(), getTextField38(), getTextField39()});//GEN-BEGIN:|764-getter|1|764-postInit
            Fevermorethan7days.addCommand(getFevermorethan7daysBackCmd());
            Fevermorethan7days.addCommand(getFevermorethan7daysOkCmd());
            Fevermorethan7days.setCommandListener(this);//GEN-END:|764-getter|1|764-postInit
            // write post-init user code here
        }//GEN-BEGIN:|764-getter|2|
        return Fevermorethan7days;
    }
//</editor-fold>//GEN-END:|764-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField32 ">//GEN-BEGIN:|769-getter|0|769-preInit
    /**
     * Returns an initialized instance of textField32 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField32() {
        if (textField32 == null) {//GEN-END:|769-getter|0|769-preInit
            // write pre-init user code here
            String str = getRecordValue(42);
            textField32 = new TextField(LocalizationSupport.getMessage("Male_cases_<5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|769-getter|1|769-postInit
            // write post-init user code here
        }//GEN-BEGIN:|769-getter|2|
        return textField32;
    }
//</editor-fold>//GEN-END:|769-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField33 ">//GEN-BEGIN:|770-getter|0|770-preInit
    /**
     * Returns an initialized instance of textField33 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField33() {
        if (textField33 == null) {//GEN-END:|770-getter|0|770-preInit
            // write pre-init user code here
            String str = getRecordValue(43);
            textField33 = new TextField(LocalizationSupport.getMessage("Female_cases_<5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|770-getter|1|770-postInit
            // write post-init user code here
        }//GEN-BEGIN:|770-getter|2|
        return textField33;
    }
//</editor-fold>//GEN-END:|770-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField34 ">//GEN-BEGIN:|771-getter|0|771-preInit
    /**
     * Returns an initialized instance of textField34 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField34() {
        if (textField34 == null) {//GEN-END:|771-getter|0|771-preInit
            // write pre-init user code here
            String str = getRecordValue(44);
            textField34 = new TextField(LocalizationSupport.getMessage("Male_cases_>5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|771-getter|1|771-postInit
            // write post-init user code here
        }//GEN-BEGIN:|771-getter|2|
        return textField34;
    }
//</editor-fold>//GEN-END:|771-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField35 ">//GEN-BEGIN:|772-getter|0|772-preInit
    /**
     * Returns an initialized instance of textField35 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField35() {
        if (textField35 == null) {//GEN-END:|772-getter|0|772-preInit
            // write pre-init user code here
            String str = getRecordValue(45);
            textField35 = new TextField(LocalizationSupport.getMessage("Female_cases_>5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|772-getter|1|772-postInit
            // write post-init user code here
        }//GEN-BEGIN:|772-getter|2|
        return textField35;
    }
//</editor-fold>//GEN-END:|772-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField36 ">//GEN-BEGIN:|773-getter|0|773-preInit
    /**
     * Returns an initialized instance of textField36 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField36() {
        if (textField36 == null) {//GEN-END:|773-getter|0|773-preInit
            // write pre-init user code here
            String str = getRecordValue(46);
            textField36 = new TextField(LocalizationSupport.getMessage("Male_deaths_<5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|773-getter|1|773-postInit
            // write post-init user code here
        }//GEN-BEGIN:|773-getter|2|
        return textField36;
    }
//</editor-fold>//GEN-END:|773-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField37 ">//GEN-BEGIN:|774-getter|0|774-preInit
    /**
     * Returns an initialized instance of textField37 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField37() {
        if (textField37 == null) {//GEN-END:|774-getter|0|774-preInit
            // write pre-init user code here
            String str = getRecordValue(47);
            textField37 = new TextField(LocalizationSupport.getMessage("Female_deaths_<5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|774-getter|1|774-postInit
            // write post-init user code here
        }//GEN-BEGIN:|774-getter|2|
        return textField37;
    }
//</editor-fold>//GEN-END:|774-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField38 ">//GEN-BEGIN:|775-getter|0|775-preInit
    /**
     * Returns an initialized instance of textField38 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField38() {
        if (textField38 == null) {//GEN-END:|775-getter|0|775-preInit
            // write pre-init user code here
            String str = getRecordValue(48);
            textField38 = new TextField(LocalizationSupport.getMessage("Male_deaths_>5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|775-getter|1|775-postInit
            // write post-init user code here
        }//GEN-BEGIN:|775-getter|2|
        return textField38;
    }
//</editor-fold>//GEN-END:|775-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField39 ">//GEN-BEGIN:|776-getter|0|776-preInit
    /**
     * Returns an initialized instance of textField39 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField39() {
        if (textField39 == null) {//GEN-END:|776-getter|0|776-preInit
            // write pre-init user code here
            String str = getRecordValue(49);
            textField39 = new TextField(LocalizationSupport.getMessage("Female_deaths_>5yrs_9"), str, 4, TextField.NUMERIC);//GEN-LINE:|776-getter|1|776-postInit
            // write post-init user code here
        }//GEN-BEGIN:|776-getter|2|
        return textField39;
    }
//</editor-fold>//GEN-END:|776-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Coughwithorwithoutfever ">//GEN-BEGIN:|777-getter|0|777-preInit
    /**
     * Returns an initialized instance of Coughwithorwithoutfever component.
     *
     * @return the initialized component instance
     */
    public Form getCoughwithorwithoutfever() {
        if (Coughwithorwithoutfever == null) {//GEN-END:|777-getter|0|777-preInit
            // write pre-init user code here
            Coughwithorwithoutfever = new Form("Cough With Or Without Fever", new Item[]{getStringItem4(), getTextField40(), getTextField41(), getTextField42(), getTextField43(), getTextField44(), getTextField45(), getTextField46(), getTextField47()});//GEN-BEGIN:|777-getter|1|777-postInit
            Coughwithorwithoutfever.addCommand(getCoughwithorwithoutfeverBackCmd());
            Coughwithorwithoutfever.addCommand(getCoughwithorwithoutfeverOkCmd());
            Coughwithorwithoutfever.setCommandListener(this);//GEN-END:|777-getter|1|777-postInit
            // write post-init user code here
        }//GEN-BEGIN:|777-getter|2|
        return Coughwithorwithoutfever;
    }
//</editor-fold>//GEN-END:|777-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem4 ">//GEN-BEGIN:|782-getter|0|782-preInit
    /**
     * Returns an initialized instance of stringItem4 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem4() {
        if (stringItem4 == null) {//GEN-END:|782-getter|0|782-preInit
            // write pre-init user code here
            stringItem4 = new StringItem("Less than 3 weeks", null);//GEN-LINE:|782-getter|1|782-postInit
            // write post-init user code here
        }//GEN-BEGIN:|782-getter|2|
        return stringItem4;
    }
//</editor-fold>//GEN-END:|782-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField40 ">//GEN-BEGIN:|785-getter|0|785-preInit
    /**
     * Returns an initialized instance of textField40 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField40() {
        if (textField40 == null) {//GEN-END:|785-getter|0|785-preInit
            // write pre-init user code here
            String str = getRecordValue(50);
            textField40 = new TextField(LocalizationSupport.getMessage("Male_cases_<5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|785-getter|1|785-postInit
            // write post-init user code here
        }//GEN-BEGIN:|785-getter|2|
        return textField40;
    }
//</editor-fold>//GEN-END:|785-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField41 ">//GEN-BEGIN:|786-getter|0|786-preInit
    /**
     * Returns an initialized instance of textField41 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField41() {
        if (textField41 == null) {//GEN-END:|786-getter|0|786-preInit
            // write pre-init user code here
            String str = getRecordValue(51);
            textField41 = new TextField(LocalizationSupport.getMessage("Female_cases_<5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|786-getter|1|786-postInit
            // write post-init user code here
        }//GEN-BEGIN:|786-getter|2|
        return textField41;
    }
//</editor-fold>//GEN-END:|786-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField42 ">//GEN-BEGIN:|787-getter|0|787-preInit
    /**
     * Returns an initialized instance of textField42 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField42() {
        if (textField42 == null) {//GEN-END:|787-getter|0|787-preInit
            // write pre-init user code here
            String str = getRecordValue(52);
            textField42 = new TextField(LocalizationSupport.getMessage("Male_cases_>5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|787-getter|1|787-postInit
            // write post-init user code here
        }//GEN-BEGIN:|787-getter|2|
        return textField42;
    }
//</editor-fold>//GEN-END:|787-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField43 ">//GEN-BEGIN:|788-getter|0|788-preInit
    /**
     * Returns an initialized instance of textField43 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField43() {
        if (textField43 == null) {//GEN-END:|788-getter|0|788-preInit
            // write pre-init user code here
            String str = getRecordValue(53);
            textField43 = new TextField(LocalizationSupport.getMessage("Female_cases_>5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|788-getter|1|788-postInit
            // write post-init user code here
        }//GEN-BEGIN:|788-getter|2|
        return textField43;
    }
//</editor-fold>//GEN-END:|788-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField44 ">//GEN-BEGIN:|789-getter|0|789-preInit
    /**
     * Returns an initialized instance of textField44 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField44() {
        if (textField44 == null) {//GEN-END:|789-getter|0|789-preInit
            // write pre-init user code here
            String str = getRecordValue(54);
            textField44 = new TextField(LocalizationSupport.getMessage("Male_deaths_<5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|789-getter|1|789-postInit
            // write post-init user code here
        }//GEN-BEGIN:|789-getter|2|
        return textField44;
    }
//</editor-fold>//GEN-END:|789-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField45 ">//GEN-BEGIN:|790-getter|0|790-preInit
    /**
     * Returns an initialized instance of textField45 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField45() {
        if (textField45 == null) {//GEN-END:|790-getter|0|790-preInit
            // write pre-init user code here
            String str = getRecordValue(55);
            textField45 = new TextField(LocalizationSupport.getMessage("Female_deaths_<5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|790-getter|1|790-postInit
            // write post-init user code here
        }//GEN-BEGIN:|790-getter|2|
        return textField45;
    }
//</editor-fold>//GEN-END:|790-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField46 ">//GEN-BEGIN:|791-getter|0|791-preInit
    /**
     * Returns an initialized instance of textField46 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField46() {
        if (textField46 == null) {//GEN-END:|791-getter|0|791-preInit
            // write pre-init user code here
            String str = getRecordValue(56);
            textField46 = new TextField(LocalizationSupport.getMessage("Male_deaths_>5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|791-getter|1|791-postInit
            // write post-init user code here
        }//GEN-BEGIN:|791-getter|2|
        return textField46;
    }
//</editor-fold>//GEN-END:|791-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField47 ">//GEN-BEGIN:|792-getter|0|792-preInit
    /**
     * Returns an initialized instance of textField47 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField47() {
        if (textField47 == null) {//GEN-END:|792-getter|0|792-preInit
            // write pre-init user code here
            String str = getRecordValue(57);
            textField47 = new TextField(LocalizationSupport.getMessage("Female_deaths_>5yrs_10"), str, 4, TextField.NUMERIC);//GEN-LINE:|792-getter|1|792-postInit
            // write post-init user code here
        }//GEN-BEGIN:|792-getter|2|
        return textField47;
    }
//</editor-fold>//GEN-END:|792-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecases5yrsBackCmd ">//GEN-BEGIN:|723-getter|0|723-preInit
    /**
     * Returns an initialized instance of Femalecases5yrsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecases5yrsBackCmd() {
        if (Femalecases5yrsBackCmd == null) {//GEN-END:|723-getter|0|723-preInit
            // write pre-init user code here
            Femalecases5yrsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|723-getter|1|723-postInit
            // write post-init user code here
        }//GEN-BEGIN:|723-getter|2|
        return Femalecases5yrsBackCmd;
    }
//</editor-fold>//GEN-END:|723-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecases5yrsOkCmd ">//GEN-BEGIN:|725-getter|0|725-preInit
    /**
     * Returns an initialized instance of Femalecases5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecases5yrsOkCmd() {
        if (Femalecases5yrsOkCmd == null) {//GEN-END:|725-getter|0|725-preInit
            // write pre-init user code here
            Femalecases5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|725-getter|1|725-postInit
            // write post-init user code here
        }//GEN-BEGIN:|725-getter|2|
        return Femalecases5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|725-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeaths5yrsBackCmd ">//GEN-BEGIN:|733-getter|0|733-preInit
    /**
     * Returns an initialized instance of Maledeaths5yrsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeaths5yrsBackCmd() {
        if (Maledeaths5yrsBackCmd == null) {//GEN-END:|733-getter|0|733-preInit
            // write pre-init user code here
            Maledeaths5yrsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|733-getter|1|733-postInit
            // write post-init user code here
        }//GEN-BEGIN:|733-getter|2|
        return Maledeaths5yrsBackCmd;
    }
//</editor-fold>//GEN-END:|733-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeaths5yrsOkCmd ">//GEN-BEGIN:|735-getter|0|735-preInit
    /**
     * Returns an initialized instance of Maledeaths5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeaths5yrsOkCmd() {
        if (Maledeaths5yrsOkCmd == null) {//GEN-END:|735-getter|0|735-preInit
            // write pre-init user code here
            Maledeaths5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|735-getter|1|735-postInit
            // write post-init user code here
        }//GEN-BEGIN:|735-getter|2|
        return Maledeaths5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|735-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeaths5yrsBackCmd1 ">//GEN-BEGIN:|742-getter|0|742-preInit
    /**
     * Returns an initialized instance of Maledeaths5yrsBackCmd1 component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeaths5yrsBackCmd1() {
        if (Maledeaths5yrsBackCmd1 == null) {//GEN-END:|742-getter|0|742-preInit
            // write pre-init user code here
            Maledeaths5yrsBackCmd1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|742-getter|1|742-postInit
            // write post-init user code here
        }//GEN-BEGIN:|742-getter|2|
        return Maledeaths5yrsBackCmd1;
    }
//</editor-fold>//GEN-END:|742-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeaths5yrsOkCmd1 ">//GEN-BEGIN:|744-getter|0|744-preInit
    /**
     * Returns an initialized instance of Maledeaths5yrsOkCmd1 component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeaths5yrsOkCmd1() {
        if (Maledeaths5yrsOkCmd1 == null) {//GEN-END:|744-getter|0|744-preInit
            // write pre-init user code here
            Maledeaths5yrsOkCmd1 = new Command("Next", Command.OK, 0);//GEN-LINE:|744-getter|1|744-postInit
            // write post-init user code here
        }//GEN-BEGIN:|744-getter|2|
        return Maledeaths5yrsOkCmd1;
    }
//</editor-fold>//GEN-END:|744-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsLT5yrsBackCmd ">//GEN-BEGIN:|756-getter|0|756-preInit
    /**
     * Returns an initialized instance of FemaledeathsLT5yrsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsLT5yrsBackCmd() {
        if (FemaledeathsLT5yrsBackCmd == null) {//GEN-END:|756-getter|0|756-preInit
            // write pre-init user code here
            FemaledeathsLT5yrsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|756-getter|1|756-postInit
            // write post-init user code here
        }//GEN-BEGIN:|756-getter|2|
        return FemaledeathsLT5yrsBackCmd;
    }
//</editor-fold>//GEN-END:|756-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsLT5yrsOkCmd ">//GEN-BEGIN:|758-getter|0|758-preInit
    /**
     * Returns an initialized instance of FemaledeathsLT5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsLT5yrsOkCmd() {
        if (FemaledeathsLT5yrsOkCmd == null) {//GEN-END:|758-getter|0|758-preInit
            // write pre-init user code here
            FemaledeathsLT5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|758-getter|1|758-postInit
            // write post-init user code here
        }//GEN-BEGIN:|758-getter|2|
        return FemaledeathsLT5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|758-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Fevermorethan7daysBackCmd ">//GEN-BEGIN:|765-getter|0|765-preInit
    /**
     * Returns an initialized instance of Fevermorethan7daysBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFevermorethan7daysBackCmd() {
        if (Fevermorethan7daysBackCmd == null) {//GEN-END:|765-getter|0|765-preInit
            // write pre-init user code here
            Fevermorethan7daysBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|765-getter|1|765-postInit
            // write post-init user code here
        }//GEN-BEGIN:|765-getter|2|
        return Fevermorethan7daysBackCmd;
    }
//</editor-fold>//GEN-END:|765-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Fevermorethan7daysOkCmd ">//GEN-BEGIN:|767-getter|0|767-preInit
    /**
     * Returns an initialized instance of Fevermorethan7daysOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFevermorethan7daysOkCmd() {
        if (Fevermorethan7daysOkCmd == null) {//GEN-END:|767-getter|0|767-preInit
            // write pre-init user code here
            Fevermorethan7daysOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|767-getter|1|767-postInit
            // write post-init user code here
        }//GEN-BEGIN:|767-getter|2|
        return Fevermorethan7daysOkCmd;
    }
//</editor-fold>//GEN-END:|767-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: CoughwithorwithoutfeverBackCmd ">//GEN-BEGIN:|778-getter|0|778-preInit
    /**
     * Returns an initialized instance of CoughwithorwithoutfeverBackCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getCoughwithorwithoutfeverBackCmd() {
        if (CoughwithorwithoutfeverBackCmd == null) {//GEN-END:|778-getter|0|778-preInit
            // write pre-init user code here
            CoughwithorwithoutfeverBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|778-getter|1|778-postInit
            // write post-init user code here
        }//GEN-BEGIN:|778-getter|2|
        return CoughwithorwithoutfeverBackCmd;
    }
//</editor-fold>//GEN-END:|778-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: CoughwithorwithoutfeverOkCmd ">//GEN-BEGIN:|780-getter|0|780-preInit
    /**
     * Returns an initialized instance of CoughwithorwithoutfeverOkCmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getCoughwithorwithoutfeverOkCmd() {
        if (CoughwithorwithoutfeverOkCmd == null) {//GEN-END:|780-getter|0|780-preInit
            // write pre-init user code here
            CoughwithorwithoutfeverOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|780-getter|1|780-postInit
            // write post-init user code here
        }//GEN-BEGIN:|780-getter|2|
        return CoughwithorwithoutfeverOkCmd;
    }
//</editor-fold>//GEN-END:|780-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Morethan3weeks ">//GEN-BEGIN:|793-getter|0|793-preInit
    /**
     * Returns an initialized instance of Morethan3weeks component.
     *
     * @return the initialized component instance
     */
    public Form getMorethan3weeks() {
        if (Morethan3weeks == null) {//GEN-END:|793-getter|0|793-preInit
            // write pre-init user code here
            Morethan3weeks = new Form("Cough With Or Without Fever", new Item[]{getStringItem12(), getTextField48(), getTextField49(), getTextField50(), getTextField51(), getTextField52(), getTextField53(), getTextField54(), getTextField55()});//GEN-BEGIN:|793-getter|1|793-postInit
            Morethan3weeks.addCommand(getMorethan3weeksBackCmd());
            Morethan3weeks.addCommand(getMorethan3weeksOkCmd());
            Morethan3weeks.setCommandListener(this);//GEN-END:|793-getter|1|793-postInit
            // write post-init user code here
        }//GEN-BEGIN:|793-getter|2|
        return Morethan3weeks;
    }
//</editor-fold>//GEN-END:|793-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField48 ">//GEN-BEGIN:|798-getter|0|798-preInit
    /**
     * Returns an initialized instance of textField48 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField48() {
        if (textField48 == null) {//GEN-END:|798-getter|0|798-preInit
            // write pre-init user code here
            String str = getRecordValue(58);
            textField48 = new TextField(LocalizationSupport.getMessage("Male_cases_<5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|798-getter|1|798-postInit
            // write post-init user code here
        }//GEN-BEGIN:|798-getter|2|
        return textField48;
    }
//</editor-fold>//GEN-END:|798-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField49 ">//GEN-BEGIN:|799-getter|0|799-preInit
    /**
     * Returns an initialized instance of textField49 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField49() {
        if (textField49 == null) {//GEN-END:|799-getter|0|799-preInit
            // write pre-init user code here
            String str = getRecordValue(59);
            textField49 = new TextField(LocalizationSupport.getMessage("Female_Cases_<5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|799-getter|1|799-postInit
            // write post-init user code here
        }//GEN-BEGIN:|799-getter|2|
        return textField49;
    }
//</editor-fold>//GEN-END:|799-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField50 ">//GEN-BEGIN:|800-getter|0|800-preInit
    /**
     * Returns an initialized instance of textField50 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField50() {
        if (textField50 == null) {//GEN-END:|800-getter|0|800-preInit
            // write pre-init user code here
            String str = getRecordValue(60);
            textField50 = new TextField(LocalizationSupport.getMessage("Male_cases_>5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|800-getter|1|800-postInit
            // write post-init user code here
        }//GEN-BEGIN:|800-getter|2|
        return textField50;
    }
//</editor-fold>//GEN-END:|800-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField51 ">//GEN-BEGIN:|801-getter|0|801-preInit
    /**
     * Returns an initialized instance of textField51 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField51() {
        if (textField51 == null) {//GEN-END:|801-getter|0|801-preInit
            // write pre-init user code here
            String str = getRecordValue(61);
            textField51 = new TextField(LocalizationSupport.getMessage("Female_cases_>5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|801-getter|1|801-postInit
            // write post-init user code here
        }//GEN-BEGIN:|801-getter|2|
        return textField51;
    }
//</editor-fold>//GEN-END:|801-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField52 ">//GEN-BEGIN:|802-getter|0|802-preInit
    /**
     * Returns an initialized instance of textField52 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField52() {
        if (textField52 == null) {//GEN-END:|802-getter|0|802-preInit
            // write pre-init user code here
            String str = getRecordValue(62);
            textField52 = new TextField(LocalizationSupport.getMessage("Male_deaths_<5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|802-getter|1|802-postInit
            // write post-init user code here
        }//GEN-BEGIN:|802-getter|2|
        return textField52;
    }
//</editor-fold>//GEN-END:|802-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField53 ">//GEN-BEGIN:|803-getter|0|803-preInit
    /**
     * Returns an initialized instance of textField53 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField53() {
        if (textField53 == null) {//GEN-END:|803-getter|0|803-preInit
            // write pre-init user code here
            String str = getRecordValue(63);
            textField53 = new TextField(LocalizationSupport.getMessage("Female_cases_<5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|803-getter|1|803-postInit
            // write post-init user code here
        }//GEN-BEGIN:|803-getter|2|
        return textField53;
    }
//</editor-fold>//GEN-END:|803-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField54 ">//GEN-BEGIN:|804-getter|0|804-preInit
    /**
     * Returns an initialized instance of textField54 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField54() {
        if (textField54 == null) {//GEN-END:|804-getter|0|804-preInit
            // write pre-init user code here
            String str = getRecordValue(64);
            textField54 = new TextField(LocalizationSupport.getMessage("Male_deaths_>5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|804-getter|1|804-postInit
            // write post-init user code here
        }//GEN-BEGIN:|804-getter|2|
        return textField54;
    }
//</editor-fold>//GEN-END:|804-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField55 ">//GEN-BEGIN:|805-getter|0|805-preInit
    /**
     * Returns an initialized instance of textField55 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField55() {
        if (textField55 == null) {//GEN-END:|805-getter|0|805-preInit
            // write pre-init user code here
            String str = getRecordValue(65);
            textField55 = new TextField(LocalizationSupport.getMessage("Female_deaths_>5yrs_11"), str, 4, TextField.NUMERIC);//GEN-LINE:|805-getter|1|805-postInit
            // write post-init user code here
        }//GEN-BEGIN:|805-getter|2|
        return textField55;
    }
//</editor-fold>//GEN-END:|805-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Weekspage ">//GEN-BEGIN:|806-getter|0|806-preInit
    /**
     * Returns an initialized instance of Weekspage component.
     *
     * @return the initialized component instance
     */
    public Form getWeekspage() {
        if (Weekspage == null) {//GEN-END:|806-getter|0|806-preInit
            // write pre-init user code here
            Weekspage = new Form("form");//GEN-BEGIN:|806-getter|1|806-postInit
            Weekspage.addCommand(getWeekspageOkCmd());
            Weekspage.addCommand(getWeekspageExitCmd());
            Weekspage.setCommandListener(this);//GEN-END:|806-getter|1|806-postInit
            // write post-init user code here
        }//GEN-BEGIN:|806-getter|2|
        return Weekspage;
    }
//</editor-fold>//GEN-END:|806-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Morethan3weeksBackCmd ">//GEN-BEGIN:|794-getter|0|794-preInit
    /**
     * Returns an initialized instance of Morethan3weeksBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMorethan3weeksBackCmd() {
        if (Morethan3weeksBackCmd == null) {//GEN-END:|794-getter|0|794-preInit
            // write pre-init user code here
            Morethan3weeksBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|794-getter|1|794-postInit
            // write post-init user code here
        }//GEN-BEGIN:|794-getter|2|
        return Morethan3weeksBackCmd;
    }
//</editor-fold>//GEN-END:|794-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Morethan3weeksOkCmd ">//GEN-BEGIN:|796-getter|0|796-preInit
    /**
     * Returns an initialized instance of Morethan3weeksOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMorethan3weeksOkCmd() {
        if (Morethan3weeksOkCmd == null) {//GEN-END:|796-getter|0|796-preInit
            // write pre-init user code here
            Morethan3weeksOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|796-getter|1|796-postInit
            // write post-init user code here
        }//GEN-BEGIN:|796-getter|2|
        return Morethan3weeksOkCmd;
    }
//</editor-fold>//GEN-END:|796-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand ">//GEN-BEGIN:|807-getter|0|807-preInit
    /**
     * Returns an initialized instance of cancelCommand component.
     *
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {//GEN-END:|807-getter|0|807-preInit
            // write pre-init user code here
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|807-getter|1|807-postInit
            // write post-init user code here
        }//GEN-BEGIN:|807-getter|2|
        return cancelCommand;
    }
//</editor-fold>//GEN-END:|807-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: WeekspageBackCmd ">//GEN-BEGIN:|809-getter|0|809-preInit
    /**
     * Returns an initialized instance of WeekspageBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getWeekspageBackCmd() {
        if (WeekspageBackCmd == null) {//GEN-END:|809-getter|0|809-preInit
            // write pre-init user code here
            WeekspageBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|809-getter|1|809-postInit
            // write post-init user code here
        }//GEN-BEGIN:|809-getter|2|
        return WeekspageBackCmd;
    }
//</editor-fold>//GEN-END:|809-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: WeekspageOkCmd ">//GEN-BEGIN:|811-getter|0|811-preInit
    /**
     * Returns an initialized instance of WeekspageOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getWeekspageOkCmd() {
        if (WeekspageOkCmd == null) {//GEN-END:|811-getter|0|811-preInit
            // write pre-init user code here
            WeekspageOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|811-getter|1|811-postInit
            // write post-init user code here
        }//GEN-BEGIN:|811-getter|2|
        return WeekspageOkCmd;
    }
//</editor-fold>//GEN-END:|811-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: WeekspageExitCmd ">//GEN-BEGIN:|813-getter|0|813-preInit
    /**
     * Returns an initialized instance of WeekspageExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getWeekspageExitCmd() {
        if (WeekspageExitCmd == null) {//GEN-END:|813-getter|0|813-preInit
            // write pre-init user code here
            WeekspageExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|813-getter|1|813-postInit
            // write post-init user code here
        }//GEN-BEGIN:|813-getter|2|
        return WeekspageExitCmd;
    }
//</editor-fold>//GEN-END:|813-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesLT5yrs ">//GEN-BEGIN:|838-getter|0|838-preInit
    /**
     * Returns an initialized instance of FemalecasesLT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemalecasesLT5yrs() {
        if (FemalecasesLT5yrs == null) {//GEN-END:|838-getter|0|838-preInit
            // write pre-init user code here
            FemalecasesLT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem6(), getTextField4(), getTextField5(), getTextField6(), getTextField7()});//GEN-BEGIN:|838-getter|1|838-postInit
            FemalecasesLT5yrs.addCommand(getFemalecasesLT5yrsBackCmd());
            FemalecasesLT5yrs.addCommand(getFemalecasesLT5yrsOkCmd());
            FemalecasesLT5yrs.setCommandListener(this);//GEN-END:|838-getter|1|838-postInit
            // write post-init user code here
        }//GEN-BEGIN:|838-getter|2|
        return FemalecasesLT5yrs;
    }
//</editor-fold>//GEN-END:|838-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField4 ">//GEN-BEGIN:|843-getter|0|843-preInit
    /**
     * Returns an initialized instance of textField4 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField4() {
        if (textField4 == null) {//GEN-END:|843-getter|0|843-preInit
            // write pre-init user code here
            String str = getRecordValue(14);
            textField4 = new TextField(LocalizationSupport.getMessage("Only_Fever_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|843-getter|1|843-postInit
            // write post-init user code here
        }//GEN-BEGIN:|843-getter|2|
        return textField4;
    }
//</editor-fold>//GEN-END:|843-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField5 ">//GEN-BEGIN:|844-getter|0|844-preInit
    /**
     * Returns an initialized instance of textField5 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField5() {
        if (textField5 == null) {//GEN-END:|844-getter|0|844-preInit
            // write pre-init user code here
            String str = getRecordValue(15);
            textField5 = new TextField(LocalizationSupport.getMessage("With_Rash_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|844-getter|1|844-postInit
            // write post-init user code here
        }//GEN-BEGIN:|844-getter|2|
        return textField5;
    }
//</editor-fold>//GEN-END:|844-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField6 ">//GEN-BEGIN:|845-getter|0|845-preInit
    /**
     * Returns an initialized instance of textField6 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField6() {
        if (textField6 == null) {//GEN-END:|845-getter|0|845-preInit
            // write pre-init user code here
            String str = getRecordValue(16);
            textField6 = new TextField(LocalizationSupport.getMessage("With_Bleeding_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|845-getter|1|845-postInit
            // write post-init user code here
        }//GEN-BEGIN:|845-getter|2|
        return textField6;
    }
//</editor-fold>//GEN-END:|845-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField7 ">//GEN-BEGIN:|846-getter|0|846-preInit
    /**
     * Returns an initialized instance of textField7 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField7() {
        if (textField7 == null) {//GEN-END:|846-getter|0|846-preInit
            // write pre-init user code here
            String str = getRecordValue(17);
            textField7 = new TextField(LocalizationSupport.getMessage("With_Daze_2"), str, 4, TextField.NUMERIC);//GEN-LINE:|846-getter|1|846-postInit
            // write post-init user code here
        }//GEN-BEGIN:|846-getter|2|
        return textField7;
    }
//</editor-fold>//GEN-END:|846-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsGT5yrs ">//GEN-BEGIN:|847-getter|0|847-preInit
    /**
     * Returns an initialized instance of FemaledeathsGT5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemaledeathsGT5yrs() {
        if (FemaledeathsGT5yrs == null) {//GEN-END:|847-getter|0|847-preInit
            // write pre-init user code here
            FemaledeathsGT5yrs = new Form("Fever < 7 days", new Item[]{getStringItem11(), getTextField24(), getTextField25(), getTextField26(), getTextField27()});//GEN-BEGIN:|847-getter|1|847-postInit
            FemaledeathsGT5yrs.addCommand(getFemaledeathsGT5yrsBackcmd());
            FemaledeathsGT5yrs.addCommand(getFemaledeathsGT5yrsOkCmd());
            FemaledeathsGT5yrs.setCommandListener(this);//GEN-END:|847-getter|1|847-postInit
            // write post-init user code here
        }//GEN-BEGIN:|847-getter|2|
        return FemaledeathsGT5yrs;
    }
//</editor-fold>//GEN-END:|847-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField24 ">//GEN-BEGIN:|852-getter|0|852-preInit
    /**
     * Returns an initialized instance of textField24 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField24() {
        if (textField24 == null) {//GEN-END:|852-getter|0|852-preInit
            // write pre-init user code here
            String str = getRecordValue(34);
            textField24 = new TextField(LocalizationSupport.getMessage("Only_Fever_8"), str, 4, TextField.NUMERIC);//GEN-LINE:|852-getter|1|852-postInit
            // write post-init user code here
        }//GEN-BEGIN:|852-getter|2|
        return textField24;
    }
//</editor-fold>//GEN-END:|852-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField25 ">//GEN-BEGIN:|853-getter|0|853-preInit
    /**
     * Returns an initialized instance of textField25 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField25() {
        if (textField25 == null) {//GEN-END:|853-getter|0|853-preInit
            // write pre-init user code here
            String str = getRecordValue(35);
            textField25 = new TextField(LocalizationSupport.getMessage("With_Rash_8"), str, 4, TextField.NUMERIC);//GEN-LINE:|853-getter|1|853-postInit
            // write post-init user code here
        }//GEN-BEGIN:|853-getter|2|
        return textField25;
    }
//</editor-fold>//GEN-END:|853-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField26 ">//GEN-BEGIN:|854-getter|0|854-preInit
    /**
     * Returns an initialized instance of textField26 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField26() {
        if (textField26 == null) {//GEN-END:|854-getter|0|854-preInit
            // write pre-init user code here
            String str = getRecordValue(36);
            textField26 = new TextField(LocalizationSupport.getMessage("With_Bleeding_8"), str, 4, TextField.NUMERIC);//GEN-LINE:|854-getter|1|854-postInit
            // write post-init user code here
        }//GEN-BEGIN:|854-getter|2|
        return textField26;
    }
//</editor-fold>//GEN-END:|854-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField27 ">//GEN-BEGIN:|855-getter|0|855-preInit
    /**
     * Returns an initialized instance of textField27 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField27() {
        if (textField27 == null) {//GEN-END:|855-getter|0|855-preInit
            // write pre-init user code here
            String str = getRecordValue(37);
            textField27 = new TextField(LocalizationSupport.getMessage("With_Daze_8"), str, 4, TextField.NUMERIC);//GEN-LINE:|855-getter|1|855-postInit
            // write post-init user code here
        }//GEN-BEGIN:|855-getter|2|
        return textField27;
    }
//</editor-fold>//GEN-END:|855-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesLT5yrsBackCmd ">//GEN-BEGIN:|839-getter|0|839-preInit
    /**
     * Returns an initialized instance of FemalecasesLT5yrsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesLT5yrsBackCmd() {
        if (FemalecasesLT5yrsBackCmd == null) {//GEN-END:|839-getter|0|839-preInit
            // write pre-init user code here
            FemalecasesLT5yrsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|839-getter|1|839-postInit
            // write post-init user code here
        }//GEN-BEGIN:|839-getter|2|
        return FemalecasesLT5yrsBackCmd;
    }
//</editor-fold>//GEN-END:|839-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesLT5yrsOkCmd ">//GEN-BEGIN:|841-getter|0|841-preInit
    /**
     * Returns an initialized instance of FemalecasesLT5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesLT5yrsOkCmd() {
        if (FemalecasesLT5yrsOkCmd == null) {//GEN-END:|841-getter|0|841-preInit
            // write pre-init user code here
            FemalecasesLT5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|841-getter|1|841-postInit
            // write post-init user code here
        }//GEN-BEGIN:|841-getter|2|
        return FemalecasesLT5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|841-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsGT5yrsBackcmd ">//GEN-BEGIN:|848-getter|0|848-preInit
    /**
     * Returns an initialized instance of FemaledeathsGT5yrsBackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsGT5yrsBackcmd() {
        if (FemaledeathsGT5yrsBackcmd == null) {//GEN-END:|848-getter|0|848-preInit
            // write pre-init user code here
            FemaledeathsGT5yrsBackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|848-getter|1|848-postInit
            // write post-init user code here
        }//GEN-BEGIN:|848-getter|2|
        return FemaledeathsGT5yrsBackcmd;
    }
//</editor-fold>//GEN-END:|848-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemaledeathsGT5yrsOkCmd ">//GEN-BEGIN:|850-getter|0|850-preInit
    /**
     * Returns an initialized instance of FemaledeathsGT5yrsOkCmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsGT5yrsOkCmd() {
        if (FemaledeathsGT5yrsOkCmd == null) {//GEN-END:|850-getter|0|850-preInit
            // write pre-init user code here
            FemaledeathsGT5yrsOkCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|850-getter|1|850-postInit
            // write post-init user code here
        }//GEN-BEGIN:|850-getter|2|
        return FemaledeathsGT5yrsOkCmd;
    }
//</editor-fold>//GEN-END:|850-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Loosewaterystoolsoflessthan2weeksduration ">//GEN-BEGIN:|867-getter|0|867-preInit
    /**
     * Returns an initialized instance of
     * Loosewaterystoolsoflessthan2weeksduration component.
     *
     * @return the initialized component instance
     */
    public Form getLoosewaterystoolsoflessthan2weeksduration() {
        if (Loosewaterystoolsoflessthan2weeksduration == null) {//GEN-END:|867-getter|0|867-preInit
            // write pre-init user code here
            Loosewaterystoolsoflessthan2weeksduration = new Form(" Watery Stools", new Item[]{getStringItem1(), getTextField56(), getTextField57(), getTextField58()});//GEN-BEGIN:|867-getter|1|867-postInit
            Loosewaterystoolsoflessthan2weeksduration.addCommand(getLoosewaterystoolsbackCmd());
            Loosewaterystoolsoflessthan2weeksduration.addCommand(getLoosewaterystoolsokCmd());
            Loosewaterystoolsoflessthan2weeksduration.setCommandListener(this);//GEN-END:|867-getter|1|867-postInit
            // write post-init user code here
        }//GEN-BEGIN:|867-getter|2|
        return Loosewaterystoolsoflessthan2weeksduration;
    }
//</editor-fold>//GEN-END:|867-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecaseslessthan5yrs ">//GEN-BEGIN:|872-getter|0|872-preInit
    /**
     * Returns an initialized instance of Femalecaseslessthan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemalecaseslessthan5yrs() {
        if (Femalecaseslessthan5yrs == null) {//GEN-END:|872-getter|0|872-preInit
            // write pre-init user code here
            Femalecaseslessthan5yrs = new Form("Watery Stools", new Item[]{getStringItem13(), getTextField59(), getTextField60(), getTextField61()});//GEN-BEGIN:|872-getter|1|872-postInit
            Femalecaseslessthan5yrs.addCommand(getFemalecasesLT5yrsbackcmd());
            Femalecaseslessthan5yrs.addCommand(getFemalecasesLT5yrsokcmd());
            Femalecaseslessthan5yrs.setCommandListener(this);//GEN-END:|872-getter|1|872-postInit
            // write post-init user code here
        }//GEN-BEGIN:|872-getter|2|
        return Femalecaseslessthan5yrs;
    }
//</editor-fold>//GEN-END:|872-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malecasesmorethan5yrs ">//GEN-BEGIN:|877-getter|0|877-preInit
    /**
     * Returns an initialized instance of Malecasesmorethan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMalecasesmorethan5yrs() {
        if (Malecasesmorethan5yrs == null) {//GEN-END:|877-getter|0|877-preInit
            // write pre-init user code here
            Malecasesmorethan5yrs = new Form("Watery Stools", new Item[]{getStringItem14(), getTextField62(), getTextField63(), getTextField64()});//GEN-BEGIN:|877-getter|1|877-postInit
            Malecasesmorethan5yrs.addCommand(getMalecasesmorethan5yrsbackcmd());
            Malecasesmorethan5yrs.addCommand(getMalecasesmorethan5yrsokcmd());
            Malecasesmorethan5yrs.setCommandListener(this);//GEN-END:|877-getter|1|877-postInit
            // write post-init user code here
        }//GEN-BEGIN:|877-getter|2|
        return Malecasesmorethan5yrs;
    }
//</editor-fold>//GEN-END:|877-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecasesmorethan5yrs ">//GEN-BEGIN:|882-getter|0|882-preInit
    /**
     * Returns an initialized instance of Femalecasesmorethan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemalecasesmorethan5yrs() {
        if (Femalecasesmorethan5yrs == null) {//GEN-END:|882-getter|0|882-preInit
            // write pre-init user code here
            Femalecasesmorethan5yrs = new Form("Watery Stools", new Item[]{getStringItem19(), getTextField65(), getTextField66(), getTextField67()});//GEN-BEGIN:|882-getter|1|882-postInit
            Femalecasesmorethan5yrs.addCommand(getFemalecasesmorethan5yrsbackcmd());
            Femalecasesmorethan5yrs.addCommand(getFemalecasesmorethan5yrsokcmd());
            Femalecasesmorethan5yrs.setCommandListener(this);//GEN-END:|882-getter|1|882-postInit
            // write post-init user code here
        }//GEN-BEGIN:|882-getter|2|
        return Femalecasesmorethan5yrs;
    }
//</editor-fold>//GEN-END:|882-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathslessthan5yrs ">//GEN-BEGIN:|887-getter|0|887-preInit
    /**
     * Returns an initialized instance of Maledeathslessthan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMaledeathslessthan5yrs() {
        if (Maledeathslessthan5yrs == null) {//GEN-END:|887-getter|0|887-preInit
            // write pre-init user code here
            Maledeathslessthan5yrs = new Form("Watery Stools", new Item[]{getStringItem16(), getTextField68(), getTextField69(), getTextField70()});//GEN-BEGIN:|887-getter|1|887-postInit
            Maledeathslessthan5yrs.addCommand(getMaledeathslessthan5yrsbackcmd());
            Maledeathslessthan5yrs.addCommand(getMaledeathslessthan5yrsokcmd());
            Maledeathslessthan5yrs.setCommandListener(this);//GEN-END:|887-getter|1|887-postInit
            // write post-init user code here
        }//GEN-BEGIN:|887-getter|2|
        return Maledeathslessthan5yrs;
    }
//</editor-fold>//GEN-END:|887-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathslessthan5yrs ">//GEN-BEGIN:|892-getter|0|892-preInit
    /**
     * Returns an initialized instance of Femaledeathslessthan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemaledeathslessthan5yrs() {
        if (Femaledeathslessthan5yrs == null) {//GEN-END:|892-getter|0|892-preInit
            // write pre-init user code here
            Femaledeathslessthan5yrs = new Form("Watery Stools", new Item[]{getStringItem17(), getTextField71(), getTextField72(), getTextField73()});//GEN-BEGIN:|892-getter|1|892-postInit
            Femaledeathslessthan5yrs.addCommand(getFemaledeathslessthan5yrsbackcmd());
            Femaledeathslessthan5yrs.addCommand(getFemaledeathslessthan5yrsokcmd());
            Femaledeathslessthan5yrs.setCommandListener(this);//GEN-END:|892-getter|1|892-postInit
            // write post-init user code here
        }//GEN-BEGIN:|892-getter|2|
        return Femaledeathslessthan5yrs;
    }
//</editor-fold>//GEN-END:|892-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathsmorethan5yrs ">//GEN-BEGIN:|897-getter|0|897-preInit
    /**
     * Returns an initialized instance of Maledeathsmorethan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getMaledeathsmorethan5yrs() {
        if (Maledeathsmorethan5yrs == null) {//GEN-END:|897-getter|0|897-preInit
            // write pre-init user code here
            Maledeathsmorethan5yrs = new Form(" Watery Stools", new Item[]{getStringItem18(), getTextField74(), getTextField75(), getTextField76()});//GEN-BEGIN:|897-getter|1|897-postInit
            Maledeathsmorethan5yrs.addCommand(getMaledeathsmorethan5yrsbackcmd());
            Maledeathsmorethan5yrs.addCommand(getMaledeathsmorethan5yrsokcmd());
            Maledeathsmorethan5yrs.setCommandListener(this);//GEN-END:|897-getter|1|897-postInit
            // write post-init user code here
        }//GEN-BEGIN:|897-getter|2|
        return Maledeathsmorethan5yrs;
    }
//</editor-fold>//GEN-END:|897-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathsmorethan5yrs ">//GEN-BEGIN:|902-getter|0|902-preInit
    /**
     * Returns an initialized instance of Femaledeathsmorethan5yrs component.
     *
     * @return the initialized component instance
     */
    public Form getFemaledeathsmorethan5yrs() {
        if (Femaledeathsmorethan5yrs == null) {//GEN-END:|902-getter|0|902-preInit
            // write pre-init user code here
            Femaledeathsmorethan5yrs = new Form(" Watery Stools", new Item[]{getStringItem15(), getTextField77(), getTextField78(), getTextField79()});//GEN-BEGIN:|902-getter|1|902-postInit
            Femaledeathsmorethan5yrs.addCommand(getFemaledeathsmorethan5yrsbackcmd());
            Femaledeathsmorethan5yrs.addCommand(getFemaledeathsmorethan5yrsokcmd());
            Femaledeathsmorethan5yrs.setCommandListener(this);//GEN-END:|902-getter|1|902-postInit
            // write post-init user code here
        }//GEN-BEGIN:|902-getter|2|
        return Femaledeathsmorethan5yrs;
    }
//</editor-fold>//GEN-END:|902-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Jaundicecasesoflessthan4weeksduration ">//GEN-BEGIN:|909-getter|0|909-preInit
    /**
     * Returns an initialized instance of Jaundicecasesoflessthan4weeksduration
     * component.
     *
     * @return the initialized component instance
     */
    public Form getJaundicecasesoflessthan4weeksduration() {
        if (Jaundicecasesoflessthan4weeksduration == null) {//GEN-END:|909-getter|0|909-preInit
            // write pre-init user code here
            Jaundicecasesoflessthan4weeksduration = new Form("Jaundice < 4 Wks", new Item[]{getStringItem3(), getTextField80(), getTextField81(), getTextField82(), getTextField83(), getTextField84(), getTextField85(), getTextField86(), getTextField87()});//GEN-BEGIN:|909-getter|1|909-postInit
            Jaundicecasesoflessthan4weeksduration.addCommand(getJaundicecasesoflessthan4weeksdurationbackcmd());
            Jaundicecasesoflessthan4weeksduration.addCommand(getJaundicecasesoflessthan4weeksdurationokcmd());
            Jaundicecasesoflessthan4weeksduration.setCommandListener(this);//GEN-END:|909-getter|1|909-postInit
            // write post-init user code here
        }//GEN-BEGIN:|909-getter|2|
        return Jaundicecasesoflessthan4weeksduration;
    }
//</editor-fold>//GEN-END:|909-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AFPcaseslessthan15yrsofage ">//GEN-BEGIN:|914-getter|0|914-preInit
    /**
     * Returns an initialized instance of AFPcaseslessthan15yrsofage component.
     *
     * @return the initialized component instance
     */
    public Form getAFPcaseslessthan15yrsofage() {
        if (AFPcaseslessthan15yrsofage == null) {//GEN-END:|914-getter|0|914-preInit
            // write pre-init user code here
            AFPcaseslessthan15yrsofage = new Form("AFP Cases <15yrs Of Age ", new Item[]{getStringItem5(), getTextField88(), getTextField89(), getTextField90(), getTextField91(), getTextField92(), getTextField93(), getTextField94(), getTextField95()});//GEN-BEGIN:|914-getter|1|914-postInit
            AFPcaseslessthan15yrsofage.addCommand(getAFPcaseslessthan15yrsofagebackcmd());
            AFPcaseslessthan15yrsofage.addCommand(getAFPcaseslessthan15yrsofageokcmd());
            AFPcaseslessthan15yrsofage.setCommandListener(this);//GEN-END:|914-getter|1|914-postInit
            // write post-init user code here
        }//GEN-BEGIN:|914-getter|2|
        return AFPcaseslessthan15yrsofage;
    }
//</editor-fold>//GEN-END:|914-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Unusualsymptomsleadingtodeath ">//GEN-BEGIN:|919-getter|0|919-preInit
    /**
     * Returns an initialized instance of Unusualsymptomsleadingtodeath
     * component.
     *
     * @return the initialized component instance
     */
    public Form getUnusualsymptomsleadingtodeath() {
        if (Unusualsymptomsleadingtodeath == null) {//GEN-END:|919-getter|0|919-preInit
            // write pre-init user code here
            Unusualsymptomsleadingtodeath = new Form("Unusual Symptoms Leading To Death Or Hospitalization", new Item[]{getTextField96(), getTextField97(), getTextField98(), getTextField99(), getTextField100(), getTextField101(), getTextField102(), getTextField103()});//GEN-BEGIN:|919-getter|1|919-postInit
            Unusualsymptomsleadingtodeath.addCommand(getUnusualsymptomsleadingtodeathbackcmd());
            Unusualsymptomsleadingtodeath.addCommand(getUnusualsymptomsleadingtodeathokcmd());
            Unusualsymptomsleadingtodeath.setCommandListener(this);//GEN-END:|919-getter|1|919-postInit
            // write post-init user code here
        }//GEN-BEGIN:|919-getter|2|
        return Unusualsymptomsleadingtodeath;
    }
//</editor-fold>//GEN-END:|919-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: loosewaterystoolsbackCmd ">//GEN-BEGIN:|868-getter|0|868-preInit
    /**
     * Returns an initialized instance of loosewaterystoolsbackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLoosewaterystoolsbackCmd() {
        if (loosewaterystoolsbackCmd == null) {//GEN-END:|868-getter|0|868-preInit
            // write pre-init user code here
            loosewaterystoolsbackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|868-getter|1|868-postInit
            // write post-init user code here
        }//GEN-BEGIN:|868-getter|2|
        return loosewaterystoolsbackCmd;
    }
//</editor-fold>//GEN-END:|868-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: loosewaterystoolsokCmd ">//GEN-BEGIN:|870-getter|0|870-preInit
    /**
     * Returns an initialized instance of loosewaterystoolsokCmd component.
     *
     * @return the initialized component instance
     */
    public Command getLoosewaterystoolsokCmd() {
        if (loosewaterystoolsokCmd == null) {//GEN-END:|870-getter|0|870-preInit
            // write pre-init user code here
            loosewaterystoolsokCmd = new Command("Next", Command.OK, 0);//GEN-LINE:|870-getter|1|870-postInit
            // write post-init user code here
        }//GEN-BEGIN:|870-getter|2|
        return loosewaterystoolsokCmd;
    }
//</editor-fold>//GEN-END:|870-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesLT5yrsbackcmd ">//GEN-BEGIN:|873-getter|0|873-preInit
    /**
     * Returns an initialized instance of FemalecasesLT5yrsbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesLT5yrsbackcmd() {
        if (FemalecasesLT5yrsbackcmd == null) {//GEN-END:|873-getter|0|873-preInit
            // write pre-init user code here
            FemalecasesLT5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|873-getter|1|873-postInit
            // write post-init user code here
        }//GEN-BEGIN:|873-getter|2|
        return FemalecasesLT5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|873-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: FemalecasesLT5yrsokcmd ">//GEN-BEGIN:|875-getter|0|875-preInit
    /**
     * Returns an initialized instance of FemalecasesLT5yrsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesLT5yrsokcmd() {
        if (FemalecasesLT5yrsokcmd == null) {//GEN-END:|875-getter|0|875-preInit
            // write pre-init user code here
            FemalecasesLT5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|875-getter|1|875-postInit
            // write post-init user code here
        }//GEN-BEGIN:|875-getter|2|
        return FemalecasesLT5yrsokcmd;
    }
//</editor-fold>//GEN-END:|875-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malecasesmorethan5yrsbackcmd ">//GEN-BEGIN:|878-getter|0|878-preInit
    /**
     * Returns an initialized instance of Malecasesmorethan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMalecasesmorethan5yrsbackcmd() {
        if (Malecasesmorethan5yrsbackcmd == null) {//GEN-END:|878-getter|0|878-preInit
            // write pre-init user code here
            Malecasesmorethan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|878-getter|1|878-postInit
            // write post-init user code here
        }//GEN-BEGIN:|878-getter|2|
        return Malecasesmorethan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|878-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Malecasesmorethan5yrsokcmd ">//GEN-BEGIN:|880-getter|0|880-preInit
    /**
     * Returns an initialized instance of Malecasesmorethan5yrsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getMalecasesmorethan5yrsokcmd() {
        if (Malecasesmorethan5yrsokcmd == null) {//GEN-END:|880-getter|0|880-preInit
            // write pre-init user code here
            Malecasesmorethan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|880-getter|1|880-postInit
            // write post-init user code here
        }//GEN-BEGIN:|880-getter|2|
        return Malecasesmorethan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|880-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecasesmorethan5yrsbackcmd ">//GEN-BEGIN:|883-getter|0|883-preInit
    /**
     * Returns an initialized instance of Femalecasesmorethan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesmorethan5yrsbackcmd() {
        if (Femalecasesmorethan5yrsbackcmd == null) {//GEN-END:|883-getter|0|883-preInit
            // write pre-init user code here
            Femalecasesmorethan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|883-getter|1|883-postInit
            // write post-init user code here
        }//GEN-BEGIN:|883-getter|2|
        return Femalecasesmorethan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|883-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femalecasesmorethan5yrsokcmd ">//GEN-BEGIN:|885-getter|0|885-preInit
    /**
     * Returns an initialized instance of Femalecasesmorethan5yrsokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemalecasesmorethan5yrsokcmd() {
        if (Femalecasesmorethan5yrsokcmd == null) {//GEN-END:|885-getter|0|885-preInit
            // write pre-init user code here
            Femalecasesmorethan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|885-getter|1|885-postInit
            // write post-init user code here
        }//GEN-BEGIN:|885-getter|2|
        return Femalecasesmorethan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|885-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathslessthan5yrsbackcmd ">//GEN-BEGIN:|888-getter|0|888-preInit
    /**
     * Returns an initialized instance of Maledeathslessthan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeathslessthan5yrsbackcmd() {
        if (Maledeathslessthan5yrsbackcmd == null) {//GEN-END:|888-getter|0|888-preInit
            // write pre-init user code here
            Maledeathslessthan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|888-getter|1|888-postInit
            // write post-init user code here
        }//GEN-BEGIN:|888-getter|2|
        return Maledeathslessthan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|888-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathslessthan5yrsokcmd ">//GEN-BEGIN:|890-getter|0|890-preInit
    /**
     * Returns an initialized instance of Maledeathslessthan5yrsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeathslessthan5yrsokcmd() {
        if (Maledeathslessthan5yrsokcmd == null) {//GEN-END:|890-getter|0|890-preInit
            // write pre-init user code here
            Maledeathslessthan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|890-getter|1|890-postInit
            // write post-init user code here
        }//GEN-BEGIN:|890-getter|2|
        return Maledeathslessthan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|890-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathslessthan5yrsbackcmd ">//GEN-BEGIN:|893-getter|0|893-preInit
    /**
     * Returns an initialized instance of Femaledeathslessthan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathslessthan5yrsbackcmd() {
        if (Femaledeathslessthan5yrsbackcmd == null) {//GEN-END:|893-getter|0|893-preInit
            // write pre-init user code here
            Femaledeathslessthan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|893-getter|1|893-postInit
            // write post-init user code here
        }//GEN-BEGIN:|893-getter|2|
        return Femaledeathslessthan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|893-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathslessthan5yrsokcmd ">//GEN-BEGIN:|895-getter|0|895-preInit
    /**
     * Returns an initialized instance of Femaledeathslessthan5yrsokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathslessthan5yrsokcmd() {
        if (Femaledeathslessthan5yrsokcmd == null) {//GEN-END:|895-getter|0|895-preInit
            // write pre-init user code here
            Femaledeathslessthan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|895-getter|1|895-postInit
            // write post-init user code here
        }//GEN-BEGIN:|895-getter|2|
        return Femaledeathslessthan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|895-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathsmorethan5yrsbackcmd ">//GEN-BEGIN:|898-getter|0|898-preInit
    /**
     * Returns an initialized instance of Maledeathsmorethan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeathsmorethan5yrsbackcmd() {
        if (Maledeathsmorethan5yrsbackcmd == null) {//GEN-END:|898-getter|0|898-preInit
            // write pre-init user code here
            Maledeathsmorethan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|898-getter|1|898-postInit
            // write post-init user code here
        }//GEN-BEGIN:|898-getter|2|
        return Maledeathsmorethan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|898-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Maledeathsmorethan5yrsokcmd ">//GEN-BEGIN:|900-getter|0|900-preInit
    /**
     * Returns an initialized instance of Maledeathsmorethan5yrsokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getMaledeathsmorethan5yrsokcmd() {
        if (Maledeathsmorethan5yrsokcmd == null) {//GEN-END:|900-getter|0|900-preInit
            // write pre-init user code here
            Maledeathsmorethan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|900-getter|1|900-postInit
            // write post-init user code here
        }//GEN-BEGIN:|900-getter|2|
        return Maledeathsmorethan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|900-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand1 ">//GEN-BEGIN:|903-getter|0|903-preInit
    /**
     * Returns an initialized instance of cancelCommand1 component.
     *
     * @return the initialized component instance
     */
    public Command getCancelCommand1() {
        if (cancelCommand1 == null) {//GEN-END:|903-getter|0|903-preInit
            // write pre-init user code here
            cancelCommand1 = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|903-getter|1|903-postInit
            // write post-init user code here
        }//GEN-BEGIN:|903-getter|2|
        return cancelCommand1;
    }
//</editor-fold>//GEN-END:|903-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathsmorethan5yrsbackcmd ">//GEN-BEGIN:|905-getter|0|905-preInit
    /**
     * Returns an initialized instance of Femaledeathsmorethan5yrsbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsmorethan5yrsbackcmd() {
        if (Femaledeathsmorethan5yrsbackcmd == null) {//GEN-END:|905-getter|0|905-preInit
            // write pre-init user code here
            Femaledeathsmorethan5yrsbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|905-getter|1|905-postInit
            // write post-init user code here
        }//GEN-BEGIN:|905-getter|2|
        return Femaledeathsmorethan5yrsbackcmd;
    }
//</editor-fold>//GEN-END:|905-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Femaledeathsmorethan5yrsokcmd ">//GEN-BEGIN:|907-getter|0|907-preInit
    /**
     * Returns an initialized instance of Femaledeathsmorethan5yrsokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getFemaledeathsmorethan5yrsokcmd() {
        if (Femaledeathsmorethan5yrsokcmd == null) {//GEN-END:|907-getter|0|907-preInit
            // write pre-init user code here
            Femaledeathsmorethan5yrsokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|907-getter|1|907-postInit
            // write post-init user code here
        }//GEN-BEGIN:|907-getter|2|
        return Femaledeathsmorethan5yrsokcmd;
    }
//</editor-fold>//GEN-END:|907-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Jaundicecasesoflessthan4weeksdurationbackcmd ">//GEN-BEGIN:|910-getter|0|910-preInit
    /**
     * Returns an initialized instance of
     * Jaundicecasesoflessthan4weeksdurationbackcmd component.
     *
     * @return the initialized component instance
     */
    public Command getJaundicecasesoflessthan4weeksdurationbackcmd() {
        if (Jaundicecasesoflessthan4weeksdurationbackcmd == null) {//GEN-END:|910-getter|0|910-preInit
            // write pre-init user code here
            Jaundicecasesoflessthan4weeksdurationbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|910-getter|1|910-postInit
            // write post-init user code here
        }//GEN-BEGIN:|910-getter|2|
        return Jaundicecasesoflessthan4weeksdurationbackcmd;
    }
//</editor-fold>//GEN-END:|910-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Jaundicecasesoflessthan4weeksdurationokcmd ">//GEN-BEGIN:|912-getter|0|912-preInit
    /**
     * Returns an initialized instance of
     * Jaundicecasesoflessthan4weeksdurationokcmd component.
     *
     * @return the initialized component instance
     */
    public Command getJaundicecasesoflessthan4weeksdurationokcmd() {
        if (Jaundicecasesoflessthan4weeksdurationokcmd == null) {//GEN-END:|912-getter|0|912-preInit
            // write pre-init user code here
            Jaundicecasesoflessthan4weeksdurationokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|912-getter|1|912-postInit
            // write post-init user code here
        }//GEN-BEGIN:|912-getter|2|
        return Jaundicecasesoflessthan4weeksdurationokcmd;
    }
//</editor-fold>//GEN-END:|912-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AFPcaseslessthan15yrsofagebackcmd ">//GEN-BEGIN:|915-getter|0|915-preInit
    /**
     * Returns an initialized instance of AFPcaseslessthan15yrsofagebackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAFPcaseslessthan15yrsofagebackcmd() {
        if (AFPcaseslessthan15yrsofagebackcmd == null) {//GEN-END:|915-getter|0|915-preInit
            // write pre-init user code here
            AFPcaseslessthan15yrsofagebackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|915-getter|1|915-postInit
            // write post-init user code here
        }//GEN-BEGIN:|915-getter|2|
        return AFPcaseslessthan15yrsofagebackcmd;
    }
//</editor-fold>//GEN-END:|915-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AFPcaseslessthan15yrsofageokcmd ">//GEN-BEGIN:|917-getter|0|917-preInit
    /**
     * Returns an initialized instance of AFPcaseslessthan15yrsofageokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getAFPcaseslessthan15yrsofageokcmd() {
        if (AFPcaseslessthan15yrsofageokcmd == null) {//GEN-END:|917-getter|0|917-preInit
            // write pre-init user code here
            AFPcaseslessthan15yrsofageokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|917-getter|1|917-postInit
            // write post-init user code here
        }//GEN-BEGIN:|917-getter|2|
        return AFPcaseslessthan15yrsofageokcmd;
    }
//</editor-fold>//GEN-END:|917-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Unusualsymptomsleadingtodeathbackcmd ">//GEN-BEGIN:|920-getter|0|920-preInit
    /**
     * Returns an initialized instance of Unusualsymptomsleadingtodeathbackcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getUnusualsymptomsleadingtodeathbackcmd() {
        if (Unusualsymptomsleadingtodeathbackcmd == null) {//GEN-END:|920-getter|0|920-preInit
            // write pre-init user code here
            Unusualsymptomsleadingtodeathbackcmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|920-getter|1|920-postInit
            // write post-init user code here
        }//GEN-BEGIN:|920-getter|2|
        return Unusualsymptomsleadingtodeathbackcmd;
    }
//</editor-fold>//GEN-END:|920-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Unusualsymptomsleadingtodeathokcmd ">//GEN-BEGIN:|922-getter|0|922-preInit
    /**
     * Returns an initialized instance of Unusualsymptomsleadingtodeathokcmd
     * component.
     *
     * @return the initialized component instance
     */
    public Command getUnusualsymptomsleadingtodeathokcmd() {
        if (Unusualsymptomsleadingtodeathokcmd == null) {//GEN-END:|922-getter|0|922-preInit
            // write pre-init user code here
            Unusualsymptomsleadingtodeathokcmd = new Command("Next", Command.OK, 0);//GEN-LINE:|922-getter|1|922-postInit
            // write post-init user code here
        }//GEN-BEGIN:|922-getter|2|
        return Unusualsymptomsleadingtodeathokcmd;
    }
//</editor-fold>//GEN-END:|922-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem1 ">//GEN-BEGIN:|946-getter|0|946-preInit
    /**
     * Returns an initialized instance of stringItem1 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem1() {
        if (stringItem1 == null) {//GEN-END:|946-getter|0|946-preInit
            // write pre-init user code here
            stringItem1 = new StringItem("Male cases < 5 yrs", null);//GEN-LINE:|946-getter|1|946-postInit
            // write post-init user code here
        }//GEN-BEGIN:|946-getter|2|
        return stringItem1;
    }
//</editor-fold>//GEN-END:|946-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField56 ">//GEN-BEGIN:|947-getter|0|947-preInit
    /**
     * Returns an initialized instance of textField56 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField56() {
        if (textField56 == null) {//GEN-END:|947-getter|0|947-preInit
            // write pre-init user code here
            String str = getRecordValue(66);
            textField56 = new TextField(LocalizationSupport.getMessage("with_some_muchdehydration_12"), str, 4, TextField.NUMERIC);//GEN-LINE:|947-getter|1|947-postInit
            // write post-init user code here
        }//GEN-BEGIN:|947-getter|2|
        return textField56;
    }
//</editor-fold>//GEN-END:|947-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField57 ">//GEN-BEGIN:|948-getter|0|948-preInit
    /**
     * Returns an initialized instance of textField57 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField57() {
        if (textField57 == null) {//GEN-END:|948-getter|0|948-preInit
            // write pre-init user code here
            String str = getRecordValue(67);
            textField57 = new TextField(LocalizationSupport.getMessage("with_nodehydration_12"), str, 4, TextField.NUMERIC);//GEN-LINE:|948-getter|1|948-postInit
            // write post-init user code here
        }//GEN-BEGIN:|948-getter|2|
        return textField57;
    }
//</editor-fold>//GEN-END:|948-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField58 ">//GEN-BEGIN:|949-getter|0|949-preInit
    /**
     * Returns an initialized instance of textField58 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField58() {
        if (textField58 == null) {//GEN-END:|949-getter|0|949-preInit
            // write pre-init user code here
            String str = getRecordValue(68);
            textField58 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_12"), str, 4, TextField.NUMERIC);//GEN-LINE:|949-getter|1|949-postInit
            // write post-init user code here
        }//GEN-BEGIN:|949-getter|2|
        return textField58;
    }
//</editor-fold>//GEN-END:|949-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField59 ">//GEN-BEGIN:|950-getter|0|950-preInit
    /**
     * Returns an initialized instance of textField59 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField59() {
        if (textField59 == null) {//GEN-END:|950-getter|0|950-preInit
            // write pre-init user code here
            String str = getRecordValue(69);
            textField59 = new TextField(LocalizationSupport.getMessage("with_some_muchdehydration_13"), str, 4, TextField.NUMERIC);//GEN-LINE:|950-getter|1|950-postInit
            // write post-init user code here
        }//GEN-BEGIN:|950-getter|2|
        return textField59;
    }
//</editor-fold>//GEN-END:|950-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField60 ">//GEN-BEGIN:|951-getter|0|951-preInit
    /**
     * Returns an initialized instance of textField60 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField60() {
        if (textField60 == null) {//GEN-END:|951-getter|0|951-preInit
            // write pre-init user code here
            String str = getRecordValue(70);
            textField60 = new TextField(LocalizationSupport.getMessage("with_nodehydration_13"), str, 4, TextField.NUMERIC);//GEN-LINE:|951-getter|1|951-postInit
            // write post-init user code here
        }//GEN-BEGIN:|951-getter|2|
        return textField60;
    }
//</editor-fold>//GEN-END:|951-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField61 ">//GEN-BEGIN:|952-getter|0|952-preInit
    /**
     * Returns an initialized instance of textField61 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField61() {
        if (textField61 == null) {//GEN-END:|952-getter|0|952-preInit
            // write pre-init user code here
            String str = getRecordValue(71);
            textField61 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_13"), str, 4, TextField.NUMERIC);//GEN-LINE:|952-getter|1|952-postInit
            // write post-init user code here
        }//GEN-BEGIN:|952-getter|2|
        return textField61;
    }
//</editor-fold>//GEN-END:|952-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField62 ">//GEN-BEGIN:|953-getter|0|953-preInit
    /**
     * Returns an initialized instance of textField62 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField62() {
        if (textField62 == null) {//GEN-END:|953-getter|0|953-preInit
            // write pre-init user code here
            String str = getRecordValue(72);
            textField62 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_14"), str, 4, TextField.NUMERIC);//GEN-LINE:|953-getter|1|953-postInit
            // write post-init user code here
        }//GEN-BEGIN:|953-getter|2|
        return textField62;
    }
//</editor-fold>//GEN-END:|953-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField63 ">//GEN-BEGIN:|954-getter|0|954-preInit
    /**
     * Returns an initialized instance of textField63 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField63() {
        if (textField63 == null) {//GEN-END:|954-getter|0|954-preInit
            // write pre-init user code here
            String str = getRecordValue(73);
            textField63 = new TextField(LocalizationSupport.getMessage("with_nodehydration_14"), str, 4, TextField.NUMERIC);//GEN-LINE:|954-getter|1|954-postInit
            // write post-init user code here
        }//GEN-BEGIN:|954-getter|2|
        return textField63;
    }
//</editor-fold>//GEN-END:|954-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField64 ">//GEN-BEGIN:|955-getter|0|955-preInit
    /**
     * Returns an initialized instance of textField64 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField64() {
        if (textField64 == null) {//GEN-END:|955-getter|0|955-preInit
            // write pre-init user code here
            String str = getRecordValue(74);
            textField64 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_14"), str, 4, TextField.NUMERIC);//GEN-LINE:|955-getter|1|955-postInit
            // write post-init user code here
        }//GEN-BEGIN:|955-getter|2|
        return textField64;
    }
//</editor-fold>//GEN-END:|955-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField65 ">//GEN-BEGIN:|956-getter|0|956-preInit
    /**
     * Returns an initialized instance of textField65 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField65() {
        if (textField65 == null) {//GEN-END:|956-getter|0|956-preInit
            // write pre-init user code here
            String str = getRecordValue(75);
            textField65 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_15"), str, 4, TextField.NUMERIC);//GEN-LINE:|956-getter|1|956-postInit
            // write post-init user code here
        }//GEN-BEGIN:|956-getter|2|
        return textField65;
    }
//</editor-fold>//GEN-END:|956-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField66 ">//GEN-BEGIN:|957-getter|0|957-preInit
    /**
     * Returns an initialized instance of textField66 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField66() {
        if (textField66 == null) {//GEN-END:|957-getter|0|957-preInit
            // write pre-init user code here
            String str = getRecordValue(76);
            textField66 = new TextField(LocalizationSupport.getMessage("with_nodehydration_15"), str, 4, TextField.NUMERIC);//GEN-LINE:|957-getter|1|957-postInit
            // write post-init user code here
        }//GEN-BEGIN:|957-getter|2|
        return textField66;
    }
//</editor-fold>//GEN-END:|957-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField67 ">//GEN-BEGIN:|958-getter|0|958-preInit
    /**
     * Returns an initialized instance of textField67 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField67() {
        if (textField67 == null) {//GEN-END:|958-getter|0|958-preInit
            // write pre-init user code here
            String str = getRecordValue(77);
            textField67 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_15"), str, 4, TextField.NUMERIC);//GEN-LINE:|958-getter|1|958-postInit
            // write post-init user code here
        }//GEN-BEGIN:|958-getter|2|
        return textField67;
    }
//</editor-fold>//GEN-END:|958-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField68 ">//GEN-BEGIN:|959-getter|0|959-preInit
    /**
     * Returns an initialized instance of textField68 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField68() {
        if (textField68 == null) {//GEN-END:|959-getter|0|959-preInit
            // write pre-init user code here
            String str = getRecordValue(78);
            textField68 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_16"), str, 4, TextField.NUMERIC);//GEN-LINE:|959-getter|1|959-postInit
            // write post-init user code here
        }//GEN-BEGIN:|959-getter|2|
        return textField68;
    }
//</editor-fold>//GEN-END:|959-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField69 ">//GEN-BEGIN:|960-getter|0|960-preInit
    /**
     * Returns an initialized instance of textField69 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField69() {
        if (textField69 == null) {//GEN-END:|960-getter|0|960-preInit
            // write pre-init user code here
            String str = getRecordValue(79);
            textField69 = new TextField(LocalizationSupport.getMessage("with_nodehydration_16"), str, 4, TextField.NUMERIC);//GEN-LINE:|960-getter|1|960-postInit
            // write post-init user code here
        }//GEN-BEGIN:|960-getter|2|
        return textField69;
    }
//</editor-fold>//GEN-END:|960-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField70 ">//GEN-BEGIN:|961-getter|0|961-preInit
    /**
     * Returns an initialized instance of textField70 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField70() {
        if (textField70 == null) {//GEN-END:|961-getter|0|961-preInit
            // write pre-init user code here
            String str = getRecordValue(80);
            textField70 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_16"), str, 4, TextField.NUMERIC);//GEN-LINE:|961-getter|1|961-postInit
            // write post-init user code here
        }//GEN-BEGIN:|961-getter|2|
        return textField70;
    }
//</editor-fold>//GEN-END:|961-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField71 ">//GEN-BEGIN:|962-getter|0|962-preInit
    /**
     * Returns an initialized instance of textField71 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField71() {
        if (textField71 == null) {//GEN-END:|962-getter|0|962-preInit
            // write pre-init user code here
            String str = getRecordValue(81);
            textField71 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_17"), str, 4, TextField.NUMERIC);//GEN-LINE:|962-getter|1|962-postInit
            // write post-init user code here
        }//GEN-BEGIN:|962-getter|2|
        return textField71;
    }
//</editor-fold>//GEN-END:|962-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField72 ">//GEN-BEGIN:|963-getter|0|963-preInit
    /**
     * Returns an initialized instance of textField72 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField72() {
        if (textField72 == null) {//GEN-END:|963-getter|0|963-preInit
            // write pre-init user code here
            String str = getRecordValue(82);
            textField72 = new TextField(LocalizationSupport.getMessage("with_nodehydration_17"), str, 4, TextField.NUMERIC);//GEN-LINE:|963-getter|1|963-postInit
            // write post-init user code here
        }//GEN-BEGIN:|963-getter|2|
        return textField72;
    }
//</editor-fold>//GEN-END:|963-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField73 ">//GEN-BEGIN:|964-getter|0|964-preInit
    /**
     * Returns an initialized instance of textField73 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField73() {
        if (textField73 == null) {//GEN-END:|964-getter|0|964-preInit
            // write pre-init user code here
            String str = getRecordValue(83);
            textField73 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_17"), str, 4, TextField.NUMERIC);//GEN-LINE:|964-getter|1|964-postInit
            // write post-init user code here
        }//GEN-BEGIN:|964-getter|2|
        return textField73;
    }
//</editor-fold>//GEN-END:|964-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField74 ">//GEN-BEGIN:|965-getter|0|965-preInit
    /**
     * Returns an initialized instance of textField74 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField74() {
        if (textField74 == null) {//GEN-END:|965-getter|0|965-preInit
            // write pre-init user code here
            String str = getRecordValue(84);
            textField74 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_18"), str, 4, TextField.NUMERIC);//GEN-LINE:|965-getter|1|965-postInit
            // write post-init user code here
        }//GEN-BEGIN:|965-getter|2|
        return textField74;
    }
//</editor-fold>//GEN-END:|965-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField75 ">//GEN-BEGIN:|966-getter|0|966-preInit
    /**
     * Returns an initialized instance of textField75 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField75() {
        if (textField75 == null) {//GEN-END:|966-getter|0|966-preInit
            // write pre-init user code here
            String str = getRecordValue(85);
            textField75 = new TextField(LocalizationSupport.getMessage("with_nodehydration_18"), str, 4, TextField.NUMERIC);//GEN-LINE:|966-getter|1|966-postInit
            // write post-init user code here
        }//GEN-BEGIN:|966-getter|2|
        return textField75;
    }
//</editor-fold>//GEN-END:|966-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField76 ">//GEN-BEGIN:|967-getter|0|967-preInit
    /**
     * Returns an initialized instance of textField76 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField76() {
        if (textField76 == null) {//GEN-END:|967-getter|0|967-preInit
            // write pre-init user code here
            String str = getRecordValue(86);
            textField76 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_18"), str, 4, TextField.NUMERIC);//GEN-LINE:|967-getter|1|967-postInit
            // write post-init user code here
        }//GEN-BEGIN:|967-getter|2|
        return textField76;
    }
//</editor-fold>//GEN-END:|967-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField77 ">//GEN-BEGIN:|968-getter|0|968-preInit
    /**
     * Returns an initialized instance of textField77 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField77() {
        if (textField77 == null) {//GEN-END:|968-getter|0|968-preInit
            // write pre-init user code here
            String str = getRecordValue(87);
            textField77 = new TextField(LocalizationSupport.getMessage("with_somemuchdehydration_19"), str, 4, TextField.NUMERIC);//GEN-LINE:|968-getter|1|968-postInit
            // write post-init user code here
        }//GEN-BEGIN:|968-getter|2|
        return textField77;
    }
//</editor-fold>//GEN-END:|968-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField78 ">//GEN-BEGIN:|969-getter|0|969-preInit
    /**
     * Returns an initialized instance of textField78 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField78() {
        if (textField78 == null) {//GEN-END:|969-getter|0|969-preInit
            // write pre-init user code here
            String str = getRecordValue(88);
            textField78 = new TextField(LocalizationSupport.getMessage("with_nodehydration_19"), str, 4, TextField.NUMERIC);//GEN-LINE:|969-getter|1|969-postInit
            // write post-init user code here
        }//GEN-BEGIN:|969-getter|2|
        return textField78;
    }
//</editor-fold>//GEN-END:|969-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField79 ">//GEN-BEGIN:|970-getter|0|970-preInit
    /**
     * Returns an initialized instance of textField79 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField79() {
        if (textField79 == null) {//GEN-END:|970-getter|0|970-preInit
            // write pre-init user code here
            String str = getRecordValue(89);
            textField79 = new TextField(LocalizationSupport.getMessage("with_bloodinstool_19"), str, 4, TextField.NUMERIC);//GEN-LINE:|970-getter|1|970-postInit
            // write post-init user code here
        }//GEN-BEGIN:|970-getter|2|
        return textField79;
    }
//</editor-fold>//GEN-END:|970-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem3 ">//GEN-BEGIN:|971-getter|0|971-preInit
    /**
     * Returns an initialized instance of stringItem3 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem3() {
        if (stringItem3 == null) {//GEN-END:|971-getter|0|971-preInit
            // write pre-init user code here
            stringItem3 = new StringItem("Cases of acute jaundice", null);//GEN-LINE:|971-getter|1|971-postInit
            // write post-init user code here
        }//GEN-BEGIN:|971-getter|2|
        return stringItem3;
    }
//</editor-fold>//GEN-END:|971-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField80 ">//GEN-BEGIN:|972-getter|0|972-preInit
    /**
     * Returns an initialized instance of textField80 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField80() {
        if (textField80 == null) {//GEN-END:|972-getter|0|972-preInit
            // write pre-init user code here
            String str = getRecordValue(90);
            textField80 = new TextField(LocalizationSupport.getMessage("male_cases<5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|972-getter|1|972-postInit
            // write post-init user code here
        }//GEN-BEGIN:|972-getter|2|
        return textField80;
    }
//</editor-fold>//GEN-END:|972-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField81 ">//GEN-BEGIN:|973-getter|0|973-preInit
    /**
     * Returns an initialized instance of textField81 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField81() {
        if (textField81 == null) {//GEN-END:|973-getter|0|973-preInit
            // write pre-init user code here
            String str = getRecordValue(91);
            textField81 = new TextField(LocalizationSupport.getMessage("female_<5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|973-getter|1|973-postInit
            // write post-init user code here
        }//GEN-BEGIN:|973-getter|2|
        return textField81;
    }
//</editor-fold>//GEN-END:|973-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField82 ">//GEN-BEGIN:|974-getter|0|974-preInit
    /**
     * Returns an initialized instance of textField82 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField82() {
        if (textField82 == null) {//GEN-END:|974-getter|0|974-preInit
            // write pre-init user code here
            String str = getRecordValue(92);
            textField82 = new TextField(LocalizationSupport.getMessage("male_cases>5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|974-getter|1|974-postInit
            // write post-init user code here
        }//GEN-BEGIN:|974-getter|2|
        return textField82;
    }
//</editor-fold>//GEN-END:|974-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField83 ">//GEN-BEGIN:|975-getter|0|975-preInit
    /**
     * Returns an initialized instance of textField83 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField83() {
        if (textField83 == null) {//GEN-END:|975-getter|0|975-preInit
            // write pre-init user code here
            String str = getRecordValue(93);
            textField83 = new TextField(LocalizationSupport.getMessage("female_cases>5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|975-getter|1|975-postInit
            // write post-init user code here
        }//GEN-BEGIN:|975-getter|2|
        return textField83;
    }
//</editor-fold>//GEN-END:|975-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField84 ">//GEN-BEGIN:|976-getter|0|976-preInit
    /**
     * Returns an initialized instance of textField84 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField84() {
        if (textField84 == null) {//GEN-END:|976-getter|0|976-preInit
            // write pre-init user code here
            String str = getRecordValue(94);
            textField84 = new TextField(LocalizationSupport.getMessage("male_deaths<5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|976-getter|1|976-postInit
            // write post-init user code here
        }//GEN-BEGIN:|976-getter|2|
        return textField84;
    }
//</editor-fold>//GEN-END:|976-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField85 ">//GEN-BEGIN:|977-getter|0|977-preInit
    /**
     * Returns an initialized instance of textField85 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField85() {
        if (textField85 == null) {//GEN-END:|977-getter|0|977-preInit
            // write pre-init user code here
            String str = getRecordValue(95);
            textField85 = new TextField(LocalizationSupport.getMessage("female_deaths<5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|977-getter|1|977-postInit
            // write post-init user code here
        }//GEN-BEGIN:|977-getter|2|
        return textField85;
    }
//</editor-fold>//GEN-END:|977-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField86 ">//GEN-BEGIN:|978-getter|0|978-preInit
    /**
     * Returns an initialized instance of textField86 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField86() {
        if (textField86 == null) {//GEN-END:|978-getter|0|978-preInit
            // write pre-init user code here
            String str = getRecordValue(96);
            textField86 = new TextField(LocalizationSupport.getMessage("male_deaths>5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|978-getter|1|978-postInit
            // write post-init user code here
        }//GEN-BEGIN:|978-getter|2|
        return textField86;
    }
//</editor-fold>//GEN-END:|978-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField87 ">//GEN-BEGIN:|979-getter|0|979-preInit
    /**
     * Returns an initialized instance of textField87 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField87() {
        if (textField87 == null) {//GEN-END:|979-getter|0|979-preInit
            // write pre-init user code here
            String str = getRecordValue(97);
            textField87 = new TextField(LocalizationSupport.getMessage("female_deaths>5yrs_20"), str, 4, TextField.NUMERIC);//GEN-LINE:|979-getter|1|979-postInit
            // write post-init user code here
        }//GEN-BEGIN:|979-getter|2|
        return textField87;
    }
//</editor-fold>//GEN-END:|979-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem5 ">//GEN-BEGIN:|980-getter|0|980-preInit
    /**
     * Returns an initialized instance of stringItem5 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem5() {
        if (stringItem5 == null) {//GEN-END:|980-getter|0|980-preInit
            // write pre-init user code here
            stringItem5 = new StringItem("Cases of acute flacid paralysis", null);//GEN-LINE:|980-getter|1|980-postInit
            // write post-init user code here
        }//GEN-BEGIN:|980-getter|2|
        return stringItem5;
    }
//</editor-fold>//GEN-END:|980-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField88 ">//GEN-BEGIN:|981-getter|0|981-preInit
    /**
     * Returns an initialized instance of textField88 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField88() {
        if (textField88 == null) {//GEN-END:|981-getter|0|981-preInit
            // write pre-init user code here
            String str = getRecordValue(98);
            textField88 = new TextField(LocalizationSupport.getMessage("male_cases<5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|981-getter|1|981-postInit
            // write post-init user code here
        }//GEN-BEGIN:|981-getter|2|
        return textField88;
    }
//</editor-fold>//GEN-END:|981-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField89 ">//GEN-BEGIN:|982-getter|0|982-preInit
    /**
     * Returns an initialized instance of textField89 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField89() {
        if (textField89 == null) {//GEN-END:|982-getter|0|982-preInit
            // write pre-init user code here
            String str = getRecordValue(99);
            textField89 = new TextField(LocalizationSupport.getMessage("female<5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|982-getter|1|982-postInit
            // write post-init user code here
        }//GEN-BEGIN:|982-getter|2|
        return textField89;
    }
//</editor-fold>//GEN-END:|982-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField90 ">//GEN-BEGIN:|983-getter|0|983-preInit
    /**
     * Returns an initialized instance of textField90 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField90() {
        if (textField90 == null) {//GEN-END:|983-getter|0|983-preInit
            // write pre-init user code here
            String str = getRecordValue(100);
            textField90 = new TextField(LocalizationSupport.getMessage("male_cases>5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|983-getter|1|983-postInit
            // write post-init user code here
        }//GEN-BEGIN:|983-getter|2|
        return textField90;
    }
//</editor-fold>//GEN-END:|983-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField91 ">//GEN-BEGIN:|984-getter|0|984-preInit
    /**
     * Returns an initialized instance of textField91 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField91() {
        if (textField91 == null) {//GEN-END:|984-getter|0|984-preInit
            // write pre-init user code here
            String str = getRecordValue(101);
            textField91 = new TextField(LocalizationSupport.getMessage("female_cases>5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|984-getter|1|984-postInit
            // write post-init user code here
        }//GEN-BEGIN:|984-getter|2|
        return textField91;
    }
//</editor-fold>//GEN-END:|984-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField92 ">//GEN-BEGIN:|985-getter|0|985-preInit
    /**
     * Returns an initialized instance of textField92 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField92() {
        if (textField92 == null) {//GEN-END:|985-getter|0|985-preInit
            // write pre-init user code here
            String str = getRecordValue(102);
            textField92 = new TextField(LocalizationSupport.getMessage("male_deaths<5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|985-getter|1|985-postInit
            // write post-init user code here
        }//GEN-BEGIN:|985-getter|2|
        return textField92;
    }
//</editor-fold>//GEN-END:|985-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField93 ">//GEN-BEGIN:|986-getter|0|986-preInit
    /**
     * Returns an initialized instance of textField93 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField93() {
        if (textField93 == null) {//GEN-END:|986-getter|0|986-preInit
            // write pre-init user code here
            String str = getRecordValue(103);
            textField93 = new TextField(LocalizationSupport.getMessage("female_deaths<5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|986-getter|1|986-postInit
            // write post-init user code here
        }//GEN-BEGIN:|986-getter|2|
        return textField93;
    }
//</editor-fold>//GEN-END:|986-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField94 ">//GEN-BEGIN:|987-getter|0|987-preInit
    /**
     * Returns an initialized instance of textField94 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField94() {
        if (textField94 == null) {//GEN-END:|987-getter|0|987-preInit
            // write pre-init user code here
            String str = getRecordValue(104);
            textField94 = new TextField(LocalizationSupport.getMessage("male_deaths>5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|987-getter|1|987-postInit
            // write post-init user code here
        }//GEN-BEGIN:|987-getter|2|
        return textField94;
    }
//</editor-fold>//GEN-END:|987-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField95 ">//GEN-BEGIN:|988-getter|0|988-preInit
    /**
     * Returns an initialized instance of textField95 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField95() {
        if (textField95 == null) {//GEN-END:|988-getter|0|988-preInit
            // write pre-init user code here
            String str = getRecordValue(105);
            textField95 = new TextField(LocalizationSupport.getMessage("female_deaths>5yrs_21"), str, 4, TextField.NUMERIC);//GEN-LINE:|988-getter|1|988-postInit
            // write post-init user code here
        }//GEN-BEGIN:|988-getter|2|
        return textField95;
    }
//</editor-fold>//GEN-END:|988-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField96 ">//GEN-BEGIN:|989-getter|0|989-preInit
    /**
     * Returns an initialized instance of textField96 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField96() {
        if (textField96 == null) {//GEN-END:|989-getter|0|989-preInit
            // write pre-init user code here
            String str = getRecordValue(106);
            textField96 = new TextField(LocalizationSupport.getMessage("male_cases<5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|989-getter|1|989-postInit
            // write post-init user code here
        }//GEN-BEGIN:|989-getter|2|
        return textField96;
    }
//</editor-fold>//GEN-END:|989-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField97 ">//GEN-BEGIN:|990-getter|0|990-preInit
    /**
     * Returns an initialized instance of textField97 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField97() {
        if (textField97 == null) {//GEN-END:|990-getter|0|990-preInit
            // write pre-init user code here
            String str = getRecordValue(107);
            textField97 = new TextField(LocalizationSupport.getMessage("female_<5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|990-getter|1|990-postInit
            // write post-init user code here
        }//GEN-BEGIN:|990-getter|2|
        return textField97;
    }
//</editor-fold>//GEN-END:|990-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField98 ">//GEN-BEGIN:|991-getter|0|991-preInit
    /**
     * Returns an initialized instance of textField98 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField98() {
        if (textField98 == null) {//GEN-END:|991-getter|0|991-preInit
            // write pre-init user code here
            String str = getRecordValue(108);
            textField98 = new TextField(LocalizationSupport.getMessage("male_cases>5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|991-getter|1|991-postInit
            // write post-init user code here
        }//GEN-BEGIN:|991-getter|2|
        return textField98;
    }
//</editor-fold>//GEN-END:|991-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField99 ">//GEN-BEGIN:|992-getter|0|992-preInit
    /**
     * Returns an initialized instance of textField99 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField99() {
        if (textField99 == null) {//GEN-END:|992-getter|0|992-preInit
            // write pre-init user code here
            String str = getRecordValue(109);
            textField99 = new TextField(LocalizationSupport.getMessage("female_cases>5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|992-getter|1|992-postInit
            // write post-init user code here
        }//GEN-BEGIN:|992-getter|2|
        return textField99;
    }
//</editor-fold>//GEN-END:|992-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField100 ">//GEN-BEGIN:|993-getter|0|993-preInit
    /**
     * Returns an initialized instance of textField100 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField100() {
        if (textField100 == null) {//GEN-END:|993-getter|0|993-preInit
            // write pre-init user code here
            String str = getRecordValue(110);
            textField100 = new TextField(LocalizationSupport.getMessage("male_deaths<5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|993-getter|1|993-postInit
            // write post-init user code here
        }//GEN-BEGIN:|993-getter|2|
        return textField100;
    }
//</editor-fold>//GEN-END:|993-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField101 ">//GEN-BEGIN:|994-getter|0|994-preInit
    /**
     * Returns an initialized instance of textField101 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField101() {
        if (textField101 == null) {//GEN-END:|994-getter|0|994-preInit
            // write pre-init user code here
            String str = getRecordValue(111);
            textField101 = new TextField(LocalizationSupport.getMessage("female_deaths<5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|994-getter|1|994-postInit
            // write post-init user code here
        }//GEN-BEGIN:|994-getter|2|
        return textField101;
    }
//</editor-fold>//GEN-END:|994-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField102 ">//GEN-BEGIN:|995-getter|0|995-preInit
    /**
     * Returns an initialized instance of textField102 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField102() {
        if (textField102 == null) {//GEN-END:|995-getter|0|995-preInit
            // write pre-init user code here
            String str = getRecordValue(112);
            textField102 = new TextField(LocalizationSupport.getMessage("male_deaths>5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|995-getter|1|995-postInit
            // write post-init user code here
        }//GEN-BEGIN:|995-getter|2|
        return textField102;
    }
//</editor-fold>//GEN-END:|995-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField103 ">//GEN-BEGIN:|996-getter|0|996-preInit
    /**
     * Returns an initialized instance of textField103 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField103() {
        if (textField103 == null) {//GEN-END:|996-getter|0|996-preInit
            // write pre-init user code here
            String str = getRecordValue(113);
            textField103 = new TextField(LocalizationSupport.getMessage("female_deaths>5yrs_22"), str, 4, TextField.NUMERIC);//GEN-LINE:|996-getter|1|996-postInit
            // write post-init user code here
        }//GEN-BEGIN:|996-getter|2|
        return textField103;
    }
//</editor-fold>//GEN-END:|996-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: yearChoiceGroup ">//GEN-BEGIN:|1008-getter|0|1008-preInit
    /**
     * Returns an initialized instance of yearChoiceGroup component.
     *
     * @return the initialized component instance
     */
    public ChoiceGroup getYearChoiceGroup() {
        if (yearChoiceGroup == null) {//GEN-END:|1008-getter|0|1008-preInit
            // write pre-init user code here
            yearChoiceGroup = new ChoiceGroup(LocalizationSupport.getMessage("yearChoiceLabel"), Choice.POPUP);//GEN-BEGIN:|1008-getter|1|1008-postInit
            yearChoiceGroup.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_2);//GEN-END:|1008-getter|1|1008-postInit
            // write post-init user code here
            
            // Get current year
            Calendar cal = Calendar.getInstance();
            for (int i = -5; i < 6 ; i++ )
            {
                int year  = cal.get(Calendar.YEAR);
                yearChoiceGroup.append(""+ (year + i), null);
            }
            try{
                    if (editingLastReport) {
                        if (lastMsgStore.getRecord(7) != null) {
                            int index = 0;
                            for(index = 0; index < yearChoiceGroup.size(); index++)
                            {       
                                if(yearChoiceGroup.getString(index).equalsIgnoreCase(new String(lastMsgStore.getRecord(7)))){
                                    break;
                                }
                            }
                            if (index == 11)
                                index = 10;
                            yearChoiceGroup.setSelectedIndex(index, true); // need the last report value
                        }
                    } else {
                        int index = 5;
                        yearChoiceGroup.setSelectedIndex(index, true); // If not need the value of the last report
                    }
            }catch(RecordStoreException rsex) {
                    rsex.printStackTrace();
            }
        }//GEN-BEGIN:|1008-getter|2|
        return yearChoiceGroup;
    }
//</editor-fold>//GEN-END:|1008-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentrecode ">//GEN-BEGIN:|1010-getter|0|1010-preInit
    /**
     * Returns an initialized instance of subcentrecode component.
     *
     * @return the initialized component instance
     */
    public Form getSubcentrecode() {
        if (subcentrecode == null) {//GEN-END:|1010-getter|0|1010-preInit
            // write pre-init user code here
            subcentrecode = new Form("Facility Code", new Item[]{getSubCentreCode()});//GEN-BEGIN:|1010-getter|1|1010-postInit
            subcentrecode.addCommand(getSubcentrecodeCmd());
            subcentrecode.addCommand(getSubcentreExistCmd());
            subcentrecode.setCommandListener(this);//GEN-END:|1010-getter|1|1010-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1010-getter|2|
        return subcentrecode;
    }
//</editor-fold>//GEN-END:|1010-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subCentreCode ">//GEN-BEGIN:|1022-getter|0|1022-preInit
    /**
     * Returns an initialized instance of subCentreCode component.
     *
     * @return the initialized component instance
     */
    public TextField getSubCentreCode() {
        if (subCentreCode == null) {//GEN-END:|1022-getter|0|1022-preInit
            // write pre-init user code here
            String str = "";
            if (editingLastReport) {
                try {
                    byte[] myValue = lastMsgStore.getRecord(6);
                    //fixed the problem that the textField was defined "numeric" still accept "-" character
                    if (myValue != null) {
                        str = new String(myValue);
                        if(!isIntNumber(str)){str = "";} // Check if the value is not a integer
                    }

                } catch (RecordStoreException rsex) {
                    rsex.printStackTrace();
                }
            }
            subCentreCode = new TextField(LocalizationSupport.getMessage("subCentreCode"), str, 4, TextField.NUMERIC);//GEN-LINE:|1022-getter|1|1022-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1022-getter|2|
        return subCentreCode;
    }
//</editor-fold>//GEN-END:|1022-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentrecodeCmd ">//GEN-BEGIN:|1011-getter|0|1011-preInit
    /**
     * Returns an initialized instance of subcentrecodeCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSubcentrecodeCmd() {
        if (subcentrecodeCmd == null) {//GEN-END:|1011-getter|0|1011-preInit
            // write pre-init user code here
            subcentrecodeCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|1011-getter|1|1011-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1011-getter|2|
        return subcentrecodeCmd;
    }
//</editor-fold>//GEN-END:|1011-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentrecodeBackCmd ">//GEN-BEGIN:|1013-getter|0|1013-preInit
    /**
     * Returns an initialized instance of subcentrecodeBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSubcentrecodeBackCmd() {
        if (subcentrecodeBackCmd == null) {//GEN-END:|1013-getter|0|1013-preInit
            // write pre-init user code here
            subcentrecodeBackCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|1013-getter|1|1013-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1013-getter|2|
        return subcentrecodeBackCmd;
    }
//</editor-fold>//GEN-END:|1013-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthBackCmd ">//GEN-BEGIN:|1016-getter|0|1016-preInit
    /**
     * Returns an initialized instance of monthBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMonthBackCmd() {
        if (monthBackCmd == null) {//GEN-END:|1016-getter|0|1016-preInit
            // write pre-init user code here
            monthBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|1016-getter|1|1016-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1016-getter|2|
        return monthBackCmd;
    }
//</editor-fold>//GEN-END:|1016-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: subcentreExistCmd ">//GEN-BEGIN:|1019-getter|0|1019-preInit
    /**
     * Returns an initialized instance of subcentreExistCmd component.
     *
     * @return the initialized component instance
     */
    public Command getSubcentreExistCmd() {
        if (subcentreExistCmd == null) {//GEN-END:|1019-getter|0|1019-preInit
            // write pre-init user code here
            subcentreExistCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|1019-getter|1|1019-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1019-getter|2|
        return subcentreExistCmd;
    }
//</editor-fold>//GEN-END:|1019-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem6 ">//GEN-BEGIN:|1023-getter|0|1023-preInit
    /**
     * Returns an initialized instance of stringItem6 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem6() {
        if (stringItem6 == null) {//GEN-END:|1023-getter|0|1023-preInit
            // write pre-init user code here
            stringItem6 = new StringItem("Female cases<5 yrs", null);//GEN-LINE:|1023-getter|1|1023-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1023-getter|2|
        return stringItem6;
    }
//</editor-fold>//GEN-END:|1023-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem10 ">//GEN-BEGIN:|1028-getter|0|1028-preInit
    /**
     * Returns an initialized instance of stringItem10 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem10() {
        if (stringItem10 == null) {//GEN-END:|1028-getter|0|1028-preInit
            // write pre-init user code here
            stringItem10 = new StringItem("Male deaths > 5 yrs", null);//GEN-LINE:|1028-getter|1|1028-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1028-getter|2|
        return stringItem10;
    }
//</editor-fold>//GEN-END:|1028-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem9 ">//GEN-BEGIN:|1027-getter|0|1027-preInit
    /**
     * Returns an initialized instance of stringItem9 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem9() {
        if (stringItem9 == null) {//GEN-END:|1027-getter|0|1027-preInit
            // write pre-init user code here
            stringItem9 = new StringItem("Female deaths <5 yrs", null);//GEN-LINE:|1027-getter|1|1027-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1027-getter|2|
        return stringItem9;
    }
//</editor-fold>//GEN-END:|1027-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem2 ">//GEN-BEGIN:|1024-getter|0|1024-preInit
    /**
     * Returns an initialized instance of stringItem2 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem2() {
        if (stringItem2 == null) {//GEN-END:|1024-getter|0|1024-preInit
            // write pre-init user code here
            stringItem2 = new StringItem("Male cases>5 yrs", null);//GEN-LINE:|1024-getter|1|1024-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1024-getter|2|
        return stringItem2;
    }
//</editor-fold>//GEN-END:|1024-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem8 ">//GEN-BEGIN:|1026-getter|0|1026-preInit
    /**
     * Returns an initialized instance of stringItem8 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem8() {
        if (stringItem8 == null) {//GEN-END:|1026-getter|0|1026-preInit
            // write pre-init user code here
            stringItem8 = new StringItem("Male deaths<5 yrs", null);//GEN-LINE:|1026-getter|1|1026-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1026-getter|2|
        return stringItem8;
    }
//</editor-fold>//GEN-END:|1026-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem7 ">//GEN-BEGIN:|1025-getter|0|1025-preInit
    /**
     * Returns an initialized instance of stringItem7 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem7() {
        if (stringItem7 == null) {//GEN-END:|1025-getter|0|1025-preInit
            // write pre-init user code here
            stringItem7 = new StringItem("Female cases > 5 yrs", null);//GEN-LINE:|1025-getter|1|1025-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1025-getter|2|
        return stringItem7;
    }
//</editor-fold>//GEN-END:|1025-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem12 ">//GEN-BEGIN:|1030-getter|0|1030-preInit
    /**
     * Returns an initialized instance of stringItem12 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem12() {
        if (stringItem12 == null) {//GEN-END:|1030-getter|0|1030-preInit
            // write pre-init user code here
            stringItem12 = new StringItem("More than 3 weeks", null);//GEN-LINE:|1030-getter|1|1030-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1030-getter|2|
        return stringItem12;
    }
//</editor-fold>//GEN-END:|1030-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem19 ">//GEN-BEGIN:|1037-getter|0|1037-preInit
    /**
     * Returns an initialized instance of stringItem19 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem19() {
        if (stringItem19 == null) {//GEN-END:|1037-getter|0|1037-preInit
            // write pre-init user code here
            stringItem19 = new StringItem("Female cases > 5 yrs", null);//GEN-LINE:|1037-getter|1|1037-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1037-getter|2|
        return stringItem19;
    }
//</editor-fold>//GEN-END:|1037-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem16 ">//GEN-BEGIN:|1034-getter|0|1034-preInit
    /**
     * Returns an initialized instance of stringItem16 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem16() {
        if (stringItem16 == null) {//GEN-END:|1034-getter|0|1034-preInit
            // write pre-init user code here
            stringItem16 = new StringItem("Male deaths < 5 yrs", null);//GEN-LINE:|1034-getter|1|1034-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1034-getter|2|
        return stringItem16;
    }
//</editor-fold>//GEN-END:|1034-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem17 ">//GEN-BEGIN:|1035-getter|0|1035-preInit
    /**
     * Returns an initialized instance of stringItem17 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem17() {
        if (stringItem17 == null) {//GEN-END:|1035-getter|0|1035-preInit
            // write pre-init user code here
            stringItem17 = new StringItem("Female deaths <  5 yrs", null);//GEN-LINE:|1035-getter|1|1035-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1035-getter|2|
        return stringItem17;
    }
//</editor-fold>//GEN-END:|1035-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem13 ">//GEN-BEGIN:|1031-getter|0|1031-preInit
    /**
     * Returns an initialized instance of stringItem13 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem13() {
        if (stringItem13 == null) {//GEN-END:|1031-getter|0|1031-preInit
            // write pre-init user code here
            stringItem13 = new StringItem("Female cases < 5 yrs", null);//GEN-LINE:|1031-getter|1|1031-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1031-getter|2|
        return stringItem13;
    }
//</editor-fold>//GEN-END:|1031-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem14 ">//GEN-BEGIN:|1032-getter|0|1032-preInit
    /**
     * Returns an initialized instance of stringItem14 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem14() {
        if (stringItem14 == null) {//GEN-END:|1032-getter|0|1032-preInit
            // write pre-init user code here
            stringItem14 = new StringItem("Male cases > 5 yrs", null);//GEN-LINE:|1032-getter|1|1032-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1032-getter|2|
        return stringItem14;
    }
//</editor-fold>//GEN-END:|1032-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem11 ">//GEN-BEGIN:|1029-getter|0|1029-preInit
    /**
     * Returns an initialized instance of stringItem11 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem11() {
        if (stringItem11 == null) {//GEN-END:|1029-getter|0|1029-preInit
            // write pre-init user code here
            stringItem11 = new StringItem("Female deaths > 5 yrs", null);//GEN-LINE:|1029-getter|1|1029-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1029-getter|2|
        return stringItem11;
    }
//</editor-fold>//GEN-END:|1029-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem15 ">//GEN-BEGIN:|1033-getter|0|1033-preInit
    /**
     * Returns an initialized instance of stringItem15 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem15() {
        if (stringItem15 == null) {//GEN-END:|1033-getter|0|1033-preInit
            // write pre-init user code here
            stringItem15 = new StringItem("Female deaths > 5 yrs", null);//GEN-LINE:|1033-getter|1|1033-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1033-getter|2|
        return stringItem15;
    }
//</editor-fold>//GEN-END:|1033-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem18 ">//GEN-BEGIN:|1036-getter|0|1036-preInit
    /**
     * Returns an initialized instance of stringItem18 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem18() {
        if (stringItem18 == null) {//GEN-END:|1036-getter|0|1036-preInit
            // write pre-init user code here
            stringItem18 = new StringItem("Male deaths  > 5 yrs", null);//GEN-LINE:|1036-getter|1|1036-postInit
            // write post-init user code here
        }//GEN-BEGIN:|1036-getter|2|
        return stringItem18;
    }
//</editor-fold>//GEN-END:|1036-getter|2|

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
                textField14.getString() + ":" +
                textField15.getString() + ":" +
                    textField13.getString() + ":" +
                    textField16.getString() + ":" +
                    textField17.getString() + ":" +
                    textField18.getString() + ":" +
                    textField19.getString() + ":" +
                textField28.getString() + ":" +
                textField29.getString() + ":" +
                textField30.getString() + ":" +
                textField31.getString() + ":" +
                    textField20.getString() + ":" +
                    textField21.getString() + ":" +
                    textField22.getString() + ":" +
                    textField23.getString() + ":" +
                    textField24.getString() + ":" +
                    textField25.getString() + ":" +
                    textField26.getString() + ":" +
                    textField27.getString() + ":" +

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
                    textField46.getString() + ":" +
                    textField47.getString() + ":" +
                    textField48.getString() + ":" +
                    textField49.getString() + ":" +
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
                    textField75.getString() + ":" +
                    textField76.getString() + ":" +
                    textField77.getString() + ":" +
                    textField78.getString() + ":" +
                    textField79.getString() + ":" +
                    textField80.getString() + ":" +
                    textField81.getString() + ":" +
                    textField82.getString() + ":" +
                    textField83.getString() + ":" +
                    textField84.getString() + ":" +
                    textField85.getString() + ":" +
                    textField86.getString() + ":" +
                    textField87.getString() + ":" +
                    textField88.getString() + ":" +
                    textField89.getString() + ":" +
                    textField90.getString() + ":" +
                    textField91.getString() + ":" +
                    textField92.getString() + ":" +
                    textField93.getString() + ":" +
                    textField94.getString() + ":" +
                    textField95.getString() + ":" +
                    textField96.getString() + ":" +
                    textField97.getString() + ":" +
                    textField98.getString() + ":" +
                    textField99.getString() + ":" +
                    textField100.getString() + ":" +
                    textField101.getString() + ":" +
                    textField102.getString() + ":" +
                    textField103.getString();
        return myData;
    }
    private String collectFormData(String monthStr, String freqStr) {

        String monthData = "";
        String year = freqStr;
        String week = monthStr;
        if(week.length() == 1)
            week = "0"+ week;
        
        monthData = year.substring(2) + week;

        
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
            if(Integer.parseInt(myDataTemp[seperateIndex]) != 0){
                myData = myData + myDataTemp[seperateIndex] + ":";
            }
            else{
                if(seperateIndex != 0)
                    myData = myData + ":";
            }
            seperateIndex++;
        }
        if(Integer.parseInt(myDataTemp[myDataTemp.length - 1]) != 0){
                myData = myData + myDataTemp[myDataTemp.length - 1];
        }
        // End remove 0 data
        
        
        // create myReturnData to content the last data to return
        String myReturnData = "";
        
        int characters = 0;  // THE CHARACTERS OF DATA on one SMS
        seperateIndex = 0; // THE INDEX OF DATA IN STRING [] myDataTemp blow
        
        formID = 53;
        String periodType = "3";
        String subCenter = subCentreCode.getString();
        
        // If the message too long, seperate to 2 message
        // lengh of "HP NRHM "+ + subCenter + formID + "*" + monthData + "$" is 20 characters
        if(myData.length() > 140 - 40)
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
                // if characters <= 137 - 40. BECAUSE seperateIndex + "!" CONTENT 3 CHARACTERS
                while(characters < 138 - 40){
                if(myData.length() + myDataTemp[seperateIndex].length() < 138 - 40){
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
        } // end if myData.leght > 144
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
                    lastMsgStore.setRecord(6, subCentreCode.getString().getBytes(), 0, subCentreCode.getString().length()); // record for sub centre code
                    
                    //<editor-fold defaultstate="collapsed" desc="update data into RMS ">
                    lastMsgStore.setRecord(11, textField.getString().getBytes(), 0, textField.getString().length());
                    lastMsgStore.setRecord(114, textField1.getString().getBytes(), 0, textField1.getString().length()); // missing textField1 so I add one more record in RMS
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
                    lastMsgStore.setRecord(85, textField75.getString().getBytes(), 0, textField75.getString().length());
                    lastMsgStore.setRecord(86, textField76.getString().getBytes(), 0, textField76.getString().length());
                    lastMsgStore.setRecord(87, textField77.getString().getBytes(), 0, textField77.getString().length());
                    lastMsgStore.setRecord(88, textField78.getString().getBytes(), 0, textField78.getString().length());
                    lastMsgStore.setRecord(89, textField79.getString().getBytes(), 0, textField79.getString().length());
                    lastMsgStore.setRecord(90, textField80.getString().getBytes(), 0, textField80.getString().length());
                    lastMsgStore.setRecord(91, textField81.getString().getBytes(), 0, textField81.getString().length());
                    lastMsgStore.setRecord(92, textField82.getString().getBytes(), 0, textField82.getString().length());
                    lastMsgStore.setRecord(93, textField83.getString().getBytes(), 0, textField83.getString().length());
                    lastMsgStore.setRecord(94, textField84.getString().getBytes(), 0, textField84.getString().length());
                    lastMsgStore.setRecord(95, textField85.getString().getBytes(), 0, textField85.getString().length());
                    lastMsgStore.setRecord(96, textField86.getString().getBytes(), 0, textField86.getString().length());
                    lastMsgStore.setRecord(97, textField87.getString().getBytes(), 0, textField87.getString().length());
                    lastMsgStore.setRecord(98, textField88.getString().getBytes(), 0, textField88.getString().length());
                    lastMsgStore.setRecord(99, textField89.getString().getBytes(), 0, textField89.getString().length());
                    lastMsgStore.setRecord(100, textField90.getString().getBytes(), 0, textField90.getString().length());
                    lastMsgStore.setRecord(101, textField91.getString().getBytes(), 0, textField91.getString().length());
                    lastMsgStore.setRecord(102, textField92.getString().getBytes(), 0, textField92.getString().length());
                    lastMsgStore.setRecord(103, textField93.getString().getBytes(), 0, textField93.getString().length());
                    lastMsgStore.setRecord(104, textField94.getString().getBytes(), 0, textField94.getString().length());
                    lastMsgStore.setRecord(105, textField95.getString().getBytes(), 0, textField95.getString().length());
                    lastMsgStore.setRecord(106, textField96.getString().getBytes(), 0, textField96.getString().length());
                    lastMsgStore.setRecord(107, textField97.getString().getBytes(), 0, textField97.getString().length());
                    lastMsgStore.setRecord(108, textField98.getString().getBytes(), 0, textField98.getString().length());
                    lastMsgStore.setRecord(109, textField99.getString().getBytes(), 0, textField99.getString().length());
                    lastMsgStore.setRecord(110, textField100.getString().getBytes(), 0, textField100.getString().length());
                    lastMsgStore.setRecord(111, textField101.getString().getBytes(), 0, textField101.getString().length());
                    lastMsgStore.setRecord(112, textField102.getString().getBytes(), 0, textField102.getString().length());
                    lastMsgStore.setRecord(113, textField103.getString().getBytes(), 0, textField103.getString().length());
                    
                    
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
        String str = "0";
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
    
    /*
     * Author: Thai Chuong
     * Get the order of the week in a year
     * @param int Year : the current year
     * @Return int : the order of the current week
     * Min value is 1 
     * And the week is WEEK_OF_YEAR
     * 
     * Month start from 0 to 11
     * Date of month start from 1
     * DAY_OF_WEEK start from 1 to 7
     * Follow : http://www.epochconverter.com/date-and-time/weeknumbers-by-year.php?year=2012
     */
     
    private int orderOfCurrentWeek(int Year){
        
        Calendar cal = Calendar.getInstance();
        
        // Current date 
//      int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentDate = cal.get(Calendar.DATE);
        
        // Order of the week
        int order = 0;
        int daysOfMonth [] = {31,29,31,30,31,30,31,31,30,31,30,31};
        if(isEvenYear(Year)){
            // have 29 days on Feb.
        }
        else{
            // have 28 days on Feb.
            daysOfMonth [1] = 28;
        }
        
        // Calculate the order of the week
        int days = 0 ;
        cal.set(Calendar.YEAR, Year);
        
        for (int i = 0; i <currentMonth + 1 ; i++){
            if( i == currentMonth){
                cal.set(Calendar.MONTH, i);
                for(int j = 1 ;j < currentDate + 1 ; j++){
                   cal.set(Calendar.DATE, j);
                   days = cal.get(Calendar.DAY_OF_WEEK);
                   if(days == 2)
                       order++;
                }
            }
            else{
               // Set the month
               cal.set(Calendar.MONTH, i);
               
               for(int j = 1 ;j < daysOfMonth[i] + 1 ; j++){
                   cal.set(Calendar.DATE, j);
                   days = cal.get(Calendar.DAY_OF_WEEK);
                   if(days == 2)
                       order++;
                }
               
            }
        }
        return order - 1; // Because at the end of calculating, we always call order ++
    }


    /*
     * Get first day of the Year
     * @Param : int Year: The year want to get the first day
     * return 1 -> 7 (Sunday ... saturday)
     */
    private int getFirstDay (int Year){
        Calendar cal = Calendar.getInstance();
        // Set Calender to the first day of year
        // Set the year is Year
        cal.set(Calendar.YEAR, Year);
        
        cal.set(Calendar.MONTH, 0);
        
        cal.set(Calendar.DATE, 1);
        
        
        int first_day = cal.get(Calendar.DAY_OF_WEEK);

        
        return first_day;
    }
    
    /*
     * return True if the year is Even Year
     * false if the year is odd year
     * @Param: int Year : The year want to check
     */
    private boolean isEvenYear(int Year){
        return Year%4==0?true:false;
    }    
}
                    
                
