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
    private Form MaternalDeaths;
    private TextField textField1;
    private TextField textField65;
    private TextField textField64;
    private TextField textField66;
    private TextField textField63;
    private TextField textField62;
    private Form Mortalitydetails_infants;
    private TextField textField2;
    private Form Infantchilddeathsupto5yearsbycause;
    private TextField textField13;
    private TextField textField12;
    private TextField textField11;
    private TextField textField10;
    private TextField textField14;
    private TextField textField15;
    private TextField textField16;
    private TextField textField17;
    private TextField textField19;
    private TextField textField18;
    private Form Infantdeathsupto4weeksbycause;
    private TextField textField5;
    private TextField textField4;
    private TextField textField3;
    private TextField textField;
    private TextField textField9;
    private TextField textField8;
    private TextField textField7;
    private TextField textField6;
    private Form AdolescentAdultdeathsbycause;
    private TextField textField20;
    private TextField textField21;
    private TextField textField22;
    private TextField textField23;
    private TextField textField24;
    private TextField textField25;
    private TextField textField26;
    private TextField textField27;
    private TextField textField29;
    private TextField textField28;
    private TextField textField31;
    private TextField textField30;
    private TextField textField33;
    private TextField textField32;
    private TextField textField34;
    private Form AdolescentAdultdeathsbycause1;
    private TextField textField42;
    private TextField textField43;
    private TextField textField40;
    private TextField textField41;
    private TextField textField47;
    private TextField textField46;
    private TextField textField45;
    private TextField textField44;
    private TextField textField51;
    private TextField textField50;
    private TextField textField49;
    private TextField textField48;
    private TextField textField53;
    private TextField textField52;
    private TextField textField35;
    private Form AdolescentAdultdeathsbycause2;
    private TextField textField37;
    private TextField textField36;
    private TextField textField55;
    private TextField textField54;
    private TextField textField39;
    private TextField textField38;
    private TextField textField61;
    private TextField textField60;
    private TextField textField57;
    private TextField textField56;
    private TextField textField59;
    private TextField textField58;
    private Form sendPage;
    private StringItem sendMsgLabel;
    private Form ExpenditureDetails;
    private TextField textField69;
    private TextField textField68;
    private TextField textField67;
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
    private Command ExpenditureDetailsCmd;
    private Command saveCmd;
    private Command sendBackCmd;
    private Command sendCmd;
    private Command ExpenditureDetailsBackCmd;
    private Command monthExitCmd;
    private Command monthCmd;
    private Command sendExitCmd;
    private Command loadExitCmd;
    private Command loadCmd;
    private Command sendSettingsCmd;
    private Command settingsBackCmd;
    private Command settingsCmd;
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
                for (int i = 0; i < 79; i++) {
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
        if (displayable == AdolescentAdultdeathsbycause) {//GEN-BEGIN:|7-commandAction|1|542-preAction
            if (command == AdolescentAdultdeathsbycauseBackCmd) {//GEN-END:|7-commandAction|1|542-preAction
                // write pre-action user code here
                switchDisplayable(null, getInfantchilddeathsupto5yearsbycause());//GEN-LINE:|7-commandAction|2|542-postAction
                // write post-action user code here
            } else if (command == AdolescentAdultdeathsbycauseCmd) {//GEN-LINE:|7-commandAction|3|544-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause1());//GEN-LINE:|7-commandAction|4|544-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|606-preAction
        } else if (displayable == AdolescentAdultdeathsbycause1) {
            if (command == AdolescentAdultdeathsbycauseBackCmd01) {//GEN-END:|7-commandAction|5|606-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause());//GEN-LINE:|7-commandAction|6|606-postAction
                // write post-action user code here
            } else if (command == AdolescentAdultdeathsbycauseCmd01) {//GEN-LINE:|7-commandAction|7|608-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause2());//GEN-LINE:|7-commandAction|8|608-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|610-preAction
        } else if (displayable == AdolescentAdultdeathsbycause2) {
            if (command == AdolescentAdultdeathsbycauseBackCmd02) {//GEN-END:|7-commandAction|9|610-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause1());//GEN-LINE:|7-commandAction|10|610-postAction
                // write post-action user code here
            } else if (command == AdolescentAdultdeathsbycauseCmd02) {//GEN-LINE:|7-commandAction|11|612-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaternalDeaths());//GEN-LINE:|7-commandAction|12|612-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|672-preAction
        } else if (displayable == ExpenditureDetails) {
            if (command == ExpenditureDetailsBackCmd) {//GEN-END:|7-commandAction|13|672-preAction
                // write pre-action user code here
                switchDisplayable(null, getMaternalDeaths());//GEN-LINE:|7-commandAction|14|672-postAction
                // write post-action user code here
            } else if (command == ExpenditureDetailsCmd) {//GEN-LINE:|7-commandAction|15|674-preAction
                // write pre-action user code here
                getEmptyFields();
                switchDisplayable(null, getSendPage());//GEN-LINE:|7-commandAction|16|674-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|537-preAction
        } else if (displayable == Infantchilddeathsupto5yearsbycause) {
            if (command == Infantchilddeathsupto5yearsbycauseBackCmd) {//GEN-END:|7-commandAction|17|537-preAction
                // write pre-action user code here
                switchDisplayable(null, getInfantdeathsupto4weeksbycause());//GEN-LINE:|7-commandAction|18|537-postAction
                // write post-action user code here
            } else if (command == Infantchilddeathsupto5yearsbycauseCmd) {//GEN-LINE:|7-commandAction|19|539-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause());//GEN-LINE:|7-commandAction|20|539-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|532-preAction
        } else if (displayable == Infantdeathsupto4weeksbycause) {
            if (command == Infantdeathsupto4weeksbycauseBackCmd) {//GEN-END:|7-commandAction|21|532-preAction
                // write pre-action user code here
                switchDisplayable(null, getMortalitydetails_infants());//GEN-LINE:|7-commandAction|22|532-postAction
                // write post-action user code here
            } else if (command == Infantdeathsupto4weeksbycauseCmd) {//GEN-LINE:|7-commandAction|23|534-preAction
                // write pre-action user code here
                switchDisplayable(null, getInfantchilddeathsupto5yearsbycause());//GEN-LINE:|7-commandAction|24|534-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|547-preAction
        } else if (displayable == MaternalDeaths) {
            if (command == MaternalDeathsBackCmd) {//GEN-END:|7-commandAction|25|547-preAction
                // write pre-action user code here
                switchDisplayable(null, getAdolescentAdultdeathsbycause2());//GEN-LINE:|7-commandAction|26|547-postAction
                // write post-action user code here
            } else if (command == MaternalDeathsCmd) {//GEN-LINE:|7-commandAction|27|549-preAction
                // write post-action user code here
                switchDisplayable(null, getExpenditureDetails());//GEN-LINE:|7-commandAction|28|549-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|527-preAction
        } else if (displayable == Mortalitydetails_infants) {
            if (command == Mortalitydatails_infantsBackcmd) {//GEN-END:|7-commandAction|29|527-preAction
                // write pre-action user code here
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|30|527-postAction
                // write post-action user code here
            } else if (command == Mortalitydatails_infantsCmd) {//GEN-LINE:|7-commandAction|31|529-preAction
                // write pre-action user code here
                switchDisplayable(null, getInfantdeathsupto4weeksbycause());//GEN-LINE:|7-commandAction|32|529-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|226-preAction
        } else if (displayable == loadPage) {
            if (command == loadCmd) {//GEN-END:|7-commandAction|33|226-preAction
                int lastSelected = lastChoice.getSelectedIndex();
                if (lastSelected == 0) {
                    editingLastReport = true;
                } else {
                    editingLastReport = false;
                }
                switchDisplayable(null, getMonthPage());//GEN-LINE:|7-commandAction|34|226-postAction
                // write post-action user code here
            } else if (command == loadExitCmd) {//GEN-LINE:|7-commandAction|35|228-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|36|228-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|180-preAction
        } else if (displayable == monthPage) {
            if (command == monthCmd) {//GEN-END:|7-commandAction|37|180-preAction
                // write pre-action user code here
                switchDisplayable(null, getMortalitydetails_infants());//GEN-LINE:|7-commandAction|38|180-postAction
                // write post-action user code here
            } else if (command == monthExitCmd) {//GEN-LINE:|7-commandAction|39|668-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|40|668-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|41|653-preAction
        } else if (displayable == sendPage) {
            if (command == saveCmd) {//GEN-END:|7-commandAction|41|653-preAction
                // write pre-action user code here
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());
                saveDataToRMS(monthStr, freqStr);
                Alert myAlert = new Alert("Save success","Your data has been saved!",null,AlertType.INFO);
                myAlert.setTimeout(2000);
                Display.getDisplay(this).setCurrent(myAlert,sendPage);
//GEN-LINE:|7-commandAction|42|653-postAction
                // write post-action user code here
            } else if (command == sendBackCmd) {//GEN-LINE:|7-commandAction|43|171-preAction
                // write pre-action user code here
                switchDisplayable(null, getExpenditureDetails());//GEN-LINE:|7-commandAction|44|171-postAction
                // write post-action user code here
            } else if (command == sendCmd) {//GEN-LINE:|7-commandAction|45|169-preAction
                sendMsgLabel.setText("Sending SMS...");
                final String monthStr = monthChoice.getString(monthChoice.getSelectedIndex());
                final String freqStr = freqGroup.getString(freqGroup.getSelectedIndex());
                final String scForm1Data = collectFormData(monthStr, freqStr);

                //<editor-fold defaultstate="collapsed" desc=" Thread to Save Records to RMS ">
                saveDataToRMS(monthStr,freqStr);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc=" Thread to Send SMS ">
                // Split to separating SMS
                sendDataViaSMS(scForm1Data);
                //</editor-fold>
//GEN-LINE:|7-commandAction|46|169-postAction
                // write post-action user code here
            } else if (command == sendExitCmd) {//GEN-LINE:|7-commandAction|47|207-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|48|207-postAction
                // write post-action user code here
            } else if (command == sendSettingsCmd) {//GEN-LINE:|7-commandAction|49|255-preAction
                // write pre-action user code here
                switchDisplayable(null, getSettingsPage());//GEN-LINE:|7-commandAction|50|255-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|51|246-preAction
        } else if (displayable == settingsPage) {
            if (command == settingsBackCmd) {//GEN-END:|7-commandAction|51|246-preAction
                // write pre-action user code here
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|52|246-postAction
                // write post-action user code here
            } else if (command == settingsCmd) {//GEN-LINE:|7-commandAction|53|243-preAction
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
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|54|243-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|55|203-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|55|203-preAction
                if (savedMsg == false) {
                    switchDisplayable(null, getMonthPage());
                } else {
                    switchDisplayable(null, getLoadPage());//GEN-LINE:|7-commandAction|56|203-postAction
                }
            }//GEN-BEGIN:|7-commandAction|57|7-postCommandAction
        }//GEN-END:|7-commandAction|57|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|58|
//</editor-fold>//GEN-END:|7-commandAction|58|

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
                int index = 0;// current month
                // int index = 1; // last month

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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Mortalitydetails_infants ">//GEN-BEGIN:|525-getter|0|525-preInit
    /**
     * Returns an initialized instance of Mortalitydetails_infants component.
     *
     * @return the initialized component instance
     */
    public Form getMortalitydetails_infants() {
        if (Mortalitydetails_infants == null) {//GEN-END:|525-getter|0|525-preInit
            // write pre-init user code here
            Mortalitydetails_infants = new Form(LocalizationSupport.getMessage("TitleMortalityDetails"), new Item[]{getTextField2()});//GEN-BEGIN:|525-getter|1|525-postInit
            Mortalitydetails_infants.addCommand(getMortalitydatails_infantsBackcmd());
            Mortalitydetails_infants.addCommand(getMortalitydatails_infantsCmd());
            Mortalitydetails_infants.setCommandListener(this);//GEN-END:|525-getter|1|525-postInit
            // write post-init user code here
        }//GEN-BEGIN:|525-getter|2|
        return Mortalitydetails_infants;
    }
//</editor-fold>//GEN-END:|525-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantdeathsupto4weeksbycause ">//GEN-BEGIN:|530-getter|0|530-preInit
    /**
     * Returns an initialized instance of Infantdeathsupto4weeksbycause
     * component.
     *
     * @return the initialized component instance
     */
    public Form getInfantdeathsupto4weeksbycause() {
        if (Infantdeathsupto4weeksbycause == null) {//GEN-END:|530-getter|0|530-preInit
            // write pre-init user code here
            Infantdeathsupto4weeksbycause = new Form(LocalizationSupport.getMessage("TitleInfantDeath<4wk"), new Item[]{getTextField(), getTextField3(), getTextField4(), getTextField5(), getTextField6(), getTextField7(), getTextField8(), getTextField9()});//GEN-BEGIN:|530-getter|1|530-postInit
            Infantdeathsupto4weeksbycause.addCommand(getInfantdeathsupto4weeksbycauseBackCmd());
            Infantdeathsupto4weeksbycause.addCommand(getInfantdeathsupto4weeksbycauseCmd());
            Infantdeathsupto4weeksbycause.setCommandListener(this);//GEN-END:|530-getter|1|530-postInit
            // write post-init user code here
        }//GEN-BEGIN:|530-getter|2|
        return Infantdeathsupto4weeksbycause;
    }
//</editor-fold>//GEN-END:|530-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: Infantchilddeathsupto5yearsbycause ">//GEN-BEGIN:|535-getter|0|535-preInit
    /**
     * Returns an initialized instance of Infantchilddeathsupto5yearsbycause
     * component.
     *
     * @return the initialized component instance
     */
    public Form getInfantchilddeathsupto5yearsbycause() {
        if (Infantchilddeathsupto5yearsbycause == null) {//GEN-END:|535-getter|0|535-preInit
            // write pre-init user code here
            Infantchilddeathsupto5yearsbycause = new Form(LocalizationSupport.getMessage("TitleChildDeath<5yr"), new Item[]{getTextField10(), getTextField11(), getTextField12(), getTextField13(), getTextField14(), getTextField15(), getTextField16(), getTextField17(), getTextField19(), getTextField18()});//GEN-BEGIN:|535-getter|1|535-postInit
            Infantchilddeathsupto5yearsbycause.addCommand(getInfantchilddeathsupto5yearsbycauseBackCmd());
            Infantchilddeathsupto5yearsbycause.addCommand(getInfantchilddeathsupto5yearsbycauseCmd());
            Infantchilddeathsupto5yearsbycause.setCommandListener(this);//GEN-END:|535-getter|1|535-postInit
            // write post-init user code here
        }//GEN-BEGIN:|535-getter|2|
        return Infantchilddeathsupto5yearsbycause;
    }
//</editor-fold>//GEN-END:|535-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycause ">//GEN-BEGIN:|540-getter|0|540-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycause
     * component.
     *
     * @return the initialized component instance
     */
    public Form getAdolescentAdultdeathsbycause() {
        if (AdolescentAdultdeathsbycause == null) {//GEN-END:|540-getter|0|540-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycause = new Form(LocalizationSupport.getMessage("TitleAdol/AdultDeath"), new Item[]{getTextField20(), getTextField21(), getTextField22(), getTextField23(), getTextField24(), getTextField25(), getTextField26(), getTextField27(), getTextField28(), getTextField29(), getTextField30(), getTextField31(), getTextField32(), getTextField33(), getTextField34()});//GEN-BEGIN:|540-getter|1|540-postInit
            AdolescentAdultdeathsbycause.addCommand(getAdolescentAdultdeathsbycauseBackCmd());
            AdolescentAdultdeathsbycause.addCommand(getAdolescentAdultdeathsbycauseCmd());
            AdolescentAdultdeathsbycause.setCommandListener(this);//GEN-END:|540-getter|1|540-postInit
            // write post-init user code here
        }//GEN-BEGIN:|540-getter|2|
        return AdolescentAdultdeathsbycause;
    }
//</editor-fold>//GEN-END:|540-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MaternalDeaths ">//GEN-BEGIN:|545-getter|0|545-preInit
    /**
     * Returns an initialized instance of MaternalDeaths component.
     *
     * @return the initialized component instance
     */
    public Form getMaternalDeaths() {
        if (MaternalDeaths == null) {//GEN-END:|545-getter|0|545-preInit
            // write pre-init user code here
            MaternalDeaths = new Form(LocalizationSupport.getMessage("TitleMaternalDeaths"), new Item[]{getTextField1(), getTextField62(), getTextField63(), getTextField64(), getTextField65(), getTextField66()});//GEN-BEGIN:|545-getter|1|545-postInit
            MaternalDeaths.addCommand(getMaternalDeathsBackCmd());
            MaternalDeaths.addCommand(getMaternalDeathsCmd());
            MaternalDeaths.setCommandListener(this);//GEN-END:|545-getter|1|545-postInit
            // write post-init user code here
        }//GEN-BEGIN:|545-getter|2|
        return MaternalDeaths;
    }
//</editor-fold>//GEN-END:|545-getter|2|

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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|564-getter|0|564-preInit
    /**
     * Returns an initialized instance of textField2 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|564-getter|0|564-preInit
            // write pre-init user code here
            String str = getRecordValue(12);
            textField2 = new TextField(LocalizationSupport.getMessage("Infant_deaths_24hrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|564-getter|1|564-postInit
            // write post-init user code here
        }//GEN-BEGIN:|564-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|564-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|562-getter|0|562-preInit
    /**
     * Returns an initialized instance of textField component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|562-getter|0|562-preInit
            // write pre-init user code here
            String str = getRecordValue(11);
            textField = new TextField(LocalizationSupport.getMessage("Sepsis_1week"), str, 4, TextField.NUMERIC);//GEN-LINE:|562-getter|1|562-postInit
            // write post-init user code here
        }//GEN-BEGIN:|562-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|562-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField3 ">//GEN-BEGIN:|565-getter|0|565-preInit
    /**
     * Returns an initialized instance of textField3 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField3() {
        if (textField3 == null) {//GEN-END:|565-getter|0|565-preInit
            // write pre-init user code here
            String str = getRecordValue(13);
            textField3 = new TextField(LocalizationSupport.getMessage("Sepsis_1-4weeks"), str, 4, TextField.NUMERIC);//GEN-LINE:|565-getter|1|565-postInit
            // write post-init user code here
        }//GEN-BEGIN:|565-getter|2|
        return textField3;
    }
//</editor-fold>//GEN-END:|565-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField4 ">//GEN-BEGIN:|566-getter|0|566-preInit
    /**
     * Returns an initialized instance of textField4 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField4() {
        if (textField4 == null) {//GEN-END:|566-getter|0|566-preInit
            // write pre-init user code here
            String str = getRecordValue(14);
            textField4 = new TextField(LocalizationSupport.getMessage("Asphyxia_1week"), str, 4, TextField.NUMERIC);//GEN-LINE:|566-getter|1|566-postInit
            // write post-init user code here
        }//GEN-BEGIN:|566-getter|2|
        return textField4;
    }
//</editor-fold>//GEN-END:|566-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField5 ">//GEN-BEGIN:|567-getter|0|567-preInit
    /**
     * Returns an initialized instance of textField5 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField5() {
        if (textField5 == null) {//GEN-END:|567-getter|0|567-preInit
            // write pre-init user code here
            String str = getRecordValue(15);
            textField5 = new TextField(LocalizationSupport.getMessage("Asphyxia1_4weeks"), str, 4, TextField.NUMERIC);//GEN-LINE:|567-getter|1|567-postInit
            // write post-init user code here
        }//GEN-BEGIN:|567-getter|2|
        return textField5;
    }
//</editor-fold>//GEN-END:|567-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField6 ">//GEN-BEGIN:|568-getter|0|568-preInit
    /**
     * Returns an initialized instance of textField6 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField6() {
        if (textField6 == null) {//GEN-END:|568-getter|0|568-preInit
            // write pre-init user code here
            String str = getRecordValue(16);
            textField6 = new TextField(LocalizationSupport.getMessage("LBW_1week"), str, 4, TextField.NUMERIC);//GEN-LINE:|568-getter|1|568-postInit
 // write post-init user code here
        }//GEN-BEGIN:|568-getter|2|
        return textField6;
    }
//</editor-fold>//GEN-END:|568-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField7 ">//GEN-BEGIN:|569-getter|0|569-preInit
    /**
     * Returns an initialized instance of textField7 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField7() {
        if (textField7 == null) {//GEN-END:|569-getter|0|569-preInit
            // write pre-init user code here
            String str = getRecordValue(17);
            textField7 = new TextField(LocalizationSupport.getMessage("LBW_1-4weeks"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|569-getter|1|569-postInit
            textField7.setInitialInputMode("UCB_BASIC_LATIN");//GEN-END:|569-getter|1|569-postInit
 // write post-init user code here
        }//GEN-BEGIN:|569-getter|2|
        return textField7;
    }
//</editor-fold>//GEN-END:|569-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField8 ">//GEN-BEGIN:|570-getter|0|570-preInit
    /**
     * Returns an initialized instance of textField8 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField8() {
        if (textField8 == null) {//GEN-END:|570-getter|0|570-preInit
            // write pre-init user code here
            String str = getRecordValue(18);
            textField8 = new TextField(LocalizationSupport.getMessage("Others_1week"), str, 4, TextField.NUMERIC);//GEN-LINE:|570-getter|1|570-postInit
            // write post-init user code here
        }//GEN-BEGIN:|570-getter|2|
        return textField8;
    }
//</editor-fold>//GEN-END:|570-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField9 ">//GEN-BEGIN:|571-getter|0|571-preInit
    /**
     * Returns an initialized instance of textField9 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField9() {
        if (textField9 == null) {//GEN-END:|571-getter|0|571-preInit
            // write pre-init user code here
            String str = getRecordValue(19);
            textField9 = new TextField(LocalizationSupport.getMessage("Others_1-4weeks"), str, 4, TextField.NUMERIC);//GEN-LINE:|571-getter|1|571-postInit
            // write post-init user code here
        }//GEN-BEGIN:|571-getter|2|
        return textField9;
    }
//</editor-fold>//GEN-END:|571-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField10 ">//GEN-BEGIN:|572-getter|0|572-preInit
    /**
     * Returns an initialized instance of textField10 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField10() {
        if (textField10 == null) {//GEN-END:|572-getter|0|572-preInit
            // write pre-init user code here
            String str = getRecordValue(20);
            textField10 = new TextField(LocalizationSupport.getMessage("Pneu_1-11months"), str, 4, TextField.NUMERIC);//GEN-LINE:|572-getter|1|572-postInit
            // write post-init user code here
        }//GEN-BEGIN:|572-getter|2|
        return textField10;
    }
//</editor-fold>//GEN-END:|572-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField11 ">//GEN-BEGIN:|573-getter|0|573-preInit
    /**
     * Returns an initialized instance of textField11 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField11() {
        if (textField11 == null) {//GEN-END:|573-getter|0|573-preInit
            // write pre-init user code here
            String str = getRecordValue(21);
            textField11 = new TextField(LocalizationSupport.getMessage("Pneu_1-5yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|573-getter|1|573-postInit
            // write post-init user code here
        }//GEN-BEGIN:|573-getter|2|
        return textField11;
    }
//</editor-fold>//GEN-END:|573-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField12 ">//GEN-BEGIN:|574-getter|0|574-preInit
    /**
     * Returns an initialized instance of textField12 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField12() {
        if (textField12 == null) {//GEN-END:|574-getter|0|574-preInit
            // write pre-init user code here
            String str = getRecordValue(22);
            textField12 = new TextField(LocalizationSupport.getMessage("Diar_1-11_months"), str, 4, TextField.NUMERIC);//GEN-LINE:|574-getter|1|574-postInit
            // write post-init user code here
        }//GEN-BEGIN:|574-getter|2|
        return textField12;
    }
//</editor-fold>//GEN-END:|574-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField13 ">//GEN-BEGIN:|575-getter|0|575-preInit
    /**
     * Returns an initialized instance of textField13 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField13() {
        if (textField13 == null) {//GEN-END:|575-getter|0|575-preInit
            // write pre-init user code here
            String str = getRecordValue(23);
            textField13 = new TextField(LocalizationSupport.getMessage("Diar_1-5yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|575-getter|1|575-postInit
            // write post-init user code here
        }//GEN-BEGIN:|575-getter|2|
        return textField13;
    }
//</editor-fold>//GEN-END:|575-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField14 ">//GEN-BEGIN:|576-getter|0|576-preInit
    /**
     * Returns an initialized instance of textField14 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField14() {
        if (textField14 == null) {//GEN-END:|576-getter|0|576-preInit
            // write pre-init user code here
            String str = getRecordValue(24);
            textField14 = new TextField(LocalizationSupport.getMessage("Fev_Rel_1-11months"), str, 4, TextField.NUMERIC);//GEN-LINE:|576-getter|1|576-postInit
            // write post-init user code here
        }//GEN-BEGIN:|576-getter|2|
        return textField14;
    }
//</editor-fold>//GEN-END:|576-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField15 ">//GEN-BEGIN:|577-getter|0|577-preInit
    /**
     * Returns an initialized instance of textField15 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField15() {
        if (textField15 == null) {//GEN-END:|577-getter|0|577-preInit
            // write pre-init user code here
            String str = getRecordValue(25);
            textField15 = new TextField(LocalizationSupport.getMessage("Fev_Rel_1-5yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|577-getter|1|577-postInit
            // write post-init user code here
        }//GEN-BEGIN:|577-getter|2|
        return textField15;
    }
//</editor-fold>//GEN-END:|577-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField16 ">//GEN-BEGIN:|578-getter|0|578-preInit
    /**
     * Returns an initialized instance of textField16 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField16() {
        if (textField16 == null) {//GEN-END:|578-getter|0|578-preInit
            // write pre-init user code here
            String str = getRecordValue(26);
            textField16 = new TextField(LocalizationSupport.getMessage("Measl_1-11_months"), str, 4, TextField.NUMERIC);//GEN-LINE:|578-getter|1|578-postInit
            // write post-init user code here
        }//GEN-BEGIN:|578-getter|2|
        return textField16;
    }
//</editor-fold>//GEN-END:|578-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField17 ">//GEN-BEGIN:|579-getter|0|579-preInit
    /**
     * Returns an initialized instance of textField17 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField17() {
        if (textField17 == null) {//GEN-END:|579-getter|0|579-preInit
            // write pre-init user code here
            String str = getRecordValue(27);
            textField17 = new TextField(LocalizationSupport.getMessage("Measl_1-5yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|579-getter|1|579-postInit
            // write post-init user code here
        }//GEN-BEGIN:|579-getter|2|
        return textField17;
    }
//</editor-fold>//GEN-END:|579-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField19 ">//GEN-BEGIN:|581-getter|0|581-preInit
    /**
     * Returns an initialized instance of textField19 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField19() {
        if (textField19 == null) {//GEN-END:|581-getter|0|581-preInit
            // write pre-init user code here
            String str = getRecordValue(29);
            textField19 = new TextField(LocalizationSupport.getMessage("Other_1-11months"), str, 4, TextField.NUMERIC);//GEN-LINE:|581-getter|1|581-postInit
            // write post-init user code here
        }//GEN-BEGIN:|581-getter|2|
        return textField19;
    }
//</editor-fold>//GEN-END:|581-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField18 ">//GEN-BEGIN:|583-getter|0|583-preInit
    /**
     * Returns an initialized instance of textField18 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField18() {
        if (textField18 == null) {//GEN-END:|583-getter|0|583-preInit
            // write pre-init user code here
            String str = getRecordValue(28);
            textField18 = new TextField(LocalizationSupport.getMessage("Others_1-5yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|583-getter|1|583-postInit
            // write post-init user code here
        }//GEN-BEGIN:|583-getter|2|
        return textField18;
    }
//</editor-fold>//GEN-END:|583-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField20 ">//GEN-BEGIN:|584-getter|0|584-preInit
    /**
     * Returns an initialized instance of textField20 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField20() {
        if (textField20 == null) {//GEN-END:|584-getter|0|584-preInit
            // write pre-init user code here
            String str = getRecordValue(30);
            textField20 = new TextField(LocalizationSupport.getMessage("Diar_dis6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|584-getter|1|584-postInit
            // write post-init user code here
        }//GEN-BEGIN:|584-getter|2|
        return textField20;
    }
//</editor-fold>//GEN-END:|584-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField21 ">//GEN-BEGIN:|585-getter|0|585-preInit
    /**
     * Returns an initialized instance of textField21 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField21() {
        if (textField21 == null) {//GEN-END:|585-getter|0|585-preInit
            // write pre-init user code here
            String str = getRecordValue(31);
            textField21 = new TextField(LocalizationSupport.getMessage("Diar_dis_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|585-getter|1|585-postInit
            // write post-init user code here
        }//GEN-BEGIN:|585-getter|2|
        return textField21;
    }
//</editor-fold>//GEN-END:|585-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField22 ">//GEN-BEGIN:|586-getter|0|586-preInit
    /**
     * Returns an initialized instance of textField22 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField22() {
        if (textField22 == null) {//GEN-END:|586-getter|0|586-preInit
            // write pre-init user code here
            String str = getRecordValue(32);
            textField22 = new TextField(LocalizationSupport.getMessage("Diar_dis_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|586-getter|1|586-postInit
            // write post-init user code here
        }//GEN-BEGIN:|586-getter|2|
        return textField22;
    }
//</editor-fold>//GEN-END:|586-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField23 ">//GEN-BEGIN:|587-getter|0|587-preInit
    /**
     * Returns an initialized instance of textField23 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField23() {
        if (textField23 == null) {//GEN-END:|587-getter|0|587-preInit
            // write pre-init user code here
            String str = getRecordValue(33);
            textField23 = new TextField(LocalizationSupport.getMessage("TB_6-14_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|587-getter|1|587-postInit
            // write post-init user code here
        }//GEN-BEGIN:|587-getter|2|
        return textField23;
    }
//</editor-fold>//GEN-END:|587-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField24 ">//GEN-BEGIN:|588-getter|0|588-preInit
    /**
     * Returns an initialized instance of textField24 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField24() {
        if (textField24 == null) {//GEN-END:|588-getter|0|588-preInit
            // write pre-init user code here
            String str = getRecordValue(34);
            textField24 = new TextField(LocalizationSupport.getMessage("TB_15-55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|588-getter|1|588-postInit
            // write post-init user code here
        }//GEN-BEGIN:|588-getter|2|
        return textField24;
    }
//</editor-fold>//GEN-END:|588-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField25 ">//GEN-BEGIN:|589-getter|0|589-preInit
    /**
     * Returns an initialized instance of textField25 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField25() {
        if (textField25 == null) {//GEN-END:|589-getter|0|589-preInit
            // write pre-init user code here
            String str = getRecordValue(35);
            textField25 = new TextField(LocalizationSupport.getMessage("TB_Above_55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|589-getter|1|589-postInit
            // write post-init user code here
        }//GEN-BEGIN:|589-getter|2|
        return textField25;
    }
//</editor-fold>//GEN-END:|589-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField26 ">//GEN-BEGIN:|590-getter|0|590-preInit
    /**
     * Returns an initialized instance of textField26 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField26() {
        if (textField26 == null) {//GEN-END:|590-getter|0|590-preInit
            // write pre-init user code here
            String str = getRecordValue(36);
            textField26 = new TextField(LocalizationSupport.getMessage("Resp_TB_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|590-getter|1|590-postInit
            // write post-init user code here
        }//GEN-BEGIN:|590-getter|2|
        return textField26;
    }
//</editor-fold>//GEN-END:|590-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField27 ">//GEN-BEGIN:|591-getter|0|591-preInit
    /**
     * Returns an initialized instance of textField27 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField27() {
        if (textField27 == null) {//GEN-END:|591-getter|0|591-preInit
            // write pre-init user code here
            String str = getRecordValue(37);
            textField27 = new TextField(LocalizationSupport.getMessage("Resp_TB_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|591-getter|1|591-postInit
            // write post-init user code here
        }//GEN-BEGIN:|591-getter|2|
        return textField27;
    }
//</editor-fold>//GEN-END:|591-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField28 ">//GEN-BEGIN:|592-getter|0|592-preInit
    /**
     * Returns an initialized instance of textField28 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField28() {
        if (textField28 == null) {//GEN-END:|592-getter|0|592-preInit
            // write pre-init user code here
            String str = getRecordValue(38);
            textField28 = new TextField(LocalizationSupport.getMessage("Resp_TB_Abo_55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|592-getter|1|592-postInit
            // write post-init user code here
        }//GEN-BEGIN:|592-getter|2|
        return textField28;
    }
//</editor-fold>//GEN-END:|592-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField29 ">//GEN-BEGIN:|593-getter|0|593-preInit
    /**
     * Returns an initialized instance of textField29 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField29() {
        if (textField29 == null) {//GEN-END:|593-getter|0|593-preInit
            // write pre-init user code here
            String str = getRecordValue(39);
            textField29 = new TextField(LocalizationSupport.getMessage("Malaria_6-14_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|593-getter|1|593-postInit
            // write post-init user code here
        }//GEN-BEGIN:|593-getter|2|
        return textField29;
    }
//</editor-fold>//GEN-END:|593-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField30 ">//GEN-BEGIN:|594-getter|0|594-preInit
    /**
     * Returns an initialized instance of textField30 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField30() {
        if (textField30 == null) {//GEN-END:|594-getter|0|594-preInit
            // write pre-init user code here
            String str = getRecordValue(40);
            textField30 = new TextField(LocalizationSupport.getMessage("Malaria_15-55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|594-getter|1|594-postInit
            // write post-init user code here
        }//GEN-BEGIN:|594-getter|2|
        return textField30;
    }
//</editor-fold>//GEN-END:|594-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField31 ">//GEN-BEGIN:|595-getter|0|595-preInit
    /**
     * Returns an initialized instance of textField31 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField31() {
        if (textField31 == null) {//GEN-END:|595-getter|0|595-preInit
            // write pre-init user code here
            String str = getRecordValue(41);
            textField31 = new TextField(LocalizationSupport.getMessage("Malaria_Above_55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|595-getter|1|595-postInit
            // write post-init user code here
        }//GEN-BEGIN:|595-getter|2|
        return textField31;
    }
//</editor-fold>//GEN-END:|595-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField32 ">//GEN-BEGIN:|596-getter|0|596-preInit
    /**
     * Returns an initialized instance of textField32 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField32() {
        if (textField32 == null) {//GEN-END:|596-getter|0|596-preInit
            // write pre-init user code here
            String str = getRecordValue(42);
            textField32 = new TextField(LocalizationSupport.getMessage("Other_FeverRel_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|596-getter|1|596-postInit
            // write post-init user code here
        }//GEN-BEGIN:|596-getter|2|
        return textField32;
    }
//</editor-fold>//GEN-END:|596-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField33 ">//GEN-BEGIN:|597-getter|0|597-preInit
    /**
     * Returns an initialized instance of textField33 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField33() {
        if (textField33 == null) {//GEN-END:|597-getter|0|597-preInit
            // write pre-init user code here
            String str = getRecordValue(43);
            textField33 = new TextField(LocalizationSupport.getMessage("Other_Fever_Rel_15-55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|597-getter|1|597-postInit
            // write post-init user code here
        }//GEN-BEGIN:|597-getter|2|
        return textField33;
    }
//</editor-fold>//GEN-END:|597-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField34 ">//GEN-BEGIN:|598-getter|0|598-preInit
    /**
     * Returns an initialized instance of textField34 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField34() {
        if (textField34 == null) {//GEN-END:|598-getter|0|598-preInit
            // write pre-init user code here
            String str = getRecordValue(44);
            textField34 = new TextField(LocalizationSupport.getMessage("Other_FeverRel_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|598-getter|1|598-postInit
            // write post-init user code here
        }//GEN-BEGIN:|598-getter|2|
        return textField34;
    }
//</editor-fold>//GEN-END:|598-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|563-getter|0|563-preInit
    /**
     * Returns an initialized instance of textField1 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|563-getter|0|563-preInit
            // write pre-init user code here
            String str = getRecordValue(77);
            textField1 = new TextField(LocalizationSupport.getMessage("Abortion_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|563-getter|1|563-postInit
            // write post-init user code here
        }//GEN-BEGIN:|563-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|563-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycause1 ">//GEN-BEGIN:|603-getter|0|603-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycause1
     * component.
     *
     * @return the initialized component instance
     */
    public Form getAdolescentAdultdeathsbycause1() {
        if (AdolescentAdultdeathsbycause1 == null) {//GEN-END:|603-getter|0|603-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycause1 = new Form(LocalizationSupport.getMessage("TitleAdol/AdultDeath"), new Item[]{getTextField35(), getTextField40(), getTextField41(), getTextField42(), getTextField43(), getTextField44(), getTextField45(), getTextField46(), getTextField47(), getTextField48(), getTextField49(), getTextField50(), getTextField51(), getTextField52(), getTextField53()});//GEN-BEGIN:|603-getter|1|603-postInit
            AdolescentAdultdeathsbycause1.addCommand(getAdolescentAdultdeathsbycauseBackCmd01());
            AdolescentAdultdeathsbycause1.addCommand(getAdolescentAdultdeathsbycauseCmd01());
            AdolescentAdultdeathsbycause1.setCommandListener(this);//GEN-END:|603-getter|1|603-postInit
            // write post-init user code here
        }//GEN-BEGIN:|603-getter|2|
        return AdolescentAdultdeathsbycause1;
    }
//</editor-fold>//GEN-END:|603-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField40 ">//GEN-BEGIN:|620-getter|0|620-preInit
    /**
     * Returns an initialized instance of textField40 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField40() {
        if (textField40 == null) {//GEN-END:|620-getter|0|620-preInit
            // write pre-init user code here
            String str = getRecordValue(50);
            textField40 = new TextField(LocalizationSupport.getMessage("HIV/AIDS_15-55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|620-getter|1|620-postInit
            // write post-init user code here
        }//GEN-BEGIN:|620-getter|2|
        return textField40;
    }
//</editor-fold>//GEN-END:|620-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField41 ">//GEN-BEGIN:|621-getter|0|621-preInit
    /**
     * Returns an initialized instance of textField41 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField41() {
        if (textField41 == null) {//GEN-END:|621-getter|0|621-preInit
            // write pre-init user code here
            String str = getRecordValue(51);
            textField41 = new TextField(LocalizationSupport.getMessage("HIV/AIDS_Abo_55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|621-getter|1|621-postInit
            // write post-init user code here
        }//GEN-BEGIN:|621-getter|2|
        return textField41;
    }
//</editor-fold>//GEN-END:|621-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField42 ">//GEN-BEGIN:|622-getter|0|622-preInit
    /**
     * Returns an initialized instance of textField42 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField42() {
        if (textField42 == null) {//GEN-END:|622-getter|0|622-preInit
            // write pre-init user code here
            String str = getRecordValue(52);
            textField42 = new TextField(LocalizationSupport.getMessage("HD/HT_rel_6-14_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|622-getter|1|622-postInit
            // write post-init user code here
        }//GEN-BEGIN:|622-getter|2|
        return textField42;
    }
//</editor-fold>//GEN-END:|622-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField43 ">//GEN-BEGIN:|623-getter|0|623-preInit
    /**
     * Returns an initialized instance of textField43 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField43() {
        if (textField43 == null) {//GEN-END:|623-getter|0|623-preInit
            // write pre-init user code here
            String str = getRecordValue(53);
            textField43 = new TextField(LocalizationSupport.getMessage("HD/HT_rel_15-55_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|623-getter|1|623-postInit
            // write post-init user code here
        }//GEN-BEGIN:|623-getter|2|
        return textField43;
    }
//</editor-fold>//GEN-END:|623-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField44 ">//GEN-BEGIN:|624-getter|0|624-preInit
    /**
     * Returns an initialized instance of textField44 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField44() {
        if (textField44 == null) {//GEN-END:|624-getter|0|624-preInit
            // write pre-init user code here
            String str = getRecordValue(54);
            textField44 = new TextField(LocalizationSupport.getMessage("HD/HT(Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|624-getter|1|624-postInit
            // write post-init user code here
        }//GEN-BEGIN:|624-getter|2|
        return textField44;
    }
//</editor-fold>//GEN-END:|624-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField45 ">//GEN-BEGIN:|625-getter|0|625-preInit
    /**
     * Returns an initialized instance of textField45 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField45() {
        if (textField45 == null) {//GEN-END:|625-getter|0|625-preInit
            // write pre-init user code here
            String str = getRecordValue(55);
            textField45 = new TextField(LocalizationSupport.getMessage("Neuro_stroke_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|625-getter|1|625-postInit
            // write post-init user code here
        }//GEN-BEGIN:|625-getter|2|
        return textField45;
    }
//</editor-fold>//GEN-END:|625-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField46 ">//GEN-BEGIN:|626-getter|0|626-preInit
    /**
     * Returns an initialized instance of textField46 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField46() {
        if (textField46 == null) {//GEN-END:|626-getter|0|626-preInit
            // write pre-init user code here
            String str = getRecordValue(56);
            textField46 = new TextField(LocalizationSupport.getMessage("Neuro_stroke15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|626-getter|1|626-postInit
            // write post-init user code here
        }//GEN-BEGIN:|626-getter|2|
        return textField46;
    }
//</editor-fold>//GEN-END:|626-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField47 ">//GEN-BEGIN:|627-getter|0|627-preInit
    /**
     * Returns an initialized instance of textField47 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField47() {
        if (textField47 == null) {//GEN-END:|627-getter|0|627-preInit
            // write pre-init user code here
            String str = getRecordValue(57);
            textField47 = new TextField(LocalizationSupport.getMessage("Neuroo_stroke_Abo55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|627-getter|1|627-postInit
            // write post-init user code here
        }//GEN-BEGIN:|627-getter|2|
        return textField47;
    }
//</editor-fold>//GEN-END:|627-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField48 ">//GEN-BEGIN:|628-getter|0|628-preInit
    /**
     * Returns an initialized instance of textField48 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField48() {
        if (textField48 == null) {//GEN-END:|628-getter|0|628-preInit
            // write pre-init user code here
            String str = getRecordValue(58);
            textField48 = new TextField(LocalizationSupport.getMessage("Trau/Acci/Burn_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|628-getter|1|628-postInit
            // write post-init user code here
        }//GEN-BEGIN:|628-getter|2|
        return textField48;
    }
//</editor-fold>//GEN-END:|628-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField49 ">//GEN-BEGIN:|629-getter|0|629-preInit
    /**
     * Returns an initialized instance of textField49 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField49() {
        if (textField49 == null) {//GEN-END:|629-getter|0|629-preInit
            // write pre-init user code here
            String str = getRecordValue(59);
            textField49 = new TextField(LocalizationSupport.getMessage("Trau/Acci/Burn_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|629-getter|1|629-postInit
            // write post-init user code here
        }//GEN-BEGIN:|629-getter|2|
        return textField49;
    }
//</editor-fold>//GEN-END:|629-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField50 ">//GEN-BEGIN:|630-getter|0|630-preInit
    /**
     * Returns an initialized instance of textField50 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField50() {
        if (textField50 == null) {//GEN-END:|630-getter|0|630-preInit
            // write pre-init user code here
            String str = getRecordValue(60);
            textField50 = new TextField(LocalizationSupport.getMessage("Trau/Acci/Burn_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|630-getter|1|630-postInit
            // write post-init user code here
        }//GEN-BEGIN:|630-getter|2|
        return textField50;
    }
//</editor-fold>//GEN-END:|630-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField51 ">//GEN-BEGIN:|631-getter|0|631-preInit
    /**
     * Returns an initialized instance of textField51 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField51() {
        if (textField51 == null) {//GEN-END:|631-getter|0|631-preInit
            // write pre-init user code here
            String str = getRecordValue(61);
            textField51 = new TextField(LocalizationSupport.getMessage("Suicide_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|631-getter|1|631-postInit
            // write post-init user code here
        }//GEN-BEGIN:|631-getter|2|
        return textField51;
    }
//</editor-fold>//GEN-END:|631-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField52 ">//GEN-BEGIN:|632-getter|0|632-preInit
    /**
     * Returns an initialized instance of textField52 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField52() {
        if (textField52 == null) {//GEN-END:|632-getter|0|632-preInit
            // write pre-init user code here
            String str = getRecordValue(62);
            textField52 = new TextField(LocalizationSupport.getMessage("Suicide_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|632-getter|1|632-postInit
            // write post-init user code here
        }//GEN-BEGIN:|632-getter|2|
        return textField52;
    }
//</editor-fold>//GEN-END:|632-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField53 ">//GEN-BEGIN:|633-getter|0|633-preInit
    /**
     * Returns an initialized instance of textField53 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField53() {
        if (textField53 == null) {//GEN-END:|633-getter|0|633-preInit
            // write pre-init user code here
            String str = getRecordValue(63);
            textField53 = new TextField(LocalizationSupport.getMessage("Suicide_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|633-getter|1|633-postInit
            // write post-init user code here
        }//GEN-BEGIN:|633-getter|2|
        return textField53;
    }
//</editor-fold>//GEN-END:|633-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: AdolescentAdultdeathsbycause2 ">//GEN-BEGIN:|604-getter|0|604-preInit
    /**
     * Returns an initialized instance of AdolescentAdultdeathsbycause2
     * component.
     *
     * @return the initialized component instance
     */
    public Form getAdolescentAdultdeathsbycause2() {
        if (AdolescentAdultdeathsbycause2 == null) {//GEN-END:|604-getter|0|604-preInit
            // write pre-init user code here
            AdolescentAdultdeathsbycause2 = new Form(LocalizationSupport.getMessage("TitleAdol/AdultDeath"), new Item[]{getTextField36(), getTextField37(), getTextField38(), getTextField39(), getTextField54(), getTextField55(), getTextField56(), getTextField57(), getTextField58(), getTextField59(), getTextField60(), getTextField61()});//GEN-BEGIN:|604-getter|1|604-postInit
            AdolescentAdultdeathsbycause2.addCommand(getAdolescentAdultdeathsbycauseBackCmd02());
            AdolescentAdultdeathsbycause2.addCommand(getAdolescentAdultdeathsbycauseCmd02());
            AdolescentAdultdeathsbycause2.setCommandListener(this);//GEN-END:|604-getter|1|604-postInit
            // write post-init user code here
        }//GEN-BEGIN:|604-getter|2|
        return AdolescentAdultdeathsbycause2;
    }
//</editor-fold>//GEN-END:|604-getter|2|

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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField62 ">//GEN-BEGIN:|646-getter|0|646-preInit
    /**
     * Returns an initialized instance of textField62 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField62() {
        if (textField62 == null) {//GEN-END:|646-getter|0|646-preInit
            // write pre-init user code here
            String str = getRecordValue(72);
            textField62 = new TextField(LocalizationSupport.getMessage("Ob/pro_labour_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-BEGIN:|646-getter|1|646-postInit
            textField62.setInitialInputMode("UCB_BASIC_LATIN");//GEN-END:|646-getter|1|646-postInit
            // write post-init user code here
        }//GEN-BEGIN:|646-getter|2|
        return textField62;
    }
//</editor-fold>//GEN-END:|646-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField63 ">//GEN-BEGIN:|647-getter|0|647-preInit
    /**
     * Returns an initialized instance of textField63 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField63() {
        if (textField63 == null) {//GEN-END:|647-getter|0|647-preInit
            // write pre-init user code here
            String str = getRecordValue(73);
            textField63 = new TextField(LocalizationSupport.getMessage("Severe_HT/fits_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|647-getter|1|647-postInit
            // write post-init user code here
        }//GEN-BEGIN:|647-getter|2|
        return textField63;
    }
//</editor-fold>//GEN-END:|647-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField64 ">//GEN-BEGIN:|648-getter|0|648-preInit
    /**
     * Returns an initialized instance of textField64 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField64() {
        if (textField64 == null) {//GEN-END:|648-getter|0|648-preInit
            // write pre-init user code here
            String str = getRecordValue(74);
            textField64 = new TextField(LocalizationSupport.getMessage("Bleeding_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|648-getter|1|648-postInit
            // write post-init user code here
        }//GEN-BEGIN:|648-getter|2|
        return textField64;
    }
//</editor-fold>//GEN-END:|648-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField65 ">//GEN-BEGIN:|649-getter|0|649-preInit
    /**
     * Returns an initialized instance of textField65 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField65() {
        if (textField65 == null) {//GEN-END:|649-getter|0|649-preInit
            // write pre-init user code here
            String str = getRecordValue(75);
            textField65 = new TextField(LocalizationSupport.getMessage("High_fever_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|649-getter|1|649-postInit
            // write post-init user code here
        }//GEN-BEGIN:|649-getter|2|
        return textField65;
    }
//</editor-fold>//GEN-END:|649-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField66 ">//GEN-BEGIN:|650-getter|0|650-preInit
    /**
     * Returns an initialized instance of textField66 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField66() {
        if (textField66 == null) {//GEN-END:|650-getter|0|650-preInit
            // write pre-init user code here
            String str = getRecordValue(76);
            textField66 = new TextField(LocalizationSupport.getMessage("Other_Causes_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|650-getter|1|650-postInit
            // write post-init user code here
        }//GEN-BEGIN:|650-getter|2|
        return textField66;
    }
//</editor-fold>//GEN-END:|650-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField36 ">//GEN-BEGIN:|634-getter|0|634-preInit
    /**
     * Returns an initialized instance of textField36 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField36() {
        if (textField36 == null) {//GEN-END:|634-getter|0|634-preInit
            // write pre-init user code here
            String str = getRecordValue(46);
            textField36 = new TextField(LocalizationSupport.getMessage("Animalbite/sting_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|634-getter|1|634-postInit
            // write post-init user code here
        }//GEN-BEGIN:|634-getter|2|
        return textField36;
    }
//</editor-fold>//GEN-END:|634-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField37 ">//GEN-BEGIN:|635-getter|0|635-preInit
    /**
     * Returns an initialized instance of textField37 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField37() {
        if (textField37 == null) {//GEN-END:|635-getter|0|635-preInit
            // write pre-init user code here
            String str = getRecordValue(47);
            textField37 = new TextField(LocalizationSupport.getMessage("Animalbite/sting_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|635-getter|1|635-postInit
            // write post-init user code here
        }//GEN-BEGIN:|635-getter|2|
        return textField37;
    }
//</editor-fold>//GEN-END:|635-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField38 ">//GEN-BEGIN:|636-getter|0|636-preInit
    /**
     * Returns an initialized instance of textField38 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField38() {
        if (textField38 == null) {//GEN-END:|636-getter|0|636-preInit
            // write pre-init user code here
            String str = getRecordValue(48);
            textField38 = new TextField(LocalizationSupport.getMessage("Animalbite_sting_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|636-getter|1|636-postInit
            // write post-init user code here
        }//GEN-BEGIN:|636-getter|2|
        return textField38;
    }
//</editor-fold>//GEN-END:|636-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField39 ">//GEN-BEGIN:|637-getter|0|637-preInit
    /**
     * Returns an initialized instance of textField39 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField39() {
        if (textField39 == null) {//GEN-END:|637-getter|0|637-preInit
            // write pre-init user code here
            String str = getRecordValue(49);
            textField39 = new TextField(LocalizationSupport.getMessage("Kno_Acute_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|637-getter|1|637-postInit
            // write post-init user code here
        }//GEN-BEGIN:|637-getter|2|
        return textField39;
    }
//</editor-fold>//GEN-END:|637-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField54 ">//GEN-BEGIN:|638-getter|0|638-preInit
    /**
     * Returns an initialized instance of textField54 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField54() {
        if (textField54 == null) {//GEN-END:|638-getter|0|638-preInit
            // write pre-init user code here
            String str = getRecordValue(64);
            textField54 = new TextField(LocalizationSupport.getMessage("Kno_Acute_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|638-getter|1|638-postInit
            // write post-init user code here
        }//GEN-BEGIN:|638-getter|2|
        return textField54;
    }
//</editor-fold>//GEN-END:|638-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField55 ">//GEN-BEGIN:|639-getter|0|639-preInit
    /**
     * Returns an initialized instance of textField55 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField55() {
        if (textField55 == null) {//GEN-END:|639-getter|0|639-preInit
            // write pre-init user code here
            String str = getRecordValue(65);
            textField55 = new TextField(LocalizationSupport.getMessage("Kno_Acute_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|639-getter|1|639-postInit
            // write post-init user code here
        }//GEN-BEGIN:|639-getter|2|
        return textField55;
    }
//</editor-fold>//GEN-END:|639-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField56 ">//GEN-BEGIN:|640-getter|0|640-preInit
    /**
     * Returns an initialized instance of textField56 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField56() {
        if (textField56 == null) {//GEN-END:|640-getter|0|640-preInit
            // write pre-init user code here
            String str = getRecordValue(66);
            textField56 = new TextField(LocalizationSupport.getMessage("Kno_Chronic_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|640-getter|1|640-postInit
            // write post-init user code here
        }//GEN-BEGIN:|640-getter|2|
        return textField56;
    }
//</editor-fold>//GEN-END:|640-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField57 ">//GEN-BEGIN:|641-getter|0|641-preInit
    /**
     * Returns an initialized instance of textField57 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField57() {
        if (textField57 == null) {//GEN-END:|641-getter|0|641-preInit
            // write pre-init user code here
            String str = getRecordValue(67);
            textField57 = new TextField(LocalizationSupport.getMessage("Kno_Chronic_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|641-getter|1|641-postInit
            // write post-init user code here
        }//GEN-BEGIN:|641-getter|2|
        return textField57;
    }
//</editor-fold>//GEN-END:|641-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField58 ">//GEN-BEGIN:|642-getter|0|642-preInit
    /**
     * Returns an initialized instance of textField58 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField58() {
        if (textField58 == null) {//GEN-END:|642-getter|0|642-preInit
            // write pre-init user code here
            String str = getRecordValue(68);
            textField58 = new TextField(LocalizationSupport.getMessage("Kno_Chronic_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|642-getter|1|642-postInit
            // write post-init user code here
        }//GEN-BEGIN:|642-getter|2|
        return textField58;
    }
//</editor-fold>//GEN-END:|642-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField59 ">//GEN-BEGIN:|643-getter|0|643-preInit
    /**
     * Returns an initialized instance of textField59 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField59() {
        if (textField59 == null) {//GEN-END:|643-getter|0|643-preInit
            // write pre-init user code here
            String str = getRecordValue(69);
            textField59 = new TextField(LocalizationSupport.getMessage("Causes_not_6-14yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|643-getter|1|643-postInit
            // write post-init user code here
        }//GEN-BEGIN:|643-getter|2|
        return textField59;
    }
//</editor-fold>//GEN-END:|643-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField60 ">//GEN-BEGIN:|644-getter|0|644-preInit
    /**
     * Returns an initialized instance of textField60 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField60() {
        if (textField60 == null) {//GEN-END:|644-getter|0|644-preInit
            // write pre-init user code here
            String str = getRecordValue(70);
            textField60 = new TextField(LocalizationSupport.getMessage("Causes_not_15-55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|644-getter|1|644-postInit
            // write post-init user code here
        }//GEN-BEGIN:|644-getter|2|
        return textField60;
    }
//</editor-fold>//GEN-END:|644-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField61 ">//GEN-BEGIN:|645-getter|0|645-preInit
    /**
     * Returns an initialized instance of textField61 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField61() {
        if (textField61 == null) {//GEN-END:|645-getter|0|645-preInit
            // write pre-init user code here
            String str = getRecordValue(71);
            textField61 = new TextField(LocalizationSupport.getMessage("Causes_not_Abo_55yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|645-getter|1|645-postInit
            // write post-init user code here
        }//GEN-BEGIN:|645-getter|2|
        return textField61;
    }
//</editor-fold>//GEN-END:|645-getter|2|

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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField35 ">//GEN-BEGIN:|665-getter|0|665-preInit
    /**
     * Returns an initialized instance of textField35 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField35() {
        if (textField35 == null) {//GEN-END:|665-getter|0|665-preInit
            // write pre-init user code here
            String str = getRecordValue(45);
            textField35 = new TextField(LocalizationSupport.getMessage("HIV/AIDS_6-14_yrs"), str, 4, TextField.NUMERIC);//GEN-LINE:|665-getter|1|665-postInit
            // write post-init user code here
        }//GEN-BEGIN:|665-getter|2|
        return textField35;
    }
//</editor-fold>//GEN-END:|665-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ExpenditureDetails ">//GEN-BEGIN:|670-getter|0|670-preInit
    /**
     * Returns an initialized instance of ExpenditureDetails component.
     *
     * @return the initialized component instance
     */
    public Form getExpenditureDetails() {
        if (ExpenditureDetails == null) {//GEN-END:|670-getter|0|670-preInit
            // write pre-init user code here
            ExpenditureDetails = new Form(LocalizationSupport.getMessage("ExpenditureDetails"), new Item[]{getTextField67(), getTextField68(), getTextField69()});//GEN-BEGIN:|670-getter|1|670-postInit
            ExpenditureDetails.addCommand(getExpenditureDetailsBackCmd());
            ExpenditureDetails.addCommand(getExpenditureDetailsCmd());
            ExpenditureDetails.setCommandListener(this);//GEN-END:|670-getter|1|670-postInit
            // write post-init user code here
        }//GEN-BEGIN:|670-getter|2|
        return ExpenditureDetails;
    }
//</editor-fold>//GEN-END:|670-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: monthExitCmd ">//GEN-BEGIN:|667-getter|0|667-preInit
    /**
     * Returns an initialized instance of monthExitCmd component.
     *
     * @return the initialized component instance
     */
    public Command getMonthExitCmd() {
        if (monthExitCmd == null) {//GEN-END:|667-getter|0|667-preInit
            // write pre-init user code here
            monthExitCmd = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|667-getter|1|667-postInit
            // write post-init user code here
        }//GEN-BEGIN:|667-getter|2|
        return monthExitCmd;
    }
//</editor-fold>//GEN-END:|667-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ExpenditureDetailsBackCmd ">//GEN-BEGIN:|671-getter|0|671-preInit
    /**
     * Returns an initialized instance of ExpenditureDetailsBackCmd component.
     *
     * @return the initialized component instance
     */
    public Command getExpenditureDetailsBackCmd() {
        if (ExpenditureDetailsBackCmd == null) {//GEN-END:|671-getter|0|671-preInit
            // write pre-init user code here
            ExpenditureDetailsBackCmd = new Command("Back", Command.BACK, 0);//GEN-LINE:|671-getter|1|671-postInit
            // write post-init user code here
        }//GEN-BEGIN:|671-getter|2|
        return ExpenditureDetailsBackCmd;
    }
//</editor-fold>//GEN-END:|671-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ExpenditureDetailsCmd ">//GEN-BEGIN:|673-getter|0|673-preInit
    /**
     * Returns an initialized instance of ExpenditureDetailsCmd component.
     *
     * @return the initialized component instance
     */
    public Command getExpenditureDetailsCmd() {
        if (ExpenditureDetailsCmd == null) {//GEN-END:|673-getter|0|673-preInit
            // write pre-init user code here
            ExpenditureDetailsCmd = new Command("Ok", Command.OK, 0);//GEN-LINE:|673-getter|1|673-postInit
            // write post-init user code here
        }//GEN-BEGIN:|673-getter|2|
        return ExpenditureDetailsCmd;
    }
//</editor-fold>//GEN-END:|673-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField67 ">//GEN-BEGIN:|677-getter|0|677-preInit
    /**
     * Returns an initialized instance of textField67 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField67() {
        if (textField67 == null) {//GEN-END:|677-getter|0|677-preInit
            // write pre-init user code here
            String str = getRecordValue(77);
            textField67 = new TextField(LocalizationSupport.getMessage("ExpenditureonUntiedFund"), str, 4, TextField.NUMERIC);//GEN-LINE:|677-getter|1|677-postInit
            // write post-init user code here
        }//GEN-BEGIN:|677-getter|2|
        return textField67;
    }
//</editor-fold>//GEN-END:|677-getter|2|

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
            textField68 = new TextField(LocalizationSupport.getMessage("ExpenditureonUntiedFund1"), str, 4, TextField.NUMERIC);//GEN-LINE:|678-getter|1|678-postInit
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
            textField69 = new TextField(LocalizationSupport.getMessage("ExpenditureonAMG"), str, 4, TextField.NUMERIC);//GEN-LINE:|679-getter|1|679-postInit
            // write post-init user code here
        }//GEN-BEGIN:|679-getter|2|
        return textField69;
    }
//</editor-fold>//GEN-END:|679-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" getEmptyFields() ">
    /*
     * Return number of textfields that have negative Value.
     */
    private int getEmptyFields() {
        
        String fullStr = "";
        int j = 0;
        int negativeData = 0;
        
        fullStr = getMyBaseData();
        String[] allContent = split(fullStr, "|");

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
    String myData = textField2.getString() + "|" +
                textField.getString() + "|" +

                textField3.getString() + "|" +
                textField4.getString() + "|" +
                textField5.getString() + "|" +
                textField6.getString() + "|" +
                textField7.getString() + "|" +
                textField8.getString() + "|" +
                textField9.getString() + "|" +
                textField10.getString() + "|" +
                textField11.getString() + "|" +
                textField12.getString() + "|" +
                textField13.getString() + "|" +
                textField14.getString() + "|" +
                textField15.getString() + "|" +
                textField16.getString() + "|" +
                textField17.getString() + "|" +
                textField19.getString() + "|" +
                textField18.getString() + "|" +
                textField20.getString() + "|" +
                textField21.getString() + "|" +
                textField22.getString() + "|" +
                textField23.getString() + "|" +
                textField24.getString() + "|" +
                textField25.getString() + "|" +
                textField26.getString() + "|" +
                textField27.getString() + "|" +
                textField28.getString() + "|" +
                textField29.getString() + "|" +
                textField30.getString() + "|" +
                textField31.getString() + "|" +
                textField32.getString() + "|" +
                textField33.getString() + "|" +
                textField34.getString() + "|" +
                textField35.getString() + "|" +
                
                textField40.getString() + "|" +
                textField41.getString() + "|" +
                textField42.getString() + "|" +
                textField43.getString() + "|" +
                textField44.getString() + "|" +
                textField45.getString() + "|" +
                textField46.getString() + "|" +
                textField47.getString() + "|" +
                textField48.getString() + "|" +
                textField49.getString() + "|" +
                textField50.getString() + "|" +
                textField51.getString() + "|" +
                textField52.getString() + "|" +
                textField53.getString() + "|" +
            textField36.getString() + "|" +
            textField37.getString() + "|" +
            textField38.getString() + "|" +
            textField39.getString() + "|" +
                textField54.getString() + "|" +
                textField55.getString() + "|" +
                textField56.getString() + "|" +
                textField57.getString() + "|" +
                textField58.getString() + "|" +
                textField59.getString() + "|" +
                textField60.getString() + "|" +
                textField61.getString() + "|" +
            textField1.getString() + "|" +
                textField62.getString() + "|" +
                textField63.getString() + "|" +
                textField64.getString() + "|" +
                textField65.getString() + "|" +
                textField66.getString() + "|" +
                textField67.getString() + "|" +
                textField68.getString() + "|" +
                textField69.getString();
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
            monthData = monthStr.substring(4) + "-0" + (i + 1) + "-01";
        } else {
            monthData = monthStr.substring(4) + "-" + (i + 1) + "-01";
        }
        
        //for frequency setting of the program
        String freqData = "";
        freqData = freqStr;
        
        String myData = "";
       
        myData = getMyBaseData();
        
        // Start remove 0 data
        String[] myDataTemp = split(myData, "|");
        myData = "";
        int seperateIndex = 0;
        while(seperateIndex < myDataTemp.length - 1){
            // create again myData
            // Check and remove 0 value
            if(myDataTemp[seperateIndex].length() != 0 && Integer.parseInt(myDataTemp[seperateIndex]) != 0){
                myData = myData + myDataTemp[seperateIndex] + "|";
            }
            else{
                if(seperateIndex != 0)
                    myData = myData + "|";
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
        
        formID = 47;
        String periodType = "3";
        
        myReturnData = msgVersion + "#" + formID + "*" + periodType + "?" + monthData + "$" + myData;
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
                            MessageConnection smsConn = (MessageConnection) Connector.open("sms://+91" + new String(lastMsgStore.getRecord(i + 1)));
                            BinaryMessage sms = (BinaryMessage) smsConn.newMessage(MessageConnection.BINARY_MESSAGE);
                            byte[] compressedData = null;
                            compressedData = Compressor.compress(scForm1Data.getBytes("UTF-8"));
                            sms.setPayloadData(compressedData);
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
                    lastMsgStore.setRecord(77, textField1.getString().getBytes(), 0, textField1.getString().length()); // missing textField1 so I add one more record in RMS
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
                    lastMsgStore.setRecord(77, textField67.getString().getBytes(), 0, textField66.getString().length());
                    lastMsgStore.setRecord(78, textField68.getString().getBytes(), 0, textField66.getString().length());
                    lastMsgStore.setRecord(79, textField69.getString().getBytes(), 0, textField66.getString().length());
                    
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
                    
                
