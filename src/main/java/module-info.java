module em.libs.jfxtableview {
    requires javafx.controls;
    requires com.jfoenix;
    requires java.desktop;
    requires io.reactivex.rxjava2;

    exports em.libs.jfxtableview;
    exports em.libs.jfxtableview.cellFactories;
    exports em.libs.jfxtableview.columns;
}