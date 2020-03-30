/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import panels.Reader.Reader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 *
 * @author andrewd12
 */
public class MainFrame extends javax.swing.JFrame
{
    TableStruct tables;
    
    java.util.ArrayList<Reader> panelsList = new java.util.ArrayList<>();
    
    java.awt.event.KeyListener commonKeyListener = new java.awt.event.KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent ke) {}
        
        @Override
        public void keyPressed(KeyEvent ke)
        {
            pressKey(ke);
        }
        
        @Override
        public void keyReleased(KeyEvent ke) {}
    };
    
    java.awt.event.KeyListener greenMeansGo = new java.awt.event.KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent ke) {}
        
        @Override
        public void keyPressed(KeyEvent ke)
        {
            switch(ke.getKeyCode())
            {
                case KeyEvent.VK_ENTER : insertNewTab();
                break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent ke) {}
    };
    
    java.awt.event.KeyListener redMeansItsTimeToStop = new java.awt.event.KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent ke) {}
        
        @Override
        public void keyPressed(KeyEvent ke)
        {
            switch(ke.getKeyCode())
            {
                case KeyEvent.VK_ENTER : removeCurrentTab();
                break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent ke) {}
    };
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame()
    {
        initComponents();
        initComponentsAgain();
        tables = new TableStruct();
        insertNewTab(0);
        
        jTabbedPane1.addKeyListener(commonKeyListener);
        jTabbedPane1.addMouseListener(new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                javax.swing.JTabbedPane source = (javax.swing.JTabbedPane) e.getSource();
                if (source.getSelectedIndex() == source.getTabCount() - 1)
                {
                    insertNewTab(source.getTabCount() - 1);
                    source.setSelectedIndex(source.getTabCount() - 2);
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {}
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            
            @Override
            public void mouseEntered(MouseEvent e) {}
            
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        jTabbedPane1.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        jTabbedPane1.addTab(" +  ", new javax.swing.JPanel());
        jTabbedPane1.setToolTipTextAt(jTabbedPane1.getTabCount() - 1, "New Tab (Ctl + N)");
        jTabbedPane1.setFocusable(false);
      //  settingsButton.addKeyListener(commonKeyListener);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        settingsMenu = new javax.swing.JMenuBar();
        edit = new javax.swing.JMenu();
        undo = new javax.swing.JMenuItem();
        paste = new javax.swing.JMenuItem();
        jSeparator = new javax.swing.JPopupMenu.Separator();
        copy = new javax.swing.JMenuItem();
        audio = new javax.swing.JMenu();
        play = new javax.swing.JMenuItem();
        pause = new javax.swing.JMenuItem();
        rewind = new javax.swing.JMenuItem();
        fastForward = new javax.swing.JMenuItem();
        font = new javax.swing.JMenu();
        fontFamily = new javax.swing.JMenu();
        abadi = new javax.swing.JMenuItem();
        albertus = new javax.swing.JMenuItem();
        antiqueO = new javax.swing.JMenuItem();
        arial = new javax.swing.JMenuItem();
        bookA = new javax.swing.JMenuItem();
        cSchoolbook = new javax.swing.JMenuItem();
        courier = new javax.swing.JMenuItem();
        tahoma = new javax.swing.JMenuItem();
        univers = new javax.swing.JMenuItem();
        fontSize = new javax.swing.JMenu();
        size8 = new javax.swing.JMenuItem();
        size9 = new javax.swing.JMenuItem();
        size10 = new javax.swing.JMenuItem();
        size11 = new javax.swing.JMenuItem();
        size12 = new javax.swing.JMenuItem();
        size13 = new javax.swing.JMenuItem();
        size14 = new javax.swing.JMenuItem();
        size15 = new javax.swing.JMenuItem();
        size16 = new javax.swing.JMenuItem();
        size17 = new javax.swing.JMenuItem();
        size18 = new javax.swing.JMenuItem();
        size20 = new javax.swing.JMenuItem();
        size22 = new javax.swing.JMenuItem();
        size24 = new javax.swing.JMenuItem();
        size26 = new javax.swing.JMenuItem();
        size28 = new javax.swing.JMenuItem();
        size36 = new javax.swing.JMenuItem();
        size48 = new javax.swing.JMenuItem();
        size72 = new javax.swing.JMenuItem();
        language = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        French = new javax.swing.JMenuItem();
        Portuguese = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        edit.setText("Edit");

        undo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/action-undo-2x.png"))); // NOI18N
        undo.setText("Undo");
        edit.add(undo);

        paste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        paste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/action-redo-2x.png"))); // NOI18N
        paste.setText("Paste");
        edit.add(paste);
        edit.add(jSeparator);

        copy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copy.setText("Copy");
        edit.add(copy);

        settingsMenu.add(edit);

        audio.setText("Audio");

        play.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_MASK));
        play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/media-play-2x.png"))); // NOI18N
        play.setText("Play");
        audio.add(play);

        pause.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, java.awt.event.InputEvent.CTRL_MASK));
        pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/media-pause-2x.png"))); // NOI18N
        pause.setText("Pause");
        pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseActionPerformed(evt);
            }
        });
        audio.add(pause);

        rewind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_MASK));
        rewind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/media-step-backward-2x.png"))); // NOI18N
        rewind.setText("Rewind");
        rewind.setToolTipText("Go Back 30 Seconds");
        audio.add(rewind);

        fastForward.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_MASK));
        fastForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/media-step-forward-2x.png"))); // NOI18N
        fastForward.setText("Fast Forward");
        fastForward.setToolTipText("Go Forward 30 Seconds");
        audio.add(fastForward);

        settingsMenu.add(audio);

        font.setText("Font");

        fontFamily.setText("Font Family");

        abadi.setText("Abadi MT Condensed Light ");
        fontFamily.add(abadi);

        albertus.setText("Albertus Extra Bold");
        fontFamily.add(albertus);

        antiqueO.setText("Antique Olive");
        fontFamily.add(antiqueO);

        arial.setText("Arial ");
        arial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arialActionPerformed(evt);
            }
        });
        fontFamily.add(arial);

        bookA.setText("Book Antiqua");
        fontFamily.add(bookA);

        cSchoolbook.setText("Century Schoolbook");
        fontFamily.add(cSchoolbook);

        courier.setText("Courier New");
        courier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courierActionPerformed(evt);
            }
        });
        fontFamily.add(courier);

        tahoma.setText("Tahoma");
        fontFamily.add(tahoma);

        univers.setText("Univers Condensed");
        fontFamily.add(univers);

        font.add(fontFamily);

        fontSize.setText("Font Size");

        size8.setText("8");
        size8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size8ActionPerformed(evt);
            }
        });
        fontSize.add(size8);

        size9.setText("9");
        size9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size9ActionPerformed(evt);
            }
        });
        fontSize.add(size9);

        size10.setText("10");
        size10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size10ActionPerformed(evt);
            }
        });
        fontSize.add(size10);

        size11.setText("11");
        size11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size11ActionPerformed(evt);
            }
        });
        fontSize.add(size11);

        size12.setText("12");
        size12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size12ActionPerformed(evt);
            }
        });
        fontSize.add(size12);

        size13.setText("13");
        size13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size13ActionPerformed(evt);
            }
        });
        fontSize.add(size13);

        size14.setText("14");
        size14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size14ActionPerformed(evt);
            }
        });
        fontSize.add(size14);

        size15.setText("15");
        size15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size15ActionPerformed(evt);
            }
        });
        fontSize.add(size15);

        size16.setText("16");
        size16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size16ActionPerformed(evt);
            }
        });
        fontSize.add(size16);

        size17.setText("17");
        size17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size17ActionPerformed(evt);
            }
        });
        fontSize.add(size17);

        size18.setText("18");
        size18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size18ActionPerformed(evt);
            }
        });
        fontSize.add(size18);

        size20.setText("20");
        size20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size20ActionPerformed(evt);
            }
        });
        fontSize.add(size20);

        size22.setText("22");
        size22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size22ActionPerformed(evt);
            }
        });
        fontSize.add(size22);

        size24.setText("24");
        size24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size24ActionPerformed(evt);
            }
        });
        fontSize.add(size24);

        size26.setText("26");
        size26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size26ActionPerformed(evt);
            }
        });
        fontSize.add(size26);

        size28.setText("28");
        size28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size28ActionPerformed(evt);
            }
        });
        fontSize.add(size28);

        size36.setText("36");
        size36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size36ActionPerformed(evt);
            }
        });
        fontSize.add(size36);

        size48.setText("48");
        size48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size48ActionPerformed(evt);
            }
        });
        fontSize.add(size48);

        size72.setText("72");
        size72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size72ActionPerformed(evt);
            }
        });
        fontSize.add(size72);

        font.add(fontSize);

        settingsMenu.add(font);

        language.setText("Language");
        language.setToolTipText("Set Current Tab's Language");

        jMenuItem1.setText("English");
        language.add(jMenuItem1);

        jMenuItem2.setText("Spanish");
        language.add(jMenuItem2);

        jMenuItem3.setText("German");
        language.add(jMenuItem3);

        French.setText("French");
        language.add(French);

        Portuguese.setText("Portuguese");
        language.add(Portuguese);

        jMenuItem8.setText("Latin");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        language.add(jMenuItem8);

        jMenuItem5.setText("Koine Greek");
        language.add(jMenuItem5);

        jMenuItem6.setText("Ancient Hebrew");
        language.add(jMenuItem6);

        settingsMenu.add(language);

        setJMenuBar(settingsMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void arialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arialActionPerformed

    private void courierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_courierActionPerformed

    private void size8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size8ActionPerformed

    private void size9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size9ActionPerformed

    private void size10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size10ActionPerformed

    private void size11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size11ActionPerformed

    private void size12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size12ActionPerformed

    private void size13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size13ActionPerformed

    private void size14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size14ActionPerformed

    private void size15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size15ActionPerformed

    private void size16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size16ActionPerformed

    private void size17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size17ActionPerformed

    private void size18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size18ActionPerformed

    private void size20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size20ActionPerformed

    private void size24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size24ActionPerformed

    private void size22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size22ActionPerformed

    private void size26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size26ActionPerformed

    private void size28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size28ActionPerformed

    private void size36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size36ActionPerformed

    private void size48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size48ActionPerformed

    private void size72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size72ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_size72ActionPerformed

    private void pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pauseActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed
    
    private void initComponentsAgain()
    {
        pack();
        addKeyListener(commonKeyListener);
    }
    
    private Reader getCurrentlySelectedTab()
    {
        return (Reader) jTabbedPane1.getSelectedComponent();
    }
    
    public interface Responder
    {
        public abstract void respond(KeyEvent e);
    }
    
    public class TableStruct
    {
        public final HashMap<Integer, Responder> CONTROL_DOWN_KEY_MAP = newControlDownKeyMap();
        public final HashMap<Integer, Responder> SHIFT_DOWN_KEY_MAP = newShiftDownKeyMap();
        public final HashMap<Integer, Responder> ALT_DOWN_KEY_MAP = newAltDownKeyMap();
        public final HashMap<Integer, Responder> NEUTRAL_KEY_MAP = newNeutralKeyMap();
        
        public HashMap<Integer, Responder> newControlDownKeyMap()
        {
            HashMap<Integer, Responder> map = new HashMap<>();
            
            map.put(KeyEvent.VK_PAGE_UP, e -> {e.consume(); goUp();});
            map.put(KeyEvent.VK_PAGE_DOWN, e -> {e.consume(); goDown();});
            map.put(KeyEvent.VK_N, e -> insertNewTab());
            map.put(KeyEvent.VK_W, e -> removeCurrentTab());
            map.put(KeyEvent.VK_F, e -> getCurrentlySelectedTab().getSearchBar().requestFocus());
            map.put(KeyEvent.VK_HOME, e -> getCurrentlySelectedTab().scrollToTop());
            map.put(KeyEvent.VK_END, e -> getCurrentlySelectedTab().scrollToBottom());
            
            return map;
        }
        
        public HashMap<Integer, Responder> newShiftDownKeyMap()
        {
            HashMap<Integer, Responder> map = new HashMap<>();
            
            map.put(KeyEvent.VK_F3, e -> {
                Reader panel = getCurrentlySelectedTab();
                panel.getTextPane().requestFocus();
                panel.searchData().moveCaretToPreviousSearchResult();
            });
            
            return map;
        }
        
        public HashMap<Integer, Responder> newAltDownKeyMap()
        {
            HashMap<Integer, Responder> map = new HashMap<>();
            
            map.put(KeyEvent.VK_RIGHT, e -> getCurrentlySelectedTab().goForward());
            map.put(KeyEvent.VK_LEFT, e -> getCurrentlySelectedTab().goBack());
            
            return map;
        }
        
        public HashMap<Integer, Responder> newNeutralKeyMap()
        {
            HashMap<Integer, Responder> map = new HashMap<>();
            
            map.put(KeyEvent.VK_PAGE_UP, e -> getCurrentlySelectedTab().scrollUp());
            map.put(KeyEvent.VK_PAGE_DOWN, e -> getCurrentlySelectedTab().scrollDown());
            map.put(KeyEvent.VK_F3, e -> {
                Reader panel = getCurrentlySelectedTab();
                panel.getTextPane().requestFocus();
                panel.searchData().moveCaretToNextSearchResult();
            });

            return map;
        }
    }
    
    private void pressKey(KeyEvent ke)
    {
        if (ke.isControlDown())
        {
            tables.CONTROL_DOWN_KEY_MAP.getOrDefault(ke.getKeyCode(), e -> {}).respond(ke);
        }
        else if (ke.isShiftDown())
        {
            tables.SHIFT_DOWN_KEY_MAP.getOrDefault(ke.getKeyCode(), e -> {}).respond(ke);
        }
        else if (ke.isAltDown())
        {
            tables.ALT_DOWN_KEY_MAP.getOrDefault(ke.getKeyCode(), e -> {}).respond(ke);
        }
        else
        {
            tables.NEUTRAL_KEY_MAP.getOrDefault(ke.getKeyCode(), e -> {}).respond(ke);
        }
    }
    // Go to previous tab
    private void goUp(int offset)
    {
        int currentIndex = jTabbedPane1.getSelectedIndex();
        jTabbedPane1.setSelectedIndex(currentIndex == 0 ? jTabbedPane1.getTabCount() - offset : currentIndex - 1);
    }
    // Go to next tab
    private void goDown(int offset)
    {
        int currentIndex = jTabbedPane1.getSelectedIndex();
        jTabbedPane1.setSelectedIndex(currentIndex == jTabbedPane1.getTabCount() - offset ? 0 : currentIndex + 1);
    }
    // Go to previous tab
    private void goUp()
    {
        goUp(MINIMUM_NUMBER_TABS);
    }
    // Go to next tab
    private void goDown()
    {
        goDown(MINIMUM_NUMBER_TABS);
    }
    
    static final int MINIMUM_NUMBER_TABS = 2;
    
    private void removeCurrentTab()
    {    
        int currentIndex = jTabbedPane1.getSelectedIndex();
        
        jTabbedPane1.removeTabAt(currentIndex);
        panelsList.remove(currentIndex);
        
        if (jTabbedPane1.getTabCount() == MINIMUM_NUMBER_TABS - 1)
            insertNewTab(0);
        
        if (jTabbedPane1.getSelectedIndex() == jTabbedPane1.getTabCount() - 1)
            jTabbedPane1.setSelectedIndex(jTabbedPane1.getSelectedIndex() - 1);
    }
    
    private void insertNewTab()
    {
        insertNewTab(jTabbedPane1.getTabCount() - 1);
    }
    
    private String newTabName(Reader r, String address)
    {
        return String.format("[%s] %s", r.getVersionMenu().getSelectedItem(), address);
    }
    
    private void insertNewTab(int index)
    {
        Reader what = new Reader();
        panels.TabBar customTabComponent = new panels.TabBar(newTabName(what, what.getCurrentAddress()), "");
        
        customTabComponent.addTabCloseKeyListener(redMeansItsTimeToStop);
        customTabComponent.addTabCloseActionListener(e -> removeCurrentTab());
        customTabComponent.setButtonToolTipText("Close Tab (Ctl + W)");
        
        what.addCurrentAddressListener((currentAdddress) -> {
            Reader r = getCurrentlySelectedTab();
            ((panels.TabBar) jTabbedPane1.getTabComponentAt(jTabbedPane1.getSelectedIndex())).setLabelText(newTabName(r, currentAdddress));
        });
        
        what.addKeyListener(commonKeyListener);
        
        for (java.awt.Component thing: what.getThings())
            thing.addKeyListener(commonKeyListener);
        
        what.getSearchBar().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown())
                {
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_ENTER :
                            
                            String text = what.getSearchBar().getText();
                            insertNewTab(jTabbedPane1.getSelectedIndex() + 1);
                            goDown();
                            Reader panel = getCurrentlySelectedTab();
                            panel.getSearchBar().setText(text);
                            panel.updateSearchResults();
                            break;
                            
                        default: break;
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        panelsList.add(index, what);
        
        jTabbedPane1.insertTab("", null, what, "New Tab", index);
        jTabbedPane1.setTabComponentAt(index, customTabComponent);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem French;
    private javax.swing.JMenuItem Portuguese;
    private javax.swing.JMenuItem abadi;
    private javax.swing.JMenuItem albertus;
    private javax.swing.JMenuItem antiqueO;
    private javax.swing.JMenuItem arial;
    private javax.swing.JMenu audio;
    private javax.swing.JMenuItem bookA;
    private javax.swing.JMenuItem cSchoolbook;
    private javax.swing.JMenuItem copy;
    private javax.swing.JMenuItem courier;
    private javax.swing.JMenu edit;
    private javax.swing.JMenuItem fastForward;
    private javax.swing.JMenu font;
    private javax.swing.JMenu fontFamily;
    private javax.swing.JMenu fontSize;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPopupMenu.Separator jSeparator;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu language;
    private javax.swing.JMenuItem paste;
    private javax.swing.JMenuItem pause;
    private javax.swing.JMenuItem play;
    private javax.swing.JMenuItem rewind;
    private javax.swing.JMenuBar settingsMenu;
    private javax.swing.JMenuItem size10;
    private javax.swing.JMenuItem size11;
    private javax.swing.JMenuItem size12;
    private javax.swing.JMenuItem size13;
    private javax.swing.JMenuItem size14;
    private javax.swing.JMenuItem size15;
    private javax.swing.JMenuItem size16;
    private javax.swing.JMenuItem size17;
    private javax.swing.JMenuItem size18;
    private javax.swing.JMenuItem size20;
    private javax.swing.JMenuItem size22;
    private javax.swing.JMenuItem size24;
    private javax.swing.JMenuItem size26;
    private javax.swing.JMenuItem size28;
    private javax.swing.JMenuItem size36;
    private javax.swing.JMenuItem size48;
    private javax.swing.JMenuItem size72;
    private javax.swing.JMenuItem size8;
    private javax.swing.JMenuItem size9;
    private javax.swing.JMenuItem tahoma;
    private javax.swing.JMenuItem undo;
    private javax.swing.JMenuItem univers;
    // End of variables declaration//GEN-END:variables
}