# JFXTableView
Advanced JavaFx TableView with filtering and total functions.

## Description
The implementation of the filter is easy. You wrap your `TableView` with the `JFXTableView`
and add the columns which wrap `JFXTableColumn`.

There are the following column implementations:

* `JFXBooleanTableColumn`
* `JFXDoubleTableColumn`
* `JFXFloatTableColumn`
* `JFXIntegerTableColumn`
* `JFXLocalDateTableColumn`
* `JFXLocalDateTimeTableColumn`
* `JFXLocalTimeTableColumn`
* `JFXLongTableColumn`
* `JFXStringTableColumn`
* `JFXBigDecimalTableColumn`

You can create your own column implementation by inheriting from the abstract `JFXTableColumn` class.

You can turn off filtering on a table by passing `false` to the `setAllowFiltering` method. Example:

```
jfxTableView.setAllowFiltering(false);
```

The following types of filtering are supported (The filter types available depend on the choice of the `JFXTableColumn` implementation):

* `Equals`
* `Not equals`
* `Greather or equals than`
* `Greather than`
* `Less or equals than`
* `Less than`
* `Start with`
* `End with`
* `Contains`
* `Not contains`
* `Regular expression`
* `Custom filtering`

The following types of column totals are supported (The totals types available depend on the choice of the `JFXTableColumn` implementation):

* `Sum`
* `Minimum`
* `Maximum`
* `Average`

## Features
JFXTableView supports the following features:

* Filtering rows by columns (multiple filter option with `Custom filtering` by column)
* Totals by column
* Count rows
* Copy cell value from context menu
* Export to file
* Localization and internationalization

## Get Started
Create table `JFXTableView` and columns `JFXTableColumn`:

```
//custom model for JFXTableView
class Model {
    private SimpleBooleanProperty boolValue = new SimpleBooleanProperty();
    private SimpleDoubleProperty doubleValue = new SimpleDoubleProperty();
    private SimpleFloatProperty floatValue= new SimpleFloatProperty();
    private SimpleIntegerProperty integerValue = new SimpleIntegerProperty();
    private SimpleLongProperty longValue = new SimpleLongProperty();
    private SimpleStringProperty stringValue = new SimpleStringProperty();
    private SimpleObjectProperty<LocalDate> localDateValue = new SimpleObjectProperty<>();
    private SimpleObjectProperty<LocalDateTime> localDateTimeValue = new SimpleObjectProperty<>();
    private SimpleObjectProperty<LocalTime> localTimeValue = new SimpleObjectProperty<>();
    
    //getters and setters
}

//initialize JFXTableView
//background is StackPane (usually this is Node on which the controls are located in the scene), which is necessary for the darkening effect when opening dialog boxes
JFXTableView<Model> jfxTableView = new JFXTableView<>(background);

//initialize JFXTableColumns
JFXTableColumn<Model, Boolean> boolColumn = new JFXBooleanTableColumn<>("Bool value");
boolColumn.setCellValueFactory(new PropertyValueFactory<>("boolValue"));

JFXTableColumn<Model, Double> doubleColumn = new JFXDoubleTableColumn<>("Double value");
doubleColumn.setCellValueFactory(new PropertyValueFactory<>("doubleValue"));

JFXTableColumn<Model, Float> floatColumn = new JFXFloatTableColumn<>("Float value");
floatColumn.setCellValueFactory(new PropertyValueFactory<>("floatValue"));

JFXTableColumn<Model, Integer> integerColumn = new JFXIntegerTableColumn<>("Integer value");
integerColumn.setCellValueFactory(new PropertyValueFactory<>("integerValue"));

JFXTableColumn<Model, Long> longColumn = new JFXLongTableColumn<>("Long value");
longColumn.setCellValueFactory(new PropertyValueFactory<>("longValue"));

JFXTableColumn<Model, LocalDate> localDateColumn = new JFXLocalDateTableColumn<>("LocalDate value");
localDateColumn.setCellValueFactory(new PropertyValueFactory<>("localDateValue"));

JFXTableColumn<Model, LocalDateTime> localDateTimeColumn = new JFXLocalDateTimeTableColumn<>("LocalDateTime value");
localDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("localDateTimeValue"));

JFXTableColumn<Model, LocalTime> localTimeColumn = new JFXLocalTimeTableColumn<>("LocalTime value");
localTimeColumn.setCellValueFactory(new PropertyValueFactory<>("localTimeValue"));

JFXTableColumn<Model, String> stringColumn = new JFXStringTableColumn<>("String value");
stringColumn.setCellValueFactory(new PropertyValueFactory<>("stringValue"));

//add columns to JFXTableView
jfxTableView.getColumns().addAll(boolColumn, doubleColumn, floatColumn, integerColumn, longColumn,
        localDateColumn, localDateTimeColumn, localTimeColumn, stringColumn);

//initialize data
ObservableList<Model> data = FXCollections.observableArrayList();
Model row1 = new Model();
row1.setBoolValue(true);
row1.setDoubleValue(1);
row1.setFloatValue(2);
row1.setIntegerValue(3);
row1.setLocalDateTimeValue(LocalDateTime.now());
row1.setLocalDateValue(LocalDate.now());
row1.setLocalTimeValue(LocalTime.now());
row1.setLongValue(4);
row1.setStringValue("string");
data.add(row1);

//set data to JFXTableView
jfxTableView.setData(data);
```

Result:
![Result](https://i.imgur.com/WVwTks5.jpg)

`background` is StackPane (usually this is Node on which the controls are located in the scene), which is necessary for the darkening effect when opening dialog boxes. For example with shadow:

![With Shadow](https://i.imgur.com/ztbHP5i.jpg)

Without shadow:

![Without Shadow](https://i.imgur.com/a2lTXLb.jpg)

## Export to file
First column have export to file button. By default export data to CSV file, but you can write your own implementation and pass a reference to the method in `jfxTableView.setExportDataAction` (To get data from the table use the `jfxTableView.getDataForExport()` method).

## Localization and internationalization
For localization and internationalization use Resource bundle `message`. Available by default `en` and `ru_RU`.

## Credits
* [FontAwesomeFX](https://bitbucket.org/Jerady/fontawesomefx)
* [JFoenix](https://github.com/jfoenixadmin/JFoenix)
* [RxJava](https://github.com/ReactiveX/RxJava)