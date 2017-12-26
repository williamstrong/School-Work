package panels.Reader;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;

public class PreferenceStruct
{
    public int defaultStartingVersionId = 1;
    public int defaultStartingBookId = 1;
    public int defaultStartingChapterId = 1;
    public String defaultStartingAddress = "Genesis 1";
    boolean caseSensitive = false;
    public DefaultHighlighter.DefaultHighlightPainter highlight = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 0));
    public DefaultHighlighter.DefaultHighlightPainter selector = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 180, 0));
}
