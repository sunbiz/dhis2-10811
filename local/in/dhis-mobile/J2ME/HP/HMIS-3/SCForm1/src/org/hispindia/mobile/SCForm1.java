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
    private Form FollowingImmunization;
    private TextField textField53;
    private TextField textField52;
    private TextField textField57;
    private TextField textField56;
    private TextField textField55;
    private TextField textField54;
    private TextField textField59;
    private TextField textField58;
    private Form HealthFacilityServices;
    private TextField textField71;
    private TextField textField72;
    private Form LabTests;
    private TextField textField73;
    private TextField textField74;
    private Form Immunizationsessions;
    private TextField textField64;
    private TextField textField63;
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
    private Command loadExitCmd;
    private Command loadCmd;
    private Command sendSettingsCmd;
    private Command settingsBackCmd;
    private Command settingsCmd;
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
                for (int i = 0; i < 33; i++) {
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
            }//GEN-BEGIN:|7-commandAction|5|673-preAction
        } else if (displayable == ChildhoodDiseasesreported) {
            if (command == childhoodBackCmd) {//GEN-END:|7-commandAction|5|673-preAction
                // write pre-action user code here
                switchDisplayable(null, getVitaminADose());//GEN-LINE:|7-commandAction|6|673-postAction
                // write post-action user code here
            } else if (command == childhoodCmd) {//GEN-LINE:|7-commandAction|7|675-preAction
                // write pre-action user code here
                switchDisplayable(null, getHealthFacilityServices());//GEN-LINE:|7-commandAction|8|675-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|629-preAction
        } else if (displayable == FollowingImmunization) {
            if (command == followingimmunizationBackCmd) {//GEN-END:|7-commandAction|9|629-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|10|629-postAction
                // write post-action user code here
            } else if (command == followingimmunizationCmd) {//GEN-LINE:|7-commandAction|11|631-preAction
                // write pre-action user code here
                switchDisplayable(null, getAEFI());//GEN-LINE:|7-commandAction|12|631-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|683-preAction
        } else if (displayable == HealthFacilityServices) {
            if (command == healthfacilityservicesBackCmd) {//GEN-END:|7-commandAction|13|683-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhoodDiseasesreported());//GEN-LINE:|7-commandAction|14|683-postAction
                // write post-action user code here
            } else if (command == healthfacilityservicesCmd) {//GEN-LINE:|7-commandAction|15|685-preAction
                // write pre-action user code here
                switchDisplayable(null, getLabTests());//GEN-LINE:|7-commandAction|16|685-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|654-preAction
        } else if (displayable == Immunizationsessions) {
            if (command == immunizationsessionsBackCmd) {//GEN-END:|7-commandAction|17|654-preAction
                // write pre-action user code here
                switchDisplayable(null, getAEFI());//GEN-LINE:|7-commandAction|18|654-postAction
                // write post-action user code here
            } else if (command == immunizationsessionsCmd) {//GEN-LINE:|7-commandAction|19|656-preAction
                // write pre-action user code here
                switchDisplayable(null, getVitaminADose());//GEN-LINE:|7-commandAction|20|656-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|690-preAction
        } else if (displayable == LabTests) {
            if (command == labtestsBackCmd) {//GEN-END:|7-commandAction|21|690-preAction
                // write pre-action user code here
                switchDisplayable(null, getHealthFacilityServices());//GEN-LINE:|7-commandAction|22|690-postAction
                // write post-action user code here
            } else if (command == labtestsCmd) {//GEN-LINE:|7-commandAction|23|692-preAction
                // write pre-action user code here
                getEmptyFields();
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|24|692-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|663-preAction
        } else if (displayable == VitaminADose) {
            if (command == vitaminadoseBackCmd) {//GEN-END:|7-commandAction|25|663-preAction
                // write pre-action user code here
                switchDisplayable(null, getImmunizationsessions());//GEN-LINE:|7-commandAction|26|663-postAction
                // write post-action user code here
            } else if (command == vitaminadoseCmd) {//GEN-LINE:|7-commandAction|27|665-preAction
                // write pre-action user code here
                switchDisplayable(null, getChildhoodDiseasesreported());//GEN-LINE:|7-commandAction|28|665-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|226-preAction
        } else if (displayable == loadPage) {
            if (command == loadCmd) {//GEN-END:|7-commandAction|29|226-preAction
                int lastSelected = lastChoice.getSelectedIndex();
                if (lastSelected == 0) {
                    editingLastReport = true;
                } else {
                    editingLastReport = false;
                }
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|30|226-postAction
                // write post-action user code here
            } else if (command == loadExitCmd) {//GEN-LINE:|7-commandAction|31|228-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|32|228-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|180-preAction
        } else if (displayable == monthPage) {
            if (command == monthCmd) {//GEN-END:|7-commandAction|33|180-preAction
                // write pre-action user code here
                switchDisplayable(null, getFollowingImmunization());//GEN-LINE:|7-commandAction|34|180-postAction
                // write post-action user code here
            } else if (command == monthExitCmd) {//GEN-LINE:|7-commandAction|35|459-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|36|459-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|703-preAction
        } else if (displayable == sendPage) {
            if (command == saveCmd) {//GEN-END:|7-commandAction|37|703-preAction
                // write pre-action user code here
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());
                saveDataToRMS(monthStr, freqStr);
                Alert myAlert = new Alert("Save success","Your data has been saved!",null,AlertType.INFO);
                myAlert.setTimeout(2000);
                Display.getDisplay(this).setCurrent(myAlert,sendPage);
//GEN-LINE:|7-commandAction|38|703-postAction
                // write post-action user code here
            } else if (command == sendBackCmd) {//GEN-LINE:|7-commandAction|39|171-preAction
                // write pre-action user code here
                switchDisplayable(null, getLabTests());//GEN-LINE:|7-commandAction|40|171-postAction
                // write post-action user code here
            } else if (command == sendCmd) {//GEN-LINE:|7-commandAction|41|169-preAction
                sendMsgLabel.setText("Sending SMS...");
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());

                final String scForm1Data = collectFormData(monthStr, freqStr);
                //<editor-fold defaultstate="collapsed" desc=" Thread to Save Records to RMS ">
                saveDataToRMS(monthStr,freqStr);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" Thread to Send SMS ">
                sendDataViaSMS(scForm1Data);
                //</editor-fold>

//GEN-LINE:|7-commandAction|42|169-postAction
                // write post-action user code here
            } else if (command == sendExitCmd) {//GEN-LINE:|7-commandAction|43|207-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|44|207-postAction
                // write post-action user code here
            } else if (command == sendSettingsCmd) {//GEN-LINE:|7-commandAction|45|255-preAction
                // write pre-action user code here
                switchDisplayable(null, getSettingsPage());//GEN-LINE:|7-commandAction|46|255-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|47|246-preAction
        } else if (displayable == settingsPage) {
            if (command == settingsBackCmd) {//GEN-END:|7-commandAction|47|246-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|48|246-postAction
                // write post-action user code here
            } else if (command == settingsCmd) {//GEN-LINE:|7-commandAction|49|243-preAction
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
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|50|243-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|51|203-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|51|203-preAction
                if (savedMsg == false) {
                    switchDisplayable(null, getMonthPage());
                } else {
                    switchDisplayable(null, getLoadPage());//GEN-LINE:|7-commandAction|52|203-postAction
                }
            }//GEN-BEGIN:|7-commandAction|53|7-postCommandAction
        }//GEN-END:|7-commandAction|53|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|54|580-postAction
//</editor-fold>//GEN-END:|7-commandAction|54|580-postAction

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
            monthPage.addCommand(getMonthExitCmd());
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
                    // int index = 0;// current month
                    int index = 1; // last month
                    
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


//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField52 ">//GEN-BEGIN:|634-getter|0|634-preInit
    /**
     * Returns an initialized instance of textField52 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField52() {
        if (textField52 == null) {//GEN-END:|634-getter|0|634-preInit
            // write pre-init user code here
            String str = getRecordValue(11);
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
            String str = getRecordValue(12);
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
            String str = getRecordValue(13);
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
            String str = getRecordValue(14);
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
            String str = getRecordValue(15);
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
            String str = getRecordValue(16);
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
            String str = getRecordValue(17);
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
            String str = getRecordValue(18);
            textField59 = new TextField(LocalizationSupport.getMessage("TT16"), str, 4, TextField.NUMERIC);//GEN-LINE:|641-getter|1|641-postInit
            // write post-init user code here
        }//GEN-BEGIN:|641-getter|2|
        return textField59;
    }
//</editor-fold>//GEN-END:|641-getter|2|

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
            String str = getRecordValue(19);
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
            String str = getRecordValue(20);
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
            String str = getRecordValue(21);
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
            Immunizationsessions = new Form("Immunization sessions", new Item[]{getTextField63(), getTextField64()});//GEN-BEGIN:|652-getter|1|652-postInit
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
            String str = getRecordValue(22);
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
            String str = getRecordValue(23);
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
            VitaminADose = new Form("Vitamin A Dose", new Item[]{getTextField65(), getTextField66(), getTextField67()});//GEN-BEGIN:|661-getter|1|661-postInit
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
            String str = getRecordValue(24);
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
            String str = getRecordValue(25);
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
            String str = getRecordValue(26);
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
            String str = getRecordValue(27);
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
            String str = getRecordValue(28);
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
            String str = getRecordValue(29);
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
            String str = getRecordValue(30);
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
            String str = getRecordValue(31);
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
            String str = getRecordValue(32);
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
            String str = getRecordValue(33);
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
            FollowingImmunization = new Form("Following Immunization", new Item[]{getTextField52(), getTextField53(), getTextField54(), getTextField55(), getTextField56(), getTextField57(), getTextField58(), getTextField59()});//GEN-BEGIN:|627-getter|1|627-postInit
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
  //<editor-fold defaultstate="collapsed" desc=" getEmptyFields() ">
    private void getEmptyFields() {
        
        String fullStr = "";
        int j = 0;
   
        fullStr = getMyBaseData();
        String[] allContent = split(fullStr, ":");

        for (int i = 0; i < allContent.length; i++) {
            if (allContent[i].equals("")) {
                j++;
            }
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
    String myData = 
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
                textField74.getString();
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
        
        formID = 1;
        String periodType = "3";
        //return "HP NRHM "+ msgVersion + "!" + formID + "*" + periodType + "?" + monthData + "$" + myData;
        
        // If the message too long, seperate to 2 message
        // lengh of "HP NRHM "+ formID + "*" + monthData + "$" is 15 characters
        if(myData.length() > 145)
        {
            // assign data into String [] myDataTemp
            String[] myDataTemp = split(myData, ":");
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
                // if characters <= 142. BECAUSE seperateIndex + "!" CONTENT 3 CHARACTERS
                while(characters < 143){
                if(myData.length() + myDataTemp[seperateIndex].length() < 143){
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
                
                // myData have to content 142 characters. And the last SMS doesn't need enough 142 characters
                while (myData.length() < 142 && seperateIndex < myDataTemp.length)
                    myData += "!";
                
                // append data to myReturnData
                myReturnData += "HP NRHM "+ formID + "*" + monthData + "$" + addIndex + "?" + myData;
                
            }// end while
        } // end if myData.leght > 145
        else{
            //myReturnData = "HP NRHM "+ formID + "*" + monthData + "$" + myData;
            myReturnData += "HP NRHM "+ formID + "*" + monthData + "$" + "52" + "?" + myData;
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
                    
                    //<editor-fold defaultstate="collapsed" desc="update data into RMS ">

                    lastMsgStore.setRecord(11, textField52.getString().getBytes(), 0, textField52.getString().length());
                    lastMsgStore.setRecord(12, textField53.getString().getBytes(), 0, textField53.getString().length());
                    lastMsgStore.setRecord(13, textField54.getString().getBytes(), 0, textField54.getString().length());
                    lastMsgStore.setRecord(14, textField55.getString().getBytes(), 0, textField55.getString().length());
                    lastMsgStore.setRecord(15, textField56.getString().getBytes(), 0, textField56.getString().length());
                    lastMsgStore.setRecord(16, textField57.getString().getBytes(), 0, textField57.getString().length());
                    lastMsgStore.setRecord(17, textField58.getString().getBytes(), 0, textField58.getString().length());
                    lastMsgStore.setRecord(18, textField59.getString().getBytes(), 0, textField59.getString().length());
                    lastMsgStore.setRecord(19, textField60.getString().getBytes(), 0, textField60.getString().length());
                    lastMsgStore.setRecord(20, textField61.getString().getBytes(), 0, textField61.getString().length());
                    lastMsgStore.setRecord(21, textField62.getString().getBytes(), 0, textField62.getString().length());
                    lastMsgStore.setRecord(22, textField63.getString().getBytes(), 0, textField63.getString().length());
                    lastMsgStore.setRecord(23, textField64.getString().getBytes(), 0, textField64.getString().length());
                    lastMsgStore.setRecord(24, textField65.getString().getBytes(), 0, textField65.getString().length());
                    lastMsgStore.setRecord(25, textField66.getString().getBytes(), 0, textField66.getString().length());
                    lastMsgStore.setRecord(26, textField67.getString().getBytes(), 0, textField67.getString().length());
                    lastMsgStore.setRecord(27, textField68.getString().getBytes(), 0, textField68.getString().length());
                    lastMsgStore.setRecord(28, textField69.getString().getBytes(), 0, textField69.getString().length());
                    lastMsgStore.setRecord(29, textField70.getString().getBytes(), 0, textField70.getString().length());
                    lastMsgStore.setRecord(30, textField71.getString().getBytes(), 0, textField71.getString().length());
                    lastMsgStore.setRecord(31, textField72.getString().getBytes(), 0, textField72.getString().length());
                    lastMsgStore.setRecord(32, textField73.getString().getBytes(), 0, textField73.getString().length());
                    lastMsgStore.setRecord(33, textField74.getString().getBytes(), 0, textField74.getString().length());
                    
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