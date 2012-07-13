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
    private Form PostNatalCare;
    private TextField textField24;
    private TextField textField25;
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
                for (int i = 0; i < 36; i++) {
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
        if (displayable == ANC_Services) {//GEN-BEGIN:|7-commandAction|1|520-preAction
            if (command == ANC_ServicesBackCmd) {//GEN-END:|7-commandAction|1|520-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|2|520-postAction
                // write post-action user code here
            } else if (command == ANC_ServicesCmd) {//GEN-LINE:|7-commandAction|3|522-preAction
                // write pre-action user code here
                switchDisplayable(null, getDeliveries());//GEN-LINE:|7-commandAction|4|522-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|493-preAction
        } else if (displayable == Antinatalcare) {
            if (command == antinatalcareBackCmd) {//GEN-END:|7-commandAction|5|493-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|6|493-postAction
                // write post-action user code here
            } else if (command == antinatalcareCmd) {//GEN-LINE:|7-commandAction|7|488-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|8|488-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|535-preAction
        } else if (displayable == Deliveries) {
            if (command == DeliveriesBackCmd) {//GEN-END:|7-commandAction|9|535-preAction
                // write pre-action user code here
                switchDisplayable(null, getANC_Services());//GEN-LINE:|7-commandAction|10|535-postAction
                // write post-action user code here
            } else if (command == DeliveriesCmd) {//GEN-LINE:|7-commandAction|11|537-preAction
                // write pre-action user code here
                switchDisplayable(null, getInstitutionalDeliveries());//GEN-LINE:|7-commandAction|12|537-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|548-preAction
        } else if (displayable == InstitutionalDeliveries) {
            if (command == institutionaldeliveriesBackCmd) {//GEN-END:|7-commandAction|13|548-preAction
                // write pre-action user code here
                switchDisplayable(null, getDeliveries());//GEN-LINE:|7-commandAction|14|548-postAction
                // write post-action user code here
            } else if (command == institutionaldeliveriesCmd) {//GEN-LINE:|7-commandAction|15|550-preAction
                // write pre-action user code here
                switchDisplayable(null, getPregnancyoutcome());//GEN-LINE:|7-commandAction|16|550-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|570-preAction
        } else if (displayable == Newbornsweighted) {
            if (command == newbornsweightedBackCmd) {//GEN-END:|7-commandAction|17|570-preAction
                // write pre-action user code here
                switchDisplayable(null, getPregnancyoutcome());//GEN-LINE:|7-commandAction|18|570-postAction
                // write post-action user code here
            } else if (command == newbornsweightedCmd) {//GEN-LINE:|7-commandAction|19|572-preAction
                // write pre-action user code here
                switchDisplayable(null, getPostNatalCare());//GEN-LINE:|7-commandAction|20|572-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|580-preAction
        } else if (displayable == PostNatalCare) {
            if (command == postnatalcareBackCmd) {//GEN-END:|7-commandAction|21|580-preAction
                // write pre-action user code here
                switchDisplayable(null, getNewbornsweighted());//GEN-LINE:|7-commandAction|22|580-postAction
                // write post-action user code here
            } else if (command == postnatalcareCmd) {//GEN-LINE:|7-commandAction|23|582-preAction
                // write pre-action user code here
                getEmptyFields();
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|24|582-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|559-preAction
        } else if (displayable == Pregnancyoutcome) {
            if (command == pregnancyoutcomeBackCmd) {//GEN-END:|7-commandAction|25|559-preAction
                // write pre-action user code here
                switchDisplayable(null, getInstitutionalDeliveries());//GEN-LINE:|7-commandAction|26|559-postAction
                // write post-action user code here
            } else if (command == pregnancyoutcomeCmd) {//GEN-LINE:|7-commandAction|27|561-preAction
                // write pre-action user code here
                switchDisplayable(null, getNewbornsweighted());//GEN-LINE:|7-commandAction|28|561-postAction
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
                switchDisplayable(null, getANC_Services());//GEN-LINE:|7-commandAction|34|180-postAction
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
                switchDisplayable(null, getPostNatalCare());//GEN-LINE:|7-commandAction|40|171-postAction
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
    }//GEN-BEGIN:|7-commandAction|54|
//</editor-fold>//GEN-END:|7-commandAction|54|





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
            String str = getRecordValue(36); //  because my mistake on missing register the record for this textField
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
            Pregnancyoutcome = new Form("Pregnancy outcome", new Item[]{getTextField17(), getTextField18(), getTextField19(), getTextField20()});//GEN-BEGIN:|557-getter|1|557-postInit
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
            Newbornsweighted = new Form("Newborns weighted", new Item[]{getTextField21(), getTextField22(), getTextField23()});//GEN-BEGIN:|568-getter|1|568-postInit
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
            textField21 = new TextField(LocalizationSupport.getMessage("Newborns_Weighed"), str, 4, TextField.NUMERIC);//GEN-LINE:|575-getter|1|575-postInit
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
            textField22 = new TextField(LocalizationSupport.getMessage("Newborns_2.5kg"), str, 4, TextField.NUMERIC);//GEN-LINE:|576-getter|1|576-postInit
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
            textField23 = new TextField(LocalizationSupport.getMessage("Newborns_Breastfed"), str, 4, TextField.NUMERIC);//GEN-LINE:|577-getter|1|577-postInit
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
                textField22.getString() + ":" +
                textField23.getString() + ":" +
                textField24.getString() + ":" +
                textField25.getString() ;       
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
            // myReturnData = "HP NRHM "+ formID + "*" + monthData + "$" + myData;
            myReturnData += "HP NRHM "+ formID + "*" + monthData + "$" + "00" + "?" + myData;
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
                    lastMsgStore.setRecord(11, textField.getString().getBytes(), 0, textField.getString().length());
                    lastMsgStore.setRecord(36, textField1.getString().getBytes(), 0, textField1.getString().length()); // missing textField1 so I add one more record in RMS
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