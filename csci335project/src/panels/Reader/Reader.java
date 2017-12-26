/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panels.Reader;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.CaretEvent;

import models.AddressParser;

/**
 *
 * @author andrewd12
 */
public class Reader extends javax.swing.JPanel
{
    private final String IDLE_SEARCH_BAR_MESSAGE = "Find (Ctl + F) Ex: Jesus or Colossians 3:17";
    
    enum ContentTypes
    {
        SEARCH_RESULT,
        WHOLE_CHAPTER
    }

    public class ContentDataStruct
    {
        boolean typedLast = false;
        
        public int currentEndingChapter = 1;
        
        public int prevBookId;
        public int prevChapter;
        
        public int nextBookId;
        public int nextChapter;
        
        private String currentAddress = prefs.defaultStartingAddress;
        ContentTypes currentContentType = ContentTypes.WHOLE_CHAPTER;
        
        public void setCurrentAddress(String newAddress)
        {
            currentAddress = newAddress;
        }
        
        public String getCurrentAddress()
        {
            return currentAddress;
        }
        
        public int getVersionId()
        {
            return versionMenu.getSelectedIndex() + 1;
        }
        
        public int getBookId()
        {
            return bookMenu.getSelectedIndex() + 1;
        }
        
        public int getChapterId()
        {
            return chapterMenu.getSelectedIndex() + 1;
        }
        
        public void setVersionId(int versionId)
        {
            versionMenu.setSelectedIndex(versionId - 1);
        }
        
        public void setBookId(int bookId)
        {
            bookMenu.setSelectedIndex(bookId - 1);
        }
        
        public void setStartingChapterId(int chapterId)
        {
            chapterMenu.setSelectedIndex(chapterId - 1);
        }
        
        public void setEndingChapterId(int chapterId)
        {
            currentEndingChapter = chapterId;
        }
        
        public void setChapterId(int starting, int ending)
        {
            setStartingChapterId(starting);
            setEndingChapterId(ending);
        }
        
        public void setChapterId(int chapterId)
        {
            setChapterId(chapterId, chapterId);
        }
        
        public void setPage()
        {
            try
            {
                Statement s = models.DatabaseConnector.request().createStatement();
                
                int bookId = getBookId();
                int chapterId = getChapterId();
                
                textPane.getHighlighter().removeAllHighlights();
                
                javafx.util.Pair<Integer, Integer> bookAndChapter = models.DatabaseQueries.getNextChapter(s, getVersionId(), bookId, currentEndingChapter);
                nextBookId = bookAndChapter.getKey();
                nextChapter = bookAndChapter.getValue();
                nextPageButton.setToolTipText("Go to " + getAddressFromPage(nextBookId, nextChapter));
                
                bookAndChapter = models.DatabaseQueries.getPreviousChapter(s, getVersionId(), bookId, chapterId);
                prevBookId = bookAndChapter.getKey();
                prevChapter = bookAndChapter.getValue();
                prevPageButton.setToolTipText("Go to " + getAddressFromPage(prevBookId, prevChapter));
                
                textPane.setText(models.DatabaseQueries.getPage(s, getVersionId(), bookId, chapterId, currentEndingChapter));
                textPane.setCaretPosition(0);
                changeCurrentAddress(getAddressFromPage(bookId, chapterId, currentEndingChapter));
            }
            catch (SQLException | ClassNotFoundException ex)
            {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public String getAddressFromPage(int bookId, int chapter)
        {
            return getAddressFromPage(bookId, chapter, chapter);
        }
        
        public String getAddressFromPage(int bookId, int start, int end)
        {
            try
            {
                Statement s = models.DatabaseConnector.request().createStatement();
                ResultSet r = s.executeQuery("SELECT FirstTitle FROM Books WHERE ID = " + bookId + ";");
                r.next();
                return String.format("%s %s", r.getString("FirstTitle"), start == end ? start : String.format("%d-%d", start, end));
            }
            catch (SQLException | ClassNotFoundException ex)
            {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return prefs.defaultStartingAddress;
        }
        
        public void goToNextChapter()
        {
            if (currentContentType == ContentTypes.WHOLE_CHAPTER)
            {
                setBookId(nextBookId);
                setChapterId(nextChapter);
                setPage();
            }
        }
        
        public void goToPreviousChapter()
        {
            if (currentContentType == ContentTypes.WHOLE_CHAPTER)
            {
                setBookId(prevBookId);
                setChapterId(prevChapter);
                setPage();
            }
        }
    }
    
    public interface ChangeHistoryPositionListener
    {
        public abstract void enable(boolean enableCursorMovement);
    }
    
    public class SearchDataStruct
    {
        final static int HISTORY_MAX = 500;
        
        String phrase = "";
        Comparable[] instances = null;
        java.util.ArrayList highlights = new java.util.ArrayList<>();
        
        int[] versions = new int[HISTORY_MAX];
        String[] history = new String[HISTORY_MAX];
        private int pos = 0;
        private int max = 0;
        
        public java.util.ArrayList<ChangeHistoryPositionListener>
                decrementListeners = new java.util.ArrayList<>();
        
        public java.util.ArrayList<ChangeHistoryPositionListener>
                incrementListeners = new java.util.ArrayList<>();
        
        public void addMoveBackwardListener(ChangeHistoryPositionListener l)
        {
            decrementListeners.add(l);
        }
        
        public void addMoveForwardListener(ChangeHistoryPositionListener l)
        {
            incrementListeners.add(l);
        }
        
        private void alertHistoryPositionListeners()
        {
            decrementListeners.forEach(l -> l.enable(pos < max));
            incrementListeners.forEach(l -> l.enable(pos > 0));
        }
        
        private int moveCursorBackward()
        {
            pos = pos - 1;
            alertHistoryPositionListeners();
            return pos;
        }
        
        private int moveCursorForward()
        {
            pos = pos + 1;
            alertHistoryPositionListeners();
            return pos;
        }
        
        public SearchDataStruct(ContentDataStruct c)
        {
            history[pos] = c.currentAddress;
            versions[pos] = prefs.defaultStartingVersionId;
        }
        
        public int getHistoryPosition()
        {
            return pos;
        }
        
        public int getHistoryPositionMax()
        {
            return max;
        }
        
        public String getHistoryItem(int position)
        {
            return history[position];
        }
        
        public void dropInstances()
        {
            instances = null;
        }
        
        public boolean matchesCurrentHistoryItem(String searchPhrase)
        {
            return history[pos].equals(searchPhrase);
        }
        
        public void updateHistory(String searchPhrase)
        {
            // The input is expected to never be null.
            if (searchPhrase.length() > 0 && !search.matchesCurrentHistoryItem(searchPhrase))
                search.newHistoryItem(searchPhrase);
        }
        
        public void setPhrase(String searchPhrase)
        {
            phrase = searchPhrase;
        }
        
        public void goBack()
        {
            if (pos > 0)
            {
                String temp = searchBar.getText();
                
                if (!matchesCurrentHistoryItem(temp))
                    newHistoryItem(temp);
                
                versions[pos] = content.getVersionId();
                moveCursorBackward();
            }
        }
        
        public void goForward()
        {
            if (pos < max)
                moveCursorForward();
        }
        
        public void goToHistoryItem(int position)
        {
            if (position >= 0 && position <= max)
            {
                pos = position;
                alertHistoryPositionListeners();
            }
        }
        
        private void record(String searchItem)
        {
            history[pos] = searchItem;
            versions[pos] = content.getVersionId();
        }
        
        public void newHistoryItem(String currentSearchItem)
        {
            if (pos < HISTORY_MAX - 1)
            {
                max = moveCursorForward();
                record(currentSearchItem);
            }
        }
        
        public void updateContent()
        {
            searchBar.setText(history[pos]);
            content.setVersionId(versions[pos]);
            changeSearch(history[pos]);
        }
        
        public void phraseSearch()
        {
            phraseSearch(phrase);
        }
        
        public void phraseSearch(String searchPhrase)
        {
            phrase = searchPhrase;
            instances = models.Algorithms.search(phrase, textPane.getText(), prefs.caseSensitive);
            
            if (instances.length > 0)
            {
                textPane.setCaretPosition((int) instances[0]);
                highlights.add(highlightSelectedResult((int) instances[0], phrase.length()));
            }
            
            for (int i = 0; i < instances.length; ++i)
            {
                highlights.add(highlightResult((int) instances[i], phrase.length(), prefs.highlight));
            }
        }
        
        public void moveCaretToNextSearchResult()
        {
            if (instances != null && instances.length > 0)
            {
                int nextIndex;
                
                if (textPane.getCaretPosition() >= (int) instances[instances.length - 1])
                {
                    nextIndex = 0;
                }
                else
                {
                    nextIndex = (int) models.Algorithms.searchRightLeaning(instances, textPane.getCaretPosition());
                    nextIndex = nextIndex + 1;
                }
                
                textPane.setCaretPosition((int) instances[nextIndex]);
            }
        }
        
        public void moveCaretToPreviousSearchResult()
        {
            if (instances != null && instances.length > 0)
            {
                int nextIndex;
                
                if (textPane.getCaretPosition() <= (int) instances[0])
                {
                    nextIndex = instances.length - 1;
                }
                else
                {
                    nextIndex = (int) models.Algorithms.searchLeftLeaning(instances, textPane.getCaretPosition());
                    nextIndex = nextIndex - 1;
                }
                
                textPane.setCaretPosition((int) instances[nextIndex]);
            }
        }
    }
    
    private static PreferenceStruct prefs = new PreferenceStruct();
    private ContentDataStruct content = new ContentDataStruct();
    private SearchDataStruct search = new SearchDataStruct(content);
    
    public SearchDataStruct searchData()
    {
        return search;
    }
    
    public ContentDataStruct contentData()
    {
        return content;
    }
    
    public void goBack()
    {
        searchBar.requestFocus();
        search.goBack();
        search.updateContent();
    }
    
    public void goForward()
    {
        searchBar.requestFocus();
        search.goForward();
        search.updateContent();
    }
    
    public void goTo(int position)
    {
        searchBar.requestFocus();
        search.goToHistoryItem(position);
        search.updateContent();
    }
    
    public interface CurrentAddressListener
    {
        public abstract void respond(String currentAddress);
    }
    
    private final java.util.ArrayList<CurrentAddressListener> currentAddressListeners = new java.util.ArrayList<>();
    
    public void addCurrentAddressListener(CurrentAddressListener l)
    {
        currentAddressListeners.add(l);
    }
    
    private void changeCurrentAddress(String newAddress)
    {
        content.setCurrentAddress(newAddress);
        currentAddressListeners.forEach(l -> l.respond(newAddress));
    }
    
    public String getCurrentAddress()
    {
        return content.currentAddress;
    }
    
    private void constructVersionsDropdown()
    {
        try
        {
            Statement s = models.DatabaseConnector.request().createStatement();
            ResultSet r = s.executeQuery("SELECT Code FROM Versions ORDER BY ID;");
            
            while (!r.isClosed() && r.next())
                versionMenu.addItem(r.getString("Code").toUpperCase());
            
            versionMenu.addItemListener(e -> changeSearch(content.currentAddress));
            versionMenu.setSelectedIndex(prefs.defaultStartingVersionId - 1);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void constructBooksDropdown()
    {
        try
        {
            Statement s = models.DatabaseConnector.request().createStatement();
            ResultSet r = s.executeQuery("SELECT FirstTitle FROM Books ORDER BY ID;");
            
            while (!r.isClosed() && r.next())
                bookMenu.addItem(r.getString("FirstTitle"));
            
            bookMenu.addItemListener(e -> setChaptersDropdown());
            bookMenu.setSelectedIndex(prefs.defaultStartingBookId - 1);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setChaptersDropdown()
    {
        chapterMenu.removeAllItems(); // resets the chapter menu to be called again
        
        try
        {
            Statement s = models.DatabaseConnector.request().createStatement();
            ResultSet r = s.executeQuery("SELECT NumberChapters FROM Books WHERE ID = " + content.getBookId() + ';');
			
            if (!r.isClosed() && r.next())
                for (int i = 1; i <= r.getInt("NumberChapters"); ++i)
                    chapterMenu.addItem(String.format("%d", i));
            
            chapterMenu.setSelectedIndex(prefs.defaultStartingChapterId - 1);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void constructChaptersDropdown()
    {
        setChaptersDropdown();
        chapterMenu.addItemListener(e -> {
            content.setEndingChapterId(content.getChapterId());
            content.setPage();
        });
    }
    
    /**
     * Creates new form Reader
     */
    public Reader()
    {
        initComponents();

        nextPageButton.setFocusable(false);
        prevPageButton.setFocusable(false);

        textPane.setEditable(false);
        textPane.addCaretListener
        (
            (CaretEvent ce) ->
            {
                if (search.instances != null)
                {
                    javax.swing.JTextPane source = (javax.swing.JTextPane) ce.getSource();

                    search.highlights.forEach(source.getHighlighter()::removeHighlight);
                    search.highlights.clear();

                    int pos = source.getCaretPosition();

                    for (Object instance : search.instances)
                    {
                        if (pos >= (int) instance && pos < (int) instance + search.phrase.length())
                            search.highlights.add(highlightSelectedResult((int) instance, search.phrase.length()));
                        else
                            search.highlights.add(highlightResult((int) instance, search.phrase.length(), prefs.highlight));
                    }
                }
            }
        );
        
        textPane.addFocusListener
        (
            new java.awt.event.FocusListener()
            {
                @Override
                public void focusGained(FocusEvent fe)
                {
                    ((javax.swing.JTextPane) fe.getSource()).getCaret().setVisible(true);
                }
                
                @Override
                public void focusLost(FocusEvent fe)
                {
                    ((javax.swing.JTextPane) fe.getSource()).getCaret().setVisible(false);
                }
            }
        );
        
        searchBar.setText(IDLE_SEARCH_BAR_MESSAGE);
        searchBar.setToolTipText("Enter an Address or Search Words (Ctl + F)");
        searchBar.addFocusListener
        (
            new java.awt.event.FocusListener()
            {
                @Override
                public void focusGained(FocusEvent fe)
                {
                    javax.swing.JTextField source = (javax.swing.JTextField) fe.getSource();
                    source.setForeground(Color.BLACK);
                    
                    if (!content.typedLast)
                    {
                        source.setText("");
                    }
                    else
                    {
                        source.setSelectionStart(0);
                        source.setSelectionEnd(source.getText().length());
                    }
                }
                
                @Override
                public void focusLost(FocusEvent fe)
                {
                    javax.swing.JTextField source = (javax.swing.JTextField) fe.getSource();
                    source.setForeground(new Color(102, 102, 102));
                    
                    if (source.getText().length() == 0)
                    {
                        source.setText(IDLE_SEARCH_BAR_MESSAGE);
                        content.typedLast = false;
                    }
                }
            }
        );
        
        searchBar.addKeyListener
        (
            new java.awt.event.KeyListener()
            {
                @Override
                public void keyTyped(KeyEvent ke)
                {
                    content.typedLast = true;
                }
                
                @Override
                public void keyPressed(KeyEvent ke) {}
                
                @Override
                public void keyReleased(KeyEvent ke) {}
            }
        );
        
        textPane.setFont(new java.awt.Font("Helvetica", 0, 14));
        nextPageButton.addKeyListener
        (
            new java.awt.event.KeyListener()
            {
                @Override
                public void keyTyped(KeyEvent ke) {}
                
                @Override
                public void keyPressed(KeyEvent ke)
                {
                    switch (ke.getKeyCode())
                    {
                        case KeyEvent.VK_ENTER : content.goToNextChapter();
                        break;
                        default : break;
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent ke) {}
            }
        );
        
        nextPageButton.addActionListener(e -> content.goToNextChapter());
        prevPageButton.addKeyListener
        (
            new java.awt.event.KeyListener()
            {
                @Override
                public void keyTyped(KeyEvent ke) {}
                
                @Override
                public void keyPressed(KeyEvent ke)
                {
                    switch (ke.getKeyCode())
                    {
                        case KeyEvent.VK_ENTER : content.goToPreviousChapter();
                        break;
                        default : break;
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent ke) {}
            }
        );
        
        backwardHistoryButton.addActionListener(e -> goBack());
        forwardHistoryButton.addActionListener(e -> goForward());
        backwardHistoryButton.setToolTipText("Undo Last Search (Alt + Left)");
        forwardHistoryButton.setToolTipText("Redo Search (Alt + Rght)");
        backwardHistoryButton.setEnabled(false);
        forwardHistoryButton.setEnabled(false);
        search.addMoveBackwardListener(moveable -> forwardHistoryButton.setEnabled(moveable));
        search.addMoveForwardListener(moveable -> backwardHistoryButton.setEnabled(moveable));
        
        javax.swing.JPopupMenu historyMenu = new javax.swing.JPopupMenu();
        
        backwardHistoryButton.addMouseListener(new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    historyMenu.removeAll();
                    
                    for (int i = 0; i < search.getHistoryPosition(); ++i)
                    {
                        javax.swing.JMenuItem temp = historyMenu.add(search.getHistoryItem(i));
                        
                        temp.addActionListener
                        (
                            f ->
                            {
                                goTo(historyMenu.getComponentIndex((javax.swing.JMenuItem) f.getSource()));
                                historyMenu.setVisible(false);
                            }
                        );
                        
                        temp.addMouseListener(new java.awt.event.MouseListener()
                        {
                            @Override public void mouseClicked(MouseEvent e) {}
                            @Override public void mousePressed(MouseEvent e) {}
                            @Override public void mouseReleased(MouseEvent e) {}
                            
                            @Override
                            public void mouseEntered(MouseEvent e)
                            {
                                ((javax.swing.JMenuItem) e.getSource()).setArmed(true);
                            }
                            
                            @Override
                            public void mouseExited(MouseEvent e)
                            {
                                ((javax.swing.JMenuItem) e.getSource()).setArmed(false);
                            }
                        });
                    }
                    
                    historyMenu.show(historyMenu.getParent(), e.getXOnScreen(), e.getYOnScreen());
                    historyMenu.requestFocus();
                }
            }
            
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
        
        forwardHistoryButton.addMouseListener(new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    historyMenu.removeAll();
                    
                    for (int i = search.getHistoryPosition() + 1; i <= search.getHistoryPositionMax(); ++i)
                    {
                        javax.swing.JMenuItem temp = historyMenu.add(search.getHistoryItem(i));
                        
                        temp.addActionListener
                        (
                            f ->
                            {
                                goTo(search.getHistoryPosition() + historyMenu.getComponentIndex((javax.swing.JMenuItem) f.getSource()) + 1);
                                historyMenu.setVisible(false);
                            }
                        );
                        
                        temp.addMouseListener(new java.awt.event.MouseListener()
                        {
                            @Override public void mouseClicked(MouseEvent e) {}
                            @Override public void mousePressed(MouseEvent e) {}
                            @Override public void mouseReleased(MouseEvent e) {}
                            
                            @Override
                            public void mouseEntered(MouseEvent e)
                            {
                                ((javax.swing.JMenuItem) e.getSource()).setArmed(true);
                            }
                            
                            @Override
                            public void mouseExited(MouseEvent e)
                            {
                                ((javax.swing.JMenuItem) e.getSource()).setArmed(false);
                            }
                        });
                    }
                    
                    historyMenu.show(historyMenu.getParent(), e.getXOnScreen(), e.getYOnScreen());
                    historyMenu.requestFocus();
                }
            }
            
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
        
        constructVersionsDropdown();
        constructBooksDropdown();
        constructChaptersDropdown();
        prevPageButton.addActionListener(e -> content.goToPreviousChapter());
        changeCurrentAddress(prefs.defaultStartingAddress);
        content.setPage();
    }
    
    private Object highlightResult(int pos, int len, javax.swing.text.Highlighter.HighlightPainter h)
    {
        Object temp = null;
        
        try
        {
            temp = textPane.getHighlighter().addHighlight(pos, pos + len, h);
        }
        catch (javax.swing.text.BadLocationException ex)
        {
            java.util.logging.Logger.getLogger(Reader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        return temp;
    }
    
    private Object highlightSelectedResult(int pos, int len)
    {
        return highlightResult(pos, len, prefs.selector);
    }
    
    public void updateSearchResults()
    {
        search.dropInstances();
        search.updateHistory(searchBar.getText());
        performSearch(searchBar.getText());
    }
    
    private void changeSearch(String searchPhrase)
    {
        search.dropInstances();
        performSearch(searchPhrase);
    }
    
    private void performSearch(String searchPhrase)
    {
        models.AddressParser parser = new models.AddressParser(searchPhrase);
        models.AddressParser.AddressList list = parser.compileToAddressList();
        
        if (!list.isEmpty())
        {
            try
            {
                Statement s = models.DatabaseConnector.request().createStatement();
                
                if (list.representsWholeChapter())
                {
                    content.currentContentType = ContentTypes.WHOLE_CHAPTER;
                    content.setBookId(models.DatabaseQueries.getBookIdFromName(s, list.get(0).getTitle()));
                    content.setChapterId(list.get(0).getChapter());
                    content.setPage();
                }
                else if (list.representsChapterRange())
                {
                    content.currentContentType = ContentTypes.WHOLE_CHAPTER;
                    content.setBookId(models.DatabaseQueries.getBookIdFromName(s, list.get(0).getTitle()));
                    int temp = models.DatabaseQueries.getNumberOfChapters(s, content.getBookId());
                    AddressParser.Range rng = list.get(0).popRanges();
                    content.currentEndingChapter = temp < rng.getEnd() ? temp : rng.getEnd();
                    content.setStartingChapterId(rng.getStart());
                    content.setPage();
                }
                else
                {
                    content.currentContentType = ContentTypes.SEARCH_RESULT;
                    String newText = models.DatabaseQueries.query(s, content.getVersionId(), list);
                    textPane.setText(newText);
                    textPane.moveCaretPosition(0);
                    changeCurrentAddress(searchPhrase);
                    nextPageButton.setToolTipText("");
                    prevPageButton.setToolTipText("");
                }
            }
            catch (SQLException | ClassNotFoundException ex)
            {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            search.setPhrase(searchPhrase);
        }
        
        search.phraseSearch();
    }
    
    public void scrollUp()
    {
        javax.swing.JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getValue() - textPane.getHeight());
    }
    
    public void scrollDown()
    {
        javax.swing.JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getValue() + textPane.getHeight());
    }

    public void set_text(String newText)
    {
        textPane.setText(newText);
    }
    
    public javax.swing.JTextField getSearchBar()
    {
        return searchBar;
    }
    
    public javax.swing.JTextPane getTextPane()
    {
        return textPane;
    }
    
    public javax.swing.JComboBox<String> getVersionMenu()
    {
        return versionMenu;
    }
    
    public static final int NUMBER_COMPONENTS = 8;
    
    public java.awt.Component[] getThings()
    {
        java.awt.Component[] temp = new java.awt.Component[NUMBER_COMPONENTS];
        
        temp[0] = nextPageButton;
        temp[1] = prevPageButton;
        temp[2] = versionMenu;
        temp[3] = scrollPane;
        temp[4] = textPane;
        temp[5] = searchBar;
        temp[6] = backwardHistoryButton;
        temp[7] = forwardHistoryButton;
        
        return temp;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        prevPageButton = new javax.swing.JButton();
        nextPageButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        historyPanel = new javax.swing.JPanel();
        backwardHistoryButton = new javax.swing.JButton();
        forwardHistoryButton = new javax.swing.JButton();
        chapterMenu = new javax.swing.JComboBox<>();
        bookMenu = new javax.swing.JComboBox<>();
        versionMenu = new javax.swing.JComboBox<>();
        searchBar = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(100, 200));

        menuPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        prevPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/if_arrow-left-01_186410.png"))); // NOI18N
        prevPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevPageButtonActionPerformed(evt);
            }
        });

        nextPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/if_arrow-right-01_186409.png"))); // NOI18N

        scrollPane.setViewportView(textPane);

        backwardHistoryButton.setText("Go Back");
        backwardHistoryButton.setToolTipText("Undo (Cnt + Z)");
        backwardHistoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backwardHistoryButtonActionPerformed(evt);
            }
        });

        forwardHistoryButton.setText("Go Forward");
        forwardHistoryButton.setToolTipText("Redo (Cnt + Y)");

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historyPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backwardHistoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(forwardHistoryButton)
                .addContainerGap())
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forwardHistoryButton)
                    .addComponent(backwardHistoryButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        historyPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {backwardHistoryButton, forwardHistoryButton});

        chapterMenu.setToolTipText("Select the Book's Chapter (Genesis 1, 2, 34...)");

        bookMenu.setToolTipText("Select Book (Genesis, Exodus...)");

        versionMenu.setToolTipText("Select Version (NIV, ESV...)");

        searchBar.setForeground(new java.awt.Color(102, 102, 102));
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchBarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(historyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prevPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPane)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(versionMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(bookMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chapterMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nextPageButton, prevPageButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chapterMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bookMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(versionMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(historyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextPageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .addComponent(prevPageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollPane))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bookMenu, chapterMenu, searchBar, versionMenu});

    }// </editor-fold>//GEN-END:initComponents

    public void scrollToTop()
    {
        scrollPane.getVerticalScrollBar().setValue(0);
    }
    
    public void scrollToBottom()
    {
        javax.swing.JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
    
    public void scrollToMiddle()
    {
        scrollToBottom();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() / 2);
    }

    private void searchBarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyPressed
        if (!evt.isControlDown())
        {
            switch(evt.getKeyCode())
            {
                case KeyEvent.VK_ENTER : updateSearchResults();
                break;
            }
        }
    }//GEN-LAST:event_searchBarKeyPressed

    private void prevPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevPageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prevPageButtonActionPerformed

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed

    private void backwardHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backwardHistoryButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backwardHistoryButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backwardHistoryButton;
    private javax.swing.JComboBox<String> bookMenu;
    private javax.swing.JComboBox<String> chapterMenu;
    private javax.swing.JButton forwardHistoryButton;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JButton prevPageButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextField searchBar;
    private javax.swing.JTextPane textPane;
    private javax.swing.JComboBox<String> versionMenu;
    // End of variables declaration//GEN-END:variables
}
