package mindmapsoftware;

import java.util.List;
import java.util.UUID;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class WebViewTest extends Region {

    final WebView webview = new WebView();
    final WebEngine webEngine = webview.getEngine ();
    private double currHeight = 100;
    private Label label = new Label ();
    private String content = null;
    private String divId = UUID.randomUUID ().toString ();

    public WebViewTest (String content) 
    {
        this.webview.setPrefHeight(1);
        this.content = content;
        this.getStyleClass().add("qwebview");
        this.label.setVisible(false);
        this.label.fontProperty().addListener((pr, oldv, newv) -> {
            this.setContent(content);
        });

        this.label.textFillProperty().addListener((pr, oldv, newv) ->{
            this.setContent(content);
        });

        this.backgroundProperty().addListener((pr, oldv, newv) -> {
            this.setContent(content);
        });

        this.widthProperty().addListener((pr, oldv, newv) -> {
            this.webview.setPrefWidth(newv.doubleValue());
            this.adjustHeight();
        });

        this.webEngine.getLoadWorker ().stateProperty ().addListener ((pr, oldv, newv) ->
        {
            if (newv == State.SUCCEEDED)
            {
                adjustHeight ();
            }
        });

        this.webview.getChildrenUnmodifiable ().addListener ((ListChangeListener<Node>) ev ->
        {
            this.webview.lookupAll (".scroll-bar").stream ()
                .forEach (n -> n.setVisible (false));
        });

        webview.setOnScroll (ev ->
        {
            this.getParent ().fireEvent (ev);
            ev.consume ();
        });

        this.setContent (content);
        this.getChildren ().add (this.webview);
        this.getChildren ().add (this.label);
    }

    public void setContent(String content)
    {
        StringBuilder b = new StringBuilder ();
        b.append ("<html><head>");
        b.append ("<style>");
        b.append ("html, body{padding: 0px; margin: 0px; offset-x: hidden; offset-y: hidden;}");
        b.append (String.format ("body{background-color: %1$s;}", this.getBackgroundAsCssString (this.getBackground ())));
        b.append (String.format ("body{font-size: %1$spx; font-family: %2$s; color: %3$s;}",
                                 this.label.getFont ().getSize () + "",
                                 this.label.getFont ().getFamily (),
                                 this.getPaintAsCssString (this.label.getTextFill ())));
        b.append ("</style>");

        b.append ("<script>");
        b.append ("function noScroll(){window.scrollTo(0,0);}window.addEventListener('scroll', noScroll);");
        b.append ("</script>");

        b.append ("</head");

        b.append (String.format ("<body><div id='%1$s'>", divId));
        b.append (content);

        b.append ("</div></body></html>");

        webEngine.loadContent (b.toString ());
    }

    @Override
    protected double computeMinHeight (double width)
    {
        return this.currHeight;
    }

    @Override
    protected double computePrefHeight (double width)
    {
        return this.currHeight;
    }

    @Override
    protected void layoutChildren()
    {
        this.layoutInArea (this.webview,0,0,this.getWidth (), this.getHeight (),0, HPos.CENTER, VPos.CENTER);
        this.layoutInArea (this.label, 0,0,0,0,0,HPos.CENTER,VPos.CENTER);
    }

    private void adjustHeight ()
    {
        Platform.runLater (() ->
        {
            try
            {
                // The document can sometimes be null, usually when the change is the result of a parent width change.
                if (this.webEngine.getDocument () == null)
                {
                    return;
                }

                Object result = this.webEngine.executeScript (String.format ("document.getElementById('%1$s').offsetHeight",
                                                                             divId));

                if (result instanceof Integer)
                {
                    Integer i = (Integer) result;
                    double height = i.doubleValue ();

                    // This check ensures that we don't get into a weird loop where the view is constantly resizing.
                    if (height != this.currHeight)
                    {
                        this.currHeight = height;
                        this.webview.setPrefHeight (height);
                        this.webview.requestLayout ();
                    }
                }

            } catch (Exception e) {
                // You should do something about this!
                e.printStackTrace ();
            }
        });
    }

    private String getPaintAsCssString (Paint p)
    {
        if (p instanceof Color)
        {
            Color c = (Color) p;
            return String.format( "#%02X%02X%02X",
                        (int)( c.getRed () * 255 ),
                        (int)( c.getGreen () * 255 ),
                        (int)( c.getBlue () * 255 ) );

        }
        return "#000000";
    }

    private String getBackgroundAsCssString (Background bg)
    {
        if (bg != null)
        {
            List<BackgroundFill> fills = bg.getFills ();
            if (fills != null)
            {
                return this.getPaintAsCssString (fills.get (0).getFill ());
            }
        }
        return "#ffffff";
    }
}
}
