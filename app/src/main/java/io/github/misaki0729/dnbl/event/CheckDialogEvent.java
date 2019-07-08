package io.github.misaki0729.dnbl.event;

public class CheckDialogEvent {
    private boolean[] checkedList;

    public CheckDialogEvent(boolean[] checkedList) {
        this.checkedList = checkedList;
    }

    private CheckDialogEvent() {}

    public boolean[] getCheckedList() {
        return checkedList;
    }
}
