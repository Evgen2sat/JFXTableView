package em.libs.jfxtableview.jfxDialogBox;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import em.libs.jfxtableview.Constants;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.font.FontAwesome;
import em.libs.jfxtableview.jfxDialogBox.events.ClosingActionEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JFXDialogBox extends StackPane {

    private BooleanProperty onCloseProperty = new SimpleBooleanProperty(false);

    //**********************************************
    //Контролы
    //**********************************************
    private JFXDialog jfxDialog = new JFXDialog();
    private JFXDialogLayout jfxDialogContent = new JFXDialogLayout();
    private Label lblCaption;
    private Label lblClose;
    private Node body;
    private List<? extends Button> actions;
    private EventHandler<JFXDialogEvent> onDialogOpenedAction;

    //**********************************************
    //События
    //**********************************************
    private EventHandler<ClosingActionEvent> onClosingAction;

    private CompletableFuture<ClosingActionEvent> completableFuture;

    protected JFXDialogBox() {
        initControls();
    }

    public JFXDialogBox(Node body, List<? extends Button> actions) {
        this.body = body;
        this.actions = actions;
        initControls();
        onCloseProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.close(ClosingResult.OK);
            }
        });
    }

    protected void setActions(List<? extends Button> actions) {
        this.actions = actions;
    }

    private void initControls() {
        lblClose = new Label(Constants.CLOSE_ICON);
        lblClose.setFont(new FontAwesome(18).getFontSolid());
        lblClose.setPrefSize(25.0, 25.0);
        lblClose.setStyle("-fx-text-fill: -primary-color");
        lblClose.setTooltip(new Tooltip(Constants.CLOSE));
        lblClose.setAlignment(Pos.CENTER_RIGHT);
        lblClose.setOnMouseClicked(this::lblClose_onMouseClicked);
        lblClose.setCursor(Cursor.HAND);
    }

    private void lblClose_onMouseClicked(MouseEvent event) {

        this.close(ClosingResult.CANCEL);
    }

    protected void setBody(Node body) {
        this.body = body;
    }

    public void close() {
        this.close(ClosingResult.OK);
    }

    public void close(ClosingResult result) {

        ClosingActionEvent closingActionEvent = new ClosingActionEvent(result);

        if (onClosingAction != null) {
            onClosingAction.handle(closingActionEvent);
        }

        if (result.equals(ClosingResult.CANCEL)) {
            completableFuture.cancel(true);
        }

        if (!closingActionEvent.isCancel()) {
            if (!completableFuture.isCancelled()) {
                completableFuture.complete(closingActionEvent);
            }
            jfxDialog.close();
        }
    }

    public void setOnClosing(EventHandler<ClosingActionEvent> onClosingAction) {
        this.onClosingAction = onClosingAction;
    }

    public void show(StackPane container, int width, int height, String caption, boolean isOverlayClose) {

        if (actions == null) {
            throw new IllegalArgumentException("buttons is not set");
        }

        if (body == null) {
            throw new IllegalArgumentException("body is not set");
        }

        completableFuture = new CompletableFuture<>();

        BorderPane header = new BorderPane();

        int maxHeaderWidth = width;

        if (width > 0 && width > 80) {
            maxHeaderWidth = width - 80;
        }

        lblCaption = new Label(caption);
        lblCaption.setStyle("-fx-text-fill: -primary-color");
        lblCaption.setMaxWidth(maxHeaderWidth);
        lblCaption.setWrapText(true);
        lblCaption.setTextAlignment(TextAlignment.LEFT);
        header.setLeft(lblCaption);
        header.setRight(lblClose);

        jfxDialogContent.setHeading(header);
        jfxDialogContent.setBody(body);
        jfxDialogContent.setActions(actions);
        jfxDialogContent.setMinWidth(width);
        jfxDialogContent.setPrefWidth(width);
        jfxDialogContent.setPrefHeight(height);
        jfxDialogContent.requestFocus();
        jfxDialogContent.requestLayout();

        jfxDialog.requestFocus();
        jfxDialog.requestLayout();
        jfxDialog.setDialogContainer(container);
        jfxDialog.setContent(jfxDialogContent);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(isOverlayClose);
        jfxDialog.getDialogContainer().requestLayout();
        jfxDialog.getDialogContainer().requestFocus();
        if (body != null) {
            body.requestFocus();
            if (actions.size() > 0) {
                actions.get(0).requestFocus();
            }
        }

        if (onDialogOpenedAction != null) {
            jfxDialog.setOnDialogOpened(onDialogOpenedAction);
        }

        jfxDialog.show();
    }

    public void setOnDialogOpenedAction(EventHandler<JFXDialogEvent> onDialogOpenedAction) {
        this.onDialogOpenedAction = onDialogOpenedAction;
    }

    public void show(StackPane container, int width, int height, String caption) {
        show(container, width, height, caption, false);
    }

    public void setCaption(String caption) {
        lblCaption.setText(caption);
    }

    public BooleanProperty onCloseProperty() {
        return onCloseProperty;
    }

    public CompletableFuture<ClosingActionEvent> getCompletableFuture() {
        return completableFuture;
    }

    public StackPane getDialogContainer() {
        return jfxDialog.getDialogContainer();
    }
}
