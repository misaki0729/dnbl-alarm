package io.github.misaki0729.dnbl.event;

public class SelectDialogEvent {
    private int selectItem;

    public SelectDialogEvent(int selectItem) {
        this.selectItem = selectItem;
    }

    private SelectDialogEvent() {}

    public int getSelectItem() {
        return selectItem;
    }
}
