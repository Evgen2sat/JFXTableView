package em.libs.jfxtableview.jfxDialogBox.events;

import javafx.event.ActionEvent;
import em.libs.jfxtableview.enums.ClosingResult;

public class ClosingActionEvent extends ActionEvent {

    private boolean isCancel = false;
    private ClosingResult result;

    public ClosingActionEvent(ClosingResult result) {
        super();
        this.result = result;
    }

    public boolean isCancel(){
        return isCancel;
    }

    public void setCancel(boolean value){
        isCancel = value;
    }

    public ClosingResult getResult() {
        return result;
    }
}
