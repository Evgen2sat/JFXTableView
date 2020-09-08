package em.libs.jfxtableview.validators;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс проверяющий наличие ошибок в валидаторах
 */
public class ExistErrorsChecker {
    private final List<ValidatorBase> validators = new ArrayList<>();
    private final SimpleBooleanProperty error = new SimpleBooleanProperty();
    private final SimpleObjectProperty<Node> srcControl = new SimpleObjectProperty<>();

    /**
     * Добавить валидаторы для проверки
     * @param validators валидаторы
     */
    public void addValidators(ValidatorBase... validators) {
        this.validators.addAll(Arrays.asList(validators));
    }

    /**
     * Удалить валидаторы из списка
     * @param validators валидаторы
     */
    public void removeValidators(ValidatorBase... validators) {
        this.validators.removeAll(Arrays.asList(validators));
    }

    /**
     * Проверить наличие ошибок в валидаторах
     * @return true - при наличии ошибок, false - при отсутствии ошибок
     */
    public boolean checkErrors() {
        for(ValidatorBase validator : validators) {
            if(validator.getHasErrors()) {
                error.set(true);
                srcControl.set(validator.getSrcControl());
                return true;
            }
        }

        error.set(false);
        srcControl.set(null);
        return false;
    }

    public SimpleBooleanProperty errorProperty() {
        return error;
    }

    /***
     * Получить элемент управления, валидатор которого сработал
     * @return Node
     */
    public Node getSrcControl() {
        return srcControl.get();
    }
}
